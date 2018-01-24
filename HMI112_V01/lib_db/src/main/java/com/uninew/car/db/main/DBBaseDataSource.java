package com.uninew.car.db.main;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Administrator on 2017/8/29 0029.
 */

public interface DBBaseDataSource<T> {
    interface LoadDBDataCallBack<T> {
        void onDBBaseDataLoaded(List<T> buffers);

        void onDataNotAailable();
    }

    interface GetDBDataCallBack<T> {
        void onDBBaseDataLoaded(T t);

        void onDataNotAailable();
    }

    void getAllDBDatas(@NonNull LoadDBDataCallBack callBack);

    void getDBData(int _id, @NonNull GetDBDataCallBack callBack);

    void saveDBData(@NonNull T... t);

    void deleteAllDBDatas();

    void deleteDBData(@NonNull int id);

}
