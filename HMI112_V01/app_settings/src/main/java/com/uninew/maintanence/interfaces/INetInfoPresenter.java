package com.uninew.maintanence.interfaces;

/**
 * Created by Administrator on 2017/9/12.
 */

public interface INetInfoPresenter {


    /**
     * 开始信号监听
     */
    public void startPhoneListener();

    /**
     * 结束信号监听
     */
    public void stopPhoneListener();

    /////////////////////////////////////////下传/////////////////////////////////////////////
    /**
     * SIM卡状态
     *
     * @param state
     */
    void ShowSIMState(String state);

    /**
     * 服务商
     *
     * @param msg
     */
    void ShowFacilitator(String msg);

    /**
     * 信号强度
     *
     * @param signal
     */
    void ShowSignal(int signal);

    /**
     * 是否漫游
     *
     * @param state 否
     *              是
     */
    void ShowRoam(String state);

    /**
     * 网络类型
     *
     * @param type
     */
    void ShowNetType(String type);

    /**
     * 网络状态
     *
     * @param state
     */
    void ShowNetState(String state);

    /**
     * IP地址
     *
     * @param ip
     */
    void ShowIPAddress(String ip);


}
