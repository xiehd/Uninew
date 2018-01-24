package com.uninew.mms;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;
import com.uninew.net.audio.TtsUtil;


public class MainApplication extends Application {
	private static MainApplication mainAPP;
	public static String mcu_version = "";

	public MainApplication() {
		mainAPP = this;
	}

	public static MainApplication getInstance() {
		if (mainAPP == null) {
			return new MainApplication();
		}
		return mainAPP;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 全局异常捕获
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(this);
		SpeechUtility.createUtility(getApplicationContext(), "appid="
				+ "53ed5edd");
		new TtsUtil(this.getApplicationContext()).speak("");
	}
	// finish
	public void exit() {
		// 杀死该应用进程
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
