package com.uninew.car.phone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.uninew.car.R;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class CallActivity extends Activity implements CallContract.View, View.OnClickListener {

    private CallContract.Presenter mPresenter;
    private ImageView iv_avatar;
    private TextView tv_contact;
    private TextView tv_attribution;
    private Button bt_answer;
    private Button bt_hang_up;


//    public static final String INTENT_CALL_STATE = "state";
//    public static final String INTENT_CALL_NUMBER = "number";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(R.layout.activity_call);
        init();
    }


    private void init() {
        mPresenter = new CallPresenter(this, this.getApplicationContext());
        initView();
        initListener();
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
//        int state = intent.getIntExtra(INTENT_CALL_STATE, -1);
//        String number = intent.getStringExtra(INTENT_CALL_NUMBER);
//        if (TextUtils.isEmpty(number)) {
//            finish();
//            return;
//        }
//        if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
//            mPresenter.callNumber(number);
//        }
//        mPresenter.setCallState(state);
//        mPresenter.setPhone(number);
    }

    private void initListener() {
        bt_hang_up.setOnClickListener(this);
        bt_answer.setOnClickListener(this);
    }

    private void initView() {
        iv_avatar = (ImageView) findViewById(R.id.iv_call_avatar);
        tv_contact = (TextView) findViewById(R.id.tv_call_contact);
        tv_attribution = (TextView) findViewById(R.id.tv_call_attribution);
        bt_answer = (Button) findViewById(R.id.bt_call_answer);
        bt_hang_up = (Button) findViewById(R.id.bt_call_hang_up);
    }

    @Override
    public void setPresenter(CallContract.Presenter presenter) {

    }

    @Override
    public void showAvatar(Bitmap bitmap) {
        iv_avatar.setImageBitmap(bitmap);
    }

    @Override
    public void showName(String name) {
        tv_contact.setText(name);
    }

    @Override
    public void showPhone(String phone) {
        tv_contact.setText(phone);
    }

    @Override
    public void showCallState(int state) {
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:// 来电响铃
                bt_answer.setVisibility(View.VISIBLE);
                bt_hang_up.setVisibility(View.VISIBLE);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:// 接听电话;电话打进来接通状态；电话打出时首先监听
                bt_answer.setVisibility(View.GONE);
                break;
            case TelephonyManager.CALL_STATE_IDLE:// 挂断电话;不管是电话打出去还是电话打进来都会监听到的状态。
                finish();
                break;
            default:
                bt_answer.setVisibility(View.VISIBLE);
                bt_hang_up.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void showCallDuration(int time) {

    }

    @Override
    public void showAttribution(String attribution) {
        tv_attribution.setText(attribution);
    }

    @Override
    public void endCall() {
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_call_answer:
                mPresenter.answer();
                break;
            case R.id.bt_call_hang_up:
                mPresenter.hangUp();
                break;
        }
    }
}
