package com.uninew.mms.aidl.utils;

import android.util.Log;

public class ByteTool {

	/**
	 * 默认编码
	 */
	public static final String DEFAULT_ENCODE = "UTF-8";

	/**
	 * 保持低字节不变，3个高字节全部用0填充
	 * 
	 * @param data
	 * @return
	 */
	public static int byteToInt(byte data) {
		return data & 0xff;
	}

	/*
	 * 检测数组的长度
	 */
	public static boolean checkLength(byte[] buff, int l) {
		if (buff.length >= l)
			return true;
		return false;
	}

	/**
	 * @param p
	 *            原数组
	 * @param start
	 *            p数组下标
	 * @param end
	 *            p数组下标
	 * @return
	 */
	public static byte[] getByteFrom(byte[] p, int start, int end) {
		byte[] temp = new byte[end - start + 1];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = p[i + start];
		}
		return temp;
	}

	/**
	 * byte 数组转换为int 输出。。。
	 * 
	 * @param bytes
	 *            数组长度4位以内.。
	 * @return
	 */
	public static int bytesToInt(byte[] bytes) {
		int value = 0;
		for (int i = bytes.length - 1; i >= 0; i--) {
			value = value << 8;
			value |= (0xff & bytes[i]);
		}
		return value;
	}

	/**
	 * byte 转为 16进制的字符串
	 * 
	 * @param data
	 * @return
	 */
	public static String byteToHexString(byte data) {
		int v = data & 0xFF;
		String hex = Integer.toHexString(v);
		return hex;
	}

	/**
	 * data[index] 转为 16进制的字符串
	 * 
	 * @param data
	 * @param index
	 * @return
	 */
	public static String byteToHexString(byte[] data, int index) {
		if (index > data.length - 1 || index < 0) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return byteToHexString(data[index]);
	}

	public static String byteToString(byte[] data) {
		String s = "";
		if (data != null) {
			for (int i = 0; i < data.length; i++) {
				s += byteToHexString(data[i]) + " ";
			}
		} else
			s = "null";
		return s;
	}

	/**
	 * byte 数组转换为int 输出。。。
	 * 
	 * @param bytes
	 *            数组长度4位以内.。
	 * @return
	 */
	public static int bytes2Int(byte[] bytes) {
		int value = 0;
		for (int i = bytes.length - 1; i >= 0; i--) {
			value = value << 8;
			value |= (0xff & bytes[i]);
		}
		return value;
	}

	/**
	 * int 数据转为 指定长度的byte数组输出 。.
	 * 
	 * @param data
	 *            int
	 * @param length
	 *            指定byte数组的长度
	 * @return
	 */
	public static byte[] int2Bytes(int data, int length) {

		byte[] bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			bytes[i] = (byte) ((data >> (8 * i)) & 0xFF);
		}
		return bytes;
	}

	/**
	 * 转化为版本号
	 * 
	 * @param vBuffer
	 * @return
	 */
	public static String byteToVersion(byte[] vBuffer) {
		byte[] temp = ByteTool.getByteFrom(vBuffer, 4, vBuffer.length - 1);
		String nRcvString;
		StringBuffer tStringBuf = new StringBuffer();
		char[] tChars = new char[temp.length];
		for (int i = 0; i < temp.length; i++) {
			tChars[i] = (char) temp[i];
		}
		tStringBuf.append(tChars);
		nRcvString = tStringBuf.toString();
		return nRcvString;
	}

	/**
	 * 16进制字符串转 字节数组
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] HexStrToBytes(String str) {
		// 如果字符串长度不为偶数，则追加0
		if (str.length() % 2 != 0) {
			str = "0" + str;
		}

		byte[] b = new byte[str.length() / 2];
		byte c1, c2;
		for (int y = 0, x = 0; x < str.length(); ++y, ++x) {
			c1 = (byte) str.charAt(x);
			if (c1 > 0x60)
				c1 -= 0x57;
			else if (c1 > 0x40)
				c1 -= 0x37;
			else
				c1 -= 0x30;
			c2 = (byte) str.charAt(++x);
			if (c2 > 0x60)
				c2 -= 0x57;
			else if (c2 > 0x40)
				c2 -= 0x37;
			else
				c2 -= 0x30;
			b[y] = (byte) ((c1 << 4) + c2);
		}
		return b;
	}

	public static void printHexString(byte[] b, String tag) {
		String temp = tag;
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			temp += " [" + i + "]= " + hex.toUpperCase();
			// Log.i("MCU", "my bytes[" + i + "]= " + hex.toUpperCase());
		}
		Log.i("MCU", temp);
	}

	public static int HexString2Int(String hexString) {
		int b = Integer.parseInt(hexString.replaceAll("^0[x|X]", ""), 16);

		return b;

	}

	/**
	 * @功能: BCD码转为10进制串(阿拉伯数据)
	 * @参数: BCD码
	 * @结果: 10进制串
	 */
	public static String Bcd2Str(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
	}

	/**
	 * @功能: 10进制串转为BCD码
	 * @参数: 10进制串
	 * @结果: BCD码
	 */
	public static byte[] Str2Bcd(String asc) {
		int len = asc.length();
		int mod = len % 2;
		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}
		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}
		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;
		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}
			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}
			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}

	// 以下是BCD编码转换

	
