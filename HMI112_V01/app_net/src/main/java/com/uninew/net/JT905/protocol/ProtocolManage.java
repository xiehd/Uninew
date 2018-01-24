package com.uninew.net.JT905.protocol;

import android.content.Context;
import android.util.Log;

import com.uninew.net.JT905.bean.BaseBean;
import com.uninew.net.JT905.net.NetBase;
import com.uninew.net.JT905.tcp.TCPLink;
import com.uninew.net.JT905.tcp.TCPLinkErrorEnum;
import com.uninew.net.JT905.bean.P_GeneralResponse;
import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.Define;
import com.uninew.net.JT905.net.INetBase;
import com.uninew.net.JT905.net.INetBaseCallBack;
import com.uninew.net.JT905.tcp.TCPRunStateEnum;
import com.uninew.net.JT905.util.ByteTools;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 发送和接收控制
 * 实现消息发送控制和消息分发处理
 * Created by Administrator on 2017/8/20 0020.
 */

public class ProtocolManage implements IProtocolManage, INetBaseCallBack {

    private static final String TAG = "ProtocolManage";
    private static final boolean D = true;
    private Context mContext;
    private static int sendSerialNumber;
    private INetBase mNetBase;
    private IProtocolCallBack mProtocolCallBack;

    private String deviceId = Define.Default_DeviceId;
    private int tcpResponseTimeOut = Define.Default_TcpResponseTimeOut;
    private int tcpResendTime = Define.Default_TcpResendTime;
    private int smsResponseTimeOut = Define.Default_SmsResponseTimeOut;
    private int smsResendTime = Define.Default_SmsResendTime;

    private Object receiveLock;// 接收锁
    private Object sendLock;// 发送锁
    private ConcurrentLinkedQueue<ProtocolPacket> sendMsgQueue;// 发送栈
    private SendMessageThread sendMessageThread;// 发送消息的处理线程
    private ReceiveDatasThread receiveDatasThread;// 接收消息的处理线程
    private ReSendControlThread resendControlThread;//重发消息控制线程
    private ProtocolPacket sendPacket;
    private ProtocolPacket receivePacket;

    private ReSendMap mResendMap;
    private static ProtocolManage instance;

    private ProtocolManage(Context mContext) {
        this.mContext = mContext;
        mNetBase = NetBase.getInstance(mContext);
        mNetBase.setNetBaseListener(this);
        mResendMap = new ReSendMap();
        receiveLock = new Object();
        sendLock = new Object();
        sendSerialNumber = 0;
        sendMessageThread = new SendMessageThread();
        receiveDatasThread = new ReceiveDatasThread();
        resendControlThread = new ReSendControlThread();
        sendMsgQueue = new ConcurrentLinkedQueue<ProtocolPacket>();
        startSendAndReceive();
    }

    public void setProtocolCallBack(IProtocolCallBack mProtocolCallBack) {
        this.mProtocolCallBack = mProtocolCallBack;
    }

    public synchronized static ProtocolManage getInstance(Context mContext) {
        if (instance == null) {
            instance = new ProtocolManage(mContext);
        }
        return instance;
    }


    /**
     * 开始发送和接收处理
     */
    public synchronized void startSendAndReceive() {
        if (sendMessageThread != null && sendMessageThread.getState() != Thread.State.RUNNABLE && !sendMessageThread.isAlive()) {
            isSendRunning = true;
            sendMessageThread.setPriority(Thread.MAX_PRIORITY);
            sendMessageThread.setName("sendMessageThread");
            sendMessageThread.start();
        }
        if (receiveDatasThread != null && receiveDatasThread.getState() != Thread.State.RUNNABLE && !receiveDatasThread.isAlive()) {
            isReceiveRunning = true;
            receiveDatasThread.setPriority(Thread.MAX_PRIORITY);
            receiveDatasThread.setName("receiveDatasThread");
            receiveDatasThread.start();
        }
        if (resendControlThread != null && resendControlThread.getState() != Thread.State.RUNNABLE && !resendControlThread.isAlive()) {
            resendControlThread.setPriority(Thread.MAX_PRIORITY);
            resendControlThread.setName("resendControlThread");
            resendControlThread.start();
        }
    }

    @Override
    public void onTCPRunState(int tcpId, TCPRunStateEnum vValue, TCPLinkErrorEnum error) {
        mProtocolCallBack.onTCPRunState(tcpId, vValue, error);
    }

    @Override
    public void onReceiveTcpDatas(int tcpId, byte[] datas) {
        if (D) Log.d(TAG, "onReceiveTcpDatas,tcpId=" + tcpId + " ,datas=" + ByteTools.logBytes(datas));
        receiveDatasThread.addReceiveDatas(Define.Transport_TCP, tcpId, datas);
    }

