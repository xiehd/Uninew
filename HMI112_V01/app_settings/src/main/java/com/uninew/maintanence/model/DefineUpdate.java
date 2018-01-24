package com.uninew.maintanence.model;

public interface DefineUpdate {
	
	/**
	 * apk升级请求
	 */
	String ApkLinkUpdateRequest = "Com.NETLink.Update";
	/**
	 * apk升级应答
	 */
	String ApkLinkUpdateResponse="Com.NETLink.Update.Back";
	

	/** 查询应答结果：无SD卡 */
	int RquestResult_NoSDCard = 0x00;
	/** 查询应答结果：无升级文件 */
	int RquestResult_NoFile = 0x01;
	/** 查询应答结果：有升级文件 */
	int RquestResult_HaveFile = 0x02;

	/** 应用名称 */
	String Key_ApkName = "apkName";
	/** 新apk包名 */
	String Key_NewApkPkg = "newApkPkg";
	/** 当前apk包名 */
	String Key_OldApkPkg = "currentApkPkg";
	/** 新版本号 */
	String Key_NewVersion = "newVersion";
	/** 当前版本号 */
	String Key_OldVersion = "currentVersion";
	/** 升级文件绝对路径 */
	String Key_FileAbsolutePath = "fileAbsolutePath";
	/**
	 * 旧versionCode
	 */
	String key_oldVersionCode="oldVersionCode";
	/**
	 * 新versionCode
	 */
	String key_newVersionCode="newVersionCode";
	
	/**
	 * apk路径
	 */
	String KEY_PATH="path";
	/**
	 * 升级类型
	 */
	String KEY_TYPE="type";
	/**
	 * 接收apk安装结果 0:失败,1:成功
	 */
	String KEY_APKINSTALL_RESULT="Back";

}
