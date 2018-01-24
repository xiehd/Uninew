package com.uninew.mms.ChuanJiLed.protocol;

import com.uninew.mms.ChuanJiLed.bean.BaseCJLed;
import com.uninew.net.JT905.common.LogTool;
import com.uninew.net.Taximeter.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 川基LED协议
 * Created by Administrator on 2017/12/27.
 */

public class CJLedProtocolPacket extends BaseCJLed {
    private int start = 0xAA55;
    private int length;
    private byte[] RES = new byte[8];
    private byte msgID;
    private byte[] res2 = new byte[3];
    private String deviceNumber;//长度8
    private byte[] body;
    private byte[] CRC = new byte[2];
    private int end = 0x88;


    public CJLedProtocolPacket() {
    }

    public CJLedProtocolPacket(int msgID, String deviceNumber, byte[] body) {
        this.msgID = (byte) msgID;
        this.deviceNumber = deviceNumber;
        this.body = body;
        this.length = body.length + 27;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeShort(start);
            out.writeShort(length);
            out.write(RES);
            out.writeByte(msgID);
            out.write(res2);
            out.write(deviceNumber.getBytes());
            out.write(body);
            out.flush();
            byte[] c = stream.toByteArray();
            CRC = ProtocolTool.xoradd(c,0,c.length, 2);
            out.write(CRC);
            out.writeByte(end);
            out.flush();
            datas = stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null)
                    stream.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return datas;
    }

    @Override
    public CJLedProtocolPacket getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            start = in.readUnsignedShort();
            length = in.readUnsignedShort();
            in.read(RES);
            msgID = in.readByte();
            in.read(res2);
            byte[] device = new byte[8];
            in.read(device);
            deviceNumber = new String(device, "UTF-8");
            body = new byte[length - 27];
            in.read(body);
            in.read(CRC);
            LogTool.logBytes("CJLedProtocolPacket","校验码：",ProtocolTool.xoradd(datas,0,datas.length-3,2));
            end = in.readByte();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null)
                    stream.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getRES() {
        return RES;
    }

    public void setRES(byte[] RES) {
        this.RES = RES;
    }

    public byte getMsgID() {
        return msgID;
    }

    public void setMsgID(byte msgID) {
        this.msgID = msgID;
    }

    public byte[] getRes2() {
        return res2;
    }

    public void setRes2(byte[] res2) {
        this.res2 = res2;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] getCRC() {
        return CRC;
    }

    public void setCRC(byte[] CRC) {
        this.CRC = CRC;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
