package com.uninew.car.db.audio;

import com.uninew.car.db.main.DBBaseDataSource;

/**
 * Created by Administrator on 2017/11/10 0010.
 */

public interface AudioLocalSource extends DBBaseDataSource<AudioData> {
    interface LoadAudiosCallBack extends LoadDBDataCallBack<AudioData> {

    }

    interface GetAudioCallBack extends GetDBDataCallBack<AudioData> {

    }

    void getAudiosByStartTime(long time,GetAudioCallBack callBack);
}
