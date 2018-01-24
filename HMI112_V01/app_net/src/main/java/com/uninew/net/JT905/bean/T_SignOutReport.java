package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/8/20 0020.
 */

public class T_SignOutReport extends BaseBean {

    /**基本位置信息*/
    private T_LocationReport locationReport;
    /**企业经营许可证号*/
    private String businessLicense;
    /**驾驶员从业资格证*/
    private String driverCertificate;
    /**车牌号*/
    private String carNumber;
    /**计价器K值*/
    private int meterKValue;
    /**当班开机时间：YYYYMMDDhhmm*/
    private String bootTime;
    /**当班关机时间：YYYYMMDDhhmm*/
    private String shutDownTime;
    /**当班里程*/
    private float mileage;
    /**当班营运里程*/
    private float operationMileage;
    /**车次*/
    private int trips;
    /**计时时间*/
    private String timingTime;
    /**总计金额*/
    private float totalIncome;
    /**卡收金额*/
    private float cardIncome;
    /**卡次*/
    private int cardTimes;
    /**班间里程*/
    private float betweenMileage;
    /**总计里程*/
    private float totalMileage;
    /**总营运里程*/
    private float totalOperationMileage;
    /**单价*/
    private float unitPrice;
    /**总营运次数*/
    private int totalOperation;
    /**签退方式*/
    private int signOutWay;
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
        return BaseMsgID.SIGN_OUT_REPORT;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.flush();
            out.write(locationReport.getDataBytes());
            out.write(ProtocolTool.stringToByte(businessLicense,ProtocolTool.CHARSET_905,16));
            out.write(ProtocolTool.stringToByte(driverCertificate,ProtocolTool.CHARSET_905,19));
            out.write(ProtocolTool.stringToByte(carNumber,ProtocolTool.CHARSET_905,6));
            out.write(ProtocolTool.intToBcd(meterKValue,2));
            out.write(ProtocolTool.str2Bcd(bootTime));
            out.write(ProtocolTool.str2Bcd(shutDownTime));
            out.write(ProtocolTool.intToBcd((int)(mileage*10),3));
            out.write(ProtocolTool.intToBcd((int)(operationMileage*10),3));
            out.write(ProtocolTool.intToBcd(trips,2));
            out.write(ProtocolTool.str2Bcd(timingTime));
            out.write(ProtocolTool.intToBcd((int)(totalIncome*10),3));
            out.write(ProtocolTool.intToBcd((int)(cardIncome*10),3));
            out.write(ProtocolTool.intToBcd(cardTimes,2));
            out.write(ProtocolTool.intToBcd((int)(betweenMileage*10),2));
            out.write(ProtocolTool.intToBcd((int)(totalMileage*10),4));
            out.write(ProtocolTool.intToBcd((int)(totalOperationMileage*10),4));
            out.write(ProtocolTool.intToBcd((int)(unitPrice*10),2));
            out.writeInt(totalOperation);
            out.writeByte(signOutWay);
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

    public int getMeterKValue() {
        return meterKValue;
    }

    public void setMeterKValue(int meterKValue) {
        this.meterKValue = meterKValue;
    }

    public String getBootTime() {
        return bootTime;
    }

    public void setBootTime(String bootTime) {
        this.bootTime = bootTime;
    }

    public String getShutDownTime() {
        return shutDownTime;
    }

    public void setShutDownTime(String shutDownTime) {
        this.shutDownTime = shutDownTime;
    }

    public float getMileage() {
        return mileage;
    }

    public void setMileage(float mileage) {
        this.mileage = mileage;
    }

    public float getOperationMileage() {
        return operationMileage;
    }

    public void setOperationMileage(float operationMileage) {
        this.operationMileage = operationMileage;
    }

    public int getTrips() {
        return trips;
    }

    public void setTrips(int trips) {
        this.trips = trips;
    }

    public String getTimingTime() {
        return timingTime;
    }

    public void setTimingTime(String timingTime) {
        this.timingTime = timingTime;
    }

    public float getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(float totalIncome) {
        this.totalIncome = totalIncome;
    }

    public float getCardIncome() {
        return cardIncome;
    }

    public void setCardIncome(float cardIncome) {
        this.cardIncome = cardIncome;
    }

    public int getCardTimes() {
        return cardTimes;
    }

    public void setCardTimes(int cardTimes) {
        this.cardTimes = cardTimes;
    }

    public float getBetweenMileage() {
        return betweenMileage;
    }

    public void setBetweenMileage(float betweenMileage) {
        this.betweenMileage = betweenMileage;
    }

    public float getTotalMileage() {
        return totalMileage;
    }

    public void setTotalMileage(float totalMileage) {
        this.totalMileage = totalMileage;
    }

    public float getTotalOperationMileage() {
        return totalOperationMileage;
    }

    public void setTotalOperationMileage(float totalOperationMileage) {
        this.totalOperationMileage = totalOperationMileage;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getTotalOperation() {
        return totalOperation;
    }

    public void setTotalOperation(int totalOperation) {
        this.totalOperation = totalOperation;
    }

    public int getSignOutWay() {
        return signOutWay;
    }

    public void setSignOutWay(int signOutWay) {
        this.signOutWay = signOutWay;
    }

    public byte[] getExtend() {
        return extend;
    }

    public void setExtend(byte[] extend) {
        this.extend = extend;
    }


}
