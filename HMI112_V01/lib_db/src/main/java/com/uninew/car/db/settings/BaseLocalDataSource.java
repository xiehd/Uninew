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

/**
 * Created by Administrator on 2017/9/4 0004.
 */

public class BaseLocalDataSource implements BaseLocalSource {

    private static final String TAG = "BaseLocalDataSource";


    private static volatile BaseLocalDataSource INSTANCE;
    private WeakReference<Context> wp;//防止context内存泄露的优化
    /**
     * contentprovider连接文本信息的uri
     **/
    private static final Uri CONTENT_URI = Uri.parse("content://" + DBMetaData.AUTHORITIES + "/"
            + DBMetaData.SettingsEntry.TABLE_NAME + "/");
    private ContentResolver resolver;//ContentProvider操作类

    private ChangeBaseSettingsListener mListener;

    private ChangeSpeedSettingsListener speedSettingsListener;

    private ChangePowerSettingsListener powerSettingsListener;

    private BaseLocalDataSource(Context context) {
        wp = new WeakReference<>(context);
        resolver = wp.get().getContentResolver();
    }

    public static BaseLocalDataSource getInstance(Context context) {
        if (INSTANCE != null) {

        } else {
            synchronized (BaseLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BaseLocalDataSource(context);
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
            if (key != -1) {
                String value = null;
                String selection = DBMetaData.SettingsEntry._ID + "=?";
                String[] selectionArgs = {
                        key + ""
                };
                Cursor c = null;
                try {
                    c = resolver.query(CONTENT_URI, null, selection, selectionArgs, null);
                }catch (Exception e){
                    e.printStackTrace();
                    return;
                }
                if (c != null && c.getCount() > 0) {
                    c.moveToFirst();
                    value = c.getString(c.getColumnIndex(DBMetaData.SettingsEntry.CONTENT));
                } else {
                    ContentValues values = getDefaultValues(key);
                    resolver.insert(CONTENT_URI, values);
                    values.clear();
                    value = getDefaultContent(key);
                }
                if (c != null) {
                    c.close();
                }
                if (TextUtils.isEmpty(value)) {
                    return;
                }
                if (mListener != null) {
                    mListener.onChangeBaseSettings(key, value);
                    switch (key) {
                        case SettingsKeyValue.BaseKeyValue.COMPANY_NUMBER:
                            mListener.getCompanyNumber(value);
                            break;
                        case SettingsKeyValue.BaseKeyValue.DELAY_TIME:
                            mListener.getDelayTime(value);
                            break;
                        case SettingsKeyValue.BaseKeyValue.DVRSERIAL_NUMBER:
                            mListener.getDvrSerialNumber(value);
                            break;
                        case SettingsKeyValue.BaseKeyValue.OUTTIME_EXITE:
                            mListener.getOutTimeExite(value);
                            break;
                        case SettingsKeyValue.BaseKeyValue.PLATE_NUMBER:
                            mListener.getPlateNumber(value);
                            break;
                        case SettingsKeyValue.BaseKeyValue.PRERECORD_TIME:
                            mListener.getPrerecordTime(value);
                            break;
                        case SettingsKeyValue.BaseKeyValue.PRINT_SENSITIVITY:
                            mListener.getPrintSensitivity(value);
                            break;
                        case SettingsKeyValue.BaseKeyValue.TERMINAL_NUMBER:
                            mListener.getTerminalNumber(value);
                            break;
                    }
                }
                if (speedSettingsListener != null) {
                    speedSettingsListener.onChangeSpeedSettings(key, value);
                    switch (key) {
                        case SettingsKeyValue.SpeedSettingsKeyValue.ALARM_SPEED_MAX:
                            speedSettingsListener.setAlarm_speed_max(StringsTool.srtToInteger(value));
                            break;
                        case SettingsKeyValue.SpeedSettingsKeyValue.ALARM_SPEED_TIME:
                            speedSettingsListener.setAlarm_speed_time(StringsTool.srtToInteger(value));
                            break;
                        case SettingsKeyValue.SpeedSettingsKeyValue.PRE_ALARM_SPEED_MAX:
                            speedSettingsListener.setPre_alarm_speed_max(StringsTool.srtToInteger(value));
                            break;
                        case SettingsKeyValue.SpeedSettingsKeyValue.PRE_ALARM_SPEED_TIME:
                            speedSettingsListener.setPre_alarm_speed_time(StringsTool.srtToInteger(value));
                            break;
                        case SettingsKeyValue.SpeedSettingsKeyValue.SPEED_SOURCE:
                            speedSettingsListener.setSpeed_source(StringsTool.srtToInteger(value));
                            break;
                        case SettingsKeyValue.SpeedSettingsKeyValue.SPEED_UNIT:
                            speedSettingsListener.setSpeed_unit(StringsTool.srtToInteger(value));
                            break;
                    }
                }
                if (powerSettingsListener != null) {
                    powerSettingsListener.onChangeSpeedSettings(key, value);
                    switch (key) {
                        case SettingsKeyValue.PowerSettingsKeyValue.ACC_MODE:
                            powerSettingsListener.setAcc_mode(StringsTool.srtToInteger(value));
                            break;
                        case SettingsKeyValue.PowerSettingsKeyValue.OPEN_MODE:
                            powerSettingsListener.setOpen_mode(StringsTool.srtToInteger(value));
                            break;
                        case SettingsKeyValue.PowerSettingsKeyValue.LOWVOLTAGE:
                            powerSettingsListener.setLowVoltage(StringsTool.srtToFloat(value));
                            break;
                        case SettingsKeyValue.PowerSettingsKeyValue.LOWVOLTAGE_PROTECTION:
                            powerSettingsListener.setLowVoltage_protection(StringsTool.srtToBoolean(value));
                            break;
                        case SettingsKeyValue.PowerSettingsKeyValue.RESTORE_VOLTAGE:
                            powerSettingsListener.setRestore_voltage(StringsTool.srtToFloat(value));
                            break;
                        case SettingsKeyValue.PowerSettingsKeyValue.UNDERVOLTAGE_ALARM:
                            powerSettingsListener.setUndervoltage_alarm(StringsTool.srtToBoolean(value));
                            break;
                        case SettingsKeyValue.PowerSettingsKeyValue.SHUTDOWN_DELAY:
                            powerSettingsListener.setShutdown_Delay(StringsTool.srtToInteger(value));
                            break;
                    }
                }
            }
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }
    };

    private boolean isRegister = false;

    @Override
    public void registerNotify(@NonNull ChangeSpeedSettingsListener listener) {
        this.speedSettingsListener = listener;
        if (isRegister == false) {
            resolver.registerContentObserver(CONTENT_URI, true, observer);
            isRegister = true;
        }
    }

    @Override
    public void registerNotify(@NonNull ChangePowerSettingsListener listener) {
        this.powerSettingsListener = listener;
        if (isRegister == false) {
            resolver.registerContentObserver(CONTENT_URI, true, observer);
            isRegister = true;
        }
    }

    @Override
    public void registerNotify(@NonNull ChangeBaseSettingsListener listener) {
        this.mListener = listener;
        if (isRegister == false) {
            resolver.registerContentObserver(CONTENT_URI, true, observer);
            isRegister = true;
        }
    }

    @Override
    public void unregisterNotify() {
        if (isRegister) {
            resolver.unregisterContentObserver(observer);
            isRegister = false;
        }
        mListener = null;
        speedSettingsListener = null;
        powerSettingsListener = null;
    }

    @Override
    public void saveBaseSettings(int id, @NonNull String value) {
        String selection = DBMetaData.SettingsEntry._ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, null);
        if (c != null && c.getCount() > 0) {
            ContentValues values = new ContentValues();
            values.put(DBMetaData.SettingsEntry.CONTENT, value);
            resolver.update(CONTENT_URI, values, selection, selectionArgs);
        } else {
            ContentValues values = getValues(id, value);
            resolver.insert(CONTENT_URI, values);
            values.clear();
        }
        if (c != null) {
            c.close();
        }
    }

