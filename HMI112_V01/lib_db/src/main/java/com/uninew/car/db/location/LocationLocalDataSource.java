package com.uninew.car.db.location;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.uninew.car.db.alarm.AlarmLocalDataSource;
import com.uninew.car.db.main.DBMetaData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/11 0011.
 */

public class LocationLocalDataSource implements LocationLocalSource {

    private static volatile LocationLocalDataSource INSTANCE;
    private WeakReference<Context> wp;//防止context内存泄露的优化
    /**
     * contentprovider连接文本信息的uri
     **/
    private static final Uri CONTENT_URI = Uri.parse("content://" + DBMetaData.AUTHORITIES + "/"
            + DBMetaData.LocationEntry.TABLE_NAME + "/");
    private ContentResolver resolver;//ContentProvider操作类


    /* 数据库保存最多天数 （7天）*/
    private static final long LOCATION_MAX_SAVE = 10000;


    private LocationLocalDataSource(Context context) {
        wp = new WeakReference<>(context);
        resolver = wp.get().getContentResolver();
    }

    public static LocationLocalDataSource getInstance(Context context) {
        if (INSTANCE != null) {

        } else {
            synchronized (LocationLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocationLocalDataSource(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAllDBDatas(@NonNull LoadDBDataCallBack callBack) {
        List<LocationMessage> locationMessages = new ArrayList<>();
        synchronized (this) {
            Cursor c = resolver.query(CONTENT_URI, null, null, null, DBMetaData.LocationEntry.TIME + DBMetaData.ASC);
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    LocationMessage locationMessage = new LocationMessage();
                    locationMessage.setId(c.getInt(c.getColumnIndex(DBMetaData.LocationEntry._ID)));
                    locationMessage.setSerialNumber(c.getInt(c.getColumnIndex(DBMetaData.LocationEntry.SERIAL_NUMBER)));
                    locationMessage.setUploadTime(c.getString(c.getColumnIndex(DBMetaData.LocationEntry.UPLOAD_TIME)));
                    locationMessage.setAlarmFlag(c.getInt(c.getColumnIndex(DBMetaData.LocationEntry.ALARM)));
                    locationMessage.setDirection(c.getDouble(c.getColumnIndex(DBMetaData.LocationEntry.DIRECTION)));
                    locationMessage.setLatitude(c.getDouble(c.getColumnIndex(DBMetaData.LocationEntry.LATITUDE)));
                    locationMessage.setLongitude(c.getDouble(c.getColumnIndex(DBMetaData.LocationEntry.LONGITUDE)));
                    locationMessage.setElevation(c.getDouble(c.getColumnIndex(DBMetaData.LocationEntry.ELEVATION)));
                    locationMessage.setSpeed(c.getDouble(c.getColumnIndex(DBMetaData.LocationEntry.SPEED)));
                    locationMessage.setState(c.getInt(c.getColumnIndex(DBMetaData.LocationEntry.STATE)));
                    locationMessage.setTime(c.getString(c.getColumnIndex(DBMetaData.LocationEntry.TIME)));
                    locationMessage.setAdditionalInfo(c.getBlob(c.getColumnIndex(DBMetaData.LocationEntry.ADDITIONAL_INFO)));
                    locationMessages.add(locationMessage);
                }
            }
            if (c != null) {
                c.close();
            }
        }
        if (locationMessages.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(locationMessages);
        }
    }

    @Override
    public void getDBData(int _id, @NonNull GetDBDataCallBack callBack) {
        LocationMessage locationMessage = null;
        String selection = DBMetaData.LocationEntry._ID + "=?";
        String[] selectionArgs = {
                _id + ""
        };
        synchronized (this) {
            Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.LocationEntry.TIME + DBMetaData.ASC);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                new LocationMessage();
                locationMessage.setId(c.getInt(c.getColumnIndex(DBMetaData.LocationEntry._ID)));
                locationMessage.setSerialNumber(c.getInt(c.getColumnIndex(DBMetaData.LocationEntry.SERIAL_NUMBER)));
                locationMessage.setUploadTime(c.getString(c.getColumnIndex(DBMetaData.LocationEntry.UPLOAD_TIME)));
                locationMessage.setAlarmFlag(c.getInt(c.getColumnIndex(DBMetaData.LocationEntry.ALARM)));
                locationMessage.setDirection(c.getDouble(c.getColumnIndex(DBMetaData.LocationEntry.DIRECTION)));
                locationMessage.setLatitude(c.getDouble(c.getColumnIndex(DBMetaData.LocationEntry.LATITUDE)));
                locationMessage.setLongitude(c.getDouble(c.getColumnIndex(DBMetaData.LocationEntry.LONGITUDE)));
                locationMessage.setElevation(c.getDouble(c.getColumnIndex(DBMetaData.LocationEntry.ELEVATION)));
                locationMessage.setSpeed(c.getDouble(c.getColumnIndex(DBMetaData.LocationEntry.SPEED)));
                locationMessage.setState(c.getInt(c.getColumnIndex(DBMetaData.LocationEntry.STATE)));
                locationMessage.setTime(c.getString(c.getColumnIndex(DBMetaData.LocationEntry.TIME)));
                locationMessage.setAdditionalInfo(c.getBlob(c.getColumnIndex(DBMetaData.LocationEntry.ADDITIONAL_INFO)));
            }
            if (c != null) {
                c.close();
            }
        }
        if (locationMessage == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(locationMessage);
        }
    }

