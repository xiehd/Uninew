package com.uninew.car.db.alarm;

import com.uninew.car.db.main.DBBaseDataSource;

/**
 * Created by Administrator on 2017/8/31 0031.
 */

public interface AlarmLocalSource extends DBBaseDataSource<AlarmMessage> {
    interface LoadAlarmsCallBack extends LoadDBDataCallBack<AlarmMessage> {

    }

    interface GetAlarmCallBack extends GetDBDataCallBack<AlarmMessage> {

    }

    void getAlarmsByType(int type, LoadAlarmsCallBack callBack);

    void getTodayAlarmsByType(String date, int type, LoadAlarmsCallBack callBack);

    void getAlarmByType(int id, int type, GetAlarmCallBack callBack);

    void getTodayAlarmByType(int id, String date, int type, GetAlarmCallBack callBack);

}
