package com.uninew.net.camera;

import android.content.Context;

/**
 * Created by Administrator on 2017/11/3 0003.
 */

public class RequestCamera {
    /*色调*/
    private float chroma = 0;
    /*饱和度*/
    private float saturation = 0;
    /*亮度*/
    private float brightness = 0;
    /* 对比度*/
    private float contrast = 0;
    /*照片高*/
    private int mHeight = 0;
    /*照片宽*/
    private int mWidth = 0;
    /* 拍照次数*/
    private int shootTimes = 0;
    /* 拍照时间间隔（或者录像时长） 单位秒*/
    private int shootInterval = 0;
    /*是否录像*/
    private boolean isViedo = false;
    /*是否保存成文件*/
    private boolean isSaveFile = false;
    /* 图片/视频质量*/
    private int imageVideoQuality = 0;
    /* 流水号 */
    private int serialNumber;


    /////////////上报需要的参数////////////////////////////
    private int cameraId;
    private String isuId;
    private int codingType;
    private String carName;
    private int revenueId;
    private int reason;
    private int duration;
    private int fileId;
    private long time;
    private double latitude;
    private double longitude;

    public RequestCamera() {
    }

    public float getChroma() {
        return chroma;
    }

    public void setChroma(float chroma) {
        this.chroma = chroma;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public float getContrast() {
        return contrast;
    }

    public void setContrast(float contrast) {
        this.contrast = contrast;
    }

    public int getmHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public int getmWidth() {
        return mWidth;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getShootTimes() {
        return shootTimes;
    }

    public void setShootTimes(int shootTimes) {
        this.shootTimes = shootTimes;
    }

    public int getShootInterval() {
        return shootInterval;
    }

    public void setShootInterval(int shootInterval) {
        this.shootInterval = shootInterval;
    }

    public boolean isViedo() {
        return isViedo;
    }

    public void setViedo(boolean viedo) {
        isViedo = viedo;
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

    public boolean isSaveFile() {
        return isSaveFile;
    }

    public void setSaveFile(boolean saveFile) {
        isSaveFile = saveFile;
    }

    public void setImageVideoQuality(int imageVideoQuality) {
        this.imageVideoQuality = imageVideoQuality;
    }

    public int getImageVideoQuality() {
        return imageVideoQuality;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return "RequestCamera{" +
                "chroma=" + chroma +
                ", saturation=" + saturation +
                ", brightness=" + brightness +
                ", contrast=" + contrast +
                ", mHeight=" + mHeight +
                ", mWidth=" + mWidth +
                ", shootTimes=" + shootTimes +
                ", shootInterval=" + shootInterval +
                ", isViedo=" + isViedo +
                ", isSaveFile=" + isSaveFile +
                ", imageVideoQuality=" + imageVideoQuality +
                ", serialNumber=" + serialNumber +
                ", cameraId=" + cameraId +
                ", isuId='" + isuId + '\'' +
                ", codingType=" + codingType +
                ", carName='" + carName + '\'' +
                ", revenueId=" + revenueId +
                ", reason=" + reason +
                ", duration=" + duration +
                ", fileId=" + fileId +
                ", time=" + time +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
