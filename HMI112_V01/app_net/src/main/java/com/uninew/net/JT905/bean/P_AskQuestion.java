package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 提问下发
 * Created by Administrator on 2017/8/19 0019.
 */

public class P_AskQuestion extends BaseBean {

    private int flag;//标志
    private boolean isExigency;//true ：紧急
    private boolean isTTS;//true ： tts播报
    private boolean isADShow;//true ：广告屏显示

    private int questionId;//问题 ID
    private String questionContent;//问题内容
    private List<PreSetAnswer> preSetAnswerList;//问题候选答案列表

    public P_AskQuestion() {
    }

    public P_AskQuestion(byte[] body) {
        getDataPacket(body);
    }

    public P_AskQuestion(boolean isExigency, boolean isTTS, boolean isADShow, int questionId, String questionContent, List<PreSetAnswer> preSetAnswerList) {
        this.isExigency = isExigency;
        this.isTTS = isTTS;
        this.isADShow = isADShow;
        this.questionId = questionId;
        this.questionContent = questionContent;
        this.preSetAnswerList = preSetAnswerList;
        if (isExigency){
            ProtocolTool.setBit(flag,0,1);
        }
        if (isTTS){
            ProtocolTool.setBit(flag,3,1);
        }
        if (isADShow){
            ProtocolTool.setBit(flag,4,1);
        }
    }

    @Override
    public int getTcpId() {
        return this.tcpId;
    }

    @Override
    public void setTcpId(int tcpId) {
        this.tcpId = tcpId;
    }

    @Override
    public int getTransportId() {
        return this.transportId;
    }

    @Override
    public void setTransportId(int transportId) {
        this.transportId = transportId;
    }

    @Override
    public String getSmsPhoneNumber() {
        return this.smsPhonenumber;
    }

    @Override
    public void setSmsPhoneNumber(String smsPhonenumber) {
        this.smsPhonenumber = smsPhonenumber;
    }

    @Override
    public int getSerialNumber() {
        return this.serialNumber;
    }

    @Override
    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public int getMsgId() {
        return BaseMsgID.ASK_QUESTION;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeByte(flag);
            out.writeInt(questionId);
            out.write(ProtocolTool.stringToByte(questionContent,ProtocolTool.CHARSET_905,100));
            for (PreSetAnswer answer:preSetAnswerList){
                out.writeByte(answer.answerId);
                out.write(ProtocolTool.stringToByte(answer.answerContent,ProtocolTool.CHARSET_905,20));
            }
            out.flush();
            datas = stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return datas;
    }

    @Override
    public Object getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            flag = in.readByte();
            if (ProtocolTool.getBit(flag, 0) == 1) {
                isExigency = true;
            } else {
                isExigency = false;
            }
            if (ProtocolTool.getBit(flag, 3) == 1) {
                isTTS = true;
            } else {
                isTTS = false;
            }
            if (ProtocolTool.getBit(flag, 4) == 1) {
                isADShow = true;
            } else {
                isADShow = false;
            }
            questionId = in.readInt();
            byte[] dd = new byte[100];
            in.read(dd);
            questionContent = new String(dd, ProtocolTool.CHARSET_905).trim();
            int number = (datas.length - 105) / 21;//候选答案个数
            PreSetAnswer answer;
            int answerId;
            String answercontent;
            preSetAnswerList = new ArrayList<>();
            for (int i = 0; i < number; i++) {
                answer = new PreSetAnswer();
                answerId = in.readByte();
                answer.setAnswerId(answerId);
                byte[] cc = new byte[20];
                in.read(cc);
                answercontent = new String(cc, ProtocolTool.CHARSET_905);
                answer.setAnswerContent(answercontent);
                preSetAnswerList.add(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    /**
     * 预设答案的内部类
     */
    public class PreSetAnswer {
        private int answerId;
        private String answerContent;

        public PreSetAnswer() {
        }

        public PreSetAnswer(int answerId, String answerContent) {
            this.answerId = answerId;
            this.answerContent = answerContent;
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
    }

    public boolean isExigency() {
        return isExigency;
    }

    public void setExigency(boolean exigency) {
        isExigency = exigency;
    }

    public boolean isTTS() {
        return isTTS;
    }

    public void setTTS(boolean TTS) {
        isTTS = TTS;
    }

    public boolean isADShow() {
        return isADShow;
    }

    public void setADShow(boolean ADShow) {
        isADShow = ADShow;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public List<PreSetAnswer> getPreSetAnswerList() {
        return preSetAnswerList;
    }

    public void setPreSetAnswerList(List<PreSetAnswer> preSetAnswerList) {
        this.preSetAnswerList = preSetAnswerList;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "P_AskQuestion{" +
                "flag=" + flag +
                ", isExigency=" + isExigency +
                ", isTTS=" + isTTS +
                ", isADShow=" + isADShow +
                ", questionId=" + questionId +
                ", questionContent='" + questionContent + '\'' +
                ", preSetAnswerList=" + preSetAnswerList +
                '}';
    }
}
