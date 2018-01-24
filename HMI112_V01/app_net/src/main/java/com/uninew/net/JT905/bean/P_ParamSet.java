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
 * 参数设置
 * Created by Administrator on 2017/8/19 0019.
 */

public class P_ParamSet extends BaseBean {

    private static final String TAG = "P_ParamSet";
    private static final boolean D = true;

    private List<Param> params;//参数列表

    public P_ParamSet() {
    }

    public P_ParamSet(byte[] body) {
        getDataPacket(body);
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

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    @Override
    public int getMsgId() {
        return BaseMsgID.TERMINAL_PARAM_SET;
    }


    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            for (Param pm : params) {
                out.writeShort(pm.paramId);
                out.writeByte(pm.paramLength);
                if (isStringType(pm.paramId)) {//String类型
                    byte[] str = ProtocolTool.stringToByte((String) pm.paramValue, ProtocolTool.CHARSET_905);
                    out.write(str);
                } else if (isBCDType(pm.paramId)) {
                    byte[] bcd = ProtocolTool.str2Bcd((String) pm.paramValue);
                    out.write(bcd);
                } else {
                    if (pm.paramLength == 1) {// byte类型
                        out.writeByte((Integer) pm.paramValue);
                    } else if (pm.paramLength == 2) {// word类型
                        out.writeShort((Integer) pm.paramValue);
                    } else if (pm.paramLength == 4) {// dword类型
                        out.writeInt((Integer) pm.paramValue);
                    }
                }
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
    public P_ParamSet getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        if (this.params == null) {
            params = new ArrayList<>();
        }
        try {
            int count = in.available();
            while (count > 0) {
                Param param = new Param();
                int id = in.readUnsignedShort();
                int size = in.readUnsignedByte();
                param.paramId = id;
                param.paramLength = size;
                if (isStringType(id)) {// string类型
                    String str = ProtocolTool.readString(in, size,
                            ProtocolTool.CHARSET_905);
                    param.paramValue = str;
                } else if (isBCDType(id)) {//BCD类型
                    byte[] d = new byte[size];
                    in.read(d);
                    String bcd = ProtocolTool.bcd2Str(d);
                    param.paramValue = bcd;
                } else {
                    Integer value = null;
                    try {
                        if (size == 1) {// byte类型
                            value = in.readUnsignedByte();
                        } else if (size == 2) {// word类型
                            value = in.readUnsignedShort();
                        } else if (size == 4) {// dword类型
                            value = in.readInt();
                        }
                        param.paramValue = value;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                this.params.add(param);
                try {

                    count = in.available();
                } catch (Exception e) {
                    e.printStackTrace();
                    count = -1;
                }
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


    public static boolean isStringType(int id) {
        switch (id) {
            case ParamKey.MainApn:
            case ParamKey.MainWirelessDialName:
            case ParamKey.MainWrielessDialPwd:
            case ParamKey.MainIpOrDomain:
            case ParamKey.SpareApn:
            case ParamKey.SpareWirelessDialName:
            case ParamKey.SpareWrielessDialPwd:
            case ParamKey.SpareIpOrDomain:
            case ParamKey.CardOrPaymentMainIpOrDomain:
            case ParamKey.CardOrPaymentSpareIpOrDomain:
            case ParamKey.ControlCenterPhoneNumber:
            case ParamKey.ResetPhoneNumber:
            case ParamKey.RestoreSettingsPhoneNumber:
            case ParamKey.ControlCenterSmsPhoneNumber:
            case ParamKey.ReceiveISUSmsAlarmPhoneNumber:
            case ParamKey.MonitorPhoneNumber:
            case ParamKey.DeviceMaintenancePWD:
            case ParamKey.BusinessOperationLicense:
            case ParamKey.TaxiBusinessName:
            case ParamKey.TaxiLicensePlate://ASCII（6）格式，可用String
            case ParamKey.VideoServerAPN:
            case ParamKey.VideoServerWirelessDialName:
            case ParamKey.VideoSeverrWirelessDialPwd:
            case ParamKey.VideoServerIpOrDomain:
                return true;
            default:
                return false;
        }
    }

    public static boolean isBCDType(int id) {
        switch (id) {
            case ParamKey.MeterOperationNumberLimit:
            case ParamKey.MeterOperationTimeLimit:
                return true;
            default:
                return false;
        }
    }

    /**
     * 当个参数对象类
     */
    public static class Param {
        public int paramId;//参数ID
        public int paramLength;//参数长度
        public Object paramValue;//参数值

        @Override
        public String toString() {
            return "Param{" +
                    "paramId=" + paramId +
                    ", paramLength=" + paramLength +
                    ", paramValue=" + paramValue +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "P_ParamSet{" +
                "params=" + params +
                '}';
    }
}
