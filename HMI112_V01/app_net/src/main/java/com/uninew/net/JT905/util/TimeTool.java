package com.uninew.net.JT905.util;

import android.os.SystemClock;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeTool {
	public static final int LOCATION_MULTIPLE = 1000000;
	public static final double LOCATION_SPEED = 3.6;

	/**
	 * 时间格式化
	 * 
	 * @param ts
	 * @return 时间戳 yyyy-MM-dd HH:mm
	 */
	public static String timestampTormat(Timestamp ts) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm");
		return dateFormat.format(ts);
	}
	/**
	 * 时间格式化
	 * 
	 * @param ts
	 * @return 时间戳 yyyyMMddHHmmss
	 */
	public static String timestampTormat2(Timestamp ts) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(ts);
	}
	/**
	 * 时间格式化
	 *
	 * @param ts
	 * @return 时间戳 yyyy-MM-dd
	 */
	public static String timestampTormat3(Timestamp ts) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd");
		return dateFormat.format(ts);
	}
	/**
	 * 转成Timestamp
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param second
	 * @param nano
	 * @return 时间戳
	 */
	public static Timestamp toTimestamp(int year, int month, int day, int hour,
			int minute, int second, int nano) {
		int y = year - 1900;
		int m = month - 1;
		return new Timestamp(y, m, day, hour, minute, second, nano);
	}

	/**
	 * time的格式为：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param time
	 * @return 时间戳
	 */
	public static Timestamp toTimestamp(String time) {
		return Timestamp.valueOf(time);
	}

	/**
	 * 转成 sql包的Date
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return 时间戳
	 */
	public static Date toDate(int year, int month, int day) {
		int y = year - 1900;
		int m = month - 1;
		return new Date(y, m, day);
	}

	/**
	 * 
	 * @param date
	 *            yyyy-MM-dd
	 * @return 时间戳
	 */
	public static Date toDate(String date) {
		return Date.valueOf(date);
	}

	/**
	 * 时间格式化
	 * 
	 * @param date
	 * @return 时间戳
	 */
	public static String dateTormat(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// Date d = new Date(System.currentTimeMillis());
		String showTime = dateFormat.format(date);// this for insert string.
		return showTime;
	}

	/**
	 * 获取系统当前时间
	 * 
	 * @return Timestamp
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}
	/**
	 * 设置系统时间
	 */
	public static void setSystemTime(long when) {
		if (when / 1000 < Integer.MAX_VALUE) {
			SystemClock.setCurrentTimeMillis(when);
		}
		
	}
}
