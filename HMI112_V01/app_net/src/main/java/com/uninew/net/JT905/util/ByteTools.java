package com.uninew.net.JT905.util;


import com.uninew.net.JT905.common.BaseMsgID;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class ByteTools {
	/**
	 * 发送给平台时的转义 0x7e -> 0x7d 0x02 0x7d -> 0x7d 0x01
	 * 
	 * @param data
	 * @param start
	 * @param end
	 * @return
	 * @throws IOException
	 */
	public static byte[] escape(byte[] data, int start, int end)
			throws IOException {
		if (data.length < end || start < 0) {
			return data;
		}
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		for (int i = 0; i < start; i++) {
			out.writeByte(data[i]);
		}
		for (int j = start; j < end; j++) {
			if (data[j] == BaseMsgID.MSG_FLAG) {
				out.writeByte(BaseMsgID.ESCAPE);
				out.writeByte(BaseMsgID.ESCAPE_E);
			} else if (data[j] == BaseMsgID.ESCAPE) {
				out.writeByte(BaseMsgID.ESCAPE);
				out.writeByte(BaseMsgID.ESCAPE_D);
			} else {
				out.writeByte(data[j]);
			}
		}
		for (int k = end, count = data.length; k < count; k++) {
			out.writeByte(data[k]);
		}
		out.flush();
		return byteStream.toByteArray();
	}

	/**
	 * 接收到平台数据的转义还原 0x7d 0x02 -> 0x7e 0x7d 0x01 -> 0x7d
	 * 
	 * @param data
	 * @param start
	 * @param end
	 * @return
	 * @throws IOException
	 */
	public static byte[] escapeReduction(byte[] data, int start, int end)
			throws IOException {
		if (data.length < end || start < 0) {
			return data;
		}
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		for (int i = 0; i < start; i++) {
			out.writeByte(data[i]);
		}
		for (int j = 0; j < end;) {
			if (data[j] == BaseMsgID.ESCAPE) {
				j++;
				if (data[j++] == BaseMsgID.ESCAPE_E) {
					out.writeByte(BaseMsgID.MSG_FLAG);
				} else {
					out.writeByte(BaseMsgID.ESCAPE);
				}
			} else {
				out.writeByte(data[j++]);
			}
		}
		for (int k = end, count = data.length; k < count; k++) {
			out.writeByte(data[k]);
		}
		out.flush();
		return byteStream.toByteArray();
	}
	
	/**
	 * 把byte数组以十六进制形式打印
	 * @param msgs
	 */
	public static String logBytes(byte[] msgs) {
		if (msgs==null) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0, length = msgs.length; i < length; i++) {
			int v = msgs[i] & 0xFF;
			String hv = null;
			if (v < 0x0F) {
				 hv = "0"+Integer.toHexString(v);
			}else{
				 hv = Integer.toHexString(v);
			}
			buffer.append(hv + " ");
		}
		return buffer.toString();
	}

	/**
	 * @param data1
	 * @param data2
	 * @return data1 与 data2拼接的结果
	 */
	public static byte[] addBytes(byte[] data1, byte[] data2) {
		byte[] data3 = new byte[data1.length + data2.length];
		System.arraycopy(data1, 0, data3, 0, data1.length);
		System.arraycopy(data2, 0, data3, data1.length, data2.length);
		return data3;

	}

	/**
	 * byte数组转short
	 *
	 * @param high
	 * @param low
	 * @return
	 */
	public static short byteToShort(byte low, byte high) {

		return (short) (low & 0xff | (high << 8));
	}

	/**
	 * byte数组转short
	 *
	 * @param data 两个byte的数组，高位在前低位在后
	 * @return
	 */
	public static short byteToShort(byte[] data) {

		return (short) (data[1] & 0xff | (data[0] << 8));
	}

	/**
	 * byte转int  高位在前低位在后
	 *
	 * @param b0 低位
	 * @param b1
	 * @param b2
	 * @param b3 高位
	 * @return
	 */
	public static int bytes2Int(byte b0, byte b1, byte b2, byte b3) {
		return ((0xff & b3) | ((0xff & b2) << 8) | ((0xff & b1) << 16) | ((0xff & b0) << 24));
	}

	/**
	 * int转byte
	 *
	 * @param num
	 * @return 高位在前低位在后的byte数组
	 */
	public static byte[] intToBytes(int num) {
		byte[] bb = new byte[4];
		bb[0] = (byte) (0xff & (num >> 24));
		bb[1] = (byte) (0xff & (num >> 16));
		bb[2] = (byte) (0xff & (num >> 8));
		bb[3] = (byte) (0xff & num);
		return bb;
	}

	/**
	 * short转byte
	 *
	 * @param num
	 * @return 高位在前低位在后的byte数组
	 */
	public static byte[] shortToBytes(short num) {
		byte[] bb = new byte[2];
		bb[0] = (byte) (0xff & (num >> 8));
		bb[1] = (byte) (0xff & num);
		return bb;
	}

	/**
	 * byte数组转为16进制字符
	 * @param msgs
	 * @return
	 */
	public static String ByteToStr(byte[] msgs) {
		if (msgs == null) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0, length = msgs.length; i < length; i++) {
			int v = msgs[i] & 0xFF;
			String hv = null;
			if (v <= 0x0F) {
				hv = "0" + Integer.toHexString(v);
			} else {
				hv = Integer.toHexString(v);
			}
			buffer.append(hv + " ");
		}
		return buffer.toString();
	}
}
