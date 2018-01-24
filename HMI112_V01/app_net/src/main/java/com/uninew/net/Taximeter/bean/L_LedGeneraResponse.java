package com.uninew.net.Taximeter.bean;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * 智能顶灯通用应答
 * Created by Administrator on 2017/12/27.
 */

public class L_LedGeneraResponse extends BaseMcuBean {

    /**
     * 结果
     */
    private int resulte = 0x00;
    @Override
    public byte[] getDataBytes() {
        return new byte[0];
    }
    @Override
    public L_LedGeneraResponse getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            resulte = in.readByte();
        } catch (IOException e) {
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
}
