package com.uninew.net.JT905.manage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.uninew.car.db.paramSet.ParamSetLocalDataSource;
import com.uninew.car.db.paramSet.ParamSetLocalSource;
import com.uninew.car.db.paramSet.ParamSetting;
import com.uninew.net.JT905.bean.BaseBean;
import com.uninew.net.JT905.bean.T_HeartBeat;
import com.uninew.net.JT905.common.Define;
import com.uninew.net.JT905.protocol.IProtocolManage;
import com.uninew.net.JT905.protocol.ProtocolManage;
import com.uninew.net.JT905.tcp.TCPLinkErrorEnum;
import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.IpInfo;
import com.uninew.net.JT905.protocol.IProtocolCallBack;
import com.uninew.net.JT905.protocol.ProtocolPacket;
import com.uninew.net.JT905.tcp.TCPRunStateEnum;
import com.uninew.net.JT905.util.NetUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/8/26 0026.
 */

public class PlatformLinkManage implements IPlatformLinkManage, IProtocolCallBack {

    private static final String TAG = "PlatformLinkManage";
    private static final boolean D = true;

    private Context mContext;
    private IProtocolManage protocolManage;
    private IPlatformLinkCallBack platformLinkCallBack;
    private ConcurrentHashMap<Integer, IpInfo> ipMap;

    private int maxLinkTimes = Define.Default_MaxLinkTimes; // TCP最大重连次数
    private long linkTimeout = Define.Default_LinkTimeout;// TCP连接超时时间
    private int mHeartbeatTime = Define.Default_HeartBeatTime;
    private Object linkLock;
    private volatile boolean isNetWork;// 网络状态

    private static PlatformLinkManage instance;

    private PlatformLinkManage(Context mContext) {
        this.mContext = mContext;
        linkLock = new Object();
        protocolManage = ProtocolManage.getInstance(mContext);
        protocolManage.setProtocolCallBack(this);
        ipMap = new ConcurrentHashMap<>();
        isNetWork = NetUtils.isConnected(mContext);
        registerNetWork();
//        paramSetLocalSource.registerNotify(this);
        Log.d(TAG, "isNetWork=" + isNetWork);
    }

    public void setPlatformLinkCallBack(IPlatformLinkCallBack mPlatformLinkCallBack) {
        this.platformLinkCallBack = mPlatformLinkCallBack;
    }

    public synchronized static PlatformLinkManage getInstance(Context mContext) {
        if (instance == null) {
            instance = new PlatformLinkManage(mContext);
        }
        return instance;
    }

    //----------------------------------------消息上报------------------------------------------
    @Override
    public void onTCPRunState(int tcpId, TCPRunStateEnum vValue, TCPLinkErrorEnum error) {
        Log.d(TAG, "tcpId=" + tcpId + ",linkErrorEnum:" + error + ",TCPRunStateEnum:" + vValue.name());
        LinkManageThread thread = threadMap.get(tcpId);
        if (vValue == TCPRunStateEnum.OPEN_SUCCESS) {
            thread.setLinkState(LinkState.linkSuccess);
        } else if (vValue == TCPRunStateEnum.OPEN_FAILURE) {
            thread.setLinkState(LinkState.linkError);
        } else if (error != TCPLinkErrorEnum.ERROR_HAND_CLOSED) {
            if (vValue == TCPRunStateEnum.RUN_STOPED && thread.getLinkState() != LinkState.waitLink) {
                thread.setLinkState(LinkState.start);
            }
            platformLinkCallBack.onTCPLinkState(tcpId, thread.isMainIp, thread.ipInfo, PlatformLinkState.ERROR_OTHERS);
        } else if (vValue == TCPRunStateEnum.RUN_STOPED) {//手动结束
            threadMap.get(tcpId).setLinkState(LinkState.waitOrder);
            platformLinkCallBack.onTCPLinkState(tcpId, thread.isMainIp, thread.ipInfo, PlatformLinkState.ERROR_HAND_CLOSED);
        }
    }

    @Override
    public void onReceiveDatas(ProtocolPacket packet) {
        platformLinkCallBack.onReceiveDatas(packet);
    }

