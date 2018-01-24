package com.uninew.net.Taximeter.bean;

/**
 * 运价参数设置应答
 * Created by Administrator on 2017/10/16.
 */

public class P_TaxiSetResult extends BaseMcuBean {
    private static final long serialVersionUID = -1L;
    private int result;//结果
    private String time;//时间
    @Override
    public byte[] getDataBytes() {
        return new byte[0];
    }

    @Override
    public Object getDataPacket(byte[] datas) {
        return null;
    }
}
