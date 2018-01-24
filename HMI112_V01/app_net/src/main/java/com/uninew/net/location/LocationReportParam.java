package com.uninew.net.location;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 保存位置上报相关参数信息
 * Created by Administrator on 2017/9/2 0002.
 */

public class LocationReportParam {

    //单例，可在不同类中直接设置，设置完成后需要执行下LocationReportHandle中的setReportParam方法才能生效。
    private static LocationReportParam instance;
    public synchronized static LocationReportParam getInstance(){
        if (instance==null){
            instance=new LocationReportParam();
        }
        return instance;
    }

    public static final int ReportStratege_Time=0x00;//定时汇报
    public static final int ReportStratege_Distance=0x01;//定距汇报
    public static final int ReportStratege_TimeAndDistance=0x02;//定时定距汇报

    public static final int ReportPlan_ByACC=0x00;//根据ACC状态
    public static final int ReportPlan_ByEmpty=0x01;//根据空车重车状态
    public static final int ReportPlan_ByLoginAndAcc=0x02;//根据登录和ACC状态
    public static final int ReportPlan_ByLoginAndEmpty=0x03;//根据登录和空重车状态

    private static final int Default_ReportStratege = ReportStratege_Time;
    private static final int Default_ReportPlan = ReportPlan_ByACC;//默认根据ACC状态汇报

    //定时汇报默认参数
    private static final int Default_UnLoginReportIntervalTime=30;//未登录时间间隔（秒）
    private static final int Default_AccOffReportIntervalTime=50;//ACC OFF时间间隔（秒）
    private static final int Default_AccOnfReportIntervalTime=20;//ACC ON时间间隔（秒）
    private static final int Default_EmptyReportIntervalTime=30;//空车时间间隔（秒）
    private static final int Default_NonEmptyReportIntervalTime=20;//重车时间间隔（秒）
    private static final int Default_sleepReportIntervalTime=60;//休眠时时间间隔（秒）
    private static final int Default_emergencyReportIntervalTime=10;//紧急报警时时间间隔（秒）

    //定距汇报默认参数
    private static final int Default_UnLoginReportIntervalDistance=30;//登录距离间隔（米）
    private static final int Default_AccOffReportIntervalDistance=50;//ACC OFF距离间隔（米）
    private static final int Default_AccOnfReportIntervalDistance=20;//ACC ON距离间隔（米）
    private static final int Default_EmptyReportIntervalDistance=30;//空车距离间隔（米）
    private static final int Default_NonEmptyReportIntervalDistance=20;//重车距离间隔（米）
    private static final int Default_sleepReportIntervalDistance=60;//休眠时距离间隔（米）
    private static final int Default_emergencyReportIntervalDistance=10;//紧急报警时距离间隔（米）

    /**
     * 位置汇报策略
     */
    private AtomicInteger reportStrategy = new AtomicInteger(Default_ReportStratege);
    /**
     * 位置汇报方案
     */
    private AtomicInteger reportPlan = new AtomicInteger(Default_ReportPlan);

    //----------------------------------------定时汇报参数---------------------------------

    /**未登录汇报时间间隔*/
    private AtomicInteger unLoginReportIntervalTime = new AtomicInteger(Default_UnLoginReportIntervalTime);
    /** ACC OFF汇报时间间隔*/
    private AtomicInteger accOffReportIntervalTime = new AtomicInteger(Default_AccOffReportIntervalTime);
    /** ACC ON汇报时间间隔*/
    private AtomicInteger accOnReportIntervalTime = new AtomicInteger(Default_AccOnfReportIntervalTime);
    /**空车汇报时间间隔*/
    private AtomicInteger emptyReportIntervalTime = new AtomicInteger(Default_EmptyReportIntervalTime);
    /** 重车汇报时间间*/
    private AtomicInteger noEmptyReportIntervalTime = new AtomicInteger(Default_NonEmptyReportIntervalTime);
    /**休眠汇报时间间隔*/
    private AtomicInteger sleepReportIntervalTime = new AtomicInteger(Default_sleepReportIntervalTime);
    /** 紧急报警汇报时间间隔*/
    private AtomicInteger emergencyReportIntervalTime = new AtomicInteger(Default_emergencyReportIntervalTime);

    //---------------------------------定距汇报参数-------------------------------------------

