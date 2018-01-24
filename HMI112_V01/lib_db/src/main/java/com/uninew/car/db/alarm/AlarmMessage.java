package com.uninew.car.db.alarm;

/**
 * Created by Administrator on 2017/8/31 0031.
 */

public class AlarmMessage {

    private int id;
    private String date;
    private String startTime;
    private String stopTime;
    private int continuedTime;
    private String content;
    private int alarmType;

    public AlarmMessage() {
    }

    public AlarmMessage(String date, String startTime, String stopTime, int continuedTime, String content, int alarmType) {
        this.date = date;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.continuedTime = continuedTime;
        this.content = content;
        this.alarmType = alarmType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public int getContinuedTime() {
        return continuedTime;
    }

    public void setContinuedTime(int continuedTime) {
        this.continuedTime = continuedTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    @Override
    public String toString() {
        return "AlarmMessage{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", stopTime='" + stopTime + '\'' +
                ", continuedTime=" + continuedTime +
                ", content='" + content + '\'' +
                ", alarmType=" + alarmType +
                '}';
    }
}