    @Override
    public void onReceiveSMSDatas(String phoneNumber, byte[] datas) {
        receiveDatasThread.addReceiveDatas(Define.Transport_SMS, 0, datas);
    }

    @Override
    public void setParams(String deviceId, int tcpResponseTimeOut, int tcpResendTime, int smsResponseTimeOut, int smsResendTime) {

        this.deviceId = deviceId;
        this.tcpResendTime = tcpResendTime;
        this.tcpResponseTimeOut = tcpResponseTimeOut;
        this.smsResendTime = smsResendTime;
        this.smsResponseTimeOut = smsResponseTimeOut;
    }

    @Override
    public void setTcpResponseTimeOut(int tcpResponseTimeOut) {
        this.tcpResponseTimeOut = tcpResponseTimeOut;
    }

    @Override
    public void setTcpResendTime(int times) {
        this.tcpResendTime = times;
    }

    @Override
    public boolean createSocket(int tcpId, String ip, int port) {
        return mNetBase.createSocket(tcpId, ip, port);
    }

    @Override
    public boolean closeSocket(int tcpId) {
        return mNetBase.closeSocket(tcpId);
    }

    @Override
    public void sendMsgByTcp(BaseBean mBaseBean) {
        sendMessageThread.addMsg(mBaseBean);
    }

    @Override
    public void sendMsgBySms(String phoneNumber, BaseBean mBaseBean) {
        sendMessageThread.addMsg(mBaseBean);
    }

    @Override
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    private int getSerialNumber() {
        if (sendSerialNumber < 0 || sendSerialNumber > 65535) {
            sendSerialNumber = 0;
        } else {
            sendSerialNumber++;
        }
        if (D) Log.d(TAG, "---------------sendSerialNumber=" + sendSerialNumber);
        return sendSerialNumber;
    }

    //----------------------------消息发送控制---------------------------------------
    private boolean isSendRunning = false;

    private class SendMessageThread extends Thread {
        private boolean isFinish = true;
        private int msgId = 0;

        public void addMsg(BaseBean mBaseBean) {
            if (mBaseBean != null) {
                synchronized (sendLock) {
                    ProtocolPacket messagePack = new ProtocolPacket();
                    byte[] tempDatas = mBaseBean.getDataBytes();
                    if (tempDatas == null || tempDatas.length == 0) {
                            messagePack.setHead(new ProtocolHead(mBaseBean.getMsgId(),
                                    0, deviceId, getSerialNumber()));
                    } else {
                            messagePack.setHead(new ProtocolHead(mBaseBean.getMsgId(),
                                    tempDatas.length, deviceId, getSerialNumber()));
                    }
                    messagePack.setTcpId(mBaseBean.getTcpId());
                    messagePack.setTransportId(mBaseBean.getTransportId());
                    messagePack.setBody(tempDatas);
                    sendMsgQueue.offer(messagePack);// 置入发送栈中
                    sendMessageThread.sendNotify();
                }
            }
        }

        public void sendNotify() {
            if (this.getState() == Thread.State.WAITING) {
                synchronized (this) {
                    this.notify();
                }
            }
        }

