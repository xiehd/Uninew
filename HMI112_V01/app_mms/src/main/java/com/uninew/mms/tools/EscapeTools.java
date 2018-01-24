package com.uninew.mms.tools;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 协议转义工具
 * @author Administrator
 *
 */
public class EscapeTools {
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
			if (data[j] == DefineTYRoadSigns.MSG_FLAG) {
				out.writeByte(DefineTYRoadSigns.ESCAPE);
				out.writeByte(DefineTYRoadSigns.FOLLOW_ESCAPE_7E);
			} else if (data[j] == DefineTYRoadSigns.ESCAPE) {
				out.writeByte(DefineTYRoadSigns.ESCAPE);
				out.writeByte(DefineTYRoadSigns.FOLLOW_ESCAPE_7D);
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
			if (data[j] == DefineTYRoadSigns.ESCAPE) {
				j++;
				if (data[j++] == DefineTYRoadSigns.FOLLOW_ESCAPE_7E) {
					out.writeByte(DefineTYRoadSigns.MSG_FLAG);
				} else {
					out.writeByte(DefineTYRoadSigns.ESCAPE);
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
}
