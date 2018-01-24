package com.uninew.mms.interfaces;

/**
 * Created by Administrator on 2017/11/10.
 */

public interface IMmsBase {
    /**
     * 接收数据上报接口
     * @param ID 设备ID
     * @param datas
     * @pdOid 067f155d-4b07-43a8-909d-405a0cd5e49d
     */
    void onReceiveTcpDatas(int ID,byte[] datas);
}
