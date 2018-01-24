package com.uninew.net.main;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;
import com.uninew.net.JT905.util.Utils;
import com.uninew.net.audio.TtsUtil;

/**
 * Created by Administrator on 2017/10/21 0021.
 */

public class NetApplication extends Application {

    private static volatile NetApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        // 全局异常捕获
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);
        init();
    }

    public static NetApplication getInstance() {
        if (instance == null) {
            return new NetApplication();
        }
        return instance;
    }

    private void init() {
       Utils.init(this);
        SpeechUtility.createUtility(getApplicationContext(), "appid="
                + "53ed5edd");
        new TtsUtil(this.getApplicationContext()).speak("");
    }

    // finish
    public void exit() {
        // 杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
