package com.uninew.param;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.uninew.car.db.paramSet.ParamSetKey;
import com.uninew.car.db.paramSet.ParamSetLocalDataSource;
import com.uninew.car.db.paramSet.ParamSetLocalSource;
import com.uninew.car.db.paramSet.ParamSetting;
import com.uninew.settings.R;

import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Administrator on 2017/10/16 0016.
 */

public class ParamPresenter implements ParamContrat.Presenter, ParamSetLocalSource.ChangeParamSetListener, ParamSetLocalSource.LoadParamSettesCallBack {

    private ParamContrat.View mView;
    private Context mContext;
    private ParamSetLocalSource paramSetLocalSource;

    public ParamPresenter(ParamContrat.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        paramSetLocalSource = ParamSetLocalDataSource.getInstance(mContext);
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        paramSetLocalSource.registerNotify(this);
        paramSetLocalSource.getAllDBDatas(this);
    }

    @Override
    public void stop() {
        paramSetLocalSource.unregisterNotify();
    }

    @Override
    public void onChangePlatsourMessage(@NonNull ParamSetting paramSetting) {
        showParamSet(paramSetting);
    }

    private void showParamSet(ParamSetting paramSetting) {
        int key = paramSetting.getKey();
        switch (key) {
            case ParamSetKey.HeartBeat:
                mView.showHeartbeat(paramSetting.getValue());
                break;
            case ParamSetKey.TcpResponseTimeOut:
                mView.showTcpTimeOut(paramSetting.getValue());
                break;
            case ParamSetKey.TcpResendTime:
                mView.showTcpTimes(paramSetting.getValue());
                break;
            case ParamSetKey.MainIpOrDomain:
                String mainIp = paramSetting.getValue();
                if (!TextUtils.isEmpty(mainIp)) {
                    mView.showMainIp(mainIp);
                }
                break;
            case ParamSetKey.SpareIpOrDomain:
                String spareIp = paramSetting.getValue();
                if (!TextUtils.isEmpty(spareIp)) {
                    mView.showSpareIp(spareIp);
                }
                break;
            case ParamSetKey.MainTcpPort:
                String mainPort = paramSetting.getValue();
                if (!TextUtils.isEmpty(mainPort)) {
                    mView.showMainPort(mainPort);
                }
                break;
            case ParamSetKey.SpareTcpPort:
                String sparePort = paramSetting.getValue();
                if (!TextUtils.isEmpty(sparePort)) {
                    mView.showSparePort(sparePort);
                }
                break;
            case ParamSetKey.LocationReportStratege:
                int stratege = stringToInt(paramSetting.getValue());
                if (stratege > -1) {
                    switch (stratege) {
                        case 0:
                            mView.showReportStrategy(mContext.getString(R.string.ReportStratege_Time));
                            break;
                        case 1:
                            mView.showReportStrategy(mContext.getString(R.string.ReportStratege_Distance));
                            break;
                        case 2:
                            mView.showReportStrategy(mContext.getString(R.string.ReportStratege_TimeAndDistance));
                            break;
                    }
                }
                break;
            case ParamSetKey.LocationReportPlan:
                int plan = stringToInt(paramSetting.getValue());
                if (plan > -1) {
                    switch (plan) {
                        case 0:
                            mView.showReportPlan(mContext.getString(R.string.ReportPlan_ByACC));
                            break;
                        case 1:
                            mView.showReportPlan(mContext.getString(R.string.ReportPlan_ByEmpty));
                            break;
                        case 2:
                            mView.showReportPlan(mContext.getString(R.string.ReportPlan_ByLoginAndAcc));
                            break;
                        case 3:
                            mView.showReportPlan(mContext.getString(R.string.ReportPlan_ByLoginAndEmpty));
                            break;
                    }
                }
                break;
            case ParamSetKey.UnLoginReportIntervalTime:
                String unloginTime = paramSetting.getValue();
                if (!TextUtils.isEmpty(unloginTime)) {
                    mView.showUnLoginReportIntervalTime(unloginTime);
                }
                break;
            case ParamSetKey.AccOffReportIntervalTime:
                String accOffTime = paramSetting.getValue();
                if (!TextUtils.isEmpty(accOffTime)) {
                    mView.showAccOffReportIntervalTime(accOffTime);
                }
                break;
            case ParamSetKey.AccOnfReportIntervalTime:
                String accOnTime = paramSetting.getValue();
                if (!TextUtils.isEmpty(accOnTime)) {
                    mView.showAccOnReportIntervalTime(accOnTime);
                }
                break;
            case ParamSetKey.EmptyReportIntervalTime:
                String emptyTime = paramSetting.getValue();
                if (!TextUtils.isEmpty(emptyTime)) {
                    mView.showEmptyReportIntervalTime(emptyTime);
                }
                break;
            case ParamSetKey.NonEmptyReportIntervalTime:
                String noEmptyTime = paramSetting.getValue();
                if (!TextUtils.isEmpty(noEmptyTime)) {
                    mView.showNoEmptyReportIntervalTime(noEmptyTime);
                }
                break;
            case ParamSetKey.SleepReportIntervalTime:
                String sleepTime = paramSetting.getValue();
                if (!TextUtils.isEmpty(sleepTime)) {
                    mView.showSleepReportIntervalTime(sleepTime);
                }
                break;
            case ParamSetKey.EmergencyReportIntervalTime:
                String emergencyTime = paramSetting.getValue();
                if (!TextUtils.isEmpty(emergencyTime)) {
                    mView.showEmergencyReportIntervalTime(emergencyTime);
                }
                break;
            case ParamSetKey.UnLoginReportIntervalDistance:
                String unloginDistance = paramSetting.getValue();
                if (!TextUtils.isEmpty(unloginDistance)) {
                    mView.showUnLoginReportIntervalDistance(unloginDistance);
                }
                break;
            case ParamSetKey.AccOffReportIntervalDistance:
                String accOffDistance = paramSetting.getValue();
                if (!TextUtils.isEmpty(accOffDistance)) {
                    mView.showAccOffReportIntervalDistance(accOffDistance);
                }
                break;
            case ParamSetKey.AccOnfReportIntervalDistance:
                String accOnDistance = paramSetting.getValue();
                if (!TextUtils.isEmpty(accOnDistance)) {
                    mView.showAccOnReportIntervalDistance(accOnDistance);
                }
                break;
            case ParamSetKey.EmptyReportIntervalDistance:
                String emptyDistance = paramSetting.getValue();
                if (!TextUtils.isEmpty(emptyDistance)) {
                    mView.showEmptyReportIntervalDistance(emptyDistance);
                }
                break;
            case ParamSetKey.NonEmptyReportIntervalDistance:
                String noEmptyDistance = paramSetting.getValue();
                if (!TextUtils.isEmpty(noEmptyDistance)) {
                    mView.showNoEmptyReportIntervalDistance(noEmptyDistance);
                }
                break;
            case ParamSetKey.SleepReportIntervalDistance:
                String sleepDistance = paramSetting.getValue();
                if (!TextUtils.isEmpty(sleepDistance)) {
                    mView.showSleepReportIntervalDistance(sleepDistance);
                }
                break;
            case ParamSetKey.EmergencyReportIntervalDistance:
                String emergencyDistance = paramSetting.getValue();
                if (TextUtils.isEmpty(emergencyDistance)) {
                    mView.showEmergencyReportIntervalDistance(emergencyDistance);
                }
                break;
        }
    }

    @Override
    public void onDBBaseDataLoaded(List<ParamSetting> buffers) {
        for (ParamSetting paramSetting : buffers) {
            showParamSet(paramSetting);
        }
    }

    @Override
    public void onDataNotAailable() {

    }

    private final int stringToInt(String s) {
        int v = -1;
        try {
            v = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return v;
    }
}
