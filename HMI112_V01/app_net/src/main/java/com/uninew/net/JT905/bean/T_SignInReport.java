package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 上班签到信息上传
 * Created by Administrator on 2017/8/20 0020.
 */

public class T_SignInReport extends BaseBean{

    /**基本位置信息*/
    private T_LocationReport locationReport;
    /**企业经营许可证号*/
    private String businessLicense;
    /**驾驶员从业资格证*/
    private String driverCertificate;
    /**车牌号*/
    private String carNumber;
    /**开机时间：YYYYMMDDhhmm*/
    private String bootTime;
    //扩展
    private byte[] extend;

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
        return BaseMsgID.SIGN_IN_REPORT;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.write(locationReport.getDataBytes());
            out.write(ProtocolTool.stringToByte(businessLicense,ProtocolTool.CHARSET_905,16));
            out.write(ProtocolTool.stringToByte(driverCertificate,ProtocolTool.CHARSET_905,19));
            out.write(ProtocolTool.stringToByte(carNumber,ProtocolTool.CHARSET_905,6));
            out.write(ProtocolTool.str2Bcd(bootTime,6));
            out.write(extend);
            out.flush();
            out.write(locationReport.getDataBytes());
            out.write(ProtocolTool.stringToByte(businessLicense,ProtocolTool.CHARSET_905,16));
            out.write(ProtocolTool.stringToByte(driverCertificate,ProtocolTool.CHARSET_905,19));
            out.write(ProtocolTool.stringToByte(carNumber,ProtocolTool.CHARSET_905,6));
            out.write(ProtocolTool.str2Bcd(bootTime));
            if(extend != null && extend.length > 0){
                out.write(extend);
            }
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

    public T_LocationReport getLocationReport() {
        return locationReport;
    }

    public void setLocationReport(T_LocationReport locationReport) {
        this.locationReport = locationReport;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getDriverCertificate() {
        return driverCertificate;
    }

    public void setDriverCertificate(String driverCertificate) {
        this.driverCertificate = driverCertificate;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getBootTime() {
        return bootTime;
    }

    public void setBootTime(String bootTime) {
        this.bootTime = bootTime;
    }

    public byte[] getExtend() {
        return extend;
    }

    public void setExtend(byte[] extend) {
        this.extend = extend;
    }
}