    @Override
    public void restoringDefault() {
        saveBaseSettings(SettingsKeyValue.BaseKeyValue.COMPANY_NUMBER,
                SettingsDefaultValue.BaseDefaultValue.COMPANY_NUMBER_DEFAUL);
        saveBaseSettings(SettingsKeyValue.BaseKeyValue.DELAY_TIME,
                SettingsDefaultValue.BaseDefaultValue.DELAY_TIME_DEFAUL + "");
        saveBaseSettings(SettingsKeyValue.BaseKeyValue.DVRSERIAL_NUMBER,
                SettingsDefaultValue.BaseDefaultValue.DVRSERIAL_NUMBER_DEFAUL);
        saveBaseSettings(SettingsKeyValue.BaseKeyValue.OUTTIME_EXITE,
                SettingsDefaultValue.BaseDefaultValue.OUTTIME_EXITE_DEFAUL + "");
        saveBaseSettings(SettingsKeyValue.BaseKeyValue.PLATE_NUMBER,
                SettingsDefaultValue.BaseDefaultValue.PLATE_NUMBER_DEFAUL);
        saveBaseSettings(SettingsKeyValue.BaseKeyValue.PRERECORD_TIME,
                SettingsDefaultValue.BaseDefaultValue.PRERECORD_TIME_DEFAUL + "");
        saveBaseSettings(SettingsKeyValue.BaseKeyValue.PRINT_SENSITIVITY,
                SettingsDefaultValue.BaseDefaultValue.PRINT_SENSITIVITY_DEFAUL + "");
        saveBaseSettings(SettingsKeyValue.BaseKeyValue.TERMINAL_NUMBER,
                SettingsDefaultValue.BaseDefaultValue.TERMINAL_NUMBER_DEFAUL);
    }


    @Override
    public void getPlateNumber(@NonNull GetBaseSettingCallBack callBack) {
        int id = SettingsKeyValue.BaseKeyValue.PLATE_NUMBER;
        getDBData(id, callBack);
    }

    @Override
    public void getTerminalNumber(@NonNull GetBaseSettingCallBack callBack) {
        int id = SettingsKeyValue.BaseKeyValue.TERMINAL_NUMBER;
        getDBData(id, callBack);
    }

    @Override
    public void getCompanyNumber(@NonNull GetBaseSettingCallBack callBack) {
        int id = SettingsKeyValue.BaseKeyValue.COMPANY_NUMBER;
        getDBData(id, callBack);
    }

    @Override
    public void getDvrSerialNumber(@NonNull GetBaseSettingCallBack callBack) {
        int id = SettingsKeyValue.BaseKeyValue.DVRSERIAL_NUMBER;
        getDBData(id, callBack);
    }

    @Override
    public void getOutTimeExite(@NonNull GetBaseSettingCallBack callBack) {
        int id = SettingsKeyValue.BaseKeyValue.OUTTIME_EXITE;
        getDBData(id, callBack);
    }

    @Override
    public void getPrintSensitivity(@NonNull GetBaseSettingCallBack callBack) {
        int id = SettingsKeyValue.BaseKeyValue.PRINT_SENSITIVITY;
        getDBData(id, callBack);
    }

    @Override
    public void getPrerecordTime(@NonNull GetBaseSettingCallBack callBack) {
        int id = SettingsKeyValue.BaseKeyValue.PRERECORD_TIME;
        getDBData(id, callBack);
    }

    @Override
    public void getDelayTime(@NonNull GetBaseSettingCallBack callBack) {
        int id = SettingsKeyValue.BaseKeyValue.DELAY_TIME;
        getDBData(id, callBack);
    }

    @Override
    public void setPlateNumber(@NonNull String value) {
        int id = SettingsKeyValue.BaseKeyValue.PLATE_NUMBER;
        saveBaseSettings(id, value);
    }

    @Override
    public void setTerminalNumber(@NonNull String value) {
        int id = SettingsKeyValue.BaseKeyValue.TERMINAL_NUMBER;
        saveBaseSettings(id, value);
    }

    @Override
    public void setCompanyNumber(@NonNull String value) {
        int id = SettingsKeyValue.BaseKeyValue.COMPANY_NUMBER;
        saveBaseSettings(id, value);
    }

