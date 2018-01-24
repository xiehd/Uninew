package com.uninew.car.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.uninew.car.R;
import com.uninew.car.audio.TtsUtil;
import com.uninew.car.db.contacts.Contact;
import com.uninew.car.db.contacts.ContactLocalDataSource;
import com.uninew.car.db.contacts.ContactLocalSource;
import com.uninew.car.db.main.DBMetaData;
import com.uninew.car.db.paramSet.ParamSetKey;
import com.uninew.car.db.paramSet.ParamSetLocalDataSource;
import com.uninew.car.db.paramSet.ParamSetLocalSource;
import com.uninew.car.db.paramSet.ParamSetting;
import com.uninew.car.db.settings.SettingsDefaultValue;
import com.uninew.car.dialog.PromptDialog;
import com.uninew.car.phone.CallActivity;
import com.uninew.car.phone.PhoneModel;

import java.util.List;

/**
 * Created by Administrator on 2017/9/18 0018.
 */

public class PhoneStateReceiver extends BroadcastReceiver {

    private static final String TAG = "PhoneStateReceiver";
    private static final boolean D = true;
    private static String phoneReset = "13590320923";//复位来电号码
    private static String phoneRefact = "123456789";//恢复出厂设置来电号码
    @Override
    public void onReceive(final Context context, Intent intent) {
        final Context ctx = context;
        ParamSetLocalDataSource.getInstance(context).getParamSettesByKey(new ParamSetLocalSource.LoadParamSettesCallBack() {
            @Override
            public void onDBBaseDataLoaded(List<ParamSetting> buffers) {
                for(ParamSetting paramSetting : buffers){
                    if(paramSetting.getKey() == ParamSetKey.ResetPhoneNumber){
                        phoneReset = paramSetting.getValue();
                    }else if(paramSetting.getKey() == ParamSetKey.RestoreSettingsPhoneNumber){
                        phoneRefact = paramSetting.getValue();
                    }
                }
            }
            @Override
            public void onDataNotAailable() {

            }
        },new int[]{
                ParamSetKey.ResetPhoneNumber,
                ParamSetKey.RestoreSettingsPhoneNumber
        });
        TelephonyManager telMgr = (TelephonyManager) ctx.getSystemService(Service.TELEPHONY_SERVICE);
        switch (telMgr.getCallState()) {
            case TelephonyManager.CALL_STATE_RINGING:// 来电响铃
                if (D)
                    Log.d(TAG, "....................主人，那家伙又来电话了....................");
                final String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                if (D)
                    Log.d(TAG, "number:" + number);

                if (numberJudge(context, number)) {//如果是复位来电或者出厂设置来电则不需要接听
                   break;
                }
                ContactLocalDataSource.getInstance(context).getContactByPhone(number, new ContactLocalSource.GetContactCallBack() {
                    @Override
                    public void onDBBaseDataLoaded(Contact contact) {
                        if(contact.getSign() == 1 && contact.getSign() == 2){

                        }else {
                            PhoneModel.endCall(context);
                            PromptDialog dialog = new PromptDialog(context);
                            dialog.isSystemAlert();
                            dialog.show();
                            TtsUtil.getInstance(context).speak(
                                    context.getString(R.string.phone_permissions_not_call));
                            dialog.showToast(R.string.phone_permissions_not_call, 5000);
                        }
                    }

                    @Override
                    public void onDataNotAailable() {
                        PhoneModel.endCall(context);
                        PromptDialog dialog = new PromptDialog(context);
                        dialog.isSystemAlert();
                        dialog.show();
                        TtsUtil.getInstance(context).speak(
                                context.getString(R.string.phone_permissions_not_call));
                        dialog.showToast(R.string.phone_permissions_not_call, 5000);
                    }
                });
//                Intent intent1 = new Intent(context, CallActivity.class);
//                intent1.putExtra(CallActivity.INTENT_CALL_NUMBER, number);
//                intent1.putExtra(CallActivity.INTENT_CALL_STATE, TelephonyManager.CALL_STATE_RINGING);
//                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent1);
//                PhoneModel.getInstance(context);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:// 接听电话;电话打进来接通状态；电话打出时首先监听
                break;
            case TelephonyManager.CALL_STATE_IDLE:// 挂断电话;不管是电话打出去还是电话打进来都会监听到的状态。
                if (D)
                    Log.d(TAG, "....................电话已挂断了....................");
                break;
            default:
                break;
        }

    }



    /**
     * 来电号码判断
     *
     * @param number
     */
    private boolean numberJudge(Context context, String number) {
        boolean ispone = false;
        if (number == null || number.equals(""))
            return ispone;

        if (number.equals(phoneReset)) {//复位
            Intent intent = new Intent();
            intent.setAction("receiver_main_reset");
            context.sendBroadcast(intent);
            ispone = true;
        }

        if (number.equals(phoneRefact)) {//恢复出厂设置
            context.sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
            ispone = true;
        }
        return ispone;
    }
}
