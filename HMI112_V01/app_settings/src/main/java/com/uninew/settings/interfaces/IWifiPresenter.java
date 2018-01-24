package com.uninew.settings.interfaces;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/9/7.
 */

public interface IWifiPresenter {

    /**
     * 打开WIFI
     */
    void OpenWifi();

    /**
     * 关闭WIFI
     */
    void CloseWifi();

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

    /*******************************下传*****************************************/
    /**
     * 显示WiFi列表
     */
    void ShowWifiList(List<ScanResult> mWifiList);


}
