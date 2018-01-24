package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 驾驶员取消订单
 * Created by Administrator on 2017/8/20 0020.
 */

public class T_DriverCancleOrder extends BaseBean{

    private int businessId;//业务ID
    private int cancleReason;//取消原因

    public T_DriverCancleOrder() {
    }

    public T_DriverCancleOrder(int businessId, int cancleReason) {
        this.businessId = businessId;
        this.cancleReason = cancleReason;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public int getCancleReason() {
        return cancleReason;
    }

    public void setCancleReason(int cancleReason) {
        this.cancleReason = cancleReason;
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
        return BaseMsgID.DRIVER_CANCLE_ORDER;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeInt(businessId);
            out.writeByte(cancleReason);
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
