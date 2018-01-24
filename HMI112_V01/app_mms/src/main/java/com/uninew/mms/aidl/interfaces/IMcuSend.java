package com.uninew.mms.aidl.interfaces;

/**
 * 发送接口类
 */
public interface IMcuSend {

    //ARM 状态同步
    void ARMstateSynchro( byte state);

    //屏幕背光开关控制 control(0： 关闭， 1： 开背光)
    void screenBacklight( byte control);

    //屏幕亮度调节
    void screenBrightness( byte brightness);

    //功放开关控制
    void powerAmplifier( byte control);

    //CAN 数据下发
    boolean sendCanDatas(byte[] canDatas);

    //RS232 数据透传,id(0x01:RS232_1,0x02:RS232_2,0x03:RS232_3)
    boolean sendRS232(byte id,byte[] rs232Datas);

    //RS485 数据透传
    boolean sendRS485(byte id,byte[] rs485Datas);

    //波特率设置
    //id(0x00:RS485,0x01:RS232_1,0x02:RS232_2,0x03:RS232_3)
    boolean setBaudRate(byte id,byte baudRate);

    //IO 输入信号
    boolean setIOState(byte id,byte state);

    //ACC OFF 后等待关机时间
    boolean setAccOffTime( int watiTime);

    //ARM 唤醒频率
    boolean setWakeupFrequency( int intervalTime);
    //查询 电池电量信息 type： 0x00-蓄电池， 0x01-自带电池电量
    void queryElectricity(byte type);

    //查询 IO输出信号的值
    void queryIOState(byte id);

    //查询 脉冲测速信号信息
    void queryPulseSignal();
}