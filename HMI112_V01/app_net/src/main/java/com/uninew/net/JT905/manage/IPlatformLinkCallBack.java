package com.uninew.net.JT905.manage;

import com.uninew.net.JT905.common.IpInfo;
import com.uninew.net.JT905.protocol.ProtocolPacket;

/**
 * Created by Administrator on 2017/8/29 0026.
 */

public interface IPlatformLinkCallBack {

    /**
     * 连接状态上报接口
     * @param tcpId  tcp编号
     * @param linkState
     */
    void onTCPLinkState(int tcpId, boolean isMainIpLinked, IpInfo ipInfo, PlatformLinkState linkState);

    /**
     * 接收数据上报接口
     * @param packet
     * @pdOid 067f155d-4b07-43a8-909d-405a0cd5e49d
     */
    void onReceiveDatas(ProtocolPacket packet);


    /**
     * 通用应答
     * @param responseId
     * @param  responseSerialNumber
     * @param result
     */
    void onGeneralResponse(int tcpId,int responseId,int responseSerialNumber,int result);

}
