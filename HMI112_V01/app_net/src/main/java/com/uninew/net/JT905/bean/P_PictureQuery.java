package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 存储图像检索
 * Created by Administrator on 2017/8/19 0019.
 */

public class P_PictureQuery extends BaseBean {

    private int cameraId;//摄像头ID
    private int shootReason;//拍照原因
    private long startTime;//起始时间,精确到毫秒
    private long endTime;//结束时间，精确到毫秒

    public P_PictureQuery() {

    }

    public P_PictureQuery(byte[] body) {
        getDataPacket(body);
    }

    public P_PictureQuery(int cameraId, int shootReason, long startTime, long endTime) {
        this.cameraId = cameraId;
        this.shootReason = shootReason;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public int getTcpId() {
        return this.tcpId;
    }

    @Override
    public void setTcpId(int tcpId) {
        this.tcpId = tcpId;
    }

    @Override
    public int getTransportId() {
        return this.transportId;
    }

    @Override
    public void setTransportId(int transportId) {
        this.transportId = transportId;
    }

    @Override
    public String getSmsPhoneNumber() {
        return this.smsPhonenumber;
    }

    @Override
    public void setSmsPhoneNumber(String smsPhonenumber) {
        this.smsPhonenumber = smsPhonenumber;
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
        return BaseMsgID.STORE_IMAGE_SEARCH;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeByte(cameraId);
            out.writeByte(shootReason);
            out.write(ProtocolTool.getBCD12TimeBytes(startTime));
            out.write(ProtocolTool.getBCD12TimeBytes(endTime));
            out.flush();
            datas = stream.toByteArray();
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
        return datas;
    }

    @Override
    public Object getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            cameraId = in.readByte();
            byte[] start=new byte[6];
            in.read(start);
            startTime=ProtocolTool.getTimeFromBCD12(ProtocolTool.bcd2Str(start))*1000;//秒转换成毫秒
            byte[] end=new byte[6];
            in.read(end);
            startTime=ProtocolTool.getTimeFromBCD12(ProtocolTool.bcd2Str(end))*1000;//秒转换成毫秒
        } catch (Exception e) {
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

}
