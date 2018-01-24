package com.uninew.net.Alarm.illegalalram;

import java.util.LinkedList;

/**
 * 区域实体类
 * Created by Administrator on 2017/10/21.
 */

public class RegionInfo {

    private int regoinID;//区域ID
    private String regoinName;
    private int regoinType;//区域类型 0 -矩形；1 -多边形；2 -圆形
    private int pointNumber;//顶点数
    private int radius;//为圆形时表示的半径  单位:米
    private LinkedList<Double> lonlist = new LinkedList<>();//经度数组，根据顶点动态设定数组大小
    private LinkedList<Double> latlist = new LinkedList<>();//纬度数组
    private String regoinAlarmType;//区域报警类型，不同报警以','号隔开 如 '2452,2453'表示进域和出域
    private int maxSpeech;//  km/秒
    private int overSpeechTime;// 秒
    private int overTendTime;//wwj Add 20140512 区域滞留超时时长
    private int overStopTime;//wwj Add 20140512 停车超时时长

    public int getRegoinID() {
        return regoinID;
    }

    public void setRegoinID(int regoinID) {
        this.regoinID = regoinID;
    }

    public String getRegoinName() {
        return regoinName;
    }

    public void setRegoinName(String regoinName) {
        this.regoinName = regoinName;
    }

    public int getRegoinType() {
        return regoinType;
    }

    public void setRegoinType(int regoinType) {
        this.regoinType = regoinType;
    }

    public int getPointNumber() {
        return pointNumber;
    }

    public void setPointNumber(int pointNumber) {
        this.pointNumber = pointNumber;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public LinkedList<Double> getLonlist() {
        return lonlist;
    }

    public void setLonlist(LinkedList<Double> lonlist) {
        this.lonlist = lonlist;
    }

    public LinkedList<Double> getLatlist() {
        return latlist;
    }

    public void setLatlist(LinkedList<Double> latlist) {
        this.latlist = latlist;
    }

    public String getRegoinAlarmType() {
        return regoinAlarmType;
    }

    public void setRegoinAlarmType(String regoinAlarmType) {
        this.regoinAlarmType = regoinAlarmType;
    }

    public int getMaxSpeech() {
        return maxSpeech;
    }

    public void setMaxSpeech(int maxSpeech) {
        this.maxSpeech = maxSpeech;
    }

    public int getOverSpeechTime() {
        return overSpeechTime;
    }

    public void setOverSpeechTime(int overSpeechTime) {
        this.overSpeechTime = overSpeechTime;
    }

    public int getOverTendTime() {
        return overTendTime;
    }

    public void setOverTendTime(int overTendTime) {
        this.overTendTime = overTendTime;
    }

    public int getOverStopTime() {
        return overStopTime;
    }

    public void setOverStopTime(int overStopTime) {
        this.overStopTime = overStopTime;
    }
}
