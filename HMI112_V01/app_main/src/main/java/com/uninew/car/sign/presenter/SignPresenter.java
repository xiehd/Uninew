package com.uninew.car.sign.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.uninew.car.sign.interfaces.ISignModel;
import com.uninew.car.sign.interfaces.ISignPresenter;
import com.uninew.car.sign.interfaces.ISignView;
import com.uninew.car.sign.model.SignModel;

/**
 * Created by Administrator on 2017/9/14.
 */

public class SignPresenter implements ISignPresenter {

    private ISignModel mISignModel;
    private ISignView mISignView;

    public SignPresenter(ISignView mISignView, Context con) {
        this.mISignView = mISignView;
        mISignModel = new SignModel(this, con);
    }

    @Override
    public void SetSignOut(String number) {
        mISignModel.SetSignOut(number);
    }

    @Override
    public void SetSignIn(String number) {
        mISignModel.SetSignIn(number);
    }

    @Override
    public void registerConnectStateListener() {
        mISignModel.registerConnectStateListener();
    }

    @Override
    public void unregisterConnectStateListener() {
        mISignModel.unregisterConnectStateListener();
    }

    @Override
    public void ShowDriverInfo(Bitmap bitmap, String name) {
        mISignView.ShowDriverInfo(bitmap, name);
    }

    @Override
    public void ShowOperateTime(String startTime, String endTime) {
        mISignView.ShowOperateTime(startTime, endTime);
    }

    @Override
    public void ShowOperateMoney(float money) {
        mISignView.ShowOperateMoney(money);
    }

    @Override
    public void ShowOperateData(int OperNumber, float runKm, float carryNumber) {
        mISignView.ShowOperateData(OperNumber, runKm, carryNumber);
    }

    @Override
    public void SignIn(int result) {
        mISignView.SignIn(result);
    }

    @Override
    public void SignOut(int result) {
        mISignView.SignOut(result);
    }
}
