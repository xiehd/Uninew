package com.uninew.car.db.location;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/9/8 0008.
 */

public class LocationMessage {
    private int id;
    private int serialNumber;
    private String uploadTime;
    private int alarmFlag;
    private int state;
    private double latitude;
    private double longitude;
    private double speed;
    private double direction;
    private double elevation;
    private String time;
    private byte[] additionalInfo;

    public LocationMessage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlarmFlag() {
        return alarmFlag;
    }

    public void setAlarmFlag(int alarmFlag) {
        this.alarmFlag = alarmFlag;
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

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public byte[] getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(byte[] additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    @Override
    public String toString() {
        return "LocationMessage{" +
                "id=" + id +
                ", serialNumber=" + serialNumber +
                ", uploadTime='" + uploadTime + '\'' +
                ", alarmFlag=" + alarmFlag +
                ", state=" + state +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", speed=" + speed +
                ", direction=" + direction +
                ", elevation=" + elevation +
                ", time='" + time + '\'' +
                ", additionalInfo=" + Arrays.toString(additionalInfo) +
                '}';
    }
}
