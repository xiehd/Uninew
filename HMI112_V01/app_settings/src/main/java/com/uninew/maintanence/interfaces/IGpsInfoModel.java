package com.uninew.maintanence.interfaces;

/**
 * Created by Administrator on 2017/9/12.
 */

public interface IGpsInfoModel {

    /**
     * 监听GPS信息
     *
     */
    public void registerGpsInfoListener();

    /**
     * 取消监听GPS信息
     */
    public void unRegisterGpsInfoListener();

}
