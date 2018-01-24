package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 车辆控制
 * Created by Administrator on 2017/8/19 0019.
 */

public class P_VehicleControl extends BaseBean {

    private int controlItem;//控制项目
    private int controlCmd;//控制命令

    //定义相关常量
    public static final int ControlItem_Oil=0x00;//油路控制
    public static final int ControlItem_Circuit=0x01;//电路控制
    public static final int ControlItem_DoorLock=0x002;//车门锁控制
    public static final int ControlItem_VehicleLock=0x003;//车辆锁控制
    public static final int ControlCmd_OilCut=1;//油路断开
    public static final int ControlCmd_OilRestore=0;//油路恢复
    public static final int ControlCmd_CircuitCut=1;//电路断开
    public static final int ControlCmd_CircuitRestore=0;//电路恢复
    public static final int ControlCmd_DoorLock=1;//车门加锁
    public static final int ControlCmd_DoorUnLock=0;//车门解锁
    public static final int ControlCmd_VehicleLock=1;//车辆加锁
    public static final int ControlCmd_VehicleUnLock=0;//车辆解锁

    public P_VehicleControl(byte[] body) {
        getDataPacket(body);
    }


    public P_VehicleControl(int controlItem, int controlCmd) {
        this.controlItem = controlItem;
        this.controlCmd = controlCmd;
    }

    public int getControlItem() {
        return controlItem;
    }

    public void setControlItem(int controlItem) {
        this.controlItem = controlItem;
    }

    public int getControlCmd() {
        return controlCmd;
    }

    public void setControlCmd(int controlCmd) {
        this.controlCmd = controlCmd;
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
        return BaseMsgID.VEHICLE_CONTROL;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeByte(controlItem);
            out.write(controlCmd);
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
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            controlItem=in.readByte();
            controlCmd=in.readByte();
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
