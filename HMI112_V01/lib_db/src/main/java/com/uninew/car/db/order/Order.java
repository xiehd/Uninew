package com.uninew.car.db.order;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/8/29 0029.
 */

public class Order implements Serializable {
    /*id*/
    private int id;
    /*业务类型*/
    private int businessType;
    /*订单状态*/
    private int orderState;
    /*业务ID*/
    private int businessId;
    /*下发时间*/
    private String receiveTime;
    /*乘客电话号码*/
    private String passengerPhoneNumber;
    /*要车时间*/
    private String needTime;
    /*乘客位置经度*/
    private double passengerLongitude;
    /*乘客位置纬度*/
    private double passengerLatitude;
    /*目的地址位置经度*/
    private double targatLongitude;
    /*目的地址位置纬度*/
    private double targatLatitude;
    /*电召服务费*/
    private double serviceCharge;
    //    /*完成时间*/
//    private String finishTime;
    /* 业务描述*/
    private String businessDescription;

    public Order() {
    }

    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getPassengerPhoneNumber() {
        return passengerPhoneNumber;
    }

    public void setPassengerPhoneNumber(String passengerPhoneNumber) {
        this.passengerPhoneNumber = passengerPhoneNumber;
    }

    public String getNeedTime() {
        return needTime;
    }

    public void setNeedTime(String needTime) {
        this.needTime = needTime;
    }

    public double getPassengerLongitude() {
        return passengerLongitude;
    }

    public void setPassengerLongitude(double passengerLongitude) {
        this.passengerLongitude = passengerLongitude;
    }

    public double getPassengerLatitude() {
        return passengerLatitude;
    }

    public void setPassengerLatitude(double passengerLatitude) {
        this.passengerLatitude = passengerLatitude;
    }

    public double getTargatLongitude() {
        return targatLongitude;
    }

    public void setTargatLongitude(double targatLongitude) {
        this.targatLongitude = targatLongitude;
    }

    public double getTargatLatitude() {
        return targatLatitude;
    }

    public void setTargatLatitude(double targatLatitude) {
        this.targatLatitude = targatLatitude;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", 业务类型：" + businessType +
                ", 订单状态：" + orderState +
                ", 业务ID：" + businessId +
                ", 下发时间：'" + receiveTime + '\'' +
                ", 乘客电话号码：'" + passengerPhoneNumber + '\'' +
                ", 要车时间：'" + needTime + '\'' +
                ", 乘客位置经度：" + passengerLongitude +
                ", 乘客位置纬度：" + passengerLatitude +
                ", 目的地址位置经度：" + targatLongitude +
                ", 目的地址位置纬度：" + targatLatitude +
                ", 电召服务费：" + serviceCharge +
                ", 业务描述: '" + businessDescription + '\'' +
                '}';
    }
}
