package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 文本下发
 * Created by Administrator on 2017/8/19 0019.
 */

public class  P_TextIssued extends BaseBean{

    private boolean isExigency;//true ：紧急
    private boolean isViewShow;//true ：终端显示屏显示
    private boolean isTTS;//true ： tts播报
    private boolean isADShow;//true ：广告屏显示
    private String msg;//信息内容

    public P_TextIssued(byte[] body) {
        getDataPacket(body);
    }

    @Override
    public int getTcpId() {
        return this.tcpId;
    }

    @Override
    public void setTcpId(int tcpId) {
        this.tcpId=tcpId;
    }

    @Override
    public int getTransportId() {
        return this.transportId;
    }

    @Override
    public void setTransportId(int transportId) {
        this.transportId=transportId;
    }

    @Override
    public String getSmsPhoneNumber() {
        return this.smsPhonenumber;
    }

    @Override
    public void setSmsPhoneNumber(String smsPhonenumber) {
        this.smsPhonenumber=smsPhonenumber;
    }

    @Override
    public int getSerialNumber() {
        return this.serialNumber;
    }

    @Override
    public void setSerialNumber(int serialNumber) {
        this.serialNumber=serialNumber;
    }

    @Override
    public int getMsgId() {
        return BaseMsgID.TEXT_MSG_DOWNLOAD;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            int flag=0;
            if (isExigency){
                flag=ProtocolTool.setBit(flag,0,1);
            }else{
                flag=ProtocolTool.setBit(flag,0,0);
            }
            if (isViewShow){
                flag=ProtocolTool.setBit(flag,2,1);
            }else{
                flag=ProtocolTool.setBit(flag,2,0);
            }
            if (isTTS){
                flag=ProtocolTool.setBit(flag,3,1);
            }else{
                flag=ProtocolTool.setBit(flag,3,0);
            }
            if (isADShow){
                flag=ProtocolTool.setBit(flag,4,1);
            }else{
                flag=ProtocolTool.setBit(flag,4,0);
            }
            out.writeByte(flag);
            out.write (msg.getBytes(ProtocolTool.CHARSET_905));
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
    public P_TextIssued getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            int mark = in.readByte();
            //紧急
            if (ProtocolTool.getBit(mark,0)==0x01){
                isExigency=true;
            }else{
                isExigency=false;
            }
            //显示
            if (ProtocolTool.getBit(mark,2)==0x01){
                isViewShow=true;
            }else{
                isViewShow=false;
            }
            //TTS
            if (ProtocolTool.getBit(mark,3)==0x01){
                isTTS=true;
            }else{
                isTTS=false;
            }
            //广告屏显示
            if (ProtocolTool.getBit(mark,4)==0x01){
                isADShow=true;
            }else{
                isADShow=false;
            }
            int length = datas.length-1;
            if(length > 0 ){
                byte[] bs = new byte[length];
                in.read(bs);
                if(bs != null){
                    msg = new String(bs, ProtocolTool.CHARSET_905);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
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

    public boolean isExigency() {
        return isExigency;
    }

    public void setExigency(boolean exigency) {
        isExigency = exigency;
    }

    public boolean isViewShow() {
        return isViewShow;
    }

    public void setViewShow(boolean viewShow) {
        isViewShow = viewShow;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
