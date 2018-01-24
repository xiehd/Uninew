package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 存儲圖像檢索应答
 * Created by Administrator on 2017/8/19 0019.
 */

public class T_PictureQueryAns extends BaseBean{

    private int responseSerialNumber;//应答流水号
    private int packetSize;//包大小
    private int offset;//偏移量
    private List<Integer> queryResults;//检索项，照片ID列表
    private int imageId;//照片ID

    public T_PictureQueryAns() {
    }

    public T_PictureQueryAns(int responseSerialNumber, int packetSize, int offset, List<Integer> queryResults) {
        this.responseSerialNumber = responseSerialNumber;
        this.packetSize = packetSize;
        this.offset = offset;
        this.queryResults = queryResults;
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
        return BaseMsgID.STORE_IMAGE_SEARCH_ANS;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeShort(responseSerialNumber);
            out.writeInt(packetSize);
            out.writeInt(offset);
            for (Integer mm:queryResults){
                out.writeInt(mm);
            }
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

}
