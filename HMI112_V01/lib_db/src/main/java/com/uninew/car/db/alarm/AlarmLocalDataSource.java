package com.uninew.car.db.alarm;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.uninew.car.db.main.DBMetaData;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import tools.TimeTool;

/**
 * Created by Administrator on 2017/8/31 0031.
 */

public class AlarmLocalDataSource implements AlarmLocalSource {

    private static volatile AlarmLocalDataSource INSTANCE;
    private WeakReference<Context> wp;//防止context内存泄露的优化
    /**
     * contentprovider连接文本信息的uri
     **/
    private static final Uri CONTENT_URI = Uri.parse("content://" + DBMetaData.AUTHORITIES + "/"
            + DBMetaData.AlarmMessageEntry.TABLE_NAME + "/");
    private ContentResolver resolver;//ContentProvider操作类

    /* 数据表最多保存天数 （30天）*/
    private static final long ALARM_MAX_SAVE = 2592000000l;

    private AlarmLocalDataSource(Context context) {
        wp = new WeakReference<>(context);
        resolver = wp.get().getContentResolver();
    }

    public static AlarmLocalDataSource getInstance(Context context) {
        if (INSTANCE != null) {

        } else {
            synchronized (AlarmLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AlarmLocalDataSource(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAlarmsByType(int type, LoadAlarmsCallBack callBack) {
        List<AlarmMessage> alarmMessages = new ArrayList<>();
        String selection = DBMetaData.AlarmMessageEntry.ALARM_TYPE + "=?";
        String[] selectionArgs = {
                type + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs,
                DBMetaData.AlarmMessageEntry.START_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                AlarmMessage alarmMessage = new AlarmMessage();
                alarmMessage.setId(c.getInt(c.getColumnIndex(DBMetaData.AlarmMessageEntry._ID)));
                alarmMessage.setContent(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.CONTENT)));
                alarmMessage.setContinuedTime(c.getInt(c.getColumnIndex(DBMetaData.AlarmMessageEntry.CONTINUED_TIME)));
                alarmMessage.setDate(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.DATE)));
                alarmMessage.setStartTime(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.START_TIME)));
                alarmMessage.setStopTime(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.STOP_TIME)));
                alarmMessage.setAlarmType(c.getInt(c.getColumnIndex(DBMetaData.AlarmMessageEntry.ALARM_TYPE)));
                alarmMessages.add(alarmMessage);
            }
        }
        if (c != null) {
            c.close();
        }
        if (alarmMessages.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(alarmMessages);
        }
    }

    @Override
    public void getTodayAlarmsByType(String date, int type, LoadAlarmsCallBack callBack) {
        List<AlarmMessage> alarmMessages = new ArrayList<>();
        String selection = DBMetaData.AlarmMessageEntry.ALARM_TYPE + "=?"
                + DBMetaData.AND + DBMetaData.AlarmMessageEntry.DATE + "=?";
        String[] selectionArgs = {
                type + "",
                date
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs,
                DBMetaData.AlarmMessageEntry.START_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                AlarmMessage alarmMessage = new AlarmMessage();
                alarmMessage.setId(c.getInt(c.getColumnIndex(DBMetaData.AlarmMessageEntry._ID)));
                alarmMessage.setContent(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.CONTENT)));
                alarmMessage.setContinuedTime(c.getInt(c.getColumnIndex(DBMetaData.AlarmMessageEntry.CONTINUED_TIME)));
                alarmMessage.setDate(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.DATE)));
                alarmMessage.setStartTime(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.START_TIME)));
                alarmMessage.setStopTime(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.STOP_TIME)));
                alarmMessage.setAlarmType(c.getInt(c.getColumnIndex(DBMetaData.AlarmMessageEntry.ALARM_TYPE)));
                alarmMessages.add(alarmMessage);
            }
        }
        if (c != null) {
            c.close();
        }
        if (alarmMessages.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(alarmMessages);
        }
    }

    @Override
    public void getAlarmByType(int id, int type, GetAlarmCallBack callBack) {
        AlarmMessage alarmMessage = null;
        String selection = DBMetaData.AlarmMessageEntry.ALARM_TYPE + "=?"
                + DBMetaData.AND + DBMetaData.AlarmMessageEntry._ID + "=?";
        String[] selectionArgs = {
                type + "",
                id + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs,
                DBMetaData.AlarmMessageEntry.START_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            alarmMessage = new AlarmMessage();
            alarmMessage.setId(c.getInt(c.getColumnIndex(DBMetaData.AlarmMessageEntry._ID)));
            alarmMessage.setContent(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.CONTENT)));
            alarmMessage.setContinuedTime(c.getInt(c.getColumnIndex(DBMetaData.AlarmMessageEntry.CONTINUED_TIME)));
            alarmMessage.setDate(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.DATE)));
            alarmMessage.setStartTime(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.START_TIME)));
            alarmMessage.setStopTime(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.STOP_TIME)));
            alarmMessage.setAlarmType(c.getInt(c.getColumnIndex(DBMetaData.AlarmMessageEntry.ALARM_TYPE)));
        }
        if (c != null) {
            c.close();
        }
        if (alarmMessage == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(alarmMessage);
        }
    }

    @Override
    public void getTodayAlarmByType(int id, String date, int type, GetAlarmCallBack callBack) {
        AlarmMessage alarmMessage = null;
        String selection = DBMetaData.AlarmMessageEntry.ALARM_TYPE + "=?"
                + DBMetaData.AND + DBMetaData.AlarmMessageEntry._ID + "=?"
                + DBMetaData.AND + DBMetaData.AlarmMessageEntry.DATE + "=?";
        String[] selectionArgs = {
                type + "",
                id + "",
                date
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs,
                DBMetaData.AlarmMessageEntry.START_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            alarmMessage = new AlarmMessage();
            alarmMessage.setId(c.getInt(c.getColumnIndex(DBMetaData.AlarmMessageEntry._ID)));
            alarmMessage.setContent(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.CONTENT)));
            alarmMessage.setContinuedTime(c.getInt(c.getColumnIndex(DBMetaData.AlarmMessageEntry.CONTINUED_TIME)));
            alarmMessage.setDate(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.DATE)));
            alarmMessage.setStartTime(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.START_TIME)));
            alarmMessage.setStopTime(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.STOP_TIME)));
            alarmMessage.setAlarmType(c.getInt(c.getColumnIndex(DBMetaData.AlarmMessageEntry.ALARM_TYPE)));
        }
        if (c != null) {
            c.close();
        }
        if (alarmMessage == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(alarmMessage);
        }
    }

    @Override
    public void getAllDBDatas(@NonNull LoadDBDataCallBack callBack) {
        List<AlarmMessage> alarmMessages = new ArrayList<>();
        Cursor c = resolver.query(CONTENT_URI, null, null, null,
                DBMetaData.AlarmMessageEntry.START_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                AlarmMessage alarmMessage = new AlarmMessage();
                alarmMessage.setId(c.getInt(c.getColumnIndex(DBMetaData.AlarmMessageEntry._ID)));
                alarmMessage.setContent(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.CONTENT)));
                alarmMessage.setContinuedTime(c.getInt(c.getColumnIndex(DBMetaData.AlarmMessageEntry.CONTINUED_TIME)));
                alarmMessage.setDate(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.DATE)));
                alarmMessage.setStartTime(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.START_TIME)));
                alarmMessage.setStopTime(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.STOP_TIME)));
                alarmMessage.setAlarmType(c.getInt(c.getColumnIndex(DBMetaData.AlarmMessageEntry.ALARM_TYPE)));
                alarmMessages.add(alarmMessage);
            }
        }
        if (c != null) {
            c.close();
        }
        if (alarmMessages.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(alarmMessages);
        }
    }

    @Override
    public void getDBData(int _id, @NonNull GetDBDataCallBack callBack) {
        AlarmMessage alarmMessage = null;
        String selection = DBMetaData.AlarmMessageEntry._ID + "=?";
        String[] selectionArgs = {
                _id + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs,
                DBMetaData.AlarmMessageEntry.START_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            alarmMessage = new AlarmMessage();
            alarmMessage.setId(c.getInt(c.getColumnIndex(DBMetaData.AlarmMessageEntry._ID)));
            alarmMessage.setContent(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.CONTENT)));
            alarmMessage.setContinuedTime(c.getInt(c.getColumnIndex(DBMetaData.AlarmMessageEntry.CONTINUED_TIME)));
            alarmMessage.setDate(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.DATE)));
            alarmMessage.setStartTime(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.START_TIME)));
            alarmMessage.setStopTime(c.getString(c.getColumnIndex(DBMetaData.AlarmMessageEntry.STOP_TIME)));
            alarmMessage.setAlarmType(c.getInt(c.getColumnIndex(DBMetaData.AlarmMessageEntry.ALARM_TYPE)));
        }
        if (c != null) {
            c.close();
        }
        if (alarmMessage == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(alarmMessage);
        }
    }

    @Override
    public void saveDBData(@NonNull final AlarmMessage... alarmMessages) {
        if (alarmMessages != null && alarmMessages.length > 0) {
            getAllDBDatas(new LoadAlarmsCallBack() {
                @Override
                public void onDBBaseDataLoaded(List<AlarmMessage> buffers) {
                    try {
                        long time = TimeTool.parseToLong(buffers.get(buffers.size() - 1).getStartTime())
                                - TimeTool.parseToLong(buffers.get(0).getStartTime());
                        if (time < ALARM_MAX_SAVE) {
                            for (AlarmMessage alarmMessage : alarmMessages) {
                                ContentValues values = new ContentValues();
                                values.put(DBMetaData.AlarmMessageEntry.ALARM_TYPE, alarmMessage.getAlarmType());
                                values.put(DBMetaData.AlarmMessageEntry.CONTENT, alarmMessage.getContent());
                                values.put(DBMetaData.AlarmMessageEntry.CONTINUED_TIME, alarmMessage.getContinuedTime());
                                values.put(DBMetaData.AlarmMessageEntry.DATE, alarmMessage.getDate());
                                values.put(DBMetaData.AlarmMessageEntry.START_TIME, alarmMessage.getStartTime());
                                values.put(DBMetaData.AlarmMessageEntry.STOP_TIME, alarmMessage.getStopTime());
                                resolver.insert(CONTENT_URI, values);
                            }
                        } else {
                            int size = alarmMessages.length;
                            for (int i = 0; i < size; i++) {
                                String selection = DBMetaData.MessageEntry._ID + "=?";
                                String[] selectionArgs = {
                                        buffers.get(i).getId() + ""
                                };
                                ContentValues values = new ContentValues();
                                values.put(DBMetaData.AlarmMessageEntry.ALARM_TYPE, alarmMessages[i].getAlarmType());
                                values.put(DBMetaData.AlarmMessageEntry.CONTENT, alarmMessages[i].getContent());
                                values.put(DBMetaData.AlarmMessageEntry.CONTINUED_TIME, alarmMessages[i].getContinuedTime());
                                values.put(DBMetaData.AlarmMessageEntry.DATE, alarmMessages[i].getDate());
                                values.put(DBMetaData.AlarmMessageEntry.START_TIME, alarmMessages[i].getStartTime());
                                values.put(DBMetaData.AlarmMessageEntry.STOP_TIME, alarmMessages[i].getStopTime());
                                resolver.update(CONTENT_URI, values, selection, selectionArgs);
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        for (AlarmMessage alarmMessage : alarmMessages) {
                            ContentValues values = new ContentValues();
                            values.put(DBMetaData.AlarmMessageEntry.ALARM_TYPE, alarmMessage.getAlarmType());
                            values.put(DBMetaData.AlarmMessageEntry.CONTENT, alarmMessage.getContent());
                            values.put(DBMetaData.AlarmMessageEntry.CONTINUED_TIME, alarmMessage.getContinuedTime());
                            values.put(DBMetaData.AlarmMessageEntry.DATE, alarmMessage.getDate());
                            values.put(DBMetaData.AlarmMessageEntry.START_TIME, alarmMessage.getStartTime());
                            values.put(DBMetaData.AlarmMessageEntry.STOP_TIME, alarmMessage.getStopTime());
                            resolver.insert(CONTENT_URI, values);
                        }
                    }
                }

                @Override
                public void onDataNotAailable() {
                    for (AlarmMessage alarmMessage : alarmMessages) {
                        ContentValues values = new ContentValues();
                        values.put(DBMetaData.AlarmMessageEntry.ALARM_TYPE, alarmMessage.getAlarmType());
                        values.put(DBMetaData.AlarmMessageEntry.CONTENT, alarmMessage.getContent());
                        values.put(DBMetaData.AlarmMessageEntry.CONTINUED_TIME, alarmMessage.getContinuedTime());
                        values.put(DBMetaData.AlarmMessageEntry.DATE, alarmMessage.getDate());
                        values.put(DBMetaData.AlarmMessageEntry.START_TIME, alarmMessage.getStartTime());
                        values.put(DBMetaData.AlarmMessageEntry.STOP_TIME, alarmMessage.getStopTime());
                        resolver.insert(CONTENT_URI, values);
                    }
                }
            });

        }
    }

    @Override
    public void deleteAllDBDatas() {
        resolver.delete(CONTENT_URI, null, null);
    }

    @Override
    public void deleteDBData(@NonNull int id) {
        String selection = DBMetaData.AlarmMessageEntry._ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        resolver.delete(CONTENT_URI, selection, selectionArgs);
    }
}
