package com.uninew.maintanence.presenter;

import android.content.Context;

import com.uninew.maintanence.interfaces.IDeviceInspectPresenter;
import com.uninew.maintanence.interfaces.IDeviceInsperctModel;
import com.uninew.maintanence.interfaces.IDeviceInsperctView;
import com.uninew.maintanence.model.DeviceModel;

/**
 * Created by Administrator on 2017/10/31.
 */

public class DevicePresenter implements IDeviceInspectPresenter {
    private IDeviceInsperctView mIDeviceInsperctView;
    private IDeviceInsperctModel mIDeviceInsperctModel;
    private Context mContext;
    public DevicePresenter(IDeviceInsperctView Iview, Context con) {
        mIDeviceInsperctView = Iview;
        mContext = con;
        mIDeviceInsperctModel = new DeviceModel(this,con);
    }

    @Override
    public void ShowDvrState(int state) {
        mIDeviceInsperctView.ShowDvrState(state);
    }

    @Override
    public void ShowTaximeterState(int state) {
        mIDeviceInsperctView.ShowTaximeterState(state);
    }

    @Override
    public void ShowCarScreenState(int state) {
        mIDeviceInsperctView.ShowCarScreenState(state);
    }

    @Override
    public void ShowGPSInfo(int result) {
        mIDeviceInsperctView.ShowGPSInfo(result);
    }

    @Override
    public void ShowNetModel(int result) {
        mIDeviceInsperctView.ShowNetModel(result);
    }

    @Override
    public void ShowPowerInfo(int result) {
        mIDeviceInsperctView.ShowPowerInfo(result);
    }

    @Override
    public void ShowRentServerState(int state) {
        mIDeviceInsperctView.ShowRentServerState(state);
    }

    @Override
    public void ShowVideoServerState(int state) {
        mIDeviceInsperctView.ShowVideoServerState(state);
    }
}
