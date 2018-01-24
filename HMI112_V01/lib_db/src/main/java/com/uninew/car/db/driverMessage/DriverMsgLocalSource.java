package com.uninew.car.db.driverMessage;

import android.support.annotation.NonNull;

import com.uninew.car.db.main.DBBaseDataSource;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public interface DriverMsgLocalSource extends DBBaseDataSource<DriverMessage> {
    interface LoadDriverMsgCallBack extends LoadDBDataCallBack<DriverMessage> {

    }

    interface GetDriverMsgCallBack extends GetDBDataCallBack<DriverMessage> {

    }

    void getDriverMsgByDriverCertificate(@NonNull String driverCertificate,@NonNull GetDriverMsgCallBack callBack);

}
