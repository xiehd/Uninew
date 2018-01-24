package com.uninew.car.db.revenue;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.uninew.car.db.main.DBMetaData;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import tools.TimeTool;

/**
 * Created by Administrator on 2017/8/30 0030.
 */

public class RevenueLocalDataSource implements RevenueLocalSource {

    private static volatile RevenueLocalDataSource INSTANCE;
    private WeakReference<Context> wp;//防止context内存泄露的优化
    /**
     * contentprovider连接文本信息的uri
     **/
    private static final Uri CONTENT_URI = Uri.parse("content://" + DBMetaData.AUTHORITIES + "/"
            + DBMetaData.RevenueEntry.TABLE_NAME + "/");
    private ContentResolver resolver;//ContentProvider操作类

    /* 数据表最多保存天数 （30天）*/
    private static final long REVENUE_MAX_SAVE = 2592000000l;

    private RevenueLocalDataSource(Context context) {
        wp = new WeakReference<>(context);
        resolver = wp.get().getContentResolver();
    }

    public static RevenueLocalDataSource getInstance(Context context) {
        if (null != INSTANCE) {

        } else {
            synchronized (RevenueLocalDataSource.class) {
                if (null == INSTANCE) {
                    INSTANCE = new RevenueLocalDataSource(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAllDBDatas(@NonNull LoadDBDataCallBack callBack) {
        List<Revenue> revenues = new ArrayList<>();
        Cursor c = resolver.query(CONTENT_URI, null, null, null, DBMetaData.RevenueEntry.TIME + DBMetaData.ASC);
        if (null != c && c.getCount() > 0) {
            while (c.moveToNext()) {
                Revenue revenue = getRevenue(c);
                revenues.add(revenue);
            }
        }
        if (null != c) {
            c.close();
        }
        if (revenues.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(revenues);
        }
    }


    private Revenue getRevenue(Cursor c) {
        Revenue revenue = new Revenue();
        revenue.setId(c.getInt(c.getColumnIndex(DBMetaData.RevenueEntry._ID)));
        revenue.setEvaluation(c.getInt(c.getColumnIndex(DBMetaData.RevenueEntry.EVALUATION)));
        revenue.setEvaluationExtended(c.getInt(c.getColumnIndex(DBMetaData.RevenueEntry.EVALUATION_EXTENDED)));
        revenue.setEvaluationId(c.getInt(c.getColumnIndex(DBMetaData.RevenueEntry.EVALUATION_ID)));
        revenue.setOrderId(c.getInt(c.getColumnIndex(DBMetaData.RevenueEntry.ORDER_ID)));
        revenue.setRevenueId(c.getInt(c.getColumnIndex(DBMetaData.RevenueEntry.REVENUEID)));
        revenue.setTime(c.getString(c.getColumnIndex(DBMetaData.RevenueEntry.TIME)));
        try {
            revenue.setDownLocation(c.getBlob(c.getColumnIndex(DBMetaData.RevenueEntry.DOWN_LOCATION)));
            revenue.setUpCarLocation(c.getBlob(c.getColumnIndex(DBMetaData.RevenueEntry.UP_CAR_LOCATION)));
            revenue.setRevenueDatas(c.getBlob(c.getColumnIndex(DBMetaData.RevenueEntry.REVENUE_DATAS)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenue;
    }

    @Override
    public void getDBData(int _id, @NonNull GetDBDataCallBack callBack) {
        Revenue revenue = null;
        String selection = DBMetaData.RevenueEntry._ID + "=?";
        String[] selectionArgs = {
                _id + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.RevenueEntry.TIME + DBMetaData.ASC);
        if (null != c && c.getCount() > 0) {
            c.moveToFirst();
            revenue = getRevenue(c);
        }
        if (null != c) {
            c.close();
        }
        if (null == revenue) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(revenue);
        }
    }

    private ContentValues getContentValues(Revenue revenue) {
        ContentValues values = new ContentValues();
        values.put(DBMetaData.RevenueEntry.DOWN_LOCATION, revenue.getDownLocation());
        values.put(DBMetaData.RevenueEntry.EVALUATION, revenue.getEvaluation());
        values.put(DBMetaData.RevenueEntry.EVALUATION_EXTENDED, revenue.getEvaluationExtended());
        values.put(DBMetaData.RevenueEntry.EVALUATION_ID, revenue.getEvaluationId());
        values.put(DBMetaData.RevenueEntry.ORDER_ID, revenue.getOrderId());
        values.put(DBMetaData.RevenueEntry.REVENUE_DATAS, revenue.getRevenueDatas());
        values.put(DBMetaData.RevenueEntry.TIME, revenue.getTime());
        values.put(DBMetaData.RevenueEntry.UP_CAR_LOCATION, revenue.getUpCarLocation());
        return values;
    }

    @Override
    public void saveDBData(@NonNull final Revenue... revenues) {
        if (null != revenues && revenues.length > 0) {
            getAllDBDatas(new LoadRevenueCallback() {

                @Override
                public void onDBBaseDataLoaded(final List<Revenue> buffers) {
                    try {
                        final long time = TimeTool.parseToLong(buffers.get(buffers.size() - 1).getTime())
                                - TimeTool.parseToLong(buffers.get(0).getTime());
                        int size = revenues.length;
                        for (int i = 0; i < size; i++) {
                            final Revenue revenue = revenues[i];
                            final ContentValues values = getContentValues(revenue);
                            getRevenueCallBack(revenue.getTime(), new GetRevenueCallback() {
                                @Override
                                public void onDBBaseDataLoaded(Revenue revenue) {
                                    String selection = DBMetaData.RevenueEntry.TIME;
                                    String[] selectionArgs = {
                                            revenue.getTime()
                                    };
                                    resolver.update(CONTENT_URI, values, selection, selectionArgs);
                                }

                                @Override
                                public void onDataNotAailable() {
                                    Log.d("mydb", "time:" + time);
                                    if (time == 0) {
                                        long t = 0;
                                        try {
                                            t = TimeTool.parseToLong(revenue.getTime())
                                                    - TimeTool.parseToLong(buffers.get(0).getTime());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        if (t < REVENUE_MAX_SAVE) {
                                            resolver.insert(CONTENT_URI, values);
                                        } else {
                                            Log.d("mydb", "更新操作！");
                                            String selection = DBMetaData.RevenueEntry._ID;
                                            String[] selectionArgs = {
                                                    buffers.get(0).getId() + ""
                                            };
                                            resolver.update(CONTENT_URI, values, selection, selectionArgs);
                                        }
                                    } else {
                                        if (time < REVENUE_MAX_SAVE) {
                                            resolver.insert(CONTENT_URI, values);
                                        } else {
                                            Log.d("mydb", "更新操作！");
                                            String selection = DBMetaData.RevenueEntry._ID;
                                            String[] selectionArgs = {
                                                    buffers.get(0).getId() + ""
                                            };
                                            resolver.update(CONTENT_URI, values, selection, selectionArgs);
                                        }
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        for (final Revenue revenue : revenues) {
                            final ContentValues values = getContentValues(revenue);
                            getRevenueCallBack(revenue.getTime(), new GetRevenueCallback() {
                                @Override
                                public void onDBBaseDataLoaded(Revenue revenue) {
                                    String selection = DBMetaData.RevenueEntry.TIME;
                                    String[] selectionArgs = {
                                            revenue.getTime()
                                    };
                                    resolver.update(CONTENT_URI, values, selection, selectionArgs);
                                }

                                @Override
                                public void onDataNotAailable() {
                                    resolver.insert(CONTENT_URI, values);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onDataNotAailable() {
                    for (Revenue revenue : revenues) {
                        ContentValues values = getContentValues(revenue);
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
        String selection = DBMetaData.RevenueEntry._ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        resolver.delete(CONTENT_URI, selection, selectionArgs);
    }

    @Override
    public void getRevenueCallBack(String time, GetRevenueCallback callback) {
        Revenue revenue = null;
        String selection = DBMetaData.RevenueEntry.TIME + "=?";
        String[] selectionArgs = {
                time
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.RevenueEntry.TIME + DBMetaData.ASC);
        if (null != c && c.getCount() > 0) {
            c.moveToFirst();
            revenue = getRevenue(c);
        }
        if (null != c) {
            c.close();
        }
        if (null == revenue) {
            callback.onDataNotAailable();
        } else {
            callback.onDBBaseDataLoaded(revenue);
        }
    }

}
