package com.uninew.car.db.message;

import com.uninew.car.db.main.DBBaseDataSource;

/**
 * Created by Administrator on 2017/9/21 0021.
 */

public interface AnswerLocalSource extends DBBaseDataSource<CarMessage.AnswerMsg> {
    interface LoadAnswersCallBack extends LoadDBDataCallBack<CarMessage.AnswerMsg> {

    }

    interface GetAnswesCallBack extends GetDBDataCallBack<CarMessage.AnswerMsg> {

    }

    void getAnswersByMsgId(int msgId, LoadAnswersCallBack callBack);

    void setAnswersByMsgId(int msgId, CarMessage.AnswerMsg... answerMsgs);

    void getgetAnswerByMsgIdAndAnswerId(int msgId, int answerId, GetAnswesCallBack callBack);
}
