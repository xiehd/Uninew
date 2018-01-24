package com.uninew.mms.util;

public class ByteTools {

	
	/**
	 * byte数组转short 
	 * @param high
	 * @param low
	 * @return
	 */
	public static short byteToShort(byte low,byte high){
		
		return (short) (low & 0xff | (high << 8));
	}
	
	/**
	 * byte数组转short 
	 * @param data 两个byte的数组，高位在前低位在后
	 * @return
	 */
	public static short byteToShort(byte[] data){
		
		return (short) (data[1] & 0xff | (data[0] << 8));
	}
	
	/**
	 * byte转int  高位在前低位在后
	 * @param b0 低位
	 * @param b1
	 * @param b2
	 * @param b3 高位
	 * @return
	 */
	public static int bytes2Int(byte b0,byte b1,byte b2,byte b3) {
		return ((0xff & b3) | ((0xff & b2) << 8) | ((0xff & b1) << 16)| ((0xff & b0) << 24));
	}
	
	/**
	 * int转byte
	 * @param num
	 * @return 高位在前低位在后的byte数组
	 */
	public static byte[] intToBytes(int num){
		byte[] bb=new byte[4];
		bb[0]=(byte) (0xff & (num >> 24));
		bb[1]=(byte) (0xff & (num >> 16));
		bb[2]=(byte) (0xff & (num >> 8));
		bb[3]=(byte) (0xff & num);
		return bb;
	}
	
	/**
	 * short转byte
	 * @param num
	 * @return 高位在前低位在后的byte数组
	 */
	public static byte[] shortToBytes(short num){
		byte[] bb=new byte[2];
		bb[0]=(byte) (0xff & (num >> 8));
		bb[1]=(byte) (0xff & num);
		return bb;
	}
	
}
