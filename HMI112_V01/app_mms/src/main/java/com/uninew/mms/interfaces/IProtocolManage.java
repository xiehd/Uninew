package com.uninew.mms.interfaces;

/**
 * Created by Administrator on 2017/8/11 0011.
 */

public interface IProtocolManage {

    public void setProtocolCallBack(IProtocolCallBack mProtocolCallBack);

    /**
     * 接收数据上报接口
     * @param ID 不同协调
     * @param datas
     * @pdOid 067f155d-4b07-43a8-909d-405a0cd5e49d
     */
    void onReceiveTcpDatas(int ID,byte[] datas);

    /**
     * 计价器通用发送方法
     * @param id       232通道
     * @param bytes
     */
    void sendTaxi(int id,byte[] bytes);


}
