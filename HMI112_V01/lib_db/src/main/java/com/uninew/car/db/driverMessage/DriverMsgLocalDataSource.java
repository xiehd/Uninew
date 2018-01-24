package com.uninew.car.db.driverMessage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.uninew.car.db.location.LocationLocalDataSource;
import com.uninew.car.db.main.DBBaseDataSource;
import com.uninew.car.db.main.DBMetaData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class DriverMsgLocalDataSource implements DriverMsgLocalSource {

    private static volatile DriverMsgLocalDataSource INSTANCE;
    private WeakReference<Context> wp;//防止context内存泄露的优化
    /**
     * contentprovider连接文本信息的uri
     **/
    private static final Uri CONTENT_URI = Uri.parse("content://" + DBMetaData.AUTHORITIES + "/"
            + DBMetaData.DriverMessageEntry.TABLE_NAME + "/");
    private ContentResolver resolver;//ContentProvider操作类

    private DriverMsgLocalDataSource(Context context) {
        wp = new WeakReference<>(context);
        resolver = wp.get().getContentResolver();
    }

    public static DriverMsgLocalDataSource getInstance(Context context) {
        if (INSTANCE != null) {

        } else {
            synchronized (DriverMsgLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DriverMsgLocalDataSource(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getDriverMsgByDriverCertificate(@NonNull String driverCertificate, @NonNull GetDriverMsgCallBack callBack) {
        if (TextUtils.isEmpty(driverCertificate)) {
            callBack.onDataNotAailable();
            return;
        }
        DriverMessage driverMessage = null;
        String selection = DBMetaData.DriverMessageEntry.DRIVER_CERTIFICATE + "=?";
        String[] selectionArgs = {
                driverCertificate
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.DriverMessageEntry._ID + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            driverMessage = new DriverMessage();
            driverMessage.setId(c.getInt(c.getColumnIndex(DBMetaData.DriverMessageEntry._ID)));
            driverMessage.setDriverCertificate(c.getString(c.getColumnIndex(DBMetaData.DriverMessageEntry.DRIVER_CERTIFICATE)));
            driverMessage.setDriverName(c.getString(c.getColumnIndex(DBMetaData.DriverMessageEntry.DRIVER_NAME)));
            driverMessage.setDriverPicture(c.getString(c.getColumnIndex(DBMetaData.DriverMessageEntry.DRIVER_PICTURE)));
            driverMessage.setDriverStar(c.getInt(c.getColumnIndex(DBMetaData.DriverMessageEntry.DRIVER_STAR)));
        }
        if (c != null) {
            c.close();
        }
        if (driverMessage == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(driverMessage);
        }
    }

    @Override
    public void getAllDBDatas(@NonNull LoadDBDataCallBack callBack) {
        List<DriverMessage> driverMessages = new ArrayList<>();
        Cursor c = resolver.query(CONTENT_URI, null, null, null, DBMetaData.DriverMessageEntry._ID + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                DriverMessage driverMessage = new DriverMessage();
                driverMessage.setId(c.getInt(c.getColumnIndex(DBMetaData.DriverMessageEntry._ID)));
                driverMessage.setDriverCertificate(c.getString(c.getColumnIndex(DBMetaData.DriverMessageEntry.DRIVER_CERTIFICATE)));
                driverMessage.setDriverName(c.getString(c.getColumnIndex(DBMetaData.DriverMessageEntry.DRIVER_NAME)));
                driverMessage.setDriverPicture(c.getString(c.getColumnIndex(DBMetaData.DriverMessageEntry.DRIVER_PICTURE)));
                driverMessage.setDriverStar(c.getInt(c.getColumnIndex(DBMetaData.DriverMessageEntry.DRIVER_STAR)));
                driverMessages.add(driverMessage);
            }
        }
        if (c != null) {
            c.close();
        }
        if (driverMessages.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(driverMessages);
        }
    }

    @Override
    public void getDBData(int _id, @NonNull GetDBDataCallBack callBack) {
        DriverMessage driverMessage = null;
        String selection = DBMetaData.DriverMessageEntry._ID + "=?";
        String[] selectionArgs = {
                _id + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.DriverMessageEntry._ID + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            driverMessage = new DriverMessage();
            driverMessage.setId(c.getInt(c.getColumnIndex(DBMetaData.DriverMessageEntry._ID)));
            driverMessage.setDriverCertificate(c.getString(c.getColumnIndex(DBMetaData.DriverMessageEntry.DRIVER_CERTIFICATE)));
            driverMessage.setDriverName(c.getString(c.getColumnIndex(DBMetaData.DriverMessageEntry.DRIVER_NAME)));
            driverMessage.setDriverPicture(c.getString(c.getColumnIndex(DBMetaData.DriverMessageEntry.DRIVER_PICTURE)));
            driverMessage.setDriverStar(c.getInt(c.getColumnIndex(DBMetaData.DriverMessageEntry.DRIVER_STAR)));
        }
        if (c != null) {
            c.close();
        }
        if (driverMessage == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(driverMessage);
        }
    }

    @Override
    public void saveDBData(@NonNull DriverMessage... driverMessages) {
        if(driverMessages != null && driverMessages.length > 0){
            for (DriverMessage driverMessage : driverMessages){
                final ContentValues values = new ContentValues();
                values.put(DBMetaData.DriverMessageEntry.DRIVER_CERTIFICATE,driverMessage.getDriverCertificate());
                values.put(DBMetaData.DriverMessageEntry.DRIVER_NAME,driverMessage.getDriverName());
                values.put(DBMetaData.DriverMessageEntry.DRIVER_PICTURE,driverMessage.getDriverPicture());
                values.put(DBMetaData.DriverMessageEntry.DRIVER_STAR,driverMessage.getDriverStar());
                getDriverMsgByDriverCertificate(driverMessage.getDriverCertificate(), new GetDriverMsgCallBack() {
                    @Override
                    public void onDBBaseDataLoaded(DriverMessage driverMessage) {
                        String selection = DBMetaData.DriverMessageEntry.DRIVER_CERTIFICATE + "=?";
                        String[] selectionArgs = {
                                driverMessage.getDriverCertificate()
                        };
                        resolver.update(CONTENT_URI,values,selection,selectionArgs);
                    }

                    @Override
                    public void onDataNotAailable() {
                        resolver.insert(CONTENT_URI,values);
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
        String selection = DBMetaData.DriverMessageEntry._ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        resolver.delete(CONTENT_URI, selection, selectionArgs);
    }
}
