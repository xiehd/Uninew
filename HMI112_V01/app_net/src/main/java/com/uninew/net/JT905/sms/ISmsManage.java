package com.uninew.net.JT905.sms;

/**
 * Created by Administrator on 2017/8/11 0011.
 */

public interface ISmsManage {

    public void setSmsManagerListener(ISmsManagerCallBack smsManagerListener);

    /**
     * 发送短信息
     * @param phoneNumber 电话号码
     * @param datas 消息内容
     */
    void sendSmsMsg(String phoneNumber,byte[] datas);

}
