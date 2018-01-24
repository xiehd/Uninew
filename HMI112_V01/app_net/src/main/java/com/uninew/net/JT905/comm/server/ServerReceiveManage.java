package com.uninew.net.JT905.comm.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.uninew.net.JT905.bean.BaseBean;
import com.uninew.net.JT905.bean.Location_AlarmFlag;
import com.uninew.net.JT905.bean.Location_TerminalState;
import com.uninew.net.JT905.bean.T_AskQuestionAns;
import com.uninew.net.JT905.bean.T_DriverCancleOrder;
import com.uninew.net.JT905.bean.T_OperationDataReport;
import com.uninew.net.JT905.bean.T_SignInReport;
import com.uninew.net.JT905.bean.T_SignOutReport;
import com.uninew.net.JT905.comm.common.BaseMessageBean;
import com.uninew.net.JT905.comm.common.DefineNetAction;
import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.IpInfo;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * 服务端接收管理类
 */
public class ServerReceiveManage implements IServerReceiveManage {

    private Context mContext;
    private IServerReceiveListener.INetReceiveListener netReceiveListener;
    private IServerReceiveListener.IMmsReceiveListener mmsReceiveListener;
    private IServerReceiveListener.ITaxiReceiveListener mITaxiReceiveListener;
    private boolean IsMMS = false;

    public ServerReceiveManage(Context mContext) {
        super();
        this.mContext = mContext;
        // registerBroadCast();
    }

    @Override
    public void SetAction(boolean type) {
        IsMMS = type;
    }

