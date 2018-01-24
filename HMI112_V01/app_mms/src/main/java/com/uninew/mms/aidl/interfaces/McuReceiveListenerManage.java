package com.uninew.mms.aidl.interfaces;


public class McuReceiveListenerManage implements IMcuReceiveListener {


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

}