    @Override
    public void setDvrSerialNumber(@NonNull String value) {
        int id = SettingsKeyValue.BaseKeyValue.DVRSERIAL_NUMBER;
        saveBaseSettings(id, value);
    }

    @Override
    public void setOutTimeExite(@NonNull String value) {
        int id = SettingsKeyValue.BaseKeyValue.OUTTIME_EXITE;
        saveBaseSettings(id, value);
    }

    @Override
    public void setPrintSensitivity(@NonNull String value) {
        int id = SettingsKeyValue.BaseKeyValue.PRINT_SENSITIVITY;
        saveBaseSettings(id, value);
    }

    @Override
    public void setPrerecordTime(@NonNull String value) {
        int id = SettingsKeyValue.BaseKeyValue.PRERECORD_TIME;
        saveBaseSettings(id, value);
    }

    @Override
    public void setDelayTime(@NonNull String value) {
        int id = SettingsKeyValue.BaseKeyValue.DELAY_TIME;
        saveBaseSettings(id, value);
    }

    @Override
    public void getAllDBDatas(@NonNull LoadDBDataCallBack callBack) {
        List<BaseSettings> baseSettingses = new ArrayList<>();
        BaseSettings baseSettings = new BaseSettings();
        String selection = DBMetaData.SettingsEntry._ID + "=?"
                + DBMetaData.OR + DBMetaData.SettingsEntry._ID + "=?"
                + DBMetaData.OR + DBMetaData.SettingsEntry._ID + "=?"
                + DBMetaData.OR + DBMetaData.SettingsEntry._ID + "=?"
                + DBMetaData.OR + DBMetaData.SettingsEntry._ID + "=?"
                + DBMetaData.OR + DBMetaData.SettingsEntry._ID + "=?"
                + DBMetaData.OR + DBMetaData.SettingsEntry._ID + "=?"
                + DBMetaData.OR + DBMetaData.SettingsEntry._ID + "=?";
        String[] selectionArgs = {
                SettingsKeyValue.BaseKeyValue.COMPANY_NUMBER + "",
                SettingsKeyValue.BaseKeyValue.DELAY_TIME + "",
                SettingsKeyValue.BaseKeyValue.DVRSERIAL_NUMBER + "",
                SettingsKeyValue.BaseKeyValue.OUTTIME_EXITE + "",
                SettingsKeyValue.BaseKeyValue.PLATE_NUMBER + "",
                SettingsKeyValue.BaseKeyValue.PRERECORD_TIME + "",
                SettingsKeyValue.BaseKeyValue.PRINT_SENSITIVITY + "",
                SettingsKeyValue.BaseKeyValue.TERMINAL_NUMBER + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, null);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndex(DBMetaData.SettingsEntry._ID));
                String content = c.getString(c.getColumnIndex(DBMetaData.SettingsEntry.CONTENT));
                switch (id) {
                    case SettingsKeyValue.BaseKeyValue.COMPANY_NUMBER:
                        baseSettings.setCompanyNumber(content);
                        break;
                    case SettingsKeyValue.BaseKeyValue.DELAY_TIME:
                        baseSettings.setDelayTime(StringsTool.srtToInteger(content));
                        break;
                    case SettingsKeyValue.BaseKeyValue.DVRSERIAL_NUMBER:
                        baseSettings.setDvrSerialNumber(content);
                        break;
                    case SettingsKeyValue.BaseKeyValue.OUTTIME_EXITE:
                        baseSettings.setOutTimeExite(StringsTool.srtToInteger(content.trim()));
                        break;
                    case SettingsKeyValue.BaseKeyValue.PLATE_NUMBER:
                        baseSettings.setPlateNumber(content);
                        break;
                    case SettingsKeyValue.BaseKeyValue.PRERECORD_TIME:
                        baseSettings.setPrerecordTime(StringsTool.srtToInteger(content));
                        break;
                    case SettingsKeyValue.BaseKeyValue.PRINT_SENSITIVITY:
                        baseSettings.setPrintSensitivity(StringsTool.srtToInteger(content));
                        break;
                    case SettingsKeyValue.BaseKeyValue.TERMINAL_NUMBER:
                        baseSettings.setTerminalNumber(content);
                        break;
                }
            }
        } else {
            ContentValues values = new ContentValues();
            values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.COMPANY_NUMBER);
            values.put(DBMetaData.SettingsEntry.CONTENT,
                    SettingsDefaultValue.BaseDefaultValue.COMPANY_NUMBER_DEFAUL);
            values.put(DBMetaData.SettingsEntry.NAME,
                    SettingsDefaultValue.BaseDefaultValue.COMPANY_NUMBER_NAME);
            resolver.insert(CONTENT_URI, values);
            values.clear();
            values = new ContentValues();
            values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.DELAY_TIME);
            values.put(DBMetaData.SettingsEntry.CONTENT,
                    SettingsDefaultValue.BaseDefaultValue.DELAY_TIME_DEFAUL);
            values.put(DBMetaData.SettingsEntry.NAME,
                    SettingsDefaultValue.BaseDefaultValue.DELAY_TIME_NAME);
            resolver.insert(CONTENT_URI, values);
            values.clear();
            values = new ContentValues();
            values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.DVRSERIAL_NUMBER);
            values.put(DBMetaData.SettingsEntry.CONTENT,
                    SettingsDefaultValue.BaseDefaultValue.DVRSERIAL_NUMBER_DEFAUL);
            values.put(DBMetaData.SettingsEntry.NAME,
                    SettingsDefaultValue.BaseDefaultValue.DVRSERIAL_NUMBER_NAME);
            resolver.insert(CONTENT_URI, values);
            values.clear();
            values = new ContentValues();
            values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.OUTTIME_EXITE);
            values.put(DBMetaData.SettingsEntry.CONTENT,
                    SettingsDefaultValue.BaseDefaultValue.OUTTIME_EXITE_DEFAUL);
            values.put(DBMetaData.SettingsEntry.NAME,
                    SettingsDefaultValue.BaseDefaultValue.OUTTIME_EXITE_NAME);
            resolver.insert(CONTENT_URI, values);
            values.clear();
            values = new ContentValues();
            values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.PLATE_NUMBER);
            values.put(DBMetaData.SettingsEntry.CONTENT,
                    SettingsDefaultValue.BaseDefaultValue.PLATE_NUMBER_DEFAUL);
            values.put(DBMetaData.SettingsEntry.NAME,
                    SettingsDefaultValue.BaseDefaultValue.PLATE_NUMBER_NAME);
            resolver.insert(CONTENT_URI, values);
            values.clear();
            values = new ContentValues();
            values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.PRERECORD_TIME);
            values.put(DBMetaData.SettingsEntry.CONTENT,
                    SettingsDefaultValue.BaseDefaultValue.PRERECORD_TIME_DEFAUL);
            values.put(DBMetaData.SettingsEntry.NAME,
                    SettingsDefaultValue.BaseDefaultValue.PRERECORD_TIME_NAME);
            resolver.insert(CONTENT_URI, values);
            values.clear();
            values = new ContentValues();
            values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.PRINT_SENSITIVITY);
            values.put(DBMetaData.SettingsEntry.CONTENT,
                    SettingsDefaultValue.BaseDefaultValue.PRINT_SENSITIVITY_DEFAUL);
            values.put(DBMetaData.SettingsEntry.NAME,
                    SettingsDefaultValue.BaseDefaultValue.PRINT_SENSITIVITY_NAME);
            resolver.insert(CONTENT_URI, values);
            values.clear();
            values = new ContentValues();
            values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.TERMINAL_NUMBER);
            values.put(DBMetaData.SettingsEntry.CONTENT,
                    SettingsDefaultValue.BaseDefaultValue.TERMINAL_NUMBER_DEFAUL);
            values.put(DBMetaData.SettingsEntry.NAME,
                    SettingsDefaultValue.BaseDefaultValue.TERMINAL_NUMBER_NAME);
            resolver.insert(CONTENT_URI, values);
            values.clear();
            baseSettings.setCompanyNumber(SettingsDefaultValue.BaseDefaultValue.COMPANY_NUMBER_DEFAUL);
            baseSettings.setDelayTime(SettingsDefaultValue.BaseDefaultValue.DELAY_TIME_DEFAUL);
            baseSettings.setDvrSerialNumber(SettingsDefaultValue.BaseDefaultValue.DVRSERIAL_NUMBER_DEFAUL);
            baseSettings.setOutTimeExite(SettingsDefaultValue.BaseDefaultValue.OUTTIME_EXITE_DEFAUL);
            baseSettings.setPlateNumber(SettingsDefaultValue.BaseDefaultValue.PLATE_NUMBER_DEFAUL);
            baseSettings.setPrerecordTime(SettingsDefaultValue.BaseDefaultValue.PRERECORD_TIME_DEFAUL);
            baseSettings.setPrintSensitivity(SettingsDefaultValue.BaseDefaultValue.PRINT_SENSITIVITY_DEFAUL);
            baseSettings.setTerminalNumber(SettingsDefaultValue.BaseDefaultValue.TERMINAL_NUMBER_DEFAUL);
        }
        if (c != null) {
            c.close();
        }
        baseSettingses.add(baseSettings);
        if (baseSettingses.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(baseSettingses);
        }
    }

    @Override
    public void getDBData(int _id, @NonNull GetDBDataCallBack callBack) {
        String value = null;
        String selection = DBMetaData.SettingsEntry._ID + "=?";
        String[] selectionArgs = {
                _id + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            value = c.getString(c.getColumnIndex(DBMetaData.SettingsEntry.CONTENT));
        } else {
            if (c != null) {
                c.close();
                c = null;
            }
            ContentValues values = getDefaultValues(_id);
            resolver.insert(CONTENT_URI, values);
            values.clear();
            value = getDefaultContent(_id);
        }
        if (c != null) {
            c.close();
        }
        if (value == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(value);
        }
    }

    @Override
    @Deprecated
    public void saveDBData(@NonNull BaseSettings... t) {

    }


    @Override
    @Deprecated
    public void deleteAllDBDatas() {

    }

    @Override
    @Deprecated
    public void deleteDBData(@NonNull int id) {

    }

