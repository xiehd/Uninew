package com.uninew.net.test;

import com.uninew.net.JT905.bean.BaseBean;
import com.uninew.net.JT905.common.BaseMsgID;

/**
 * 模拟报警下发数据
 * Created by Administrator on 2017/10/23.
 */

public class p_Alarm extends BaseBean {
    private int ID = BaseMsgID.PLATFORM_CONFIRM_ALARM;//确认/解除报警

    public p_Alarm() {
    }

    public p_Alarm(int ID) {
        this.ID = ID;
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
        return ID;
    }


    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        datas = new byte[0];
        return datas;
    }

    @Override
    public p_Alarm getDataPacket(byte[] datas) {
        return this;
    }
}
