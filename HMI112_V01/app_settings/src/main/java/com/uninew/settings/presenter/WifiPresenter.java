package com.uninew.settings.presenter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;

import com.uninew.settings.interfaces.IWifiModel;
import com.uninew.settings.interfaces.IWifiPresenter;
import com.uninew.settings.interfaces.IWifiView;
import com.uninew.settings.model.WifiModel;

import java.util.List;

/**
 * Created by Administrator on 2017/9/7.
 */

public class WifiPresenter implements IWifiPresenter {
    private IWifiView mIWifiView;
    private IWifiModel mIWifiModel;

    public WifiPresenter(IWifiView mIWifiView, Context context){
        this.mIWifiView = mIWifiView;
        mIWifiModel = new WifiModel(context,this);
    }

    @Override
    public void OpenWifi() {
        mIWifiModel.OpenWifi();
    }

    @Override
    public void CloseWifi() {
        mIWifiModel.CloseWifi();
    }

    @Override
    public WifiInfo getmWifiInfo() {
        return mIWifiModel.getmWifiInfo();
    }

    @Override
    public WifiConfiguration IsExsits(String SSID) {
        return mIWifiModel.IsExsits(SSID);
    }

    @Override
    public boolean connectConfiguration(WifiConfiguration wcg) {
        return mIWifiModel.connectConfiguration(wcg);
    }

/****************************************下传*******************************************************************************/
    @Override
    public void ShowWifiList(List<ScanResult> mWifiList) {
        mIWifiView.ShowWifiList(mWifiList);
    }
}
