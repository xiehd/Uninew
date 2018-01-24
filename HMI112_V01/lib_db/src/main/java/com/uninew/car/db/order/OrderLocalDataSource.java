package com.uninew.car.db.order;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.uninew.car.db.main.DBMetaData;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import tools.TimeTool;

/**
 * Created by Administrator on 2017/8/29 0029.
 */

public class OrderLocalDataSource implements OrderLocalSource {

    private static volatile OrderLocalDataSource INSTANCE;
    private WeakReference<Context> wp;//防止context内存泄露的优化
    /**
     * contentprovider连接文本信息的uri
     **/
    private static final Uri CONTENT_URI = Uri.parse("content://" + DBMetaData.AUTHORITIES + "/"
            + DBMetaData.OrderEntry.TABLE_NAME + "/");
    private ContentResolver resolver;//ContentProvider操作类

    /* 数据库保存最多天数 （7天）*/
    private static final long ORDER_MAX_SAVE = 604800000L;

    private OrderLocalDataSource(@NonNull Context context) {
        wp = new WeakReference<>(context);
        resolver = wp.get().getContentResolver();
    }

    public static OrderLocalDataSource getInstance(Context context) {
        if (INSTANCE != null) {

        } else {
            synchronized (OrderLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new OrderLocalDataSource(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getOrdersByState(@NonNull LoadOrdersCallback callBack, int... state) {
        List<Order> orders = new ArrayList<>();
        String selection = null;
        String[] selectionArgs = null;
        if (state != null && state.length > 0) {
            int length = state.length;
            StringBuilder buffer = new StringBuilder();
            selectionArgs = new String[length];
            for (int i = 0; i < length; i++) {
                if (i == 0) {
                    buffer.append(DBMetaData.OrderEntry.ORDER_STATE + "=?");
                } else {
                    buffer.append(DBMetaData.OR + DBMetaData.OrderEntry.ORDER_STATE + "=?");
                }
                selectionArgs[i] = String.valueOf(state[i]);
            }
            selection = buffer.toString();
        }

        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.OrderEntry.RECEIVE_TIME + DBMetaData.ASC);
        if (null != c && c.getCount() > 0) {
            while (c.moveToNext()) {
                Order order = new Order();
                order.setId(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry._ID)));
                order.setBusinessDescription(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.BUSINESS_DESCRIPTION)));
                order.setBusinessId(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry.BUSINESS_ID)));
                order.setBusinessType(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry.BUSINESS_TYPE)));
                order.setNeedTime(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.NEED_TIME)));
                order.setOrderState(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry.ORDER_STATE)));
                order.setServiceCharge(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.SERVICE_CHARGE)));
                order.setPassengerLatitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.PASSENGER_LATITUDE)));
                order.setPassengerLongitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.PASSENGER_LONGITUDE)));
                order.setPassengerPhoneNumber(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.PASSENGER_PHONE_NUMBER)));
                order.setReceiveTime(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.RECEIVE_TIME)));
                order.setTargatLatitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.TARGET_LATITUDE)));
                order.setTargatLongitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.TARGET_LONGITUDE)));
                orders.add(order);
            }
        }
        if (c != null) {
            c.close();
        }
        if (orders.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(orders);
        }
    }

    @Override
    public void getOrderByState(int id, int state, @NonNull GetOrderCallback callBack) {
        Order order = null;
        String selection = DBMetaData.OrderEntry.ORDER_STATE + "=?"
                + DBMetaData.AND + DBMetaData.OrderEntry._ID + "=?";
        String[] selectionArgs = {
                state + "",
                id + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.OrderEntry.RECEIVE_TIME + DBMetaData.ASC);
        if (null != c && c.getCount() > 0) {
            c.moveToFirst();
            order = new Order();
            order.setId(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry._ID)));
            order.setBusinessDescription(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.BUSINESS_DESCRIPTION)));
            order.setBusinessId(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry.BUSINESS_ID)));
            order.setBusinessType(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry.BUSINESS_TYPE)));
            order.setNeedTime(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.NEED_TIME)));
            order.setOrderState(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry.ORDER_STATE)));
            order.setServiceCharge(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.SERVICE_CHARGE)));
            order.setPassengerLatitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.PASSENGER_LATITUDE)));
            order.setPassengerLongitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.PASSENGER_LONGITUDE)));
            order.setPassengerPhoneNumber(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.PASSENGER_PHONE_NUMBER)));
            order.setReceiveTime(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.RECEIVE_TIME)));
            order.setTargatLatitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.TARGET_LATITUDE)));
            order.setTargatLongitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.TARGET_LONGITUDE)));
        }
        if (c != null) {
            c.close();
        }
        if (null == order) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(order);
        }
    }

    @Override
    public void getOrderByStateAndType(int type, int state, @NonNull LoadOrdersCallback callBack) {
        List<Order> orders = new ArrayList<>();
        String selection = DBMetaData.OrderEntry.ORDER_STATE + "=?"
                + DBMetaData.AND + DBMetaData.OrderEntry.BUSINESS_TYPE + "=?";
        String[] selectionArgs = {
                state + "",
                type + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.OrderEntry.RECEIVE_TIME + DBMetaData.ASC);
        if (null != c && c.getCount() > 0) {
            while (c.moveToNext()) {
                Order order = new Order();
                order.setId(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry._ID)));
                order.setBusinessDescription(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.BUSINESS_DESCRIPTION)));
                order.setBusinessId(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry.BUSINESS_ID)));
                order.setBusinessType(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry.BUSINESS_TYPE)));
                order.setNeedTime(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.NEED_TIME)));
                order.setOrderState(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry.ORDER_STATE)));
                order.setServiceCharge(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.SERVICE_CHARGE)));
                order.setPassengerLatitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.PASSENGER_LATITUDE)));
                order.setPassengerLongitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.PASSENGER_LONGITUDE)));
                order.setPassengerPhoneNumber(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.PASSENGER_PHONE_NUMBER)));
                order.setReceiveTime(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.RECEIVE_TIME)));
                order.setTargatLatitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.TARGET_LATITUDE)));
                order.setTargatLongitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.TARGET_LONGITUDE)));
                orders.add(order);
            }
        }
        if (c != null) {
            c.close();
        }
        if (orders.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(orders);
        }
    }

    @Override
    public void getOrderByWorkId(int id, @NonNull GetOrderCallback callBack) {
        Order order = null;
        String selection = DBMetaData.OrderEntry.BUSINESS_ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.OrderEntry.RECEIVE_TIME + DBMetaData.ASC);
        if (null != c && c.getCount() > 0) {
            c.moveToFirst();
            order = new Order();
            order.setId(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry._ID)));
            order.setBusinessDescription(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.BUSINESS_DESCRIPTION)));
            order.setBusinessId(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry.BUSINESS_ID)));
            order.setBusinessType(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry.BUSINESS_TYPE)));
            order.setNeedTime(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.NEED_TIME)));
            order.setOrderState(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry.ORDER_STATE)));
            order.setServiceCharge(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.SERVICE_CHARGE)));
            order.setPassengerLatitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.PASSENGER_LATITUDE)));
            order.setPassengerLongitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.PASSENGER_LONGITUDE)));
            order.setPassengerPhoneNumber(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.PASSENGER_PHONE_NUMBER)));
            order.setReceiveTime(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.RECEIVE_TIME)));
            order.setTargatLatitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.TARGET_LATITUDE)));
            order.setTargatLongitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.TARGET_LONGITUDE)));
        }
        if (c != null) {
            c.close();
        }
        if (null == order) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(order);
        }
    }

    @Override
    public void changeState(int workId, int state, String finishTime) {
        String selection = DBMetaData.OrderEntry.BUSINESS_ID + "=?";
        String[] selectionArgs = {
                workId + ""
        };
        ContentValues values = new ContentValues();
        values.put(DBMetaData.OrderEntry.ORDER_STATE, state);
        values.put(DBMetaData.OrderEntry.BUSINESS_ID, workId);
        values.put(DBMetaData.OrderEntry.RECEIVE_TIME, finishTime);
        resolver.update(CONTENT_URI, values, selection, selectionArgs);
    }

    @Override
    public void getAllDBDatas(@NonNull LoadDBDataCallBack callBack) {
        List<Order> orders = new ArrayList<>();
        Cursor c = resolver.query(CONTENT_URI, null, null, null, DBMetaData.OrderEntry.RECEIVE_TIME + DBMetaData.ASC);
        if (null != c && c.getCount() > 0) {
            while (c.moveToNext()) {
                Order order = new Order();
                order.setId(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry._ID)));
                order.setBusinessDescription(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.BUSINESS_DESCRIPTION)));
                order.setBusinessId(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry.BUSINESS_ID)));
                order.setBusinessType(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry.BUSINESS_TYPE)));
                order.setNeedTime(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.NEED_TIME)));
                order.setOrderState(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry.ORDER_STATE)));
                order.setServiceCharge(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.SERVICE_CHARGE)));
                order.setPassengerLatitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.PASSENGER_LATITUDE)));
                order.setPassengerLongitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.PASSENGER_LONGITUDE)));
                order.setPassengerPhoneNumber(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.PASSENGER_PHONE_NUMBER)));
                order.setReceiveTime(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.RECEIVE_TIME)));
                order.setTargatLatitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.TARGET_LATITUDE)));
                order.setTargatLongitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.TARGET_LONGITUDE)));
                orders.add(order);
            }
        }
        if (c != null) {
            c.close();
        }
        if (orders.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(orders);
        }
    }

    @Override
    public void getDBData(int _id, @NonNull GetDBDataCallBack callBack) {
        Order order = null;
        String selection = DBMetaData.OrderEntry._ID + "=?";
        String[] selectionArgs = {
                _id + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.OrderEntry.RECEIVE_TIME + DBMetaData.ASC);
        if (null != c && c.getCount() > 0) {
            c.moveToFirst();
            order = new Order();
            order.setId(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry._ID)));
            order.setBusinessDescription(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.BUSINESS_DESCRIPTION)));
            order.setBusinessId(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry.BUSINESS_ID)));
            order.setBusinessType(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry.BUSINESS_TYPE)));
            order.setNeedTime(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.NEED_TIME)));
            order.setOrderState(c.getInt(c.getColumnIndex(DBMetaData.OrderEntry.ORDER_STATE)));
            order.setServiceCharge(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.SERVICE_CHARGE)));
            order.setPassengerLatitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.PASSENGER_LATITUDE)));
            order.setPassengerLongitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.PASSENGER_LONGITUDE)));
            order.setPassengerPhoneNumber(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.PASSENGER_PHONE_NUMBER)));
            order.setReceiveTime(c.getString(c.getColumnIndex(DBMetaData.OrderEntry.RECEIVE_TIME)));
            order.setTargatLatitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.TARGET_LATITUDE)));
            order.setTargatLongitude(c.getDouble(c.getColumnIndex(DBMetaData.OrderEntry.TARGET_LONGITUDE)));
        }
        if (c != null) {
            c.close();
        }
        if (null == order) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(order);
        }
    }

    @Override
    public void saveDBData(@NonNull final Order... orders) {
        if (null != orders && orders.length > 0) {
            getAllDBDatas(new LoadOrdersCallback() {
                @Override
                public void onDBBaseDataLoaded(final List<Order> buffers) {
                    try {
                        final long time = TimeTool.parseToLong(buffers.get(buffers.size() - 1).getReceiveTime())
                                - TimeTool.parseToLong(buffers.get(0).getReceiveTime());
                        int size = orders.length;
                        for (int i = 0; i < size; i++) {
                            final Order order = orders[i];
                            final ContentValues values = new ContentValues();
                            values.put(DBMetaData.OrderEntry.RECEIVE_TIME, order.getReceiveTime());
                            values.put(DBMetaData.OrderEntry.BUSINESS_ID, order.getBusinessId());
                            values.put(DBMetaData.OrderEntry.ORDER_STATE, order.getOrderState());
                            values.put(DBMetaData.OrderEntry.BUSINESS_DESCRIPTION, order.getBusinessDescription());
                            values.put(DBMetaData.OrderEntry.SERVICE_CHARGE, order.getServiceCharge());
                            values.put(DBMetaData.OrderEntry.BUSINESS_TYPE, order.getBusinessType());
                            values.put(DBMetaData.OrderEntry.NEED_TIME, order.getNeedTime());
                            values.put(DBMetaData.OrderEntry.PASSENGER_LATITUDE, order.getPassengerLatitude());
                            values.put(DBMetaData.OrderEntry.PASSENGER_LONGITUDE, order.getPassengerLongitude());
                            values.put(DBMetaData.OrderEntry.PASSENGER_PHONE_NUMBER, order.getPassengerPhoneNumber());
                            values.put(DBMetaData.OrderEntry.TARGET_LATITUDE, order.getTargatLatitude());
                            values.put(DBMetaData.OrderEntry.TARGET_LONGITUDE, order.getTargatLongitude());
                            getOrderByWorkId(order.getBusinessId(), new GetOrderCallback() {
                                @Override
                                public void onDBBaseDataLoaded(Order order) {
                                    String selection = DBMetaData.OrderEntry.BUSINESS_ID + "=?";
                                    String[] selectionArgs = {
                                            order.getBusinessId() + ""
                                    };
                                    resolver.update(CONTENT_URI, values, selection, selectionArgs);
                                }

                                @Override
                                public void onDataNotAailable() {
                                    if (time < ORDER_MAX_SAVE) {
                                        resolver.insert(CONTENT_URI, values);
                                    } else {

                                        String selection = DBMetaData.OrderEntry._ID + "=?";
                                        String[] selectionArgs = {
                                                order.getId() + ""
                                        };
                                        resolver.update(CONTENT_URI, values, selection, selectionArgs);
                                    }
                                }
                            });
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        for (final Order order : orders) {
                            final ContentValues values = new ContentValues();
                            values.put(DBMetaData.OrderEntry.RECEIVE_TIME, order.getReceiveTime());
                            values.put(DBMetaData.OrderEntry.BUSINESS_ID, order.getBusinessId());
                            values.put(DBMetaData.OrderEntry.ORDER_STATE, order.getOrderState());
                            values.put(DBMetaData.OrderEntry.BUSINESS_DESCRIPTION, order.getBusinessDescription());
                            values.put(DBMetaData.OrderEntry.SERVICE_CHARGE, order.getServiceCharge());
                            values.put(DBMetaData.OrderEntry.BUSINESS_TYPE, order.getBusinessType());
                            values.put(DBMetaData.OrderEntry.NEED_TIME, order.getNeedTime());
                            values.put(DBMetaData.OrderEntry.PASSENGER_LATITUDE, order.getPassengerLatitude());
                            values.put(DBMetaData.OrderEntry.PASSENGER_LONGITUDE, order.getPassengerLongitude());
                            values.put(DBMetaData.OrderEntry.PASSENGER_PHONE_NUMBER, order.getPassengerPhoneNumber());
                            values.put(DBMetaData.OrderEntry.TARGET_LATITUDE, order.getTargatLatitude());
                            values.put(DBMetaData.OrderEntry.TARGET_LONGITUDE, order.getTargatLongitude());
                            getOrderByWorkId(order.getBusinessId(), new GetOrderCallback() {
                                @Override
                                public void onDBBaseDataLoaded(Order order) {
                                    String selection = DBMetaData.OrderEntry.BUSINESS_ID + "=?";
                                    String[] selectionArgs = {
                                            order.getBusinessId() + ""
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
                    for (Order order : orders) {
                        ContentValues values = new ContentValues();
                        values.put(DBMetaData.OrderEntry.RECEIVE_TIME, order.getReceiveTime());
                        values.put(DBMetaData.OrderEntry.BUSINESS_ID, order.getBusinessId());
                        values.put(DBMetaData.OrderEntry.ORDER_STATE, order.getOrderState());
                        values.put(DBMetaData.OrderEntry.BUSINESS_DESCRIPTION, order.getBusinessDescription());
                        values.put(DBMetaData.OrderEntry.SERVICE_CHARGE, order.getServiceCharge());
                        values.put(DBMetaData.OrderEntry.BUSINESS_TYPE, order.getBusinessType());
                        values.put(DBMetaData.OrderEntry.NEED_TIME, order.getNeedTime());
                        values.put(DBMetaData.OrderEntry.PASSENGER_LATITUDE, order.getPassengerLatitude());
                        values.put(DBMetaData.OrderEntry.PASSENGER_LONGITUDE, order.getPassengerLongitude());
                        values.put(DBMetaData.OrderEntry.PASSENGER_PHONE_NUMBER, order.getPassengerPhoneNumber());
                        values.put(DBMetaData.OrderEntry.TARGET_LATITUDE, order.getTargatLatitude());
                        values.put(DBMetaData.OrderEntry.TARGET_LONGITUDE, order.getTargatLongitude());
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
        String selection = DBMetaData.OrderEntry._ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        resolver.delete(CONTENT_URI, selection, selectionArgs);
    }
}
