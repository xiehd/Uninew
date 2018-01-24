package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/8/18 0018.
 */

public class T_GeneralResponse extends BaseBean{

    public static final int Result_Success=0x00;//成功
    public static final int Result_Failure=0x01;//失败
    public static final int Result_MsgError=0x02;//消息有误
    public static final int Result_UnSupport=0x03;//不支持

    private int id;//应答ID
    private int result;//结果  0：成功 1：失败 2：消息有误 3：不支持
    private int responseSerialMumber;

    public T_GeneralResponse(int msgId, int responseSerialMumber, int result) {
        this.responseSerialMumber = responseSerialMumber;
        this.id = msgId;
        this.result = result;
    }


    @Override
    public int getTcpId() {
        return this.tcpId;
    }

    @Override
    public void setTcpId(int tcpId) {
        this.tcpId=tcpId;
    }

    @Override
    public int getTransportId() {
        return this.transportId;
    }

    @Override
    public void setTransportId(int transportId) {
        this.transportId=transportId;
    }

    @Override
    public String getSmsPhoneNumber() {
        return this.smsPhonenumber;
    }

    @Override
    public void setSmsPhoneNumber(String smsPhonenumber) {
        this.smsPhonenumber=smsPhonenumber;
    }

    @Override
    public int getSerialNumber() {
        return this.serialNumber;
    }

    @Override
    public void setSerialNumber(int serialNumber) {
        this.serialNumber=serialNumber;
    }

    @Override
    public int getMsgId() {
        return BaseMsgID.TERMINAL_GENERAL_ANS;
    }

    public int getId() {
        return id;
    }

    public int getResult() {
        return result;
    }

    public int getResponseSerialMumber() {
        return responseSerialMumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void setResponseSerialMumber(int responseSerialMumber) {
        this.responseSerialMumber = responseSerialMumber;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeShort(responseSerialMumber);
            out.writeShort(id);
            out.writeByte(result);
            out.flush();
            datas = stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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
        return datas;
    }

    @Override
    public Object getDataPacket(byte[] datas) {
        return null;
    }

    @Override
    public String toString() {
        return "T_GeneralResponse{" +
                "id=" + id +
                ", result=" + result +
                '}';
    }
}
