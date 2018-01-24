package com.uninew.net.Taximeter.bean;

import java.io.Serializable;

/**
 * 计价器消息基类
 * Created by Administrator on 2017/10/14.
 */

public abstract class BaseMcuBean implements Serializable {
    private static final long serialVersionUID = -8342314580618120065L;
    public BaseMcuBean() {
        super();
    }

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
