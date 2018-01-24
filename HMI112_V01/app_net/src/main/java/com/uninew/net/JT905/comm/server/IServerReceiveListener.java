package com.uninew.net.JT905.comm.server;


import com.uninew.net.JT905.bean.BaseBean;
import com.uninew.net.JT905.bean.Location_AlarmFlag;
import com.uninew.net.JT905.bean.Location_TerminalState;
import com.uninew.net.JT905.bean.T_AskQuestionAns;
import com.uninew.net.JT905.bean.T_DriverCancleOrder;
import com.uninew.net.JT905.bean.T_OperationDataReport;
import com.uninew.net.JT905.bean.T_SignInReport;
import com.uninew.net.JT905.bean.T_SignOutReport;
import com.uninew.net.JT905.common.IpInfo;

public interface IServerReceiveListener {

    public interface INetReceiveListener {
        /**
         * 连接平台
         *
         * @param ipInfo
         */
        void createLink(IpInfo ipInfo);

        /**
         * 断开平台连接
         *
         * @param tcpId
         */
        void disConnectLink(int tcpId);

        /**
         * 连接状态查询
         *
         * @param tcpId
         */
        void queryLinkState(int tcpId);

        /**
         * 驾驶员抢答
         *
         * @param businessId
         */
        void driverAnswerOrder(int businessId);

        /**
         * 驾驶员取消订单
         *
         * @param driverCancleOrder
         */
        void driverCancelOrder(T_DriverCancleOrder driverCancleOrder);

        /**
         * 电召任务完成确认
         *
         * @param businessId
         */
        void orderFinishEnsure(int businessId);

        /**
         * 上传运营数据
         *
         * @param operationDataReport
         */
        void operationDataReport(T_OperationDataReport operationDataReport);

        /**
         * 事件报告
         *
         * @param eventId
         */
        void eventReport(int eventId);

        /**
         * 提问应答
         *
         * @param askQuestionAns
         */
        void askQuestionAns(T_AskQuestionAns askQuestionAns);

        /**
         * 签到上报
         *
         * @param signInMsg
         */
        void signInReport(T_SignInReport signInMsg);

        /**
         * 签退上报
         *
         * @param signOutMsg
         */
        void signOutReport(T_SignOutReport signOutMsg);

        /**
         * 发送报警消息：超速、预超速、疲劳驾驶、危险报警
         *
         * @param alarmFlag
         */
        void sendWarnMsg(Location_AlarmFlag alarmFlag);

        /**
         * 发状态变化消息:ACC开关，定位状态，车灯状态变化。
         *
         * @param StateFlag
         */
        void sendStateMsg(Location_TerminalState StateFlag);

        void sendMsg(BaseBean msg);
    }

    //---------------------------------------MMS----------------------------------
    public interface IMmsReceiveListener {

        //ARM 状态同步
        void ARMstateSynchro(byte state);

        //屏幕背光开关控制 control(0： 关闭， 1： 开背光)
        void screenBacklight(byte control);

        //屏幕亮度调节
        void screenBrightness(byte brightness);

        //功放开关控制
        void powerAmplifier(byte control);

        //CAN 数据下发
        boolean sendCanDatas(byte[] canDatas);

        //RS232 数据透传,id(0x01:RS232_1,0x02:RS232_2,0x03:RS232_3)
        boolean sendRS232(byte id, byte[] rs232Datas);

        //RS485 数据透传
        boolean sendRS485(byte id, byte[] rs485Datas);

        //波特率设置
        //id(0x00:RS485,0x01:RS232_1,0x02:RS232_2,0x03:RS232_3)
        boolean setBaudRate(byte id, byte baudRate);

        //IO 输入信号
        boolean setIOState(byte id, byte state);

        //ACC OFF 后等待关机时间
        boolean setAccOffTime(int watiTime);

        //ARM 唤醒频率
        boolean setWakeupFrequency(int intervalTime);

        //查询 电池电量信息 type： 0x00-蓄电池， 0x01-自带电池电量
        void queryElectricity(byte type);

        //查询 IO输出信号的值
        void queryIOState(byte id);

        //查询 脉冲测速信号信息
        void queryPulseSignal();
    }

    //----------------------------------------------计价器---------------------------------------------------------
    public interface ITaxiReceiveListener {
        /**
         * 计价器状态查询
         * @param time ISU当前时间 ：YYYYMMDDhhmmss
         */
        void quryTaxiState(String time);

        /**
         * 计价器运价参数查询
         */
        void quryTaxiFreight();

        /**
         * 查询历史运营数据
         * @param car 运营车次
         */
        void quryTaxiHistory(int car);

        /**
         * 永久时钟校验
         * @param time YYYYMMDDhhmmss
         */
        void ChekTaxiClock(String time);
    }
}
