package com.uninew.net.JT905.bean;

import android.graphics.Bitmap;

import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/11/1 0001.
 */

public class CameraPhoto extends BaseBean{

    private int cameraId;
    private String isuId;
    private int codingType;
    private String carName;
    private int revenueId;
    private int reason;
    private long time;
    private double latitude;
    private double longitude;
    private int fileId;
    private int photoLength;
    private byte[] photoBuffers;
    /* 是否是立即拍摄 */
    private boolean isPromptlyCamera;

    public CameraPhoto() {
    }

    @Override
    public int getTcpId() {
        return 0;
    }

    @Override
    public void setTcpId(int tcpId) {

    }

    @Override
    public int getTransportId() {
        return 0;
    }

    @Override
    public void setTransportId(int transportId) {

    }

    @Override
    public String getSmsPhoneNumber() {
        return null;
    }

    @Override
    public void setSmsPhoneNumber(String smsPhonenumber) {

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
        return 0;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.write(ProtocolTool.str2Bcd(isuId,5));
            out.write(ProtocolTool.intToBcd(codingType,1));
            out.write(ProtocolTool.stringToByte(carName,ProtocolTool.CHARSET_905,10));
            out.writeInt(revenueId);
            out.writeByte(reason);
            out.write(ProtocolTool.getBCD12TimeBytes(time));
            out.writeInt((int)(latitude*1000000));
            out.writeInt((int)(longitude*1000000));
            out.writeInt(fileId);
            out.writeInt(photoLength);
            out.write(new byte[84]);
            out.write(photoBuffers);
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
            byte[] a = new byte[5];
            in.read(a);
            isuId = ProtocolTool.bcd2Str(a);
            codingType = ProtocolTool.BCDToInt(in.readByte());
            byte[] s = new byte[10];
            in.read(s);
            carName = new String(s,ProtocolTool.CHARSET_905).trim();
            revenueId = in.readInt();
            reason = in.readUnsignedByte();
            byte[] b = new byte[6];
            in.read(b);
            time = ProtocolTool.bcdToTimestamp(b).getTime();
            latitude = in.readInt()/1000000;
            longitude = in.readInt()/1000000;
            fileId = in.readInt();
            photoLength = in.readInt();
            byte[] d = new byte[84];
            in.read(d);
//            int size = in.available();
            photoBuffers = new byte[photoLength];
            in.read(photoBuffers);
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

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getPhotoLength() {
        return photoLength;
    }

    public void setPhotoLength(int photoLength) {
        this.photoLength = photoLength;
    }

    public byte[] getPhotoBuffers() {
        return photoBuffers;
    }

    public void setPhotoBuffers(byte[] photoBuffers) {
        this.photoBuffers = photoBuffers;
    }

    public boolean isPromptlyCamera() {
        return isPromptlyCamera;
    }

    public void setPromptlyCamera(boolean promptlyCamera) {
        isPromptlyCamera = promptlyCamera;
    }

    @Override
    public String toString() {
        return "CameraPhoto{" +
                "cameraId=" + cameraId +
                ", isuId='" + isuId + '\'' +
                ", codingType=" + codingType +
                ", carName='" + carName + '\'' +
                ", revenueId=" + revenueId +
                ", reason=" + reason +
                ", time=" + time +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", fileId=" + fileId +
                ", photoLength=" + photoLength +
                ", photoBuffers=" + Arrays.toString(photoBuffers) +
                ", isPromptlyCamera=" + isPromptlyCamera +
                '}';
    }
}


