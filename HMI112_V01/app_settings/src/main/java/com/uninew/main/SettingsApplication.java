package com.uninew.main;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.uninew.export.Utils;
import com.uninew.maintanence.view.IDeviceStateListener;
import com.uninew.net.JT905.common.DeviceState;
import com.uninew.until.CrashHandler;

/**
 * Created by Administrator on 2017/10/18 0018.
 */
public class SettingsApplication extends Application {
    private static final String TAG = "SettingsApplication";
    private static DeviceState mDeviceState;
    private static IDeviceStateListener mIDeviceStateListener;
    private static SettingsApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        // 全局异常捕获
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getInstance());
        init();
    }

    public static SettingsApplication getInstance() {
        if (instance == null) {
            return new SettingsApplication();
        }
        return instance;
    }


    private void init() {
        Utils.init(getInstance());
        registerBroadCast();

    }

    private DeviceReceiver mBroadcastReceiver;

    private void registerBroadCast() {

        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction("Action_Device_StateDateResponse");//获得设备更新对象
        mBroadcastReceiver = new DeviceReceiver();
        this.registerReceiver(mBroadcastReceiver, iFilter);
    }


    class DeviceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "Action_Device_StateDateResponse":
                    Log.i(TAG, "-------获得设备状态-----");
                    mDeviceState = (DeviceState) intent.getSerializableExtra("device_obj");
                    if (mIDeviceStateListener != null) {
                        mIDeviceStateListener.setDeviceState(mDeviceState);
                    }
                    break;
            }
        }
    }


    public void setDeviceListener(IDeviceStateListener mIDeviceStateListener) {
        this.mIDeviceStateListener = mIDeviceStateListener;
    }
    public void unDeviceState(){
        if (mIDeviceStateListener != null){
            mIDeviceStateListener = null;
        }
    }



    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.d(TAG, "onTerminate");
        super.onTerminate();
        if (mBroadcastReceiver != null) {
            this.unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
    }

    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        Log.d(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行（回收内存）
        Log.d(TAG, "onTrimMemory");// HOME键退出应用程序、长按MENU键，打开Recent TASK都会执行
        super.onTrimMemory(level);
    }

    public void exit() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
