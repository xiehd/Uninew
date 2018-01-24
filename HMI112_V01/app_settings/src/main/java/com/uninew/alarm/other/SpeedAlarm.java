package com.uninew.alarm.other;

/**
 * Created by Administrator on 2017/9/13.
 * 报警实体类
 */

public class SpeedAlarm {

    private int alarmid = 0;//报警ID，0：速度报警；1：设备故障；2：其他
    private String type;//报警类型
    private String startTime;
    private String endTime;
    private String continuedTime;//持续时间

    public SpeedAlarm() {
    }

    public SpeedAlarm(int alarmid, String type, String startTime, String endTime, String continuedTime) {
        this.alarmid = alarmid;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.continuedTime = continuedTime;
    }

    public int getAlarmid() {
        return alarmid;
    }

    public void setAlarmid(int alarmid) {
        this.alarmid = alarmid;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public void setEndTmine(String endTmine) {
        this.endTime = endTmine;
    }

    public String getContinuedTime() {
        return continuedTime;
    }

    public void setContinuedTime(String continuedTime) {
        this.continuedTime = continuedTime;
    }
}
