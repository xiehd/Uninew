package com.uninew.car.revenue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.uninew.car.R;
import com.uninew.car.db.revenue.Revenue;
import com.uninew.net.Taximeter.bean.P_TaxiOperationDataReport;

public class DetailsRevenueActivity extends Activity implements DetailsRevenueContrat.View {

    private DetailsRevenueContrat.Presenter mPresenter;
    private TextView tv_details_time;
    private TextView tv_boarding_time;
    private TextView tv_driving_time;
    private TextView tv_driving_mileage;
    private TextView tv_bare_mileage;
    private TextView tv_current_times;
    private TextView tv_travel_type;
    private TextView tv_wait_timer;
    private TextView tv_details_surcharge;

    public static final String DETAILSREVENUEKEY = "Revenue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_details_revenue);
        init();
    }

    private void init() {
        mPresenter = new DetailsRevenuePresenter(this, this.getApplicationContext());
        initView();
        Intent intent = getIntent();
        if(intent == null){
            finish();
            return;
        }
        Revenue revenue = (Revenue) intent.getSerializableExtra(DETAILSREVENUEKEY);
        if(revenue == null){
            finish();
            return;
        }
        byte[] buffers = revenue.getRevenueDatas();
        P_TaxiOperationDataReport dataReport = new P_TaxiOperationDataReport();
        dataReport.getDataPacket(buffers);
        mPresenter.setBareMileage(dataReport.getEmptyMileage());
        mPresenter.setDriningTime(getTime(dataReport.getUpCarTime(),dataReport.getDownCarTime()));
        mPresenter.setDrivingMileage(dataReport.getMileage());
        mPresenter.setwBoardingTime(dataReport.getUpCarTime());
        mPresenter.setFinishTime(dataReport.getDownCarTime());
        mPresenter.setWaitTime(dataReport.getWaitTimingTime());
        mPresenter.setSurcharge((int) dataReport.getSurcharge());
        mPresenter.setTrips(dataReport.getTrips());
    }


    private String getTime(String startTime, String endTime) {
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            return null;
        }
        int sHour = 0;
        int sMinute = 0;
        int sSecond = 0;
        int eHour = 0;
        int eMinute = 0;
        int eSecond = 0;
//        Pattern pattern = Pattern.compile("[^0-9]");
//        Matcher matcher = pattern.matcher(startTime);
//        String[] temp = matcher.replaceAll(" ").split(" ");
//        Log.d("mm","temp:"+ Arrays.toString(temp));
        if (startTime.length() >= 10) {
            sHour = Integer.parseInt(startTime.substring(6, 8));
            sMinute = Integer.parseInt(startTime.substring(8, 10));
        }
        if (endTime.length() >= 4) {
            eHour = Integer.parseInt(endTime.substring(0, 2));
            eMinute = Integer.parseInt(endTime.substring(2, 4));
        }
        Log.d("mm", "sHour:" + sHour + ",sMinute:" + sMinute + ",sSecond:" + sSecond);
        Log.d("mm", "eHour:" + eHour + ",eMinute:" + eMinute + ",eSecond:" + eSecond);
        int s = eSecond - sSecond;
        int m = eMinute - sMinute;
        int h = eHour - sHour;
        if (s < 0) {
            s = s + 60;
            m = m - 1;
        }
        if (m < 0) {
            m = m + 60;
            h = h - 1;
        }
        if (h < 0) {
            h = 24 + h;
        }
        Log.d("mm", "h:" + h + ",m:" + m + ",s:" + s);
        return h * 60 + m + "";
    }

    private void initView() {
        tv_details_time = (TextView) findViewById(R.id.tv_revenue_details_time);
        tv_boarding_time = (TextView) findViewById(R.id.tv_revenue_boarding_time);
        tv_driving_time = (TextView) findViewById(R.id.tv_revenue_driving_time);
        tv_driving_mileage = (TextView) findViewById(R.id.tv_revenue_driving_mileage);
        tv_bare_mileage = (TextView) findViewById(R.id.tv_revenue_bare_mileage);
        tv_current_times = (TextView) findViewById(R.id.tv_revenue_current_times);
        tv_travel_type = (TextView) findViewById(R.id.tv_revenue_travel_type);
        tv_wait_timer = (TextView) findViewById(R.id.tv_revenue_wait_timer);
        tv_details_surcharge = (TextView) findViewById(R.id.tv_revenue_details_surcharge);
    }

    @Override
    public void setPresenter(DetailsRevenueContrat.Presenter presenter) {

    }

    @Override
    public void showBoardingTime(String time) {
        tv_boarding_time.setText(time);
    }

    @Override
    public void showFinishTime(String time) {
        tv_details_time.setText(time);
    }

    @Override
    public void showDriningTime(String time) {
        tv_driving_time.setText(time);
    }

    @Override
    public void showDrivingMileage(String Mileage) {
        tv_driving_mileage.setText(Mileage);
    }

    @Override
    public void showBareMileage(String Mileage) {
        tv_bare_mileage.setText(Mileage);
    }

    @Override
    public void showtRavelType(String type) {
        tv_travel_type.setText(type);
    }

    @Override
    public void showWaitTime(String time) {
        tv_wait_timer.setText(time);
    }

    @Override
    public void showSurcharge(String surcharge) {
        tv_details_surcharge.setText(surcharge);
    }

    @Override
    public void showTrips(int trips) {
        tv_current_times.setText(trips+"");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);// 淡出淡入动画效果
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);// 淡出淡入动画效果
    }
}
