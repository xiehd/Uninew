package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.Define;

import java.io.Serializable;

/**
 * 消息基类
 */
public abstract class BaseBean implements Serializable {

    private static final long serialVersionUID = -1254314580618120065L;

    protected int serialNumber;
    protected int tcpId=Define.TCP_ONE;
    /**通信方式*/
    protected int transportId= Define.Transport_TCP;
    /**短信通信对方号码*/
    protected String smsPhonenumber;

    /**
     * @return tcpId
     */
    public abstract int getTcpId();

    /**
     * @param tcpId 平台Id
     */
    public abstract void setTcpId(int tcpId);

    /**
     * @return transportId
     */
    public abstract int getTransportId();

    /**
     * @param transportId
     */
    public abstract void setTransportId(int transportId);

    /**
     * @return 电话号码
     */
    public abstract String getSmsPhoneNumber();

    /**
     * @param smsPhonenumber 短信电话号码
     */
    public abstract void setSmsPhoneNumber(String smsPhonenumber);
    /**
     * @return 流水号
     */
    public abstract int getSerialNumber();

    /**
     * @param serialNumber 流水号
     */
    public abstract void setSerialNumber(int serialNumber);

    /**
     * @return 消息ID
     */
    public abstract int getMsgId();

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
