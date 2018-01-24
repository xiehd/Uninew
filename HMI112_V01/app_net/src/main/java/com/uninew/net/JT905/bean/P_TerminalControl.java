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
 * 终端控制
 * Created by Administrator on 2017/8/19 0019.
 */

public class P_TerminalControl extends BaseBean {

    private static final boolean D = true;
    private static final String TAG = "P_TerminalControl";

    private int cmd;//命令字
    private UpdateParam param;//命令参数

    public static final int Cmd_Update = 0x01;//升级
    public static final int Cmd_ShutDown = 0x02;//关机
    public static final int Cmd_Reset = 0x03;//复位
    public static final int Cmd_RestoreSettings = 0x04;//恢复出厂设置
    public static final int Cmd_CloseDataCommunication = 0x05;//关闭数据通信
    public static final int Cmd_CloseAllCommunication = 0x06;//关闭无线通信

    public P_TerminalControl() {
    }

    public P_TerminalControl(byte[] body) {
        getDataPacket(body);
    }

    public P_TerminalControl(int cmd, UpdateParam param) {
        this.cmd = cmd;
        this.param = param;
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
        return BaseMsgID.TERMINAL_CONTROL;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public UpdateParam getParam() {
        return param;
    }

    public void setParam(UpdateParam param) {
        this.param = param;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeByte(cmd);
            if (cmd==Cmd_Update){
                out.writeByte(param.deviceType);
                out.writeByte(param.companyNumber);
                out.write(ProtocolTool.str2Bcd(param.hardwareVersion,1));
                out.write(ProtocolTool.str2Bcd(param.softwareVersion,2));
                out.write(ProtocolTool.stringToByte(param.APN,ProtocolTool.CHARSET_905));
                out.writeByte(0x00);
                out.write(ProtocolTool.stringToByte(param.dialName,ProtocolTool.CHARSET_905));
                out.writeByte(0x00);
                out.write(ProtocolTool.stringToByte(param.dialPassword,ProtocolTool.CHARSET_905));
                out.writeByte(0x00);
                out.write(ProtocolTool.stringToByte(param.updateServerAddress,ProtocolTool.CHARSET_905));
                out.writeByte(0x00);
                out.writeShort(param.updateServerPort);
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
    public Object getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            cmd = in.readByte();
            if (cmd==Cmd_Update){
                //升级含有命令参数，其余无
                UpdateParam param=new UpdateParam();
                param.deviceType=in.readByte();
                param.companyNumber=in.readByte();
                //硬件版本
                byte[] hard=new byte[1];
                in.read(hard);
                param.hardwareVersion=ProtocolTool.bcd2Str(hard);
                //软件版本
                byte[] soft=new byte[2];
                in.read(soft);
                param.softwareVersion=ProtocolTool.bcd2Str(soft);
               //APN String
                byte[] dd=new byte[datas.length-8];//
                in.read(dd);
                byte[] a = null;
                List<String> list=new ArrayList<>();
                int j=0;
                for (int i = 0; i < dd.length; i++) {
                    if (dd[i]==0x00) {
                        a = new byte[i+1];
                        System.arraycopy(dd, j, a, 0, i+1 -j);
                        list.add(new String(a,ProtocolTool.CHARSET_905).trim());
                        j=i;
                    }
                }
                if (list.size()==4){
                    param.APN=list.get(0);
                    param.dialName=list.get(1);
                    param.dialPassword=list.get(2);
                    param.updateServerAddress=list.get(3);
                }
                param.updateServerPort=in.readShort();
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
     * 无线升级命令参数类
     */
    public class UpdateParam{
        public int deviceType;//设备类型
        public int companyNumber;//厂商编号
        public String hardwareVersion;//硬件版本
        public String softwareVersion;//软件版本
        public String APN;//APN
        public String dialName;//拨号用户名
        public String dialPassword;//拨号密码
        public String updateServerAddress;//升级服务器地址IP或域名
        public int updateServerPort;//升级服务器端口
    }
}
