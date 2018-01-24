package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 抢答结果信息
 * Created by Administrator on 2017/8/20 0020.
 */

public class P_DriverAnswerOrderAns extends BaseBean {
    /**
     * 业务ID
     */
    private int businessId;
    /**
     * 业务类型
     */
    private int businessType;
    /**
     * 要车时间
     */
    private String needTime;
    /**
     * 乘客位置经度
     */
    private double passengerLongitude;
    /**
     * 乘客位置纬度
     */
    private double passengerLatitude;
    /**
     * 目的地位置经度
     */
    private double targetLongitude;
    /**
     * 目的地位置纬度
     */
    private double targetLatitude;
    /**
     * 电召服务费
     */
    private double serviceCharge;
    /**
     * 乘客电话号码
     */
    private String passengerPhoneNumber;
    /**
     * 业务描述
     */
    private String businessDescription;

    public P_DriverAnswerOrderAns() {
    }

    public P_DriverAnswerOrderAns(byte[] datas) {
        getDataPacket(datas);
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
            out.writeInt((int) (passengerLongitude*10000));
            out.writeInt((int) (passengerLatitude*10000));
            out.writeInt((int) (targetLongitude*10000));
            out.writeInt((int) (targetLatitude*10000));
            int a= (int) (serviceCharge*10);
            out.write(ProtocolTool.intToBcd(a,2));//xxx.x
            out.write(ProtocolTool.stringToByte(passengerPhoneNumber,ProtocolTool.CHARSET_905,11));
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
    public P_DriverAnswerOrderAns getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            businessId = in.readInt();
            businessType = in.readByte();
            byte[] bcdTime = new byte[6];
            in.read(bcdTime);
            needTime = ProtocolTool.bcd2Str(bcdTime);
            passengerLongitude = in.readInt() / 10000.0;
            passengerLatitude = in.readInt() / 10000.0;
            targetLongitude = in.readInt() / 10000.0;
            targetLatitude = in.readInt() / 10000.0;
            byte[] bb = new byte[2];
            in.read(bb);
            String service = ProtocolTool.bcd2Str(bb);
            StringBuilder sb = new StringBuilder();
            sb.append(service.substring(0, 1)).append(".").append(service.substring(1, 2));
            serviceCharge = Float.parseFloat(sb.toString());
            byte[] phone=new byte[11];
            in.read(phone);
            passengerPhoneNumber=new String(phone,ProtocolTool.CHARSET_905);
            byte[] strByte = new byte[datas.length - 40];
            in.read(strByte);
            businessDescription = new String(strByte, ProtocolTool.CHARSET_905);
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
        return BaseMsgID.DRIVER_ANSWER_ORDER_MSG;
    }


    public int getBusinessId() {
        return businessId;
    }

    public int getBusinessType() {
        return businessType;
    }

    public String getNeedTime() {
        return needTime;
    }

    public double getPassengerLongitude() {
        return passengerLongitude;
    }

    public double getPassengerLatitude() {
        return passengerLatitude;
    }

    public double getTargetLongitude() {
        return targetLongitude;
    }

    public double getTargetLatitude() {
        return targetLatitude;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public String getPassengerPhoneNumber() {
        return passengerPhoneNumber;
    }

    public String getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public void setNeedTime(String needTime) {
        this.needTime = needTime;
    }

    public void setPassengerLongitude(double passengerLongitude) {
        this.passengerLongitude = passengerLongitude;
    }

    public void setPassengerLatitude(double passengerLatitude) {
        this.passengerLatitude = passengerLatitude;
    }

    public void setTargetLongitude(double targetLongitude) {
        this.targetLongitude = targetLongitude;
    }

    public void setTargetLatitude(double targetLatitude) {
        this.targetLatitude = targetLatitude;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public void setPassengerPhoneNumber(String passengerPhoneNumber) {
        this.passengerPhoneNumber = passengerPhoneNumber;
    }

    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }

    @Override
    public String toString() {
        return "P_DriverAnswerOrderAns{" +
                "businessId=" + businessId +
                ", businessType=" + businessType +
                ", needTime='" + needTime + '\'' +
                ", passengerLongitude=" + passengerLongitude +
                ", passengerLatitude=" + passengerLatitude +
                ", targetLongitude=" + targetLongitude +
                ", targetLatitude=" + targetLatitude +
                ", serviceCharge=" + serviceCharge +
                ", passengerPhoneNumber='" + passengerPhoneNumber + '\'' +
                ", businessDescription='" + businessDescription + '\'' +
                '}';
    }
}
