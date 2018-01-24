package com.uninew.mms.interfaces;


public interface IProtocolPacket {
	/**
	 * 协议封装
	 * @return 协议数据
	 */
	public byte[] getBytes();
	
	/**
	 * 协议解析
	 * @param datas
	 * @return 协议对象
	 */
	public Object getProtocolPacket(byte[] datas);
}
