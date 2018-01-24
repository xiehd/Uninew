package com.uninew.net.JT905.comm.common;

public interface ConnectState {
	
	
	/**连接状态-未连接*/
	int ConnectState_UnConnected=0x00;
	/**连接状态-已连接*/
	int ConnectState_Connected=0x01;
	/**连接状态-已注册*/
	int ConnectState_Registed=0x02;
	/**连接状态-已鉴权*/
	int ConnectState_Authed=0x03;

}
