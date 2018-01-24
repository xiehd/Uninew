package com.uninew.net.JT905.common;

import android.util.Log;

public class LogTool {
//	static File logf = new File(Environment.getExternalStorageDirectory()  
//            + File.separator + "HMI106Log"+ File.separator+"HMI106Log_"+TimeTool.timestampTormat2(TimeTool.getCurrentTimestamp())+".txt");  
//	static File logf=new File("mnt/sdcard/HMI106/"+"HMI106Log_"+ TimeTool.timestampTormat2(TimeTool.getCurrentTimestamp())+".txt");
//	private static LogWriter mLogWriter;
//	private static boolean isWrite=false;
         
       
	public static final String TAG = "LogTool";
	
	public static void writeToFile(String msg) {
//		if (isWrite==false) {
//			return;
//		}
//		if (mLogWriter == null) {
//			try {
//				logf.getParentFile().mkdirs();
//		        mLogWriter = LogWriter.open(logf.getAbsolutePath());
//		    } catch (IOException e) {
//		    	Log.e(TAG, e.getMessage());
//		    }
//		}
//		if (mLogWriter != null) {
//			try {
//		        mLogWriter.print(msg);
//		    } catch (IOException e) {
//		    	Log.e(TAG, e.getMessage());
//		    }
//		}

//		Log.e(TAG, e.getMessage());
	}
	
	public static void logI(String tag, String msg) {
		writeToFile(msg);
		Log.i(tag, msg);
	}
	
	public static void logE(String tag, String msg) {
		writeToFile(msg);
		Log.e(tag, msg);
	}
	
	public static void logV(String tag, String msg) {
		writeToFile(msg);
		Log.v(tag, msg);
	}
	public static void logD(String tag, String msg) {
		writeToFile(msg);
		Log.d(tag, msg);
	}
	public static void logW(String tag, String msg) {
		writeToFile(msg);
		Log.w(tag, msg);
	}
	/**
	 * 把byte数组以十六进制形式打印
	 * @param msg byte数据前面的log
	 * @param msgs
	 */
	public static void logBytes(String msg, byte[] msgs) {
		logBytes(TAG, msg, msgs);
	}
	
	/**
	 * 把byte数组以十六进制形式打印
	 * @param msg
	 * @param msgs
	 * @param length
	 */
	public static void logBytes(String msg, byte[] msgs, int length) {
		
		byte[] datas = new byte[length];
		System.arraycopy(msgs, 0, datas, 0, length);
		logBytes(TAG, msg, datas);
	}
	
	/**
	 * 把byte数组以十六进制形式打印
	 * @param msg byte数据前面的log
	 * @param msgs
	 */
	public static void logBytes(String tag, String msg, byte[] msgs) {
		if (msgs==null) {
			return ;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0, length = msgs.length; i < length; i++) {
			int v = msgs[i] & 0xFF;
			if (i == 0) {
				buffer.append(msg);
			}
			String hv = null;
			if (v <= 0x0F) {
				 hv = "0"+Integer.toHexString(v);
			}else{
				 hv = Integer.toHexString(v);
			}
			buffer.append(hv + " ");
		}
		logI(tag, buffer.toString());
	}



	public static String getStrFormBytes(byte[] msgs) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0, length = msgs.length; i < length; i++) {
			int v = msgs[i] & 0xFF;
			String hv = Integer.toHexString(v);
			buffer.append(hv + " ");
		}
		return buffer.toString();
	}
	
	/**
	 * 把byte数组以十六进制形式打印
	 * @param msg byte数据前面的log
	 * @param msgs
	 */
	public static void logBytes(String tag, String msg, int[] msgs) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0, length = msgs.length; i < length; i++) {
			if (i == 0) {
				buffer.append(msg);
			}
			String hv = Integer.toHexString(msgs[i]);
			buffer.append(hv + " ");
		}
		logI(tag, buffer.toString());
	}
	
	/**
	 * 把byte数组以十六进制形式打印
	 * @param msg
	 * @param msgs
	 * @param length
	 */
	public static void logBytes(String tag, String msg, byte[] msgs, int length) {
		byte[] datas = new byte[length];
		System.arraycopy(msgs, 0, datas, 0, length);
		logBytes(tag, msg, datas);
	}
}
