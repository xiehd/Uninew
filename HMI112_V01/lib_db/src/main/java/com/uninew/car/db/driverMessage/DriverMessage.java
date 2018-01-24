package com.uninew.car.db.driverMessage;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class DriverMessage {
    private int id;
    private String driverCertificate;
    private String driverName;
    private String driverPicture;
    private int driverStar;

    public DriverMessage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDriverCertificate() {
        return driverCertificate;
    }

    public void setDriverCertificate(String driverCertificate) {
        this.driverCertificate = driverCertificate;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPicture() {
        return driverPicture;
    }

    public void setDriverPicture(String driverPicture) {
        this.driverPicture = driverPicture;
    }

    public int getDriverStar() {
        return driverStar;
    }

    public void setDriverStar(int driverStar) {
        this.driverStar = driverStar;
    }

    @Override
    public String toString() {
        return "DriverMessage{" +
                "id=" + id +
                ", driverCertificate='" + driverCertificate + '\'' +
                ", driverName='" + driverName + '\'' +
                ", driverPicture='" + driverPicture + '\'' +
                ", driverStar=" + driverStar +
                '}';
    }
}
