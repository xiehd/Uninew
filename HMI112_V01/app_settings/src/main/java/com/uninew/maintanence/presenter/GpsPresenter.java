package com.uninew.maintanence.presenter;

import android.content.Context;

import com.uninew.maintanence.interfaces.IGpsInfoModel;
import com.uninew.maintanence.interfaces.IGpsInfoPresenter;
import com.uninew.maintanence.interfaces.IGpsInfoView;
import com.uninew.maintanence.model.GpsModel;

import java.util.Map;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GpsPresenter implements IGpsInfoPresenter {
    private IGpsInfoModel mIGpsInfoModel;
    private IGpsInfoView mIGpsInfoView;

    public GpsPresenter(Context context,IGpsInfoView mIGpsInfoView) {
        this.mIGpsInfoView = mIGpsInfoView;
        mIGpsInfoModel = new GpsModel(context,this);
    }

    @Override
    public void registerGpsInfoListener() {
        mIGpsInfoModel.registerGpsInfoListener();
    }

    @Override
    public void unRegisterGpsInfoListener() {
        mIGpsInfoModel.unRegisterGpsInfoListener();
    }

    @Override
    public void ShowLocationSate(int type, String state) {
        mIGpsInfoView.ShowLocationSate(type,state);
    }

    @Override
    public void ShowLocationInfo(double longitude, double latitude, int sateNumber, int userNumber,int gpsNumber,int bdNUmber) {
        mIGpsInfoView.ShowLocationInfo(longitude,latitude,sateNumber,userNumber,gpsNumber,bdNUmber);
    }

    @Override
    public void ShowLocationgSignals(Map<Integer, Integer> signals) {
        mIGpsInfoView.ShowLocationgSignals(signals);
    }
}
