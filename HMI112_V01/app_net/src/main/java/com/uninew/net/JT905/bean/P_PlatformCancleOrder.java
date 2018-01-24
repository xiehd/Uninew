package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 平台取消订单
 * Created by Administrator on 2017/8/20 0020.
 */

public class P_PlatformCancleOrder extends BaseBean {

    private int businessId;

    public P_PlatformCancleOrder() {
    }

    public P_PlatformCancleOrder(byte[] dats) {
        getDataPacket(dats);
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
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
        return BaseMsgID.PLATFORM_CANCLE_ORDER;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeInt(businessId);
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
    public P_PlatformCancleOrder getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            businessId = in.readInt();
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
