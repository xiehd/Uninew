package com.uninew.net.JT905.common;

/**
 * Created by Administrator on 2017/8/17 0017.
 */

public interface Define {

    /**平台个数*/
    int TCP_NUMBER=0x02;
    /**平台编号0x00*/
    int TCP_ONE=0x00;
    /**平台编号0x01*/
    int TCP_TWO=0x01;

    /**通讯方式：TCP 0x00*/
    int Transport_TCP=0x00;
    /**通讯方式：SMS 0x01*/
    int Transport_SMS=0x01;

     /**默认主服务器ip*/
     String Default_MainIp = "192.168.1.119";
    /**默认主服务器端口*/
     int Default_MainPort = 8888;
    /**默认备用服务器ip*/
     String Default_SpareIp = "";
    /**默认备用服务器端口*/
     int Default_SparePort = 0;
    /**默认TCP最大重连次数*/
     int Default_MaxLinkTimes = 3;
    /**默认TCP连接超时时间*/
    long Default_LinkTimeout = 1000 * 10;

    /**设备编号*/
     String Default_DeviceId="13200000021";
    /**默认TCP应答超时时间*/
     int Default_TcpResponseTimeOut=1;
    /**默认TCP应答超时次数*/
     int Default_TcpResendTime=3;
    /**默认SMS应答超时时间*/
     int Default_SmsResponseTimeOut=3;
    /**默认SMS应答超时次数*/
     int Default_SmsResendTime=2;

    /**默认心跳时间间隔*/
    int Default_HeartBeatTime = 20 * 1000;

}
