package com.uninew.mms.util;

/**
 * MCU通讯Log打印类
 * @author Administrator
 *
 */
public class McuLog {
	private static boolean open = true;

	public static void i(String tag, String msg) {
		if (open) {
			LogTool.logI(tag, msg);
		}
	}

	public static void v(String tag, String msg) {
		if (open) {
			LogTool.logV(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (open) {
			LogTool.logD(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (open) {

			LogTool.logW(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (open) {
			LogTool.logE(tag, msg);
		}
	}
}
