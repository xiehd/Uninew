package com.uninew.car.db.dialler;

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
 * Created by Administrator on 2017/9/11 0011.
 */

public class DialerLocalDataSource implements DialerLocalSource {

    private static volatile DialerLocalDataSource INSTANCE;
    private WeakReference<Context> wp;//防止context内存泄露的优化
    /**
     * contentprovider连接文本信息的uri
     **/
    private static final Uri CONTENT_URI = Uri.parse("content://" + DBMetaData.AUTHORITIES + "/"
            + DBMetaData.DiallerEntry.TABLE_NAME + "/");
    private ContentResolver resolver;//ContentProvider操作类


    private DialerLocalDataSource(Context context) {
        wp = new WeakReference<>(context);
        resolver = wp.get().getContentResolver();
    }

    public static DialerLocalDataSource getInstance(Context context) {
        if (INSTANCE != null) {

        } else {
            synchronized (DialerLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DialerLocalDataSource(context);
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public void getAllDiallersByState(int state, LoadDialersCallback callBack) {
        List<Dialer> dialers = new ArrayList<>();
        String selection = DBMetaData.DiallerEntry.DIALLER_STATE + "=?";
        String[] selectionArgs = {
                state + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.DiallerEntry.CALL_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                Dialer dialer = new Dialer();
                dialer.setId(c.getInt(c.getColumnIndex(DBMetaData.DiallerEntry._ID)));
                dialer.setCallDate(c.getString(c.getColumnIndex(DBMetaData.DiallerEntry.CALL_TIME)));
                dialer.setCallTime(c.getInt(c.getColumnIndex(DBMetaData.DiallerEntry.CALL_DURATION)));
                dialer.setContact(c.getString(c.getColumnIndex(DBMetaData.DiallerEntry.CONTACT)));
                dialer.setDiallerState(c.getInt(c.getColumnIndex(DBMetaData.DiallerEntry.DIALLER_STATE)));
                dialer.setPhone(c.getString(c.getColumnIndex(DBMetaData.DiallerEntry.PHONE)));
                dialers.add(dialer);
            }
        }
        if (c != null) {
            c.close();
        }
        if (dialers.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(dialers);
        }
    }

    @Override
    public void getAllDBDatas(@NonNull LoadDBDataCallBack callBack) {
        List<Dialer> dialers = new ArrayList<>();
        Cursor c = resolver.query(CONTENT_URI, null, null, null, DBMetaData.DiallerEntry.CALL_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                Dialer dialer = new Dialer();
                dialer.setId(c.getInt(c.getColumnIndex(DBMetaData.DiallerEntry._ID)));
                dialer.setCallDate(c.getString(c.getColumnIndex(DBMetaData.DiallerEntry.CALL_TIME)));
                dialer.setCallTime(c.getInt(c.getColumnIndex(DBMetaData.DiallerEntry.CALL_DURATION)));
                dialer.setContact(c.getString(c.getColumnIndex(DBMetaData.DiallerEntry.CONTACT)));
                dialer.setDiallerState(c.getInt(c.getColumnIndex(DBMetaData.DiallerEntry.DIALLER_STATE)));
                dialer.setPhone(c.getString(c.getColumnIndex(DBMetaData.DiallerEntry.PHONE)));
                dialers.add(dialer);
            }
        }
        if (c != null) {
            c.close();
        }
        if (dialers.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(dialers);
        }
    }

    @Override
    public void getDBData(int _id, @NonNull GetDBDataCallBack callBack) {
        Dialer dialer = null;
        String selection = DBMetaData.DiallerEntry._ID + "=?";
        String[] selectionArgs = {
                _id + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.DiallerEntry.CALL_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                dialer = new Dialer();
                dialer.setId(c.getInt(c.getColumnIndex(DBMetaData.DiallerEntry._ID)));
                dialer.setCallDate(c.getString(c.getColumnIndex(DBMetaData.DiallerEntry.CALL_TIME)));
                dialer.setCallTime(c.getInt(c.getColumnIndex(DBMetaData.DiallerEntry.CALL_DURATION)));
                dialer.setContact(c.getString(c.getColumnIndex(DBMetaData.DiallerEntry.CONTACT)));
                dialer.setDiallerState(c.getInt(c.getColumnIndex(DBMetaData.DiallerEntry.DIALLER_STATE)));
                dialer.setPhone(c.getString(c.getColumnIndex(DBMetaData.DiallerEntry.PHONE)));
            }
        }
        if (c != null) {
            c.close();
        }
        if (dialer == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(dialer);
        }
    }

    @Override
    public void saveDBData(@NonNull Dialer... dialers) {
        if (dialers != null && dialers.length > 0) {
            for (Dialer dialer : dialers) {
                ContentValues values = new ContentValues();
                values.put(DBMetaData.DiallerEntry.CALL_DURATION, dialer.getCallTime());
                values.put(DBMetaData.DiallerEntry.CALL_TIME, dialer.getCallDate());
                values.put(DBMetaData.DiallerEntry.CONTACT, dialer.getContact());
                values.put(DBMetaData.DiallerEntry.DIALLER_STATE, dialer.getDiallerState());
                values.put(DBMetaData.DiallerEntry.PHONE, dialer.getPhone());
                resolver.insert(CONTENT_URI, values);
            }
        }

    }

    @Override
    public void deleteAllDBDatas() {
        resolver.delete(CONTENT_URI, null, null);
    }

    @Override
    public void deleteDBData(@NonNull int id) {
        String selection = DBMetaData.DiallerEntry._ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        resolver.delete(CONTENT_URI, selection, selectionArgs);
    }
}
