package com.uninew.car.db.settings;

/**
 * Created by Administrator on 2017/9/5 0005.
 */

public class PlatformSettings {

    private int tvpId;
    private String mainIp;
    private int mainPort;
    private String spareIp;
    private int sparePort;

    public PlatformSettings() {
    }

    public PlatformSettings(int tvpId, String mainIp, int mainPort, String spareIp, int sparePort) {
        this.tvpId = tvpId;
        this.mainIp = mainIp;
        this.mainPort = mainPort;
        this.spareIp = spareIp;
        this.sparePort = sparePort;
    }

    public int getTvpId() {
        return tvpId;
    }

    public void setTvpId(int tvpId) {
        this.tvpId = tvpId;
    }

    public String getMainIp() {
        return mainIp;
    }

    public void setMainIp(String mainIp) {
        this.mainIp = mainIp;
    }

    public int getMainPort() {
        return mainPort;
    }

    public void setMainPort(int mainPort) {
        this.mainPort = mainPort;
    }

    public String getSpareIp() {
        return spareIp;
    }

    public void setSpareIp(String spareIp) {
        this.spareIp = spareIp;
    }

    public int getSparePort() {
        return sparePort;
    }

    public void setSparePort(int sparePort) {
        this.sparePort = sparePort;
    }

    @Override
    public String toString() {
        return "PlatformSettings{" +
                "tvpId=" + tvpId +
                ", mainIp='" + mainIp + '\'' +
                ", mainPort=" + mainPort +
                ", spareIp='" + spareIp + '\'' +
                ", sparePort=" + sparePort +
                '}';
    }
}
