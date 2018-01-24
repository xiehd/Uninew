package com.uninew.maintanence.model;

/***********************************************************************
 * Module:  UpdateModel.java
 * Author:  Administrator
 * Purpose: Defines the Class UpdateModel
 ***********************************************************************/

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.uninew.bean.AppInfo;
import com.uninew.comm.IUpdateManage;
import com.uninew.comm.UpdateManage;
import com.uninew.maintanence.presenter.IApkCheckCallBack;
import com.uninew.maintanence.presenter.IResultCallBack;
import com.uninew.maintanence.presenter.ISDCardListener;
import com.uninew.maintanence.presenter.IUpdateListener;
import com.uninew.maintanence.presenter.IUpdatePresenter;
import com.uninew.maintanence.presenter.IUpdateResultCallBack;
import com.uninew.settings.R;
import com.uninew.until.SDCardUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressLint("NewApi")
public class UpdateModel implements IUpdatePresenter, ISDCardListener, IUpdateManage.IUpdateListener {
    private static final String TAG = "UpdateModel";
    private UpDateManager mUpDateManager;
    private IUpdateListener mUpdateListener;
    private SDManager mSdManager;
    private Context context;
    private SDCardUtils mSDCardUtils;
    private UpdateManage updateManage;
    private static final String MAIN_KEY="main";
    private static final String NET_KEY="net";
    private static final String MMS_KEY="mms";
    private static final String SETTINGS_KEY="settings";
    private static final String MAINACTIVITY_ACTION = "com.uninew.car.Main.MainActivity";
    private static final String NETSERVICE_ACTION = "com.JT905.main.LinkService";
    /**
     * 当前MCU版本
     */
    private String oldMcuVersion;
    public static final String updateApkPath = "/mnt/media_rw/udisk/";

    public UpdateModel(Context mContext, IUpdateListener mUpdateListener) {
        super();
        this.mUpdateListener = mUpdateListener;
        this.context = mContext;
        mUpDateManager = new UpDateManager(mContext);
        mSdManager = new SDManager(mContext, this);
        mSDCardUtils = new SDCardUtils(mContext);
        updateManage = new UpdateManage(mContext);
        init();
    }

    private void init() {
        mUpdateListener.ShowAppVersion(mSDCardUtils.getVersionName("com.uninew.car"));
        updateManage.startUpateService();
        updateManage.registerUpdateListener(this);
    }

    @Override
    public void updateApk(String apkAbsolutePath) {
        if(apkAbsolutePath.toLowerCase().contains(MAIN_KEY)){
            updateManage.requestUpate(apkAbsolutePath,1,MAINACTIVITY_ACTION);
        }else if(apkAbsolutePath.toLowerCase().contains(NET_KEY)){
            updateManage.requestUpate(apkAbsolutePath,2,NETSERVICE_ACTION);
        }else if(apkAbsolutePath.toLowerCase().contains(MMS_KEY)){
            updateManage.requestUpate(apkAbsolutePath);
        }else if(apkAbsolutePath.toLowerCase().contains(SETTINGS_KEY)){
            updateManage.requestUpate(apkAbsolutePath);
        }
    }

    @Override
    public void updateOS(String osAbsolutePath, IResultCallBack resultCallBack) {
        mUpDateManager.updateOS(osAbsolutePath, resultCallBack);
    }

    @Override
    public void updateApkRequest(final IUpdateResultCallBack mCallBack) {
        if (!isSdCardEnable()) {
            mCallBack.updateResultCallBack(0, null);
            Log.d(TAG, "SD卡不存在...");
        } else {
            mUpDateManager.getApkUpdateFile(updateApkPath, new IApkCheckCallBack() {

                @Override
                public void apkCheckCallBack(List<AppInfo> oldAppInfos, List<AppInfo> newAppInfos) {
                    Log.e(TAG, "回调里面获取到的apk信息" + oldAppInfos.size() + "," + newAppInfos.size());
                    if (null != newAppInfos && newAppInfos.size() > 0) {
                        List<Map<String, String>> datas = new ArrayList<>();
                        Map<String, String> appMap = null;
                        List<AppInfo> tempList = new ArrayList<>();
                        for (AppInfo newApk : newAppInfos) {
                            tempList.add(newApk);
                            for (AppInfo old : oldAppInfos) {
                                // 新旧包名一致,找到升级apk文件
                                if (old.getAppPackageName().equals(newApk.getAppPackageName())) {
                                    Log.e(TAG,
                                            "找到新版本的apk文件" + newApk.getAppName() + ",\t旧版本="
                                                    + old.getOldVersionCode() + ",\t新版本="
                                                    + newApk.getNewVersionCode());
                                    appMap = new ArrayMap<>();
                                    appMap.put(DefineUpdate.Key_ApkName, newApk.getAppName());
                                    appMap.put(DefineUpdate.Key_NewApkPkg, newApk.getAppPackageName());
                                    appMap.put(DefineUpdate.Key_OldVersion, old.getOldVersionName());
                                    appMap.put(DefineUpdate.Key_NewVersion, newApk.getNewVersionName());
                                    appMap.put(DefineUpdate.key_oldVersionCode, String.valueOf(old.getOldVersionCode()));
                                    appMap.put(DefineUpdate.key_newVersionCode, String.valueOf(newApk.getNewVersionCode()));
                                    appMap.put(DefineUpdate.Key_FileAbsolutePath, newApk.getApkPath());
                                    datas.add(appMap);
                                    tempList.remove(newApk);
                                }
                            }
                        }
                        for (AppInfo temp : tempList) {
                            appMap = new ArrayMap<>();
                            appMap.put(DefineUpdate.Key_ApkName, temp.getAppName());
                            appMap.put(DefineUpdate.Key_NewApkPkg, temp.getAppPackageName());
                            appMap.put(DefineUpdate.Key_OldVersion, context.getResources().getString(R.string.noCurrentVersionName));
                            appMap.put(DefineUpdate.Key_NewVersion, temp.getNewVersionName());
                            appMap.put(DefineUpdate.key_oldVersionCode, "0");
                            appMap.put(DefineUpdate.key_newVersionCode, String.valueOf(temp.getNewVersionCode()));
                            appMap.put(DefineUpdate.Key_FileAbsolutePath, temp.getApkPath());
                            datas.add(appMap);
                        }
                        mCallBack.updateResultCallBack(2, datas);
                    } else {
                        // 没有升级文件
                        mCallBack.updateResultCallBack(1, null);
                    }

                }
            });

        }

    }

