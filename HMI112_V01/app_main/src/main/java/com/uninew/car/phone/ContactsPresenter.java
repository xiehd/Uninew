package com.uninew.car.phone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.uninew.car.R;
import com.uninew.car.audio.TtsUtil;
import com.uninew.car.db.contacts.Contact;
import com.uninew.car.db.contacts.ContactLocalDataSource;
import com.uninew.car.db.contacts.ContactLocalSource;
import com.uninew.car.dialog.PromptDialog;
import com.uninew.car.until.ComparatorContacts;
import com.uninew.car.until.PinyinUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class ContactsPresenter implements ContactsContract.Presenter, ContactLocalSource.LoadContactsCallBack {

    private Context mContext;
    private ContactsContract.View mView;
    private ContactLocalSource mDBContacts;
    private List<Contact> mContacts;

    public ContactsPresenter(final Context context, ContactsContract.View view) {
        this.mContext = context;
        this.mView = view;
        mView.setPresenter(this);
        mDBContacts = ContactLocalDataSource.getInstance(mContext);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                Contact contact = new Contact();
                contact.setSign(0);
                contact.setContact(context.getString(R.string.main_default_drivername));
                contact.setPhone("15876999260");
////                Contact contact1 = new Contact();
////                contact1.setContact(context.getString(R.string.main_default_drivername));
////                contact1.setPhone("1587692265234");
////                Contact contact2 = new Contact();
////                contact2.setContact(context.getString(R.string.main_default_drivername));
////                contact2.setPhone("158769226525");
////                Contact contact3 = new Contact();
////                contact3.setContact(context.getString(R.string.main_default_drivername));
////                contact3.setPhone("158769226528");
//                mDBContacts.saveDBData(new Contact[]{contact, contact1, contact2, contact3});
        mDBContacts.saveDBData(new Contact[]{contact});
//            }
//        }).start();

    }

    @Override
    public void start() {
        mDBContacts.getAllDBDatas(this);
    }

    @Override
    public void stop() {

    }

    @Override
    public void delectContact(int id) {
        mDBContacts.deleteDBData(id);
        mDBContacts.getAllDBDatas(this);
    }

    @Override
    public void call(@NonNull final Contact contact) {
        if (TextUtils.isEmpty(contact.getPhone())) {
            PromptDialog dialog = new PromptDialog(mContext);
            dialog.isSystemAlert();
            dialog.show();
            dialog.showToast(R.string.phone_null, 3000);
        }
        if (contact.getSign() == 0 || contact.getSign() == 2) {
            PromptDialog dialog = new PromptDialog(mContext);
            dialog.isSystemAlert();
            dialog.show();
            dialog.setContent(R.string.phone_call_advisory);
            dialog.setOnDialogClickListener(R.string.confirm, R.string.back, new PromptDialog.OnDialogClickListener() {
                @Override
                public void onLeft(PromptDialog dialog) {
                    PhoneModel.callNumber(contact.getPhone(),mContext);
                    dialog.cancel();
                }

                @Override
                public void onRight(PromptDialog dialog) {
                    dialog.cancel();
                }
            });
        } else {
            PromptDialog dialog = new PromptDialog(mContext);
            dialog.isSystemAlert();
            dialog.show();
            TtsUtil.getInstance(mContext).speak(
                    mContext.getString(R.string.phone_permissions_not));
            dialog.showToast(R.string.phone_permissions_not, 3000);
        }
    }

    @Override
    public void search(@NonNull String str) {
        List<Contact> searchContacts = new ArrayList<>();
        if (mContacts != null && !mContacts.isEmpty()) {
            int size = mContacts.size();
            for (int i = 0; i < size; i++) {
                Contact contact = mContacts.get(i);
                if (contact.getContact().contains(str) || contact.getPhone().contains(str)
                        || PinyinUtil.getPinyin(contact.getContact()).contains(PinyinUtil.getPinyin(str))
                        || TextUtils.equals(PinyinUtil.getPinyin(contact.getContact()).toLowerCase().charAt(0) + "", str.toLowerCase())) {
                    searchContacts.add(contact);
                }
            }
            mView.showContacts(searchContacts);
        }
    }

    @Override
    public void onDBBaseDataLoaded(List<Contact> buffers) {
        List<Contact> contacts = ComparatorContacts.sort(buffers);
        this.mContacts = contacts;
        mView.showContacts(contacts);
    }

    @Override
    public void onDataNotAailable() {
        mView.showContacts(null);
    }
}
