package com.uninew.maintanence.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.uninew.export.Utils;
import com.uninew.main.SettingsApplication;
import com.uninew.maintanence.interfaces.IDeviceInspectPresenter;
import com.uninew.maintanence.interfaces.IDeviceInsperctModel;
import com.uninew.maintanence.presenter.DevicePresenter;
import com.uninew.maintanence.view.IDeviceStateListener;
import com.uninew.net.JT905.comm.client.ClientSendManage;
import com.uninew.net.JT905.common.DeviceState;
import com.uninew.net.JT905.common.TimeTool;

/**
 * Created by Administrator on 2017/10/31.
 */

public class DeviceModel implements IDeviceInsperctModel,IDeviceStateListener {

    private DevicePresenter mDevicePresenter;
    private DeviceState mDeviceState;
    private SettingsApplication  application;
    private ClientSendManage mClientSendManage;
    public DeviceModel(IDeviceInspectPresenter p, Context con) {
        mDevicePresenter = (DevicePresenter) p;
        application = (SettingsApplication) Utils.getApp();
        application.setDeviceListener(this);
        mClientSendManage = new ClientSendManage(con);
        if (mClientSendManage != null)
            mClientSendManage.quryTaxiState(TimeTool.timestampTormat2(TimeTool.getCurrentTimestamp()));

//        Intent intent = new Intent("Action_Device_StateDate");
//        this.sendBroadcast(intent);

        init();
    }
    private void init(){
        if (mDeviceState != null){
            mDevicePresenter.ShowDvrState(mDeviceState.getDvr_state());
            mDevicePresenter.ShowCarScreenState(mDeviceState.getCarscreen_state());
            mDevicePresenter.ShowGPSInfo(mDeviceState.getGps_state());
            mDevicePresenter.ShowNetModel(mDeviceState.getNet_state());
            mDevicePresenter.ShowPowerInfo(mDeviceState.getPower_state());
            mDevicePresenter.ShowRentServerState(mDeviceState.getRentserver_state());
            mDevicePresenter.ShowVideoServerState(mDeviceState.getVedio_state());
            mDevicePresenter.ShowTaximeterState(mDeviceState.getTaximeter_state());
        }
    }

    @Override
    public void setDeviceState(DeviceState mDeviceState) {
        this.mDeviceState = mDeviceState;
        init();
    }
}
