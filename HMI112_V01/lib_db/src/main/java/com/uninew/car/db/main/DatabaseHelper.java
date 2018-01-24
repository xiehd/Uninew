package com.uninew.car.db.main;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public final class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "uninew_car_db";
    private static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBMetaData.OrderEntry.SQL_CREATE_TABLE);
        db.execSQL(DBMetaData.RevenueEntry.SQL_CREATE_TABLE);
        db.execSQL(DBMetaData.AlarmMessageEntry.SQL_CREATE_TABLE);
        db.execSQL(DBMetaData.AttendanceEntry.SQL_CREATE_TABLE);
        db.execSQL(DBMetaData.ContactEntry.SQL_CREATE_TABLE);
        db.execSQL(DBMetaData.DiallerEntry.SQL_CREATE_TABLE);
        db.execSQL(DBMetaData.LocationEntry.SQL_CREATE_TABLE);
        db.execSQL(DBMetaData.MessageEntry.SQL_CREATE_TABLE);
        db.execSQL(DBMetaData.SettingsEntry.SQL_CREATE_TABLE);
        db.execSQL(DBMetaData.DriverMessageEntry.SQL_CREATE_TABLE);
        db.execSQL(DBMetaData.PlatformEntry.SQL_CREATE_TABLE);
        db.execSQL(DBMetaData.AnswerEntry.SQL_CREATE_TABLE);
        db.execSQL(DBMetaData.SignsEntry.SQL_CREATE_TABLE);
        db.execSQL(DBMetaData.ParamSetEntry.SQL_CREATE_TABLE);
        db.execSQL(DBMetaData.PhotoEntry.SQL_CREATE_TABLE);
        db.execSQL(DBMetaData.AudioEntry.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
