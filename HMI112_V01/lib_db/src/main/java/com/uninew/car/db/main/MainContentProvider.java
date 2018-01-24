package com.uninew.car.db.main;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public final class MainContentProvider extends ContentProvider {
    private DatabaseHelper mDatabaseHelper;
    // 访问表的所有列
    public static final int INCOMING_USER_COLLECTION = 1;
    // 访问单独的列
    public static final int INCOMING_USER_SINGLE = 2;
    // 操作URI的类
    public static final UriMatcher uriMatcher;

    // 为UriMatcher添加自定义的URI
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DBMetaData.AUTHORITIES, "/guideboard", INCOMING_USER_COLLECTION);
        uriMatcher.addURI(DBMetaData.AUTHORITIES, "/guideboard/#", INCOMING_USER_SINGLE);
    }

    @Override
    public boolean onCreate() {
        init();
        return true;
    }

    private void init() {
        mDatabaseHelper = new DatabaseHelper(this.getContext());
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        Cursor c = null;
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = DBMetaData.DEFAULT_SORT_ORDER ;
        } else {
            orderBy = sortOrder;
        }
        try {
            queryBuilder.setTables(uri.getLastPathSegment());
            c = queryBuilder.query(db, projection, selection, selectionArgs, null, null, orderBy);
        } catch (Exception e) {
            e.printStackTrace();
            c = null;
        }
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        long rowId = 0;
        db.beginTransaction();
        try {
            rowId = db.insert(uri.getLastPathSegment(), null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        if (rowId > 0) {
            Uri insertedUserUri = ContentUris.withAppendedId(uri, rowId);
            // 通知监听器，数据已经改变
            getContext().getContentResolver().notifyChange(insertedUserUri, null);
//			Log.d(TAG, insertedUserUri.toString());
            return insertedUserUri;
        }
//        db.close();
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            count = db.delete(uri.getLastPathSegment(), selection, selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
//        db.close();
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            count = db.update(uri.getLastPathSegment(), values, selection, selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        if (count > 0) {
            if (selectionArgs != null && selectionArgs.length > 0) {
                StringBuffer buffer = new StringBuffer();
                for (String s : selectionArgs) {
                    buffer.append("/" + s);
                }
                Uri insertedUserUri = Uri.parse(uri.toString() + buffer.toString());
                this.getContext().getContentResolver().notifyChange(insertedUserUri, null);
            } else {
                this.getContext().getContentResolver().notifyChange(uri, null);//如果改变数据，则通知所有人
            }
        }
//        db.close();
        return count;
    }
}
