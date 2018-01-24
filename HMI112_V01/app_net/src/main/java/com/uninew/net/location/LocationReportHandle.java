package com.uninew.net.location;

import android.util.Log;

import com.uninew.car.db.location.LocationLocalDataSource;
import com.uninew.car.db.location.LocationMessage;
import com.uninew.location.GpsInfo;
import com.uninew.net.JT905.bean.T_LocationReport;
import com.uninew.net.JT905.common.Define;
import com.uninew.net.JT905.common.IpInfo;
import com.uninew.net.JT905.util.CalculationTools;
import com.uninew.net.main.LinkService;
import com.uninew.net.main.PublicVar;


import java.text.ParseException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import tools.TimeTool;

/**
 * 位置上报管理
 * 包括定时上报、定距上报、盲区等
 * Created by Administrator on 2017/8/31 0031.
 */

public class LocationReportHandle {

    private static boolean D = true;
    private static String TAG = "LocationReportHandle";

    public volatile static GpsInfo mGpsInfo = new GpsInfo();
    public static T_LocationReport currentLocation;
    private LocationReportParam reportParam;
    private LinkService service;

    private int currentIntervalTime;
    private int currentIntervalDistance;
    private int currentReportStratege;

    public static int currentWarnFlag = 0;//当前的报警标志
    public static int currentState = 0;//当前状态标志

    private GpsInfo lastGpsInfo;

    public LocationReportHandle(LinkService service) {
        this.service = service;
        reportParam = LocationReportParam.getInstance();
        currentLocation = new T_LocationReport();
        setReportParams();
        reStartTimer();
    }

//    private void init() {
//        mGpsInfo.setElevation(54.0);
//        mGpsInfo.setLatitude(54.456526);
//        mGpsInfo.setDirection(45.2f);
//        mGpsInfo.setLongitude(112.012214);
//        mGpsInfo.setSpeed(50.0f);
//        mGpsInfo.setTime(System.currentTimeMillis());
//    }

    public void setEmergencyState(boolean isEmergencyOn) {
        if (isEmergencyOn) {
            currentIntervalTime = reportParam.getEmergencyReportIntervalTime();
            currentIntervalDistance = reportParam.getEmergencyReportIntervalDistance();
        } else {
            setReportParams();
        }
    }

    public void setReportStrategy(int reportStrategy) {
        reportParam.setReportStrategy(reportStrategy);
    }

    public void setReportPlan(int reportPlan) {
        reportParam.setReportPlan(reportPlan);
    }

    public void setUnLoginReportIntervalTime(int unLoginReportIntervalTime) {
        reportParam.setUnLoginReportIntervalTime(unLoginReportIntervalTime);
    }

    public void setAccOffReportIntervalTime(int accOffReportIntervalTime) {
        reportParam.setAccOffReportIntervalTime(accOffReportIntervalTime);
    }

    public void setAccOnReportIntervalTime(int accOnReportIntervalTime) {
        reportParam.setAccOnReportIntervalTime(accOnReportIntervalTime);
    }

    public void setEmptyReportIntervalTime(int emptyReportIntervalTime) {
        this.reportParam.setEmptyReportIntervalTime(emptyReportIntervalTime);
    }

    public void setNoEmptyReportIntervalTime(int noEmptyReportIntervalTime) {
        this.reportParam.setNoEmptyReportIntervalTime(noEmptyReportIntervalTime);
    }

    public void setSleepReportIntervalTime(int sleepReportIntervalTime) {
        this.reportParam.setSleepReportIntervalTime(sleepReportIntervalTime);
    }

    public void setEmergencyReportIntervalTime(int emergencyReportIntervalTime) {
        this.reportParam.setEmergencyReportIntervalTime(emergencyReportIntervalTime);
    }

    public void setUnLoginReportIntervalDistance(int unLoginReportIntervalDistance) {
        this.reportParam.setUnLoginReportIntervalDistance(unLoginReportIntervalDistance);
    }

    public void setAccOffReportIntervalDistance(int accOffReportIntervalDistance) {
        this.reportParam.setAccOffReportIntervalDistance(accOffReportIntervalDistance);
    }

