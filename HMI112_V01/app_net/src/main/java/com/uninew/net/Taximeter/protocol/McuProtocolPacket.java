package com.uninew.net.Taximeter.protocol;

import android.util.Log;

import com.uninew.net.Taximeter.common.DefineID;
import com.uninew.net.Taximeter.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 通讯协议
 * Created by Administrator on 2017/10/14.
 */

public class McuProtocolPacket {

    private static final boolean D = true;
    private static final String TAG = "McuProtocolPacket";
    /**
     * 起始位
     */
    private short start = DefineID.TAXI_FLAG;
    /**
     * 消息长度
     */
    private int length;
    /**
     * 设备类型
     */
    private int deviceType = 0x02;
    /**
     * 厂商编号
     */
    private int companyNumber = 0x00;
    /**
     * 命令字
     */
    private int msgId;
    /**
     * 消息体
     */
    private byte[] body;
    private int check;
    /**
     * 结束位
     */
    private short end = DefineID.TAXI_FLAG;

    public McuProtocolPacket() {
    }

    public McuProtocolPacket(int msgId, byte[] body, int deviceType) {
        this.msgId = msgId;
        this.body = body;
        if (body != null) {
            this.length = body.length + 4;
        } else {
            this.length = 4;
        }
        this.deviceType = deviceType;

    }

    public byte[] getProtocolPacketBytes() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        byte[] data = null;
        try {
            out.writeShort(DefineID.TAXI_FLAG);
            out.writeByte(length >> 8 & 0xff);
            out.writeByte(length & 0xff);
            out.writeByte(deviceType);
            out.writeByte(companyNumber);
            out.writeShort(msgId);
            if (body != null) {
                out.write(body);
            }
            out.flush();
            byte[] msgBytes = stream.toByteArray();
            byte checksum = ProtocolTool.xor(msgBytes, 2); // 校验码
            out.writeByte(checksum);
            out.writeShort(DefineID.TAXI_FLAG);
            out.flush();
            data = stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public McuProtocolPacket getProtocolPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            start = (short) in.readUnsignedShort();
            length = in.readUnsignedShort();
            deviceType = in.readByte();
            companyNumber = in.readByte();
            msgId = in.readUnsignedShort();
            body = new byte[length - 4];
            in.read(body);
            check = in.readByte();
            end = (short) in.readUnsignedShort();

            if (D) {
                Log.d(TAG, "read start = " + start);
                Log.d(TAG, "read length = " + length);
                Log.d(TAG, "read deviceType = " + deviceType);
                Log.d(TAG, "read companyNumber = " + companyNumber);
                Log.d(TAG, "read msgId = " + msgId);
                Log.d(TAG, "read body length = " + body.length);
                Log.d(TAG, "read check = " + check);
                Log.d(TAG, "read end = " + end);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return this;
    }


    public short getStart() {
        return start;
    }

    public void setStart(short start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(int companyNumber) {
        this.companyNumber = companyNumber;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public short getEnd() {
        return end;
    }

    public void setEnd(short end) {
        this.end = end;
    }
}
