package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;

/**
 * 位置汇报数据补传
 * Created by Administrator on 2017/10/12 0012.
 */

public class T_LocationReplenish extends BaseBean{

    private byte[] mBuffer;//0x0200位置汇报中的基本信息

    public T_LocationReplenish(byte[] mBuffer) {
        this.mBuffer = mBuffer;
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
        return BaseMsgID.BLAND_DATA_REPORT;
    }

    @Override
    public byte[] getDataBytes() {
        return mBuffer;
    }

    @Override
    public Object getDataPacket(byte[] datas) {
        return null;
    }
}
