package com.uninew.car.sign.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uninew.car.R;
import com.uninew.car.dialog.SignDialog;
import com.uninew.car.sign.interfaces.ISignPresenter;
import com.uninew.car.sign.interfaces.ISignView;
import com.uninew.car.sign.presenter.SignPresenter;


public class SignActivity extends Activity implements ISignView, View.OnClickListener, SignDialog.ClickListenerInterface {

    private SignDialog mSignDialog;
    private ISignPresenter mISignPresenter;
    private ImageView sign_driver_image;
    private TextView sign_out_ok, sign_drever_name, sign_money, sign_out_starTime, sign_out_endTime, sign_out_operNumber, sign_out_runKm,
            sign_out_carryKm;
    public static boolean IsSign = false;//是否签到
    private static String carNumber = "00000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        initView();
        mSignDialog = new SignDialog(this);
        mISignPresenter = new SignPresenter(this, this);
        if (savedInstanceState != null){
            carNumber = savedInstanceState.getString("car_number", "");
        }
        setListener();
        updateState();
    }

    private void initView() {
        sign_driver_image = (ImageView) findViewById(R.id.sign_driver_image);
        sign_out_ok = (TextView) findViewById(R.id.sign_out_ok);
        sign_money = (TextView) findViewById(R.id.sign_money);
        sign_out_starTime = (TextView) findViewById(R.id.sign_out_starTime);
        sign_out_endTime = (TextView) findViewById(R.id.sign_out_endTime);
        sign_out_operNumber = (TextView) findViewById(R.id.sign_out_operNumber);
        sign_out_runKm = (TextView) findViewById(R.id.sign_out_runKm);
        sign_out_carryKm = (TextView) findViewById(R.id.sign_out_carryKm);
        sign_drever_name = (TextView) findViewById(R.id.sign_drever_name);
    }

    private void setListener() {
        sign_driver_image.setOnClickListener(this);
        sign_out_ok.setOnClickListener(this);
        mSignDialog.setClicklistener(this);
        mISignPresenter.registerConnectStateListener();
    }

    private void updateState() {
        if (IsSign) {
//            mSignDialog.setCancelable(true);
//            mSignDialog.dismiss();
//            finish();
        } else {
            mSignDialog.setCancelable(true);
            mSignDialog.show();
        }
    }

    @Override
    public void ShowDriverInfo(Bitmap bitmap, String name) {
        sign_driver_image.setImageBitmap(bitmap);
        sign_drever_name.setText(name);
    }

    @Override
    public void ShowOperateMoney(float money) {
        sign_money.setText(String.valueOf(money));
    }

    @Override
    public void ShowOperateTime(String startTime, String endTime) {
        sign_out_starTime.setText(startTime);
        sign_out_endTime.setText(endTime);
    }

    @Override
    public void ShowOperateData(int OperNumber, float runKm, float carryNumber) {
        sign_out_operNumber.setText(String.valueOf(OperNumber));
        sign_out_runKm.setText(String.valueOf(runKm));
        sign_out_carryKm.setText(String.valueOf(carryNumber));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_driver_image:
                break;
            case R.id.sign_out_ok://签退
                mISignPresenter.SetSignOut(carNumber);
                mSignDialog.setCancelable(true);
                mSignDialog.show();
                break;
        }
    }


    //签到
    @Override
    public void sign(String jobNumber) {
        mISignPresenter.SetSignIn(jobNumber);

    }

    @Override
    public void SignIn(int result) {
        if (result == 0) {//成功
            IsSign = true;
            //updateState();
//            mSignDialog.setCancelable(true);
//            mSignDialog.dismiss();
            finish();
        }else{//失败

        }
    }

    @Override
    public void SignOut(int result) {
        if (result == 0) {
            IsSign = false;
            updateState();
        }else{

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSignDialog != null && mSignDialog.isShowing()){
            mSignDialog.setCancelable(true);
            mSignDialog.dismiss();
        }
        mISignPresenter.unregisterConnectStateListener();
    }

}
