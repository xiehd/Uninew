package com.uninew.net.JT905.protocol;

import android.util.Log;

import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.ProtocolTool;
import com.uninew.net.JT905.util.ByteTools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/8/19 0019.
 */

public class ProtocolPacket {

    private static final String TAG = "ProtocolPacket";
    private static final boolean D = true;

    private ProtocolHead head;
    private byte[] body;
    private int check;


    /**平台编号*/
    private int tcpId;
    /**通信方式：0x01-TCP,0x02-SMS*/
    private int transportId;
    private String smsPhoneNunber;
    //发送时间
    private long sendTime;
    //发送次数
    private int sendNumber;

    public byte[] getProtocolPacketBytes() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        byte[] data = null;
        try {
            out.writeByte(BaseMsgID.MSG_FLAG);
            if (head != null) {
                out.write(head.getProtocolHeadBytes());
            }
            if (body != null) {
                out.write(body); // 消息体
            }
            out.flush();
            byte[] msgBytes = stream.toByteArray();
            byte checksum = ProtocolTool.xor(msgBytes, 1); // 校验码
            out.writeByte(checksum);
            out.writeByte(BaseMsgID.MSG_FLAG);
            out.flush();
            byte[] msgByte = stream.toByteArray();
            data = ProtocolTool.escape(msgByte, 1, msgByte.length - 1);//转义
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

    public ProtocolPacket getProtocolPacket(byte[] datas) {
        if(D) Log.e(TAG, "getProtocolPacket:"+ ByteTools.logBytes(datas));
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            byte[] b = new byte[12];
            in.read(b);
            head = new ProtocolHead().getProtocolHead(b);
            body = new byte[head.getLength()];
            in.read(body);
            check = in.readByte();
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
        return head.getMsgId();
    }

    public ProtocolHead getHead() {
        return head;
    }

    public byte[] getBody() {
        return body;
    }

    public int getCheck() {
        return check;
    }

    public void setMsgId(int msgId) {
        head.setMsgId(msgId);
    }

    public void setHead(ProtocolHead head) {
        this.head = head;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public int getSerialNumber() {
        return head.getSerialNumber();
    }

    public void setSerialNumber(int serialNumber) {
        head.setSerialNumber(serialNumber);
    }

    public String getSmsPhoneNunber() {
        return smsPhoneNunber;
    }

    public void setSmsPhoneNunber(String smsPhoneNunber) {
        this.smsPhoneNunber = smsPhoneNunber;
    }

    public int getTcpId() {
        return tcpId;
    }

    public int getTransportId() {
        return transportId;
    }

    public void setTcpId(int tcpId) {
        this.tcpId = tcpId;
    }

    public void setTransportId(int transportId) {
        this.transportId = transportId;
    }

    public long getSendTime() {
        return sendTime;
    }

    public int getSendNumber() {
        return sendNumber;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public void setSendNumber(int sendNumber) {
        this.sendNumber = sendNumber;
    }
}
