package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 位置跟踪控制
 * Created by Administrator on 2017/8/19 0019.
 */

public class P_LocationTrackControl extends BaseBean {

    private int attribute;//属性
    private int timeOrDistance;//时间或者距离
    private int continuedTimeOrDistance;//持续时间或者持续距离

    public static final int Attr_TimeAndcontinueTime = 0x00;//按时间间隔，持续时间
    public static final int Attr_DistanceAndContinueDistance = 0x11;//按距离间隔，持续距离
    public static final int Attr_TimeAndContinueDistance = 0x01;//按时间间隔，持续距离
    public static final int Attr_DistanceAndcontinueTime = 0x10;//按距离间隔，持续时间
    public static final int Attr_StopTrackControl = 0xFF;//停止当前跟踪

    public P_LocationTrackControl() {
    }

    public P_LocationTrackControl(byte[] body) {
        getDataPacket(body);
    }

    public P_LocationTrackControl(int attribute, int timeOrDistance, int continuedTimeOrDistance) {
        this.attribute = attribute;
        this.timeOrDistance = timeOrDistance;
        this.continuedTimeOrDistance = continuedTimeOrDistance;
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
        return BaseMsgID.TEMP_LOCATION_QUERY;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeByte(attribute);
            out.writeShort(timeOrDistance);
            out.writeInt(continuedTimeOrDistance);
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
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            attribute = in.readByte();
            timeOrDistance = in.readShort();
            continuedTimeOrDistance = in.readInt();
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

    public int getAttribute() {
        return attribute;
    }

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    public int getTimeOrDistance() {
        return timeOrDistance;
    }

    public void setTimeOrDistance(int timeOrDistance) {
        this.timeOrDistance = timeOrDistance;
    }

    public int getContinuedTimeOrDistance() {
        return continuedTimeOrDistance;
    }

    public void setContinuedTimeOrDistance(int continuedTimeOrDistance) {
        this.continuedTimeOrDistance = continuedTimeOrDistance;
    }

    @Override
    public String toString() {
        return "P_LocationTrackControl{" +
                "attribute=" + attribute +
                ", timeOrDistance=" + timeOrDistance +
                ", continuedTimeOrDistance=" + continuedTimeOrDistance +
                '}';
    }
}
