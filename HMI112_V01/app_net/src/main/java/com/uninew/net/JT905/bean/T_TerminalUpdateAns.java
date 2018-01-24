package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 终端升级结果报告消息
 * Created by Administrator on 2017/8/19 0019.
 */

public class T_TerminalUpdateAns extends BaseBean {

    private static final boolean D = true;
    private static final String TAG = "T_TerminalUpdateAns";

    private int deviceType;//设备类型
    private int companyNumber;//厂商编号
    private String hardwareVersion;//硬件版本
    private String softwareVersion;//软件版本
    private int updateResult;//升级结果

    public T_TerminalUpdateAns() {

    }

    public T_TerminalUpdateAns(int deviceType, int companyNumber, String hardwareVersion, String softwareVersion, int updateResult) {
        this.deviceType = deviceType;
        this.companyNumber = companyNumber;
        this.hardwareVersion = hardwareVersion;
        this.softwareVersion = softwareVersion;
        this.updateResult = updateResult;
    }

    @Override
    public int getTcpId() {
        return this.tcpId;
    }

    @Override
    public void setTcpId(int tcpId) {
        this.tcpId = tcpId;
    }

    @Override
    public int getTransportId() {
        return this.transportId;
    }

    @Override
    public void setTransportId(int transportId) {
        this.transportId = transportId;
    }

    @Override
    public String getSmsPhoneNumber() {
        return this.smsPhonenumber;
    }

    @Override
    public void setSmsPhoneNumber(String smsPhonenumber) {
        this.smsPhonenumber = smsPhonenumber;
    }

    @Override
    public int getSerialNumber() {
        return this.serialNumber;
    }

    @Override
    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public int getMsgId() {
        return BaseMsgID.TERMINAL_UPDATE_ANS;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(int companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public void setHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public int getUpdateResult() {
        return updateResult;
    }

    public void setUpdateResult(int updateResult) {
        this.updateResult = updateResult;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeByte(deviceType);
            out.writeByte(companyNumber);
            if (hardwareVersion.length()>2){
                hardwareVersion=hardwareVersion.substring(0,1);
            }
            byte[] hard=ProtocolTool.str2Bcd(hardwareVersion);
            out.write(hard);
            if (softwareVersion.length()>3){
                softwareVersion=softwareVersion.substring(0,2);
            }
            byte[] soft=ProtocolTool.str2Bcd(softwareVersion);
            out.write(soft);
            out.writeByte(updateResult);
            out.flush();
            datas = stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
        return this;
    }
}
