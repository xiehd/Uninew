package com.uninew.settings.model;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

import com.uninew.settings.interfaces.ILighetModel;


/**
 * Created by Administrator on 2017/9/1.
 */

public class LightModel implements ILighetModel {


    private ContentResolver aContentResolver;
    private Context mContext;
    public LightModel(Context mContext) {
        this.mContext = mContext;
        aContentResolver = mContext.getContentResolver();
    }


    @Override
    public boolean isAutoBrightness() {
        boolean automicBrightness = false;
        try {
            automicBrightness = Settings.System.getInt(aContentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return automicBrightness;
    }

    @Override
    public int getScreenBrightness() {
        int nowBrightnessValue = 0;
        try {
            nowBrightnessValue = Settings.System.getInt(
                    aContentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    @Override
    public void setBrightness(int brightness) {
        try{
            Settings.System.putInt(aContentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
        }
        catch (Exception localException){
            localException.printStackTrace();
        }
    }

    @Override
    public void setAutoBrightness(int mode) {

    }
}
