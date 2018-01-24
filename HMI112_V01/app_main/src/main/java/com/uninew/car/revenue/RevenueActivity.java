package com.uninew.car.revenue;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.uninew.car.R;
import com.uninew.car.adapter.RevenueAdapter;
import com.uninew.car.db.revenue.Revenue;
import com.uninew.car.orders.DetailsOrderActivity;

import java.text.DecimalFormat;
import java.util.List;

public class RevenueActivity extends Activity implements RevenueContrat.View,
        RevenueAdapter.OnRevenueItemClickListener,View.OnClickListener {

    private ListView lv_show_revenue;
    private RevenueContrat.Presenter mPresenter;
    private RevenueAdapter mAdapter;
    private ImageView iv_currentfare_ten;
    private ImageView iv_currentfare_bit;
    private ImageView iv_currentfare_point1;
    private ImageView iv_currentfare_point2;
    private TextView tv_waitTime;
    private TextView tv_drivingMileage;
    private TextView tv_bareMileage;
    private TextView tv_times;
    private TextView tv_bareRate;
    private TextView tv_amount;
    private TextView tv_cardAmount;
    private TextView bt_history;
    private TextView tv_title;
    private static final DecimalFormat df   = new DecimalFormat("######0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.stop();
    }

    private void init() {
        mPresenter = new RevenuesPresenter(this, this.getApplicationContext());
        initView();
    }

    private void initView() {
        lv_show_revenue = (ListView) findViewById(R.id.lv_show_revenue);
        iv_currentfare_point1 = (ImageView) findViewById(R.id.iv_currentfare_point1);
        iv_currentfare_ten = (ImageView) findViewById(R.id.iv_currentfare_ten);
        iv_currentfare_bit = (ImageView) findViewById(R.id.iv_currentfare_bit);
        iv_currentfare_point2 = (ImageView) findViewById(R.id.iv_currentfare_point2);
        tv_waitTime = (TextView) findViewById(R.id.tv_revenue_waitTime);
        tv_drivingMileage = (TextView) findViewById(R.id.tv_revenue_drivingMileage);
        tv_bareMileage = (TextView) findViewById(R.id.tv_revenue_bareMileage);
        tv_times = (TextView) findViewById(R.id.tv_revenue_times);
        tv_bareRate = (TextView) findViewById(R.id.tv_revenue_bareRate);
        tv_amount = (TextView) findViewById(R.id.tv_revenue_amount);
        tv_cardAmount = (TextView) findViewById(R.id.tv_revenue_cardAmount);
        bt_history = (TextView) findViewById(R.id.bt_revenue_history);
        tv_title = (TextView) findViewById(R.id.tv_revenue_title);
        bt_history.setOnClickListener(this);
    }

    @Override
    public void setPresenter(RevenueContrat.Presenter presenter) {

    }

    @Override
    public void showWaitTime(int waitTime) {
        tv_waitTime.setText(waitTime+" "+getString(R.string.minute));
    }

    @Override
    public void setDrivingMileage(float drivingMileage) {
        tv_drivingMileage.setText(df.format(drivingMileage)+" "+getString(R.string.kilometre));
    }

    @Override
    public void showBareMileage(float bareMileage) {
        tv_bareMileage.setText(df.format(bareMileage)+" "+getString(R.string.kilometre));
    }

    @Override
    public void showTimes(int times) {
        tv_times.setText(times+"");
    }

    @Override
    public void showBareRate(int rate) {
        tv_bareRate.setText(rate+"%");
    }

    @Override
    public void setAmount(float amount) {
        tv_amount.setText(df.format(amount)+" "+getString(R.string.yuan));
        int t = (int) (amount * 100);
        int ten = t/1000;
        if(ten >= 10){
            ten = ten - (ten / 10)*10;
        }
        if(ten != 0) {
            iv_currentfare_ten.setVisibility(View.VISIBLE);
            showNumber(iv_currentfare_ten, ten);
        }else {
            iv_currentfare_ten.setVisibility(View.INVISIBLE);
        }
        int bit = (t - (t/1000)*1000)/100;
        showNumber(iv_currentfare_bit,bit);
        int point1 = (t - (t/1000)*1000 - bit*100)/10;
        showNumber(iv_currentfare_point1,point1);
        int point2 = t - (t/1000)*1000 - bit*100 - point1*10;
        showNumber(iv_currentfare_point2,point2);
        Log.d("mm","t:"+t+",ten:"+ten+",bit:"+bit+",point1:"+point1+",point2:"+point2);
    }

    @Override
    public void showCardAmount(float amount) {
        tv_cardAmount.setText(df.format(amount)+getString(R.string.yuan));
    }

    @Override
    public void showDetailsRevenue(Revenue revenue) {
        Intent intent = new Intent(this, DetailsRevenueActivity.class);
        intent.putExtra(DetailsRevenueActivity.DETAILSREVENUEKEY,revenue);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);// 淡出淡入动画效果
    }

    @Override
    public void showRevenues(List<Revenue> revenues) {
        mAdapter = new RevenueAdapter(revenues, this.getApplicationContext());
        mAdapter.setOnRevenueItemClickListener(this);
        lv_show_revenue.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(Revenue item, int viewId, int position) {
        mPresenter.showDetailsRevenue(item);
    }

    private void showNumber(ImageView v,int n){
        switch (n){
            case 0:
                v.setImageResource(R.mipmap.zero);
                break;
            case 1:
                v.setImageResource(R.mipmap.one);
                break;
            case 2:
                v.setImageResource(R.mipmap.two);
                break;
            case 3:
                v.setImageResource(R.mipmap.three);
                break;
            case 4:
                v.setImageResource(R.mipmap.four);
                break;
            case 5:
                v.setImageResource(R.mipmap.five);
                break;
            case 6:
                v.setImageResource(R.mipmap.six);
                break;
            case 7:
                v.setImageResource(R.mipmap.seven);
                break;
            case 8:
                v.setImageResource(R.mipmap.eight);
                break;
            case 9:
                v.setImageResource(R.mipmap.nine);
                break;
        }
    }

    private boolean isHistory = false;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bt_revenue_history:
                if(!isHistory){
                    mPresenter.setHistoryRevenue();
                    bt_history.setText(R.string.revenue_current_button);
                    tv_title.setText(R.string.revenue_history);
                    isHistory = true;
                }else {
                    mPresenter.setCurrentRevenue();
                    bt_history.setText(R.string.revenue_history_button);
                    tv_title.setText(R.string.revenue_current);
                    isHistory = false;
                }
                break;
        }
    }
}
