package com.uninew.net.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class NetBroadCastTool {

    private boolean D = false;
    private static final String TAG = "NetBroadCastTool";
    private Context mContext;

    public NetBroadCastTool(Context mContext) {
        super();
        this.mContext = mContext;
    }

    /**
     * 发送状态
     * 定位状态：0x00：未定位，0x01：已定位
     *
     * 故障状态：0x00：正常，0x01：异常
     * 连接状态：0x10：1路断开，0x11：1路连接
     *           0x20：2路断开，0x21：2路连接
     * 报警状态：0x00：无告警，0x01：有告警
     */
    public void sendState(String action,int state) {
        if (D)
            Log.d(TAG, "sendState");
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("status",state);
        mContext.sendBroadcast(intent);
    }

    /**
     * 发送设备状态
     * @param type
     * @param state
     */
    public void sendDeviceState(int type,int state){
        Intent intent = new Intent();
        intent.setAction("receiver_device_state");
        intent.putExtra("device_state",state);
        intent.putExtra("device_type",type);
        mContext.sendBroadcast(intent);

    }



}
