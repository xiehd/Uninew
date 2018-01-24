package com.uninew.car;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.iflytek.cloud.SpeechUtility;
import com.uninew.car.audio.TtsUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public class MainApplication extends Application {
	private static final String TAG = "MainApplication";
	private static ArrayList<Activity> activities = new ArrayList<>();
	private static MainApplication instance;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e(TAG, "----onCreate-----"+System.currentTimeMillis());

		//全局异常捕获
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(this);

		startService();
		instance = this;
        init();
	}

    private void init() {
        // /** 语音播报初始化 */
        SpeechUtility.createUtility(getApplicationContext(), "appid="
                + getString(R.string.app_id));
        TtsUtil.getInstance(this).speak("");

    }

	/**
	 * 开启服务
	 */
	private void startService() {
		// TODO Auto-generated method stub
		Log.d(TAG, "-------------startservice---------------");
		Intent netService = new Intent();
		netService.setAction("com.JT905.main.LinkService");
		netService.setPackage("com.uninew.net");
		startService(netService);

		Intent mainService = new Intent();
		mainService.setAction("com.uninew.service.MainService");
		mainService.setPackage("com.uninew.car");
		startService(mainService);
	}

	// 单例模式中获取唯一的MyApplication实例
	public static MainApplication getInstance() {
		if (null == instance) {
			instance = new MainApplication();
		}
		return instance;
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activities.add(activity);
	}

	public void deleteActivity(Activity activity) {
		activities.remove(activity);
	}
	// finish
	public void exit() {
		for (Activity activity : activities) {
			activity.finish();
		}
		activities.clear();
		// 杀死该应用进程
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}
