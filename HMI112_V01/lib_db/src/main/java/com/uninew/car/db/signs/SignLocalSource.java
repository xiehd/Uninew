package com.uninew.car.db.signs;

import com.uninew.car.db.main.DBBaseDataSource;

/**
 * Created by Administrator on 2017/10/11 0011.
 */

public interface SignLocalSource extends DBBaseDataSource<SignOrSignOut> {
    interface LoadSignsCallBack extends LoadDBDataCallBack<SignOrSignOut> {

    }

    interface GetSignCallBack extends GetDBDataCallBack<SignOrSignOut> {

    }

    void getSignsByteSignstate(int signState,LoadSignsCallBack callBack);
}
