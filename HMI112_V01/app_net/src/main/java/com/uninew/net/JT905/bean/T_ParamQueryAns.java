package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 参数查询应答
 * Created by Administrator on 2017/8/19 0019.
 */

public class T_ParamQueryAns extends BaseBean{

    private static final boolean D=true;
    private static final String TAG="T_ParamQueryAns";

    private int responseSerialNumber;//应答流水号
    private List<P_ParamSet.Param> params;//参数列表

    @Override
    public int getTcpId() {
        return this.tcpId;
    }

    @Override
    public void setTcpId(int tcpId) {
        this.tcpId=tcpId;
    }

    @Override
    public int getTransportId() {
        return this.transportId;
    }

    @Override
    public void setTransportId(int transportId) {
        this.transportId=transportId;
    }

    @Override
    public String getSmsPhoneNumber() {
        return this.smsPhonenumber;
    }

    @Override
    public void setSmsPhoneNumber(String smsPhonenumber) {
        this.smsPhonenumber=smsPhonenumber;
    }

    @Override
    public int getSerialNumber() {
        return this.serialNumber;
    }

    @Override
    public void setSerialNumber(int serialNumber) {
        this.serialNumber=serialNumber;
    }

    @Override
    public int getMsgId() {
        return BaseMsgID.TERMINAL_PARAM_QUERY_ANS;
    }

    public int getResponseSerialNumber() {
        return responseSerialNumber;
    }

    public void setResponseSerialNumber(int responseSerialNumber) {
        this.responseSerialNumber = responseSerialNumber;
    }

    public List<P_ParamSet.Param> getParams() {
        return params;
    }

    public void setParams(List<P_ParamSet.Param> params) {
        this.params = params;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            for (P_ParamSet.Param pm : params) {
                out.writeShort(pm.paramId);
                out.writeByte(pm.paramLength);
                if (isStringType(pm.paramId)) {//String类型
                    byte[] str= ProtocolTool.stringToByte((String) pm.paramValue,ProtocolTool.CHARSET_905);
                    out.write(str);
                } else if (isBCDType(pm.paramId)) {
                    byte[] bcd=ProtocolTool.str2Bcd((String) pm.paramValue);
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
        }finally {
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

    private boolean isStringType(int id) {
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

    private boolean isBCDType(int id) {
        switch (id) {
            case ParamKey.MeterOperationNumberLimit:
            case ParamKey.MeterOperationTimeLimit:
                return true;
            default:
                return false;
        }
    }
}
