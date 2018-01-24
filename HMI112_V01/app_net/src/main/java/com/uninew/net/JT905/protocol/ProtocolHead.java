package com.uninew.net.JT905.protocol;

import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/8/19 0019.
 */

public class ProtocolHead {

    /** 消息ID*/
    private int msgId;
    /** 消息长度 */
    private int length;
    /** 厂商编号 */
    private int companyNumber;
    /** 设备类型 */
    private int deviceType;
    /** 设备序列号 */
    private String deviceSerialNumber;
    /** 流水号 */
    private int serialNumber;
    /** 终端标识 （0x10+厂商编号+设备类型+序列号）*/
    private String deviceId;

    public ProtocolHead() {

    }
    public ProtocolHead(int msgId, int length, String deviceId, int serialNumber) {
        this.msgId = msgId;
        this.length = length;
        this.deviceId = deviceId;
        this.serialNumber = serialNumber;
    }

    public ProtocolHead(int msgId, int length, int companyNumber, int deviceType, String deviceSerialNumber, int serialNumber) {
        this.msgId = msgId;
        this.length = length;
        this.companyNumber = companyNumber;
        this.deviceType = deviceType;
        this.deviceSerialNumber = deviceSerialNumber;
        this.serialNumber = serialNumber;
    }


    public byte[] getProtocolHeadBytes(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        byte[] data=null;
        try {
            out.writeShort(msgId);
            out.writeShort(length);
            if (deviceId==null || "".equals(deviceId)){
                deviceId=ProtocolTool.intToBcd(companyNumber)+ProtocolTool.intToBcd(deviceType)+deviceSerialNumber;
            }
            byte[] id = ProtocolTool.str2Bcd(deviceId);
            byte[] id2 = new byte[6];
            if (id.length > 6) {
                System.arraycopy(id, 0, id2, 0, 6);
            } else {
                System.arraycopy(id, 0, id2, 6 - id.length, id.length);
            }
            out.write(id2);
            out.writeShort(serialNumber);
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

    public ProtocolHead getProtocolHead(byte[] datas){
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            msgId = in.readUnsignedShort();
            length = in.readUnsignedShort();
            byte[] b = new byte[6];
            in.read(b);
            deviceId = ProtocolTool.bcd2Str(b);
            companyNumber=ProtocolTool.BCDToInt(b[0]);
            deviceType=ProtocolTool.BCDToInt(b[1]);
            deviceSerialNumber=deviceId.substring(2);
            serialNumber = in.readShort();
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

    public int getMsgId() {
        return msgId;
    }

    public int getLength() {
        return length;
    }

    public int getCompanyNumber() {
        return companyNumber;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setCompanyNumber(int companyNumber) {
        this.companyNumber = companyNumber;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "ProtocolHead{" +
                "msgId=" + msgId +
                ", length=" + length +
                ", companyNumber=" + companyNumber +
                ", deviceType=" + deviceType +
                ", deviceSerialNumber=" + deviceSerialNumber +
                ", serialNumber=" + serialNumber +
                '}';
    }
}
