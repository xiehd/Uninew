package com.uninew.car.db.revenue;

import com.uninew.car.db.main.DBBaseDataSource;

/**
 * Created by Administrator on 2017/8/30 0030.
 */

public interface RevenueLocalSource extends DBBaseDataSource<Revenue> {
    interface LoadRevenueCallback extends LoadDBDataCallBack<Revenue> {

    }

    interface GetRevenueCallback extends GetDBDataCallBack<Revenue> {

    }

    void getRevenueCallBack(String time, GetRevenueCallback callback);

}
