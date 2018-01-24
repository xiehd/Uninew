package com.uninew.car.db.paramSet;

import android.support.annotation.NonNull;

import com.uninew.car.db.main.DBBaseDataSource;
import com.uninew.car.db.settings.PlatformLocalSource;
import com.uninew.car.db.settings.PlatformSettings;

/**
 * Created by Administrator on 2017/10/14 0014.
 */

public interface ParamSetLocalSource extends DBBaseDataSource<ParamSetting> {
    interface LoadParamSettesCallBack extends LoadDBDataCallBack<ParamSetting> {

    }

    interface GetParamSetCallBack extends GetDBDataCallBack<ParamSetting> {

    }

    interface ChangeParamSetListener {
        void onChangePlatsourMessage(@NonNull ParamSetting paramSetting);
    }

    void registerNotify(@NonNull ChangeParamSetListener listener);

    void unregisterNotify();

    void getParamSettesByKey(LoadParamSettesCallBack callBack, int... key);
}