    /**
     * 解包处理
     *
     * @param msg
     */
    private void unPackingMsg(BaseMessageBean msg) {

        switch (msg.getMsgId()) {
            case BaseMsgID.CONNECTSTATE_REQUEST://连接状态查询
                if (netReceiveListener != null) {
                    netReceiveListener.queryLinkState((Integer) msg.getObject());
                }
                break;
            case BaseMsgID.PLATFORM_CONNECT:// 连接平台
                if (netReceiveListener != null) {
                    netReceiveListener.createLink((IpInfo) msg.getObject());
                }
                break;

            case BaseMsgID.PLATFORM_DISCONNECT:// 断开连接
                if (netReceiveListener != null) {
                    netReceiveListener.disConnectLink((Integer) msg.getObject());
                }
                break;
            case BaseMsgID.DRIVER_ANSWER_ORDER:// 驾驶员抢答
                if (netReceiveListener != null) {
                    netReceiveListener.driverAnswerOrder((Integer) msg.getObject());
                }
                break;
            case BaseMsgID.DRIVER_CANCLE_ORDER:// 驾驶员取消订单
                if (netReceiveListener != null) {
                    netReceiveListener.driverCancelOrder((T_DriverCancleOrder) msg.getObject());
                }
                break;
            case BaseMsgID.ORDER_FINISH_ENSURE:// 电召任务完成确认
                if (netReceiveListener != null) {
                    netReceiveListener.orderFinishEnsure((Integer) msg.getObject());
                }
                break;
            case BaseMsgID.EVENT_REPORT:// 事件报告
                if (netReceiveListener != null) {
                    netReceiveListener.eventReport((Integer) msg.getObject());
                }
                break;
            case BaseMsgID.ASK_QUESTION_ANS:// 提问应答
                if (netReceiveListener != null) {
                    netReceiveListener.askQuestionAns((T_AskQuestionAns) msg.getObject());
                }
                break;
            case BaseMsgID.SIGN_IN_REPORT:// 上班签到
                if (netReceiveListener != null) {
                    netReceiveListener.signInReport((T_SignInReport) msg.getObject());
                }
                break;
            case BaseMsgID.SIGN_OUT_REPORT:// 下班签退
                if (netReceiveListener != null) {
                    netReceiveListener.signOutReport((T_SignOutReport) msg.getObject());
                }
                break;
            case BaseMsgID.WARN_REPORT:// 报警消息
                if (netReceiveListener != null) {
                    netReceiveListener.sendWarnMsg((Location_AlarmFlag) msg.getObject());
                }
                break;
            case BaseMsgID.STATE_REPORT:// 状态变化上报
                if (netReceiveListener != null) {
                    netReceiveListener.sendStateMsg((Location_TerminalState) msg.getObject());
                }
                break;
            case BaseMsgID.OPERATION_DATA_REPORT://上传运营数据
                if (null != netReceiveListener) {
                    netReceiveListener.operationDataReport((T_OperationDataReport) msg.getObject());
                }
                break;
            default:
                if (null != netReceiveListener) {
                    netReceiveListener.sendMsg((BaseBean) msg.getObject());
                }
                break;
            //---------------------MMS---------------------------------------------------------------
            case BaseMsgID.MMS_ARMSTATESYNCHRO://ARM状态同步
                if (null != mmsReceiveListener) {
                    mmsReceiveListener.ARMstateSynchro((byte) msg.getObject());
                }
                break;
            case BaseMsgID.MMS_SCREENBACKLIGHT://屏幕背光开关控制
                if (null != mmsReceiveListener) {
                    mmsReceiveListener.screenBacklight((byte) msg.getObject());
                }
                break;
            case BaseMsgID.MMS_SCREENBRIGHTNESS://屏幕亮度调节
                if (null != mmsReceiveListener) {
                    mmsReceiveListener.screenBrightness((byte) msg.getObject());
                }
                break;
            case BaseMsgID.MMS_POWERAMPLIFIER://功放开关控制
                if (null != mmsReceiveListener) {
                    mmsReceiveListener.powerAmplifier((byte) msg.getObject());
                }
                break;
            case BaseMsgID.MMS_SENDCANDATAS://CAN数据下发
                if (null != mmsReceiveListener) {
                    mmsReceiveListener.sendCanDatas((byte[]) msg.getObject());
                }
                break;
            case BaseMsgID.MMS_SENDRS232://RS232数据透传
                if (null != mmsReceiveListener) {
                    Hashtable<Byte, byte[]> ht = (Hashtable<Byte, byte[]>) msg.getObject();
                    if (ht == null) {
                        break;
                    }
                    //利用循环遍历出key和value
                    Iterator<Byte> itr = ht.keySet().iterator();
                    while (itr.hasNext()) {
                        Byte id = itr.next();
                        byte[] dtate = ht.get(id);
                        mmsReceiveListener.sendRS232(id, dtate);
                    }
                }
                break;
            case BaseMsgID.MMS_SENDRS485://RS485数据透传
                if (null != mmsReceiveListener) {
                    Hashtable<Byte, byte[]> ht = (Hashtable<Byte, byte[]>) msg.getObject();
                    if (ht == null) {
                        break;
                    }
                    //利用循环遍历出key和value
                    Iterator<Byte> itr = ht.keySet().iterator();
                    while (itr.hasNext()) {
                        Byte id = itr.next();
                        byte[] dtate = ht.get(id);
                        mmsReceiveListener.sendRS485(id, dtate);
                    }
                }
                break;
            case BaseMsgID.MMS_SETBAUDRATE://波特率设置
                if (null != mmsReceiveListener) {
                    Hashtable<Byte, Byte> ht = (Hashtable<Byte, Byte>) msg.getObject();
                    if (ht == null) {
                        break;
                    }
                    //利用循环遍历出key和value
                    Iterator<Byte> itr = ht.keySet().iterator();
                    while (itr.hasNext()) {
                        Byte id = itr.next();
                        Byte dtate = ht.get(id);
                        mmsReceiveListener.setBaudRate(id, dtate);
                    }
                }
                break;
            case BaseMsgID.MMS_SETIOSTATE://IO输入信号
                if (null != mmsReceiveListener) {
                    Hashtable<Byte, Byte> ht = (Hashtable<Byte, Byte>) msg.getObject();
                    if (ht == null) {
                        break;
                    }
                    //利用循环遍历出key和value
                    Iterator<Byte> itr = ht.keySet().iterator();
                    while (itr.hasNext()) {
                        Byte id = itr.next();
                        Byte dtate = ht.get(id);
                        mmsReceiveListener.setIOState(id, dtate);
                    }
                }
                break;
            case BaseMsgID.MMS_IOSTATE://IO输出信号查询
                if (null != mmsReceiveListener) {
                    mmsReceiveListener.queryIOState((byte) msg.getObject());
                }
                break;
            case BaseMsgID.MMS_ELECTRICITY://电量信息查询
                if (null != mmsReceiveListener) {
                    mmsReceiveListener.queryElectricity((byte) msg.getObject());
                }
                break;
            case BaseMsgID.MMS_PULSESIGNAL://脉冲测速查询查询
                if (null != mmsReceiveListener) {
                    mmsReceiveListener.queryPulseSignal();
                }
                break;
            case BaseMsgID.MMS_SETACCOFFTIME://等待关机时间
                if (null != mmsReceiveListener) {
                    mmsReceiveListener.setAccOffTime((Integer) msg.getObject());
                }
                break;
            case BaseMsgID.MMS_SETWAKEUPFREQUENCY://ARM唤醒频率
                if (null != mmsReceiveListener) {
                    mmsReceiveListener.setWakeupFrequency((Integer) msg.getObject());
                }
                break;
            //---------------------------------------计价器------------------------------------------------------------
            case BaseMsgID.TAXI_STATEQUREY://计价器状态
                if (null != mITaxiReceiveListener) {
                    mITaxiReceiveListener.quryTaxiState((String) msg.getObject());
                }
                break;
            case BaseMsgID.TAXI_FREIGHT://计价器运价参数
                if (null != mITaxiReceiveListener) {
                    mITaxiReceiveListener.quryTaxiFreight();
                }
                break;
            case BaseMsgID.TAXI_HISTORY_OPERA://计价器历史数据查询
                if (null != mITaxiReceiveListener) {
                    mITaxiReceiveListener.quryTaxiHistory((int) msg.getObject());
                }
                break;
            case BaseMsgID.TAXI_CLOCK://永久时钟校验
                if (null != mITaxiReceiveListener) {
                    mITaxiReceiveListener.ChekTaxiClock((String) msg.getObject());
                }
                break;

        }
    }

