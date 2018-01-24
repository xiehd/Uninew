package com.uninew.car.db.message;

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
 * Created by Administrator on 2017/8/31 0031.
 */

public class MessageLocalDataSource implements MessageLocalSource {

    private static volatile MessageLocalDataSource INSTANCE;
    private WeakReference<Context> wp;//防止context内存泄露的优化
    /**
     * contentprovider连接文本信息的uri
     **/
    private static final Uri CONTENT_URI = Uri.parse("content://" + DBMetaData.AUTHORITIES + "/"
            + DBMetaData.MessageEntry.TABLE_NAME + "/");
    /**
     * contentprovider连接文本信息的uri
     **/
    private static final Uri CONTENT_URI_ANSWER = Uri.parse("content://" + DBMetaData.AUTHORITIES + "/"
            + DBMetaData.AnswerEntry.TABLE_NAME + "/");
    private ContentResolver resolver;//ContentProvider操作类

    /* 数据表最多保存天数 （30天）*/
    private static final long MESSAGE_MAX_SAVE = 2592000000l;

    private MessageLocalDataSource(Context context) {
        wp = new WeakReference<>(context);
        resolver = wp.get().getContentResolver();
    }

    public static MessageLocalDataSource getInstance(Context context) {
        if (INSTANCE != null) {

        } else {
            synchronized (MessageLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MessageLocalDataSource(context);
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public void changeAnswerState(int problemId, int answerState, int answerId) {
        String selection = DBMetaData.MessageEntry.MESSAGE_TYPE + "=?"
                + DBMetaData.AND + DBMetaData.MessageEntry.MESSAGE_ID + "=?";
        String[] selectionArgs = {
                MessageKey.MessageTypeKey.QUESTION_TYPE + "",
                problemId + ""
        };
        ContentValues values = new ContentValues();
        values.put(DBMetaData.MessageEntry.ANSWER_STATE, answerState);
        values.put(DBMetaData.MessageEntry.ANSWER_ID, answerId);
        resolver.update(CONTENT_URI, values, selection, selectionArgs);
    }

    @Override
    public void changeEventContent(int eventId, String content) {
        String selection = DBMetaData.MessageEntry.MESSAGE_TYPE + "=?"
                + DBMetaData.AND + DBMetaData.MessageEntry.MESSAGE_ID + "=?";
        String[] selectionArgs = {
                MessageKey.MessageTypeKey.EVENT_TYPE + "",
                eventId + ""
        };
        ContentValues values = new ContentValues();
        values.put(DBMetaData.MessageEntry.CONTENT, content);
        resolver.update(CONTENT_URI, values, selection, selectionArgs);
    }

    @Override
    public void getAllMessagesByType(LoadMessageCallBack callBack, int... type) {
        List<CarMessage> carMessages = new ArrayList<>();
        String selection = null;
        String[] selectionArgs = null;
        if (type != null && type.length > 0) {
            int length = type.length;
            StringBuilder buffer = new StringBuilder();
            selectionArgs = new String[length];
            for (int i = 0; i < length; i++) {
                if (i == 0) {
                    buffer.append(DBMetaData.MessageEntry.MESSAGE_TYPE + "=?");
                } else {
                    buffer.append(DBMetaData.OR + DBMetaData.MessageEntry.MESSAGE_TYPE + "=?");
                }
                selectionArgs[i] = String.valueOf(type[i]);
            }
            selection = buffer.toString();
        }
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.MessageEntry.TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int msgType = c.getInt(c.getColumnIndex(DBMetaData.MessageEntry.MESSAGE_TYPE));
                int msgId = c.getInt(c.getColumnIndex(DBMetaData.MessageEntry.MESSAGE_ID));
                CarMessage carMessage = new CarMessage();
                carMessage.setId(c.getInt(c.getColumnIndex(DBMetaData.MessageEntry._ID)));
                carMessage.setAnswerState(c.getInt(c.getColumnIndex(DBMetaData.MessageEntry.ANSWER_STATE)));
                carMessage.setContent(c.getString(c.getColumnIndex(DBMetaData.MessageEntry.CONTENT)));
                carMessage.setMessageType(c.getInt(c.getColumnIndex(DBMetaData.MessageEntry.MESSAGE_TYPE)));
                carMessage.setTime(c.getString(c.getColumnIndex(DBMetaData.MessageEntry.TIME)));
                carMessage.setReadState(c.getInt(c.getColumnIndex(DBMetaData.MessageEntry.READ_STATE)));
                carMessage.setMassgeId(msgId);
                if (msgType == MessageKey.MessageTypeKey.QUESTION_TYPE) {
                    List<CarMessage.AnswerMsg> answerMsgs = new ArrayList<>();
                    String selection1 = DBMetaData.AnswerEntry.MSG_ID + "=?";
                    String[] selectionArgs1 = {
                            msgId + ""
                    };
                    Log.d("msg", "msgId:" + msgId);
                    Cursor c1 = resolver.query(CONTENT_URI_ANSWER, null, selection1, selectionArgs1, DBMetaData.AnswerEntry._ID + DBMetaData.ASC);
                    if (c1 != null && c1.getCount() > 0) {
                        while (c1.moveToNext()) {
                            CarMessage.AnswerMsg answerMsg = new CarMessage.AnswerMsg();
                            answerMsg.setId(c1.getInt(c1.getColumnIndex(DBMetaData.AnswerEntry._ID)));
                            answerMsg.setAnswerId(c1.getInt(c1.getColumnIndex(DBMetaData.AnswerEntry.ANSWER_ID)));
                            answerMsg.setAnswerContent(c1.getString(c1.getColumnIndex(DBMetaData.AnswerEntry.ANSWER_CONTENT)));
                            Log.d("msg", answerMsg.toString());
                            answerMsgs.add(answerMsg);
                        }
                    }
                    if (c1 != null) {
                        c1.close();
                    }
                    carMessage.setAnswerSelectgList(answerMsgs);
                }
                carMessages.add(carMessage);
            }
        }
        if (c != null) {
            c.close();
        }
        if (carMessages.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(carMessages);
        }
    }

    @Override
    public void changeReadState(int id, int state) {
        String selection = DBMetaData.MessageEntry._ID + "=?";
        String[] selectionArgs = {
                id + "",
        };
        ContentValues values = new ContentValues();
        values.put(DBMetaData.MessageEntry.READ_STATE, state);
        resolver.update(CONTENT_URI, values, selection, selectionArgs);
    }

    @Override
    public void getAllDBDatas(@NonNull LoadDBDataCallBack callBack) {
        List<CarMessage> carMessages = new ArrayList<>();
        Cursor c = resolver.query(CONTENT_URI, null, null, null, DBMetaData.MessageEntry.TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int msgType = c.getInt(c.getColumnIndex(DBMetaData.MessageEntry.MESSAGE_TYPE));
                int msgId = c.getInt(c.getColumnIndex(DBMetaData.MessageEntry.MESSAGE_ID));
                CarMessage carMessage = new CarMessage();
                carMessage.setId(c.getInt(c.getColumnIndex(DBMetaData.MessageEntry._ID)));
                carMessage.setAnswerState(c.getInt(c.getColumnIndex(DBMetaData.MessageEntry.ANSWER_STATE)));
                carMessage.setContent(c.getString(c.getColumnIndex(DBMetaData.MessageEntry.CONTENT)));
                carMessage.setMessageType(c.getInt(c.getColumnIndex(DBMetaData.MessageEntry.MESSAGE_TYPE)));
                carMessage.setTime(c.getString(c.getColumnIndex(DBMetaData.MessageEntry.TIME)));
                carMessage.setReadState(c.getInt(c.getColumnIndex(DBMetaData.MessageEntry.READ_STATE)));
                carMessage.setMassgeId(msgId);
                if (msgType == MessageKey.MessageTypeKey.QUESTION_TYPE) {
                    List<CarMessage.AnswerMsg> answerMsgs = new ArrayList<>();
                    String selection1 = DBMetaData.AnswerEntry.MSG_ID + "=?";
                    String[] selectionArgs1 = {
                            msgId + ""
                    };
                    Cursor c1 = resolver.query(CONTENT_URI_ANSWER, null, selection1, selectionArgs1, DBMetaData.AnswerEntry._ID + DBMetaData.ASC);
                    if (c1 != null && c1.getCount() > 0) {
                        while (c1.moveToNext()) {
                            CarMessage.AnswerMsg answerMsg = new CarMessage.AnswerMsg();
                            answerMsg.setId(c1.getInt(c1.getColumnIndex(DBMetaData.AnswerEntry._ID)));
                            answerMsg.setAnswerId(c1.getInt(c1.getColumnIndex(DBMetaData.AnswerEntry.ANSWER_ID)));
                            answerMsg.setAnswerContent(c1.getString(c1.getColumnIndex(DBMetaData.AnswerEntry.ANSWER_CONTENT)));
                            answerMsgs.add(answerMsg);
                        }
                    }
                    if (c1 != null) {
                        c1.close();
                    }
                    carMessage.setAnswerSelectgList(answerMsgs);
                }
                carMessages.add(carMessage);
            }
        }
        if (c != null) {
            c.close();
        }
        if (carMessages.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(carMessages);
        }
    }

