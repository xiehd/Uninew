package com.uninew.settings.interfaces;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;

/**
 * Created by Administrator on 2017/9/7.
 */

public interface IWifiModel {

    /**
     * 打开WIFI
     */
    void OpenWifi();

    /**
     * 关闭WIFI
     */
    void CloseWifi();

    /**
     * 连接WIFI
     */
    boolean ConnectWifi(String SSID, String Password,
                        int Type);
    /**
     * 得到当前连接信息
     */
    WifiInfo getmWifiInfo();

    /**
     * 判断是否有配置好的连接
     */
    WifiConfiguration IsExsits(String SSID);
    /**
     * 连接配置好的WiFi
     */
    boolean connectConfiguration(WifiConfiguration wcg);
}
