package com.uninew.car.db.audio;

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
 * Created by Administrator on 2017/11/10 0010.
 */

public class AudioLocalDataSource implements AudioLocalSource {


    private static volatile AudioLocalDataSource INSTANCE;
    private WeakReference<Context> wp;//防止context内存泄露的优化
    /**
     * contentprovider连接文本信息的uri
     **/
    private static final Uri CONTENT_URI = Uri.parse("content://" + DBMetaData.AUTHORITIES + "/"
            + DBMetaData.AudioEntry.TABLE_NAME + "/");
    private ContentResolver resolver;//ContentProvider操作类


    /* 数据库保存最多天数 （7天）*/
    private static final long ORDER_MAX_SAVE = 604800000L;

    private AudioLocalDataSource(Context context) {
        wp = new WeakReference<>(context);
        resolver = wp.get().getContentResolver();
    }

    public static AudioLocalDataSource getInstance(Context context) {
        if (INSTANCE != null) {

        } else {
            synchronized (AudioLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AudioLocalDataSource(context);
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public void getAllDBDatas(@NonNull LoadDBDataCallBack callBack) {
        List<AudioData> audioDatas = new ArrayList<>();
        Cursor c = resolver.query(CONTENT_URI, null, null, null, DBMetaData.AudioEntry.START_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                audioDatas.add(getData(c));
            }
        }
        if (c != null) {
            c.close();
        }
        if (audioDatas.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(audioDatas);
        }
    }

    @Override
    public void getDBData(int _id, @NonNull GetDBDataCallBack callBack) {
        String selection = DBMetaData.AudioEntry._ID + "=?";
        String[] selectionArgs = {
                _id + ""
        };
        AudioData audioDatas = null;
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.AudioEntry.START_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            audioDatas = getData(c);
        }
        if (c != null) {
            c.close();
        }
        if (audioDatas == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(audioDatas);
        }
    }

    @Override
    public void saveDBData(@NonNull AudioData... audioDatas) {
        if (audioDatas != null && audioDatas.length > 0) {
            for (final AudioData audioData : audioDatas) {
                final ContentValues values = getValues(audioData);
                getAllDBDatas(new LoadAudiosCallBack() {
                    @Override
                    public void onDBBaseDataLoaded(List<AudioData> buffers) {
                        if ((buffers.get(buffers.size() - 1).getStartTime()
                                - buffers.get(0).getStartTime()) > ORDER_MAX_SAVE) {
                            String selection = DBMetaData.PhotoEntry._ID + "=?";
                            String[] selectionArgs = {
                                    buffers.get(0).getId() + ""
                            };
                            resolver.update(CONTENT_URI, values, selection, selectionArgs);
                        } else {
                            getAudiosByStartTime(audioData.getStartTime(), new GetAudioCallBack() {
                                @Override
                                public void onDBBaseDataLoaded(AudioData audioData) {
                                    String selection = DBMetaData.PhotoEntry._ID + "=?";
                                    String[] selectionArgs = {
                                            audioData.getStartTime() + ""
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
        String selection = DBMetaData.AudioEntry._ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        resolver.delete(CONTENT_URI, selection, selectionArgs);
    }

    private ContentValues getValues(AudioData audio) {
        ContentValues values = new ContentValues();
        values.put(DBMetaData.AudioEntry.CAR_NAME, audio.getCarName());
        values.put(DBMetaData.AudioEntry.CODING_TYPE, audio.getCodingType());
        values.put(DBMetaData.AudioEntry.FILE_ID, audio.getFileId());
        values.put(DBMetaData.AudioEntry.ISU_ID, audio.getIsuId());
        values.put(DBMetaData.AudioEntry.START_LATITUDE, audio.getStartLatitude());
        values.put(DBMetaData.AudioEntry.START_LONGITUDE, audio.getStartLongitude());
        values.put(DBMetaData.AudioEntry.END_LATITUDE, audio.getEndLatitude());
        values.put(DBMetaData.AudioEntry.END_LONGITUDE, audio.getEndLongitude());
        values.put(DBMetaData.AudioEntry.AUDIO_BUFFERS, audio.getAudioBuffers());
        values.put(DBMetaData.AudioEntry.AUDIO_LENGTH, audio.getAudioLength());
        values.put(DBMetaData.AudioEntry.REASON, audio.getReason());
        values.put(DBMetaData.AudioEntry.REVENUE_ID, audio.getRevenueId());
        values.put(DBMetaData.AudioEntry.START_TIME, audio.getStartTime());
        values.put(DBMetaData.AudioEntry.END_TIME, audio.getEndTime());
        return values;
    }

    private AudioData getData(Cursor c) {
        AudioData audioData = new AudioData();
        audioData.setCarName(c.getString(c.getColumnIndex(DBMetaData.AudioEntry.CAR_NAME)));
        audioData.setCodingType(c.getInt(c.getColumnIndex(DBMetaData.AudioEntry.CODING_TYPE)));
        audioData.setFileId(c.getInt(c.getColumnIndex(DBMetaData.AudioEntry.FILE_ID)));
        audioData.setId(c.getInt(c.getColumnIndex(DBMetaData.AudioEntry._ID)));
        audioData.setIsuId(c.getString(c.getColumnIndex(DBMetaData.AudioEntry.ISU_ID)));
        audioData.setStartLatitude(c.getDouble(c.getColumnIndex(DBMetaData.AudioEntry.START_LATITUDE)));
        audioData.setStartLongitude(c.getDouble(c.getColumnIndex(DBMetaData.AudioEntry.START_LONGITUDE)));
        audioData.setEndLatitude(c.getDouble(c.getColumnIndex(DBMetaData.AudioEntry.END_LATITUDE)));
        audioData.setEndLongitude(c.getDouble(c.getColumnIndex(DBMetaData.AudioEntry.END_LONGITUDE)));
        audioData.setAudioBuffers(c.getBlob(c.getColumnIndex(DBMetaData.AudioEntry.AUDIO_BUFFERS)));
        audioData.setAudioLength(c.getInt(c.getColumnIndex(DBMetaData.AudioEntry.AUDIO_LENGTH)));
        audioData.setReason(c.getInt(c.getColumnIndex(DBMetaData.AudioEntry.REASON)));
        audioData.setRevenueId(c.getInt(c.getColumnIndex(DBMetaData.AudioEntry.REVENUE_ID)));
        audioData.setStartTime(c.getLong(c.getColumnIndex(DBMetaData.AudioEntry.START_TIME)));
        audioData.setEndTime(c.getLong(c.getColumnIndex(DBMetaData.AudioEntry.END_TIME)));
        return audioData;
    }

    @Override
    public void getAudiosByStartTime(long time, GetAudioCallBack callBack) {
        String selection = DBMetaData.AudioEntry.START_TIME + "=?";
        String[] selectionArgs = {
                time + ""
        };
        AudioData audioDatas = null;
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.AudioEntry.START_TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            audioDatas = getData(c);
        }
        if (c != null) {
            c.close();
        }
        if (audioDatas == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(audioDatas);
        }
    }
}
