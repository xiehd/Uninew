package com.uninew.mms.interfaces;

import com.uninew.net.Taximeter.protocol.McuProtocolPacket;

/**
 * Created by Administrator on 2017/8/17 0011.
 */

public interface IProtocolCallBack {

    /**
     * 计价器接收数据上报接口
     * @param packet
     * @pdOid 067f155d-4b07-43a8-909d-405a0cd5e49d
     */
    void onTaxiReceiveDatas(McuProtocolPacket packet);


}
