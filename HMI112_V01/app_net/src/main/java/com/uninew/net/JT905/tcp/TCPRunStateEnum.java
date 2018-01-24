package com.uninew.net.JT905.tcp;
/**
 * TCP状态
 *
 */
public enum TCPRunStateEnum {
	/**
	 * TCP打开成功
	 */
	OPEN_SUCCESS,
	
	/**
	 * TCP打开失败
	 */
	OPEN_FAILURE,
	
	/**
	 * TCP运行停止
	 */
	RUN_STOPED,
	
	/**平台手动断开成功*/
	DISCONNECT_PLATFORM_SUCCESS,
}
