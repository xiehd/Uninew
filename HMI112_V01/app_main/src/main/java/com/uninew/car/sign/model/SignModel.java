package com.uninew.car.sign.model;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.uninew.car.R;
import com.uninew.car.sign.interfaces.ISignModel;
import com.uninew.car.sign.interfaces.ISignPresenter;
import com.uninew.car.until.ToastCommon;
import com.uninew.net.JT905.bean.T_LocationReport;
import com.uninew.net.JT905.bean.T_SignInReport;
import com.uninew.net.JT905.bean.T_SignOutReport;
import com.uninew.net.JT905.comm.client.ClientReceiveManage;
import com.uninew.net.JT905.comm.client.ClientSendManage;
import com.uninew.net.JT905.comm.client.IClientReceiveListener;
import com.uninew.net.JT905.comm.client.IClientReceiveManage;
import com.uninew.net.JT905.comm.client.IClientSendManage;

/**
 * Created by Administrator on 2017/9/14.
 */

public class SignModel implements ISignModel {
    private ISignPresenter mISignPresenter;
    private Context mContext;
    private IClientReceiveManage mClientReceiveManage;
    private IClientSendManage mClientSendManage;

    public SignModel(ISignPresenter mISignPresenter, Context con) {
        this.mISignPresenter = mISignPresenter;
        this.mContext = con;

        mClientReceiveManage = new ClientReceiveManage(mContext);
        mClientSendManage = new ClientSendManage(mContext);


        mISignPresenter.ShowOperateTime("08-03 06:00", "08-03 22:00");
        mISignPresenter.ShowDriverInfo(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.driver_image), "李四");
        mISignPresenter.ShowOperateData(20, 80, 30);
        mISignPresenter.ShowOperateMoney(74.1f);
    }

    @Override
    public void SetSignOut(String number) {
        T_SignOutReport test = new T_SignOutReport();
        test.setLocationReport(new T_LocationReport());
        test.setBusinessLicense("123456789");
        test.setDriverCertificate("0000000");
        test.setCarNumber("粤B00005");
        test.setMeterKValue(1);
        test.setBootTime("20170811112233");
        test.setShutDownTime("20170811114455");
        test.setMileage(22f);
        test.setOperationMileage(11f);
        test.setTrips(3);
        test.setTimingTime("2233");
        test.setTotalIncome(55f);
        test.setCardIncome(22f);
        test.setCardTimes(0);
        test.setBetweenMileage(11f);
        test.setTotalMileage(88f);
        test.setUnitPrice(2f);
        test.setTotalOperation(5);
        test.setSignOutWay(0);
        test.setExtend(new byte[1]);
        mClientSendManage.signOutReport(test);

        //保存数据库
//        SignOrSignOut signOrSignOut = new SignOrSignOut();
//        db.saveDBData(signOrSignOut);
    }

    @Override
    public void SetSignIn(String number) {
        T_SignInReport test = new T_SignInReport();
        test.setBusinessLicense("123456789");
        test.setDriverCertificate("0000000");
        test.setCarNumber("粤B00005");
        test.setBootTime("20170811112233");
        test.setExtend(new byte[1]);
        mClientSendManage.signInReport(test);


        //保存数据库

    }

    //考勤应答
    private IClientReceiveListener.IPushCardListener mIPushCardListener = new IClientReceiveListener.IPushCardListener() {
        @Override
        public void signInReportAns(int i) {
                if (i == 0x00){
                    ToastCommon.ToastShow(mContext, mContext.getResources().getString(R.string.sign_in_success));
                }else{
                    ToastCommon.ToastShow(mContext, mContext.getResources().getString(R.string.sign_in_fail));
                }
                mISignPresenter.SignIn(i);
        }

        @Override
        public void signOutReportAns(int i) {
            if (i == 0x00){
                ToastCommon.ToastShow(mContext, mContext.getResources().getString(R.string.sign_out_success));
            }else{
                ToastCommon.ToastShow(mContext, mContext.getResources().getString(R.string.sign_out_success));
            }
            mISignPresenter.SignOut(i);
        }
    };

    @Override
    public void registerConnectStateListener() {
        if (mClientReceiveManage != null)
            mClientReceiveManage.registerPushCardListener(mIPushCardListener);
    }

    @Override
    public void unregisterConnectStateListener() {
        if (mClientReceiveManage != null)
            mClientReceiveManage.unRegisterPushCardListener();
    }
}
