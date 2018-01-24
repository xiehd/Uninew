package com.uninew.car.Main;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.uninew.car.R;
import com.uninew.car.until.ActivityAction;


public class MainActivity extends Activity implements MainContract.View, View.OnClickListener {

    private Button bt_heavycar;//重车
    private Button bt_emptycar;//空车
    private Button bt_pause;//暂停
    private TextView tv_price;//单价
    private TextView tv_mileage;//计程
    private TextView tv_timing;//计时
    private TextView tv_carstate;//车辆状态
    private TextView tv_drivername;//司机姓名
    private TextView tv_controlphone;//监督电话
    private TextView tv_certificate;//从业资格证
    private RatingBar rb_evaluation;//服务评分
    private MainContract.Presenter mPresenter;
    private ImageView iv_currentfare_ten;
    private ImageView iv_currentfare_bit;
    private ImageView iv_currentfare_point1;
    private ImageView iv_currentfare_point2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mPresenter = new MainPresenter(this, this.getApplicationContext());
        initView();
        initClickListener();
    }


    private void initClickListener() {
        bt_pause.setOnClickListener(this);
        bt_heavycar.setOnClickListener(this);
        bt_emptycar.setOnClickListener(this);
    }


    private void initView() {
        bt_heavycar = (Button) findViewById(R.id.bt_heavycar);
        bt_emptycar = (Button) findViewById(R.id.bt_emptycar);
        bt_pause = (Button) findViewById(R.id.bt_pause);
        tv_price = (TextView) findViewById(R.id.tv_main_content_price);
        tv_mileage = (TextView) findViewById(R.id.tv_mian_content_mileage);
        tv_timing = (TextView) findViewById(R.id.tv_mian_content_timing);
        tv_carstate = (TextView) findViewById(R.id.tv_main_carstate);
        tv_drivername = (TextView) findViewById(R.id.tv_main_drivername);
        tv_controlphone = (TextView) findViewById(R.id.tv_main_content_controlphone);
        tv_certificate = (TextView) findViewById(R.id.tv_main_content_certificate);
        rb_evaluation = (RatingBar) findViewById(R.id.rb_main_evaluation);
        iv_currentfare_point1 = (ImageView) findViewById(R.id.iv_currentfare_point1);
        iv_currentfare_ten = (ImageView) findViewById(R.id.iv_currentfare_ten);
        iv_currentfare_bit = (ImageView) findViewById(R.id.iv_currentfare_bit);
        iv_currentfare_point2 = (ImageView) findViewById(R.id.iv_currentfare_point2);

    }


    public void onCutoverActivity(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_mian_orders://电召
                mPresenter.changeActivityView(ActivityAction.Order_Activity_Action, this.getApplicationContext());
                break;
            case R.id.ll_mian_call://通话
                mPresenter.changeActivityView(ActivityAction.PHONE_ACTIVITY_ACTION, this.getApplicationContext());
                break;
            case R.id.ll_mian_navigation://导航
//                TtsUtil.getInstance(this.getApplicationContext()).speak("未有导航界面！");
                mPresenter.changeNavigation(this.getApplicationContext());
                break;
            case R.id.ll_mian_revenue://营收
                mPresenter.changeActivityView(ActivityAction.REVENUE_ACTIVITY_ACTION, this.getApplicationContext());
                break;
            case R.id.ll_mian_more://更多
                mPresenter.changeActivityView(ActivityAction.SETTINGS_ACTIVITY_ACTION, this.getApplicationContext());
                break;
            case R.id.ll_mian_msg://消息
                mPresenter.changeActivityView(ActivityAction.Message_Activity_Action, this.getApplicationContext());
                break;
            case R.id.iv_main_headportrait://签到
                mPresenter.changeActivityView(ActivityAction.SIGN_ACTIVITY_ACTION, this.getApplicationContext());
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
//        if (!SignActivity.IsSign){
//            mPresenter.changeActivityView(ActivityAction.SIGN_ACTIVITY_ACTION, this.getApplicationContext());
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {

    }

    @Override
    public void showPrice(float price) {
        tv_price.setText(price + getString(R.string.yuan));
    }

    @Override
    public void showMileage(float mileage) {
        tv_mileage.setText(mileage + getString(R.string.kilometre));
    }

    @Override
    public void showTiming(String time) {
        if(TextUtils.isEmpty(time))
            return;
        tv_timing.setText(time+" "+getString(R.string.minute));
    }

    @Override
    public void showTotal(float total) {
        int t = (int) (total * 100);
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
    public void showDriverName(String name) {
        tv_drivername.setText(name);
    }

    @Override
    public void showHeadPortrait(Bitmap bitmap) {

    }

    @Override
    public void showControlPhone(String phone) {
        tv_controlphone.setText(phone);
    }

    @Override
    public void showCertificate(String certificate) {
        tv_certificate.setText(certificate);
    }

    @Override
    public void showEvaluation(float evaluation) {
        rb_evaluation.setRating(evaluation);
    }

    @Override
    public void showCarState(int state) {
        switch (state) {
            case MainContract.CAR_STATE_EMPTY:
                tv_carstate.setText(getString(R.string.main_empty_car));
                bt_heavycar.setBackgroundResource(R.mipmap.button_s);
                bt_emptycar.setBackgroundResource(R.mipmap.button_p);
                bt_pause.setBackgroundResource(R.mipmap.button_s);
                break;
            case MainContract.CAR_STATE_HEAVY:
                tv_carstate.setText(getString(R.string.main_heavy_car));
                bt_heavycar.setBackgroundResource(R.mipmap.button_p);
                bt_emptycar.setBackgroundResource(R.mipmap.button_s);
                bt_pause.setBackgroundResource(R.mipmap.button_s);
                iv_currentfare_ten.setVisibility(View.INVISIBLE);
                showNumber(iv_currentfare_bit,0);
                showNumber(iv_currentfare_point1,0);
                showNumber(iv_currentfare_point2,0);
                tv_mileage.setText("0.00"+getString(R.string.kilometre));
                tv_timing.setText("00:00:00");
                break;
            case MainContract.CAR_STATE_PAUSE:
                tv_carstate.setText(getString(R.string.main_pause));
                bt_heavycar.setBackgroundResource(R.mipmap.button_s);
                bt_emptycar.setBackgroundResource(R.mipmap.button_s);
                bt_pause.setBackgroundResource(R.mipmap.button_p);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_heavycar:
                mPresenter.setCarState(MainContract.CAR_STATE_HEAVY);
                break;
            case R.id.bt_emptycar:
                mPresenter.setCarState(MainContract.CAR_STATE_EMPTY);
                break;
            case R.id.bt_pause:
                mPresenter.setCarState(MainContract.CAR_STATE_PAUSE);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //activity复用时调用
        super.onNewIntent(intent);
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
}

