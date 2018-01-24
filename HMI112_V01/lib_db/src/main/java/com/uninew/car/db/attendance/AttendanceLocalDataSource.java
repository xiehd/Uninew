package com.uninew.car.db.attendance;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.uninew.car.db.main.DBMetaData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/8 0008.
 */

public class AttendanceLocalDataSource implements AttendanceLocalSource {

    private static volatile AttendanceLocalDataSource INSTANCE;
    private WeakReference<Context> wp;//防止context内存泄露的优化
    /**
     * contentprovider连接文本信息的uri
     **/
    private static final Uri CONTENT_URI = Uri.parse("content://" + DBMetaData.AUTHORITIES + "/"
            + DBMetaData.AttendanceEntry.TABLE_NAME + "/");
    private ContentResolver resolver;//ContentProvider操作类


    private AttendanceLocalDataSource(Context context) {
        wp = new WeakReference<>(context);
        resolver = wp.get().getContentResolver();
    }

    public static AttendanceLocalDataSource getInstance(Context context) {
        if (INSTANCE != null) {

        } else {
            synchronized (AttendanceLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AttendanceLocalDataSource(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAllAttendancesByState(int state, LoadAttendancesCallback callback) {
        List<Attendance> attendances = new ArrayList<>();
        String selection = DBMetaData.AttendanceEntry.ATTENDANCE_STATE + "=?";
        String[] selectionArgs = {
                state + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.AttendanceEntry.START_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                Attendance attendance = new Attendance();
                attendance.setId(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry._ID)));
                attendance.setActionTimes(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry.ACTION_TIMES)));
                attendance.setAttendanceState(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry.ATTENDANCE_STATE)));
                attendance.setDrivingMileage(c.getDouble(c.getColumnIndex(DBMetaData.AttendanceEntry.DRIVING_MILEAGE)));
                attendance.setEndTime(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.END_TIME)));
                attendance.setImgAvatar(c.getBlob(c.getColumnIndex(DBMetaData.AttendanceEntry.IMG_AVATAR)));
                attendance.setJobNumber(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.JOB_NUMBER)));
                attendance.setPassengerMileage(c.getDouble(c.getColumnIndex(DBMetaData.AttendanceEntry.PASSENGER_MILEAGE)));
                attendance.setStartTime(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.START_TIME)));
                attendance.setUrseName(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.URSE_NAME)));
                attendances.add(attendance);
            }
        }
        if (c != null) {
            c.close();
        }
        if (attendances.isEmpty()) {
            callback.onDataNotAailable();
        } else {
            callback.onDBBaseDataLoaded(attendances);
        }
    }

    @Override
    public void getAllAttendancesByJobNumber(String jobNumber, LoadAttendancesCallback callback) {
        List<Attendance> attendances = new ArrayList<>();
        String selection = DBMetaData.AttendanceEntry.JOB_NUMBER + "=?";
        String[] selectionArgs = {
                jobNumber
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.AttendanceEntry.START_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                Attendance attendance = new Attendance();
                attendance.setId(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry._ID)));
                attendance.setActionTimes(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry.ACTION_TIMES)));
                attendance.setAttendanceState(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry.ATTENDANCE_STATE)));
                attendance.setDrivingMileage(c.getDouble(c.getColumnIndex(DBMetaData.AttendanceEntry.DRIVING_MILEAGE)));
                attendance.setEndTime(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.END_TIME)));
                attendance.setImgAvatar(c.getBlob(c.getColumnIndex(DBMetaData.AttendanceEntry.IMG_AVATAR)));
                attendance.setJobNumber(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.JOB_NUMBER)));
                attendance.setPassengerMileage(c.getDouble(c.getColumnIndex(DBMetaData.AttendanceEntry.PASSENGER_MILEAGE)));
                attendance.setStartTime(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.START_TIME)));
                attendance.setUrseName(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.URSE_NAME)));
                attendances.add(attendance);
            }
        }
        if (c != null) {
            c.close();
        }
        if (attendances.isEmpty()) {
            callback.onDataNotAailable();
        } else {
            callback.onDBBaseDataLoaded(attendances);
        }
    }

    @Override
    public void getAttendanceByState(int id, int state, GetAttendanceCallback callback) {
        Attendance attendance = null;
        String selection = DBMetaData.AttendanceEntry._ID + "=?"
                + DBMetaData.AND + DBMetaData.AttendanceEntry.ATTENDANCE_STATE + "=?";
        String[] selectionArgs = {
                id + "",
                state + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.AttendanceEntry.START_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            attendance = new Attendance();
            attendance.setId(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry._ID)));
            attendance.setActionTimes(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry.ACTION_TIMES)));
            attendance.setAttendanceState(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry.ATTENDANCE_STATE)));
            attendance.setDrivingMileage(c.getDouble(c.getColumnIndex(DBMetaData.AttendanceEntry.DRIVING_MILEAGE)));
            attendance.setEndTime(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.END_TIME)));
            attendance.setImgAvatar(c.getBlob(c.getColumnIndex(DBMetaData.AttendanceEntry.IMG_AVATAR)));
            attendance.setJobNumber(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.JOB_NUMBER)));
            attendance.setPassengerMileage(c.getDouble(c.getColumnIndex(DBMetaData.AttendanceEntry.PASSENGER_MILEAGE)));
            attendance.setStartTime(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.START_TIME)));
            attendance.setUrseName(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.URSE_NAME)));
        }
        if (c != null) {
            c.close();
        }
        if (attendance == null) {
            callback.onDataNotAailable();
        } else {
            callback.onDBBaseDataLoaded(attendance);
        }
    }

    @Override
    public void getAttendanceByJobNumber(int id, String jobNumber, GetAttendanceCallback callback) {
        Attendance attendance = null;
        String selection = DBMetaData.AttendanceEntry._ID + "=?"
                + DBMetaData.AND + DBMetaData.AttendanceEntry.JOB_NUMBER + "=?";
        String[] selectionArgs = {
                id + "",
                jobNumber + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.AttendanceEntry.START_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            attendance = new Attendance();
            attendance.setId(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry._ID)));
            attendance.setActionTimes(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry.ACTION_TIMES)));
            attendance.setAttendanceState(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry.ATTENDANCE_STATE)));
            attendance.setDrivingMileage(c.getDouble(c.getColumnIndex(DBMetaData.AttendanceEntry.DRIVING_MILEAGE)));
            attendance.setEndTime(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.END_TIME)));
            attendance.setImgAvatar(c.getBlob(c.getColumnIndex(DBMetaData.AttendanceEntry.IMG_AVATAR)));
            attendance.setJobNumber(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.JOB_NUMBER)));
            attendance.setPassengerMileage(c.getDouble(c.getColumnIndex(DBMetaData.AttendanceEntry.PASSENGER_MILEAGE)));
            attendance.setStartTime(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.START_TIME)));
            attendance.setUrseName(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.URSE_NAME)));
        }
        if (c != null) {
            c.close();
        }
        if (attendance == null) {
            callback.onDataNotAailable();
        } else {
            callback.onDBBaseDataLoaded(attendance);
        }
    }

    @Override
    public void getCurrentAttendance(int state, String jobNumber, GetAttendanceCallback callback) {
        Attendance attendance = null;
        String selection = DBMetaData.AttendanceEntry.JOB_NUMBER + "=?"
                + DBMetaData.AND + DBMetaData.AttendanceEntry.ATTENDANCE_STATE + "=?";
        String[] selectionArgs = {
                jobNumber + "",
                state + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.AttendanceEntry.START_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            attendance = new Attendance();
            attendance.setId(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry._ID)));
            attendance.setActionTimes(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry.ACTION_TIMES)));
            attendance.setAttendanceState(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry.ATTENDANCE_STATE)));
            attendance.setDrivingMileage(c.getDouble(c.getColumnIndex(DBMetaData.AttendanceEntry.DRIVING_MILEAGE)));
            attendance.setEndTime(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.END_TIME)));
            attendance.setImgAvatar(c.getBlob(c.getColumnIndex(DBMetaData.AttendanceEntry.IMG_AVATAR)));
            attendance.setJobNumber(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.JOB_NUMBER)));
            attendance.setPassengerMileage(c.getDouble(c.getColumnIndex(DBMetaData.AttendanceEntry.PASSENGER_MILEAGE)));
            attendance.setStartTime(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.START_TIME)));
            attendance.setUrseName(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.URSE_NAME)));
        }
        if (c != null) {
            c.close();
        }
        if (attendance == null) {
            callback.onDataNotAailable();
        } else {
            callback.onDBBaseDataLoaded(attendance);
        }
    }

    @Override
    public void getAllDBDatas(@NonNull LoadDBDataCallBack callBack) {
        List<Attendance> attendances = new ArrayList<>();
        Cursor c = resolver.query(CONTENT_URI, null, null, null, DBMetaData.AttendanceEntry.START_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                Attendance attendance = new Attendance();
                attendance.setId(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry._ID)));
                attendance.setActionTimes(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry.ACTION_TIMES)));
                attendance.setAttendanceState(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry.ATTENDANCE_STATE)));
                attendance.setDrivingMileage(c.getDouble(c.getColumnIndex(DBMetaData.AttendanceEntry.DRIVING_MILEAGE)));
                attendance.setEndTime(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.END_TIME)));
                attendance.setImgAvatar(c.getBlob(c.getColumnIndex(DBMetaData.AttendanceEntry.IMG_AVATAR)));
                attendance.setJobNumber(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.JOB_NUMBER)));
                attendance.setPassengerMileage(c.getDouble(c.getColumnIndex(DBMetaData.AttendanceEntry.PASSENGER_MILEAGE)));
                attendance.setStartTime(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.START_TIME)));
                attendance.setUrseName(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.URSE_NAME)));
                attendances.add(attendance);
            }
        }
        if (c != null) {
            c.close();
        }
        if (attendances.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(attendances);
        }
    }

    @Override
    public void getDBData(int _id, @NonNull GetDBDataCallBack callBack) {
        Attendance attendance = null;
        String selection = DBMetaData.AttendanceEntry._ID + "=?";
        String[] selectionArgs = {
                _id + "",
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.AttendanceEntry.START_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            attendance = new Attendance();
            attendance.setId(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry._ID)));
            attendance.setActionTimes(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry.ACTION_TIMES)));
            attendance.setAttendanceState(c.getInt(c.getColumnIndex(DBMetaData.AttendanceEntry.ATTENDANCE_STATE)));
            attendance.setDrivingMileage(c.getDouble(c.getColumnIndex(DBMetaData.AttendanceEntry.DRIVING_MILEAGE)));
            attendance.setEndTime(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.END_TIME)));
            attendance.setImgAvatar(c.getBlob(c.getColumnIndex(DBMetaData.AttendanceEntry.IMG_AVATAR)));
            attendance.setJobNumber(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.JOB_NUMBER)));
            attendance.setPassengerMileage(c.getDouble(c.getColumnIndex(DBMetaData.AttendanceEntry.PASSENGER_MILEAGE)));
            attendance.setStartTime(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.START_TIME)));
            attendance.setUrseName(c.getString(c.getColumnIndex(DBMetaData.AttendanceEntry.URSE_NAME)));
        }
        if (c != null) {
            c.close();
        }
        if (attendance == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(attendance);
        }
    }

    @Override
    public void saveDBData(@NonNull Attendance... attendances) {
        if (attendances != null && attendances.length > 0) {
            for (Attendance attendance : attendances) {
                ContentValues values = new ContentValues();
                values.put(DBMetaData.AttendanceEntry.ACTION_TIMES, attendance.getActionTimes());
                values.put(DBMetaData.AttendanceEntry.ATTENDANCE_STATE, attendance.getAttendanceState());
                values.put(DBMetaData.AttendanceEntry.DRIVING_MILEAGE, attendance.getDrivingMileage());
                values.put(DBMetaData.AttendanceEntry.END_TIME, attendance.getEndTime());
                values.put(DBMetaData.AttendanceEntry.IMG_AVATAR, attendance.getImgAvatar());
                values.put(DBMetaData.AttendanceEntry.JOB_NUMBER, attendance.getJobNumber());
                values.put(DBMetaData.AttendanceEntry.PASSENGER_MILEAGE, attendance.getPassengerMileage());
                values.put(DBMetaData.AttendanceEntry.START_TIME, attendance.getStartTime());
                values.put(DBMetaData.AttendanceEntry.URSE_NAME, attendance.getUrseName());
                resolver.insert(CONTENT_URI, values);
                values.clear();
            }
        }
    }

    @Override
    public void deleteAllDBDatas() {
        resolver.delete(CONTENT_URI, null, null);
    }

    @Override
    public void deleteDBData(@NonNull int id) {
        String selection = DBMetaData.AttendanceEntry._ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        resolver.delete(CONTENT_URI, selection, selectionArgs);
    }
}
