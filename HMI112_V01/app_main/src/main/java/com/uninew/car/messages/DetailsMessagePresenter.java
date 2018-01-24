package com.uninew.car.messages;

import android.content.Context;

import com.uninew.car.db.message.CarMessage;
import com.uninew.car.db.message.MessageKey;
import com.uninew.net.JT905.comm.client.ClientSendManage;
import com.uninew.net.JT905.comm.client.IClientSendManage;

import java.util.List;

/**
 * Created by Administrator on 2017/9/21 0021.
 */

public class DetailsMessagePresenter implements DetailsMessageContrat.Presenter {

    private DetailsMessageContrat.View mView;
    private Context mContext;
    private IClientSendManage sendManage;
    private MessagesModel model;
    private List<CarMessage.AnswerMsg> answers;
    private int mType = -1;

    public DetailsMessagePresenter(DetailsMessageContrat.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        mView.setPresenter(this);
        sendManage = new ClientSendManage(context);
        model = new MessagesModel(context);
    }

    @Override
    public void start() {
        model.initSendManage(sendManage);
    }

    @Override
    public void stop() {

    }

    @Override
    public void setTime(String time) {
        mView.showTime(time);
    }

    @Override
    public void setMessageType(int messageType) {
        mView.showMessageType(messageType);
        mType = messageType;
    }

    @Override
    public void setAnswerState(int answerState) {

    }

    @Override
    public void setContent(String content) {
        mView.showContent(content);
    }

    @Override
    public void setFlag(int flag) {

    }

    @Override
    public void setMassgeId(int massgeId) {

    }

    @Override
    public void setAnswerSelectgList(List<CarMessage.AnswerMsg> answerSelectgList) {
        mView.showAnswerSelectgList(answerSelectgList);
        answers = answerSelectgList;
    }

    @Override
    public void setAnswerId(int answerId) {

    }

    @Override
    public void answerAction(int msgId, int answerId) {
        if (mType == -1) {
            return;
        }
        switch (mType) {
            case MessageKey.MessageTypeKey.EVENT_TYPE:
                model.eventReport(msgId);
                break;
            case MessageKey.MessageTypeKey.QUESTION_TYPE:
                if (answers == null || answers.isEmpty()) {
                    return;
                }
                for (CarMessage.AnswerMsg answerMsg : answers) {
                    if (answerId == answerMsg.getAnswerId()) {
                        model.answerByQuestion(msgId, answerMsg.getAnswerContent(), answerId);
                        return;
                    }
                }
                break;
        }
    }
}
