package com.uninew.car.phone;

import android.content.Context;
import android.text.TextUtils;

import com.uninew.car.R;
import com.uninew.car.audio.TtsUtil;
import com.uninew.car.db.contacts.Contact;
import com.uninew.car.db.contacts.ContactLocalDataSource;
import com.uninew.car.db.contacts.ContactLocalSource;
import com.uninew.car.dialog.PromptDialog;

/**
 * Created by Administrator on 2017/9/2 0002.
 */

public class DialerPresenter implements DialerContract.Presenter {

    private DialerContract.View dialView;
    private StringBuffer phoneNumber;
    private Context mContext;

    public DialerPresenter(DialerContract.View dialView, Context context) {
        this.dialView = dialView;
        this.dialView.setPresenter(this);
        phoneNumber = new StringBuffer();
        this.mContext = context;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void cleared() {
        phoneNumber = null;
        if (phoneNumber == null) {
            phoneNumber = new StringBuffer();
        }
        dialView.showPhone("");
    }

    @Override
    public void delete(int index) {
        if (index == 0) {
            return;
        }
        if (phoneNumber != null && phoneNumber.length() >= index) {
            phoneNumber.delete(index - 1, index);
            dialView.showPhone(phoneNumber.toString());
        }
    }

    @Override
    public void call(String phone) {
//        Intent intent = new Intent(mContext, CallActivity.class);
//        intent.putExtra(CallActivity.INTENT_CALL_NUMBER, phone);
//        intent.putExtra(CallActivity.INTENT_CALL_STATE, TelephonyManager.CALL_STATE_OFFHOOK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);
//        PhoneModel.getInstance(mContext).callNumber(phone);
        if (TextUtils.isEmpty(phone)) {
            PromptDialog dialog = new PromptDialog(mContext);
            dialog.isSystemAlert();
            dialog.showToast(R.string.phone_null, 3000);
            dialog.show();
            return;
        }
        ContactLocalDataSource.getInstance(mContext).getContactByPhone(phone, new ContactLocalSource.GetContactCallBack() {
            @Override
            public void onDBBaseDataLoaded(final Contact contact) {
                if (contact.getSign() == 0 || contact.getSign() == 2) {
                    PhoneModel.callNumber(contact.getPhone(),mContext);
                } else {
                    PromptDialog dialog = new PromptDialog(mContext);
                    dialog.isSystemAlert();
                    TtsUtil.getInstance(mContext).speak(
                            mContext.getString(R.string.phone_permissions_not));
                    dialog.show();
                    dialog.showToast(R.string.phone_permissions_not, 3000);
                }
            }

            @Override
            public void onDataNotAailable() {
                PromptDialog dialog = new PromptDialog(mContext);
                dialog.isSystemAlert();
                TtsUtil.getInstance(mContext).speak(
                        mContext.getString(R.string.phone_permissions_not));
                dialog.show();
                dialog.showToast(R.string.phone_permissions_not, 3000);
            }
        });
    }

    @Override
    public void enterPhone(int index, String number) {
        if (phoneNumber == null) {
            phoneNumber = new StringBuffer();
        }
        if (phoneNumber.length() >= index) {
            phoneNumber.insert(index, number);
        } else {
            phoneNumber.append(number);
        }
        dialView.showPhone(phoneNumber.toString());
    }

    @Override
    public void init(String number) {
        if (phoneNumber == null) {
            phoneNumber = new StringBuffer();
        }
        phoneNumber.append(number);
        dialView.showPhone(number);
    }
}
