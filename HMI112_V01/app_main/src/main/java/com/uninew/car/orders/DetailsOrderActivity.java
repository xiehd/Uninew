package com.uninew.car.orders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.uninew.car.R;
import com.uninew.car.dialog.PromptDialog;
import com.uninew.car.db.order.Order;
import com.uninew.car.db.order.OrderKey;

/**
 * Created by Administrator on 2017/9/13 0013.
 */

public class DetailsOrderActivity extends Activity implements DetailsOrderContrat.View, View.OnClickListener {

    private static final String TAG = "DetailsOrderActivity";
    private DetailsOrderContrat.Presenter mPresenter;
    private TextView tv_state;
    private TextView tv_id;
    private TextView tv_show_time;
    private TextView tv_title_time;
    private TextView tv_phone;
    private LinearLayout ll_phone;
    private TextView tv_callTime;
    private LinearLayout ll_serviceCharge;
    private TextView tv_serviceCharge;
    private TextView tv_details;
    private LinearLayout ll_bt_action;
    private Button bt_cancel;
    private Button bt_finish;
    private Button bt_back;
    private Order mOrder;
    public static final String ORDER_INTENT_KEY = "order";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_details);
        init();
    }

    private void init() {
        mPresenter = new DetailsOrderPresenter(this, this.getApplicationContext());
        iniView();
        initClickListener();
        Intent intent = getIntent();
        if (intent != null) {
            mOrder = (Order) intent.getSerializableExtra(ORDER_INTENT_KEY);
        }
        if (mOrder != null) {
            mPresenter.setState(mOrder.getOrderState());
            mPresenter.setServiceCharge(mOrder.getServiceCharge());
            mPresenter.setCallTime(mOrder.getNeedTime());
            mPresenter.setDetails(mOrder.getBusinessDescription());
            mPresenter.setId(mOrder.getBusinessId() + "");
            mPresenter.setPhone(mOrder.getPassengerPhoneNumber());
            mPresenter.setTime(mOrder.getReceiveTime());
        }
    }

    private void initClickListener() {
        bt_back.setOnClickListener(this);
        bt_finish.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
    }

    private void iniView() {
        tv_state = (TextView) findViewById(R.id.tv_order_show_state);
        tv_id = (TextView) findViewById(R.id.tv_order_show_id);
        tv_show_time = (TextView) findViewById(R.id.tv_order_show_time);
        tv_title_time = (TextView) findViewById(R.id.tv_order_title_time);
        tv_phone = (TextView) findViewById(R.id.tv_order_show_phone);
        ll_phone = (LinearLayout) findViewById(R.id.ll_orders_phone);
        tv_callTime = (TextView) findViewById(R.id.tv_order_show_calltime);
        ll_serviceCharge = (LinearLayout) findViewById(R.id.ll_orders_servicecharge);
        tv_serviceCharge = (TextView) findViewById(R.id.tv_order_servicecharge);
        tv_details = (TextView) findViewById(R.id.tv_order_details);
        ll_bt_action = (LinearLayout) findViewById(R.id.ll_bt_action);
        bt_cancel = (Button) findViewById(R.id.bt_order_cancel);
        bt_finish = (Button) findViewById(R.id.bt_order_finish);
        bt_back = (Button) findViewById(R.id.bt_order_back);
    }

    @Override
    public void setPresenter(DetailsOrderContrat.Presenter presenter) {

    }

    @Override
    public void showState(int state) {
        switch (state) {
            case OrderKey.OrderStateKey.BADE_STATE:
                tv_state.setText(getString(R.string.orders_bade_state));
                tv_title_time.setText(getString(R.string.order_title_bade_time));
                break;
            case OrderKey.OrderStateKey.DRIVER_CANCEL_STATE:
                tv_state.setText(getString(R.string.orders_driver_cancel_state));
                ll_bt_action.setVisibility(View.GONE);
                tv_title_time.setText(getString(R.string.order_title_cancel_time));
                break;
            case OrderKey.OrderStateKey.FAILURE_STATE:
                tv_state.setText(getString(R.string.orders_failure_state));
                ll_bt_action.setVisibility(View.GONE);
                ll_serviceCharge.setVisibility(View.GONE);
                ll_phone.setVisibility(View.GONE);
                tv_title_time.setText(getString(R.string.order_title_grad_time));
                break;
            case OrderKey.OrderStateKey.FINISH_STATE:
                tv_state.setText(getString(R.string.orders_finish_state));
                ll_bt_action.setVisibility(View.GONE);
                tv_title_time.setText(getString(R.string.order_title_finish_time));
                break;
            case OrderKey.OrderStateKey.OTHER_STATE:
                tv_state.setText(getString(R.string.orders_other_state));
                ll_bt_action.setVisibility(View.GONE);
                ll_serviceCharge.setVisibility(View.GONE);
                ll_phone.setVisibility(View.GONE);
                tv_title_time.setText(getString(R.string.order_title_cancel_time));
                break;
            case OrderKey.OrderStateKey.PASSENGER_CANCEL_STATE:
                tv_state.setText(getString(R.string.orders_passenger_cancel_state));
                ll_bt_action.setVisibility(View.GONE);
                tv_title_time.setText(getString(R.string.order_title_grad_time));
                break;
        }
    }

    @Override
    public void showId(String id) {
        tv_id.setText(id);
    }

    @Override
    public void showTime(String time) {
        tv_show_time.setText(time);
    }

    @Override
    public void showCallTime(String time) {
        tv_callTime.setText(time);
    }

    @Override
    public void showPhone(String phone) {
        tv_phone.setText(phone);
    }

    @Override
    public void showServiceCharge(double i) {
        if (i > 0) {
            tv_serviceCharge.setText(i + getString(R.string.yuan));
        } else {
            tv_serviceCharge.setText(getString(R.string.no));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);// 淡出淡入动画效果
    }

    @Override
    public void showDetails(String details) {
        tv_details.setText(details);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_order_back:
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);// 淡出淡入动画效果
                mPresenter.onBack();
                break;
            case R.id.bt_order_cancel:
                mPresenter.onCancel(mOrder);
                break;
            case R.id.bt_order_finish:
                mPresenter.onFinish(mOrder);
                break;
        }
    }


}