    @Override
    public void getDBData(int _id, @NonNull GetDBDataCallBack callBack) {
        CarMessage carMessage = null;
        String selection = DBMetaData.MessageEntry._ID + "=?";
        String[] selectionArgs = {
                _id + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.MessageEntry.TIME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            int msgType = c.getInt(c.getColumnIndex(DBMetaData.MessageEntry.MESSAGE_TYPE));
            int msgId = c.getInt(c.getColumnIndex(DBMetaData.MessageEntry.MESSAGE_ID));
            carMessage = new CarMessage();
            carMessage.setId(c.getInt(c.getColumnIndex(DBMetaData.MessageEntry._ID)));
            carMessage.setAnswerState(c.getInt(c.getColumnIndex(DBMetaData.MessageEntry.ANSWER_STATE)));
            carMessage.setContent(c.getString(c.getColumnIndex(DBMetaData.MessageEntry.CONTENT)));
            carMessage.setMessageType(msgType);
            carMessage.setTime(c.getString(c.getColumnIndex(DBMetaData.MessageEntry.TIME)));
            carMessage.setAnswerId(c.getInt(c.getColumnIndex(DBMetaData.MessageEntry.ANSWER_ID)));
            carMessage.setReadState(c.getInt(c.getColumnIndex(DBMetaData.MessageEntry.READ_STATE)));
            carMessage.setMassgeId(msgId);
            if (msgType == MessageKey.MessageTypeKey.QUESTION_TYPE) {
                List<CarMessage.AnswerMsg> answerMsgs = new ArrayList<>();
                String selection1 = DBMetaData.AnswerEntry.MSG_ID + "=?";
                String[] selectionArgs1 = {
                        msgId + ""
                };
                Cursor c1 = resolver.query(CONTENT_URI_ANSWER, null, selection1, selectionArgs1, DBMetaData.AnswerEntry._ID + DBMetaData.ASC);
                if (c1 != null && c1.getCount() > 0) {
                    while (c.moveToNext()) {
                        CarMessage.AnswerMsg answerMsg = new CarMessage.AnswerMsg();
                        answerMsg.setId(c1.getInt(c1.getColumnIndex(DBMetaData.AnswerEntry._ID)));
                        answerMsg.setAnswerId(c1.getInt(c1.getColumnIndex(DBMetaData.AnswerEntry.ANSWER_ID)));
                        answerMsg.setAnswerContent(c1.getString(c1.getColumnIndex(DBMetaData.AnswerEntry.ANSWER_CONTENT)));
                        answerMsgs.add(answerMsg);
                    }
                }
                if (c1 != null) {
                    c1.close();
                }
                carMessage.setAnswerSelectgList(answerMsgs);
            }
        }
        if (c != null) {
            c.close();
        }
        if (carMessage == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(carMessage);
        }
    }

