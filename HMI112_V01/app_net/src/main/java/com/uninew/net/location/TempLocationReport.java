package com.uninew.net.location;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.uninew.location.GpsInfo;
import com.uninew.net.JT905.bean.P_LocationTrackControl;
import com.uninew.net.JT905.bean.T_LocationReport;
import com.uninew.net.JT905.bean.T_LocationTrackAns;
import com.uninew.net.JT905.common.Define;
import com.uninew.net.JT905.util.CalculationTools;
import com.uninew.net.main.LinkService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 临时位置跟踪汇报
 * Created by Administrator on 2017/9/22 0022.
 */

public class TempLocationReport {

    private static final String TAG = "TempLocationReport";
    private static final boolean D = true;

    private LinkService service;
    private static TempLocationReport instance;

    private int currentAttr=0x00;//当前属性
    private int timeOrDistance=15;//时间或者间距（秒或者米）
    private int continueTimeOrDistance=60;//持续时间或者间距(秒或者米)

    private static final int WHAT_STOP = 0x01;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what){
                case WHAT_STOP:
                    stopTimer();
                    break;
            }
        }
    };

    public synchronized static TempLocationReport getInstance(LinkService service) {
        if (instance == null) {
            instance = new TempLocationReport(service);
        }
        return instance;
    }

    public TempLocationReport(LinkService service) {
        this.service = service;
    }

    public void setParam(int currentAttr, int timeOrDistance, int continueTimeOrDistance) {
        this.currentAttr = currentAttr;
        this.timeOrDistance = timeOrDistance;
        this.continueTimeOrDistance = continueTimeOrDistance;
        startResopnse();
    }

    public void startResopnse() {
        //执行临时监控应答
        reStartTimer();
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
        mTimer.schedule(mTimerTask, 2000, 1000);
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

    private void stopTimer(){
        if (mTimer != null && mTimerTask != null) {
            mTimerTask.cancel();
            mTimer.cancel();
            mTimerTask = null;
            mTimer = null;
        }
    }


    private int time = 0;//时间累积参数
    private int totalTime = 0;//所有时间
    private int distance = 0;//距离累计参数
    private int totalDistance = 0;//所有距离
    private int intervalDistance;//每秒距离
    private GpsInfo lastGpsInfo;

    private void sendGpsControl() {
        switch (currentAttr) {
            case P_LocationTrackControl.Attr_TimeAndcontinueTime://定时、持续时间
                distance = 0;
                sendGpsTimeAndcontinueTime();
                break;
            case P_LocationTrackControl.Attr_DistanceAndContinueDistance://定距、持续距离
                time = 0;
                sendGpsDistanceAndContinueDistance();
                break;
            case P_LocationTrackControl.Attr_TimeAndContinueDistance://定时、持续距离
                sendGpsTimeAndContinueDistance();
                break;
            case P_LocationTrackControl.Attr_DistanceAndcontinueTime://定距、持续时间
                sendGpsDistanceAndcontinueTime();
                break;
        }
    }

    /**
     * 定时、持续时间控制
     */
    private void sendGpsTimeAndcontinueTime() {
        time++;
        totalTime++;
        if (time == timeOrDistance) {
            time = 0;
            sendGpsInfo();//发送位置信息
        }
        if (totalTime>=continueTimeOrDistance){
            mHandler.sendEmptyMessage(WHAT_STOP);
            time=0;
            totalTime=0;
        }
    }

    /**
     * 定距、持续距离
     */
    private void sendGpsDistanceAndContinueDistance() {
       if (lastGpsInfo == null) {
            lastGpsInfo = LocationReportHandle.mGpsInfo;
        } else {
           intervalDistance= (int) CalculationTools.getDistance(LocationReportHandle.mGpsInfo.getLongitude(), LocationReportHandle.mGpsInfo.getLatitude(),
                    lastGpsInfo.getLongitude(), lastGpsInfo.getLatitude());
            distance+=intervalDistance;
            lastGpsInfo = LocationReportHandle.mGpsInfo;
            totalDistance += intervalDistance;
           if (distance >= timeOrDistance) {
               distance = 0;
               sendGpsInfo();//发送位置信息
           }
       }
        if (totalDistance>=continueTimeOrDistance){
            mHandler.sendEmptyMessage(WHAT_STOP);
            distance=0;
            totalDistance=0;
            intervalDistance=0;
        }
    }

    /**
     * 定时、持续距离
     */
    private void sendGpsTimeAndContinueDistance() {
        time++;
        if (time == timeOrDistance) {
            time = 0;
            sendGpsInfo();//发送位置信息
        }
        if (lastGpsInfo == null) {
            lastGpsInfo = LocationReportHandle.mGpsInfo;
        } else {
            intervalDistance += (int) CalculationTools.getDistance(LocationReportHandle.mGpsInfo.getLongitude(), LocationReportHandle.mGpsInfo.getLatitude(),
                    lastGpsInfo.getLongitude(), lastGpsInfo.getLatitude());
            distance+=intervalDistance;
            lastGpsInfo = LocationReportHandle.mGpsInfo;
            totalDistance += intervalDistance;
        }
        if (totalDistance>=continueTimeOrDistance){
            mHandler.sendEmptyMessage(WHAT_STOP);
            time=0;
            distance=0;
            totalDistance=0;
            intervalDistance=0;
        }
    }

    /**
     * 定距、持续时间
     */
    private void sendGpsDistanceAndcontinueTime() {
        totalTime++;
        if (lastGpsInfo == null) {
            lastGpsInfo = LocationReportHandle.mGpsInfo;
        } else {
            intervalDistance += (int) CalculationTools.getDistance(LocationReportHandle.mGpsInfo.getLongitude(), LocationReportHandle.mGpsInfo.getLatitude(),
                    lastGpsInfo.getLongitude(), lastGpsInfo.getLatitude());
            distance+=intervalDistance;
            lastGpsInfo = LocationReportHandle.mGpsInfo;
            totalDistance += intervalDistance;
            if (distance >= timeOrDistance) {
                distance = 0;
                sendGpsInfo();//发送位置信息
            }
        }
        if (totalTime>=continueTimeOrDistance){
            mHandler.sendEmptyMessage(WHAT_STOP);
            totalTime=0;
            distance=0;
            totalDistance=0;
            intervalDistance=0;
        }
    }

    private void sendGpsInfo() {
        if (service.connectStateMap == null)
            return;
        if (service.connectStateMap.get(Define.TCP_ONE) != null && service.connectStateMap.get(Define.TCP_ONE).linkState == 0x01) {
            //已连接，消息发送
            T_LocationReport report = new T_LocationReport(
                    LocationReportHandle.currentWarnFlag
                    ,LocationReportHandle.currentWarnFlag
                    ,LocationReportHandle.mGpsInfo.getLongitude()
                    ,LocationReportHandle.mGpsInfo.getLatitude()
                    ,LocationReportHandle.mGpsInfo.getElevation()
                    ,LocationReportHandle.mGpsInfo.getSpeed()
                    ,LocationReportHandle.mGpsInfo.getDirection()
                    ,LocationReportHandle.mGpsInfo.getTime()
            );
            service.sendToJT905(new T_LocationTrackAns(report));
            if (D) Log.d(TAG,"-----TempLocationReport----"+report.toString());
        }
    }

}
