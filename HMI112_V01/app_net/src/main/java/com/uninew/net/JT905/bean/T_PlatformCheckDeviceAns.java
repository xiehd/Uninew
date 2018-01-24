package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 设备巡检应答
 * Created by Administrator on 2017/8/20 0020.
 */

public class T_PlatformCheckDeviceAns extends BaseBean{

    private String deviceSerialId;//
    private String hardwareVersion;//硬件版本，最多2个数字
    private String softwareVersion;//软件版本，最多4个数字，主版本+次版本
    private int deviceState;//设备状态，同位置信息状态
    private int alarmFlag;//报警标志，同位置信息标志
    private int signInCacheNumber;//签到缓存条数
    private int signOutCacheNumber;//签退缓存条数
    private int operationCacheNumber;//营运记录缓存条数
    private int cardDealCacheNumber;//一卡通交易缓存条数

    public T_PlatformCheckDeviceAns() {
    }
    public T_PlatformCheckDeviceAns(String deviceSerialId, String hardwareVersion,
                                    String softwareVersion, int deviceState, int alarmFlag,
                                    int signInCacheNumber, int signOutCacheNumber, int operationCacheNumber,
                                    int cardDealCacheNumber) {
        this.deviceSerialId = deviceSerialId;
        this.hardwareVersion = hardwareVersion;
        this.softwareVersion = softwareVersion;
        this.deviceState = deviceState;
        this.alarmFlag = alarmFlag;
        this.signInCacheNumber = signInCacheNumber;
        this.signOutCacheNumber = signOutCacheNumber;
        this.operationCacheNumber = operationCacheNumber;
        this.cardDealCacheNumber = cardDealCacheNumber;
    }

    @Override
    public int getTcpId() {
        return this.tcpId;
    }

    @Override
    public void setTcpId(int tcpId) {
        this.tcpId=tcpId;
    }

    @Override
    public int getTransportId() {
        return this.transportId;
    }

    @Override
    public void setTransportId(int transportId) {
        this.transportId=transportId;
    }

    @Override
    public String getSmsPhoneNumber() {
        return this.smsPhonenumber;
    }

    @Override
    public void setSmsPhoneNumber(String smsPhonenumber) {
        this.smsPhonenumber=smsPhonenumber;
    }

    @Override
    public int getSerialNumber() {
        return this.serialNumber;
    }

    @Override
    public void setSerialNumber(int serialNumber) {
        this.serialNumber=serialNumber;
    }

    @Override
    public int getMsgId() {
        return BaseMsgID.PLATFORM_CHECK_DEVICE_ANS;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.write(ProtocolTool.str2Bcd(deviceSerialId,5));
            out.write(ProtocolTool.str2Bcd(hardwareVersion,1));
            out.write(ProtocolTool.str2Bcd(softwareVersion,2));
            out.writeInt(deviceState);
            out.writeInt(alarmFlag);
            out.writeByte(signInCacheNumber);
            out.writeByte(signOutCacheNumber);
            out.writeByte(operationCacheNumber);
            out.writeByte(cardDealCacheNumber);
            out.flush();
            datas = stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return datas;
    }

    @Override
    public Object getDataPacket(byte[] datas) {
        return null;
    }
}
