package com.uninew.car.db.audio;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/11/2 0002.
 */

public class AudioData {
    private int id;
    private String isuId;
    private int codingType;
    private String carName;
    private int revenueId;
    private int reason;
    private long startTime;
    private double startLatitude;
    private double startLongitude;
    private long endTime;
    private double endLatitude;
    private double endLongitude;
    private int fileId;
    private int audioLength;
    private byte[] audioBuffers;


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

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
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

    @Override
    public String toString() {
        return "AudioData{" +
                "id=" + id +
                ", isuId='" + isuId + '\'' +
                ", codingType=" + codingType +
                ", carName='" + carName + '\'' +
                ", revenueId=" + revenueId +
                ", reason=" + reason +
                ", startTime=" + startTime +
                ", startLatitude=" + startLatitude +
                ", startLongitude=" + startLongitude +
                ", endTime=" + endTime +
                ", endLatitude=" + endLatitude +
                ", endLongitude=" + endLongitude +
                ", fileId=" + fileId +
                ", audioLength=" + audioLength +
                ", audioBuffers=" + Arrays.toString(audioBuffers) +
                '}';
    }
}
