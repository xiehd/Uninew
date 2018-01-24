package com.uninew.car.messages;

import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;

import com.uninew.car.R;
import com.uninew.car.audio.TtsUtil;
import com.uninew.car.db.message.CarMessage;
import com.uninew.car.db.message.MessageKey;
import com.uninew.car.db.message.MessageLocalDataSource;
import com.uninew.car.db.message.MessageLocalSource;
import com.uninew.car.orders.DetailsOrderActivity;
import com.uninew.net.JT905.bean.P_AskQuestion;
import com.uninew.net.JT905.bean.P_EventSet;
import com.uninew.net.JT905.bean.P_TextIssued;
import com.uninew.net.JT905.comm.client.IClientReceiveListener;
import com.uninew.net.JT905.comm.client.IClientReceiveManage;
import com.uninew.net.JT905.comm.client.IClientSendManage;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tools.TimeTool;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class MessagesModel implements IClientReceiveListener.IMsgListener {

    private IClientReceiveManage mReceiveManage;
    private IClientSendManage mSendManage;
    private MessageLocalSource messageLocalSource;
    private Context mContext;

    public MessagesModel(Context context) {
        messageLocalSource = MessageLocalDataSource.getInstance(context);
        this.mContext = context;
    }

    public void initReceiveManage(IClientReceiveManage receiveManage) {
        this.mReceiveManage = receiveManage;
        mReceiveManage.registerMsgListener(this);
    }

    public void initSendManage(IClientSendManage sendManage) {
        this.mSendManage = sendManage;
    }

    public void unRegisterListener() {
        mReceiveManage.unRegisterMsgListener();
    }

    @Override
    public void textSendDown(P_TextIssued p_textIssued) {
        if (p_textIssued == null) {
            return;
        }
        try {
            String time = TimeTool.formatDate(new Date(System.currentTimeMillis()));
            if(p_textIssued.isTTS()){
                TtsUtil.getInstance(mContext).speak(p_textIssued.getMsg());
            }
            CarMessage carMessage = new CarMessage();
            carMessage.setContent(p_textIssued.getMsg());
            carMessage.setTime(time);
            carMessage.setMessageType(MessageKey.MessageTypeKey.COMMUNIQUE_TYPE);
            messageLocalSource.saveDBData(carMessage);
            showMessageActivity(carMessage);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eventSet(P_EventSet p_eventSet) {
        if (p_eventSet == null || p_eventSet.getEventList() == null || p_eventSet.getEventList().isEmpty()) {
            return;
        }
        List<P_EventSet.Event> events = p_eventSet.getEventList();
        for (P_EventSet.Event event : events) {
            try {
                String time = TimeTool.formatDate(new Date(System.currentTimeMillis()));
                CarMessage carMessage = new CarMessage();
                carMessage.setContent(event.getEventContent());
                carMessage.setMassgeId(event.getEventId());
                carMessage.setTime(time);
                carMessage.setMessageType(MessageKey.MessageTypeKey.EVENT_TYPE);
                messageLocalSource.saveDBData(carMessage);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void askQuestion(P_AskQuestion p_askQuestion) {
        if (p_askQuestion == null) {
            return;
        }
        try {
            String time = TimeTool.formatDate(new Date(System.currentTimeMillis()));
            if(p_askQuestion.isTTS()){
                TtsUtil.getInstance(mContext).speak(p_askQuestion.getQuestionContent());
            }
            CarMessage carMessage = new CarMessage();
            carMessage.setContent(p_askQuestion.getQuestionContent());
            carMessage.setMassgeId(p_askQuestion.getQuestionId());
            List<P_AskQuestion.PreSetAnswer> preSetAnswers = p_askQuestion.getPreSetAnswerList();
            if (preSetAnswers != null && !preSetAnswers.isEmpty()) {
                List<CarMessage.AnswerMsg> answerMsgs = new ArrayList<>();
                for (P_AskQuestion.PreSetAnswer answer : preSetAnswers) {
                    CarMessage.AnswerMsg answerMsg = new CarMessage.AnswerMsg();
                    answerMsg.setAnswerId(answer.getAnswerId());
                    answerMsg.setAnswerContent(answer.getAnswerContent());
                    answerMsgs.add(answerMsg);
                }
                carMessage.setAnswerSelectgList(answerMsgs);
            }
            carMessage.setTime(time);
            carMessage.setMessageType(MessageKey.MessageTypeKey.QUESTION_TYPE);
            messageLocalSource.saveDBData(carMessage);
            showMessageActivity(carMessage);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void showMessageActivity(CarMessage carMessage) {
        Intent intent = new Intent(mContext, DetailsMessageActivity.class);
        intent.putExtra(DetailsMessageActivity.CARMESSAGE_INTENT_KEY, carMessage);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
//        mContext.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);// 淡出淡入动画效果
    }

    public void answerByQuestion(int questionId, String answer, int answerId) {
        if (mSendManage != null) {
            mSendManage.askQuestionAns(questionId, answerId);
            messageLocalSource.changeAnswerState(questionId, MessageKey.MessageAnswerState.ANSWERED, answerId);
        }
    }

    public void eventReport(int eventId) {
        if (mSendManage != null) {
            mSendManage.eventReport(eventId);
        }
    }
}
