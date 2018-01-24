package com.uninew.maintanence.presenter;

import android.content.Context;

import com.uninew.maintanence.interfaces.INetInfoModel;
import com.uninew.maintanence.interfaces.INetInfoPresenter;
import com.uninew.maintanence.interfaces.INetInfoView;
import com.uninew.maintanence.model.NetModel;

/**
 * Created by Administrator on 2017/9/25.
 */

public class NetPresent implements INetInfoPresenter {
    private INetInfoView mINetInfoView;
    private INetInfoModel mINetInfoModel;

    public NetPresent(Context context, INetInfoView mINetInfoView) {
        this.mINetInfoView = mINetInfoView;
        mINetInfoModel = new NetModel(context,this);
    }

    @Override
    public void startPhoneListener() {
        mINetInfoModel.startPhoneListener();

    }

    @Override
    public void stopPhoneListener() {
        mINetInfoModel.stopPhoneListener();
    }

    @Override
    public void ShowSIMState(String state) {
        mINetInfoView.ShowSIMState(state);
    }

    @Override
    public void ShowFacilitator(String msg) {
        mINetInfoView.ShowFacilitator(msg);
    }

    @Override
    public void ShowSignal(int signal) {
        mINetInfoView.ShowSignal(signal);
    }

    @Override
    public void ShowRoam(String state) {
        mINetInfoView.ShowRoam(state);
    }

    @Override
    public void ShowNetType(String type) {
        mINetInfoView.ShowNetType(type);
    }

    @Override
    public void ShowNetState(String state) {
        mINetInfoView.ShowNetState(state);
    }

    @Override
    public void ShowIPAddress(String ip) {
        mINetInfoView.ShowIPAddress(ip);
    }
}
