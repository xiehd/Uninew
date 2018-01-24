package com.uninew.car.db.location;

import com.uninew.car.db.main.DBBaseDataSource;

/**
 * Created by Administrator on 2017/9/11 0011.
 */

public interface LocationLocalSource extends DBBaseDataSource<LocationMessage> {
    interface LoadLocationsCallBack extends LoadDBDataCallBack<LocationMessage> {

    }

    interface GetLocationCallBack extends GetDBDataCallBack<LocationMessage> {

    }
}
