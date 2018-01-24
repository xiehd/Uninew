package com.uninew.maintanence.interfaces;

/**
 * Created by Administrator on 2017/9/12.
 */

public interface IDeviceInsperctView {
    /**
     * DVR状态
     *
     * @param state 0:未连接；1：已连接
     */
    void ShowDvrState(int state);

    /**
     * 计价器状态
     *
     * @param state 0:未连接；1：已连接
     */
    void ShowTaximeterState(int state);

    /**
     * 空车屏状态
     *
     * @param state 0:未连接；1：已连接
     */
    void ShowCarScreenState(int state);

    /**
     * GPS信息
     * @param result
     */
    void ShowGPSInfo(int result);

    /**
     * 通讯模块
     * @param result
     */
    void ShowNetModel(int result);

    /**
     * 供电情况
     * @param result
     */
    void ShowPowerInfo(int result);

    /**
     * 出租服务器状态
     *
     * @param state 0:未连接；1：已连接
     */
    void ShowRentServerState(int state);

    /**
     * 视频服务器状态
     *
     * @param state 0:未连接；1：已连接
     */
    void ShowVideoServerState(int state);
}
