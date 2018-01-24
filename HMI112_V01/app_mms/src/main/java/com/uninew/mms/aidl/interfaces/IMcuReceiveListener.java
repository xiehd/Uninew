package com.uninew.mms.aidl.interfaces;

/**
 *
 * 采用监听的方式进行数据的接收， 发送则是直接调用接口中的方法。 以下为接收监听接口类：
 */
public interface IMcuReceiveListener {

	//系统状态通知
	boolean systemStateNotify(byte state);

	//MCU 版本号
	void mcuVersionNotify(String version);

	//电池电量信息 type： 0x00-蓄电池， 0x01-自带电池电量
	boolean electricity(byte type,byte electricity);

	//物理按键通知
	void handleMcuKey(byte key, byte action);

	//CAN 数据透传上报
	boolean receiveCanDatas(byte[] canDatas);

	//RS232 数据透传,id(0x01:RS232_1,0x02:RS232_2,0x03:RS232_3)
	boolean receiveRS232(byte id,byte[] rs232Datas);

	//RS485 数据透传
	boolean receiveRS485(byte id,byte[] rs485Datas);

	//波特率设置信息
	//id(0x00:RS485,0x01:RS232_1,0x02:RS232_2,0x03:RS232_3)
	boolean receiveBaudRate(byte id,byte baudRate);

	//IO 输出信号
	boolean receiveIOState(byte id,byte state);

	//接收脉冲测速信号
	boolean receivePulseSignal(int speed);
}