package com.uninew.maintanence.presenter;


public interface IUpdate {

	/**
	 * apk升级方法
	 * 
	 * @param apkAbsolutePath
	 *            绝对路径
	 * @param resultCallBack
	 *            升级结果回调接口
	 */
	public void updateApk(String apkAbsolutePath, IResultCallBack resultCallBack);

	/**
	 * OS升级方法
	 * 
	 * @param osAbsolutePath
	 * @param resultCallBack
	 */
	public void updateOS(String osAbsolutePath, IResultCallBack resultCallBack);
	
	/**
	 * 根据规则查询制定的文件
	 * @param fileStart 文件开始字符,如：MCU_
	 * @param fileEnd 文件结束字符，如：.apk
	 * @param apkMsgsCallBack 回调信息 
	 */
	public void findMsgsFromSD(String fileStart, String fileEnd, IMsgsCallBack apkMsgsCallBack);

	/**
	 * 版本比较
	 * @param version1
	 * @param version2
	 * @return -1:version1 < version2;0:Version1 = version2;1:Version1 > version2;
	 */
	public int compareVersion(String version1, String version2);

}
