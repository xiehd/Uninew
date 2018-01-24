package com.uninew.car.db.order;

import android.support.annotation.NonNull;

import com.uninew.car.db.main.DBBaseDataSource;

/**
 * Created by Administrator on 2017/8/29 0029.
 */

public interface OrderLocalSource extends DBBaseDataSource<Order>{
    interface LoadOrdersCallback extends LoadDBDataCallBack<Order>{

    }
    interface GetOrderCallback extends GetDBDataCallBack<Order>{

    }

    /**
     *
     * @param callBack
     * @param state {@link OrderKey}
     */
    void getOrdersByState(@NonNull LoadOrdersCallback callBack, int... state);

    /**
     *
     * @param id
     * @param state {@link OrderKey}
     * @param callBack
     */
    void getOrderByState(int id ,int state, @NonNull GetOrderCallback callBack);

    void getOrderByStateAndType(int type,int state,@NonNull LoadOrdersCallback callBack);

    /**
     *
     * @param id
     * @param callBack
     */
    void getOrderByWorkId(int id , @NonNull GetOrderCallback callBack);

    /**
     *
     * @param workId
     * @param state {@link OrderKey}
     * @param finishTime
     */
    void changeState(int workId,int state,String finishTime);
}