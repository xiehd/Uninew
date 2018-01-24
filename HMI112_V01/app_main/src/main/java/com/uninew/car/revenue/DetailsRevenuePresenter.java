package com.uninew.car.revenue;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.uninew.car.R;

/**
 * Created by Administrator on 2017/9/30 0030.
 */

public class DetailsRevenuePresenter implements DetailsRevenueContrat.Presenter {

    private DetailsRevenueContrat.View mView;
    private Context mContext;
    public DetailsRevenuePresenter(DetailsRevenueContrat.View view, Context context){
        this.mView = view;
        this.mContext = context;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setwBoardingTime(String time) {
        mView.showBoardingTime(revenueTime(time));
    }

    @Override
    public void setFinishTime(String time) {
        mView.showFinishTime(finishTime(time));
    }

    @Override
    public void setDriningTime(String time) {
        mView.showDriningTime(time);
    }

    @Override
    public void setDrivingMileage(double Mileage) {
        mView.showDrivingMileage(Mileage+mContext.getString(R.string.kilometre));
    }

    @Override
    public void setBareMileage(double Mileage) {
        mView.showBareMileage(Mileage+mContext.getString(R.string.kilometre));
    }

    @Override
    public void setRavelType(int type) {

    }

    @Override
    public void setTrips(int trips) {
        mView.showTrips(trips);
    }

    @Override
    public void setWaitTime(String time) {
        mView.showWaitTime(time+mContext.getString(R.string.minute));
    }

    @Override
    public void setSurcharge(int surcharge) {
        mView.showSurcharge(surcharge+mContext.getString(R.string.yuan));
    }
    String sy = "01";
    String sm = "01";
    String sd = "01";

    private String finishTime(String endTime){
        String eHour =endTime.substring(0, 2);
        String eMinute = endTime.substring(2, 4);
        return "20" + sy + "_" + sm + "_" + sd + " " + eHour + ":" + eMinute + ":" + "00";
    }

    private String revenueTime(String startTime) {
        String sHour = "00";
        String sMinute = "00";
        String sSecond = "00";
        if (startTime.length() >= 10) {
            sy = startTime.substring(0, 2);
            sm = startTime.substring(2, 4);
            sd = startTime.substring(4, 6);
            sHour = startTime.substring(6, 8);
            sMinute = startTime.substring(8, 10);
        }
        return "20" + sy + "_" + sm + "_" + sd + " " + sHour + ":" + sMinute + ":" + sSecond;
    }
}
