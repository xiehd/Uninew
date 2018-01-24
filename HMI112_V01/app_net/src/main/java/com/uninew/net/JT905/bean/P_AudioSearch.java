package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 音频检索
 * Created by Administrator on 2017/8/20 0020.
 */

public class P_AudioSearch extends  BaseBean{

    private int recordingReason;//录音原因，0：正常录音，1：乘客投诉，2：报警录音
    private String startTime;//开始时间：YYMMDDhhmmss
    private String endTime;//结束时间：YYMMDDhhmmss


    public P_AudioSearch() {
    }

    public P_AudioSearch(byte[] body) {
        getDataPacket(body);
    }

    public P_AudioSearch(int recordingReason, String startTime, String endTime) {
        this.recordingReason = recordingReason;
        this.startTime = startTime;
        this.endTime = endTime;
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
        return BaseMsgID.STORE_AUDIO_SEARCH;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeByte(recordingReason);
            out.write(ProtocolTool.str2Bcd(startTime,6));
            out.write(ProtocolTool.str2Bcd(endTime,6));
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
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            recordingReason=in.readByte();
            byte[] a=new byte[6];
            in.read(a);
            startTime=ProtocolTool.bcd2Str(a);
            byte[] b=new byte[6];
            in.read(b);
            endTime=ProtocolTool.bcd2Str(b);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }
}
