package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 图片、音视频上传命令
 * Created by Administrator on 2017/8/20 0020.
 */

public class T_AudioVideoUpload extends BaseBean {

    private int responseSerialNumber;//存储图像音视频上传命令流水号
    private int fileId;//文件id
    private int packetSize;//数据大小
    private int startOffset;//起始位置
    private byte[] audioVideoDatas;//音视频数据包

    public T_AudioVideoUpload() {
    }

    public T_AudioVideoUpload(int responseSerialNumber, int fileId, int packetSize, int startOffset, byte[] audioVideoDatas) {
        this.responseSerialNumber = responseSerialNumber;
        this.fileId = fileId;
        this.packetSize = packetSize;
        this.startOffset = startOffset;
        this.audioVideoDatas = audioVideoDatas;
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
        return BaseMsgID.STORE_AUDIOVIDEO_UPLOAD;
    }

    public int getResponseSerialNumber() {
        return responseSerialNumber;
    }

    public void setResponseSerialNumber(int responseSerialNumber) {
        this.responseSerialNumber = responseSerialNumber;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public byte[] getAudioVideoDatas() {
        return audioVideoDatas;
    }

    public void setAudioVideoDatas(byte[] audioVideoDatas) {
        this.audioVideoDatas = audioVideoDatas;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeShort(responseSerialNumber);
            out.writeInt(fileId);
            out.writeInt(packetSize);
            out.writeInt(startOffset);
            out.write(audioVideoDatas);
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
