package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 参数查询
 * Created by Administrator on 2017/8/19 0019.
 */

public class P_ParamQuery extends BaseBean {

    private List<Integer> paramIds;//参数列表

    public P_ParamQuery() {
    }

    public P_ParamQuery(byte[] body) {
        getDataPacket(body);
    }

    public P_ParamQuery(List<Integer> paramIds) {
        this.paramIds = paramIds;
    }

    public List<Integer> getParamIds() {
        return paramIds;
    }

    public void setParamIds(List<Integer> paramIds) {
        this.paramIds = paramIds;
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
        return BaseMsgID.TERMINAL_PARAM_QUERY;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            for (Integer integer:paramIds){
                out.writeShort(integer);
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
    public P_ParamQuery getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            int number=datas.length/2;//参数ID根数
            int paramId;
            for (int i=0;i<number;i++){
                paramId=in.readShort();
                paramIds.add(paramId);
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

}
