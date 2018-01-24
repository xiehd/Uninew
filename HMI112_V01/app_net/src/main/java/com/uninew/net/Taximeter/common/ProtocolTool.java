package com.uninew.net.Taximeter.common;

import com.uninew.net.JT905.common.BaseMsgID;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 协议工具类
 *
 * @author Administrator
 */
public class ProtocolTool {
    public static final String CHARSET_905 = "GBK";

    /**
     * 对byte数组数据逐字节异或
     *
     * @param data
     * @param off  起始位置
     * @param len  字节长度
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
     * 校验和
     *
     * @param data
     * @param start  开始校验字节数
     * @param crclen 校验字节长度
     * @param len  取后len个字节，高字节在前，多余省略，len <= 4
     * @return
     */
    public static byte[] xoradd(byte[] data,int start,int crclen, int len) {
        byte[] crc = new byte[len];
        if (start < 0 || crclen <= 0 || data.length - start < crclen) {
            return crc;
        }

        int sum = 0;

        for (int i = start; i < crclen; i++) {
            sum = sum + (data[i] & 0xff);
        }

        if (len == 1) {
            crc[len] = (byte) ((sum) & 0xff);
        } else if (len == 2) {
            crc[0] = (byte) ((sum >> 8) & 0xff);
            crc[1] = (byte) ((sum) & 0xff);

        } else if (len == 3) {
            crc[0] = (byte) ((sum >> 16) & 0xff);
            crc[1] = (byte) ((sum >> 8) & 0xff);
            crc[2] = (byte) ((sum ) & 0xff);

        } else {
            crc[0] = (byte) ((sum >> 24) & 0xff);
            crc[1] = (byte) ((sum >> 16) & 0xff);
            crc[2] = (byte) ((sum >> 8) & 0xff);
            crc[3] = (byte) ((sum) & 0xff);

        }


        return crc;
    }

    /**
     * 对byte数组数据逐字节异或
     *
     * @param data
     * @param start 起始位置
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

    /**
     * BCD转int
     *
     * @param bcd
     * @return
     */
    public static int BCDToInt(byte bcd) {
        return (0xff & (bcd >> 4)) * 10 + (0xf & bcd);
    }

    /**
     * int转BCD
     *
     * @param i
     * @return
     */
    public static int intToBcd(int i) {
        int data = i % 100;
        return (((data / 10) << 4) + ((data % 10) & 0x0f));
    }

