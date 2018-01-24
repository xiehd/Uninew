package com.uninew.car.db.signs;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/10/11 0011.
 */

public class SignOrSignOut {
    private int id;
    /*报警*/
    private int alarm;
    /*设备状态*/
    private int state;
    /*纬度*/
    private double latitude;
    /**
     * 高程 m
     **/
    private double elevation;
    /*经度*/
    private double longitude;
    /*速度*/
    private double speed;
    /*方向*/
    private double direction;
    /*时间*/
    private String time;
    /*签到签退状态*/
    private int signState;
    /*企业经营许可证*/
    private String plateNumber;
    /*驾驶员从业许可证*/
    private String businessLicense;
    /*车牌号码*/
    private String driverCertificate;
    /**计价器K值*/
    private int meterKValue;
    /*开机时间*/
    private String bootTime;
    /*关机时间*/
    private String shutdownTime;
    /*当班运营里程*/
    private double runMileage;
    /*当班里程*/
    private double onDutyMileage;
    /*车次*/
    private int carTimes;
    /*计时时间*/
    private String timingTime;
    /*总计金额*/
    private double amount;
    /*卡收金额*/
    private double cashCardAmount;
    /*卡次*/
    private int cardTimes;
    /*夜间里程*/
    private double nightMileage;
    /*总计里程*/
    private double allMileage;
    /*总营运里程*/
    private double revenueAllMileage;
    /*总营运次数*/
    private int revenueTimes;
    /*单价*/
    private double price;
    /*签退方式*/
    private int signOutType;
    /*扩展属性*/
    private byte[] extendedAttributes;

    public SignOrSignOut() {
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSignState() {
        return signState;
    }

    public void setSignState(int signState) {
        this.signState = signState;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
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

    public String getBootTime() {
        return bootTime;
    }

    public void setBootTime(String bootTime) {
        this.bootTime = bootTime;
    }

    public String getShutdownTime() {
        return shutdownTime;
    }

    public void setShutdownTime(String shutdownTime) {
        this.shutdownTime = shutdownTime;
    }

    public double getRunMileage() {
        return runMileage;
    }

    public void setRunMileage(double runMileage) {
        this.runMileage = runMileage;
    }

    public double getOnDutyMileage() {
        return onDutyMileage;
    }

    public void setOnDutyMileage(double onDutyMileage) {
        this.onDutyMileage = onDutyMileage;
    }

    public int getCarTimes() {
        return carTimes;
    }

    public void setCarTimes(int carTimes) {
        this.carTimes = carTimes;
    }

    public String getTimingTime() {
        return timingTime;
    }

    public void setTimingTime(String timingTime) {
        this.timingTime = timingTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCashCardAmount() {
        return cashCardAmount;
    }

    public void setCashCardAmount(double cashCardAmount) {
        this.cashCardAmount = cashCardAmount;
    }

    public int getCardTimes() {
        return cardTimes;
    }

    public void setCardTimes(int cardTimes) {
        this.cardTimes = cardTimes;
    }

    public double getNightMileage() {
        return nightMileage;
    }

    public void setNightMileage(double nightMileage) {
        this.nightMileage = nightMileage;
    }

    public double getAllMileage() {
        return allMileage;
    }

    public void setAllMileage(double allMileage) {
        this.allMileage = allMileage;
    }

    public double getRevenueAllMileage() {
        return revenueAllMileage;
    }

    public void setRevenueAllMileage(double revenueAllMileage) {
        this.revenueAllMileage = revenueAllMileage;
    }

    public int getRevenueTimes() {
        return revenueTimes;
    }

    public void setRevenueTimes(int revenueTimes) {
        this.revenueTimes = revenueTimes;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSignOutType() {
        return signOutType;
    }

    public void setSignOutType(int signOutType) {
        this.signOutType = signOutType;
    }

    public byte[] getExtendedAttributes() {
        return extendedAttributes;
    }

    public void setExtendedAttributes(byte[] extendedAttributes) {
        this.extendedAttributes = extendedAttributes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public int getMeterKValue() {
        return meterKValue;
    }

    public void setMeterKValue(int meterKValue) {
        this.meterKValue = meterKValue;
    }

    @Override
    public String toString() {
        return "SignOrSignOut{" +
                "id=" + id +
                ", alarm=" + alarm +
                ", state=" + state +
                ", latitude=" + latitude +
                ", elevation=" + elevation +
                ", longitude=" + longitude +
                ", speed=" + speed +
                ", direction=" + direction +
                ", time='" + time + '\'' +
                ", signState=" + signState +
                ", plateNumber='" + plateNumber + '\'' +
                ", businessLicense='" + businessLicense + '\'' +
                ", driverCertificate='" + driverCertificate + '\'' +
                ", meterKValue=" + meterKValue +
                ", bootTime='" + bootTime + '\'' +
                ", shutdownTime='" + shutdownTime + '\'' +
                ", runMileage=" + runMileage +
                ", onDutyMileage=" + onDutyMileage +
                ", carTimes=" + carTimes +
                ", timingTime='" + timingTime + '\'' +
                ", amount=" + amount +
                ", cashCardAmount=" + cashCardAmount +
                ", cardTimes=" + cardTimes +
                ", nightMileage=" + nightMileage +
                ", allMileage=" + allMileage +
                ", revenueAllMileage=" + revenueAllMileage +
                ", revenueTimes=" + revenueTimes +
                ", price=" + price +
                ", signOutType=" + signOutType +
                ", extendedAttributes=" + Arrays.toString(extendedAttributes) +
                '}';
    }
}
