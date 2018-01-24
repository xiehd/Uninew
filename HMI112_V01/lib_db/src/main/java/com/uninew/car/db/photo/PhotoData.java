package com.uninew.car.db.photo;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/11/1 0001.
 */

public class PhotoData {
    private int id;
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

    public PhotoData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    @Override
    public String toString() {
        return "PhotoData{" +
                "id=" + id +
                ", cameraId=" + cameraId +
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
                '}';
    }
}
