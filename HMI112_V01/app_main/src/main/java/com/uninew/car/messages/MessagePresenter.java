package com.uninew.car.messages;

import android.content.Context;
import android.util.Log;

import com.uninew.car.R;
import com.uninew.car.db.message.CarMessage;
import com.uninew.car.db.message.MessageKey;
import com.uninew.car.db.message.MessageLocalDataSource;
import com.uninew.car.db.message.MessageLocalSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public class MessagePresenter implements MessagesContrat.Presenter, MessageLocalSource.LoadMessageCallBack {

    private MessagesContrat.View mView;
    private Context mContext;
    private MessageLocalSource mDBMessages;

    public MessagePresenter(MessagesContrat.View view, Context context) {
        this.mContext = context;
        this.mView = view;
        mDBMessages = MessageLocalDataSource.getInstance(mContext);
        mView.setPresenter(this);
        CarMessage carMessage = new CarMessage();
        carMessage.setMassgeId(1);
        carMessage.setMessageType(0);
        carMessage.setContent(context.getString(R.string.rainy));
        carMessage.setTime("2017-10-01 20:20:10");
        carMessage.setReadState(0);
        carMessage.setAnswerState(1);
        CarMessage carMessage1 = new CarMessage();
        carMessage1.setMassgeId(1);
        carMessage1.setMessageType(1);
        carMessage1.setContent(context.getString(R.string.rainy));
        carMessage1.setTime("2017-10-01 20:20:10");
        carMessage1.setReadState(0);
        carMessage1.setAnswerState(1);
        CarMessage carMessage2 = new CarMessage();
        carMessage2.setMassgeId(1);
        carMessage2.setMessageType(2);
        carMessage2.setContent(context.getString(R.string.rainy));
        carMessage2.setTime("2017-10-01 20:20:10");
        carMessage2.setReadState(0);
        carMessage2.setAnswerState(1);
        List<CarMessage.AnswerMsg> answerMsgs = new ArrayList<>();
        CarMessage.AnswerMsg answerMsg = new CarMessage.AnswerMsg();
        answerMsg.setAnswerContent(context.getString(R.string.normal));
        answerMsg.setAnswerId(0);
        CarMessage.AnswerMsg answerMsg1 = new CarMessage.AnswerMsg();
        answerMsg1.setAnswerContent(context.getString(R.string.unusual));
        answerMsg1.setAnswerId(1);
        answerMsgs.add(answerMsg);
        answerMsgs.add(answerMsg1);
        carMessage2.setAnswerSelectgList(answerMsgs);
        mDBMessages.saveDBData(new CarMessage[]{carMessage, carMessage1, carMessage2});
    }

    @Override
    public void start() {
        this.changeShowMessageType(MessageKey.MessageTypeKey.COMMUNIQUE_TYPE);
    }

    @Override
    public void stop() {

    }

    @Override
    public void changeShowMessageType(int type) {
        mDBMessages.getAllMessagesByType(this, type);
        mView.setMessageType(type);
    }

    @Override
    public void changeMessageState(int id, int state, String finishTime) {

    }

    @Override
    public void confirmFinish(int position) {

    }

    @Override
    public void cancelMessage(int position) {

    }

    @Override
    public void showDetailedMessage(CarMessage message) {
        mView.showDetailedMessage(message);
        Log.d("id","id:"+message.getId());
        mDBMessages.changeReadState(message.getId(), MessageKey.MessageReadState.READED);
    }

    @Override
    public void onDBBaseDataLoaded(List<CarMessage> buffers) {
        mView.showMessages(buffers);
    }

    @Override
    public void onDataNotAailable() {
        mView.showMessages(null);
    }
}
