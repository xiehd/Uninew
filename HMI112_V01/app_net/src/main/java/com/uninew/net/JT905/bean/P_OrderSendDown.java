package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 订单任务下发
 * Created by Administrator on 2017/8/20 0020.
 *
 */

public class P_OrderSendDown extends BaseBean {
    /**业务ID*/
    private int businessId;
    /**业务类型*/
    private int businessType;
    /**要车时间:YYMMDDhhmmss*/
    private String needTime;
    /**业务描述*/
    private String businessDescription;

    public P_OrderSendDown() {
    }

    public P_OrderSendDown(byte[] datas) {
        getDataPacket(datas);
    }

    public P_OrderSendDown(int businessId, int businessType, String needTime, String businessDescription) {
        this.businessId = businessId;
        this.businessType = businessType;
        this.needTime = needTime;
        this.businessDescription = businessDescription;
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
        return BaseMsgID.ORDER_SEND_DOWN;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public String getNeedTime() {
        return needTime;
    }

    public void setNeedTime(String needTime) {
        this.needTime = needTime;
    }

    public String getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeInt(businessId);
            out.writeByte(businessType);
            out.write(ProtocolTool.str2Bcd(needTime));
            out.write(ProtocolTool.stringToByte(businessDescription,ProtocolTool.CHARSET_905));
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
    public P_OrderSendDown getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            businessId=in.readInt();
            businessType=in.readByte();
            byte[] bcdTime=new byte[6];
            in.read(bcdTime);
            needTime= ProtocolTool.bcd2Str(bcdTime);
            byte[] strByte=new byte[datas.length-11];
            in.read(strByte);
            businessDescription=new String(strByte,ProtocolTool.CHARSET_905);
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

    @Override
    public String toString() {
        return "P_OrderSendDown{" +
                "businessId=" + businessId +
                ", businessType=" + businessType +
                ", needTime='" + needTime + '\'' +
                ", businessDescription='" + businessDescription + '\'' +
                '}';
    }
}
