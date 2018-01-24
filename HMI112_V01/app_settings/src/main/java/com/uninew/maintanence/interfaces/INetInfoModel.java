package com.uninew.maintanence.interfaces;

/**
 * Created by Administrator on 2017/9/12.
 */

public interface INetInfoModel {

    /**
     * 开始信号监听
     */
    public void startPhoneListener();

    /**
     * 结束信号监听
     */
    public void stopPhoneListener();




    /**
     * 监听网络信号强度
     * @author Administrator
     */
    public interface INetSignalListener {

        void getSignal(int signal);
    }

    /**
     * 网络状态回调接口
     * @author Administrator
     *
     */
    public interface INetStateCallBack{
        /**
         *
         * @param state 0-不可用，1-可用
         */
        void netStateCallBack(int state);
    }

}
