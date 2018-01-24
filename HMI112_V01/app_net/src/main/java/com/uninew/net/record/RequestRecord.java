package com.uninew.net.record;

import com.uninew.net.JT905.common.Define;
import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/11/2 0002.
 */

public class RequestRecord {
    private String isuId = Define.Default_DeviceId;
    private int codingType;
    private String carName = "粤B50000";
    private int revenueId;
    private int reason;
    private int duration;
    private int fileId;
    private long startTime;
    private double startLatitude;
    private double startLongitude;
    private long endTime;
    private double endLatitude;
    private double endLongitude;
    private int audioLength;
    private byte[] audioBuffers;
    private int serialNumber;

    public RequestRecord() {
    }

    public RequestRecord(String isuId, int codingType, String carName, int revenueId, int reason, int duration, int fileId) {
        this.isuId = isuId;
        this.codingType = codingType;
        this.carName = carName;
        this.revenueId = revenueId;
        this.reason = reason;
        this.duration = duration;
        this.fileId = fileId;
    }

    public String getIsuId() {
        return isuId;
    }

    public void setIsuId(String isuId) {
        this.isuId = isuId;
    }

    public int getCodingType() {
        return codingType;
    }

    public void setCodingType(int codingType) {
        this.codingType = codingType;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public int getRevenueId() {
        return revenueId;
    }

    public void setRevenueId(int revenueId) {
        this.revenueId = revenueId;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public int getAudioLength() {
        return audioLength;
    }

    public void setAudioLength(int audioLength) {
        this.audioLength = audioLength;
    }

    public byte[] getAudioBuffers() {
        return audioBuffers;
    }

    public void setAudioBuffers(byte[] audioBuffers) {
        this.audioBuffers = audioBuffers;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return "RequestRecord{" +
                "isuId='" + isuId + '\'' +
                ", codingType=" + codingType +
                ", carName='" + carName + '\'' +
                ", revenueId=" + revenueId +
                ", reason=" + reason +
                ", duration=" + duration +
                ", fileId=" + fileId +
                ", startTime=" + startTime +
                ", startLatitude=" + startLatitude +
                ", startLongitude=" + startLongitude +
                ", endTime=" + endTime +
                ", endLatitude=" + endLatitude +
                ", endLongitude=" + endLongitude +
                ", audioLength=" + audioLength +
                '}';
    }


    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.write(ProtocolTool.str2Bcd(isuId, 5));
            out.write(ProtocolTool.intToBcd(codingType, 1));
            out.write(ProtocolTool.stringToByte(carName, ProtocolTool.CHARSET_905, 10));
            out.writeInt(revenueId);
            out.writeByte(reason);
            out.write(ProtocolTool.getBCD12TimeBytes(startTime));
            out.writeInt((int) (startLatitude * 1000000));
            out.writeInt((int) (startLongitude * 1000000));
            out.write(ProtocolTool.getBCD12TimeBytes(endTime));
            out.writeInt((int) (endLatitude * 1000000));
            out.writeInt((int) (endLongitude * 1000000));
            out.writeInt(fileId);
            out.writeInt(audioLength);
            out.write(new byte[69]);
            out.write(audioBuffers);
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

    public Object getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            byte[] a = new byte[5];
            in.read(a);
            isuId = ProtocolTool.bcd2Str(a);
            codingType = ProtocolTool.BCDToInt(in.readByte());
            byte[] s = new byte[10];
            in.read(s);
            carName = new String(s, ProtocolTool.CHARSET_905).trim();
            revenueId = in.readInt();
            reason = in.readUnsignedByte();
            byte[] b = new byte[6];
            in.read(b);
            startTime = ProtocolTool.bcdToTimestamp(b).getTime();
            startLatitude = in.readInt() / 1000000;
            startLongitude = in.readInt() / 1000000;
            endTime = ProtocolTool.bcdToTimestamp(b).getTime();
            endLatitude = in.readInt() / 1000000;
            endLongitude = in.readInt() / 1000000;
            fileId = in.readInt();
            audioLength = in.readInt();
            byte[] d = new byte[84];
            in.read(d);
            audioBuffers = new byte[audioLength];
            in.read(audioBuffers);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
