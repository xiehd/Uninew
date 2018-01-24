package com.uninew.net.main;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.uninew.car.db.paramSet.ParamSetKey;
import com.uninew.car.db.paramSet.ParamSetLocalDataSource;
import com.uninew.car.db.paramSet.ParamSetLocalSource;
import com.uninew.car.db.paramSet.ParamSetting;
import com.uninew.car.db.settings.BaseLocalDataSource;
import com.uninew.car.db.settings.PlatformLocalDataSource;
import com.uninew.car.db.settings.PlatformLocalSource;
import com.uninew.car.db.settings.PlatformSettings;
import com.uninew.car.db.settings.SettingsDefaultValue;
import com.uninew.car.db.settings.SpeedSettingsLocalSource;
import com.uninew.net.JT905.common.ProtocolTool;

/**
 * Created by Administrator on 2017/10/14 0014.
 */

public class ParamSetHandle implements ParamSetLocalSource.ChangeParamSetListener, PlatformLocalSource.ChangePlatformListener {

    private static volatile ParamSetHandle INSTANCE;

    private LinkService mService;
    private PlatformLocalSource platformLocalSource;
    private ParamSetLocalSource paramSetLocalSource;
    private BaseLocalDataSource speedSettingsLocalSource;

    private ParamSetHandle(LinkService service) {
        this.mService = service;
        paramSetLocalSource = ParamSetLocalDataSource.getInstance(mService.getApplicationContext());
        paramSetLocalSource.registerNotify(this);
        platformLocalSource = PlatformLocalDataSource.getInstance(mService.getApplicationContext());
        speedSettingsLocalSource = BaseLocalDataSource.getInstance(mService.getApplicationContext());
    }

