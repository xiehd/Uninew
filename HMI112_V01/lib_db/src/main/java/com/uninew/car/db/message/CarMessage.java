package com.uninew.car.db.message;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/31 0031.
 */

public class CarMessage implements Serializable {
    private int id;
    private String time;
    private int readState;
    private int messageType;
    private int answerState;
    private String content;
    private int massgeId;
    private List<AnswerMsg> answerSelectgList;
    private int answerId;

    public CarMessage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getAnswerState() {
        return answerState;
    }

    public void setAnswerState(int answerState) {
        this.answerState = answerState;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getMassgeId() {
        return massgeId;
    }

    public void setMassgeId(int massgeId) {
        this.massgeId = massgeId;
    }

    public List<AnswerMsg> getAnswerSelectgList() {
        return answerSelectgList;
    }

    public void setAnswerSelectgList(List<AnswerMsg> answerSelectgList) {
        this.answerSelectgList = answerSelectgList;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getReadState() {
        return readState;
    }

    public void setReadState(int readState) {
        this.readState = readState;
    }

    @Override
    public String toString() {
        return "CarMessage{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", readState=" + readState +
                ", messageType=" + messageType +
                ", answerState=" + answerState +
                ", content='" + content + '\'' +
                ", massgeId=" + massgeId +
                ", answerSelectgList=" + answerSelectgList +
                ", answerId=" + answerId +
                '}';
    }

    /**
     * 答案类
     */
    public final static class AnswerMsg implements Serializable{
        private int answerId;
        private String answerContent;
        private int id;

        public AnswerMsg() {
        }

        public int getAnswerId() {
            return answerId;
        }

        public void setAnswerId(int answerId) {
            this.answerId = answerId;
        }

        public String getAnswerContent() {
            return answerContent;
        }

        public void setAnswerContent(String answerContent) {
            this.answerContent = answerContent;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "AnswerMsg{" +
                    "answerId=" + answerId +
                    ", answerContent='" + answerContent + '\'' +
                    '}';
        }
    }
}
