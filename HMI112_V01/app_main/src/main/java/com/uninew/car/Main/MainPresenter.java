package com.uninew.car.Main;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.uninew.car.service.MainService;
import com.uninew.net.JT905.bean.Location_AlarmFlag;
import com.uninew.net.JT905.bean.Location_TerminalState;
import com.uninew.net.JT905.comm.client.ClientReceiveManage;
import com.uninew.net.JT905.comm.client.ClientSendManage;
import com.uninew.net.JT905.comm.client.IClientReceiveListener;
import com.uninew.net.JT905.comm.client.IClientReceiveManage;
import com.uninew.net.JT905.comm.client.IClientSendManage;
import com.uninew.net.Taximeter.bean.P_TaxiOperationDataReport;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/9/1 0001.
 */

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mainView;
    private IClientSendManage clientSendManage;
    private IClientReceiveManage clientReceiveManage;
    private int mState = MainContract.CAR_STATE_EMPTY;
    private Context mContext = null;

    public MainPresenter(@NonNull MainContract.View mainView, Context context) {
        this.mainView = mainView;
        this.mainView.setPresenter(this);
        clientSendManage = new ClientSendManage(context);
        clientReceiveManage = new ClientReceiveManage(context);
        this.mContext = context;
    }

    @Override
    public void start() {
        mainView.showCarState(MainContract.CAR_STATE_EMPTY);
        clientReceiveManage.registerTaxiOperateStateListener(mTaxiOperateStateListener);//运营状态监听
    }

    @Override
    public void stop() {
//        clientReceiveManage.unRegisterTaxiOperateSateListener();
    }


    @Override
    public void setCarState(int state) {
        switch (state) {
            case MainContract.CAR_STATE_EMPTY:
                Location_TerminalState terminalState = new Location_TerminalState();
                if (mState == MainContract.CAR_STATE_HEAVY) {
                    terminalState.setHeavyToEmpty(true);
                }
                terminalState.setHeavy(false);
                terminalState.setRunning(true);
                clientSendManage.sendStateMsg(terminalState);
                break;
            case MainContract.CAR_STATE_HEAVY:
                Location_TerminalState terminalState1 = new Location_TerminalState();
                if (mState == MainContract.CAR_STATE_EMPTY) {
                    terminalState1.setEmptyToHeavy(true);
                }
                terminalState1.setHeavy(true);
                terminalState1.setRunning(true);
                clientSendManage.sendStateMsg(terminalState1);
                break;
            case MainContract.CAR_STATE_PAUSE:
                Location_TerminalState terminalState2 = new Location_TerminalState();
                terminalState2.setRunning(false);
                clientSendManage.sendStateMsg(terminalState2);
                break;
        }
        mainView.showCarState(state);
        this.mState = state;
    }

    @Override
    public void changeActivityView(String action, Context context) {
        try {
            Intent intent = new Intent(action);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }

    private static final String NAVIGATION_PKGNAME = "com.autonavi.amapauto";
    private static final String NAVIGATION_ACTIVITYNAME = "com.autonavi.auto.remote.fill.UsbFillActivity";

    @Override
    public void changeNavigation(Context context) {
        //高德地图车机版本 使用该包名/
        try {
            Intent launchIntent = new Intent();
            launchIntent.setComponent(
                    new ComponentName(NAVIGATION_PKGNAME,
                            NAVIGATION_ACTIVITYNAME));
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(launchIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        mContext = null;
    }


    IClientReceiveListener.ITaxiOperateStateListener mTaxiOperateStateListener = new IClientReceiveListener.ITaxiOperateStateListener() {
        @Override
        public void TaxiOperateStateStart(String time) {//进入重车（单次运营开始）
//            if (D)
//                Log.d(TAG,"------------进入重车------------"+time);
            mainView.showCarState(MainContract.CAR_STATE_HEAVY);
//            mainView.showTiming(time);
        }

        @Override
        public void TaxiOperateStateEnd(P_TaxiOperationDataReport p_taxiOperationDataReport) {//进入空车（单次运营结束）
//            if (D)
//                Log.d(TAG,"------------进入空车------------");
            mainView.showCarState(MainContract.CAR_STATE_EMPTY);
            if (p_taxiOperationDataReport != null) {
//                mainView.showPrice(p_taxiOperationDataReport.);
                mainView.showMileage(p_taxiOperationDataReport.getMileage());
                mainView.showTotal(p_taxiOperationDataReport.getTransactionIncome());
                mainView.showTiming(getTime(p_taxiOperationDataReport.getUpCarTime(),p_taxiOperationDataReport.getDownCarTime()));
            }
        }
    };

    private String getTime(String startTime, String endTime) {
        if(TextUtils.isEmpty(startTime)||TextUtils.isEmpty(endTime)){
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
            sHour = Integer.parseInt(startTime.substring(6,8));
            sMinute = Integer.parseInt(startTime.substring(8,10));
        }
        if (endTime.length() >= 4) {
            eHour = Integer.parseInt(endTime.substring(0,2));
            eMinute = Integer.parseInt(endTime.substring(2,4));
        }
        Log.d("mm","sHour:"+sHour+",sMinute:"+sMinute+",sSecond:"+sSecond);
        Log.d("mm","eHour:"+eHour+",eMinute:"+eMinute+",eSecond:"+eSecond);
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
        Log.d("mm","h:"+h+",m:"+m+",s:"+s);
        return h * 60 + m+"";
    }


}
