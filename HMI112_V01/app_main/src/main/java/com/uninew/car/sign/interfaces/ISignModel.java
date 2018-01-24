package com.uninew.car.sign.interfaces;

/**
 * Created by Administrator on 2017/9/14.
 */

public interface ISignModel {

    /**
     * 签退
     *
     * @param number 工号
     */
    void SetSignOut(String number);

    /**
     * 签到
     *
     * @param number 工号
     */
    void SetSignIn(String number);
    /**
     * 打开监听
     */
    void registerConnectStateListener();
    /**
     * 注册监听
     */
    void unregisterConnectStateListener();
}
