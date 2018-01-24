package com.uninew.net.JT905.sms;

/**
 * Created by Administrator on 2017/8/11 0011.
 */

public interface ISmsManagerCallBack {

    /**
     * 接收数据上报接口
     *
     * @param phoneNumber 平台电话号码
     * @param vBuffer
     * @pdOid 067f155d-4b07-43a8-909d-405a0cd5e49d
     */
    void onSMSReceiveDatas(String phoneNumber, byte[] vBuffer);

}
