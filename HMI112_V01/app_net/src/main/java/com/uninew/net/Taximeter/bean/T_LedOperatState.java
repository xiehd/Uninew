package com.uninew.net.Taximeter.bean;

import com.uninew.net.Taximeter.common.ProtocolTool;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 顶灯运营状态/星级评价显示
 * Created by Administrator on 2017/12/27.
 */

public class T_LedOperatState extends BaseMcuBean {
    /**
     * 状态
     * 空车：0x00      未评定
     * 载客：0x01      一星
     * 停运：0x02      二星
     * 电召：0x03      三星
     * 报警：0x04      四星
     * 显示防伪密标：0x05   五星
     * 换班：0x06
     */
    private int operate = 0x00;

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeByte(ProtocolTool.intToBcd(operate));
            out.flush();
            datas = stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
        return null;
    }
}
