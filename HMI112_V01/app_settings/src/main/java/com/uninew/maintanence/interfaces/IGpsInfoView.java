package com.uninew.maintanence.interfaces;

import java.util.Map;

/**
 * Created by Administrator on 2017/9/12.
 */

public interface IGpsInfoView {

    /**
     * 定位状态
     * @param state
     * @param type 0:GPS;1:北斗
     */
    void ShowLocationSate(int type,String state);

    /**
     * 定位信息
     * @param longitude 经度
     * @param latitude 纬度
     * @param sateNumber 搜星数
     * @param userNumber 有效卫星数
     */
    void ShowLocationInfo(double longitude,double latitude,int sateNumber,int userNumber,int gpsNumber,int bdNUmber);
    /**
     * 信号强度
     * @param signals
     */
    void ShowLocationgSignals(Map<Integer, Integer> signals);
}