//    private String getDBContent(int id) {
//        String value = null;
//        String selection = DBMetaData.SettingsEntry._ID + "=?";
//        String[] selectionArgs = {
//                id + ""
//        };
//        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, null);
//        if (c != null && c.getCount() > 0) {
//            c.moveToFirst();
//            value = c.getString(c.getColumnIndex(DBMetaData.SettingsEntry.CONTENT));
//        } else {
//            ContentValues values = getDefaultValues(id);
//            resolver.insert(CONTENT_URI, values);
//            values.clear();
//            value = getDefaultContent(id);
//        }
//        if (c != null) {
//            c.close();
//        }
//        return value;
//    }

    private ContentValues getValues(int id, String value) {
        ContentValues values = new ContentValues();
        switch (id) {
            case SettingsKeyValue.BaseKeyValue.COMPANY_NUMBER:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.COMPANY_NUMBER);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        value);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.BaseDefaultValue.COMPANY_NUMBER_NAME);
                break;
            case SettingsKeyValue.BaseKeyValue.DELAY_TIME:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.DELAY_TIME);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        value);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.BaseDefaultValue.DELAY_TIME_NAME);
                break;
            case SettingsKeyValue.BaseKeyValue.DVRSERIAL_NUMBER:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.DVRSERIAL_NUMBER);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        value);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.BaseDefaultValue.DVRSERIAL_NUMBER_NAME);
                break;
            case SettingsKeyValue.BaseKeyValue.OUTTIME_EXITE:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.OUTTIME_EXITE);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        value);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.BaseDefaultValue.OUTTIME_EXITE_NAME);
                break;
            case SettingsKeyValue.BaseKeyValue.PLATE_NUMBER:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.PLATE_NUMBER);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        value);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.BaseDefaultValue.PLATE_NUMBER_NAME);
                break;
            case SettingsKeyValue.BaseKeyValue.PRERECORD_TIME:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.PRERECORD_TIME);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        value);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.BaseDefaultValue.PRERECORD_TIME_NAME);
                break;
            case SettingsKeyValue.BaseKeyValue.PRINT_SENSITIVITY:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.PRINT_SENSITIVITY);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        value);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.BaseDefaultValue.PRINT_SENSITIVITY_NAME);
                break;
            case SettingsKeyValue.BaseKeyValue.TERMINAL_NUMBER:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.TERMINAL_NUMBER);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        value);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.BaseDefaultValue.TERMINAL_NUMBER_NAME);
                break;
        }
        return values;
    }

    private ContentValues getDefaultValues(int id) {
        ContentValues values = new ContentValues();
        switch (id) {
            case SettingsKeyValue.BaseKeyValue.COMPANY_NUMBER:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.COMPANY_NUMBER);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.BaseDefaultValue.COMPANY_NUMBER_DEFAUL);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.BaseDefaultValue.COMPANY_NUMBER_NAME);
                break;
            case SettingsKeyValue.BaseKeyValue.DELAY_TIME:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.DELAY_TIME);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.BaseDefaultValue.DELAY_TIME_DEFAUL);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.BaseDefaultValue.DELAY_TIME_NAME);
                break;
            case SettingsKeyValue.BaseKeyValue.DVRSERIAL_NUMBER:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.DVRSERIAL_NUMBER);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.BaseDefaultValue.DVRSERIAL_NUMBER_DEFAUL);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.BaseDefaultValue.DVRSERIAL_NUMBER_NAME);
                break;
            case SettingsKeyValue.BaseKeyValue.OUTTIME_EXITE:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.OUTTIME_EXITE);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.BaseDefaultValue.OUTTIME_EXITE_DEFAUL);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.BaseDefaultValue.OUTTIME_EXITE_NAME);
                break;
            case SettingsKeyValue.BaseKeyValue.PLATE_NUMBER:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.PLATE_NUMBER);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.BaseDefaultValue.PLATE_NUMBER_DEFAUL);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.BaseDefaultValue.PLATE_NUMBER_NAME);
                break;
            case SettingsKeyValue.BaseKeyValue.PRERECORD_TIME:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.PRERECORD_TIME);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.BaseDefaultValue.PRERECORD_TIME_DEFAUL);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.BaseDefaultValue.PRERECORD_TIME_NAME);
                break;
            case SettingsKeyValue.BaseKeyValue.PRINT_SENSITIVITY:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.PRINT_SENSITIVITY);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.BaseDefaultValue.PRINT_SENSITIVITY_DEFAUL);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.BaseDefaultValue.PRINT_SENSITIVITY_NAME);
                break;
            case SettingsKeyValue.BaseKeyValue.TERMINAL_NUMBER:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.BaseKeyValue.TERMINAL_NUMBER);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.BaseDefaultValue.TERMINAL_NUMBER_DEFAUL);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.BaseDefaultValue.TERMINAL_NUMBER_NAME);
                break;
            case SettingsKeyValue.SpeedSettingsKeyValue.ALARM_SPEED_MAX:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.SpeedSettingsKeyValue.ALARM_SPEED_MAX);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.SpeedSettingsValue.ALARM_SPEED_MAX_VALUE + "");
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.SpeedSettingsValue.ALARM_SPEED_MAX_NAME);
                break;
            case SettingsKeyValue.SpeedSettingsKeyValue.ALARM_SPEED_TIME:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.SpeedSettingsKeyValue.ALARM_SPEED_TIME);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.SpeedSettingsValue.ALARM_SPEED_TIME_VALUE + "");
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.SpeedSettingsValue.ALARM_SPEED_TIME_NAME);
                break;
            case SettingsKeyValue.SpeedSettingsKeyValue.PRE_ALARM_SPEED_MAX:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.SpeedSettingsKeyValue.PRE_ALARM_SPEED_MAX);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.SpeedSettingsValue.PRE_ALARM_SPEED_MAX_VALUE + "");
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.SpeedSettingsValue.PRE_ALARM_SPEED_MAX_NAME);
                break;
            case SettingsKeyValue.SpeedSettingsKeyValue.PRE_ALARM_SPEED_TIME:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.SpeedSettingsKeyValue.PRE_ALARM_SPEED_TIME);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.SpeedSettingsValue.PRE_ALARM_SPEED_TIME_VALUE + "");
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.SpeedSettingsValue.PRE_ALARM_SPEED_TIME_NAME);
                break;
            case SettingsKeyValue.SpeedSettingsKeyValue.SPEED_SOURCE:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.SpeedSettingsKeyValue.SPEED_SOURCE);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.SpeedSettingsValue.SPEED_SOURCE_VALUE + "");
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.SpeedSettingsValue.SPEED_SOURCE_NAME);
                break;
            case SettingsKeyValue.SpeedSettingsKeyValue.SPEED_UNIT:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.SpeedSettingsKeyValue.SPEED_UNIT);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.SpeedSettingsValue.SPEED_UNIT_VALUE + "");
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.SpeedSettingsValue.SPEED_UNIT_NAME);
                break;
            case SettingsKeyValue.PowerSettingsKeyValue.ACC_MODE:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.PowerSettingsKeyValue.ACC_MODE);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.PowerSettingsValue.ACC_MODE_VALUE + "");
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.PowerSettingsValue.ACC_MODE_NAME);
                break;
            case SettingsKeyValue.PowerSettingsKeyValue.OPEN_MODE:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.PowerSettingsKeyValue.OPEN_MODE);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.PowerSettingsValue.OPEN_MODE_VALUE + "");
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.PowerSettingsValue.OPEN_MODE_NAME);
                break;
            case SettingsKeyValue.PowerSettingsKeyValue.SHUTDOWN_DELAY:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.PowerSettingsKeyValue.SHUTDOWN_DELAY);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.PowerSettingsValue.SHUTDOWN_DELAY_VALUE + "");
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.PowerSettingsValue.SHUTDOWN_DELAY_NAME);
                break;
            case SettingsKeyValue.PowerSettingsKeyValue.LOWVOLTAGE_PROTECTION:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.PowerSettingsKeyValue.LOWVOLTAGE_PROTECTION);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.PowerSettingsValue.LOWVOLTAGE_PROTECTION_VALUE + "");
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.PowerSettingsValue.LOWVOLTAGE_PROTECTION_NAME);
                break;
            case SettingsKeyValue.PowerSettingsKeyValue.LOWVOLTAGE:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.PowerSettingsKeyValue.LOWVOLTAGE);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.PowerSettingsValue.LOWVOLTAGE_VALUE + "");
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.PowerSettingsValue.LOWVOLTAGE_NAME);
                break;
            case SettingsKeyValue.PowerSettingsKeyValue.RESTORE_VOLTAGE:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.PowerSettingsKeyValue.RESTORE_VOLTAGE);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.PowerSettingsValue.RESTORE_VOLTAGE_VALUE + "");
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.PowerSettingsValue.RESTORE_VOLTAGE_NAME);
                break;
            case SettingsKeyValue.PowerSettingsKeyValue.UNDERVOLTAGE_ALARM:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.PowerSettingsKeyValue.UNDERVOLTAGE_ALARM);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        SettingsDefaultValue.PowerSettingsValue.UNDERVOLTAGE_ALARM_VALUE + "");
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.PowerSettingsValue.UNDERVOLTAGE_ALARM_NAME);
                break;
        }
        return values;
    }

    private String getDefaultContent(int id) {
        switch (id) {
            case SettingsKeyValue.BaseKeyValue.COMPANY_NUMBER:
                return SettingsDefaultValue.BaseDefaultValue.COMPANY_NUMBER_DEFAUL;
            case SettingsKeyValue.BaseKeyValue.DELAY_TIME:
                return SettingsDefaultValue.BaseDefaultValue.DELAY_TIME_DEFAUL + "";
            case SettingsKeyValue.BaseKeyValue.DVRSERIAL_NUMBER:
                return SettingsDefaultValue.BaseDefaultValue.DVRSERIAL_NUMBER_DEFAUL;
            case SettingsKeyValue.BaseKeyValue.OUTTIME_EXITE:
                return SettingsDefaultValue.BaseDefaultValue.OUTTIME_EXITE_DEFAUL + "";
            case SettingsKeyValue.BaseKeyValue.PLATE_NUMBER:
                return SettingsDefaultValue.BaseDefaultValue.PLATE_NUMBER_DEFAUL;
            case SettingsKeyValue.BaseKeyValue.PRERECORD_TIME:
                return SettingsDefaultValue.BaseDefaultValue.PRERECORD_TIME_DEFAUL + "";
            case SettingsKeyValue.BaseKeyValue.PRINT_SENSITIVITY:
                return SettingsDefaultValue.BaseDefaultValue.PRINT_SENSITIVITY_DEFAUL + "";
            case SettingsKeyValue.BaseKeyValue.TERMINAL_NUMBER:
                return SettingsDefaultValue.BaseDefaultValue.TERMINAL_NUMBER_DEFAUL;
            case SettingsKeyValue.SpeedSettingsKeyValue.ALARM_SPEED_MAX:
                return SettingsDefaultValue.SpeedSettingsValue.ALARM_SPEED_MAX_VALUE + "";
            case SettingsKeyValue.SpeedSettingsKeyValue.ALARM_SPEED_TIME:
                return SettingsDefaultValue.SpeedSettingsValue.ALARM_SPEED_TIME_VALUE + "";
            case SettingsKeyValue.SpeedSettingsKeyValue.PRE_ALARM_SPEED_MAX:
                return SettingsDefaultValue.SpeedSettingsValue.PRE_ALARM_SPEED_MAX_VALUE + "";
            case SettingsKeyValue.SpeedSettingsKeyValue.PRE_ALARM_SPEED_TIME:
                return SettingsDefaultValue.SpeedSettingsValue.PRE_ALARM_SPEED_TIME_VALUE + "";
            case SettingsKeyValue.SpeedSettingsKeyValue.SPEED_SOURCE:
                return SettingsDefaultValue.SpeedSettingsValue.SPEED_SOURCE_VALUE + "";
            case SettingsKeyValue.SpeedSettingsKeyValue.SPEED_UNIT:
                return SettingsDefaultValue.SpeedSettingsValue.SPEED_UNIT_VALUE + "";
            case SettingsKeyValue.PowerSettingsKeyValue.ACC_MODE:
                return SettingsDefaultValue.PowerSettingsValue.ACC_MODE_VALUE + "";
            case SettingsKeyValue.PowerSettingsKeyValue.OPEN_MODE:
                return SettingsDefaultValue.PowerSettingsValue.OPEN_MODE_VALUE + "";
            case SettingsKeyValue.PowerSettingsKeyValue.LOWVOLTAGE:
                return SettingsDefaultValue.PowerSettingsValue.LOWVOLTAGE_VALUE + "";
            case SettingsKeyValue.PowerSettingsKeyValue.LOWVOLTAGE_PROTECTION:
                return SettingsDefaultValue.PowerSettingsValue.LOWVOLTAGE_PROTECTION_VALUE + "";
            case SettingsKeyValue.PowerSettingsKeyValue.RESTORE_VOLTAGE:
                return SettingsDefaultValue.PowerSettingsValue.RESTORE_VOLTAGE_VALUE + "";
            case SettingsKeyValue.PowerSettingsKeyValue.UNDERVOLTAGE_ALARM:
                return SettingsDefaultValue.PowerSettingsValue.UNDERVOLTAGE_ALARM_VALUE + "";
            case SettingsKeyValue.PowerSettingsKeyValue.SHUTDOWN_DELAY:
                return SettingsDefaultValue.PowerSettingsValue.UNDERVOLTAGE_ALARM_VALUE + "";
        }
        return null;
    }


    @Override
    public void saveSpeedSettings(int id, @NonNull String value) {
        String selection = DBMetaData.SettingsEntry._ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, null);
        if (c != null && c.getCount() > 0) {
            ContentValues values = new ContentValues();
            values.put(DBMetaData.SettingsEntry.CONTENT, value);
            resolver.update(CONTENT_URI, values, selection, selectionArgs);
        } else {
            ContentValues values = getSpeedValues(id, value);
            resolver.insert(CONTENT_URI, values);
            values.clear();
        }
        if (c != null) {
            c.close();
        }
    }

    @Override
    public void restoringSpeedDefault() {
        setAlarm_speed_max(SettingsDefaultValue.SpeedSettingsValue.ALARM_SPEED_MAX_VALUE);
        setAlarm_speed_time(SettingsDefaultValue.SpeedSettingsValue.ALARM_SPEED_TIME_VALUE);
        setPre_alarm_speed_max(SettingsDefaultValue.SpeedSettingsValue.PRE_ALARM_SPEED_MAX_VALUE);
        setPre_alarm_speed_time(SettingsDefaultValue.SpeedSettingsValue.PRE_ALARM_SPEED_TIME_VALUE);
        setSpeed_source(SettingsDefaultValue.SpeedSettingsValue.SPEED_SOURCE_VALUE);
        setSpeed_unit(SettingsDefaultValue.SpeedSettingsValue.SPEED_UNIT_VALUE);
//        saveDBData(speedSettings);
    }

    @Override
    public void getAlarm_speed_max(@NonNull GetSpeedSettingCallBack callBack) {
        getDBData(SettingsKeyValue.SpeedSettingsKeyValue.ALARM_SPEED_MAX, callBack);
    }

    @Override
    public void getAlarm_speed_time(@NonNull GetSpeedSettingCallBack callBack) {
        getDBData(SettingsKeyValue.SpeedSettingsKeyValue.ALARM_SPEED_TIME, callBack);
    }

    @Override
    public void getPre_alarm_speed_max(@NonNull GetSpeedSettingCallBack callBack) {
        getDBData(SettingsKeyValue.SpeedSettingsKeyValue.PRE_ALARM_SPEED_MAX, callBack);
    }

    @Override
    public void getPre_alarm_speed_time(@NonNull GetSpeedSettingCallBack callBack) {
        getDBData(SettingsKeyValue.SpeedSettingsKeyValue.PRE_ALARM_SPEED_TIME, callBack);
    }

    @Override
    public void getSpeed_source(@NonNull GetSpeedSettingCallBack callBack) {
        getDBData(SettingsKeyValue.SpeedSettingsKeyValue.SPEED_SOURCE, callBack);
    }

    @Override
    public void getSpeed_unit(@NonNull GetSpeedSettingCallBack callBack) {
        getDBData(SettingsKeyValue.SpeedSettingsKeyValue.SPEED_UNIT, callBack);
    }

    @Override
    public void setAlarm_speed_max(int alarm_speed_max) {
        saveSpeedSettings(SettingsKeyValue.SpeedSettingsKeyValue.ALARM_SPEED_MAX, alarm_speed_max + "");
    }

    @Override
    public void setAlarm_speed_time(int alarm_speed_time) {
        saveSpeedSettings(SettingsKeyValue.SpeedSettingsKeyValue.ALARM_SPEED_TIME, alarm_speed_time + "");
    }

    @Override
    public void setPre_alarm_speed_max(int pre_alarm_speed_max) {
        saveSpeedSettings(SettingsKeyValue.SpeedSettingsKeyValue.PRE_ALARM_SPEED_MAX, pre_alarm_speed_max + "");
    }

    @Override
    public void setPre_alarm_speed_time(int pre_alarm_speed_time) {
        saveSpeedSettings(SettingsKeyValue.SpeedSettingsKeyValue.PRE_ALARM_SPEED_TIME, pre_alarm_speed_time + "");
    }

    @Override
    public void setSpeed_source(int speed_source) {
        saveSpeedSettings(SettingsKeyValue.SpeedSettingsKeyValue.SPEED_SOURCE, speed_source + "");
    }

    @Override
    public void setSpeed_unit(int speed_unit) {
        saveSpeedSettings(SettingsKeyValue.SpeedSettingsKeyValue.SPEED_UNIT, speed_unit + "");
    }

    private ContentValues getSpeedValues(int id, String context) {
        ContentValues values = new ContentValues();
        switch (id) {
            case SettingsKeyValue.SpeedSettingsKeyValue.ALARM_SPEED_MAX:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.SpeedSettingsKeyValue.ALARM_SPEED_MAX);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        context);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.SpeedSettingsValue.ALARM_SPEED_MAX_NAME);
                break;
            case SettingsKeyValue.SpeedSettingsKeyValue.ALARM_SPEED_TIME:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.SpeedSettingsKeyValue.ALARM_SPEED_TIME);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        context);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.SpeedSettingsValue.ALARM_SPEED_TIME_NAME);
                break;
            case SettingsKeyValue.SpeedSettingsKeyValue.PRE_ALARM_SPEED_MAX:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.SpeedSettingsKeyValue.PRE_ALARM_SPEED_MAX);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        context);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.SpeedSettingsValue.PRE_ALARM_SPEED_MAX_NAME);
                break;
            case SettingsKeyValue.SpeedSettingsKeyValue.PRE_ALARM_SPEED_TIME:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.SpeedSettingsKeyValue.PRE_ALARM_SPEED_TIME);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        context);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.SpeedSettingsValue.PRE_ALARM_SPEED_TIME_NAME);
                break;
            case SettingsKeyValue.SpeedSettingsKeyValue.SPEED_SOURCE:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.SpeedSettingsKeyValue.SPEED_SOURCE);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        context);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.SpeedSettingsValue.SPEED_SOURCE_NAME);
                break;
            case SettingsKeyValue.SpeedSettingsKeyValue.SPEED_UNIT:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.SpeedSettingsKeyValue.SPEED_UNIT);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        context);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.SpeedSettingsValue.SPEED_UNIT_NAME);
                break;
        }
        return values;
    }


    @Override
    public void savePowerSettings(int id, @NonNull String value) {
        String selection = DBMetaData.SettingsEntry._ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, null);
        if (c != null && c.getCount() > 0) {
            ContentValues values = new ContentValues();
            values.put(DBMetaData.SettingsEntry.CONTENT, value);
            resolver.update(CONTENT_URI, values, selection, selectionArgs);
        } else {
            ContentValues values = getPowerValues(id, value);
            resolver.insert(CONTENT_URI, values);
            values.clear();
        }
        if (c != null) {
            c.close();
        }
    }

    @Override
    public void restoringPowerDefault() {
        setShutdown_Delay(SettingsDefaultValue.PowerSettingsValue.SHUTDOWN_DELAY_VALUE);
        setLowVoltage_protection(SettingsDefaultValue.PowerSettingsValue.LOWVOLTAGE_PROTECTION_VALUE);
        setLowVoltage(SettingsDefaultValue.PowerSettingsValue.LOWVOLTAGE_VALUE);
        setUndervoltage_alarm(SettingsDefaultValue.PowerSettingsValue.UNDERVOLTAGE_ALARM_VALUE);
        setAcc_mode(SettingsDefaultValue.PowerSettingsValue.ACC_MODE_VALUE);
        setOpen_mode(SettingsDefaultValue.PowerSettingsValue.OPEN_MODE_VALUE);
        setRestore_voltage(SettingsDefaultValue.PowerSettingsValue.RESTORE_VOLTAGE_VALUE);
    }

    @Override
    public void getAcc_mode(GetPowerSettingCallBack callBack) {
        getDBData(SettingsKeyValue.PowerSettingsKeyValue.ACC_MODE, callBack);
    }

    @Override
    public void getOpen_mode(GetPowerSettingCallBack callBack) {
        getDBData(SettingsKeyValue.PowerSettingsKeyValue.OPEN_MODE, callBack);
    }

    @Override
    public void getShutdown_Delay(GetPowerSettingCallBack callBack) {
        getDBData(SettingsKeyValue.PowerSettingsKeyValue.SHUTDOWN_DELAY, callBack);
    }

    @Override
    public void isLowVoltage_protection(GetPowerSettingCallBack callBack) {
        getDBData(SettingsKeyValue.PowerSettingsKeyValue.LOWVOLTAGE_PROTECTION, callBack);
    }

    @Override
    public void getLowVoltage(GetPowerSettingCallBack callBack) {
        getDBData(SettingsKeyValue.PowerSettingsKeyValue.LOWVOLTAGE, callBack);
    }

    @Override
    public void getRestore_voltage(GetPowerSettingCallBack callBack) {
        getDBData(SettingsKeyValue.PowerSettingsKeyValue.RESTORE_VOLTAGE, callBack);
    }

    @Override
    public void isUndervoltage_alarm(GetPowerSettingCallBack callBack) {
        getDBData(SettingsKeyValue.PowerSettingsKeyValue.UNDERVOLTAGE_ALARM, callBack);
    }

    @Override
    public void setAcc_mode(int acc_mode) {
        savePowerSettings(SettingsKeyValue.PowerSettingsKeyValue.ACC_MODE, acc_mode + "");
    }

    @Override
    public void setOpen_mode(int open_mode) {
        savePowerSettings(SettingsKeyValue.PowerSettingsKeyValue.OPEN_MODE, open_mode + "");
    }

    @Override
    public void setShutdown_Delay(int shutdown_Delay) {
        savePowerSettings(SettingsKeyValue.PowerSettingsKeyValue.SHUTDOWN_DELAY, shutdown_Delay + "");
    }

    @Override
    public void setLowVoltage_protection(boolean lowVoltage_protection) {
        savePowerSettings(SettingsKeyValue.PowerSettingsKeyValue.LOWVOLTAGE_PROTECTION, lowVoltage_protection + "");
    }

    @Override
    public void setLowVoltage(float lowVoltage) {
        savePowerSettings(SettingsKeyValue.PowerSettingsKeyValue.LOWVOLTAGE, lowVoltage + "");
    }

    @Override
    public void setRestore_voltage(float restore_voltage) {
        savePowerSettings(SettingsKeyValue.PowerSettingsKeyValue.RESTORE_VOLTAGE, restore_voltage + "");
    }

    @Override
    public void setUndervoltage_alarm(boolean undervoltage_alarm) {
        savePowerSettings(SettingsKeyValue.PowerSettingsKeyValue.UNDERVOLTAGE_ALARM, undervoltage_alarm + "");
    }