    @Override
    public void saveDBData(@NonNull final CarMessage... carMessages) {
        if (carMessages != null && carMessages.length > 0) {
            getAllDBDatas(new LoadMessageCallBack() {
                @Override
                public void onDBBaseDataLoaded(List<CarMessage> buffers) {
                    try {
                        long time = TimeTool.parseToLong(buffers.get(buffers.size() - 1).getTime())
                                - TimeTool.parseToLong(buffers.get(0).getTime());
                        Log.d("msg","time:"+time+",MESSAGE_MAX_SAVE:"+MESSAGE_MAX_SAVE);
                        if (time < MESSAGE_MAX_SAVE) {
                            for (CarMessage carMessage : carMessages) {
                                saveMsg(carMessage);
                            }
                        } else {
                            Log.d("msg","<<<<<<<<<<<<<<<<<<<<time:"+time);
                            int size = carMessages.length;
                            for (int i = 0; i < size; i++) {
                                deleteDBData(buffers.get(i).getId());
                            }
                            for (CarMessage carMessage : carMessages) {
                                saveMsg(carMessage);
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        for (CarMessage carMessage : carMessages) {
                            saveMsg(carMessage);
                        }
                    }
                }

                @Override
                public void onDataNotAailable() {
                    for (CarMessage carMessage : carMessages) {
                        saveMsg(carMessage);
                    }
                }
            });
        }
    }

    private void saveMsg(CarMessage carMessage) {
        ContentValues values = new ContentValues();
        values.put(DBMetaData.MessageEntry.CONTENT, carMessage.getContent());
        values.put(DBMetaData.MessageEntry.TIME, carMessage.getTime());
        int type = carMessage.getMessageType();
        values.put(DBMetaData.MessageEntry.MESSAGE_TYPE, type);
        switch (type) {
            case MessageKey.MessageTypeKey.COMMUNIQUE_TYPE:
                int msgId_b = carMessage.getMassgeId();
                String selection_b = DBMetaData.MessageEntry.MESSAGE_TYPE + "=?"
                        + DBMetaData.AND + DBMetaData.MessageEntry.MESSAGE_ID + "=?";
                String[] selectionArgs_b = {
                        MessageKey.MessageTypeKey.COMMUNIQUE_TYPE + "",
                        msgId_b + ""
                };
                values.put(DBMetaData.MessageEntry.ANSWER_STATE, 0);
                values.put(DBMetaData.MessageEntry.MESSAGE_ID, msgId_b);
                values.put(DBMetaData.MessageEntry.ANSWER_ID, -1);
                Cursor c_b = resolver.query(CONTENT_URI, null, selection_b,
                        selectionArgs_b, DBMetaData.MessageEntry.TIME + DBMetaData.ASC);
                if (c_b != null && c_b.getCount() > 0) {
                    resolver.update(CONTENT_URI, values, selection_b, selectionArgs_b);
                } else {
                    resolver.insert(CONTENT_URI, values);
                }
                if (c_b != null) {
                    c_b.close();
                }
                break;
            case MessageKey.MessageTypeKey.EVENT_TYPE:
                int msgId = carMessage.getMassgeId();
                String selection = DBMetaData.MessageEntry.MESSAGE_TYPE + "=?"
                        + DBMetaData.AND + DBMetaData.MessageEntry.MESSAGE_ID + "=?";
                String[] selectionArgs = {
                        MessageKey.MessageTypeKey.EVENT_TYPE + "",
                        msgId + ""
                };
                values.put(DBMetaData.MessageEntry.ANSWER_STATE, 0);
                values.put(DBMetaData.MessageEntry.MESSAGE_ID, msgId);
                values.put(DBMetaData.MessageEntry.ANSWER_ID, -1);
                Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.MessageEntry.TIME + DBMetaData.ASC);
                if (c != null && c.getCount() > 0) {
                    resolver.update(CONTENT_URI, values, selection, selectionArgs);
                } else {
                    resolver.insert(CONTENT_URI, values);
                }
                if (c != null) {
                    c.close();
                }
                break;
            case MessageKey.MessageTypeKey.QUESTION_TYPE:
                int questionId = carMessage.getMassgeId();
                String selection1 = DBMetaData.MessageEntry.MESSAGE_TYPE + "=?"
                        + DBMetaData.AND + DBMetaData.MessageEntry.MESSAGE_ID + "=?";
                String[] selectionArgs1 = {
                        MessageKey.MessageTypeKey.QUESTION_TYPE + "",
                        questionId + ""
                };
                int answerState = carMessage.getAnswerState();
                if (answerState == MessageKey.MessageAnswerState.ANSWERED) {
                    values.put(DBMetaData.MessageEntry.ANSWER_ID, carMessage.getAnswerId());
                } else {
                    values.put(DBMetaData.MessageEntry.ANSWER_ID, -1);
                }
                values.put(DBMetaData.MessageEntry.ANSWER_STATE, carMessage.getAnswerState());
                values.put(DBMetaData.MessageEntry.MESSAGE_ID, questionId);
                Cursor c1 = resolver.query(CONTENT_URI, null, selection1, selectionArgs1, DBMetaData.MessageEntry.TIME + DBMetaData.ASC);
                if (c1 != null && c1.getCount() > 0) {
                    resolver.update(CONTENT_URI, values, selection1, selectionArgs1);
                } else {
                    resolver.insert(CONTENT_URI, values);
                }
                if (c1 != null) {
                    c1.close();
                }
                List<CarMessage.AnswerMsg> answerMsgs = carMessage.getAnswerSelectgList();
                if (answerMsgs != null && !answerMsgs.isEmpty()) {
                    String selection2 = DBMetaData.AnswerEntry.MSG_ID + "=?";
                    String[] selectionArgs2 = {
                            questionId + ""
                    };
                    Cursor c2 = resolver.query(CONTENT_URI_ANSWER, null, selection2, selectionArgs2, DBMetaData.AnswerEntry._ID + DBMetaData.ASC);
                    if (c2 != null && c2.getCount() > 0) {
                        while (c2.moveToNext()) {
                            int id = c2.getInt(c2.getColumnIndex(DBMetaData.AnswerEntry._ID));
                            String selection3 = DBMetaData.AnswerEntry._ID + "=?";
                            String[] selectionArgs3 = {
                                    id + ""
                            };
                            resolver.delete(CONTENT_URI_ANSWER, selection3, selectionArgs3);
                        }
                    }
                    if (c2 != null) {
                        c2.close();
                    }
                    for (CarMessage.AnswerMsg answerMsg : answerMsgs) {
                        ContentValues values1 = new ContentValues();
                        values1.put(DBMetaData.AnswerEntry.MSG_ID, questionId);
                        values1.put(DBMetaData.AnswerEntry.ANSWER_ID, answerMsg.getAnswerId());
                        values1.put(DBMetaData.AnswerEntry.ANSWER_CONTENT, answerMsg.getAnswerContent());
                        resolver.insert(CONTENT_URI_ANSWER, values1);
                    }
                }
                break;
        }
    }

    @Override
    public void deleteAllDBDatas() {
        resolver.delete(CONTENT_URI, null, null);
    }

    @Override
    public void deleteDBData(@NonNull int id) {
        String selection = DBMetaData.MessageEntry._ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        resolver.delete(CONTENT_URI, selection, selectionArgs);
    }
}