    /** 未登录汇报距离间隔*/
    private AtomicInteger unLoginReportIntervalDistance = new AtomicInteger(Default_UnLoginReportIntervalDistance);
    /**ACC OFF汇报距离间隔*/
    private AtomicInteger accOffReportIntervalDistance = new AtomicInteger(Default_AccOffReportIntervalDistance);
    /** ACC ON汇报距离间隔*/
    private AtomicInteger accOnReportIntervalDistance = new AtomicInteger(Default_AccOnfReportIntervalDistance);
    /** 空车汇报距离间隔*/
    private AtomicInteger emptyReportIntervalDistance = new AtomicInteger(Default_EmptyReportIntervalDistance);
    /**重车汇报距离间隔*/
    private AtomicInteger noEmptyReportIntervalDistance = new AtomicInteger(Default_NonEmptyReportIntervalDistance);
    /**休眠汇报距离间隔*/
    private AtomicInteger sleepReportIntervalDistance = new AtomicInteger(Default_sleepReportIntervalDistance);
    /** 紧急报警汇报距离间隔*/
    private AtomicInteger emergencyReportIntervalDistance = new AtomicInteger(Default_emergencyReportIntervalDistance);


//-------------------------对外调用接口------------------------------------------
    public int getReportStrategy() {
        return reportStrategy.get();
    }

    public int getReportPlan() {
        return reportPlan.get();
    }

    public int getUnLoginReportIntervalTime() {
        return unLoginReportIntervalTime.get();
    }

    public int getAccOffReportIntervalTime() {
        return accOffReportIntervalTime.get();
    }

    public int getAccOnReportIntervalTime() {
        return accOnReportIntervalTime.get();
    }

    public int getEmptyReportIntervalTime() {
        return emptyReportIntervalTime.get();
    }

    public int getNoEmptyReportIntervalTime() {
        return noEmptyReportIntervalTime.get();
    }

    public int getSleepReportIntervalTime() {
        return sleepReportIntervalTime.get();
    }

    public int getEmergencyReportIntervalTime() {
        return emergencyReportIntervalTime.get();
    }

    public int getUnLoginReportIntervalDistance() {
        return unLoginReportIntervalDistance.get();
    }

    public int getAccOffReportIntervalDistance() {
        return accOffReportIntervalDistance.get();
    }

    public int getAccOnReportIntervalDistance() {
        return accOnReportIntervalDistance.get();
    }

    public int getEmptyReportIntervalDistance() {
        return emptyReportIntervalDistance.get();
    }

    public int getNoEmptyReportIntervalDistance() {
        return noEmptyReportIntervalDistance.get();
    }

    public int getSleepReportIntervalDistance() {
        return sleepReportIntervalDistance.get();
    }

    public int getEmergencyReportIntervalDistance() {
        return emergencyReportIntervalDistance.get();
    }

    public void setReportStrategy(int reportStrategy) {
        this.reportStrategy.set(reportStrategy);
    }

    public void setReportPlan(int reportPlan) {
        this.reportPlan.set(reportPlan);
    }

    public void setUnLoginReportIntervalTime(int unLoginReportIntervalTime) {
        this.unLoginReportIntervalTime.set(unLoginReportIntervalTime);
    }

    public void setAccOffReportIntervalTime(int accOffReportIntervalTime) {
        this.accOffReportIntervalTime.set(accOffReportIntervalTime);
    }

    public void setAccOnReportIntervalTime(int accOnReportIntervalTime) {
        this.accOnReportIntervalTime.set(accOnReportIntervalTime);
    }

    public void setEmptyReportIntervalTime(int emptyReportIntervalTime) {
        this.emptyReportIntervalTime.set(emptyReportIntervalTime);
    }

    public void setNoEmptyReportIntervalTime(int noEmptyReportIntervalTime) {
        this.noEmptyReportIntervalTime.set(noEmptyReportIntervalTime);
    }

    public void setSleepReportIntervalTime(int sleepReportIntervalTime) {
        this.sleepReportIntervalTime.set(sleepReportIntervalTime);
    }

    public void setEmergencyReportIntervalTime(int emergencyReportIntervalTime) {
        this.emergencyReportIntervalTime.set(emergencyReportIntervalTime);
    }

    public void setUnLoginReportIntervalDistance(int unLoginReportIntervalDistance) {
        this.unLoginReportIntervalDistance.set(unLoginReportIntervalDistance);
    }

    public void setAccOffReportIntervalDistance(int accOffReportIntervalDistance) {
        this.accOffReportIntervalDistance.set(accOffReportIntervalDistance);
    }

    public void setAccOnReportIntervalDistance(int accOnReportIntervalDistance) {
        this.accOnReportIntervalDistance.set(accOnReportIntervalDistance);
    }

