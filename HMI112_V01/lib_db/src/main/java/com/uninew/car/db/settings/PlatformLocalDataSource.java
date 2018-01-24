package com.uninew.car.db.settings;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.uninew.car.db.main.DBMetaData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import tools.StringsTool;

import static android.R.attr.id;

/**
 * Created by Administrator on 2017/9/5 0005.
 */

public class PlatformLocalDataSource implements PlatformLocalSource {


    private static final String TAG = "PlatformLocalDataSource";


    private static volatile PlatformLocalDataSource INSTANCE;
    private WeakReference<Context> wp;//防止context内存泄露的优化
    /**
     * contentprovider连接文本信息的uri
     **/
    private static final Uri CONTENT_URI = Uri.parse("content://" + DBMetaData.AUTHORITIES + "/"
            + DBMetaData.PlatformEntry.TABLE_NAME + "/");
    private ContentResolver resolver;//ContentProvider操作类

    private ChangePlatformListener mListener;

    private PlatformLocalDataSource(Context context) {
        wp = new WeakReference<>(context);
        resolver = wp.get().getContentResolver();
    }

    public static PlatformLocalDataSource getInstance(Context context) {
        if (INSTANCE != null) {

        } else {
            synchronized (BaseLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PlatformLocalDataSource(context);
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
            if (key != -1 && mListener != null) {
                String selection = DBMetaData.PlatformEntry._ID + "=?";
                String[] selectionArgs = {
                        key + ""
                };
                PlatformSettings platformSettings = null;
                Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, null);
                if (c != null && c.getCount() > 0) {
                    c.moveToFirst();
                    int tcp_id = c.getInt(c.getColumnIndex(DBMetaData.PlatformEntry.TCP_ID));
                    String mainIp = c.getString(c.getColumnIndex(DBMetaData.PlatformEntry.MAIN_IP));
                    int mainPort = c.getInt(c.getColumnIndex(DBMetaData.PlatformEntry.MAIN_PORT));
                    String spareIp = c.getString(c.getColumnIndex(DBMetaData.PlatformEntry.SPARE_IP));
                    int sparePort = c.getInt(c.getColumnIndex(DBMetaData.PlatformEntry.SPARE_PORT));
                    platformSettings = new PlatformSettings();
                    platformSettings.setTvpId(tcp_id);
                    platformSettings.setMainIp(mainIp);
                    platformSettings.setMainPort(mainPort);
                    platformSettings.setSpareIp(spareIp);
                    platformSettings.setSparePort(sparePort);
                }
                if (c != null) {
                    c.close();
                }
                if (platformSettings != null && mListener != null) {
                    mListener.onChangePlatsourMessage(platformSettings);
                }
            }
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }
    };


