package com.uninew.net.Alarm.overtime;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 超时判断
 * Created by Administrator on 2017/10/18.
 */

public class OverTimeJudge implements IOverTimeJudge {

    private static long starteSingleTime;//单次驾驶开始时间 (毫秒)
    private static long startContinuedTime;//当天驾驶开始时间(毫秒)
    private static long currentTime;//本地时间(毫秒)
    private static long longTime1;//单次相隔时间（单位：秒）
    private static long longTime2;//累计相隔时间（单位：秒）
    private static long time1 = 1*60;//单次驾驶限制时间（单位：秒）
    private static long time2 = 2*60;//累计驾驶限制时间（单位：秒）
    private static int timerTime = 5*1000;//计时器运行周期(毫秒)
    private boolean ISsingleAlarm = false;
    private boolean IScontinuedAlarm = false;
    private Context mContext;
    private IOverTimeJudgeListener mIOverTimeJudgeListener;
    private Timer OverTimer;

    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }
    };

    public OverTimeJudge(IOverTimeJudgeListener mIOverTimeJudgeListener) {
        this.mIOverTimeJudgeListener = mIOverTimeJudgeListener;
        startContinuedTime = System.currentTimeMillis();
        starteSingleTime = System.currentTimeMillis();
        // startTimer();
    }

    private TimerTask OverTimerTask = new TimerTask() {
        @Override
        public void run() {
            overTimeManager(System.currentTimeMillis());
        }
    };

    private void startTimer() {
        if (OverTimer == null) {
            OverTimer = new Timer();
            OverTimer.schedule(OverTimerTask,1000,timerTime);//timerTime分钟执行一次
        }
    }

    private void closeTimer() {
        if (OverTimer != null) {
            OverTimer.cancel();
            OverTimer = null;
        }
    }

    @Override
    public void sendSingleTime(int type) {
        if (type == 0) {
            starteSingleTime = System.currentTimeMillis();
            startContinuedTime = System.currentTimeMillis();
            timerTime = 5 * 1000;
            startTimer();
        } else {
            longTime2 += (System.currentTimeMillis() - startContinuedTime) / (1000);
            closeTimer();
            // timerTime = 5*1000*60;
            if (ISsingleAlarm) {
                mIOverTimeJudgeListener.overTime(1, 0, longTime1);
                ISsingleAlarm = false;
            }
            if (IScontinuedAlarm) {
                mIOverTimeJudgeListener.overTime(2, 0, longTime2);
                IScontinuedAlarm = false;
            }
        }
    }


    @Override
    public void sendTimeAlarm(int type) {
        if (type == 0) {
            startTimer();
        } else {
            closeTimer();
        }
    }

    /**
     * 超时处理
     *
     * @param time
     */
    private void overTimeManager(long time) {

        currentTime = time;
        longTime1 = (currentTime - starteSingleTime) / (1000);
        longTime2 += (currentTime - startContinuedTime) / (1000);
        startContinuedTime = currentTime;

        if (longTime1 > time1) {//单次驾驶超时
            mIOverTimeJudgeListener.overTime(1, 1, longTime1);
            ISsingleAlarm = true;
            if (timerTime == 5 * 1000 * 60) {//加快报警周期
                closeTimer();
                timerTime = 5 * 1000;
                startTimer();
            }
        }
        if (longTime2 > time2) {//当天累计驾驶超时
            IScontinuedAlarm = true;
            mIOverTimeJudgeListener.overTime(2, 1, longTime1);
            if (timerTime == 5 * 1000 * 60) {
                closeTimer();
                timerTime = 5 * 1000;
                startTimer();
            }
        }
    }
}
