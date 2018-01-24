package com.uninew.net.JT905.protocol;

import com.uninew.net.JT905.bean.BaseBean;

/**
 * Created by Administrator on 2017/8/11 0011.
 */

public interface IProtocolManage {

    public void setProtocolCallBack(IProtocolCallBack mProtocolCallBack);

    /**
     *
     */
    void setParams(String deviceId, int tcpResponseTimeOut, int tcpResendTime, int smsResponseTimeOut, int smsResendTime);

    /**
     * 设置tcp消息应答超时时间
     * @param tcpResponseTimeOut
     */
    void setTcpResponseTimeOut(int tcpResponseTimeOut);

    /**
     * 设置tcp消息重连次数
     * @param times
     */
    void setTcpResendTime(int times);

    /**
     * 创建连接
     *
     * @param tcpId TCP编号
     * @param ip    ip地址或者域名
     * @param port  端口号
     */
    boolean createSocket(int tcpId, String ip, int port);

    /**
     * 关闭连接
     *
     * @param tcpId
     */
    boolean closeSocket(int tcpId);

    /**
     * 发送消息（TCP）
     *
     * @param mBaseBean 消息内容
     */
    void sendMsgByTcp(BaseBean mBaseBean);

    /**
     * 发送消息（SMS）
     *
     * @param phoneNumber 电话号码
     * @param mBaseBean   消息内容
     */
    void sendMsgBySms(String phoneNumber, BaseBean mBaseBean);

    /**
     * 设置终端ID
     * @param deviceId
     */
    void setDeviceId(String deviceId);
}
