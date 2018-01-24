package com.uninew.car.db.settings;

import android.support.annotation.NonNull;

import com.uninew.car.db.main.DBBaseDataSource;

/**
 * Created by Administrator on 2017/9/5 0005.
 */

public interface PlatformLocalSource extends DBBaseDataSource<PlatformSettings> {

    interface LoadPlaformsCallBack extends LoadDBDataCallBack<PlatformSettings> {

    }

    interface GetPlaformCallBack extends GetDBDataCallBack<PlatformSettings> {

    }


    interface ChangePlatformListener {
        void onChangePlatsourMessage(@NonNull PlatformSettings platformSettings);
    }

    void registerNotify(@NonNull ChangePlatformListener listener);

    void unregisterNotify();

    void restoringDefault();

    void getService(int tcpId,@NonNull GetPlaformCallBack callBack);

    void setService(@NonNull PlatformSettings platformSettings);


}