    private ServerBroadcastReceiver mBroadcastReceiver;

    private void registerBroadCast() {
        IntentFilter iFilter = new IntentFilter();
        if (IsMMS) {//注册根据需要不同的广播（默认注册net）
            iFilter.addAction(DefineNetAction.MMSLinkProtocolClient);
        } else {
            iFilter.addAction(DefineNetAction.NETLinkProtocolClient);
        }
        mBroadcastReceiver = new ServerBroadcastReceiver();
        mContext.registerReceiver(mBroadcastReceiver, iFilter);
    }

    @Override
    public void registerNetReceiveListener(IServerReceiveListener.INetReceiveListener netReceiveListener) {
        this.netReceiveListener = netReceiveListener;
        if (mBroadcastReceiver == null) {
            registerBroadCast();
        }
    }

    @Override
    public void unRegisterNetReceiveListener() {
        this.netReceiveListener = null;
        if (mBroadcastReceiver != null) {
            mContext.unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public void registerMmsReceiveListener(IServerReceiveListener.IMmsReceiveListener mmsReceiveListener) {
        this.mmsReceiveListener = mmsReceiveListener;
        if (mBroadcastReceiver == null) {
            registerBroadCast();
        }
    }

    @Override
    public void unRegisterMmsReceiveListener() {
        this.mmsReceiveListener = null;
        if (mBroadcastReceiver != null) {
            mContext.unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public void registerTaxiReceiveListener(IServerReceiveListener.ITaxiReceiveListener mTaxiReceiveListener) {
        this.mITaxiReceiveListener = mTaxiReceiveListener;
        if (mBroadcastReceiver == null) {
            registerBroadCast();
        }
    }

    @Override
    public void unRegisterTaxiReceiveListener() {
        this.mITaxiReceiveListener = null;
        if (mBroadcastReceiver != null) {
            mContext.unregisterReceiver(mBroadcastReceiver);
        }
    }

    public class ServerBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case DefineNetAction.NETLinkProtocolClient:
                    receiveManage(intent);
                    break;
                case DefineNetAction.MMSLinkProtocolClient://mms
                    receiveManage(intent);
                    break;
                default:
                    break;
            }
        }

        private void receiveManage(Intent intent) {
            // TODO Auto-generated method stub
            BaseMessageBean msg = (BaseMessageBean) intent.getSerializableExtra(DefineNetAction.Key_Object);
            unPackingMsg(msg);
        }
    }
}
