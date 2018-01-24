package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/8/18 0018.
 */

public class P_GeneralResponse extends BaseBean {

    private int pSerialNumber;
    private int id;//应答ID
    private int result;//结果  0：成功 1：失败 2：消息有误 3：不支持

    public P_GeneralResponse(byte[] datas) {
        getDataPacket(datas);
    }

    /**
     *
     * @param pSerialNumber 应答流水号
     * @param id  应答ID
     * @param result  结果   0：成功 1：失败 2：消息有误 3：不支持
     */
    public P_GeneralResponse(int pSerialNumber, int id, int result) {
        this.pSerialNumber = pSerialNumber;
        this.id = id;
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
        this.serialNumber = serialNumber;
    }

    @Override
    public int getMsgId() {
        return BaseMsgID.PLATFORM_GENERAL_ANS;
    }

    public int getpSerialNumber() {
        return pSerialNumber;
    }

    public int getId() {
        return id;
    }

    public void setpSerialNumber(int pSerialNumber) {
        this.pSerialNumber = pSerialNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeShort(pSerialNumber);
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
    public P_GeneralResponse getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            this.pSerialNumber = in.readUnsignedShort();
            id = in.readShort();
            result = in.readUnsignedByte();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
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

    public int getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "P_GeneralResponse{" +
                "id=" + id +
                ", result=" + result +
                '}';
    }
}
