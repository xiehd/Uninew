package com.uninew.net.JT905.comm.client;

import android.content.Context;
import android.content.Intent;

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


public class ClientSendManage implements IClientSendManage {

    private Context mContext;

    public ClientSendManage(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public void send(BaseMessageBean baseMsg) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(DefineNetAction.NETLinkProtocolClient);
        intent.putExtra(DefineNetAction.Key_Object, baseMsg);
        mContext.sendBroadcast(intent);
    }

    private void send(int msgId, Object object) {
        send(new BaseMessageBean(msgId, object));
    }

    @Override
    public void sendMms(BaseMessageBean baseMsg) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(DefineNetAction.MMSLinkProtocolClient);
        intent.putExtra(DefineNetAction.Key_Object, baseMsg);
        mContext.sendBroadcast(intent);
    }

    private void sendMms(int msgId, Object object) {
        sendMms(new BaseMessageBean(msgId, object));
    }

    @Override
    public void createLink(IpInfo ipInfo) {
        // TODO Auto-generated method stub
        send(BaseMsgID.PLATFORM_CONNECT, ipInfo);
    }

    @Override
    public void disConnectLink(int tcpId) {
        // TODO Auto-generated method stub
        send(BaseMsgID.PLATFORM_DISCONNECT, tcpId);
    }


    @Override
    public void queryLinkState(int tcpId) {
        // TODO Auto-generated method stub
        send(BaseMsgID.CONNECTSTATE_REQUEST, tcpId);
    }

    @Override
    public void driverAnswerOrder(int businessId) {
        send(BaseMsgID.DRIVER_ANSWER_ORDER, businessId);
    }

    @Override
    public void driverCancelOrder(int businessId, int reason) {
        send(BaseMsgID.DRIVER_CANCLE_ORDER, new T_DriverCancleOrder(businessId, reason));
    }

    @Override
    public void orderFinishEnsure(int businessId) {
        send(BaseMsgID.ORDER_FINISH_ENSURE, businessId);
    }

    @Override
    public void eventReport(int eventId) {
        send(BaseMsgID.EVENT_REPORT, eventId);
    }

    @Override
    public void askQuestionAns(int questionId, int answerId) {
        send(BaseMsgID.ASK_QUESTION_ANS, new T_AskQuestionAns(questionId, answerId));
    }

    @Override
    public void signInReport(T_SignInReport signInMsg) {
        send(BaseMsgID.SIGN_IN_REPORT, signInMsg);
    }

    @Override
    public void signOutReport(T_SignOutReport signOutMsg) {
        send(BaseMsgID.SIGN_OUT_REPORT, signOutMsg);
    }

    @Override
    public void operationDataReport(T_OperationDataReport operationDataReport) {
        send(BaseMsgID.OPERATION_DATA_REPORT, operationDataReport);
    }


    @Override
    public void sendWarnMsg(Location_AlarmFlag alarmFlag) {
        // TODO Auto-generated method stub
        send(new BaseMessageBean(BaseMsgID.WARN_REPORT, alarmFlag));
    }

    @Override
    public void sendStateMsg(Location_TerminalState StateFlag) {
        // TODO Auto-generated method stub
        send(new BaseMessageBean(BaseMsgID.STATE_REPORT, StateFlag));
    }

    //------------------------------------------mms-------------------------------------------------------
    @Override
    public void ARMstateSynchro(byte state) {
        sendMms(BaseMsgID.MMS_ARMSTATESYNCHRO, state);
    }

    @Override
    public void screenBacklight(byte control) {
        sendMms(BaseMsgID.MMS_SCREENBACKLIGHT, control);
    }

    @Override
    public void screenBrightness(byte brightness) {
        sendMms(BaseMsgID.MMS_SCREENBRIGHTNESS, brightness);
    }

    @Override
    public void powerAmplifier(byte control) {
        sendMms(BaseMsgID.MMS_POWERAMPLIFIER, control);
    }

    @Override
    public boolean sendCanDatas(byte[] canDatas) {
        sendMms(BaseMsgID.MMS_SENDCANDATAS, canDatas);
        return true;
    }

    @Override
    public boolean sendRS232(byte id, byte[] rs232Datas) {
        Hashtable<Byte, byte[]> ht = new Hashtable<>();
        ht.put(id, rs232Datas);
        sendMms(BaseMsgID.MMS_SENDRS232, ht);
        return true;
    }

    @Override
    public boolean sendRS485(byte id, byte[] rs485Datas) {
        Hashtable<Byte, byte[]> ht = new Hashtable<>();
        ht.put(id, rs485Datas);
        sendMms(BaseMsgID.MMS_SENDRS485, ht);
        return true;
    }

    @Override
    public boolean setBaudRate(byte id, byte baudRate) {
        Hashtable<Byte, Byte> ht = new Hashtable<>();
        ht.put(id, baudRate);
        sendMms(BaseMsgID.MMS_SETBAUDRATE, ht);
        return true;
    }

    @Override
    public boolean setIOState(byte id, byte state) {
        Hashtable<Byte, Byte> ht = new Hashtable<>();
        ht.put(id, state);
        sendMms(BaseMsgID.MMS_SETIOSTATE, ht);
        return true;
    }

    @Override
    public boolean setAccOffTime(int watiTime) {
        sendMms(BaseMsgID.MMS_SETACCOFFTIME, watiTime);
        return true;
    }

    @Override
    public boolean setWakeupFrequency(int intervalTime) {
        sendMms(BaseMsgID.MMS_SETWAKEUPFREQUENCY, intervalTime);
        return true;
    }

    @Override
    public void queryElectricity(byte type) {
        sendMms(BaseMsgID.MMS_ELECTRICITY, type);
    }

    @Override
    public void queryIOState(byte id) {
        sendMms(BaseMsgID.MMS_IOSTATE, id);
    }

    @Override
    public void queryPulseSignal() {
        sendMms(BaseMsgID.MMS_PULSESIGNAL, 0x00);
    }

    //--------------------------------------计价器---------------------------------------------------------------------
    @Override
    public void quryTaxiState(String time) {
        sendMms(BaseMsgID.TAXI_STATEQUREY, time);
    }

    @Override
    public void quryTaxiFreight() {
        sendMms(BaseMsgID.TAXI_FREIGHT, 0x00);
    }

    @Override
    public void quryTaxiHistory(int car) {
        sendMms(BaseMsgID.TAXI_HISTORY_OPERA, car);
    }

    @Override
    public void ChekTaxiClock(String time) {
        sendMms(BaseMsgID.TAXI_CLOCK, time);
    }
}
