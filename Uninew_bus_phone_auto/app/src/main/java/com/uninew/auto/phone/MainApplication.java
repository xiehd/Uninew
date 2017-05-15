package com.uninew.auto.phone;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.uninew.json.AutoState;
import com.uninew.utils.SPTools;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;


public class MainApplication extends Application {
	private static final String TAG = "MainApplication";
	private static ArrayList<Activity> activities;
	private static MainApplication instance;
	private static AutoState mAutoState;
	public static boolean logo = false;
	
	public static double latitude = 22.6485;
	public static double longitude = 113.52648;
	public static long time = 00000;
	public static int speed = 0;
	public static int direction = 0;
	
	private static final String LOG_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mylog/";
	private static final String LOG_NAME = getCurrentDateString() + ".txt";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e(TAG, "----onCreate-----"+System.currentTimeMillis());
		instance = this;
		// 全局异常捕获
//		 CrashHandler crashHandler = CrashHandler.getInstance();
//		 crashHandler.init(this);
		activities = new ArrayList<Activity>();
		mAutoState = new AutoState();
		sp = new SPTools(this);
		init();
		// 全局异常捕获 保存异常日志
		Thread.setDefaultUncaughtExceptionHandler(handler);
	}

	private SPTools sp;
	private void init() {
		// TODO Auto-generated method stub
		if(sp != null){
		mAutoState.setFTP_id(sp.getSharedString("ftp_id"));
		mAutoState.setFTP_user(sp.getSharedString("ftp_user"));
		mAutoState.setFTP_password(sp.getSharedString("ftp_password"));
		mAutoState.setmCurrenlineIndex(sp.getSharedInt(SPTools.ROUTE_NAME_KEY));
		}
	}


	public static AutoState getmAutoState() {
		if(mAutoState == null){
			mAutoState = new AutoState();
		}
		return mAutoState;
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
	
	//捕获崩溃日志
	UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			writeErrorLog(ex);
			exit();
		}
	};
	
	/**
	 * 打印错误日志
	 * 
	 * @param ex
	 */
	protected void writeErrorLog(Throwable ex) {
		String info = null;
		ByteArrayOutputStream baos = null;
		PrintStream printStream = null;
		try {
			baos = new ByteArrayOutputStream();
			printStream = new PrintStream(baos);
			ex.printStackTrace(printStream);
			byte[] data = baos.toByteArray();
			info = new String(data);
			data = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (printStream != null) {
					printStream.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Log.e("example", "崩溃信息\n" + info);
		File dir = new File(LOG_DIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, LOG_NAME);
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file, true);
			fileOutputStream.write(info.getBytes());
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	private static String getCurrentDateString() {
		String result = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm",
				Locale.getDefault());
		Date nowDate = new Date();
		result = sdf.format(nowDate);
		return result;
	}


}
