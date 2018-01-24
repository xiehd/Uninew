package com.uninew.net.JT905.comm.server;

import com.uninew.net.JT905.bean.P_AskQuestion;
import com.uninew.net.JT905.bean.P_Callback;
import com.uninew.net.JT905.bean.P_DriverAnswerOrderAns;
import com.uninew.net.JT905.bean.P_EventSet;
import com.uninew.net.JT905.bean.P_OrderSendDown;
import com.uninew.net.JT905.bean.P_PhoneBookSet;
import com.uninew.net.JT905.bean.P_TextIssued;
import com.uninew.net.JT905.comm.common.BaseMessageBean;
import com.uninew.net.JT905.common.IpInfo;
import com.uninew.net.Taximeter.bean.P_TaxiFreight;
import com.uninew.net.Taximeter.bean.P_TaxiOperationDataReport;
import com.uninew.net.Taximeter.bean.T_HeartBeat;


public interface IServerSendManage {

    /**
     * 通用net发送方法
     *
     * @param baseMsg
     */
    void send(BaseMessageBean baseMsg);

    /**
     * 通用mms发送方法
     *
     * @param baseMsg
     */
    void sendMms(BaseMessageBean baseMsg);

    /**
     * 发送连接状态信息
     *
     * @param ipInfo 0-未连接，1-已连接，2-已注册，3-已鉴权
     */
    void linkStateNotify(IpInfo ipInfo);

    /**
     * 订单任务下发
     *
     * @param orderMsg
     */
    void orderSendDown(P_OrderSendDown orderMsg);

    /**
     * 抢答结果
     *
     * @param result
     */
    void answerOrderResponse(int result);

    /**
     * 抢答成功消息
     *
     * @param answerResult
     */
    void answerOrderMsg(P_DriverAnswerOrderAns answerResult);

    /**
     * 平台取消订单
     *
     * @param businessId
     */
    void platformcancelOrder(int businessId);

    /**
     * 司机取消订单应答
     *
     * @param cancelResult
     */
    void driverCancelOrderAns(int cancelResult);

    /**
     * 电召任务完成确认应答
     *
     * @param ensureReuslt 0-成功，1-失败
     */
    void orderFinishEnsureAns(int ensureReuslt);

    /**
     * 文本下发
     *
     * @param textMsg
     */
    void textSendDown(P_TextIssued textMsg);

    /**
     * 事件设置
     *
     * @param eventMsg
     */
    void eventSet(P_EventSet eventMsg);

    /**
     * 提问下发
     *
     * @param questionMsg
     */
    void askQuestion(P_AskQuestion questionMsg);

    /**
     * 电话回拨
     *
     * @param callbackMsg
     */
    void callBack(P_Callback callbackMsg);

    /**
     * 设置电话本
     *
     * @param phoneBookMsg
     */
    void setPhoneBook(P_PhoneBookSet phoneBookMsg);

    /**
     * 上班签到应答
     *
     * @param result
     */
    void signInReportAns(int result);

    /**
     * 上班签退应答
     *
     * @param result
     */
    void signOutReportAns(int result);

    /**
     * 上传运营数据结果
     *
     * @param result
     */
    void operationDataReportAns(int result);


    //------------------------------mms---------------------------------------------

    //系统状态通知
    boolean systemStateNotify(byte state);

    //MCU 版本号
    void mcuVersionNotify(String version);

    //电池电量信息 type： 0x00-蓄电池， 0x01-自带电池电量
    boolean electricity(byte type, byte electricity);

    //物理按键通知
    void handleMcuKey(byte key, byte action);

    //CAN 数据透传上报
    boolean receiveCanDatas(byte[] canDatas);

    //RS232 数据透传,id(0x01:RS232_1,0x02:RS232_2,0x03:RS232_3)
    boolean receiveRS232(byte id, byte[] rs232Datas);

    //RS485 数据透传
    boolean receiveRS485(byte id, byte[] rs485Datas);

    //波特率设置信息
    //id(0x00:RS485,0x01:RS232_1,0x02:RS232_2,0x03:RS232_3)
    boolean receiveBaudRate(byte id, byte baudRate);

    //IO 输出信号
    boolean receiveIOState(byte id, byte state);

    //接收脉冲测速信号
    boolean receivePulseSignal(int speed);

    //--------------------------------------------------------计价器---------------------------------------------------------

    /**
     * 计价器状态查询应答
     *
     * @param deviceState  设备状态
     * @param taxiState    计价器工作状态
     * @param deviceNumber 设备编号
     * @param carNUmber    车牌号
     * @param oprateNumber 运营总次数
     */
    void quryTaxiStateResponse(int deviceState, int taxiState, String deviceNumber, String carNUmber, int oprateNumber);

    /**
     * 运营开始（进入重车）
     *
     * @param time
     */
    void OperteStart(String time);

    /**
     * 运营结束（进入空车）
     *
     * @param type                       0x00:当班运营数据上传；0x01:运营数据补传
     * @param mP_TaxiOperationDataReport
     */
    void OperteEnd(int type, P_TaxiOperationDataReport mP_TaxiOperationDataReport);

    /**
     * 计价器心跳状态
     *
     * @param mT_HeartBeat
     */
    void TaxiHeartState(T_HeartBeat mT_HeartBeat);

    /**
     * 运价参数查询应答
     *
     * @param mP_TaxiFreight
     */
    void quryTaxiFreightResponse(P_TaxiFreight mP_TaxiFreight);

}
