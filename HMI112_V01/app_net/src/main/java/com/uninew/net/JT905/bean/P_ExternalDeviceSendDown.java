package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 外围设备下行透传
 * Created by Administrator on 2017/8/20 0020.
 */

public class P_ExternalDeviceSendDown extends BaseBean {

    private int typeId;//设备类型
    private int dataType;//数据类型
    //数据类型详情
    private boolean isGzCompression;//是否GZ压缩
    private boolean isEncrypt;//是否加密

    private byte[] penetrateDatas;//透传数据包

    public P_ExternalDeviceSendDown(byte[] body) {
        getDataPacket(body);
    }

    public P_ExternalDeviceSendDown(int typeId, boolean isGzCompression, boolean isEncrypt, byte[] datas) {
        this.typeId = typeId;
        this.isGzCompression = isGzCompression;
        this.isEncrypt = isEncrypt;
        this.penetrateDatas = datas;
        //将数据类型转换成dataType
        if (isGzCompression){
            ProtocolTool.setBit(dataType,0,1);
        }
        if (isEncrypt){
            ProtocolTool.setBit(dataType,3,1);
        }
    }

    public P_ExternalDeviceSendDown(int typeId, int dataType, byte[] datas) {
        this.typeId = typeId;
        this.dataType = dataType;
        this.penetrateDatas = datas;
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
        return BaseMsgID.EXTERNAL_DEVICE_DOWN_PENETRATE;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public boolean isGzCompression() {
        return isGzCompression;
    }

    public void setGzCompression(boolean gzCompression) {
        isGzCompression = gzCompression;
    }

    public boolean isEncrypt() {
        return isEncrypt;
    }

    public void setEncrypt(boolean encrypt) {
        isEncrypt = encrypt;
    }

    public byte[] getPenetrateDatas() {
        return penetrateDatas;
    }

    public void setPenetrateDatas(byte[] penetrateDatas) {
        this.penetrateDatas = penetrateDatas;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeByte(typeId);
            out.writeShort(dataType);
            out.write(penetrateDatas);
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
    public P_ExternalDeviceSendDown getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            typeId=in.readByte();
            dataType=in.readShort();
            penetrateDatas=new byte[datas.length-3];
            in.read(penetrateDatas);
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