//
//    private ContentValues getDefaultPowerValues(int id) {
//        ContentValues values = new ContentValues();
//        switch (id) {
//
//        }
//        return values;
//    }

    private ContentValues getPowerValues(int id, String context) {
        ContentValues values = new ContentValues();
        switch (id) {
            case SettingsKeyValue.PowerSettingsKeyValue.ACC_MODE:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.PowerSettingsKeyValue.ACC_MODE);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        context);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.PowerSettingsValue.ACC_MODE_NAME);
                break;
            case SettingsKeyValue.PowerSettingsKeyValue.OPEN_MODE:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.PowerSettingsKeyValue.OPEN_MODE);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        context);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.PowerSettingsValue.OPEN_MODE_NAME);
                break;
            case SettingsKeyValue.PowerSettingsKeyValue.SHUTDOWN_DELAY:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.PowerSettingsKeyValue.SHUTDOWN_DELAY);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        context);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.PowerSettingsValue.SHUTDOWN_DELAY_NAME);
                break;
            case SettingsKeyValue.PowerSettingsKeyValue.LOWVOLTAGE_PROTECTION:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.PowerSettingsKeyValue.LOWVOLTAGE_PROTECTION);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        context);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.PowerSettingsValue.LOWVOLTAGE_PROTECTION_NAME);
                break;
            case SettingsKeyValue.PowerSettingsKeyValue.LOWVOLTAGE:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.PowerSettingsKeyValue.LOWVOLTAGE);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        context);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.PowerSettingsValue.LOWVOLTAGE_NAME);
                break;
            case SettingsKeyValue.PowerSettingsKeyValue.RESTORE_VOLTAGE:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.PowerSettingsKeyValue.RESTORE_VOLTAGE);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        context);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.PowerSettingsValue.RESTORE_VOLTAGE_NAME);
                break;
            case SettingsKeyValue.PowerSettingsKeyValue.UNDERVOLTAGE_ALARM:
                values.put(DBMetaData.SettingsEntry._ID, SettingsKeyValue.PowerSettingsKeyValue.UNDERVOLTAGE_ALARM);
                values.put(DBMetaData.SettingsEntry.CONTENT,
                        context);
                values.put(DBMetaData.SettingsEntry.NAME,
                        SettingsDefaultValue.PowerSettingsValue.UNDERVOLTAGE_ALARM_NAME);
                break;
        }
        return values;
    }

//    private String getDefaultContent(int id) {
//        switch (id) {
//
//        }
//        return null;
//    }


}