    @Override
    public void saveDBData(@NonNull LocationMessage... locationMessages) {
        if (locationMessages != null && locationMessages.length > 0) {
            for (LocationMessage locationMessage : locationMessages) {
                final ContentValues values = new ContentValues();
                values.put(DBMetaData.LocationEntry.ALARM, locationMessage.getAlarmFlag());
                values.put(DBMetaData.LocationEntry.DIRECTION, locationMessage.getDirection());
                values.put(DBMetaData.LocationEntry.LATITUDE, locationMessage.getLatitude());
                values.put(DBMetaData.LocationEntry.LONGITUDE, locationMessage.getLongitude());
                values.put(DBMetaData.LocationEntry.ELEVATION, locationMessage.getElevation());
                values.put(DBMetaData.LocationEntry.SPEED, locationMessage.getSpeed());
                values.put(DBMetaData.LocationEntry.STATE, locationMessage.getState());
                values.put(DBMetaData.LocationEntry.TIME, locationMessage.getTime());
                values.put(DBMetaData.LocationEntry.UPLOAD_TIME, locationMessage.getUploadTime());
                values.put(DBMetaData.LocationEntry.SERIAL_NUMBER, locationMessage.getSerialNumber());
                values.put(DBMetaData.LocationEntry.ADDITIONAL_INFO, locationMessage.getAdditionalInfo());
                getAllDBDatas(new LoadLocationsCallBack() {
                    @Override
                    public void onDBBaseDataLoaded(List<LocationMessage> buffers) {
                        int size = buffers.size();
                        if (size > LOCATION_MAX_SAVE) {
                            String selection = DBMetaData.LocationEntry._ID + "=?";
                            String[] selectionArgs = {
                                    buffers.get(0).getId() + ""
                            };
                            resolver.update(CONTENT_URI, values, selection, selectionArgs);
                            values.clear();
                        } else {
                            resolver.insert(CONTENT_URI, values);
                            values.clear();
                        }
                    }

                    @Override
                    public void onDataNotAailable() {
                        resolver.insert(CONTENT_URI, values);
                        values.clear();
                    }
                });

            }
        }
    }

    @Override
    public void deleteAllDBDatas() {
        resolver.delete(CONTENT_URI, null, null);
    }

    @Override
    public void deleteDBData(@NonNull int id) {
        String selection = DBMetaData.LocationEntry._ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        synchronized (this) {
            resolver.delete(CONTENT_URI, selection, selectionArgs);
        }
    }
}
