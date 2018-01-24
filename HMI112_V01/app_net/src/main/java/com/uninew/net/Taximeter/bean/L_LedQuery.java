package com.uninew.net.Taximeter.bean;

import com.uninew.net.Taximeter.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 智能顶灯状态查询
 * Created by Administrator on 2017/12/4.
 */

public class L_LedQuery extends BaseMcuBean {
    private String deviceNumber;
    private int hardwareVersion;
    private int softwareMainVersion;
    private int softwareScondVersion;
    private int ledState;// 0x00:正常   0x0X(X为厂商自定义异常代码)
    private int ledShowState;//顶灯显示状态  0x00:空车；0x01:载客；0x02:停运；0x03:电召；0x04:报警；0x05:显示防伪密标；0x06:换班
    private int ledModel;// 0x00:一般模式  0x01:夜间模式
    private byte[] RFU = new byte[5];

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.write(ProtocolTool.str2Bcd(deviceNumber));
            out.writeByte(ProtocolTool.intToBcd(hardwareVersion));
            out.writeByte(ProtocolTool.intToBcd(softwareMainVersion));
            out.writeByte(ProtocolTool.intToBcd(softwareScondVersion));
            out.writeByte(ledState);
            out.writeByte(ledShowState);
            out.writeByte(ledModel);
            out.write(RFU);
            stream.flush();
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
    public L_LedQuery getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            byte[] a = new byte[5];
            in.read(a);
            deviceNumber = ProtocolTool.bcd2Str(a);
            hardwareVersion = ProtocolTool.BCDToInt(in.readByte());
            softwareMainVersion = ProtocolTool.BCDToInt(in.readByte());
            softwareScondVersion = ProtocolTool.BCDToInt(in.readByte());
            ledState = in.readByte();
            ledShowState = in.readByte();
            ledModel = in.readByte();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (stream != null){
                    stream.close();
                }
                if (in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }
}
