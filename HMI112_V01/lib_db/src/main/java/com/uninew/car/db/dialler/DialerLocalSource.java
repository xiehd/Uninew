package com.uninew.car.db.dialler;

import com.uninew.car.db.main.DBBaseDataSource;

/**
 * Created by Administrator on 2017/9/7 0007.
 */

public interface DialerLocalSource extends DBBaseDataSource<Dialer> {
    interface LoadDialersCallback extends LoadDBDataCallBack<Dialer> {

    }

    interface GetDialerCallback extends GetDBDataCallBack<Dialer> {

    }

    void getAllDiallersByState(int state,LoadDialersCallback callBack);
}
