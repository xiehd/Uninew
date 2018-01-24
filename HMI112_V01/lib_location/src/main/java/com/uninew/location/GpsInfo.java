package com.uninew.location;

import android.util.Log;

import java.io.Serializable;
import java.util.Map;

public class GpsInfo implements Serializable {

    private static final long serialVersionUID = 7580406435425801522L;
    /**
     * 是否已定位
     */
    private boolean isPositioned;
    /**
     * 经度
     */
    private double longitude;
    /**
     * 纬度
     */
    private double latitude;
    /**
     * 速度
     */
    private float speed;
    /**
     * 方向
     */
    private float direction;
    /**
     * 海拔
     */
    private double elevation;
    /**
     * 时间
     */
    private long time;
    /**
     * 天线状态：0：断开，1:正常，2-短路
     */
    private int antennaState;
    /**
     * 北斗可见卫星数
     */
    private int visiableSatellite;
    /**
     * GPS可见卫星数
     */
    private int gpsvisiableSatellite;
    /**
     * 北斗有效卫星数
     */
    private int effectiveSatellite;
    /**
     * GPS有效卫星数
     */
    private int gpseffectiveSatellite;
    /**
     * 北斗信噪比:PRN-Value
     */
    private Map<Integer, Integer> signals;
    /**
     * GPS信噪比:PRN-Value
     */
    private Map<Integer, Integer> mgpssignals;

    public GpsInfo() {
        super();
    }

    public GpsInfo(boolean isPositioned, double longitude, double latitude, float speed, long time) {
        this.isPositioned = isPositioned;
        this.longitude = longitude;
        this.latitude = latitude;
        this.speed = speed;
        this.time = time;
    }

    public GpsInfo(boolean isPositioned, double longitude, double latitude,
                   float speed, int direction, double elevation, long time,
                   int antennaState, int visiableSatellite, int effectiveSatellite,
                   Map<Integer, Integer> signals) {
        super();
        this.isPositioned = isPositioned;
        this.longitude = longitude;
        this.latitude = latitude;
        this.speed = speed;
        this.direction = direction;
        this.elevation = elevation;
        this.time = time;
        this.antennaState = antennaState;
        this.visiableSatellite = visiableSatellite;
        this.effectiveSatellite = effectiveSatellite;
        this.signals = signals;
    }

    public boolean isPositioned() {
        return isPositioned;
    }

    public void setPositioned(boolean isPositioned) {
        this.isPositioned = isPositioned;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getAntennaState() {
        return antennaState;
    }

    public void setAntennaState(int antennaState) {
        this.antennaState = antennaState;
    }

    public int getVisiableSatellite() {
        return visiableSatellite;
    }

    public void setVisiableSatellite(int visiableSatellite) {
        this.visiableSatellite = visiableSatellite;
    }

    public int getGpsVisiableSatellite() {
        return gpsvisiableSatellite;
    }

    public void setGpsVisiableSatellite(int gpsvisiableSatellite) {
        this.gpsvisiableSatellite = gpsvisiableSatellite;
    }

    public int getEffectiveSatellite() {
        return effectiveSatellite;
    }

    public void setEffectiveSatellite(int effectiveSatellite) {
        this.effectiveSatellite = effectiveSatellite;
    }

    public int getGpsEffectiveSatellite() {
        return gpseffectiveSatellite;
    }

    public void setGpsEffectiveSatellite(int gpseffectiveSatellite) {
        this.gpseffectiveSatellite = gpseffectiveSatellite;
    }

    public Map<Integer, Integer> getSignals() {
        return signals;
    }

    public void setSignals(Map<Integer, Integer> signals) {
        this.signals = signals;
    }

    public Map<Integer, Integer> getGpsSignals() {
        return this.mgpssignals;
    }

    public void setGpsSignals(Map<Integer, Integer> gpssignals) {
        this.mgpssignals = gpssignals;
    }

    @Override
    public String toString() {
        return "GpsInfo [isPositioned=" + isPositioned + ", longitude="
                + longitude + ", latitude=" + latitude + ", speed=" + speed
                + ", direction=" + direction + ", elevation=" + elevation
                + ", time=" + time + ", antennaState=" + antennaState
                + ", visiableSatellite=" + visiableSatellite
                + ", effectiveSatellite=" + effectiveSatellite + ", signals="
                + signals + "]";
    }

}
