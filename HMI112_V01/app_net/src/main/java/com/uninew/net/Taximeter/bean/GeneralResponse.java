package com.uninew.net.Taximeter.bean;

import com.uninew.net.Taximeter.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 通用应答
 * Created by Administrator on 2017/10/16.
 */

public class GeneralResponse extends BaseMcuBean {
    private String inTime = null;//时间
    private int result = -101;//应答结果

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            if (inTime != null) {
                byte[] bcdtime = ProtocolTool.str2Bcd(inTime);
                out.write(bcdtime);
            }
            if (result != -101) {
                out.writeByte(result);
            }
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
    public GeneralResponse getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            byte[] time = new byte[7];
            if (datas.length > 7) {
                in.read(time);
                inTime = ProtocolTool.bcd2Str(time);
                result = in.readByte();
            } else if (datas.length < 7) {
                result = in.readByte();
            } else if (datas.length == 7) {
                inTime = ProtocolTool.bcd2Str(time);
            }

        } catch (IOException e) {
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

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