    public void setEmptyReportIntervalDistance(int emptyReportIntervalDistance) {
        this.emptyReportIntervalDistance.set(emptyReportIntervalDistance);
    }

    public void setNoEmptyReportIntervalDistance(int noEmptyReportIntervalDistance) {
        this.noEmptyReportIntervalDistance.set(noEmptyReportIntervalDistance);
    }

    public void setSleepReportIntervalDistance(int sleepReportIntervalDistance) {
        this.sleepReportIntervalDistance.set(sleepReportIntervalDistance);
    }

    public void setEmergencyReportIntervalDistance(int emergencyReportIntervalDistance) {
        this.emergencyReportIntervalDistance.set(emergencyReportIntervalDistance);
    }

    public LocationReportParam() {
    }

    public LocationReportParam(int reportStrategy, int reportPlan) {
        this.reportStrategy.set(reportStrategy);
        this.reportPlan.set(reportPlan);
    }

    public LocationReportParam(int unLoginReportIntervalTime, int accOffReportIntervalTime,
                               int accOnReportIntervalTime, int emptyReportIntervalTime,
                               int noEmptyReportIntervalTime, int sleepReportIntervalTime,
                               int emergencyReportIntervalTime) {
        this.unLoginReportIntervalTime.set(unLoginReportIntervalTime);
        this.accOffReportIntervalTime.set(accOffReportIntervalTime);
        this.accOnReportIntervalTime.set(accOnReportIntervalTime);
        this.emptyReportIntervalTime.set(emptyReportIntervalTime);
        this.noEmptyReportIntervalTime.set(noEmptyReportIntervalTime);
        this.sleepReportIntervalTime.set(sleepReportIntervalTime);
        this.emergencyReportIntervalTime.set(emergencyReportIntervalTime);
    }

    public LocationReportParam(int unLoginReportIntervalDistance, int accOffReportIntervalDistance,
                               int accOnReportIntervalDistance, int emptyReportIntervalDistance,
                               int noEmptyReportIntervalDistance, int sleepReportIntervalDistance,
                               int emergencyReportIntervalDistance,int non) {
        this.unLoginReportIntervalDistance.set(unLoginReportIntervalDistance);
        this.accOffReportIntervalDistance.set(accOffReportIntervalDistance);
        this.accOnReportIntervalDistance.set(accOnReportIntervalDistance);
        this.emptyReportIntervalDistance.set(emptyReportIntervalDistance);
        this.noEmptyReportIntervalDistance.set(noEmptyReportIntervalDistance);
        this.sleepReportIntervalDistance.set(sleepReportIntervalDistance);
        this.emergencyReportIntervalDistance.set(emergencyReportIntervalDistance);
    }

    public LocationReportParam(int reportStrategy, int reportPlan, int unLoginReportIntervalTime,
                               int accOffReportIntervalTime, int accOnReportIntervalTime, int emptyReportIntervalTime,
                               int noEmptyReportIntervalTime, int sleepReportIntervalTime, int emergencyReportIntervalTime,
                               int unLoginReportIntervalDistance, int accOffReportIntervalDistance, int accOnReportIntervalDistance,
                               int emptyReportIntervalDistance, int noEmptyReportIntervalDistance, int sleepReportIntervalDistance,
                               int emergencyReportIntervalDistance) {
        this.reportStrategy.set(reportStrategy);
        this.reportPlan.set(reportPlan);
        this.unLoginReportIntervalTime.set(unLoginReportIntervalTime);
        this.accOffReportIntervalTime.set(accOffReportIntervalTime);
        this.accOnReportIntervalTime.set(accOnReportIntervalTime);
        this.emptyReportIntervalTime.set(emptyReportIntervalTime);
        this.noEmptyReportIntervalTime.set(noEmptyReportIntervalTime);
        this.sleepReportIntervalTime.set(sleepReportIntervalTime);
        this.emergencyReportIntervalTime.set(emergencyReportIntervalTime);
        this.unLoginReportIntervalDistance.set(unLoginReportIntervalDistance);
        this.accOffReportIntervalDistance.set(accOffReportIntervalDistance);
        this.accOnReportIntervalDistance.set(accOnReportIntervalDistance);
        this.emptyReportIntervalDistance.set(emptyReportIntervalDistance);
        this.noEmptyReportIntervalDistance.set(noEmptyReportIntervalDistance);
        this.sleepReportIntervalDistance.set(sleepReportIntervalDistance);
        this.emergencyReportIntervalDistance.set(emergencyReportIntervalDistance);
    }

}
