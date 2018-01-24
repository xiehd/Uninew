package com.uninew.net.JT905.comm.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.uninew.net.JT905.bean.P_Callback;
import com.uninew.net.JT905.bean.P_DriverAnswerOrderAns;
import com.uninew.net.JT905.bean.P_EventSet;
import com.uninew.net.JT905.bean.P_OrderSendDown;
import com.uninew.net.JT905.bean.P_PhoneBookSet;
import com.uninew.net.JT905.bean.P_TextIssued;
import com.uninew.net.JT905.comm.common.ActionDefine;
import com.uninew.net.JT905.comm.common.BaseMessageBean;
import com.uninew.net.JT905.comm.common.DefineNetAction;
import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.IpInfo;
import com.uninew.net.Taximeter.bean.P_TaxiOperationDataReport;
import com.uninew.net.Taximeter.bean.T_HeartBeat;

import java.util.Hashtable;

/**
 * 客户端接收管理类
 */
public class ClientReceiveManage implements IClientReceiveManage {

    private Context mContext;
    private IClientReceiveListener.IConnectListener mConnectListener;
    private IClientReceiveListener.ICallListener mCallListener;
    private IClientReceiveListener.IMsgListener msgLinstener;
    private IClientReceiveListener.IPhoneListener mPhoneListener;
    private IClientReceiveListener.IPushCardListener mPushCardListener;
    private IClientReceiveListener.IOperationListener mOperationListener;
    private IClientReceiveListener.IMmsDataListener mIMmsDataListener;
    private IClientReceiveListener.ITaxiStateListener mTaxiStateListener;
    private IClientReceiveListener.ITaxiOperateStateListener mTaxiOperateStateListener;
    private IClientReceiveListener.ITaxiHeartStateLister mTaxiHeartStateLister;
    private IClientReceiveListener.ITaxiTaxiFreightLister mTaxiTaxiFreightLister;


    public ClientReceiveManage(Context mContext) {
        super();
        this.mContext = mContext;
        //registerBroadCast();
    }


    //------------------------------------------------------------------

    /**
     * 监听连接状态
     *
     * @param mConnectListener
     */
    public void registerConnectStateListener(
            IClientReceiveListener.IConnectListener mConnectListener) {
        this.mConnectListener = mConnectListener;
        if (mBroadcastReceiver == null) {
            registerBroadCast();
        }
    }

