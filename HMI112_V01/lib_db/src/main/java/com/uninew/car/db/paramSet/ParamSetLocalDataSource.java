package com.uninew.car.db.paramSet;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.uninew.car.db.main.DBMetaData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import tools.StringsTool;

/**
 * Created by Administrator on 2017/10/14 0014.
 */

public class ParamSetLocalDataSource implements ParamSetLocalSource {

    private static final String TAG = "ParamSetLocalDataSource";
    private static volatile ParamSetLocalDataSource INSTANCE;
    private WeakReference<Context> wp;//防止context内存泄露的优化
    /**
     * contentprovider连接文本信息的uri
     **/
    private static final Uri CONTENT_URI = Uri.parse("content://" + DBMetaData.AUTHORITIES + "/"
            + DBMetaData.ParamSetEntry.TABLE_NAME + "/");
    private ContentResolver resolver;//ContentProvider操作类

    private ParamSetLocalSource.ChangeParamSetListener mListener;

    private ParamSetLocalDataSource(Context context) {
        wp = new WeakReference<>(context);
        resolver = wp.get().getContentResolver();
    }

    public static ParamSetLocalDataSource getInstance(Context context) {
        if (INSTANCE != null) {

        } else {
            synchronized (ParamSetLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ParamSetLocalDataSource(context);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 创建一个contentprovider的监听类
     */
    private ContentObserver observer = new ContentObserver(new Handler()) {

        @SuppressLint("NewApi")
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Log.d(TAG, uri.getLastPathSegment());
            final String[] s = {uri.getLastPathSegment()};
            int key = StringsTool.srtToInteger(s[0]);
            if (key > -1 && mListener != null) {
                getDBData(key, new GetParamSetCallBack() {
                    @Override
                    public void onDBBaseDataLoaded(ParamSetting paramSetting) {
                        if (mListener != null) {
                            mListener.onChangePlatsourMessage(paramSetting);
                        }
                    }

                    @Override
                    public void onDataNotAailable() {

                    }
                });
            }
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }
    };


    @Override
    public void registerNotify(@NonNull ChangeParamSetListener listener) {
        this.mListener = listener;
        resolver.registerContentObserver(CONTENT_URI, true, observer);
    }

    @Override
    public void unregisterNotify() {
        resolver.unregisterContentObserver(observer);
        mListener = null;
    }

    @Override
    public void getParamSettesByKey(LoadParamSettesCallBack callBack, int... key) {
        List<ParamSetting> paramSettings = new ArrayList<>();
        String selection = null;
        String[] selectionArgs = null;
        if (key != null && key.length > 0) {
            int length = key.length;
            StringBuilder buffer = new StringBuilder();
            selectionArgs = new String[length];
            for (int i = 0; i < length; i++) {
                if (i == 0) {
                    buffer.append(DBMetaData.ParamSetEntry.KEY + "=?");
                } else {
                    buffer.append(DBMetaData.OR + DBMetaData.ParamSetEntry.KEY + "=?");
                }
                selectionArgs[i] = String.valueOf(key[i]);
            }
            selection = buffer.toString();
        }
        synchronized (this) {
            Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.ParamSetEntry.KEY + DBMetaData.ASC);
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    ParamSetting paramSetting = getParam(c);
                    paramSettings.add(paramSetting);
                }
            }
            if (c != null) {
                c.close();
            }
        }
        if (paramSettings.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(paramSettings);
        }
    }


    private ParamSetting getParam(Cursor c) {
        ParamSetting paramSetting = new ParamSetting();
        paramSetting.setId(c.getInt(c.getColumnIndex(DBMetaData.ParamSetEntry._ID)));
        paramSetting.setKey(c.getInt(c.getColumnIndex(DBMetaData.ParamSetEntry.KEY)));
        paramSetting.setLength(c.getInt(c.getColumnIndex(DBMetaData.ParamSetEntry.LANGTH)));
        paramSetting.setValue(c.getString(c.getColumnIndex(DBMetaData.ParamSetEntry.VALUE)));
        return paramSetting;
    }

    @Override
    public void getAllDBDatas(@NonNull LoadDBDataCallBack callBack) {
        List<ParamSetting> paramSettings = new ArrayList<>();
        Cursor c = resolver.query(CONTENT_URI, null, null, null, DBMetaData.ParamSetEntry.KEY + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                ParamSetting paramSetting = getParam(c);
                paramSettings.add(paramSetting);
            }
        }
        if (c != null) {
            c.close();
        }
        if (paramSettings.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(paramSettings);
        }
    }

    @Override
    public void getDBData(int _id, @NonNull GetDBDataCallBack callBack) {
        ParamSetting paramSetting = null;
        String selection = DBMetaData.ParamSetEntry._ID + "=?";
        String[] selectionArgs = {
                _id + ""
        };
        synchronized (this) {
            Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.ParamSetEntry.KEY + DBMetaData.ASC);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                paramSetting = getParam(c);
            }
            if (c != null) {
                c.close();
            }
        }
        if (paramSetting == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(paramSetting);
        }
    }

    @Override
    public void saveDBData(@NonNull ParamSetting... paramSettings) {
        if (paramSettings != null && paramSettings.length > 0) {
            for (ParamSetting paramSetting : paramSettings) {
                final int key = paramSetting.getKey();
                final ContentValues values = getValues(paramSetting);
                getParamSettesByKey(new LoadParamSettesCallBack() {
                    @Override
                    public void onDBBaseDataLoaded(List<ParamSetting> buffers) {
                        String selection = DBMetaData.ParamSetEntry.KEY + "=?";
                        String[] selectionArgs = {
                                key + ""
                        };
                        resolver.update(CONTENT_URI, values, selection, selectionArgs);
                    }

                    @Override
                    public void onDataNotAailable() {
                        resolver.insert(CONTENT_URI, values);
                    }
                }, key);
            }
        }
    }

    private ContentValues getValues(ParamSetting paramSetting) {
        ContentValues values = new ContentValues();
        values.put(DBMetaData.ParamSetEntry.KEY, paramSetting.getKey());
        values.put(DBMetaData.ParamSetEntry.VALUE, paramSetting.getValue());
        values.put(DBMetaData.ParamSetEntry.LANGTH, paramSetting.getLength());
        return values;
    }

    @Override
    public void deleteAllDBDatas() {
        resolver.delete(CONTENT_URI, null, null);
    }

    @Override
    public void deleteDBData(@NonNull int id) {
        String selection = DBMetaData.ParamSetEntry._ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        resolver.delete(CONTENT_URI, selection, selectionArgs);
    }
}
