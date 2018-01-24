package com.uninew.settings.interfaces;

import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Created by Administrator on 2017/9/7.
 */

public interface IWifiView {

    /**
     * 显示WiFi列表
     */
    void ShowWifiList(List<ScanResult> mWifiList);
}