    @Override
    public void onGeneralResponse(int tcpId, int responseId, int responseSerialNumber, int result) {
        if (D)Log.d(TAG, "tcpId=" + tcpId + " ,rP_GeneralResponse=0x"
                + Integer.toHexString(responseId));
        if (responseId == BaseMsgID.TERMINAL_HEARBEAT) {
            //心跳应答
            if (threadMap.containsKey(tcpId)) {
                if (D) Log.d(TAG, "--------心跳应答--- tcpId=" + tcpId);
                threadMap.get(tcpId).setLinkState(LinkState.normal);
            }
        }
        platformLinkCallBack.onGeneralResponse(tcpId, responseId, responseSerialNumber, result);
    }

    //----------------------------------------消息下传------------------------------------------
    @Override
    public void setLinkParams(String deviceId, int tcpResponseTimeOut, int tcpResendTime, int smsResponseTimeOut, int smsResendTime) {
        protocolManage.setParams(deviceId, tcpResponseTimeOut, tcpResendTime, smsResponseTimeOut, smsResendTime);
    }

    @Override
    public void setTcpResponseTimeOut(int tcpResponseTimeOut) {
        protocolManage.setTcpResponseTimeOut(tcpResponseTimeOut);
    }

    @Override
    public void setTcpResendTime(int times) {
        protocolManage.setTcpResendTime(times);
    }

    @Override
    public void setTcpParams(int tcpId, String mainIp, int mainPort, String spareIp, int sparePort) {
        if (ipMap != null) {
            ipMap.put(tcpId, new IpInfo(tcpId, mainIp, mainPort, spareIp, sparePort));
        }
    }

    private Map<Integer, LinkManageThread> threadMap = new HashMap();
    private LinkManageThread currentThread;

    @Override
    public boolean createSocket(int tcpId) {
        if (D)
            Log.d(TAG, "createSocket,tcpId=" + tcpId + "ipMap.containsKey(tcpId)=" + ipMap.get(tcpId));
        if (ipMap.containsKey(tcpId)) {//参数已设置
            // 当前线程存在
            if (threadMap.containsKey(tcpId)) {
                currentThread = threadMap.get(tcpId);
            } else {
                currentThread = new LinkManageThread();
                currentThread.start();
                threadMap.put(tcpId, currentThread);
            }
            currentThread.createLink(ipMap.get(tcpId));
        }
        return true;
    }

    @Override
    public boolean closeSocket(int tcpId) {
        Log.e(TAG, "closeSocket,tcpId=" + tcpId);
        protocolManage.closeSocket(tcpId);
        if (threadMap.containsKey(tcpId)) {
            threadMap.get(tcpId).closeLink();
        }
        return true;
    }

    @Override
    public void sendMsg(BaseBean mBaseBean) {
        if (mBaseBean.getTransportId() == Define.Transport_TCP) {
            protocolManage.sendMsgByTcp(mBaseBean);
        } else if (mBaseBean.getTransportId() == Define.Transport_SMS) {
            protocolManage.sendMsgBySms(mBaseBean.getSmsPhoneNumber(), mBaseBean);
        }
    }

    @Override
    public void setHeartbeatTime(int time) {
        currentThread.reStartHeartbeat(time);
    }


    private class LinkManageThread extends Thread {

        private volatile boolean isFinish = true;
        private boolean isRunning = true;
        private long currentTime = 0;
        private int linkTimes = 0;
        private IpInfo ipInfo;
        private boolean isMainIp;
        private boolean handleClose;
        private boolean isLinked;

        public void createLink(IpInfo ipInfo) {
            this.ipInfo = ipInfo;
            isRunning = true;
            handleClose = false;
            //避免多次开启
            if (D) Log.v(TAG, "createLink---state---" + getLinkState());
            isMainIp = true;
            linkTimes = 0;
            isLinked = false;
            closeTimer();
            setLinkState(LinkState.start);
            linkNotify();
            //此处进行网络判定
//            if (!NetUtils.isConnected(mContext)) {
//                platformLinkCallBack.onTCPLinkState(ipInfo.tcpId, true, ipInfo, PlatformLinkState.ERROR_NO_NET);
//                Log.e(TAG, "无网络");
//                linkWait();
//            } else {
//                linkNotify();
//            }
        }

