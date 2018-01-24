package com.uninew.car.db.signs;

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
 * Created by Administrator on 2017/10/11 0011.
 */

public class SignLocalDataSource implements SignLocalSource {

    private static volatile SignLocalDataSource INSTANCE;
    private WeakReference<Context> wp;//防止context内存泄露的优化
    /**
     * contentprovider连接文本信息的uri
     **/
    private static final Uri CONTENT_URI = Uri.parse("content://" + DBMetaData.AUTHORITIES + "/"
            + DBMetaData.SignsEntry.TABLE_NAME + "/");
    private ContentResolver resolver;//ContentProvider操作类

    /* 数据表最多保存天数 （30天）*/
    private static final long REVENUE_MAX_SAVE = 1000 * 60 * 60 * 24 * 30L;

    private SignLocalDataSource(Context context) {
        wp = new WeakReference<>(context);
        resolver = wp.get().getContentResolver();
    }

    public static SignLocalDataSource getInstance(Context context) {
        if (null != INSTANCE) {

        } else {
            synchronized (SignLocalDataSource.class) {
                if (null == INSTANCE) {
                    INSTANCE = new SignLocalDataSource(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAllDBDatas(@NonNull LoadDBDataCallBack callBack) {
        List<SignOrSignOut> signOrSignOuts = new ArrayList<>();
        Cursor c = resolver.query(CONTENT_URI, null, null, null, DBMetaData.SignsEntry.TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                signOrSignOuts.add(getSigns(c));
            }
        }
        if (c != null) {
            c.close();
        }
        if (signOrSignOuts.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(signOrSignOuts);
        }
    }

    @Override
    public void getDBData(int _id, @NonNull GetDBDataCallBack callBack) {
        SignOrSignOut signOrSignOut = new SignOrSignOut();
        Cursor c = resolver.query(CONTENT_URI, null, null, null, DBMetaData.SignsEntry.TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            signOrSignOut = getSigns(c);
        }
        if (c != null) {
            c.close();
        }
        if (signOrSignOut == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(signOrSignOut);
        }
    }

    @Override
    public void saveDBData(@NonNull SignOrSignOut... signOrSignOuts) {
        if (signOrSignOuts != null && signOrSignOuts.length > 0) {
            for (SignOrSignOut signOrSignOut : signOrSignOuts) {
                ContentValues values = getValues(signOrSignOut);
                resolver.insert(CONTENT_URI, values);
                values.clear();
            }
        }
    }

    @Override
    public void deleteAllDBDatas() {
        resolver.delete(CONTENT_URI, null, null);
    }

    private ContentValues getValues(SignOrSignOut signOrSignOut) {
        ContentValues values = new ContentValues();
        values.put(DBMetaData.SignsEntry.ALARM, signOrSignOut.getAlarm());
        values.put(DBMetaData.SignsEntry.ALL_MILEAGE, signOrSignOut.getAllMileage());
        values.put(DBMetaData.SignsEntry.AMOUNT, signOrSignOut.getAmount());
        values.put(DBMetaData.SignsEntry.BOOT_TIME, signOrSignOut.getBootTime());
        values.put(DBMetaData.SignsEntry.BUSINESS_LICENSE, signOrSignOut.getBusinessLicense());
        values.put(DBMetaData.SignsEntry.ELEVATION, signOrSignOut.getElevation());
        values.put(DBMetaData.SignsEntry.CAR_TIMES, signOrSignOut.getCarTimes());
        values.put(DBMetaData.SignsEntry.CARD_TIMES, signOrSignOut.getCardTimes());
        values.put(DBMetaData.SignsEntry.METERKVALUE,signOrSignOut.getMeterKValue());
        values.put(DBMetaData.SignsEntry.CASH_CARD_AMOUNT, signOrSignOut.getCashCardAmount());
        values.put(DBMetaData.SignsEntry.DIRECTION, signOrSignOut.getDirection());
        values.put(DBMetaData.SignsEntry.DRIVER_CERTIFICATE, signOrSignOut.getDriverCertificate());
        values.put(DBMetaData.SignsEntry.EXTENDED_ATTRIBUTES, signOrSignOut.getExtendedAttributes());
        values.put(DBMetaData.SignsEntry.LATITUDE, signOrSignOut.getLatitude());
        values.put(DBMetaData.SignsEntry.LONGITUDE, signOrSignOut.getLongitude());
        values.put(DBMetaData.SignsEntry.NIGHT_MILEAGE, signOrSignOut.getNightMileage());
        values.put(DBMetaData.SignsEntry.ON_DUTY_MILEAGE, signOrSignOut.getOnDutyMileage());
        values.put(DBMetaData.SignsEntry.PLATE_NUMBER, signOrSignOut.getPlateNumber());
        values.put(DBMetaData.SignsEntry.PRICE, signOrSignOut.getPrice());
        values.put(DBMetaData.SignsEntry.REVENUE_ALL_MILEAGE, signOrSignOut.getRevenueAllMileage());
        values.put(DBMetaData.SignsEntry.REVENUE_TIMES, signOrSignOut.getRevenueTimes());
        values.put(DBMetaData.SignsEntry.RUN_MILEAGE, signOrSignOut.getRunMileage());
        values.put(DBMetaData.SignsEntry.SHUTDOWN_TIME, signOrSignOut.getShutdownTime());
        values.put(DBMetaData.SignsEntry.SIGN_OUT_TYPE, signOrSignOut.getSignOutType());
        values.put(DBMetaData.SignsEntry.SIGN_STATE, signOrSignOut.getSignState());
        values.put(DBMetaData.SignsEntry.STATE, signOrSignOut.getState());
        values.put(DBMetaData.SignsEntry.TIME, signOrSignOut.getTime());
        values.put(DBMetaData.SignsEntry.TIMING_TIME, signOrSignOut.getTimingTime());
        return values;
    }

    private SignOrSignOut getSigns(Cursor c) {
        SignOrSignOut signOrSignOut = new SignOrSignOut();
        signOrSignOut.setAlarm(c.getInt(c.getColumnIndex(DBMetaData.SignsEntry.ALARM)));
        signOrSignOut.setAllMileage(c.getDouble(c.getColumnIndex(DBMetaData.SignsEntry.ALL_MILEAGE)));
        signOrSignOut.setAmount(c.getDouble(c.getColumnIndex(DBMetaData.SignsEntry.AMOUNT)));
        signOrSignOut.setBootTime(c.getString(c.getColumnIndex(DBMetaData.SignsEntry.BOOT_TIME)));
        signOrSignOut.setBusinessLicense(c.getString(c.getColumnIndex(DBMetaData.SignsEntry.BUSINESS_LICENSE)));
        signOrSignOut.setCardTimes(c.getInt(c.getColumnIndex(DBMetaData.SignsEntry.CARD_TIMES)));
        signOrSignOut.setCarTimes(c.getInt(c.getColumnIndex(DBMetaData.SignsEntry.CAR_TIMES)));
        signOrSignOut.setMeterKValue(c.getInt(c.getColumnIndex(DBMetaData.SignsEntry.METERKVALUE )));
        signOrSignOut.setElevation(c.getDouble(c.getColumnIndex(DBMetaData.SignsEntry.ELEVATION)));
        signOrSignOut.setCashCardAmount(c.getDouble(c.getColumnIndex(DBMetaData.SignsEntry.CASH_CARD_AMOUNT)));
        signOrSignOut.setDirection(c.getDouble(c.getColumnIndex(DBMetaData.SignsEntry.DIRECTION)));
        signOrSignOut.setDriverCertificate(c.getString(c.getColumnIndex(DBMetaData.SignsEntry.DRIVER_CERTIFICATE)));
        signOrSignOut.setExtendedAttributes(c.getBlob(c.getColumnIndex(DBMetaData.SignsEntry.EXTENDED_ATTRIBUTES)));
        signOrSignOut.setId(c.getInt(c.getColumnIndex(DBMetaData.SettingsEntry._ID)));
        signOrSignOut.setLatitude(c.getDouble(c.getColumnIndex(DBMetaData.SignsEntry.LATITUDE)));
        signOrSignOut.setLongitude(c.getDouble(c.getColumnIndex(DBMetaData.SignsEntry.LONGITUDE)));
        signOrSignOut.setNightMileage(c.getDouble(c.getColumnIndex(DBMetaData.SignsEntry.NIGHT_MILEAGE)));
        signOrSignOut.setOnDutyMileage(c.getDouble(c.getColumnIndex(DBMetaData.SignsEntry.ON_DUTY_MILEAGE)));
        signOrSignOut.setPlateNumber(c.getString(c.getColumnIndex(DBMetaData.SignsEntry.PLATE_NUMBER)));
        signOrSignOut.setPrice(c.getDouble(c.getColumnIndex(DBMetaData.SignsEntry.PRICE)));
        signOrSignOut.setRevenueAllMileage(c.getDouble(c.getColumnIndex(DBMetaData.SignsEntry.REVENUE_ALL_MILEAGE)));
        signOrSignOut.setRevenueTimes(c.getInt(c.getColumnIndex(DBMetaData.SignsEntry.REVENUE_TIMES)));
        signOrSignOut.setSignState(c.getInt(c.getColumnIndex(DBMetaData.SignsEntry.SIGN_STATE)));
        signOrSignOut.setShutdownTime(c.getString(c.getColumnIndex(DBMetaData.SignsEntry.SHUTDOWN_TIME)));
        signOrSignOut.setSignOutType(c.getInt(c.getColumnIndex(DBMetaData.SignsEntry.SIGN_OUT_TYPE)));
        signOrSignOut.setSpeed(c.getDouble(c.getColumnIndex(DBMetaData.SignsEntry.SPEED)));
        signOrSignOut.setState(c.getInt(c.getColumnIndex(DBMetaData.SignsEntry.STATE)));
        signOrSignOut.setTime(c.getString(c.getColumnIndex(DBMetaData.SignsEntry.TIME)));
        signOrSignOut.setTimingTime(c.getString(c.getColumnIndex(DBMetaData.SignsEntry.TIMING_TIME)));
        signOrSignOut.setRunMileage(c.getDouble(c.getColumnIndex(DBMetaData.SignsEntry.RUN_MILEAGE)));
        return signOrSignOut;
    }

    @Override
    public void deleteDBData(@NonNull int id) {
        String selection = DBMetaData.SignsEntry._ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        resolver.delete(CONTENT_URI, selection, selectionArgs);
    }

    @Override
    public void getSignsByteSignstate(int signState, LoadSignsCallBack callBack) {
        String selection = DBMetaData.SignsEntry.SIGN_STATE + "=?";
        String[] selectionArgs = {
                signState + ""
        };
        List<SignOrSignOut> signOrSignOuts = new ArrayList<>();
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.SignsEntry.TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                signOrSignOuts.add(getSigns(c));
            }
        }
        if (c != null) {
            c.close();
        }
        if (signOrSignOuts.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(signOrSignOuts);
        }
    }
}
