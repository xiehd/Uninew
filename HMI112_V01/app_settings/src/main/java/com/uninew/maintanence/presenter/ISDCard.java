package com.uninew.maintanence.presenter;

public interface ISDCard {

	/**
	 * 监听SD状态变化
	 */
	public void registerListener();
	
	/**
	 * 注销监听SD变化
	 */
	public void unRegisterListener();
	
	/**
	 * 查询SD卡是否可用
	 * @return
	 */
	boolean isSdcardEnable();
	
}
