package com.uninew.location;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.GpsStatus;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

public class GpsInfoManage implements IGpsInfoManage {

    private boolean D = false;
    private static final String TAG = "GpsInfoManage";
    public static GPSManager gpsManager;
    public static boolean mLocationValid = false;
    private LocationManager locationManager;
    private GpsInfo mGpsInfo;

    private IGpsInfoListener mGpsInfoListener;

    /**
     * 监听
     *
     * @param mContext
     * @param mGpsInfoListener
     */
    public void registerGpsInfoListener(int type, Context mContext, IGpsInfoListener mGpsInfoListener) {
        this.mGpsInfoListener = mGpsInfoListener;
        mGpsInfo = new GpsInfo();
        locationManager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);
        switch (type) {
            case DefineLocation.Location_ReadType_Android:
                setGpsStateListener();
                break;
            case DefineLocation.Location_ReadType_NMEA:
                if (gpsManager == null) {
                    gpsManager = new GPSManager();
                    gpsManager.setGpsStateListener();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 取消监听
     */
    public void unRegisterGpsInfoListener(int type) {
        switch (type) {
            case DefineLocation.Location_ReadType_Android:
                if (locationManager != null && gpsStatelistener != null) {
                    locationManager.removeGpsStatusListener(gpsStatelistener);
                    locationManager = null;
                }
                break;
            case DefineLocation.Location_ReadType_NMEA:
                if (gpsManager != null) {
                    gpsManager.colseGPSManager();
                    gpsManager = null;
                }
                break;
            default:
                break;
        }
        mGpsInfo = null;
    }

    // ------------------------------解析NEMA方法------------------------------------------------------

    /**
     * GPS位置监听类
     *
     * @author Administrator
     */
    public class GPSManager {
        private static final String TAG = "GPSManager";

        public GPSManager() {
            super();
        }

        public void colseGPSManager() {
            if (locationManager != null && nmeaListener != null) {
                locationManager.removeNmeaListener(nmeaListener);
                locationManager = null;
            }
        }

        /**
         * 处理GPS
         */
        public void setGpsStateListener() {
            // 判断GPS是否正常启动
            if (!locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (D) Log.d(TAG, "未开启GPS导航...");
                return;
            }
            // 监听状态
            locationManager.addNmeaListener(nmeaListener);
        }

        private NmeaListener nmeaListener = new NmeaListener() {

            @Override
            public void onNmeaReceived(long timestamp, String nmea) {
                String[] datas = nmea.split("[,*]");
                // Log.v("NmeaListener","datas = "+nmea);
//                Log.i("xhd",datas[0]);
                switch (datas[0]) {
                    case "$GNGGA":
                        parseGNGGA(datas);
                        break;
                    case "$GNRMC":
                        parseGNRMC(datas);
                        break;
                    case "$GPGSV":
                        parseGPGSV(0x01, datas);
                        break;
                    case "$BDGSV":
                        parseGPGSV(0x00, datas);
                        break;
                    case "$BDGSA":
                        parseGNGSA(0x00, datas);
                        break;
                    case "$GPGSA":
                        parseGNGSA(0x01, datas);
                        break;
                    case "$GPTXT":
                        parseGPTXT(datas);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /**
     * 解析GNRMC数据
     *
     * @param gpgsa
     */
    private int effectiveCount;// 有效卫星
    private int maxSatellites;// 可见卫星
    private int antennaState;// 天线状态
    private double latitude;
    private double longitude;
    private float speed;
    private double elevation;
    private float direction;
    private long time;

    public void parseGNGGA(String data[]) {
        if (!data[6].equals("0")) {// 有效定位
            // 海拔高度
            if (data[9] != null && !data[9].equals("")) {
                elevation = Double.parseDouble(data[9]);
            }
        } else {
            //未定位
        }
        if (mGpsInfo != null)
            mGpsInfo.setElevation(elevation);
    }

    public void parseGNRMC(String data[]) {
        if (data[2].equals("A")) {// 有效定位
            // 时间
            StringBuffer time_str = new StringBuffer(data[9] + data[1]);
            time_str.insert(4, "0").insert(4, "2");
            time = parseTime(time_str.toString()) + 8 * 60 * 60 * 1000;
            // 纬度
            if (data[3] != null && !data[3].equals("") && data[3].length() > 3) {
                String d1 = data[3].substring(0, 2);
                String d2 = data[3].substring(2, data[3].length());
                latitude = Double.parseDouble(d2) / 60.0f
                        + Double.parseDouble(d1);
                latitude = ((int) (1000000 * latitude)) / 1000000.0;
            }
            // 经度
            if (data[5] != null && !data[5].equals("") && data[5].length() > 4) {
                String d3 = data[5].substring(0, 3);
                String d4 = data[5].substring(3, data[5].length());
                longitude = Double.parseDouble(d4) / 60.0f
                        + Double.parseDouble(d3);
                longitude = ((int) (1000000 * longitude)) / 1000000.0;
            }
            // 速度
            if (data[7] != null && !data[7].equals("")) {
                speed = Float.parseFloat(data[7]) * 1.842f;// 海里*1.842/小时
                // <===>千米/小时
            }
            // 方向
            if (data[8] != null && !data[8].equals("")
                    && isInteger(data[8])) {
                direction = Float.parseFloat(data[8]);
            }
            //对象赋值
            if (mGpsInfo != null) {
                mGpsInfo.setPositioned(true);
                mGpsInfo.setTime(time);
                mGpsInfo.setLongitude(longitude);
                mGpsInfo.setLatitude(latitude);
                mGpsInfo.setSpeed(speed);
                mGpsInfo.setDirection(direction);
            }
        } else {
            //
            if (mGpsInfo != null) mGpsInfo.setPositioned(false);

        }
        if (mGpsInfoListener != null) mGpsInfoListener.gpsInfo(mGpsInfo);
    }

    /**
     * 判断字符串是否是整数
     */
    public static boolean isInteger(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * 解析时间
     *
     * @param time_str
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    private long parseTime(String time_str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss.SSS");
        Date date = null;
        try {
            if (time_str.length() >= 17) {
                date = dateFormat.parse(time_str);
            } else {
                date = new Date();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 可见卫星
     *
     * @param datas
     */
    int count;
    int currentNumber;
    int prn;
    int signal;
    Map<Integer, Integer> signals = new HashMap<>();
    Map<Integer, Integer> gpssignals = new HashMap<>();

    /**
     * @param type  0x00:北斗  0x01：GPS
     * @param datas
     */
    private void parseGPGSV(int type, String[] datas) {
        //个数
        if (!TextUtils.isEmpty(datas[1]) && datas[1].matches("[0-9]+")) {
            count = Integer.parseInt(datas[1]);
        } else {
            count = 0;
            return;
        }

        if (!TextUtils.isEmpty(datas[2]) && datas[2].matches("[0-9]+")) {
            currentNumber = Integer.parseInt(datas[2]);
            if (currentNumber == 1) {
                if (type == 0x00) {
                    signals.clear();
                } else {
                    gpssignals.clear();
                }
            }
            if (currentNumber <= count) {
                for (int i = 4; i < datas.length - 4; i = i + 4) {
                    prn = parseInteger(datas[i]);
                    signal = parseInteger(datas[i + 3]);
                    if (type == 0x00) {
                        signals.put(prn, signal);
                    } else {
                        gpssignals.put(prn, signal);
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(datas[3]) && datas[3].matches("[0-9]+")) {
            maxSatellites = Integer.parseInt(datas[3]);
        } else {
            maxSatellites = 0;
        }
        if (currentNumber >= count) {
            if (mGpsInfo != null) {
                if (type == 0x00) {
                    mGpsInfo.setSignals(signals);
                    mGpsInfo.setVisiableSatellite(maxSatellites);
                } else {
                    mGpsInfo.setGpsSignals(gpssignals);
                    mGpsInfo.setGpsVisiableSatellite(maxSatellites);
                }
            }
        }
    }

    /**
     * 有效卫星
     *
     * @param type  0x00 ;北斗  0x01:GPS
     * @param datas
     */
    private void parseGNGSA(int type, String[] datas) {

        if (!TextUtils.isEmpty(datas[3])) {
            if (Integer.parseInt(datas[3]) > 32) {
                return;
            } else {
                effectiveCount = 0;
                for (int i = 3; i < 15; i++) {
                    if (!TextUtils.isEmpty(datas[i])) {
                        effectiveCount++;
                    }
                }
            }
        } else {
            effectiveCount = 0;
        }
        if (mGpsInfo != null) {
            if (type == 0x00) {
                mGpsInfo.setEffectiveSatellite(effectiveCount);
            } else {
                mGpsInfo.setGpsEffectiveSatellite(effectiveCount);
            }
        }
    }

    /**
     * 获取天线状态
     *
     * @param datas
     */
    private void parseGPTXT(String[] datas) {
        if ("01".equals(datas[3])) {
            if (datas[4].indexOf("OPEN") != -1) {
                if (D)
                    Log.d(TAG, "天线开路,未接天线");
                antennaState = 0;
            } else if (datas[4].indexOf("OK") != -1) {
                if (D)
//						Log.d(TAG, "天线连接良好");
                    antennaState = 1;
            } else if (datas[4].indexOf("SHORT") != -1) {
                if (D)
                    Log.d(TAG, "天线短路");
                antennaState = 2;
            }
            if (mGpsInfo != null) mGpsInfo.setAntennaState(antennaState);
        }

    }


    // ////////////////////////////GPS位置监听-Android系统接口//////////////////////////////////////////////////
    private long mLastLocationMillis = 0;

    /**
     * 处理GPS
     */
    private void setGpsStateListener() {
        // 判断GPS是否正常启动
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e(TAG, "未开启GPS导航...");
            return;
        }

        // 监听状态
        locationManager.addGpsStatusListener(gpsStatelistener);
        // 绑定监听，有4个参数
        // 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
        // 参数2，位置信息更新周期，单位毫秒
        // 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
        // 参数4，监听
        // 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

        // 1秒更新一次，或最小位移变化超过1米更新一次；
        // 注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0, locationListener);
    }

    private GpsStatus.Listener gpsStatelistener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                // 第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    if (D) Log.i(TAG, "第一次定位");
                    break;
                // 卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    if (D) Log.i(TAG, "卫星状态改变");
                    break;
                // 定位启动
                case GpsStatus.GPS_EVENT_STARTED:
                    if (D) Log.i(TAG, "定位启动");
                    break;
                // 定位结束
                case GpsStatus.GPS_EVENT_STOPPED:
                    if (D) Log.i(TAG, "定位结束");
                    break;
            }
            if (mLocationValid
                    && SystemClock.elapsedRealtime() - mLastLocationMillis > 10000) {
                Log.e(TAG, "location failure!!!");
                mLocationValid = false;
                if (mGpsInfo != null) mGpsInfo.setPositioned(false);
                mGpsInfoListener.gpsInfo(mGpsInfo);
            }
        }

        ;
    };

    /**
     * GPS位置监听
     */
    private LocationListener locationListener = new LocationListener() {

        /**
         * 位置信息变化时触发
         */
        public void onLocationChanged(Location location) {
            if (location != null) {
                mLastLocationMillis = SystemClock.elapsedRealtime();
                if (!mLocationValid) {
                    mLocationValid = true;
                    Log.v(TAG, "location Success!!!");
                }
                latitude = (double) ((int) (location.getLatitude() * 1000000)) / 1000000;
                longitude = (double) ((int) (location.getLongitude() * 1000000)) / 1000000;
                speed = (int) location.getSpeed();
                direction = (int) location.getBearing();
                elevation = location.getAccuracy();
                time = location.getTime();
                if (mGpsInfo != null) {
                    mGpsInfo.setPositioned(true);
                    mGpsInfo.setTime(time);
                    mGpsInfo.setLongitude(longitude);
                    mGpsInfo.setLatitude(latitude);
                    mGpsInfo.setSpeed(speed);
                    mGpsInfo.setDirection(direction);
                    mGpsInfo.setElevation(elevation);
                    mGpsInfoListener.gpsInfo(mGpsInfo);
                }
            }
        }

        /**
         * GPS状态变化时触发
         */
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                // GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    if (D) Log.i(TAG, "当前GPS状态为可见状态");
                    break;
                // GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    if (D) Log.i(TAG, "当前GPS状态为服务区外状态");
                    break;
                // GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    if (D) Log.i(TAG, "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        /**
         * GPS开启时触发
         */
        public void onProviderEnabled(String provider) {
        }

        /**
         * GPS禁用时触发
         */
        public void onProviderDisabled(String provider) {
        }

    };


}
