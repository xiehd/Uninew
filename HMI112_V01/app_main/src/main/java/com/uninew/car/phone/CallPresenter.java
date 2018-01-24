package com.uninew.car.phone;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.uninew.car.R;
import com.uninew.car.db.contacts.Contact;
import com.uninew.car.db.contacts.ContactLocalDataSource;
import com.uninew.car.db.contacts.ContactLocalSource;
import com.uninew.car.db.dialler.Dialer;
import com.uninew.car.db.dialler.DialerLocalDataSource;
import com.uninew.car.db.dialler.DialerLocalSource;
import com.uninew.car.db.paramSet.ParamSetKey;
import com.uninew.car.db.paramSet.ParamSetLocalDataSource;
import com.uninew.car.db.paramSet.ParamSetLocalSource;
import com.uninew.car.db.paramSet.ParamSetting;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import tools.TimeTool;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class CallPresenter implements CallContract.Presenter, ContactLocalSource.GetContactCallBack {

    private static final String TAG = "CallPresenter";
    private static final boolean D = true;
    private CallContract.View mView;
    private Context mContext;
//    private TimerModel timerModel;
    private PhoneModel phoneModel;
//    private MyPhoneStateListener mPhoneStateListener;
    private ContactLocalSource mDBContact;
    private DialerLocalSource mDBDialers;
    private String mPhone = "未知联系人";

    public CallPresenter(CallContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        mView.setPresenter(this);
//        timerModel = TimerModel.getInstance();
//        mPhoneStateListener = new MyPhoneStateListener();
        phoneModel = PhoneModel.getInstance(context);
        mDBContact = ContactLocalDataSource.getInstance(context);
        mDBDialers = DialerLocalDataSource.getInstance(context);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void hangUp() {
//        timerModel.stopClock();
        phoneModel.endCall();
//        if (mPhoneStateListener != null) {
//            mPhoneStateListener = null;
//        }
    }

    @Override
    public void answer() {
//        ParamSetLocalDataSource.getInstance(mContext).getParamSettesByKey(new ParamSetLocalSource.LoadParamSettesCallBack() {
//            @Override
//            public void onDBBaseDataLoaded(List<ParamSetting> buffers) {
//                for (ParamSetting paramSetting : buffers) {
//                    if (paramSetting.getKey() == ParamSetKey.MaximumCallTimePerOne) {
//                        int time = strToInt(paramSetting.getValue().trim());
//                        if (time > 0) {
////                            limitTime = time;
//                        }
//                    }
//                }
//            }
//            @Override
//            public void onDataNotAailable() {
//
//            }
//        }, new int[]{
//                ParamSetKey.MaximumCallTimePerOne
//        });
//        timerModel.startClock(this);
        phoneModel.acceptCall();
    }

    @Override
    public void callNumber(String number) {
        phoneModel.callNumber(number,mContext);
    }

    @Override
    public void setCallState(int state) {
        mView.showCallState(state);
    }


    @Override
    public void setPhone(String phone) {
        mPhone = phone;
        mDBContact.getContactByPhone(phone, this);
    }

    @Override
    public void onDBBaseDataLoaded(Contact contact) {
        if (contact == null || TextUtils.isEmpty(contact.getContact())) {
            mView.showName(mPhone);
            Dialer dialer = new Dialer();
            try {
                dialer.setCallDate(TimeTool.formatDate(new Date(System.currentTimeMillis())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dialer.setContact(mContext.getString(R.string.phone_unknown_contact));
            dialer.setDiallerState(2);
            dialer.setPhone(mPhone);
            mDBDialers.saveDBData(dialer);
            return;
        }
        mView.showName(contact.getContact());
        Dialer dialer = new Dialer();
        try {
            dialer.setCallDate(TimeTool.formatDate(new Date(System.currentTimeMillis())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dialer.setContact(contact.getContact());
        dialer.setDiallerState(2);
        dialer.setPhone(mPhone);
        mDBDialers.saveDBData(dialer);
    }

    @Override
    public void onDataNotAailable() {
        mView.showName(mPhone);
        Dialer dialer = new Dialer();
        try {
            dialer.setCallDate(TimeTool.formatDate(new Date(System.currentTimeMillis())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dialer.setContact(mContext.getString(R.string.phone_unknown_contact));
        dialer.setDiallerState(2);
        dialer.setPhone(mPhone);
        mDBDialers.saveDBData(dialer);
    }

//    private class MyPhoneStateListener extends PhoneStateListener {
//        @Override
//        public void onCallStateChanged(int state, String incomingNumber) {
//            //注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
//            super.onCallStateChanged(state, incomingNumber);
//            mView.showCallState(state);
//            switch (state) {
//                case TelephonyManager.CALL_STATE_IDLE:
//                    Log.d(TAG, "挂断");
////                    mHandler.sendEmptyMessage(ENDCALL_WHAT);
//                    break;
//                case TelephonyManager.CALL_STATE_OFFHOOK:
//                    Log.d(TAG, "接听");
//                    break;
//                case TelephonyManager.CALL_STATE_RINGING://输出来电号码
//                    Log.d(TAG, "响铃:来电号码" + incomingNumber);
//                    break;
//            }
//        }
//    }

}
