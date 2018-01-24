package com.uninew.net.Alarm.devicefailure;

/**
 * Created by Administrator on 2017/10/19.
 */

public interface IDevicefaiureListener {
    /**
     * 发送设备故障报警
     * @param type 故障类型 见协议 表20
     * @param state 状态 0x00:报警;0x01解除
     */
    void devicefailure(int type,int state);
}
