package com.uninew.bean;
import android.graphics.drawable.Drawable;

/**
 * 保存查询到的apk信息
 * 
 * @author lusy 创建日期:2017-1-11
 */
public class AppInfo {
	private Drawable appIcon;
	private String appName;
	private String appPackageName;
	private int newVersionCode;
	private int oldVersionCode;
	private String newVersionName;
	private String oldVersionName;
	private String apkPath;
	
	
/**
 * 
 * @param appIcon        应用图标
 * @param appName 		  应用名称
 * @param appPackageName 应用包名
 * @param newVersionCode 新versionCode
 * @param oldVersionCode 旧versionCode
 * @param newVersionName 新versionName
 * @param oldVersionName 旧versionName
 * @param apkPath        apk更新包文件路径
 */
	public AppInfo(Drawable appIcon, String appName, String appPackageName, int newVersionCode,
                   int oldVersionCode, String newVersionName, String oldVersionName, String apkPath) {
		this.appIcon = appIcon;
		this.appName = appName;
		this.appPackageName = appPackageName;
		this.newVersionCode = newVersionCode;
		this.oldVersionCode = oldVersionCode;
		this.newVersionName = newVersionName;
		this.oldVersionName = oldVersionName;
		this.apkPath = apkPath;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppPackageName() {
		return appPackageName;
	}

	public void setAppPackageName(String appPackageName) {
		this.appPackageName = appPackageName;
	}

	public int getNewVersionCode() {
		return newVersionCode;
	}

	public void setNewVersionCode(int newVersionCode) {
		this.newVersionCode = newVersionCode;
	}

	public int getOldVersionCode() {
		return oldVersionCode;
	}

	public void setOldVersionCode(int oldVersionCode) {
		this.oldVersionCode = oldVersionCode;
	}

	public String getNewVersionName() {
		return newVersionName;
	}

	public void setNewVersionName(String newVersionName) {
		this.newVersionName = newVersionName;
	}

	public String getOldVersionName() {
		return oldVersionName;
	}

	public void setOldVersionName(String oldVersionName) {
		this.oldVersionName = oldVersionName;
	}

	public String getApkPath() {
		return apkPath;
	}

	public void setApkPath(String apkPath) {
		this.apkPath = apkPath;
	}

}