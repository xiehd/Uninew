package com.uninew.car.db.message;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.uninew.car.db.main.DBBaseDataSource;
import com.uninew.car.db.main.DBMetaData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/21 0021.
 */

public class AnswerLocalDataSource implements AnswerLocalSource {


    private static volatile AnswerLocalDataSource INSTANCE;
    private WeakReference<Context> wp;//防止context内存泄露的优化
    /**
     * contentprovider连接文本信息的uri
     **/
    private static final Uri CONTENT_URI = Uri.parse("content://" + DBMetaData.AUTHORITIES + "/"
            + DBMetaData.AnswerEntry.TABLE_NAME + "/");
    private ContentResolver resolver;//ContentProvider操作类


    private AnswerLocalDataSource(Context context) {
        wp = new WeakReference<>(context);
        resolver = wp.get().getContentResolver();
    }

    public static AnswerLocalDataSource getInstance(Context context) {
        if (INSTANCE != null) {

        } else {
            synchronized (AnswerLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AnswerLocalDataSource(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAnswersByMsgId(int msgId, LoadAnswersCallBack callBack) {
        List<CarMessage.AnswerMsg> answerMsgs = new ArrayList<>();
        String selection = DBMetaData.AnswerEntry.MSG_ID + "=?";
        String[] selectionArgs = {
                msgId + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.AnswerEntry._ID + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                CarMessage.AnswerMsg answerMsg = new CarMessage.AnswerMsg();
                answerMsg.setId(c.getInt(c.getColumnIndex(DBMetaData.AnswerEntry._ID)));
                answerMsg.setAnswerId(c.getInt(c.getColumnIndex(DBMetaData.AnswerEntry.ANSWER_ID)));
                answerMsg.setAnswerContent(c.getString(c.getColumnIndex(DBMetaData.AnswerEntry.ANSWER_CONTENT)));
                answerMsgs.add(answerMsg);
            }
        }
        if (c != null) {
            c.close();
        }
        if (answerMsgs.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(answerMsgs);
        }
    }

    @Override
    public void setAnswersByMsgId(final int msgId, final CarMessage.AnswerMsg... answerMsgs) {
        getAnswersByMsgId(msgId, new LoadAnswersCallBack() {
            @Override
            public void onDBBaseDataLoaded(List<CarMessage.AnswerMsg> buffers) {
                for (CarMessage.AnswerMsg answerMsg : buffers) {
                    deleteDBData(answerMsg.getId());
                }
                if (answerMsgs != null && answerMsgs.length > 0) {
                    for (CarMessage.AnswerMsg answerMsg : answerMsgs) {
                        ContentValues values = new ContentValues();
                        values.put(DBMetaData.AnswerEntry.MSG_ID, msgId);
                        values.put(DBMetaData.AnswerEntry.ANSWER_ID, answerMsg.getAnswerId());
                        values.put(DBMetaData.AnswerEntry.ANSWER_CONTENT, answerMsg.getAnswerContent());
                        resolver.insert(CONTENT_URI, values);
                    }
                }
            }

            @Override
            public void onDataNotAailable() {
                if (answerMsgs != null && answerMsgs.length > 0) {
                    for (CarMessage.AnswerMsg answerMsg : answerMsgs) {
                        ContentValues values = new ContentValues();
                        values.put(DBMetaData.AnswerEntry.MSG_ID, msgId);
                        values.put(DBMetaData.AnswerEntry.ANSWER_ID, answerMsg.getAnswerId());
                        values.put(DBMetaData.AnswerEntry.ANSWER_CONTENT, answerMsg.getAnswerContent());
                        resolver.insert(CONTENT_URI, values);
                    }
                }
            }
        });

    }

    @Override
    public void getgetAnswerByMsgIdAndAnswerId(int msgId, int answerId, GetAnswesCallBack callBack) {
        CarMessage.AnswerMsg answerMsg = null;
        String selection = DBMetaData.AnswerEntry.MSG_ID + "=?"
                + DBMetaData.AND + DBMetaData.AnswerEntry.ANSWER_ID + "=?";
        String[] selectionArgs = {
                msgId + "",
                answerId + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.AnswerEntry._ID + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            answerMsg = new CarMessage.AnswerMsg();
            answerMsg.setId(c.getInt(c.getColumnIndex(DBMetaData.AnswerEntry._ID)));
            answerMsg.setAnswerId(c.getInt(c.getColumnIndex(DBMetaData.AnswerEntry.ANSWER_ID)));
            answerMsg.setAnswerContent(c.getString(c.getColumnIndex(DBMetaData.AnswerEntry.ANSWER_CONTENT)));
        }
        if (c != null) {
            c.close();
        }
        if (answerMsg == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(answerMsg);
        }
    }

    @Override
    @Deprecated
    public void getAllDBDatas(@NonNull LoadDBDataCallBack callBack) {

    }

    @Override
    @Deprecated
    public void getDBData(int _id, @NonNull GetDBDataCallBack callBack) {

    }

    @Override
    @Deprecated
    public void saveDBData(@NonNull CarMessage.AnswerMsg... t) {

    }

    @Override
    public void deleteAllDBDatas() {
        resolver.delete(CONTENT_URI, null, null);
    }

    @Override
    public void deleteDBData(@NonNull int id) {
        String selection = DBMetaData.AnswerEntry._ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        resolver.delete(CONTENT_URI, selection, selectionArgs);
    }
}