    @Override
    public void updateOsRequest(IUpdateResultCallBack mCallBack) {
        if (!isSdCardEnable()) {
            mCallBack.updateResultCallBack(0, null);
        } else {
            // 获取旧版本号
            String oldVersion = SystemProperties.get("ro.build.id", "B01_V0.80");
            Log.e(TAG, "OS旧版本" + oldVersion);
            // 获得SD卡的升级文件压缩包
            List<File> osFiles = mUpDateManager.findOsUpdateFile();
            if (null != osFiles && osFiles.size() > 0) {
                List<Map<String, String>> datas = new ArrayList<>();
                for (File f : osFiles) {
                    String name = f.getName();
                    String path = f.getAbsolutePath();
                    String newVersion = mUpDateManager.findOsUpdateFileVersion(path);
                    String[] temp = newVersion.split("_");
                    Log.e(TAG, "OS新版本" + newVersion);
                    Map<String, String> map = new ArrayMap<>();
                    if (!TextUtils.isEmpty(newVersion)) {
                        map.put(DefineUpdate.Key_ApkName, temp[0]);
                        map.put(DefineUpdate.Key_OldVersion, oldVersion.split("_")[1]);
                        map.put(DefineUpdate.Key_NewVersion, temp[1]);
                        map.put(DefineUpdate.Key_FileAbsolutePath, path);
                        datas.add(map);
                    }
                }
                mCallBack.updateResultCallBack(2, datas);
            } else {
                mCallBack.updateResultCallBack(1, null);
            }

        }
    }

    @Override
    public void updateMcuRequest(IUpdateResultCallBack mCallBack) {
        if (!isSdCardEnable()) {
            // sd卡不存在
            mCallBack.updateResultCallBack(0, null);
        } else {
            // MCU更新后马上打开设置页面,版本号查询不到
            if (TextUtils.isEmpty(oldMcuVersion)) {
                mCallBack.updateResultCallBack(1, null);
            } else {
                List<File> mcuFiles = mUpDateManager.findMCUUpdateFile();
                if (null != mcuFiles && mcuFiles.size() > 0) {
                    Log.e(TAG, "当前的mcu版本号" + oldMcuVersion);
                    // 查询当前mcu版本信息
                    List<Map<String, String>> datas = new ArrayList<>();
                    // 遍历sd卡所有的mcu升级文件,提取文件的版本号和路径
                    for (File file : mcuFiles) {
                        Map<String, String> map = new ArrayMap<>();
                        String[] oldVersions = oldMcuVersion.split("_");
                        map.put(DefineUpdate.Key_ApkName, oldVersions[0]);
                        map.put(DefineUpdate.Key_OldVersion, oldVersions[1]);
                        // 获取文件名
                        String fileName = file.getName();
                        // 获取新版本号
                        String[] newVersions = fileName.split("_");
                        map.put(DefineUpdate.Key_NewVersion, newVersions[1]);
                        map.put(DefineUpdate.Key_FileAbsolutePath, file.getAbsolutePath());
                        datas.add(map);
                    }
                    mCallBack.updateResultCallBack(2, datas);
                } else {
                    mCallBack.updateResultCallBack(1, null);
                }
            }

        }

    }

    @Override
    public void updateMcu(String mcuAbsolutePath, IResultCallBack resultCallBack) {
    }

    @Override
    public void registerListener() {
        IntentFilter apkFilter = new IntentFilter(DefineUpdate.ApkLinkUpdateResponse);
        //apkFilter.addAction(DefineMMSAction.MCULinkVersionResponse);
        context.registerReceiver(apkInstallReceiver, apkFilter);
        mSdManager.registerListener();
    }

    @Override
    public void unRegisterListener() {
        context.unregisterReceiver(apkInstallReceiver);
        mSdManager.unRegisterListener();
        updateManage.unRegisterUpdateListener();
    }

    @Override
    public void SDChange(boolean isEnable) {
        mUpdateListener.setSDChange(isEnable);
    }

    @Override
    public boolean isSdCardEnable() {
        return mSdManager.isSdcardEnable();

    }

    /**
     * 监听apk升级结果的广播
     */
    private BroadcastReceiver apkInstallReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            if (action.equals(DefineUpdate.ApkLinkUpdateResponse)) {
                // 返回1升级成功
                int result = bundle.getInt(DefineUpdate.KEY_APKINSTALL_RESULT);
                Log.e(TAG, "收到的升级结果" + result);
                if (result == 1) {
                    mUpdateListener.onApkInsatll(true);
                } else {
                    mUpdateListener.onApkInsatll(false);
                }
            }
        }

    };

    @Override
    public void onUpdateState(int state, String apk, boolean openState, boolean setLauncherState) {
        if(TextUtils.isEmpty(apk)){
            return;
        }
        if(mUpdateListener != null)
        mUpdateListener.onUpdateState(state,apk,openState,setLauncherState);
    }
}