package com.uninew.car.db.settings;

/**
 * Created by Administrator on 2017/8/31 0031.
 */

public class BaseSettings {
    private String plateNumber;
    private String terminalNumber;
    private String companyNumber;
    private String dvrSerialNumber;
    private int outTimeExite;
    private int printSensitivity;
    private int prerecordTime;
    private int delayTime;


    public BaseSettings() {
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getTerminalNumber() {
        return terminalNumber;
    }

    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getDvrSerialNumber() {
        return dvrSerialNumber;
    }

    public void setDvrSerialNumber(String dvrSerialNumber) {
        this.dvrSerialNumber = dvrSerialNumber;
    }

    public int getOutTimeExite() {
        return outTimeExite;
    }

    public void setOutTimeExite(int outTimeExite) {
        this.outTimeExite = outTimeExite;
    }

    public int getPrintSensitivity() {
        return printSensitivity;
    }

    public void setPrintSensitivity(int printSensitivity) {
        this.printSensitivity = printSensitivity;
    }

    public int getPrerecordTime() {
        return prerecordTime;
    }

    public void setPrerecordTime(int prerecordTime) {
        this.prerecordTime = prerecordTime;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    @Override
    public String toString() {
        return "BaseSettings{" +
                "plateNumber='" + plateNumber + '\'' +
                ", terminalNumber='" + terminalNumber + '\'' +
                ", companyNumber='" + companyNumber + '\'' +
                ", dvrSerialNumber='" + dvrSerialNumber + '\'' +
                ", outTimeExite=" + outTimeExite +
                ", printSensitivity=" + printSensitivity +
                ", prerecordTime=" + prerecordTime +
                ", delayTime=" + delayTime +
                '}';
    }
}
