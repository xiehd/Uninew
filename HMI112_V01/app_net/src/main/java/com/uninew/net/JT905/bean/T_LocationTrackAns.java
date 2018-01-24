package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 临时位置跟踪汇报
 * Created by Administrator on 2017/8/19 0019.
 */

public class T_LocationTrackAns extends BaseBean{

    private T_LocationReport locationReport;

    public T_LocationTrackAns() {
    }

    public T_LocationTrackAns(T_LocationReport locationReport) {
        this.locationReport = locationReport;
    }

    public T_LocationReport getLocationReport() {
        return locationReport;
    }

    public void setLocationReport(T_LocationReport locationReport) {
        this.locationReport = locationReport;
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
        return BaseMsgID.TEMP_LOCATION_QUERY_ANS;
    }

    @Override
    public byte[] getDataBytes() {
        return locationReport.getDataBytes();
    }

    @Override
    public Object getDataPacket(byte[] datas) {
        return null;
    }
}
