package com.uninew.car.db.attendance;

import com.uninew.car.db.main.DBBaseDataSource;

/**
 * Created by Administrator on 2017/9/7 0007.
 */

public interface AttendanceLocalSource extends DBBaseDataSource<Attendance> {
    interface LoadAttendancesCallback extends LoadDBDataCallBack<Attendance> {

    }

    interface GetAttendanceCallback extends GetDBDataCallBack<Attendance> {

    }

    void getAllAttendancesByState(int state, LoadAttendancesCallback callback);

    void getAllAttendancesByJobNumber(String jobNumber, LoadAttendancesCallback callback);

    void getAttendanceByState(int id, int state, GetAttendanceCallback callback);

    void getAttendanceByJobNumber(int id, String jobNumber, GetAttendanceCallback callback);

    void getCurrentAttendance(int state,String jobNumber,GetAttendanceCallback callback);
}
