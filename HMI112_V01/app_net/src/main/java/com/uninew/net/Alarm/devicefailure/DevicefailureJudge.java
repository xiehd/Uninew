package com.uninew.net.Alarm.devicefailure;

/**
 * 设备故障
 * Created by Administrator on 2017/10/19.
 */

public class DevicefailureJudge implements IDevicefailureJudge{

   private IDevicefaiureListener mIDevicefaiureListener;

    public DevicefailureJudge(IDevicefaiureListener mIDevicefaiureListener) {
        this.mIDevicefaiureListener = mIDevicefaiureListener;
    }

    @Override
    public void sendfailure(int type,int state) {
        mIDevicefaiureListener.devicefailure(type,state);
    }
}
