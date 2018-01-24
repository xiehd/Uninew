package com.uninew.mms.interactive;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class MMSBroadCastTool {

    private boolean D = true;
    private static final String TAG = "BroadCastTool";
    private Context mContext;

    public MMSBroadCastTool(Context mContext) {
        super();
        this.mContext = mContext;
    }

    /**
     * MCU版本查询
     */
    public void sendMCUVersionRequest() {
        if (D)
            Log.d(TAG, "sendMCUVersionRequest");
        Intent intent = new Intent();
        intent.setAction(DefineMMSAction.MCULinkVersionRequest);
        mContext.sendBroadcast(intent);
    }

    /**
     * MCU版本查询应答
     */
    public void sendMCUVersionResponse(String version) {
        if (D)
            Log.d(TAG, "sendMCUVersionResponse");
        Intent intent = new Intent();
        intent.setAction(DefineMMSAction.MCULinkVersionResponse);
        intent.putExtra(DefineMMSAction.MCUVersion.KEY_VERSION, version);
        mContext.sendBroadcast(intent);
    }

    /**
     * OS升级通知
     */
    public void sendOSUpdateNotify() {
        if (D)
            Log.d(TAG, "sendOSUpdateNotify");
        Intent intent = new Intent();
        intent.setAction(DefineMMSAction.MCULinkOSUpdateNotify);
        mContext.sendBroadcast(intent);
    }

    /**
     * ACC状态通知
     *
     * @param state 0-ACC_OFF, 1-ACC_ON
     */
    public void sendAccStateNotify(int state) {
        if (D)
            Log.d(TAG, "sendAccStateNotify");
        Intent intent = new Intent();
        intent.setAction(DefineMMSAction.MCULinkAccState);
        intent.putExtra(DefineMMSAction.AccState.KEY_STATE, state);
        mContext.sendBroadcast(intent);
    }

    /**
     * 参数设置
     *
     */
    public void SetParamTypeSet(int typeAction,int value) {
        if (D)
            Log.d(TAG, "SetParamTypeSet");
        Intent intent = new Intent();
        intent.setAction(DefineMMSAction.MCULinkParamTypeSet);
        intent.putExtra(DefineMMSAction.ParamType.PARAM_TYPE, typeAction);
        intent.putExtra(DefineMMSAction.ParamType.PARAM_VALUE, value);
        mContext.sendBroadcast(intent);
    }


    /**
     * 发送按键值
     *
     * @param keyCode
     */
    public void sendKeyBroadCast(int keyCode) {
        Intent intent = new Intent();
        intent.setAction(DefineMMSAction.MCULinkKeys);
        intent.putExtra("key", keyCode);
        int result = (keyCode >> 7) & 1;
        intent.putExtra("state", (result == 1) ? 1 : 0);// 默认值
        mContext.sendBroadcast(intent);
    }




    /**
     * 232发送数据
     * @return
     */
    public void getMcuTestDate(byte[] body) {
        Intent intent = new Intent();
        intent.setAction("com.uninew.mcutest.send232");
        intent.putExtra("send_mcu_date", body);
        mContext.sendBroadcast(intent);
    }

}
