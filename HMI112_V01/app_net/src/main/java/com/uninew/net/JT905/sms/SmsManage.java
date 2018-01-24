package com.uninew.net.JT905.sms;

import android.content.Context;
import android.util.Log;

/**
 * Created by Administrator on 2017/8/17 0017.
 */

public class SmsManage implements ISmsManage {

    private static final String TAG="SmsManage";
    private static final boolean D=true;
    private Context mContext;
    private ISmsManagerCallBack smsManagerListener;
    private static SmsManage instance;

    private SmsManage(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void setSmsManagerListener(ISmsManagerCallBack smsManagerListener){
        this.smsManagerListener = smsManagerListener;
    }

    public synchronized static SmsManage getInstance(Context mContext){
        if (instance==null){
            instance=new SmsManage(mContext);
        }
        return instance;
    }

    @Override
    public void sendSmsMsg(String phoneNumber, byte[] datas) {
        //短信发送
        if(D) Log.d(TAG,"phoneNumber="+phoneNumber);
    }


}