public static byte[] hexStringToByte(String hex) {
    int len = (hex.length() / 2);
    byte[] result = new byte[len];
    char[] achar = hex.toCharArray();
    for (int i = 0; i < len; i++) {
     int pos = i * 2;
     result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
    }
    return result;
}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

//	public static final Object bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
//		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
//		ObjectInputStream oi = new ObjectInputStream(in);
//		Object o = oi.readObject();
//		oi.close();
//		return o;
//	}

//	public static final byte[] objectToBytes(Serializable s) throws IOException {
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		ObjectOutputStream ot = new ObjectOutputStream(out);
//		ot.writeObject(s);
//		ot.flush();
//		ot.close();
//		return out.toByteArray();
//	}

//	public static final String objectToHexString(Serializable s) throws IOException {
//		return bytesToHexString(objectToBytes(s));
//	}

//	public static final Object hexStringToObject(String hex) throws IOException, ClassNotFoundException {
//		return bytesToObject(hexStringToByte(hex));
//	}

	public static String bcd2Str(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
	}

	public static byte[] str2Bcd(String asc) {
		int len = asc.length();
		int mod = len % 2;
		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}
		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}
		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;
		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}
			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}
			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}
	public final static char[] BToA = "0123456789abcdef".toCharArray() ;

	public static String BCD2ASC(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			int h = ((bytes[i] & 0xf0) >>> 4);
			int l = (bytes[i] & 0x0f);
			temp.append(BToA[h]).append(BToA[l]);
		}
		return temp.toString();
	}

	public static byte[] addArr(int newArrLength,byte [] oldArrs, byte[]addArrs) {
		byte [] newArrs  =new byte [newArrLength];
		
		for (int i = 0; i < oldArrs.length; i++) {
			newArrs[i]= oldArrs[i];
		}
		
		int count = 0;
		for (int j = oldArrs.length; j < addArrs.length+oldArrs.length; j++) {
			newArrs[j] = addArrs[count];
			count++;
		}
		
		return newArrs;
	}
	
	
//	public static String MD5EncodeToHex(String origin) {
//		return bytesToHexString(MD5Encode(origin));
//	}

//	public static byte[] MD5Encode(String origin) {
//		return MD5Encode(origin.getBytes());
//	}

//	public static byte[] MD5Encode(byte[] bytes) {
//		MessageDigest md = null;
//		try {
//			md = MessageDigest.getInstance("MD5");
//			return md.digest(bytes);
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//			return new byte[0];
//		}
//
//	}
}
