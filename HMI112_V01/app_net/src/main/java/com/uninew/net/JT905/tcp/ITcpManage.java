package com.uninew.net.JT905.tcp;

/**
 * Created by Administrator on 2017/8/11 0011.
 */

public interface ITcpManage {

    /**
     * 设置监听
     * @param mTcpManagerListener
     */
    public void setTcpManagerListener(ITcpManagerCallBack mTcpManagerListener);

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
     * 发送消息
     * @param tcpId
     * @param datas
     */
    void sendMsg(int tcpId,byte[] datas);
}
