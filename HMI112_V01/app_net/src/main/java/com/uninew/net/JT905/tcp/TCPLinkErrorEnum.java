package com.uninew.net.JT905.tcp;

/**
 * 服务器状态
 *
 */
public enum TCPLinkErrorEnum {
	/**
	 * 正常
	 */
	NORMAL,
	/**
	 * 无网络 
	 * */
	ERROR_NO_NET,
	
	/**
	 * 人为关闭
	 */
	ERROR_HAND_CLOSED,
	
	/**
	 * 服务器断开监听
	 * */
	ERROR_SERVER_CLOSED,
	
	/**
	 * IP或者端口号错误
	 *  */
	ERROR_IPORPORT,
	/**
	 * 连接服务器失败，服务器拒绝或者没有监听该端口
	 *  */
	ERROR_CONNECT,
	/**
	 * 未知异常
	 */
	ERROR_OTHERS,
	/////////////////////////////////串口返回状态////////////////////////////////////
	
}
