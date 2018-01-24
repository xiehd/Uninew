
package com.uninew.maintanence.presenter;
/***********************************************************************
 * Module:  IUpdatePassenger.java
 * Author:  Administrator
 * Purpose: Defines the Interface IUpdatePassenger
 ***********************************************************************/


/** @pdOid 9e5eb772-5247-4e06-b98f-38ccd6edd4fd */
public interface IUpdatePresenter {
	/**
	 * apk升级请求
	 */
	public void updateApkRequest(IUpdateResultCallBack mCallBack);

	/**
	 * OS升级请求
	 */
	public void updateOsRequest(IUpdateResultCallBack mCallBack);
	
	/**
	 * MCU升级请求
	 */
	public void updateMcuRequest(IUpdateResultCallBack mCallBack);


	/**
	 * apk升级方法
	 * 
	 * @param apkAbsolutePath
	 *            绝对路径
	 *            升级结果回调接口
	 */
	public void updateApk(String apkAbsolutePath);

	/**
	 * OS升级方法
	 * 
	 * @param osAbsolutePath
	 * @param resultCallBack
	 */
	public void updateOS(String osAbsolutePath, IResultCallBack resultCallBack);
	
	/**
	 * 升级MCU
	 */
	public void updateMcu(String mcuAbsolutePath, IResultCallBack resultCallBack);

	/**
	 * SD卡是否存在
	 */
	public boolean isSdCardEnable();
	
	/**
	 * 监听SD状态变化
	 */
	public void registerListener();
	
	/**
	 * 注销监听SD变化
	 */
	public void unRegisterListener();

}