package com.uninew.mms.interfaces;

/**
 * Created by Administrator on 2017/11/10.
 */

public interface IMMsCallBackListener {
    /**
     * 串口沾包完整数据回调（905协议）
     */
    public interface ITaxiDatasCallBack {
        void taxiDateCallBack(byte[] bytes);
    }
}