    /**
     * bcd 转成 string BCD码转为10进制串(阿拉伯数据)
     *
     * @param bytes BCD码
     * @return 10进制串
     */
    public static String bcd2Str(byte[] bytes) {
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            builder.append((byte) ((bytes[i] & 0xf0) >> 4));
            builder.append((byte) (bytes[i] & 0x0f));
        }
        return builder.toString().substring(0, 1).equals("0") ? builder
                .toString().substring(1) : builder.toString();
    }

    /**
     * string 转成bcd
     *
     * @param asc      10进制串
     * @param areaCode 大陆为0，港澳台为区号
     * @return BCD码
     */
    public static byte[] str2Bcd(String asc, String areaCode) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = areaCode + asc;
            len = asc.length();
        }
        if (len >= 2) {
            len = len / 2;
        }
        byte[] bbt = new byte[len];
        byte[] abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < len; p++) {
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

    /**
     * 默认为大陆 ，区号为0 string 转成bcd
     *
     * @param asc 10进制串
     * @return BCD码
     */
    public static byte[] str2Bcd(String asc) {
        return str2Bcd(asc, "0");
    }

    /**
     * string 转换成byte数组
     *
     * @param data
     * @param characterSet
     * @return
     */
    public static byte[] stringToByte(String data, String characterSet) {
        if (data == null) {
            return new byte[0];
        }
        try {
            return data.getBytes(characterSet);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data.getBytes();
    }

    /**
     * 把string 转换成byte数组，长度为0，不足后补0x00 string 转换成byte数组
     *
     * @param data
     * @param characterSet
     * @param len
     * @return
     */
    public static byte[] stringToByte(String data, String characterSet, int len) {
        byte[] result = new byte[len];
        if (data == null) {
            return result;
        }
        try {
            byte[] trans = data.getBytes(characterSet);
            int transLen = trans.length;
            if (trans != null && transLen <= len) {
                System.arraycopy(trans, 0, result, 0, transLen);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 把string 转换成byte数组，长度为0，不足后补0x00 string 转换成byte数组
     *
     * @param data
     * @param characterSet
     * @return
     */
    public static byte[] stringToByteEnd(String data, String characterSet) {
        if (data == null) {
            return new byte[1];
        }
        try {
            byte[] trans = data.getBytes(characterSet);
            int transLen = trans.length;
            byte[] result = new byte[transLen + 1];
            if (trans != null) {
                System.arraycopy(trans, 0, result, 0, transLen);
            }
            result[transLen] = 0x00;
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[1];
    }

    /**
     * byte数组转换成string
     *
     * @param data
     * @param characterSet
     * @return
     */
    public static String byteToString(byte[] data, String characterSet) {
        try {
            return new String(data, characterSet);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new String(data);
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
        for (int j = 0; j < end; ) {
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
     * String转换为byte数组
     *
     * @param str
     * @param size
     * @param charset
     * @return
     */
    public static byte[] strToGoodByte(String str, int size, String charset) {
        if (str == null) {
            return getGoodByte(null, size);
        } else {
            try {
                return getGoodByte(str.getBytes(charset), size);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return getGoodByte(null, size);
            }
        }
    }

    /**
     * 获取指定的长度的字节数组，原始数据不足指定长度时，补充空格。
     *
     * @param bytes 原始数组
     * @param size  指定长度
     * @return 指定长度的字节数组
     */
    public static byte[] getGoodByte(byte[] bytes, int size) {
        if (size <= 0) {
            throw new IndexOutOfBoundsException("size must >0");
        }
        byte[] buftemp = new byte[size];
        if (bytes == null) {
            for (int i = 0; i < size; i++) {
                buftemp[i] = 0x00;// 补空格
            }
            return buftemp;
        }
        try {
            if (bytes.length >= size) {
                buftemp = getBytes(bytes, 0, size);
            } else {
                ByteBuffer buf = ByteBuffer.allocate(size);
                buf.put(bytes);
                while (buf.hasRemaining()) {
                    buf.put((byte) 0x00);// 不足的补空格
                }
                buftemp = buf.array();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buftemp;
    }

    public static byte[] getGoodByte(byte[] bytes, int size, byte tooShort) {
        if (size <= 0) {
            throw new IndexOutOfBoundsException("size must >0");
        }
        byte[] buftemp = new byte[size];
        if (bytes == null) {
            for (int i = 0; i < size; i++) {
                buftemp[i] = (byte) tooShort;
            }
            return buftemp;
        }
        try {
            if (bytes.length >= size) {
                buftemp = getBytes(bytes, 0, size);
            } else {
                ByteBuffer buf = ByteBuffer.allocate(size);
                buf.put(bytes);
                while (buf.hasRemaining()) {
                    buf.put((byte) tooShort);
                }
                buftemp = buf.array();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buftemp;
    }

    /**
     * 获取指定范围的字节数组
     *
     * @param data 目标数组
     * @param off  开始位置
     * @param len  长度
     * @return 指定范围的字节数组
     */
    public static byte[] getBytes(byte[] data, int off, int len) {
        if (data == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > data.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return null;
        }
        byte[] buf = new byte[len];
        for (int j = 0, i = off; i < len + off; i++, j++) {
            buf[j] = data[i];
        }
        return buf;
    }

    /**
     * long 获取 BCD
     *
     * @param time
     * @return
     */
    public static byte[] getBCD12TimeBytes(long time) {
        return number2ToBCD(getGPSTime(time));
    }

    /**
     * 获取json时间 str
     *
     * @param time
     * @return
     */
    public static String getJsonTime(long time) {
        // hh-mm-ss-msms
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(time);
        return sdf.format(curDate);
    }

    /**
     * 获取时间 str
     *
     * @param time
     * @return
     */
    public static String getGPSTime(long time) {
        // hh-mm-ss-msms
        Date curDate = new Date(time);
        return df.get().format(curDate);
    }

    /**
     * 获取时间 Timestamp
     *
     * @param timestamp :HHmmssSSSS
     * @return
     */
    public static String getTimestampTime(Timestamp timestamp) {
        // hh-mm-ss-msms
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSSS");
        return sdf.format(timestamp);
    }

    /**
     * 时间戳格式化
     *
     * @param ts yyyy-MM-dd HH:mm:ss.SSS
     * @return
     */
    public static String timestampFormat(Timestamp ts) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        //		Date d = new Date(System.currentTimeMillis());
        String showTime = dateFormat.format(ts);//this for insert string.
        return showTime;
    }

    /**
     * int转换成bcd
     *
     * @param bcd
     * @return
     */
    public static byte[] intToBcd(int bcd, int len) {
        int length = String.valueOf(bcd).length();
        if (length % 2 == 0) {
            length = length / 2;
        } else {
            length = length / 2 + 1;
        }
        byte[] bcds = new byte[len];
        if (length <= len) {
            for (int i = length - 1; i >= 0; i--) {
                long temp = bcd % 100;
                bcds[i] = (byte) (((temp / 10) << 4) + ((temp % 10) & 0x0F));
                bcd /= 100;
            }
        }
        return bcds;
    }

    /**
     * 获取时间 long ,时间格式必须为yyMMddHHmmss;
     *
     * @param time
     * @return
     */
    public static long getTimeFromBCD12(String time) {
        if (time == null) {
            return 0;
        }
        if (time.length() != 12) {
            throw new IllegalArgumentException();
        }
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            int year = Integer.parseInt("20" + time.substring(0, 2));
            int month = Integer.parseInt(time.substring(2, 4));
            int date = Integer.parseInt(time.substring(4, 6));
            int hourOfDay = Integer.parseInt(time.substring(6, 8));
            int minute = Integer.parseInt(time.substring(8, 10));
            int second = Integer.parseInt(time.substring(10, 12));
            calendar.set(year, month - 1, date, hourOfDay, minute, second);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将bcd数组转换成数字
     *
     * @param bcd
     * @param start
     * @param end
     * @return
     */
    public static String number2FromBCD(byte[] bcd, int start, int end) {
        if (bcd == null) {
            return null;
        }
        ByteBuffer buf = ByteBuffer.allocate(bcd.length * 2);
        byte b = 0;
        byte temp = 0;
        for (int i = start; i < end; i++) {
            b = bcd[i];
            try {
                temp = (byte) ((byte) ((b & 0xff) >> 4) | 0x30);
                buf.put(temp);
                temp = (byte) ((byte) (b & 0x0f) | 0x30);
                buf.put(temp);
            } catch (Exception e) {
                break;
            }
        }
        buf.flip();
        return new String(buf.array(), 0, buf.limit());
    }

    /**
     * 将数字转换成BCD数组，number的长度必须为偶数
     *
     * @param number 20120908121520
     * @return
     */
    public static byte[] number2ToBCD(String number) {
        if (number == null)
            return null;
        int len;
        if (number.length() % 2 != 0) {
            len = number.length() / 2 + 1;
            throw new IllegalArgumentException("number len:" + len);
        } else {
            len = number.length() / 2;
        }
        ByteBuffer buf = ByteBuffer.allocate(len);
        byte[] tempBuf = number.getBytes();
        byte tempByte = 0;
        for (int i = 0; i < tempBuf.length; i += 2) {
            tempBuf[i] <<= 4;// 移到高四位
            if (i + 1 < tempBuf.length) {
                tempByte = (byte) (tempBuf[i + 1] & 0x0f);// 下一字节清除高四位
            }
            buf.put((byte) (tempBuf[i] | tempByte));// 合并
            tempByte = 0;
        }
        return buf.array();
    }

    /**
     * 将数字转换成定长的BCD数组，不足的使用0x00填充。
     *
     * @param number number的长度必须为偶数
     * @param size   数组的长度必须大于或等于number.length/2
     * @return
     */
    public static byte[] number2ToBCD(String number, int size) {
        if (number == null)
            return null;
        if (size < (number.length() / 2)) {
            return null;
        }
        int len;
        if (number.length() % 2 != 0) {
            len = number.length() / 2 + 1;
            throw new IllegalArgumentException("number len:" + len);
        } else {
            len = number.length() / 2;
        }
        ByteBuffer buf = ByteBuffer.allocate(size);
        for (int i = 0; i < size - len; i++) {
            buf.put((byte) 0x00);
        }
        byte[] tempBuf = number.getBytes();
        byte tempByte = 0;
        for (int i = 0; i < tempBuf.length; i += 2) {
            tempBuf[i] <<= 4;// 移到高四位
            if (i + 1 < tempBuf.length) {
                tempByte = (byte) (tempBuf[i + 1] & 0x0f);// 下一字节清除高四位
            }
            buf.put((byte) (tempBuf[i] | tempByte));// 合并
            tempByte = 0;
        }
        return buf.array();
    }

    /**
     * 将带有小数的数字转换为BCD数组
     *
     * @param number 格式必须为 XX.XX
     * @return 第一字节为整数部分，第二字节为小数部分
     */
    public static byte[] number3tobcd(String number) {
        byte[] bcd = new byte[2];
        if (number != null) {
            String[] str = null;
            str = number.split("\\.");
            if (str.length >= 2) {
                bcd[0] = (byte) intToBcd(Integer.parseInt(str[0]));
                bcd[1] = (byte) intToBcd(Integer.parseInt(str[1]));
            } else {
                bcd[0] = (byte) intToBcd(Integer.parseInt(number));
            }
        }
        return bcd;
    }

    /**
     * 将BCD数组转换成小数点数字 格式 XX.XX
     *
     * @param dates 必须两个字节，前一个表示整数；后一个表示小数
     * @return
     */
    public static String bcdToStringX(byte[] dates) {
        String string = null;
        if (dates.length >= 2) {
            string = BCDToInt(dates[0]) + "." + BCDToInt(dates[1]);
        }
        return string;
    }

    public static String readString(InputStream is, int size, String charset) {
        if (size == 0) {
            return null;
        }
        try {
            byte[] buf = new byte[size];
            is.read(buf);
            return new String(buf, charset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * bcd转换成Timestamp
     *
     * @param bcds
     * @return
     */
    public static Timestamp bcdToTimestamp(byte[] bcds) {
        int year = bcd2Int(bcds[0]);
        int month = bcd2Int(bcds[1]);
        int day = bcd2Int(bcds[2]);
        int h = bcd2Int(bcds[3]);
        int m = bcd2Int(bcds[4]);
        int s = bcd2Int(bcds[5]);
        return new Timestamp(year + 2000 - 1900, month - 1, day, h, m, s, 0);
    }

    /**
     * Timestamp转BCD
     *
     * @param t
     * @return
     */
    public static byte[] timeStampToBcd(Timestamp t) {
        byte[] datas = new byte[6];
        datas[0] = intToBcd((t.getYear() - 2000 + 1900), 1)[0];
        datas[1] = intToBcd((t.getMonth() + 1), 1)[0];
        datas[2] = intToBcd(t.getDate(), 1)[0];
        datas[3] = intToBcd(t.getHours(), 1)[0];
        datas[4] = intToBcd(t.getMinutes(), 1)[0];
        datas[5] = intToBcd(t.getSeconds(), 1)[0];
        return datas;
    }


    private static int bcd2Int(byte b) {
        return ((b >> 4) & 0x0F) * 10 + (b & 0x0F);
    }


    /**
     * 获取系统当前时间，注意设置系统时间
     *
     * @return
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 转成Timestamp
     *
     * @param year   年
     * @param month  月
     * @param day    日
     * @param hour   小时
     * @param minute 分钟
     * @param second 秒
     * @param nano   纳秒
     * @return
     */
    public static Timestamp toTimestamp(int year, int month, int day, int hour, int minute, int second, int nano) {
        int y = year - 1900;
        int m = month - 1;
        return new Timestamp(y, m, day, hour, minute, second, nano);
    }

    /**
     * 把给定字符串转换成指定length的byte[]数组
     *
     * @param string 给定字符串
     * @param length 指定长度
     * @return
     */
    public static byte[] stringToArray(String string, int length) {
        byte[] bytes = null;
        try {
            bytes = string.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] arr = new byte[length];
        for (int i = 0; i < bytes.length; i++) {
            arr[i] = bytes[i];
        }
        return arr;
    }


    /**
     * 获取Int的第n个bit值（低位为0）
     *
     * @param data 数据
     * @param n    第几位  0<= n <=15
     * @return
     */
    public static int getBit(int data, int n) {
        int result = 0;
        result = (data & (0x01 << n)) >> n;
        return result;
    }

    /**
     * 设置Int的第n个bit值（低位为0）
     *
     * @param data 数据
     * @param n    第几位  0<= n <=15
     * @param bit  设置的bit值 0或者1
     * @return
     */
    public static int setBit(int data, int n, int bit) {
        int result = 0;
        if (bit == 0) {
            result = (data & ((1 << n) ^ 0xFFFF));
            System.out.print(" result=" + result);
        } else {
            result = (data | (bit << n));
        }
        return result;
    }


    private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {

        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyMMddHHmmss");
        }

    };


}