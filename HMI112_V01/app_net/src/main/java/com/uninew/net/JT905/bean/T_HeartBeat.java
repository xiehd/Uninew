package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;

/**
 * Created by Administrator on 2017/8/30 0030.
 */

public class T_HeartBeat extends BaseBean {

    public T_HeartBeat(int tcpId) {
        setTcpId(tcpId);
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
        return BaseMsgID.TERMINAL_HEARBEAT;
    }

    @Override
    public byte[] getDataBytes() {
        return null;
    }

    @Override
    public Object getDataPacket(byte[] datas) {
        return null;
    }
}
