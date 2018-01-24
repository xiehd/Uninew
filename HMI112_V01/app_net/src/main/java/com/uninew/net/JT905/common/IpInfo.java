package com.uninew.net.JT905.common;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/29 0029.
 */

public class IpInfo implements Serializable{

    /**平台编号*/
    public int tcpId;
    /**主服务器ip*/
    public String mainIp;
    /**主服务器端口*/
    public int mainPort;
    /**备用服务器ip*/
    public String spareIp;
    /**备用服务器端口*/
    public int sparePort;

    /**是否连接主服务器*/
    public boolean isLinkMain;
    /**连接状态:0-未连接，1-已连接*/
    public int linkState;


    public IpInfo() {
    }

    public IpInfo(int tcpId, String mainIp, int mainPort) {
        this.tcpId = tcpId;
        this.mainIp = mainIp;
        this.mainPort = mainPort;
    }

    public IpInfo(int tcpId, String mainIp, int mainPort, String spareIp, int sparePort) {
        this.tcpId=tcpId;
        this.mainIp = mainIp;
        this.mainPort = mainPort;
        this.spareIp = spareIp;
        this.sparePort = sparePort;
    }

    @Override
    public String toString() {
        return "IpInfo{" +
                "tcpId=" + tcpId +
                ", mainIp='" + mainIp + '\'' +
                ", mainPort=" + mainPort +
                ", spareIp='" + spareIp + '\'' +
                ", sparePort=" + sparePort +
                ", isLinkMain=" + isLinkMain +
                ", linkState=" + linkState +
                '}';
    }
}