    public void unRegisterConnectStateListener() {
        this.mConnectListener = null;
        if (mBroadcastReceiver != null) {
            mContext.unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public void registerCallListener(IClientReceiveListener.ICallListener callListener) {
        this.mCallListener = callListener;
        if (mBroadcastReceiver == null) {
            registerBroadCast();
        }
    }

    @Override
    public void unRegisterCallListener() {
        this.mCallListener = null;
        if (mBroadcastReceiver != null) {
            mContext.unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public void registerMsgListener(IClientReceiveListener.IMsgListener msgListener) {
        this.msgLinstener = msgListener;
        if (mBroadcastReceiver == null) {
            registerBroadCast();
        }
    }

    @Override
    public void unRegisterMsgListener() {
        this.mCallListener = null;
        if (mBroadcastReceiver != null) {
            mContext.unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public void registerPhoneListener(IClientReceiveListener.IPhoneListener phoneListener) {
        this.mPhoneListener = phoneListener;
        if (mBroadcastReceiver == null) {
            registerBroadCast();
        }
    }

    @Override
    public void unRegisterPhoneListener() {
        this.mCallListener = null;
        if (mBroadcastReceiver != null) {
            mContext.unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public void registerPushCardListener(IClientReceiveListener.IPushCardListener pushCardListener) {
        this.mPushCardListener = pushCardListener;
        if (mBroadcastReceiver == null) {
            registerBroadCast();
        }
    }

    @Override
    public void unRegisterPushCardListener() {
        this.mPushCardListener = null;
        if (mBroadcastReceiver != null) {
            mContext.unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public void registerOperationListener(IClientReceiveListener.IOperationListener operationListener) {
        this.mOperationListener = operationListener;
        if (mBroadcastReceiver == null) {
            registerBroadCast();
        }
    }

    @Override
    public void unRegisterOperationListener() {
        this.mOperationListener = null;
        if (mBroadcastReceiver != null) {
            mContext.unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public void registerMmsListener(IClientReceiveListener.IMmsDataListener mmsDataListener) {
        this.mIMmsDataListener = mmsDataListener;
        if (mBroadcastReceiver == null) {
            registerBroadCast();
        }
    }

    @Override
    public void unRegisterMmsListener() {
        this.mIMmsDataListener = null;
        if (mBroadcastReceiver != null) {
            mContext.unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public void registerTaxiStateListener(IClientReceiveListener.ITaxiStateListener mTaxiSateListener) {
        this.mTaxiStateListener = mTaxiSateListener;
        if (mBroadcastReceiver == null) {
            registerBroadCast();
        }
    }

    @Override
    public void unRegisterTaxiSateListener() {
        this.mTaxiStateListener = null;
        if (mBroadcastReceiver != null) {
            mContext.unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public void registerTaxiOperateStateListener(IClientReceiveListener.ITaxiOperateStateListener mTaxiOperateStateListener) {
        this.mTaxiOperateStateListener = mTaxiOperateStateListener;
        if (mBroadcastReceiver == null) {
            registerBroadCast();
        }
    }

    @Override
    public void unRegisterTaxiOperateSateListener() {
        this.mTaxiOperateStateListener = null;
        if (mBroadcastReceiver != null) {
            mContext.unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public void registerTaxiHeartStateListener(IClientReceiveListener.ITaxiHeartStateLister mTaxiHeartStateLister) {
        this.mTaxiHeartStateLister = mTaxiHeartStateLister;
        if (mBroadcastReceiver == null) {
            registerBroadCast();
        }
    }

    @Override
    public void unRegisterTaxiHeartSateListener() {
        this.mTaxiHeartStateLister = null;
        if (mBroadcastReceiver != null) {
            mContext.unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public void registerTaxiTaxiFreightLister(IClientReceiveListener.ITaxiTaxiFreightLister mTaxiTaxiFreightLister) {
        this.mTaxiTaxiFreightLister = mTaxiTaxiFreightLister;
        if (mBroadcastReceiver == null) {
            registerBroadCast();
        }
    }

    @Override
    public void unRegisterTaxiFreightLister() {
        this.mTaxiTaxiFreightLister = null;
        if (mBroadcastReceiver != null) {
            mContext.unregisterReceiver(mBroadcastReceiver);
        }
    }
    //------------------------------------------------------------------

    /**
     * 解包处理
     *
     * @param msg
     */
    private void unPackingMsg(BaseMessageBean msg) {
        switch (msg.getMsgId()) {
            case BaseMsgID.CONNECTSTATE_RESPONSE://连接状态应答
                if (mConnectListener != null) {
                    mConnectListener.linkStateNotify((IpInfo) msg.getObject());
                }
                break;
            case BaseMsgID.ORDER_SEND_DOWN://订单下发
                if (mCallListener != null) {
                    mCallListener.orderSendDown((P_OrderSendDown) msg.getObject());
                }
                break;
            case BaseMsgID.DRIVER_ANSWER_ORDER_RESPONSE://抢答结果通知
                if (mCallListener != null) {
                    mCallListener.answerOrderResponse((Integer) msg.getObject());
                }
                break;
            case BaseMsgID.DRIVER_ANSWER_ORDER_MSG://抢答成功订单信息下发
                if (mCallListener != null) {
                    mCallListener.answerOrderMsg((P_DriverAnswerOrderAns) msg.getObject());
                }
                break;
            case BaseMsgID.PLATFORM_CANCLE_ORDER://中心取消订单
                if (mCallListener != null) {
                    mCallListener.platformcancelOrder((Integer) msg.getObject());
                }
                break;
            case BaseMsgID.DRIVER_CANCLE_ORDER_ANS://驾驶员取消订单结果
                if (mCallListener != null) {
                    mCallListener.driverCancelOrderAns((Integer) msg.getObject());
                }
                break;
            case BaseMsgID.TEXT_MSG_DOWNLOAD://文本下发
                if (msgLinstener != null) {
                    msgLinstener.textSendDown((P_TextIssued) msg.getObject());
                }
                break;
            case BaseMsgID.EVENT_SET://事件设置
                if (msgLinstener != null) {
                    msgLinstener.eventSet((P_EventSet) msg.getObject());
                }
                break;
            case BaseMsgID.ASK_QUESTION://提问下发
                if (msgLinstener != null) {
                    msgLinstener.textSendDown((P_TextIssued) msg.getObject());
                }
                break;
            case BaseMsgID.CALL_BACK://电话回拨
                if (mPhoneListener != null) {
                    P_Callback callback = (P_Callback) msg.getObject();
                    mPhoneListener.callBack(callback.getId(), callback.getPhoneNumber());
                }
                break;
            case BaseMsgID.SET_PHONE_BOOK://设置电话本
                if (mPhoneListener != null) {
                    mPhoneListener.setPhoneBook((P_PhoneBookSet) msg.getObject());
                }
                break;
            case BaseMsgID.SIGN_IN_ANS://签到应答
                if (mPushCardListener != null) {
                    mPushCardListener.signInReportAns((Integer) msg.getObject());
                }
                break;
            case BaseMsgID.SIGN_OUT_ANS://签退应答
                if (mPushCardListener != null) {
                    mPushCardListener.signOutReportAns((Integer) msg.getObject());
                }
                break;
            case BaseMsgID.OPERATION_DATA_REPORT:
                if (mOperationListener != null) {
                    mOperationListener.operationDataReportAns((Integer) msg.getObject());
                }
                break;
            //mms----------------------------------------------------------------------
            case BaseMsgID.MMS_SYSTEMSTATE://系统状态通知
                if (mIMmsDataListener != null) {
                }
                break;
            case BaseMsgID.MMS_MCUVERSION://MCU版本号
                if (mIMmsDataListener != null) {
                }
                break;
            case BaseMsgID.MMS_ELECTRICITY://电池电量信息
                if (mIMmsDataListener != null) {
                }
                break;
            case BaseMsgID.MMS_HANDLEMCUKEY://物理按键通知
                if (mIMmsDataListener != null) {
                }
                break;
            case BaseMsgID.MMS_CANDATAS://CAN数据透传上报
                if (mIMmsDataListener != null) {
                }
                break;
            case BaseMsgID.MMS_RECEIVERS232://RS232数据透传
                if (mIMmsDataListener != null) {
                }
                break;
            case BaseMsgID.MMS_RECEIVERS485://RS485数据透传
                if (mIMmsDataListener != null) {
                }
                break;
            case BaseMsgID.MMS_BAUDRATE://波特率设置信息
                if (mIMmsDataListener != null) {
                }
                break;
            case BaseMsgID.MMS_IOSTATE://IO输出信号
                if (mIMmsDataListener != null) {
                }
                break;
            case BaseMsgID.MMS_PULSESIGNAL://接收脉冲测速信号
                if (mIMmsDataListener != null) {
                }
                break;
            //-----------------------------------------计价器-------------------------------------------------------
            case BaseMsgID.TAXI_STATEQUREY://计价器状态
                if (mTaxiStateListener != null) {
                    Hashtable<String, Object> hashtable = new Hashtable<>();
                    hashtable = (Hashtable<String, Object>) msg.getObject();
                    if (hashtable == null)
                        break;
                    int deviceState = (int) hashtable.get(ActionDefine.TaxiKey.key_deviceState);
                    int taxiState = (int) hashtable.get(ActionDefine.TaxiKey.key_taxiState);
                    String deviceNumber = (String) hashtable.get(ActionDefine.TaxiKey.key_deviceNumber);
                    String carNumber = (String) hashtable.get(ActionDefine.TaxiKey.key_carNUmber);
                    int oprateNumber = (int) hashtable.get(ActionDefine.TaxiKey.key_oprateNumber);
                    mTaxiStateListener.quryTaxiStateResponse(deviceState, taxiState, deviceNumber, carNumber, oprateNumber);
                }
                break;
            case BaseMsgID.TAXI_OPERATE_STATR://进入重车
                if (mTaxiOperateStateListener != null) {
                    mTaxiOperateStateListener.TaxiOperateStateStart((String) msg.getObject());
                }
                break;
            case BaseMsgID.TAXI_OPERATE_END://进入空车
                if (mTaxiOperateStateListener != null) {
                    mTaxiOperateStateListener.TaxiOperateStateEnd((P_TaxiOperationDataReport) msg.getObject());
                }
                break;
            case BaseMsgID.TAXI_HEART://心跳状态
                if (mTaxiHeartStateLister != null) {
                    mTaxiHeartStateLister.TaxiHeartState((T_HeartBeat) msg.getObject());
                }
                break;
        }
    }

    private boolean IsMMS = false;

    @Override
    public void SetAction(boolean type) {
        IsMMS = type;
    }

    private ClientBroadcastReceiver mBroadcastReceiver;

    private void registerBroadCast() {
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(DefineNetAction.MMSLinkProtocolServer);
        iFilter.addAction(DefineNetAction.NETLinkProtocolServer);
        mBroadcastReceiver = new ClientBroadcastReceiver();
        mContext.registerReceiver(mBroadcastReceiver, iFilter);
    }

    public class ClientBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case DefineNetAction.NETLinkProtocolServer:
                    //Log.i("xhd","客户接受 net");
                    receiveManage(intent);
                    break;
                case DefineNetAction.MMSLinkProtocolServer://mms
                    //Log.i("xhd","客户接受 mms");
                    receiveManage(intent);
                    break;
                default:
                    break;
            }
        }

        private synchronized void receiveManage(Intent intent) {
            // TODO Auto-generated method stub
            BaseMessageBean msg = (BaseMessageBean) intent
                    .getSerializableExtra(DefineNetAction.Key_Object);
            unPackingMsg(msg);
        }
    }

}
