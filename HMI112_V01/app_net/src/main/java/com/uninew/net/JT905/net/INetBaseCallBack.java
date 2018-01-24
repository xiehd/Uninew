package com.uninew.net.JT905.net;

import com.uninew.net.JT905.tcp.TCPLinkErrorEnum;
import com.uninew.net.JT905.tcp.TCPRunStateEnum;

/**
 * Created by Administrator on 2017/8/17 0011.
 */

public interface INetBaseCallBack {
    /**
     * 状态上报接口
     * @param tcpId  tcp编号
     * @param vValue
     * @pdOid 5b2906d8-c0a5-437f-a682-ec956c6853dc
     */
    void onTCPRunState(int tcpId, TCPRunStateEnum vValue, TCPLinkErrorEnum error);

    /**
     * 接收数据上报接口
     * @param tcpId
     * @param datas
     * @pdOid 067f155d-4b07-43a8-909d-405a0cd5e49d
     */
    void onReceiveTcpDatas(int tcpId, byte[] datas);

    /**
     * 接收数据上报接口
     * @param phoneNumber
     * @param datas
     * @pdOid 067f155d-4b07-43a8-909d-405a0cd5e49d
     */
    void onReceiveSMSDatas(String phoneNumber, byte[] datas);

}
