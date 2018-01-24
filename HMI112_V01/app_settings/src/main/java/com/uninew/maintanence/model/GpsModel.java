package com.uninew.maintanence.model;

import android.content.Context;
import android.util.Log;

import com.uninew.location.DefineLocation;
import com.uninew.location.GpsInfo;
import com.uninew.location.GpsInfoManage;
import com.uninew.location.IGpsInfoListener;
import com.uninew.location.IGpsInfoManage;
import com.uninew.maintanence.interfaces.IGpsInfoModel;
import com.uninew.maintanence.interfaces.IGpsInfoPresenter;
import com.uninew.settings.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GpsModel implements IGpsInfoModel {
    private IGpsInfoPresenter mIGpsInfoPresenter;
    private Context mContext;
    private IGpsInfoManage mGpsInfoManage;

    public GpsModel(Context mContext, IGpsInfoPresenter mIGpsInfoPresenter) {
        this.mContext = mContext;
        this.mIGpsInfoPresenter = mIGpsInfoPresenter;
        mGpsInfoManage = new GpsInfoManage();
        init();
    }

    private void init() {
        registerGpsInfoListener();
    }

    private IGpsInfoListener mGpsInfoListener = new IGpsInfoListener() {

        @Override
        public void gpsInfo(GpsInfo gpsInfo) {
            // TODO Auto-generated method stub
            showGpsInfo(gpsInfo);
        }
    };

    protected void showGpsInfo(GpsInfo gpsInfo) {
        if (gpsInfo != null) {
            // Log.i("xhd",gpsInfo.toString());
            int allgps = gpsInfo.getVisiableSatellite() + gpsInfo.getGpsVisiableSatellite();
            int effecgps = gpsInfo.getEffectiveSatellite() + gpsInfo.getGpsEffectiveSatellite();

            mIGpsInfoPresenter.ShowLocationInfo(gpsInfo.getLongitude(), gpsInfo.getLatitude(), allgps,
                    effecgps,gpsInfo.getGpsVisiableSatellite(),gpsInfo.getVisiableSatellite());

            Map<Integer, Integer> allSignals = new HashMap<>();
            if (gpsInfo.getSignals() != null) {
                Iterator<Map.Entry<Integer, Integer>> it = gpsInfo.getSignals().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Integer, Integer> entry = it.next();
                    allSignals.put(entry.getKey(),entry.getValue());
                }
            }
            if (gpsInfo.getGpsSignals() != null) {
                Iterator<Map.Entry<Integer, Integer>> itgps = gpsInfo.getGpsSignals().entrySet().iterator();
                while (itgps.hasNext()) {
                    Map.Entry<Integer, Integer> entrygps = itgps.next();
                    allSignals.put(entrygps.getKey(),entrygps.getValue());
                }
            }

            mIGpsInfoPresenter.ShowLocationgSignals(allSignals);
            if (gpsInfo.isPositioned()) {
                mIGpsInfoPresenter.ShowLocationSate(0, mContext.getResources().getString(R.string.maintance_location_ok));
            } else {
                mIGpsInfoPresenter.ShowLocationSate(0, mContext.getResources().getString(R.string.maintance_location_no));
            }
        }
    }

    /**
     * 打开位置监听
     */
    @Override
    public void registerGpsInfoListener() {
        mGpsInfoManage.registerGpsInfoListener(DefineLocation.Location_ReadType_NMEA, mContext, mGpsInfoListener);
    }

    /**
     * 关闭位置监听
     */
    @Override
    public void unRegisterGpsInfoListener() {
        if (mGpsInfoManage != null) {
            mGpsInfoManage.unRegisterGpsInfoListener(DefineLocation.Location_ReadType_NMEA);
        }
    }


}
