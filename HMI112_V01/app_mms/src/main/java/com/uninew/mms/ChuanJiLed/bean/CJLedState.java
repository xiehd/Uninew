package com.uninew.mms.ChuanJiLed.bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 状态和星级显示
 * Created by Administrator on 2018/1/11.
 */

public class CJLedState extends BaseCJLed {

    private int operateState = 0x00;//运营状态
    private int starState = 0x00;//星级状态

    public CJLedState(int operateState, int starState) {
        this.operateState = operateState;
        this.starState = starState;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeByte(operateState);
            out.writeByte(starState);
            out.flush();
            datas = stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null)
                    stream.close();
                if (out != null)
                    out.close();
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
            operateState = in.readByte();
            starState = in.readByte();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null)
                    stream.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }
}
