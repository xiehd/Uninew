package com.uninew.car.phone;

import android.support.annotation.NonNull;

import com.uninew.car.base.BasePresenter;
import com.uninew.car.base.BaseView;
import com.uninew.car.db.contacts.Contact;

import java.util.List;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public interface ContactsContract {

    interface View extends BaseView<Presenter>{
        void showContacts(List<Contact> contacts);
    }

    interface Presenter extends BasePresenter{
        void delectContact(int id);
        void call(@NonNull Contact contact);
        void search(@NonNull String str);
    }
}
