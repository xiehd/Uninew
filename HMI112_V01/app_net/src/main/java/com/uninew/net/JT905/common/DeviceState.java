package com.uninew.net.JT905.common;

import java.io.Serializable;

/**
 * 设备状态
 * Created by Administrator on 2017/10/31.
 */

public class DeviceState implements Serializable {
    private  int dvr_state = 0x00;//0x01 正常  0x00：故障
    private  int taximeter_state = 0x00;
    private  int carscreen_state = 0x00;
    private  int gps_state = 0x00;
    private  int net_state = 0x00;
    private  int power_state = 0x01;//供电 0x01：正常  0x00 欠压
    private  int rentserver_state = 0x0;
    private int vedio_state = 0x00;
    private  static DeviceState mDeviceState = null;

    public  static DeviceState getInstant(){
        if (mDeviceState == null){
            mDeviceState = new DeviceState();
        }
        return mDeviceState;
    }

    public int getVedio_state() {
        return vedio_state;
    }

    public void setVedio_state(int vedio_state) {
        this.vedio_state = vedio_state;
    }

    public int getDvr_state() {
        return dvr_state;
    }

    public void setDvr_state(int dvr_state) {
        this.dvr_state = dvr_state;
    }

    public int getTaximeter_state() {
        return taximeter_state;
    }

    public void setTaximeter_state(int taximeter_state) {
        this.taximeter_state = taximeter_state;
    }

    public int getCarscreen_state() {
        return carscreen_state;
    }

    public void setCarscreen_state(int carscreen_state) {
        this.carscreen_state = carscreen_state;
    }

    public int getGps_state() {
        return gps_state;
    }

    public void setGps_state(int gps_state) {
        this.gps_state = gps_state;
    }

    public int getNet_state() {
        return net_state;
    }

    public void setNet_state(int net_state) {
        this.net_state = net_state;
    }

    public int getPower_state() {
        return power_state;
    }

    public void setPower_state(int power_state) {
        this.power_state = power_state;
    }

    public int getRentserver_state() {
        return rentserver_state;
    }

    public void setRentserver_state(int rentserver_state) {
        this.rentserver_state = rentserver_state;
    }

    public DeviceState getmDeviceState() {
        return mDeviceState;
    }

    public void setmDeviceState(DeviceState mDeviceState) {
        this.mDeviceState = mDeviceState;
    }
}
