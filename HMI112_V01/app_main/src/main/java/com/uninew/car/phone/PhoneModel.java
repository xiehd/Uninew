package com.uninew.car.phone;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.uninew.car.MainApplication;
import com.uninew.car.R;
import com.uninew.car.db.contacts.Contact;
import com.uninew.car.db.contacts.ContactLocalDataSource;
import com.uninew.car.db.contacts.ContactLocalSource;
import com.uninew.car.db.dialler.Dialer;
import com.uninew.car.db.dialler.DialerKey;
import com.uninew.car.db.dialler.DialerLocalDataSource;
import com.uninew.car.db.dialler.DialerLocalSource;
import com.uninew.car.db.paramSet.ParamSetKey;
import com.uninew.car.db.paramSet.ParamSetLocalDataSource;
import com.uninew.car.db.paramSet.ParamSetLocalSource;
import com.uninew.car.db.paramSet.ParamSetting;
import com.uninew.car.until.PhoneUtils;
import com.uninew.net.JT905.bean.P_PhoneBookSet;
import com.uninew.net.JT905.comm.client.IClientReceiveListener;
import com.uninew.net.JT905.comm.client.IClientReceiveManage;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tools.TimeTool;

/**
 * Created by Administrator on 2017/9/26 0026.
 */

public class PhoneModel implements IClientReceiveListener.IPhoneListener, TimerModel.TimerCallBack
        , ContactLocalSource.GetContactCallBack {

    private static final String TAG = "PhoneModel";
    private static final boolean D = true;

    private volatile static PhoneModel INSTANCE;
    private Context mContext;
    private ITelephony mTelephony;
    private TelephonyManager telMgr;
    private IClientReceiveManage receiveManage;
    private static final int TIMER_WHAT = 0x01;
    private static final int ENDCALL_WHAT = 0x02;
    private static final int CALL_WHAT = 0x03;
    private static final int CALLING_WHAT = 0x04;
    private int limitTime = 20 * 60;
    private TimerModel timerModel;
    private MyPhoneStateListener mPhoneState;
    private boolean isCalling = false;
    private DialerLocalSource dialerLocalSource;
    private ContactLocalSource mDBContact;
    private String mPhone = "";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case TIMER_WHAT:
                    int time = msg.arg1;
                    if (D) Log.d(TAG, "time:" + time + "s");
                    if (limitTime < time) {
                        mHandler.sendEmptyMessage(ENDCALL_WHAT);
                    }
                    break;
                case ENDCALL_WHAT:
                    timerModel.stopClock();
//                    mView.endCall();
                    endCall();
                    break;
                case CALL_WHAT:
                    startCallTimer();
                    break;
                case CALLING_WHAT:
                    stopCallTimer();
                    if (!TextUtils.isEmpty(mPhone)) {
                        mDBContact.getContactByPhone(mPhone, PhoneModel.this);
                    }
                    break;
            }
        }
    };

    private Timer callTimer;

    private void startCallTimer() {
        if (callTimer == null) {
            callTimer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    callState = DialerKey.DialerStateKey.MISSEDE_CALLS;
                    mHandler.sendEmptyMessage(CALLING_WHAT);
                }
            };
            callTimer.schedule(timerTask, 60 * 1000);
        }
    }

    private void stopCallTimer() {
        if (callTimer != null) {
            callTimer.cancel();
            callTimer = null;
        }
    }

    private PhoneModel(Context context) {
        this.mContext = context;
        telMgr = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        mTelephony = PhoneUtils.getITelephony(telMgr);
        timerModel = TimerModel.getInstance();
        mPhoneState = new MyPhoneStateListener();
        dialerLocalSource = DialerLocalDataSource.getInstance(context);
        mDBContact = ContactLocalDataSource.getInstance(context);
        telMgr.listen(mPhoneState, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public static PhoneModel getInstance(Context context) {
        if (INSTANCE != null) {

        } else {
            synchronized (PhoneModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PhoneModel(context);
                }
            }
        }
        return INSTANCE;
    }

    public void setReceiveManage(IClientReceiveManage receiveManage) {
        this.receiveManage = receiveManage;
    }

    public void registerPhoneListener() {
        if (receiveManage != null) {
            receiveManage.registerPhoneListener(this);
        }
    }

    public void unRegisterPhoneListener() {
        if (receiveManage != null) {
            receiveManage.unRegisterPhoneListener();
        }
    }

    /*
     * 挂断电话
     */
    public void endCall() {
//        AudioManager mAudioManager = (AudioManager) mContext
//                .getSystemService(Context.AUDIO_SERVICE);
//        // 先静音处理
//        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        timerModel.stopClock();
        if (mTelephony != null) {
            try {
                mTelephony.endCall();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        // 再恢复正常铃声
//        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }
    public static final String STOP_CAll_ACTION  = "com.uninew.stopCall";
    public static void endCall(Context context){
        Intent intent = new Intent(STOP_CAll_ACTION);
        context.sendBroadcast(intent);
    }

    /**
     * 自动接听电话
     */
    public void acceptCall() {
        if (mTelephony != null) {
            try {
                mTelephony.answerRingingCall();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    public static void callNumber(String number,Context context) {
        if (PhoneUtils.isPhoneNumberValid(number)) {
            call(number,context);
//            mPhone = number;
//            callState = DialerKey.DialerStateKey.DIALOUT_CALLS;
        } else {
            Log.e(TAG, "输入的不是电话号码");
        }
    }

    private static final void call(String number,Context context) {
//        Class<TelephonyManager> c = TelephonyManager.class;
//        Method getITelephonyMethod = null;
//        try {
//            getITelephonyMethod = c.getDeclaredMethod("getITelephony",
//                    (Class[]) null);
//            getITelephonyMethod.setAccessible(true);
//            TelephonyManager tManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//            Object iTelephony = (Object) getITelephonyMethod.invoke(tManager, (Object[]) null);
//            Method dial = iTelephony.getClass().getDeclaredMethod("call", String.class);
//            dial.invoke(iTelephony, number);
////            tManager.listen(mTelephonyListener, PhoneStateListener.LISTEN_CALL_STATE);
//        } catch (IllegalArgumentException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (SecurityException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        Log.d(TAG, "拨号，number：" + number);
//        if (mTelephony != null) {
//            try {
//                mTelephony.call(MainApplication.getInstance().getPackageName(), number);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(intent);
    }


    public void startTimer() {
        ParamSetLocalDataSource.getInstance(mContext).getParamSettesByKey(new ParamSetLocalSource.LoadParamSettesCallBack() {
            @Override
            public void onDBBaseDataLoaded(List<ParamSetting> buffers) {
                for (ParamSetting paramSetting : buffers) {
                    if (paramSetting.getKey() == ParamSetKey.MaximumCallTimePerOne) {
                        int time = strToInt(paramSetting.getValue().trim());
                        if (time > 0) {
                            limitTime = time;
                        }
                    }
                }
            }

            @Override
            public void onDataNotAailable() {

            }
        }, new int[]{
                ParamSetKey.MaximumCallTimePerOne
        });
        timerModel.startClock(this);
    }

    @Override
    public void callBack(int i, String s) {
//        Intent intent1 = new Intent(mContext, CallActivity.class);
//        intent1.putExtra(CallActivity.INTENT_CALL_NUMBER, s);
//        intent1.putExtra(CallActivity.INTENT_CALL_STATE, TelephonyManager.CALL_STATE_OFFHOOK);
//        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent1);
        callNumber(s,mContext);
    }

    @Override
    public void setPhoneBook(P_PhoneBookSet p_phoneBookSet) {

    }

    @Override
    public void onTime(int time) {
        Message msg = Message.obtain();
        msg.what = TIMER_WHAT;
        msg.arg1 = time;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onDBBaseDataLoaded(Contact contact) {
        Dialer dialer = new Dialer();
        try {
            dialer.setCallDate(TimeTool.formatDate(new Date(System.currentTimeMillis())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dialer.setContact(contact.getContact());
        dialer.setDiallerState(callState);
        dialer.setPhone(mPhone);
        dialerLocalSource.saveDBData(dialer);
        mPhone = null;
        callState = DialerKey.DialerStateKey.MISSEDE_CALLS;
    }

    @Override
    public void onDataNotAailable() {
        Dialer dialer = new Dialer();
        try {
            dialer.setCallDate(TimeTool.formatDate(new Date(System.currentTimeMillis())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dialer.setContact(mContext.getString(R.string.phone_unknown_contact));
        dialer.setDiallerState(callState);
        dialer.setPhone(mPhone);
        dialerLocalSource.saveDBData(dialer);
        mPhone = null;
        callState = DialerKey.DialerStateKey.MISSEDE_CALLS;
    }

    private int callState = DialerKey.DialerStateKey.MISSEDE_CALLS;

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            //注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
            super.onCallStateChanged(state, incomingNumber);
//            mView.showCallState(state);
            if (D) Log.d(TAG, "通话状态，state:"+state);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if (D) Log.d(TAG, "挂断");
                    isCalling = false;
//                    mHandler.sendEmptyMessage(ENDCALL_WHAT);
                    timerModel.stopClock();
                    mHandler.sendEmptyMessage(CALLING_WHAT);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (D) Log.d(TAG, "接听");
                    startTimer();
                    isCalling = true;
                    if(callState != DialerKey.DialerStateKey.DIALOUT_CALLS) {
                        callState = DialerKey.DialerStateKey.NORMAL_CALLS;
                    }
                    mHandler.sendEmptyMessage(CALLING_WHAT);
                    break;
                case TelephonyManager.CALL_STATE_RINGING://输出来电号码
                    if (D) Log.d(TAG, "响铃:来电号码" + incomingNumber);
                    if (callState != DialerKey.DialerStateKey.DIALOUT_CALLS) {
                        callState = DialerKey.DialerStateKey.MISSEDE_CALLS;
                        mPhone = incomingNumber;
                    }
                    mHandler.sendEmptyMessage(CALL_WHAT);
                    break;
            }
        }
    }

    private int strToInt(String str) {
        int i = 0;
        try {
            i = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }
}
