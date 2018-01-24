package com.uninew.car.phone;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

import java.util.Timer;
import java.util.TimerTask;

/**
 * 计时器
 */
public class TimerModel {
    private static volatile TimerModel INSTANCE;
    private TimerCallBack mCallBack = null;
    private Timer clockTimer;
    private ClockTimerTask mClockTimerTask;
    private int mTime = 0;

    private TimerModel() {

    }

    public static TimerModel getInstance() {
        if (INSTANCE != null) {

        } else {
            synchronized (TimerModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TimerModel();
                }
            }
        }
        return INSTANCE;
    }


    public void startClock(TimerCallBack callBack) {
        mCallBack = callBack;
        clockTimer = new Timer();
        mClockTimerTask = new ClockTimerTask();
        clockTimer.schedule(mClockTimerTask, 1000, 1000);
    }

    public void stopClock() {
        if (clockTimer != null) {
            mClockTimerTask.cancel();
            clockTimer.cancel();
            mClockTimerTask = null;
            clockTimer = null;
            mCallBack = null;
            mTime = 0;
        }
    }

    public interface TimerCallBack {
        void onTime(int time);
    }

    private final class ClockTimerTask extends TimerTask {

        @Override
        public void run() {
            mTime++;
            if (mCallBack != null) {
                mCallBack.onTime(mTime);
            }
        }
    }

}
