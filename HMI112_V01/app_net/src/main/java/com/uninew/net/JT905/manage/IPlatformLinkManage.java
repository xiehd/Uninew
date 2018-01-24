package com.uninew.net.JT905.manage;

import com.uninew.net.JT905.bean.BaseBean;

/**
 * Created by Administrator on 2017/8/26 0026.
 */

public interface IPlatformLinkManage {

    public void setPlatformLinkCallBack(IPlatformLinkCallBack mPlatformLinkCallBack);

    /**
     * 参数设置
     *
     * @param deviceId           设备ID，小于等于6位的BCD码
     * @param tcpResponseTimeOut tcp应答超时时间
     * @param tcpResendTime      TCP重传次数
     * @param smsResponseTimeOut SMS应答超时时间
     * @param smsResendTime      SMS重传次数
     */
    void setLinkParams(String deviceId, int tcpResponseTimeOut, int tcpResendTime, int smsResponseTimeOut, int smsResendTime);

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
     * 参数设置
     *
     * @param mainIp    主服务器ip
     * @param mainPort  主服务器端口
     * @param spareIp   备用服务器ip
     * @param sparePort 备用服务器端口
     */
    void setTcpParams(int tcpId, String mainIp, int mainPort, String spareIp, int sparePort);

    /**
     * 创建连接
     *
     * @param tcpId TCP编号
     */
    boolean createSocket(int tcpId);

    /**
     * 关闭连接
     *
     * @param tcpId
     */
    boolean closeSocket(int tcpId);

    /**
     * 发送消息
     *
     * @param mBaseBean 消息内容
     */
    void sendMsg(BaseBean mBaseBean);

    /**
     * 设置心跳间隔时间
     * @param time
     */
    void setHeartbeatTime(int time);

    /**
     * 关闭网络监听
     */
    void unRegisterNetWork();

    /**
     * 设置终端ID
     * @param deviceId
     */
    void setDeviceId(String deviceId);
}
