package com.uninew.mms.ChuanJiLed.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/27.
 */

public abstract class BaseCJLed implements Serializable {
    private static final long serialVersionUID = -3345675645334L;

    /**
     * @return 消息体
     */
    public abstract byte[] getDataBytes();

    /**
     * 获取对象包
     *
     * @param datas 消息内容
     * @return
     */
    public abstract Object getDataPacket(byte[] datas);

}
