package com.uninew.maintanence.presenter;

public interface IUpdateView {
	/**
	 * SD状态变化
	 * @param isEnable
	 */
	public void SDChange(boolean isEnable);
	
	/**
	 * apk安装是否成功
	 * @param isSuccess
	 */
	public void onApkInsatll(boolean isSuccess);

	/**
	 * 显示app版本
	 * @param version
	 */
	void ShowAppVersion(String version);

	/**
	 * 显示OS版本
	 * @param version
	 */
	void ShowOSVersion(String version);
	/**
	 * 显示Mcu版本
	 * @param version
	 */
	void ShowMcuVersion(String version);
	/**
	 * 显示摄像头版本
	 * @param version
	 */
	void ShowDvrVersion(String version);
	/**
	 * 显示空车屏版本
	 * @param version
	 */
	void ShowCarSrceenVersion(String version);
	/**
	 * 显示地图版本
	 * @param version
	 */
	void ShowMapVersion(String version);
	/**
	 * 显示高德版本
	 * @param version
	 */
	void ShowGaoDeVersion(String version);

	void onUpdateState(int state, String apk, boolean openState, boolean setLauncherState);
}
