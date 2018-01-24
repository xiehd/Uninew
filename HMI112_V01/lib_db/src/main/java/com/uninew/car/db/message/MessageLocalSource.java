package com.uninew.car.db.message;

import com.uninew.car.db.main.DBBaseDataSource;

/**
 * Created by Administrator on 2017/8/31 0031.
 */

public interface MessageLocalSource extends DBBaseDataSource<CarMessage> {
    interface LoadMessageCallBack extends LoadDBDataCallBack<CarMessage>{

    }
    interface GetMessageCallBack extends GetDBDataCallBack<CarMessage>{

    }
    void changeAnswerState(int problemId,int answerState,int answerId);
    void changeEventContent(int eventId,String content);
    void getAllMessagesByType(LoadMessageCallBack callBack,int... type);
    void changeReadState(int id , int state);
}
