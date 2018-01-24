package com.uninew.car.db.attendance;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/9/7 0007.
 */

public class Attendance {
    private int id;
    private int actionTimes;
    private double drivingMileage;
    private double passengerMileage;
    private String urseName;
    private byte[] imgAvatar;
    private String startTime;
    private String endTime;
    private int attendanceState;
    private String jobNumber;

    public Attendance() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActionTimes() {
        return actionTimes;
    }

    public void setActionTimes(int actionTimes) {
        this.actionTimes = actionTimes;
    }

    public double getDrivingMileage() {
        return drivingMileage;
    }

    public void setDrivingMileage(double drivingMileage) {
        this.drivingMileage = drivingMileage;
    }

    public double getPassengerMileage() {
        return passengerMileage;
    }

    public void setPassengerMileage(double passengerMileage) {
        this.passengerMileage = passengerMileage;
    }

    public String getUrseName() {
        return urseName;
    }

    public void setUrseName(String urseName) {
        this.urseName = urseName;
    }

    public byte[] getImgAvatar() {
        return imgAvatar;
    }

    public void setImgAvatar(byte[] imgAvatar) {
        this.imgAvatar = imgAvatar;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getAttendanceState() {
        return attendanceState;
    }

    public void setAttendanceState(int attendanceState) {
        this.attendanceState = attendanceState;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", actionTimes=" + actionTimes +
                ", drivingMileage=" + drivingMileage +
                ", passengerMileage=" + passengerMileage +
                ", urseName='" + urseName + '\'' +
                ", imgAvatar=" + Arrays.toString(imgAvatar) +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", attendanceState=" + attendanceState +
                ", jobNumber='" + jobNumber + '\'' +
                '}';
    }
}
