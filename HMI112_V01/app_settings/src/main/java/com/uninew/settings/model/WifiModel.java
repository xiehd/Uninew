package com.uninew.settings.model;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.uninew.settings.interfaces.IWifiModel;
import com.uninew.settings.interfaces.IWifiPresenter;

/**
 * Created by Administrator on 2017/9/2.
 */

public class WifiModel implements IWifiModel {

    private Context mContext;
    private WifiHelper mWifiHelper;
    private IWifiPresenter mIWifiPresenter;

    public WifiModel(Context mContext,IWifiPresenter presenter) {
        this.mContext = mContext;
        mIWifiPresenter = presenter;
        mWifiHelper = new WifiHelper(mContext);
    }

    @Override
    public void OpenWifi() {
        mWifiHelper.openWifi();
        // 开启wifi功能需要一段时间(我在手机上测试一般需要1-3秒左右)，所以要等到wifi
        // 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
        while (mWifiHelper.checkState() == WifiManager.WIFI_STATE_ENABLING) {
            try {
                // 为了避免程序一直while循环，让它睡个100毫秒检测……
                Thread.sleep(100);
            } catch (InterruptedException ie) {
            }
        }
        mWifiHelper.startScan();
        if (mWifiHelper.getWifiList()!=null){
            mIWifiPresenter.ShowWifiList(mWifiHelper.getWifiList());
        }else{

        }
    }

    @Override
    public void CloseWifi() {
        mWifiHelper.closeWifi();
    }

    @Override
    public boolean ConnectWifi(String SSID, String Password,
                            int Type) {
        WifiConfiguration tempConfig = mWifiHelper.CreateWifiInfo(SSID,Password,Type);
        return mWifiHelper.addNetwork(tempConfig);
    }


    @Override
    public WifiInfo getmWifiInfo() {
        return mWifiHelper.getmWifiInfo();
    }

    @Override
    public WifiConfiguration IsExsits(String SSID) {
        return mWifiHelper.IsExsits(SSID);
    }

    @Override
    public boolean connectConfiguration(WifiConfiguration wcg) {
        return mWifiHelper.connectConfiguration(wcg);
    }
}
