package com.uninew.car.db.contacts;

import com.uninew.car.db.main.DBBaseDataSource;

/**
 * Created by Administrator on 2017/9/11 0011.
 */

public interface ContactLocalSource extends DBBaseDataSource<Contact> {
    interface LoadContactsCallBack extends LoadDBDataCallBack<Contact> {

    }

    interface GetContactCallBack extends GetDBDataCallBack<Contact> {

    }

    void getContactByPhone(String phone,GetContactCallBack callBack);
}
