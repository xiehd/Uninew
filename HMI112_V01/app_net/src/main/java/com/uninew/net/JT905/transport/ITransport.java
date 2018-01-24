package com.uninew.net.JT905.transport;

/**
 * Created by Administrator on 2017/8/11 0011.
 */

public interface ITransport {

    public void setTransportListener(ITransportCallBack mTransportListener);

    /**
     *创建连接
     * @param tcpId TCP编号
     * @param ip ip地址或者域名
     * @param port 端口号
     */
    boolean createSocket(int tcpId,String ip,int port);

    /**
     * 关闭连接
     * @param tcpId
     */
    boolean closeSocket(int tcpId);

    /**
     * 发送消息（TCP）
     * @param tcpId TCP连接ID
     * @param datas  消息内容
     */
    void sendMsgByTcp(int tcpId,byte[] datas);

    /**
     * 发送消息（SMS）
     * @param phoneNumber 电话号码
     * @param datas  消息内容
     */
    void sendMsgBySms(String phoneNumber,byte[] datas);

}