        public void sendWaiting() {
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
            while (isSendRunning) {
                if (this.isInterrupted()) {
                    return;
                }
                if (!sendMsgQueue.isEmpty() && isFinish) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        synchronized (sendLock) {
                            sendDataHandler();
                        }
                    }
                } else {
                    sendWaiting();
                }
            }
        }

        private void sendDataHandler() {
            isFinish = false;
            if (!sendMsgQueue.isEmpty()) {
                sendPacket = sendMsgQueue.peek();
                sendMsgToTcpSms(sendPacket);
                reSendMsgHandle(sendPacket);
                sendMsgQueue.remove(sendPacket);
            }
            isFinish = true;
        }

        private void reSendMsgHandle(ProtocolPacket sendedPacket) {
            if (D) Log.d(TAG, "reSendMsgHandle");
            //判断是否需要重发
            if (mResendMap.isNeedReSend(sendedPacket.getMsgId())) {
                sendedPacket.setSendTime(System.currentTimeMillis());
                if (D)
                    Log.d(TAG, "sendMsg,System.currentTimeMillis()=" + System.currentTimeMillis());
                sendedPacket.setSendNumber(0);
                resendControlThread.addSendedMsg(sendedPacket);
                if (D) Log.d(TAG, "reSendMsgHandle---isNeedReSend-" + sendedPacket.toString());
            }
        }
    }

    private void sendMsgToTcpSms(ProtocolPacket pp) {
        if (D) Log.e(TAG, "sendMsgToTcpSms");
        if (pp.getTransportId() == Define.Transport_TCP) {
            mNetBase.sendMsgByTcp(pp.getTcpId(), pp.getProtocolPacketBytes());
        } else if (pp.getTransportId() == Define.Transport_SMS) {
            mNetBase.sendMsgBySms(pp.getSmsPhoneNunber(), pp.getProtocolPacketBytes());
        }
    }

    //----------------------------消息接收控制---------------------------------------
    private boolean isReceiveRunning = false;

    private class ReceiveDatasThread extends Thread {
        private boolean isFinish = true;
        private ConcurrentLinkedQueue<ReceiveClass> receiveDataQueue;// 接收的数据栈
        private Lock lock = new ReentrantLock();

        public ReceiveDatasThread() {
            receiveDataQueue = new ConcurrentLinkedQueue<>();
        }

        private void addReceiveDatas(int transport, int tcpId, byte[] datas) {
            lock.lock();
            if (D)
                Log.d(TAG, "addReceiveDatas,transport=" + transport + ",tcpId=" + tcpId + " " +
                        ",datas=" + ByteTools.logBytes(datas));
            try {
                receiveDataQueue.add(new ReceiveClass(transport, tcpId, datas));
                receiveNotify();
            } finally {
                lock.unlock();
            }
        }

        private ReceiveClass getReciveDatas() {
            return receiveDataQueue.peek();
        }

        private boolean removeReciveDatas(ReceiveClass receiveClass) {
            lock.lock();
            try {
                if (!receiveDataQueue.isEmpty()) {
                    return receiveDataQueue.remove(receiveClass);
                }
            } finally {
                lock.unlock();
            }
            return false;
        }

        public void receiveNotify() {
            if (this.getState() == Thread.State.WAITING) {
                synchronized (this) {
                    this.notify();
                }
            }
        }

        public void receiveWaiting() {
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
            while (isReceiveRunning) {
                if (isInterrupted()) {
                    return;
                }
                if (!receiveDataQueue.isEmpty() && isFinish) {
                    try {
                        Thread.sleep(20);
                        lock.lock();
                        isFinish = false;
                        receiveDatasHandler();
                        isFinish = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                } else {
                    if (D) Log.d(TAG, "数据栈为空---receiveWaiting");
                    receiveWaiting();
                }
            }
        }

        ProtocolPacket receivePacket;

        private void receiveDatasHandler() {
            // 消息转换
            datasToObject(getReciveDatas());
            // 消息判断
            judge(receivePacket);

        }

        private void judge(ProtocolPacket receive) {
            switch (receive.getMsgId()) {
                case BaseMsgID.PLATFORM_GENERAL_ANS: //平台通用应答
                    //获取应答消息ID
                    P_GeneralResponse pg = new P_GeneralResponse(receive.getBody());
                    pg.setTcpId(receive.getTcpId());
                    pg.setTransportId(receive.getTransportId());
                    if (D) Log.d(TAG, "rP_GeneralResponse=" + pg.toString());
                    int responseId = pg.getId();
                    mProtocolCallBack.onGeneralResponse(receive.getTcpId(), responseId, pg.getpSerialNumber(), pg.getResult());
                    if (D) Log.d(TAG, "response ID=" + Integer.toHexString(responseId));
                    //是否在重发消息队里
                    if (mResendMap.isNeedReSend(responseId)) {
                        resendControlThread.removeMsg(responseId);
                    }
                    break;
                default:
                    //是否为重发应答消息
                    if (mResendMap.isNeedReSendAns(receive.getMsgId())) {
                        int keyMsgId = mResendMap.getKeyByValue(receive.getMsgId());
                        resendControlThread.removeMsg(keyMsgId);
                    }
                    break;
            }
            receiveDataQueue.remove();
        }

        private ProtocolPacket datasToObject(ReceiveClass receiveClass) {
            //将消息转换成协议包
            receivePacket = new ProtocolPacket();
            receivePacket = receivePacket.getProtocolPacket(receiveClass.datas);
            receivePacket.setTcpId(receiveClass.tcpId);
            receivePacket.setTransportId(receiveClass.transportId);
            //消息上报
            mProtocolCallBack.onReceiveDatas(receivePacket);
            if (D)
                Log.d(TAG, "datasToObject-receivePacket=0x" + Integer.toHexString(receivePacket.getHead().getMsgId()));
            return receivePacket;
        }
    }

    //接收临时存储用
    private class ReceiveClass {
        int tcpId;
        int transportId;
        byte[] datas;

        public ReceiveClass(int transportId, int tcpId, byte[] datas) {
            this.tcpId = tcpId;
            this.transportId = transportId;
            this.datas = datas;
        }
    }

    //----------------------------消息重发控制------------------------------------------

    private class ReSendControlThread extends Thread {
        private boolean isFinish = true;
        private ConcurrentLinkedQueue<ProtocolPacket> sendedMsgQueue;// 已发送栈
        private Lock resendLock = new ReentrantLock();

        public ReSendControlThread() {
            sendedMsgQueue = new ConcurrentLinkedQueue<ProtocolPacket>();
        }

        private void addSendedMsg(ProtocolPacket msg) {
            resendLock.lock();
            try {
                sendedMsgQueue.add(msg);
                controlNotify();
                if (D) Log.d(TAG, "addSendedMsg");
            } finally {
                resendLock.unlock();
            }
        }

        private ProtocolPacket getSendedMsg() {
            synchronized (this) {
                if (!sendedMsgQueue.isEmpty()) {
                    return sendedMsgQueue.peek();
                }
            }
            return null;
        }

        private boolean removeMsg(ProtocolPacket removeMsg) {
            resendLock.lock();
            try {
                if (!sendedMsgQueue.isEmpty()) {
                    return sendedMsgQueue.remove(removeMsg);
                }
            } finally {
                resendLock.unlock();
            }
            return false;
        }

        private boolean removeMsg(int msgId) {
            resendLock.lock();
            try {
                ProtocolPacket pp;
                for (int i = 0; i < sendedMsgQueue.size(); i++) {
                    pp = sendedMsgQueue.peek();
                    if (pp.getMsgId() == msgId) {
                        sendedMsgQueue.remove(pp);
                        break;
                    }
                }
            } finally {
                resendLock.unlock();
            }
            return false;
        }

        public void controlNotify() {
            if (this.getState() == Thread.State.WAITING) {
                synchronized (this) {
                    this.notify();
                }
            }
        }

        public void controlWaiting() {
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
            while (true) {
                if (isInterrupted()) {
                    return;
                }
                if (!sendedMsgQueue.isEmpty() && isFinish) {
                    resendLock.lock();
                    try {
                        isFinish = false;
                        reSendControlHandler();
                        isFinish = true;
                    } finally {
                        resendLock.unlock();
                    }

                } else {
                    if (D) Log.d(TAG, "数据栈为空----controlWaiting");
                    controlWaiting();
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


        private ProtocolPacket resendMsg;

        private void reSendControlHandler() {
            switch (controlState) {
                case isTimeOut://超时判断
                    for (int i = 0; i < sendedMsgQueue.size(); i++) {
                        resendMsg = getSendedMsg();
                        if ((System.currentTimeMillis() - resendMsg.getSendTime()) / 1000 >= getTimeOut(resendMsg.getTransportId(), resendMsg.getSendNumber() + 1)) {
                            //超时
                            if (D) Log.d(TAG, "-------isTimeOut-true-------");
                            setControlState(ControlState.isOver);
                            reSendControlHandler();
                            break;
                        }
                    }
                    break;
                case isOver://判断重传次数是否已达到
                    int resendNumber = resendMsg.getTransportId() == Define.Transport_TCP ? tcpResendTime : smsResendTime;
                    if (D)
                        Log.d(TAG, "resendNumber=" + resendNumber + " ,resendMsg.getSendNumber()=" + resendMsg.getSendNumber());
                    if (resendMsg.getSendNumber() == resendNumber) {
                        //重传次数已到，不再重传
//                      sendedMsgQueue.remove(resendMsg);
                        if (D) Log.e(TAG, "------removeMsg-----");
                        resendControlThread.removeMsg(resendMsg);
                        setControlState(ControlState.isTimeOut);
                    } else {
                        setControlState(ControlState.resend);
                    }
                    reSendControlHandler();
                    break;
                case resend:
                    if (D) Log.d(TAG, "----------resend-----");
                    sendMsgToTcpSms(resendMsg);
                    resendMsg.setSendNumber(resendMsg.getSendNumber() + 1);
                    resendMsg.setSendTime(System.currentTimeMillis());
                    if (D)
                        Log.d(TAG, "----------resend-----resendMsg.getSendNumber()=" + resendMsg.getSendNumber());
                    setControlState(ControlState.isTimeOut);
                    reSendControlHandler();
                    break;
            }
        }
    }

    /**
     * 获取超时时间 Tn+1=Tnx(n+1)
     *
     * @param time 重传次数(不包含第一次发送)
     * @return 超时时间（S）
     */
    private int getTimeOut(int transportId, int time) {
        int timeOut = 0;
        int t = 1;
        for (int i = 1; i <= time; i++) {
            t = t * i;
        }
        if (transportId == Define.Transport_TCP) {
            timeOut = tcpResponseTimeOut * t;
        } else if (transportId == Define.Transport_SMS) {
            timeOut = smsResponseTimeOut * t;
        }
        return timeOut;
    }

    private enum ControlState {
        isTimeOut,
        resend,
        isOver;
    }

    private volatile ControlState controlState = ControlState.isTimeOut;

    private void setControlState(ControlState controlState) {
        this.controlState = controlState;
//        if (D) Log.d(TAG, "ControlState:" + controlState.name());
    }

}
