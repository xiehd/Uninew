package com.uninew.car.messages;

import com.uninew.car.base.BasePresenter;
import com.uninew.car.base.BaseView;
import com.uninew.car.db.message.CarMessage;

import java.util.List;

/**
 * Created by Administrator on 2017/9/21 0021.
 */

public interface DetailsMessageContrat {

    interface View extends BaseView<Presenter> {

        void showTime(String time);

        void showMessageType(int messageType);

        void showContent(String content);

        void showAnswerSelectgList(List<CarMessage.AnswerMsg> answerSelectgList);

    }

    interface Presenter extends BasePresenter {
        void setTime(String time);

        void setMessageType(int messageType);

        void setAnswerState(int answerState);

        void setContent(String content);

        void setFlag(int flag);

        void setMassgeId(int massgeId);

        void setAnswerSelectgList(List<CarMessage.AnswerMsg> answerSelectgList);

        void setAnswerId(int answerId);

        void answerAction(int msgId, int answerId);
    }
}
