package com.uninew.net.JT905.comm.server;

import android.content.Context;
import android.content.Intent;

import com.uninew.net.JT905.bean.P_AskQuestion;
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
import com.uninew.net.Taximeter.bean.P_TaxiFreight;
import com.uninew.net.Taximeter.bean.P_TaxiOperationDataReport;
import com.uninew.net.Taximeter.bean.T_HeartBeat;

import java.util.Hashtable;

public class ServerSendManage implements IServerSendManage {

    private Context mContext;

    public ServerSendManage(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public void send(BaseMessageBean baseMsg) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(DefineNetAction.NETLinkProtocolServer);
        intent.putExtra(DefineNetAction.Key_Object, baseMsg);
        mContext.sendBroadcast(intent);
    }

    private void send(int msgId, Object object) {
        BaseMessageBean baseMsg = new BaseMessageBean(msgId, object);
        send(baseMsg);
    }

    @Override
    public void sendMms(BaseMessageBean baseMsg) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(DefineNetAction.MMSLinkProtocolServer);
        intent.putExtra(DefineNetAction.Key_Object, baseMsg);
        mContext.sendBroadcast(intent);
    }

    private void sendMms(int msgId, Object object) {
        BaseMessageBean baseMsg = new BaseMessageBean(msgId, object);
        sendMms(baseMsg);
    }

    @Override
    public void linkStateNotify(IpInfo ipInfo) {
        // TODO Auto-generated method stub
        send(BaseMsgID.CONNECTSTATE_RESPONSE, ipInfo);
    }

    @Override
    public void orderSendDown(P_OrderSendDown orderMsg) {
        send(BaseMsgID.ORDER_SEND_DOWN, orderMsg);
    }

    @Override
    public void answerOrderResponse(int result) {
        send(BaseMsgID.DRIVER_ANSWER_ORDER_RESPONSE, result);
    }

    @Override
    public void answerOrderMsg(P_DriverAnswerOrderAns answerResult) {
        send(BaseMsgID.DRIVER_ANSWER_ORDER_MSG, answerResult);
    }

    @Override
    public void platformcancelOrder(int businessId) {
        send(BaseMsgID.PLATFORM_CANCLE_ORDER, businessId);
    }

    @Override
    public void driverCancelOrderAns(int cancelResult) {
        send(BaseMsgID.DRIVER_CANCLE_ORDER_ANS, cancelResult);
    }

    @Override
    public void orderFinishEnsureAns(int ensureReuslt) {
        send(BaseMsgID.ORDER_FINISH_ENSURE_ANS, ensureReuslt);
    }

    @Override
    public void textSendDown(P_TextIssued textMsg) {
        send(BaseMsgID.TEXT_MSG_DOWNLOAD, textMsg);
    }

    @Override
    public void eventSet(P_EventSet eventMsg) {
        send(BaseMsgID.EVENT_SET, eventMsg);
    }

    @Override
    public void askQuestion(P_AskQuestion questionMsg) {
        send(BaseMsgID.ASK_QUESTION, questionMsg);
    }

    @Override
    public void callBack(P_Callback callbackMsg) {
        send(BaseMsgID.CALL_BACK, callbackMsg);
    }

    @Override
    public void setPhoneBook(P_PhoneBookSet phoneBookMsg) {
        send(BaseMsgID.SET_PHONE_BOOK, phoneBookMsg);
    }

    @Override
    public void signInReportAns(int result) {
        send(BaseMsgID.SIGN_IN_ANS, result);
    }

    @Override
    public void signOutReportAns(int result) {
        send(BaseMsgID.SIGN_OUT_ANS, result);
    }

    @Override
    public void operationDataReportAns(int result) {
        send(BaseMsgID.OPERATION_DATA_REPORT, result);
    }

    //------------------------------MMS--------------------------------------------------------------
    @Override
    public boolean systemStateNotify(byte state) {
        return false;
    }

    @Override
    public void mcuVersionNotify(String version) {

    }

    @Override
    public boolean electricity(byte type, byte electricity) {
        return false;
    }

    @Override
    public void handleMcuKey(byte key, byte action) {

    }

    @Override
    public boolean receiveCanDatas(byte[] canDatas) {
        return false;
    }

    @Override
    public boolean receiveRS232(byte id, byte[] rs232Datas) {
        return false;
    }

    @Override
    public boolean receiveRS485(byte id, byte[] rs485Datas) {
        return false;
    }

    @Override
    public boolean receiveBaudRate(byte id, byte baudRate) {
        return false;
    }

    @Override
    public boolean receiveIOState(byte id, byte state) {
        return false;
    }

    @Override
    public boolean receivePulseSignal(int speed) {
        return false;
    }


    //----------------------------------------------计价器--------------------------------------------------------------------
    @Override
    public void quryTaxiStateResponse(int deviceState, int taxiState, String deviceNumber, String carNumber, int oprateNumber) {
        Hashtable<String, Object> hashtable = new Hashtable<>();
        hashtable.put(ActionDefine.TaxiKey.key_deviceState, deviceState);
        hashtable.put(ActionDefine.TaxiKey.key_taxiState, taxiState);
        hashtable.put(ActionDefine.TaxiKey.key_deviceNumber, deviceNumber);
        hashtable.put(ActionDefine.TaxiKey.key_carNUmber, carNumber);
        hashtable.put(ActionDefine.TaxiKey.key_oprateNumber, oprateNumber);
        sendMms(BaseMsgID.TAXI_STATEQUREY, hashtable);
    }

    @Override
    public void OperteStart(String time) {
        sendMms(BaseMsgID.TAXI_OPERATE_STATR, time);
    }

    @Override
    public void OperteEnd(int type, P_TaxiOperationDataReport mP_TaxiOperationDataReport) {
        mP_TaxiOperationDataReport.setId_type(type);
        if (type == 0x00) {
            sendMms(BaseMsgID.TAXI_OPERATE_END, mP_TaxiOperationDataReport);
        } else if (type == 0x01) {//补传
            sendMms(BaseMsgID.TAXI_OPERATE_END, mP_TaxiOperationDataReport);
        }
    }

    @Override
    public void TaxiHeartState(T_HeartBeat mT_HeartBeat) {
        sendMms(BaseMsgID.TAXI_HEART, mT_HeartBeat);
    }

    @Override
    public void quryTaxiFreightResponse(P_TaxiFreight mP_TaxiFreight) {
        sendMms(BaseMsgID.TAXI_FREIGHT, mP_TaxiFreight);
    }


}