        public void closeLink() {
            handleClose = true;
            closeTimer();
            setLinkState(LinkState.over);
        }

        private void setRunning(boolean running) {
            this.isRunning = running;
        }

        private void linkNotify() {
            if (this.getState() == State.WAITING) {
                synchronized (this) {
                    this.notify();
                }
            }
        }

        private void linkWait() {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            super.run();
            while (isRunning) {
                if (isInterrupted()) {
                    return;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!handleClose && isFinish) {
                    linkHandle();
                }

            }
        }

        private void linkHandle() {
            isFinish = false;
            switch (getLinkState()) {
                case start:
                    isLinked = false;
                    isNetWork = NetUtils.isConnected(mContext);
                    if (isNetWork) {
                        if (getLinkState() == LinkState.start) {
                            setLinkState(LinkState.link);
                            if (D) Log.d(TAG, "正在尝试连接主服务器?=" + isMainIp);
                        }
                    } else {
                        if (getLinkState() == LinkState.start) {
                            setLinkState(LinkState.over);
//                            linkWait();
                        }
                    }
                    break;
                case link:
                    isLinked = false;
                    if (getLinkState() == LinkState.link) {
                        currentTime = System.currentTimeMillis();
                        setLinkState(LinkState.waitLink);
                        if (isMainIp) {
                            protocolManage.createSocket(ipInfo.tcpId, ipInfo.mainIp, ipInfo.mainPort);
                        } else {
                            if (ipInfo != null && !TextUtils.isEmpty(ipInfo.spareIp))
                                protocolManage.createSocket(ipInfo.tcpId, ipInfo.spareIp, ipInfo.sparePort);
                        }
                    }
                    break;
                case waitLink:
                    isLinked = false;
                    long time = System.currentTimeMillis();
                    if ((time - currentTime) > linkTimeout) {
                        if (getLinkState() == LinkState.waitLink) {
                            currentTime = 0;
                            if (D) Log.e(TAG, "连接超时");
                            setLinkState(LinkState.linkError);
                        }
                    }
                    break;
                case linkError:
                    if (D) Log.e(TAG, "连接失败,linkTimes=" + linkTimes);
                    isLinked = false;
                    if (linkTimes < maxLinkTimes) {
                        linkTimes++;
                        currentTime = System.currentTimeMillis();
                        if (D) Log.d(TAG, "重新连接");
                    } else {
                        //来回切换到主服务器、备用服务器
                        if (isMainIp && ipInfo.spareIp != null && !"".equals(ipInfo.spareIp) && ipInfo.sparePort != 0) {
                            isMainIp = false;
                        } else {
                            isMainIp = true;
                        }
                    }
                    setLinkState(LinkState.relinkWait);
                    break;
                case relinkWait:
                    isLinked = false;
                    long times = System.currentTimeMillis();
                    if ((times - currentTime) > linkTimeout) {
                        currentTime = 0;
                        setLinkState(LinkState.start);
                    }
                    break;
                case linkSuccess:
                    // 开启接收信息线程服务
//                  platformLinkCallBack.startSendMsg();
                    platformLinkCallBack.onTCPLinkState(ipInfo.tcpId, isMainIp, ipInfo, PlatformLinkState.NORMAL);
                    if (D) Log.d(TAG, "服务器连接成功");
                    linkTimes = 0;
                    isLinked = true;
                    currentTime = System.currentTimeMillis();
                    sendHeartBeat(ipInfo.tcpId);
                    setLinkState(LinkState.normal);
                    break;
                case listenHearbeat:
                    isLinked = true;
                    if ((System.currentTimeMillis() - currentTime) > (mHeartbeatTime * 3)) {
                        mLinkState = LinkState.linkError;
                        closeTimer();
                        platformLinkCallBack.onTCPLinkState(ipInfo.tcpId, isMainIp, ipInfo, PlatformLinkState.ERROR_OTHERS);
                        setLinkState(LinkState.linkError);
                        if (D) Log.e(TAG, "等待心跳接收超时了！");
                    }
                    break;
                case normal:
                    isLinked = true;
                    currentTime = System.currentTimeMillis();
                    setLinkState(LinkState.listenHearbeat);
                    break;
                case over:
                    isLinked = false;
                    // 状态上报
                    if (!isNetWork) {
//                        if (D) Log.e(TAG, "无网络状态");
//                        platformLinkCallBack.onTCPLinkState(ipInfo.tcpId, isMainIp, ipInfo, PlatformLinkState.ERROR_NO_NET);
                        platformLinkCallBack.onTCPLinkState(ipInfo.tcpId, true, ipInfo, PlatformLinkState.ERROR_NO_NET);
                        Log.e(TAG, "无网络");
                    }
                    linkWait();
                    break;
                case waitOrder:
                    isLinked = false;
                    //手动关闭等待中
                    break;
            }
            isFinish = true;
        }