    public static ParamSetHandle getInstance(LinkService service) {
        if (INSTANCE != null) {

        } else {
            synchronized (ParamSetHandle.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ParamSetHandle(service);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void onChangePlatsourMessage(@NonNull ParamSetting paramSetting) {
        int key = paramSetting.getKey();
        switch (key) {
            case ParamSetKey.HeartBeat:
                int time = ProtocolTool.stringToInt(paramSetting.getValue());
                if (time > 0) {
                    mService.platformLinkManage.setHeartbeatTime(time);
                }
                break;
            case ParamSetKey.TcpResponseTimeOut:
                int timeout = ProtocolTool.stringToInt(paramSetting.getValue());
                if (timeout > 0) {
                    mService.platformLinkManage.setTcpResponseTimeOut(timeout);
                }
                break;
            case ParamSetKey.TcpResendTime:
                int times = ProtocolTool.stringToInt(paramSetting.getValue());
                if (times > 0) {
                    mService.platformLinkManage.setTcpResendTime(times);
                }
                break;
            case ParamSetKey.MainIpOrDomain:
                String mainIp = paramSetting.getValue();
                if (!TextUtils.isEmpty(mainIp)) {
                    platformLocalSource.registerNotify(this);
                    PlatformSettings platformSettings = new PlatformSettings();
                    platformSettings.setMainIp(mainIp);
                    platformSettings.setTvpId(SettingsDefaultValue.PlatformDefaultValue.SERVICE_PORT_1_ID);
                    platformLocalSource.setService(platformSettings);
                }
                break;
            case ParamSetKey.SpareIpOrDomain:
                String spareIp = paramSetting.getValue();
                if (!TextUtils.isEmpty(spareIp)) {
                    platformLocalSource.registerNotify(this);
                    PlatformSettings platformSettings = new PlatformSettings();
                    platformSettings.setSpareIp(spareIp);
                    platformSettings.setTvpId(SettingsDefaultValue.PlatformDefaultValue.SERVICE_PORT_1_ID);
                    platformLocalSource.setService(platformSettings);
                }
                break;
            case ParamSetKey.MainTcpPort:
                int mainPort = ProtocolTool.stringToInt(paramSetting.getValue());
                if (mainPort > 0) {
                    platformLocalSource.registerNotify(this);
                    PlatformSettings platformSettings = new PlatformSettings();
                    platformSettings.setMainPort(mainPort);
                    platformSettings.setTvpId(SettingsDefaultValue.PlatformDefaultValue.SERVICE_PORT_1_ID);
                    platformLocalSource.setService(platformSettings);
                }
                break;
            case ParamSetKey.SpareTcpPort:
                int sparePort = ProtocolTool.stringToInt(paramSetting.getValue());
                if (sparePort > 0) {
                    platformLocalSource.registerNotify(this);
                    PlatformSettings platformSettings = new PlatformSettings();
                    platformSettings.setMainPort(sparePort);
                    platformSettings.setTvpId(SettingsDefaultValue.PlatformDefaultValue.SERVICE_PORT_1_ID);
                    platformLocalSource.setService(platformSettings);
                }
                break;
            case ParamSetKey.LocationReportStratege:
                int stratege = ProtocolTool.stringToInt(paramSetting.getValue());
                if (stratege > -1) {
                    mService.mLocationReportHandle.setReportStrategy(stratege);
                    mService.mLocationReportHandle.setReportParams();
                }
                break;
            case ParamSetKey.LocationReportPlan:
                int plan = ProtocolTool.stringToInt(paramSetting.getValue());
                if (plan > -1) {
                    mService.mLocationReportHandle.setReportPlan(plan);
                    mService.mLocationReportHandle.setReportParams();
                }
                break;
            case ParamSetKey.UnLoginReportIntervalTime:
                int unloginTime = ProtocolTool.stringToInt(paramSetting.getValue());
                if (unloginTime > -1) {
                    mService.mLocationReportHandle.setUnLoginReportIntervalTime(unloginTime);
                    mService.mLocationReportHandle.setReportParams();
                }
                break;
            case ParamSetKey.AccOffReportIntervalTime:
                int accOffTime = ProtocolTool.stringToInt(paramSetting.getValue());
                if (accOffTime > -1) {
                    mService.mLocationReportHandle.setAccOffReportIntervalTime(accOffTime);
                    mService.mLocationReportHandle.setReportParams();
                }
                break;
            case ParamSetKey.AccOnfReportIntervalTime:
                int accOnTime = ProtocolTool.stringToInt(paramSetting.getValue());
                if (accOnTime > -1) {
                    mService.mLocationReportHandle.setAccOnReportIntervalTime(accOnTime);
                    mService.mLocationReportHandle.setReportParams();
                }
                break;
            case ParamSetKey.EmptyReportIntervalTime:
                int emptyTime = ProtocolTool.stringToInt(paramSetting.getValue());
                if (emptyTime > -1) {
                    mService.mLocationReportHandle.setEmergencyReportIntervalTime(emptyTime);
                    mService.mLocationReportHandle.setReportParams();
                }
                break;
            case ParamSetKey.NonEmptyReportIntervalTime:
                int noEmptyTime = ProtocolTool.stringToInt(paramSetting.getValue());
                if (noEmptyTime > -1) {
                    mService.mLocationReportHandle.setNoEmptyReportIntervalTime(noEmptyTime);
                    mService.mLocationReportHandle.setReportParams();
                }
                break;
            case ParamSetKey.SleepReportIntervalTime:
                int sleepTime = ProtocolTool.stringToInt(paramSetting.getValue());
                if (sleepTime > -1) {
                    mService.mLocationReportHandle.setSleepReportIntervalTime(sleepTime);
                    mService.mLocationReportHandle.setReportParams();
                }
                break;
            case ParamSetKey.EmergencyReportIntervalTime:
                int emergencyTime = ProtocolTool.stringToInt(paramSetting.getValue());
                if (emergencyTime > -1) {
                    mService.mLocationReportHandle.setEmergencyReportIntervalTime(emergencyTime);
                    mService.mLocationReportHandle.setReportParams();
                }
                break;
            case ParamSetKey.UnLoginReportIntervalDistance:
                int unloginDistance = ProtocolTool.stringToInt(paramSetting.getValue());
                if (unloginDistance > -1) {
                    mService.mLocationReportHandle.setUnLoginReportIntervalDistance(unloginDistance);
                    mService.mLocationReportHandle.setReportParams();
                }
                break;
            case ParamSetKey.AccOffReportIntervalDistance:
                int accOffDistance = ProtocolTool.stringToInt(paramSetting.getValue());
                if (accOffDistance > -1) {
                    mService.mLocationReportHandle.setAccOffReportIntervalDistance(accOffDistance);
                    mService.mLocationReportHandle.setReportParams();
                }
                break;
            case ParamSetKey.AccOnfReportIntervalDistance:
                int accOnDistance = ProtocolTool.stringToInt(paramSetting.getValue());
                if (accOnDistance > -1) {
                    mService.mLocationReportHandle.setAccOnReportIntervalDistance(accOnDistance);
                    mService.mLocationReportHandle.setReportParams();
                }
                break;
            case ParamSetKey.EmptyReportIntervalDistance:
                int emptyDistance = ProtocolTool.stringToInt(paramSetting.getValue());
                if (emptyDistance > -1) {
                    mService.mLocationReportHandle.setEmergencyReportIntervalDistance(emptyDistance);
                    mService.mLocationReportHandle.setReportParams();
                }
                break;
            case ParamSetKey.NonEmptyReportIntervalDistance:
                int noEmptyDistance = ProtocolTool.stringToInt(paramSetting.getValue());
                if (noEmptyDistance > -1) {
                    mService.mLocationReportHandle.setNoEmptyReportIntervalDistance(noEmptyDistance);
                    mService.mLocationReportHandle.setReportParams();
                }
                break;
            case ParamSetKey.SleepReportIntervalDistance:
                int sleepDistance = ProtocolTool.stringToInt(paramSetting.getValue());
                if (sleepDistance > -1) {
                    mService.mLocationReportHandle.setSleepReportIntervalDistance(sleepDistance);
                    mService.mLocationReportHandle.setReportParams();
                }
                break;
            case ParamSetKey.EmergencyReportIntervalDistance:
                int emergencyDistance = ProtocolTool.stringToInt(paramSetting.getValue());
                if (emergencyDistance > -1) {
                    mService.mLocationReportHandle.setEmergencyReportIntervalDistance(emergencyDistance);
                    mService.mLocationReportHandle.setReportParams();
                }
                break;
            case ParamSetKey.MaximumSpeed:
                int speed = ProtocolTool.stringToInt(paramSetting.getValue());
                if (speed > -1) {
                    speedSettingsLocalSource.setAlarm_speed_max(speed);
                }
                break;
            case ParamSetKey.SpeedingDuration:
                int speed_time = ProtocolTool.stringToInt(paramSetting.getValue());
                if (speed_time > -1) {
                    speedSettingsLocalSource.setAlarm_speed_time(speed_time);
                }
                break;
        }
    }

    @Override
    public void onChangePlatsourMessage(@NonNull PlatformSettings platformSettings) {
        mService.platformLinkManage.setTcpParams(platformSettings.getTvpId(), platformSettings.getMainIp(),
                platformSettings.getMainPort(), platformSettings.getSpareIp(), platformSettings.getSparePort());
        mService.platformLinkManage.createSocket(platformSettings.getTvpId());
        platformLocalSource.unregisterNotify();
    }

    public void unregisterNotify() {
        paramSetLocalSource.unregisterNotify();
    }

    public void saveParamSetting(ParamSetting... paramSettings) {
        paramSetLocalSource.saveDBData(paramSettings);
    }
}