    public void setAccOnReportIntervalDistance(int accOnReportIntervalDistance) {
        this.reportParam.setAccOnReportIntervalDistance(accOnReportIntervalDistance);
    }

    public void setEmptyReportIntervalDistance(int emptyReportIntervalDistance) {
        this.reportParam.setEmptyReportIntervalDistance(emptyReportIntervalDistance);
    }

    public void setNoEmptyReportIntervalDistance(int noEmptyReportIntervalDistance) {
        this.reportParam.setNoEmptyReportIntervalDistance(noEmptyReportIntervalDistance);
    }

    public void setSleepReportIntervalDistance(int sleepReportIntervalDistance) {
        this.reportParam.setSleepReportIntervalDistance(sleepReportIntervalDistance);
    }

    public void setEmergencyReportIntervalDistance(int emergencyReportIntervalDistance) {
        this.reportParam.setEmergencyReportIntervalDistance(emergencyReportIntervalDistance);
    }

    private Timer mTimer;
    private TimerTask mTimerTask;

    private void setTimerTask() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                sendGpsControl();
            }
        };
        mTimer.schedule(mTimerTask, 3000, 1000);
    }

    private void reStartTimer() {
        if (mTimer != null && mTimerTask != null) {
            mTimerTask.cancel();
            mTimer.cancel();
            mTimerTask = null;
            mTimer = null;
        }
        setTimerTask();
    }

    private int i = 0;
    private int distance;

    private void sendGpsControl() {
        switch (currentReportStratege) {
            case LocationReportParam.ReportStratege_Time://定时
                distance = 0;
                sendGpsIntervalTime();
                break;
            case LocationReportParam.ReportStratege_Distance://定距
                i = 0;
                sendGpsIntervalDistance();
                break;
            case LocationReportParam.ReportStratege_TimeAndDistance://定时定距
                sendGpsIntervalTime();
                sendGpsIntervalDistance();
                break;
        }
    }

    private void sendGpsIntervalTime() {
        i++;
//        if (D) Log.d(TAG, "定时上传位置信息,time:" + i);
        if (i == currentIntervalTime) {
            i = 0;
            sendGpsInfo();//发送位置信息
        }
    }

    private void sendGpsIntervalDistance() {
        if (D) Log.d(TAG, "定距上传位置信息");
        if (lastGpsInfo == null) {
            lastGpsInfo = mGpsInfo;
        } else {
            distance += (int) CalculationTools.getDistance(mGpsInfo.getLongitude(), mGpsInfo.getLatitude(),
                    lastGpsInfo.getLongitude(), lastGpsInfo.getLatitude());
//            distance ++;
            lastGpsInfo = mGpsInfo;
            if (D) Log.d(TAG, "distance:" + distance);
            if (distance >= currentIntervalDistance) {
                distance = 0;
                sendGpsInfo();//发送位置信息
            }
        }
    }

    private void sendGpsInfo() {
        if (service.connectStateMap == null) {
            return;
        }
        if (service.connectStateMap.containsKey(Define.TCP_ONE)) {
            IpInfo ipInfo = service.connectStateMap.get(Define.TCP_ONE);
            if (ipInfo != null && ipInfo.linkState == 0x01) {
                //已连接，消息发送
                currentLocation.setAlarmIndication(currentWarnFlag);
                currentLocation.setState(currentState);
                currentLocation.setLongitude(mGpsInfo.getLongitude());
                currentLocation.setLatitude(mGpsInfo.getLatitude());
                currentLocation.setElevation(mGpsInfo.getElevation());
                currentLocation.setSpeed(mGpsInfo.getSpeed());
                currentLocation.setDirection(mGpsInfo.getDirection());
                currentLocation.setTime(mGpsInfo.getTime());
                currentLocation.setTcpId(Define.TCP_ONE);
                service.sendToJT905(currentLocation);
                if (D) Log.d(TAG, "currentLocation:" + currentLocation);
            } else {
                //未连接，盲区数据保存
                LocationMessage locationMessage = new LocationMessage();
                locationMessage.setAlarmFlag(currentWarnFlag);
                locationMessage.setState(currentState);
                locationMessage.setLongitude(mGpsInfo.getLongitude());
                locationMessage.setLatitude(mGpsInfo.getLatitude());
                locationMessage.setElevation(mGpsInfo.getElevation());
                locationMessage.setSpeed(mGpsInfo.getSpeed());
                locationMessage.setDirection(mGpsInfo.getDirection());
                try {
                    locationMessage.setTime(TimeTool.formatDate(new Date(mGpsInfo.getTime())));
                    locationMessage.setUploadTime(TimeTool.formatDate(new Date(System.currentTimeMillis())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                LocationLocalDataSource.getInstance(service.getApplicationContext()).saveDBData(locationMessage);
                if (D) Log.d(TAG, "locationMessage:" + locationMessage);
            }
        }
        if (service.connectStateMap.containsKey(Define.TCP_TWO)) {
            IpInfo ipInfo = service.connectStateMap.get(Define.TCP_TWO);
            if (ipInfo != null && ipInfo.linkState == 0x01) {
                //已连接，消息发送
                T_LocationReport location = new T_LocationReport();
                location.setAlarmIndication(currentWarnFlag);
                location.setState(currentState);
                location.setLongitude(mGpsInfo.getLongitude());
                location.setLatitude(mGpsInfo.getLatitude());
                location.setElevation(mGpsInfo.getElevation());
                location.setSpeed(mGpsInfo.getSpeed());
                location.setDirection(mGpsInfo.getDirection());
                location.setTime(mGpsInfo.getTime());
                location.setTcpId(Define.TCP_TWO);
                service.sendToJT905(location);
                if (D) Log.d(TAG, "Location:" + location);
            }
        }
        //超速判断
        service.mAlarmManager.mOverSpeedJudge.sendSpeed(mGpsInfo.getSpeed());
    }


    /**
     * 初始化上报参数（对象初始化和参数信息改变时执行该方法）
     */
    public void setReportParams() {
        if (D) Log.d(TAG, "----------setReportParams-----------");
        currentReportStratege = reportParam.getReportStrategy();
        switch (reportParam.getReportPlan()) {
            case LocationReportParam.ReportPlan_ByACC://根据ACC状态
                getAccParam();
                break;
            case LocationReportParam.ReportPlan_ByEmpty://根据空车重车
                getEmptyNonEmptyParam();
                break;
            case LocationReportParam.ReportPlan_ByLoginAndAcc://根据登录和ACC状态
                if (PublicVar.Login_State == PublicVar.Login_OFF) {
                    currentIntervalTime = reportParam.getUnLoginReportIntervalTime();
                    currentIntervalDistance = reportParam.getUnLoginReportIntervalDistance();
                } else {
                    getAccParam();
                }
                break;
            case LocationReportParam.ReportPlan_ByLoginAndEmpty://根据登录和空重车
                if (PublicVar.Login_State == PublicVar.Login_OFF) {
                    currentIntervalTime = reportParam.getUnLoginReportIntervalTime();
                    currentIntervalDistance = reportParam.getUnLoginReportIntervalDistance();
                } else {
                    getEmptyNonEmptyParam();
                }
                break;
        }
    }

    private void getAccParam() {
        if (PublicVar.ACC_State == PublicVar.ACC_ON) {
            currentIntervalTime = reportParam.getAccOnReportIntervalTime();
            currentIntervalDistance = reportParam.getAccOnReportIntervalDistance();
        } else {
            currentIntervalTime = reportParam.getAccOffReportIntervalTime();
            currentIntervalDistance = reportParam.getAccOffReportIntervalDistance();
        }
    }

    private void getEmptyNonEmptyParam() {
        if (PublicVar.Car_State == PublicVar.Car_Empty) {
            currentIntervalTime = reportParam.getEmptyReportIntervalTime();
            currentIntervalDistance = reportParam.getEmptyReportIntervalDistance();
        } else {
            currentIntervalTime = reportParam.getNoEmptyReportIntervalTime();
            currentIntervalDistance = reportParam.getNoEmptyReportIntervalDistance();
        }
    }
}
