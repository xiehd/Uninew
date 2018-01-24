package com.uninew.car.evaluation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.uninew.car.R;
import com.uninew.car.audio.TtsUtil;
import com.uninew.car.db.revenue.Revenue;
import com.uninew.car.until.ActivityAction;
import com.uninew.car.until.ToastCommon;
import com.uninew.net.JT905.bean.T_LocationReport;
import com.uninew.net.JT905.bean.T_OperationDataReport;
import com.uninew.net.JT905.comm.client.ClientSendManage;
import com.uninew.net.JT905.common.ProtocolTool;
import com.uninew.net.JT905.common.TimeTool;
import com.uninew.net.Taximeter.bean.P_TaxiOperationDataReport;

import java.text.DecimalFormat;
import java.util.Timer;

public class EvaluationActivity extends Activity implements EvaluationContract.View, View.OnClickListener {

    private Button bt_satisfied;
    private Button bt_commonly;
    private Button bt_dissatisfied;
    private TextView tv_amount;
    private TextView tv_time;
    private TextView tv_mileage;
    private TextView tv_surcharge;
    private TextView tv_order;

    //private Revenue mRevenue;
    private T_OperationDataReport operationDataReport;

    private EvaluationContract.Presenter mPresenter;
    private BroadCastKey mBroadCastKey;
    private Handler mHandler;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_evaluation);
        init();
    }

    private void init() {
        mPresenter = new EvaluationPresenter(this, this.getApplicationContext());
        initView();
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        operationDataReport = (T_OperationDataReport) intent.getSerializableExtra(ActivityAction.REVENUE_INTENT_KEY);
        if (operationDataReport == null && operationDataReport.getOperationDatas() != null
                && operationDataReport.getOperationDatas().length > 0) {
            finish();
            return;
        }
        P_TaxiOperationDataReport taxiOperationDataReportt = new P_TaxiOperationDataReport();
        taxiOperationDataReportt.getDataPacket(operationDataReport.getOperationDatas());
        mPresenter.setAmount(taxiOperationDataReportt.getTransactionIncome());
        mPresenter.setMileage(taxiOperationDataReportt.getMileage());
        mPresenter.setOrder(operationDataReport.getOrderId());
        mPresenter.setSurcharge(taxiOperationDataReportt.getSurcharge());
        mPresenter.setTime(getTime(taxiOperationDataReportt.getUpCarTime(), taxiOperationDataReportt.getDownCarTime()));
        TtsUtil.getInstance(this).speak(mContext.getResources().getString(R.string.evaluation_hini));
        initListener();
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
        return h * 60 + m+"";
    }

    private void initListener() {
        bt_commonly.setOnClickListener(this);
        bt_satisfied.setOnClickListener(this);
        bt_dissatisfied.setOnClickListener(this);

        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 30 * 1000);

        SetRegisterReceiver();

    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            ToastCommon.ToastShow(mContext, mContext.getResources().getString(R.string.evaluation_toast));
            mPresenter.sendOperationDataReport(operationDataReport,0x00);
            finish();
        }
    };

    private void SetRegisterReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.uninew.evaluat.key");
        if (mBroadCastKey == null) {
            mBroadCastKey = new BroadCastKey();
            this.registerReceiver(mBroadCastKey, intentFilter);
        }
    }

    private void initView() {
        bt_satisfied = (Button) findViewById(R.id.bt_evaluation_satisfied);
        bt_commonly = (Button) findViewById(R.id.bt_evaluation_commonly);
        bt_dissatisfied = (Button) findViewById(R.id.bt_evaluation_dissatisfied);
        tv_amount = (TextView) findViewById(R.id.tv_evaluation_amount_content);
        tv_time = (TextView) findViewById(R.id.tv_evaluation_time_content);
        tv_mileage = (TextView) findViewById(R.id.tv_evaluation_mileage_content);
        tv_surcharge = (TextView) findViewById(R.id.tv_evaluation_surcharge_content);
        tv_order = (TextView) findViewById(R.id.tv_evaluation_order_content);
    }

    @Override
    public void setPresenter(EvaluationContract.Presenter presenter) {

    }

    @Override
    public void showAmount(String amount) {
        tv_amount.setText(amount);
    }

    @Override
    public void showTime(String time) {
        tv_time.setText(time+" "+getString(R.string.minute));
    }

    @Override
    public void showMileage(String mileage) {
        tv_mileage.setText(mileage);
    }

    @Override
    public void showSurcharge(String surcharge) {
        tv_surcharge.setText(surcharge);
    }

    @Override
    public void showOrder(String order) {
        tv_order.setText(order);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_evaluation_satisfied://满意
                if (operationDataReport != null) {
                    ToastCommon.ToastShow(mContext, mContext.getResources().getString(R.string.evaluation_succeed));
                    mPresenter.sendOperationDataReport(operationDataReport,0x01);
                    mHandler.removeCallbacks(mRunnable);
                    finish();
                }
                break;
            case R.id.bt_evaluation_commonly://一般
                if (operationDataReport != null) {
                    ToastCommon.ToastShow(mContext, mContext.getResources().getString(R.string.evaluation_succeed));
                    mPresenter.sendOperationDataReport(operationDataReport,0x02);
                    mHandler.removeCallbacks(mRunnable);
                    finish();
                }
                break;
            case R.id.bt_evaluation_dissatisfied://不满意
                if (operationDataReport != null) {
                    ToastCommon.ToastShow(mContext, mContext.getResources().getString(R.string.evaluation_succeed));
                    mPresenter.sendOperationDataReport(operationDataReport,0x03);
                    mHandler.removeCallbacks(mRunnable);
                    finish();
                }
                break;
        }
    }

    class BroadCastKey extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "com.uninew.evaluat.key"://评价按键
                    int key_value = intent.getIntExtra("key", 0);
                    if (operationDataReport != null) {
                        ToastCommon.ToastShow(mContext, mContext.getResources().getString(R.string.evaluation_succeed));
                        mPresenter.sendOperationDataReport(operationDataReport,key_value);
                        finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBroadCastKey != null) {
            this.unregisterReceiver(mBroadCastKey);
            mBroadCastKey = null;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

}
