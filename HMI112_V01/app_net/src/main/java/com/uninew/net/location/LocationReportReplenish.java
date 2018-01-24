package com.uninew.net.location;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.uninew.car.db.location.LocationLocalDataSource;
import com.uninew.car.db.location.LocationLocalSource;
import com.uninew.car.db.location.LocationMessage;
import com.uninew.net.JT905.bean.T_LocationReplenish;
import com.uninew.net.JT905.bean.T_LocationReport;
import com.uninew.net.JT905.util.ByteTools;
import com.uninew.net.main.LinkService;

import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import tools.TimeTool;

/**
 * Created by Administrator on 2017/10/12 0012.
 */

public class LocationReportReplenish {

    private static final String TAG = "LocationReportReplenish";
    private static final boolean D = true;
    private static volatile LocationReportReplenish INSTANCE;
    private Queue<LocationMessage> locationQueue;
    private Timer mTimer;
    private static final int MAX_TIMES = 8;
    private LinkService mService;
    private LocationTimerTask mTimerTask;
    private LocationLocalSource locationLocalSource;
    private static final int WHAT_FINISH = 0x01;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case WHAT_FINISH:
                    stopReplenish();
                    break;
            }
        }
    };


    private LocationReportReplenish(LinkService service) {
        this.mService = service;
        locationQueue = new LinkedList<>();
        locationLocalSource = LocationLocalDataSource.getInstance(service);
    }

    public static LocationReportReplenish getInstance(LinkService service) {
        if (INSTANCE != null) {

        } else {
            synchronized (LocationReportReplenish.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocationReportReplenish(service);
                }
            }
        }
        return INSTANCE;
    }

    public void startReplenish() {
        locationLocalSource.getAllDBDatas(new LocationLocalSource.LoadLocationsCallBack() {
            @Override
            public void onDBBaseDataLoaded(List<LocationMessage> buffers) {
                for (LocationMessage location : buffers) {
                    locationQueue.offer(location);
                }
                mTimer = new Timer();
                mTimerTask = new LocationTimerTask();
                mTimer.schedule(mTimerTask, 100);
            }

            @Override
            public void onDataNotAailable() {

            }
        });
    }

    public void stopReplenish() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimerTask.cancel();
            mTimer = null;
            mTimerTask = null;
        }
    }

    private class LocationTimerTask extends TimerTask {
        private byte[] mBuffer = null;
        private Queue<Integer> idQueue;

        public LocationTimerTask() {
            idQueue = new LinkedList<>();
        }

        @Override
        public void run() {
            if (locationQueue != null) {
                while (!locationQueue.isEmpty()) {
                    if (idQueue == null) {
                        idQueue = new LinkedList<>();
                    }
                    final int size = locationQueue.size();
                    if (size > MAX_TIMES) {
                        for (int i = 0; i < MAX_TIMES; i++) {
                            LocationMessage locationMessage = locationQueue.poll();
                            if (locationMessage != null) {
                                T_LocationReport currentLocation = new T_LocationReport();
                                idQueue.offer(locationMessage.getId());
                                currentLocation.setAlarmIndication(locationMessage.getAlarmFlag());
                                currentLocation.setState(locationMessage.getState());
                                currentLocation.setLongitude(locationMessage.getLongitude());
                                currentLocation.setLatitude(locationMessage.getLatitude());
                                currentLocation.setElevation(locationMessage.getElevation());
                                currentLocation.setSpeed((float) locationMessage.getSpeed());
                                currentLocation.setDirection((float) locationMessage.getDirection());
                                try {
                                    currentLocation.setTime(TimeTool.parseToLong(locationMessage.getTime()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (mBuffer == null) {
                                    mBuffer = currentLocation.getDataBytes();
                                } else {
                                    mBuffer = ByteTools.addBytes(mBuffer, currentLocation.getDataBytes());
                                }
                            }
                        }
                        if (D)
                            Log.d(TAG, "位置补传消息上报,长度:" + MAX_TIMES + ",buffers length:" + mBuffer.length
                                    + "，buffers:" + ByteTools.logBytes(mBuffer));
                        mService.sendToJT905(new T_LocationReplenish(mBuffer));
                        mBuffer = null;
                        for (int j = 0; j < idQueue.size(); j++) {
                            int id = idQueue.poll();
                            locationLocalSource.deleteDBData(id);
                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            LocationMessage locationMessage = locationQueue.poll();
                            if (locationMessage != null) {
                                T_LocationReport currentLocation = new T_LocationReport();
                                idQueue.offer(locationMessage.getId());
                                currentLocation.setAlarmIndication(locationMessage.getAlarmFlag());
                                currentLocation.setState(locationMessage.getState());
                                currentLocation.setLongitude(locationMessage.getLongitude());
                                currentLocation.setLatitude(locationMessage.getLatitude());
                                currentLocation.setElevation(locationMessage.getElevation());
                                currentLocation.setSpeed((float) locationMessage.getSpeed());
                                currentLocation.setDirection((float) locationMessage.getDirection());
                                try {
                                    currentLocation.setTime(TimeTool.parseToLong(locationMessage.getTime()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (mBuffer == null) {
                                    mBuffer = currentLocation.getDataBytes();
                                } else {
                                    mBuffer = ByteTools.addBytes(mBuffer, currentLocation.getDataBytes());
                                }
                            }
                        }
                        if (D) Log.d(TAG, "位置补传消息上报,长度:" + size + ",buffers length:" + mBuffer.length
                                + "，buffers:" + ByteTools.logBytes(mBuffer));
                        mService.sendToJT905(new T_LocationReplenish(mBuffer));
                        mBuffer = null;
                        for (int j = 0; j < idQueue.size(); j++) {
                            int id = idQueue.poll();
                            locationLocalSource.deleteDBData(id);
                        }
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (locationQueue.isEmpty()) {
                        if (D) Log.d(TAG, "补传完成！！");
                        mHandler.sendEmptyMessage(WHAT_FINISH);
                    }
                }
            }
        }
    }
}
