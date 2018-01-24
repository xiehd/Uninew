package com.uninew.settings.presenter;

import android.content.Context;

import com.uninew.settings.interfaces.ILighetModel;
import com.uninew.settings.interfaces.ILightPresenter;
import com.uninew.settings.interfaces.ILightView;
import com.uninew.settings.model.LightModel;


/**
 * Created by Administrator on 2017/9/1.
 */

public class LightPresenter implements ILightPresenter {

    private ILighetModel mILighetModel;
    private ILightView mILightView;
    public LightPresenter(Context mContext, ILightView mILightView){
        this.mILightView = mILightView;
        mILighetModel = new LightModel(mContext);
    }

    @Override
    public void setAutoBrightness(int mode) {
        mILighetModel.setAutoBrightness(mode);
    }

    @Override
    public void addLight() {

    }

    @Override
    public void subLight() {

    }

    @Override
    public void setLight(int light) {

    }

    @Override
    public void ShowTochLight(boolean mode) {
            mILightView.ShowTochLight(mode);
    }

    @Override
    public void ShowCurrentLight(int light) {
            mILightView.ShowCurrentLight(light);
    }
}
