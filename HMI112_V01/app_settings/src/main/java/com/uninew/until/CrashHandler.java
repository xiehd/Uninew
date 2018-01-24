package com.uninew.until;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import com.uninew.main.SettingActivity;
import com.uninew.main.SettingsApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: CrashHandler Function:
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 * 
 * @author Norris Norris.sly@gmail.com
 * @version
 * @since Ver 1.0 I used to be a programmer like you, then I took an arrow in
 *        the knee
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	/**
	 * Log日志的tag String : TAG
	 * 
	 */
	// private static final String TAG = "NorrisInfo" ;
	/**
	 * 系统默认的UncaughtException处理类 Thread.UncaughtExceptionHandler :
	 * mDefaultHandler
	 * 
	 */
	private UncaughtExceptionHandler mDefaultHandler;
	/**
	 * CrashHandler实例 CrashHandler : mInstance
	 * 
	 */
	private static CrashHandler mInstance = new CrashHandler();
	/**
	 * 程序的Context对象 Context : mContext
	 * 
	 */
	private Context mContext;
	/**
	 * 用来存储设备信息和异常信息 Map<String,String> : mLogInfo
	 * 
	 */
	private Map<String, String> mLogInfo = new HashMap<String, String>();
	/**
	 * 用于格式化日期,作为日志文件名的一部分(FIXME 注意在windows下文件名无法使用：等符号！) SimpleDateFormat :
	 * mSimpleDateFormat
	 * 
	 */
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");

	/**
	 * Creates a new instance of CrashHandler.
	 */
	private CrashHandler() {
	}

	/**
	 * getInstance:{获取CrashHandler实例 ,单例模式 } ──────────────────────────────────
	 * 
	 * @return CrashHandler
	 * @throws
	 * @since I used to be a programmer like you, then I took an arrow in the
	 *        knee　Ver 1.0
	 *        ──────────────────────────────────────────────────────
	 *        ──────────────────────────────────────────────── *
	 * 
	 */
	public static CrashHandler getInstance() {
		return mInstance;
	}

	/**
	 * init:{初始化} ──────────────────────────────────
	 * 
	 * @param paramContext
	 * @return void
	 * @throws
	 * @since I used to be a programmer like you, then I took an arrow in the
	 *        knee　Ver 1.0
	 * 
	 * 
	 */
	public void init(Context paramContext) {
		mContext = paramContext;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该重写的方法来处理 (non-Javadoc)
	 * 
	 * @see UncaughtExceptionHandler#uncaughtException(Thread,
	 *      Throwable)
	 */
	@SuppressWarnings("static-access")
	public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
		if (!handleException(paramThrowable) && mDefaultHandler != null) {
			// 如果自定义的没有处理则让系统默认的异常处理器来处理
			Log.e("logError", "异常没有处理，让系统默认的异常处理器来处理");
			mDefaultHandler.uncaughtException(paramThread, paramThrowable);
		} else {
			try {
				// 如果处理了，让程序继续运行1秒再退出，保证文件保存并上传到服务器
				Log.e("logError", "异常处理了，让程序继续运行1秒再退出，保证文件保存并上传到服务器");
				paramThread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Log.e("logError", "异常处理了，开启服务");
			Intent intent = new Intent(mContext, SettingActivity.class);
            PendingIntent restartIntent = PendingIntent.getActivity(    
            		mContext, 0, intent,    
                    Intent.FLAG_ACTIVITY_NEW_TASK);                                                 
            //退出程序                                          
            AlarmManager mgr = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);    
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,    
                    restartIntent); // 1秒钟后重启应用   
            SettingsApplication.getInstance().exit();
		}
	}

	private static final String DIR_FILE = "/HMI112CrashInfo/Settings";
	private File exceptionFile, fileDir;

	/** 创建存储目录 */
	private void createFile() {
		if (!fileDir.exists())
			fileDir.mkdirs();
		exceptionFile = new File(fileDir, "HMI112_Exception_settings.txt");
		if (!exceptionFile.exists()) {
			try {
				exceptionFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * handleException:{自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.}
	 * 
	 * @param paramThrowable
	 * @return true:如果处理了该异常信息;否则返回false.
	 * @throws
	 * @since I used to be a programmer like you, then I took an arrow in the
	 *        knee　Ver 1.0
	 * 
	 * 
	 * 
	 */
	public boolean handleException(Throwable paramThrowable) {
		if (paramThrowable == null)
			return false;
		new Thread() {
			public void run() {
				Looper.prepare();
				// Toast.makeText(mContext , "很抱歉,程序出现异常,即将退出" , 0) ;
				Looper.loop();
			}
		}.start();
		// 判断u盘是否挂载
		/*	if (UsbDiskUtil.isUsbDiskMounted()) {
			fileDir = new File(Configuration.getUsbMediaRootPath()
																 * + File.
																 * pathSeparator
																 + DIR_FILE);
			createFile();
			saveData(paramThrowable);
			// 判断sd卡是否存在
		} */
		
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			fileDir = new File(Environment.getExternalStorageDirectory()
			/* + File.pathSeparator */,DIR_FILE);
			createFile();
			saveData(paramThrowable);
		}

		return true;
	}

	private void saveData(Throwable paramThrowable) {
		// 获取设备参数信息
		getDeviceInfo(mContext);
		// 保存日志文件
		saveCrashLogToFile(paramThrowable);
	}

	/**
	 * getDeviceInfo:{获取设备参数信息} ──────────────────────────────────
	 * 
	 * @param paramContext
	 * @throws
	 * @since I used to be a programmer like you, then I took an arrow in the
	 *        knee　Ver 1.0
	 * 
	 * 
	 */
	public void getDeviceInfo(Context paramContext) {
		try {
			// 获得包管理器
			PackageManager mPackageManager = paramContext.getPackageManager();
			// 得到该应用的信息，即主Activity
			PackageInfo mPackageInfo = mPackageManager.getPackageInfo(paramContext.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (mPackageInfo != null) {
				String versionName = mPackageInfo.versionName == null ? "null" : mPackageInfo.versionName;
				String versionCode = mPackageInfo.versionCode + "";
				mLogInfo.put("versionName", versionName);
				mLogInfo.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		// 反射机制
		Field[] mFields = Build.class.getDeclaredFields();
		// 迭代Build的字段key-value 此处的信息主要是为了在服务器端手机各种版本手机报错的原因
		for (Field field : mFields) {
			try {
				field.setAccessible(true);
				mLogInfo.put(field.getName(), field.get("").toString());
				// Log.d(TAG , field.getName() + ":" + field.get("")) ;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * saveCrashLogToFile:{将崩溃的Log保存到本地} TODO 可拓展，将Log上传至指定服务器路径
	 * ──────────────────────────────────
	 * 
	 * @param paramThrowable
	 * @return FileName
	 * @throws
	 * @since I used to be a programmer like you, then I took an arrow in the
	 *        knee　Ver 1.0
	 * 
	 */
	private String saveCrashLogToFile(Throwable paramThrowable) {
		StringBuffer mStringBuffer = new StringBuffer();
		mStringBuffer.append("\r\n exception date:");
		mStringBuffer.append(new Date(System.currentTimeMillis()).toLocaleString() + "\r\n");
		for (Map.Entry<String, String> entry : mLogInfo.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			mStringBuffer.append(key + "=" + value + "\r\n");
		}
		Writer mWriter = new StringWriter();
		PrintWriter mPrintWriter = new PrintWriter(mWriter);
		paramThrowable.printStackTrace(mPrintWriter);
		Throwable mThrowable = paramThrowable.getCause();
		// 迭代栈队列把所有的异常信息写入writer中
		while (mThrowable != null) {
			mThrowable.printStackTrace(mPrintWriter);
			// 换行 每个个异常栈之间换行
			mPrintWriter.append("\r\n");
			mThrowable = mThrowable.getCause();
		}
		// 记得关闭
		mPrintWriter.close();
		String mResult = mWriter.toString();
		mStringBuffer.append(mResult);
		// 保存文件，设置文件名
		try {
			FileOutputStream mFileOutputStream = new FileOutputStream(exceptionFile, true);

			mFileOutputStream.write(mStringBuffer.toString().getBytes());
			mFileOutputStream.flush();
			mFileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * String mTime = mSimpleDateFormat.format(new Date()) ; String
		 * mFileName = "CrashBaiDuMusicLog-" + mTime + ".log" ;
		 * if(Environment.getExternalStorageState
		 * ().equals(Environment.MEDIA_MOUNTED)) { try { File mDirectory = new
		 * File(Environment.getExternalStorageDirectory() + "/CrashBaiDuMusics")
		 * ; // Log.v(TAG , mDirectory.toString()) ; if( ! mDirectory.exists())
		 * mDirectory.mkdir() ; FileOutputStream mFileOutputStream = new
		 * FileOutputStream(mDirectory + "/" + mFileName) ;
		 * mFileOutputStream.write(mStringBuffer.toString().getBytes()) ;
		 * mFileOutputStream.close() ; return mFileName ; }
		 * catch(FileNotFoundException e) { e.printStackTrace() ; }
		 * catch(IOException e) { e.printStackTrace() ; } }
		 */
		return null;
	}
}