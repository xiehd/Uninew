package com.uninew.car.db.photo;

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
 * Created by Administrator on 2017/11/1 0001.
 */

public class PhotoLocalDataSource implements PhotoLocalSource {


    private static volatile PhotoLocalDataSource INSTANCE;
    private WeakReference<Context> wp;//防止context内存泄露的优化
    /**
     * contentprovider连接文本信息的uri
     **/
    private static final Uri CONTENT_URI = Uri.parse("content://" + DBMetaData.AUTHORITIES + "/"
            + DBMetaData.PhotoEntry.TABLE_NAME + "/");
    private ContentResolver resolver;//ContentProvider操作类

    /* 数据库保存最多天数 （7天）*/
    private static final long ORDER_MAX_SAVE = 1000 * 60 * 60 * 24 * 7L;

    private PhotoLocalDataSource(@NonNull Context context) {
        wp = new WeakReference<>(context);
        resolver = wp.get().getContentResolver();
    }

    public static PhotoLocalDataSource getInstance(Context context) {
        if (INSTANCE != null) {

        } else {
            synchronized (PhotoLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PhotoLocalDataSource(context);
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public void searchPhotos(int cameraId,int reason, long startTime, long endTime, LoadPhotosCallBack callBack) {
        List<PhotoData> photoDatas = new ArrayList<>();
        String selection = DBMetaData.PhotoEntry.REASON + "=?"
                +DBMetaData.AND + DBMetaData.PhotoEntry.CAMERA_ID + "=?";
        String[] selectionArgs = {
                reason + "",
                cameraId+""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.PhotoEntry.TIME + DBMetaData.ASC);
        if (null != c && c.getCount() > 0) {
            while (c.moveToNext()) {
                PhotoData photoData = getData(c);
                if (photoData.getTime() >= startTime || photoData.getTime() <= endTime) {
                    photoDatas.add(photoData);
                }
            }
        }
        if (c != null) {
            c.close();
        }
        if (photoDatas.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(photoDatas);
        }
    }

    @Override
    public void getPhotosByTime(long time, GetPhotoCallBack callBack) {
        PhotoData photoData = null;
        String selection = DBMetaData.PhotoEntry.TIME + "=?";
        String[] selectionArgs = {
                time + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.PhotoEntry.TIME + DBMetaData.ASC);
        if (null != c && c.getCount() > 0) {
            c.moveToFirst();
            photoData = getData(c);
        }
        if (c != null) {
            c.close();
        }
        if (photoData == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(photoData);
        }
    }

    @Override
    public void getAllDBDatas(@NonNull LoadDBDataCallBack callBack) {
        List<PhotoData> photoDatas = new ArrayList<>();
        Cursor c = resolver.query(CONTENT_URI, null, null, null, DBMetaData.PhotoEntry.TIME + DBMetaData.ASC);
        if (null != c && c.getCount() > 0) {
            while (c.moveToNext()) {
                photoDatas.add(getData(c));
            }
        }
        if (c != null) {
            c.close();
        }
        if (photoDatas.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(photoDatas);
        }
    }

    @Override
    public void getDBData(int _id, @NonNull GetDBDataCallBack callBack) {
        PhotoData photoData = null;
        String selection = DBMetaData.PhotoEntry._ID + "=?";
        String[] selectionArgs = {
                _id + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.PhotoEntry.TIME + DBMetaData.ASC);
        if (null != c && c.getCount() > 0) {
            c.moveToFirst();
            photoData = getData(c);
        }
        if (c != null) {
            c.close();
        }
        if (photoData == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(photoData);
        }
    }

    @Override
    public void saveDBData(@NonNull PhotoData... photoDatas) {
        if (photoDatas != null && photoDatas.length > 0) {
            for (final PhotoData photoData : photoDatas) {
                final ContentValues values = getValues(photoData);
                getAllDBDatas(new LoadPhotosCallBack() {
                    @Override
                    public void onDBBaseDataLoaded(List<PhotoData> buffers) {
                        if ((buffers.get(buffers.size() - 1).getTime()
                                - buffers.get(0).getTime()) > ORDER_MAX_SAVE) {
                            String selection = DBMetaData.PhotoEntry._ID + "=?";
                            String[] selectionArgs = {
                                    buffers.get(0).getId() + ""
                            };
                            resolver.update(CONTENT_URI, values, selection, selectionArgs);
                        } else {
                            getPhotosByTime(photoData.getTime(), new GetPhotoCallBack() {
                                @Override
                                public void onDBBaseDataLoaded(PhotoData photoData) {
                                    String selection = DBMetaData.PhotoEntry.TIME + "=?";
                                    String[] selectionArgs = {
                                            photoData.getTime() + ""
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

                    @Override
                    public void onDataNotAailable() {
                        resolver.insert(CONTENT_URI, values);
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
        String selection = DBMetaData.PhotoEntry._ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        resolver.delete(CONTENT_URI, selection, selectionArgs);
    }

    private ContentValues getValues(PhotoData photoData) {
        ContentValues values = new ContentValues();
        values.put(DBMetaData.PhotoEntry.CAR_NAME, photoData.getCarName());
        values.put(DBMetaData.PhotoEntry.CODING_TYPE, photoData.getCodingType());
        values.put(DBMetaData.PhotoEntry.FILE_ID, photoData.getFileId());
        values.put(DBMetaData.PhotoEntry.ISU_ID, photoData.getIsuId());
        values.put(DBMetaData.PhotoEntry.LATITUDE, photoData.getLatitude());
        values.put(DBMetaData.PhotoEntry.LONGITUDE, photoData.getLongitude());
        values.put(DBMetaData.PhotoEntry.PHOTO_BUFFERS, photoData.getPhotoBuffers());
        values.put(DBMetaData.PhotoEntry.PHOTO_LENGTH, photoData.getPhotoLength());
        values.put(DBMetaData.PhotoEntry.REASON, photoData.getReason());
        values.put(DBMetaData.PhotoEntry.REVENUE_ID, photoData.getRevenueId());
        values.put(DBMetaData.PhotoEntry.TIME, photoData.getTime());
        values.put(DBMetaData.PhotoEntry.CAMERA_ID,photoData.getCameraId());
        return values;
    }

    private PhotoData getData(Cursor c) {
        PhotoData photoData = new PhotoData();
        photoData.setCarName(c.getString(c.getColumnIndex(DBMetaData.PhotoEntry.CAR_NAME)));
        photoData.setCodingType(c.getInt(c.getColumnIndex(DBMetaData.PhotoEntry.CODING_TYPE)));
        photoData.setFileId(c.getInt(c.getColumnIndex(DBMetaData.PhotoEntry.FILE_ID)));
        photoData.setId(c.getInt(c.getColumnIndex(DBMetaData.PhotoEntry._ID)));
        photoData.setIsuId(c.getString(c.getColumnIndex(DBMetaData.PhotoEntry.ISU_ID)));
        photoData.setLatitude(c.getDouble(c.getColumnIndex(DBMetaData.PhotoEntry.LATITUDE)));
        photoData.setLongitude(c.getDouble(c.getColumnIndex(DBMetaData.PhotoEntry.LONGITUDE)));
        photoData.setPhotoBuffers(c.getBlob(c.getColumnIndex(DBMetaData.PhotoEntry.PHOTO_BUFFERS)));
        photoData.setPhotoLength(c.getInt(c.getColumnIndex(DBMetaData.PhotoEntry.PHOTO_LENGTH)));
        photoData.setReason(c.getInt(c.getColumnIndex(DBMetaData.PhotoEntry.REASON)));
        photoData.setRevenueId(c.getInt(c.getColumnIndex(DBMetaData.PhotoEntry.REVENUE_ID)));
        photoData.setTime(c.getLong(c.getColumnIndex(DBMetaData.PhotoEntry.TIME)));
        photoData.setCameraId(c.getInt(c.getColumnIndex(DBMetaData.PhotoEntry.CAMERA_ID)));
        return photoData;
    }
}