    @Override
    public void getAllDBDatas(@NonNull LoadDBDataCallBack callBack) {
        List<PlatformSettings> platformSettingses = new ArrayList<>();
        Cursor c = resolver.query(CONTENT_URI, null, null, null, DBMetaData.PlatformEntry._ID + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int tcp_id = c.getInt(c.getColumnIndex(DBMetaData.PlatformEntry.TCP_ID));
                String mainIp = c.getString(c.getColumnIndex(DBMetaData.PlatformEntry.MAIN_IP));
                int mainPort = c.getInt(c.getColumnIndex(DBMetaData.PlatformEntry.MAIN_PORT));
                String spareIp = c.getString(c.getColumnIndex(DBMetaData.PlatformEntry.SPARE_IP));
                int sparePort = c.getInt(c.getColumnIndex(DBMetaData.PlatformEntry.SPARE_PORT));
                PlatformSettings platformSettings = new PlatformSettings();
                platformSettings.setTvpId(tcp_id);
                platformSettings.setMainIp(mainIp);
                platformSettings.setMainPort(mainPort);
                platformSettings.setSpareIp(spareIp);
                platformSettings.setSparePort(sparePort);
                platformSettingses.add(platformSettings);
            }
        }
        if (c != null) {
            c.close();
        }
        if (platformSettingses.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(platformSettingses);
        }
    }

    @Override
    public void getDBData(int _id, @NonNull GetDBDataCallBack callBack) {
        PlatformSettings platformSettings = getPlatform(_id);
        if (platformSettings == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(platformSettings);
        }
    }

    @Override
    public void saveDBData(@NonNull PlatformSettings... platformSettingses) {
        if (platformSettingses != null && platformSettingses.length > 0) {
            for (PlatformSettings platformSettings : platformSettingses) {
                int id = platformSettings.getTvpId();
                String selection = DBMetaData.PlatformEntry.TCP_ID + "=?";
                String[] selectionArgs = {
                        id + ""
                };
                ContentValues values = new ContentValues();
                values.put(DBMetaData.PlatformEntry.TCP_ID, id);
                if(!TextUtils.isEmpty(platformSettings.getMainIp())) {
                    values.put(DBMetaData.PlatformEntry.MAIN_IP, platformSettings.getMainIp());
                }
                if( platformSettings.getMainPort() > 0) {
                    values.put(DBMetaData.PlatformEntry.MAIN_PORT, platformSettings.getMainPort());
                }
                if(!TextUtils.isEmpty(platformSettings.getSpareIp())) {
                    values.put(DBMetaData.PlatformEntry.SPARE_IP, platformSettings.getSpareIp());
                }
                if( platformSettings.getSparePort() > 0) {
                    values.put(DBMetaData.PlatformEntry.SPARE_PORT, platformSettings.getSparePort());
                }
                Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, null);
                if (c != null && c.getCount() > 0) {
                    resolver.update(CONTENT_URI, values, selection, selectionArgs);
                    values.clear();
                } else {
//                    ContentValues values = getDefaultValues(id);
                    resolver.insert(CONTENT_URI, values);
                    values.clear();
                }
                if (c != null) {
                    c.close();
                }
            }
        }
    }

    @Override
    @Deprecated
    public void deleteAllDBDatas() {

    }

    @Override
    @Deprecated
    public void deleteDBData(@NonNull int id) {
        String selection = DBMetaData.PlatformEntry.TCP_ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        resolver.delete(CONTENT_URI, selection, selectionArgs);
    }


    @Override
    public void registerNotify(@NonNull ChangePlatformListener listener) {
        mListener = listener;
        resolver.registerContentObserver(CONTENT_URI, true, observer);
    }

    @Override
    public void unregisterNotify() {
        resolver.unregisterContentObserver(observer);
        mListener = null;
    }

    @Override
    public void restoringDefault() {
        PlatformSettings platformSettings = getDefaultPlatform(SettingsDefaultValue.PlatformDefaultValue.SERVICE_PORT_1_ID);
        saveDBData(platformSettings);
        getAllDBDatas(new LoadPlaformsCallBack() {
            @Override
            public void onDBBaseDataLoaded(List<PlatformSettings> buffers) {
                if (buffers == null || buffers.isEmpty()) {
                    return;
                }
                for (PlatformSettings platformSettings : buffers) {
                    int tcpId = platformSettings.getTvpId();
                    if (tcpId != SettingsDefaultValue.PlatformDefaultValue.SERVICE_PORT_1_ID) {
                        deleteDBData(tcpId);
                    }
                }
            }

            @Override
            public void onDataNotAailable() {

            }
        });
    }

    @Override
    public void getService(int tcpId, @NonNull GetPlaformCallBack callBack) {
        PlatformSettings platformSettings = getPlatform(tcpId);
        if (platformSettings == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(platformSettings);
        }
    }

    @Override
    public void setService(@NonNull PlatformSettings platformSettings) {
        saveDBData(platformSettings);
    }


    private PlatformSettings getPlatform(int tcpId) {
        PlatformSettings platformSettings = null;
        String selection = DBMetaData.PlatformEntry.TCP_ID + "=?";
        String[] selectionArgs = {
                tcpId + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            int tcp_id = c.getInt(c.getColumnIndex(DBMetaData.PlatformEntry.TCP_ID));
            String mainIp = c.getString(c.getColumnIndex(DBMetaData.PlatformEntry.MAIN_IP));
            int mainPort = c.getInt(c.getColumnIndex(DBMetaData.PlatformEntry.MAIN_PORT));
            String spareIp = c.getString(c.getColumnIndex(DBMetaData.PlatformEntry.SPARE_IP));
            int sparePort = c.getInt(c.getColumnIndex(DBMetaData.PlatformEntry.SPARE_PORT));
            platformSettings = new PlatformSettings();
            platformSettings.setTvpId(tcp_id);
            platformSettings.setMainIp(mainIp);
            platformSettings.setMainPort(mainPort);
            platformSettings.setSpareIp(spareIp);
            platformSettings.setSparePort(sparePort);
        } else {
            if (tcpId == SettingsDefaultValue.PlatformDefaultValue.SERVICE_PORT_1_ID) {
                ContentValues values = getDefaultValues(tcpId);
                resolver.insert(CONTENT_URI, values);
                values.clear();
                platformSettings = getDefaultPlatform(tcpId);
            }
        }
        if (c != null) {
            c.close();
        }
        return platformSettings;
    }

    private ContentValues getDefaultValues(int tcpId) {
        ContentValues values = new ContentValues();
        values.put(DBMetaData.PlatformEntry.TCP_ID, tcpId);
        values.put(DBMetaData.PlatformEntry.MAIN_IP, SettingsDefaultValue.PlatformDefaultValue.SERVICE_IP_1_DEFAUL);
        values.put(DBMetaData.PlatformEntry.MAIN_PORT, SettingsDefaultValue.PlatformDefaultValue.SERVICE_PORT_1_DEFAUL);
        return values;
    }

    private PlatformSettings getDefaultPlatform(int tcpId) {
        PlatformSettings platformSettings = new PlatformSettings();
        platformSettings.setTvpId(tcpId);
        platformSettings.setMainIp(SettingsDefaultValue.PlatformDefaultValue.SERVICE_IP_1_DEFAUL);
        platformSettings.setMainPort(SettingsDefaultValue.PlatformDefaultValue.SERVICE_PORT_1_DEFAUL);
        return platformSettings;
    }
}
