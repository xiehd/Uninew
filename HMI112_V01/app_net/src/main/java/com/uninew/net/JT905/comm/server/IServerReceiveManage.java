package com.uninew.net.JT905.comm.server;

public interface IServerReceiveManage {


	/**
	 * 修改监听类型 true mms；false：net
	 * 默认net
	 * @param actionType
	 */
	void SetAction(boolean actionType);
	/**
	 * NET数据接收监听
	 */
	public void registerNetReceiveListener(
            IServerReceiveListener.INetReceiveListener netReceiveListener);
	
	/**
	 * 注销NET数据接收监听
	 */
	public void unRegisterNetReceiveListener();


	/**
	 * MMS数据接收监听
	 */
	public void registerMmsReceiveListener(
            IServerReceiveListener.IMmsReceiveListener mmsReceiveListener);

	/**
	 * 注销MMS数据接收监听
	 */
	public void unRegisterMmsReceiveListener();

	/**
	 * 计价器数据接收监听
	 */
	public void registerTaxiReceiveListener(
			IServerReceiveListener.ITaxiReceiveListener mTaxiReceiveListener);

	/**
	 * 注销计价器数据接收监听
	 */
	public void unRegisterTaxiReceiveListener();

}