        //------------------------心跳发送，不同平台各一个------------------------------------------
        private Timer timer = new Timer();
        private TimerTask timerTask;
        private T_HeartBeat heartBeat;

        private void sendHeartBeat(int tcpId) {
            heartBeat = new T_HeartBeat(tcpId);
            timer = new Timer();
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    protocolManage.sendMsgByTcp(heartBeat);
                }
            };
            timer.schedule(timerTask, 1000,
                    mHeartbeatTime/* 表示1000毫秒之後，每隔1000毫秒執行一次 */);
        }


        private void closeTimer() {
            if (timer != null && timerTask != null) {
                timerTask.cancel();
                timer.cancel();
                timerTask = null;
                timer = null;
            }
        }

        private void reStartTimer(int tcpId) {
            try {
                closeTimer();
            } finally {
                sendHeartBeat(tcpId);
            }
        }

        //-------------------------------------------------------------------------
        //线程独有
        private LinkState mLinkState = LinkState.start;

        private LinkState getLinkState() {
            return mLinkState;
        }

        private void setLinkState(LinkState mLinkState) {
            this.mLinkState = mLinkState;
            if (D) Log.d(TAG, "连接状态：" + mLinkState.name());
        }

        public void reStartHeartbeat(int time) {
            mHeartbeatTime = time;
            reStartTimer(ipInfo.tcpId);
        }
    }

    private enum LinkState {
        start, link, waitLink, relinkWait, linkError, linkSuccess,
        listenHearbeat, normal, over, waitOrder, no_net
    }


    //-------------------------------网络开断控制-----------------------------------------------------
    private NetWorkReceiver netWorkReceiver;// 网络状态监听广播

    /**
     * 打开网络监听
     */
    private void registerNetWork() {
        if (netWorkReceiver == null) {
            netWorkReceiver = new NetWorkReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mContext.registerReceiver(netWorkReceiver, filter);
        }
    }

    /**
     * 关闭网络监听
     */
    @Override
    public void unRegisterNetWork() {
        if (netWorkReceiver != null) {
            mContext.unregisterReceiver(netWorkReceiver);
            netWorkReceiver = null;
        }
//        paramSetLocalSource.unregisterNotify();
    }

    @Override
    public void setDeviceId(String deviceId) {
        protocolManage.setDeviceId(deviceId);
    }

    public class NetWorkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "网络状态，action=" + action);
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                isNetWork = NetUtils.isConnected(context);
                Log.d(TAG, "网络状态：" + isNetWork);
                if (isNetWork) {
                    for (Map.Entry<Integer, LinkManageThread> entry : threadMap.entrySet()) {
                        Log.v(TAG, "Net connect111!!!,createSocket tcpId = " + entry.getKey());
                        if (threadMap.get(entry.getKey()).isLinked == false) {
                            threadMap.get(entry.getKey()).setLinkState(LinkState.start);
                            threadMap.get(entry.getKey()).linkNotify();
                            Log.v(TAG, "Net connect!!!,createSocket tcpId = " + entry.getKey());
                        }
                    }
                } else {
                    for (Map.Entry<Integer, LinkManageThread> entry : threadMap.entrySet()) {
                        threadMap.get(entry.getKey()).setLinkState(LinkState.over);
                        Log.e(TAG, "Net disconnect!!!,closeSocket tcpId = " + entry.getKey());
                    }
                }
            }
        }
    }

}
