package com.uninew.mms.tools;

/**
 * 异或校验
 * @author Administrator
 *
 */
public class CheckTools {
	
	/**
	 * 对byte数组数据逐字节异或
	 * 
	 * @param data 数据
	 * @param off 开始位置
	 * @param len 长度
	 * @return
	 */
	public static byte xor(byte[] data, int off, int len) {
		byte res = 0x00;
		if (off < 0 || len <= 0 || data.length - off < len) {
			return res;
		}
		for (int i = off; i < off + len; i++) {
			res ^= data[i];
		}
		return res;
	}

	/**
	 * 对byte数组数据逐字节异或
	 * 
	 * @param data
	 * @param start
	 *            起始位置
	 * @return
	 */
	public static byte xor(byte[] data, int start) {
		return xor(data, start, data.length - start);
	}

	/**
	 * 对byte数组数据逐字节异或
	 * 
	 * @param data
	 * @return
	 */
	public static byte xor(byte[] data) {
		return xor(data, 0, data.length);
	}
}
