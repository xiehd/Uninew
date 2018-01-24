package com.uninew.net.Taximeter.bean;

import android.util.Log;

import com.uninew.net.Taximeter.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 计价器心跳
 * Created by Administrator on 2017/10/17.
 */


public class T_HeartBeat extends BaseMcuBean {

    private static final String TAG = "T_HeartBeat";
    private static final boolean D = true;
    private int carState = 0;//空重车状  0：空车  1：重车
    private int oprateState = 0;//停运（签退状态） 0:签退   1：运营
    private int openState = 0;//开机状态  0：正常  1：强制开机
    private int closeState = 0;//关机状态  0：正常  1：强制关机
    private int speedInfo = 0;//异常速度信号  0：正常  1：异常
    /**
     * 企业经营许可证号
     */
    private String businessLicense;
    /**
     * 驾驶员从业资格证
     */
    private String driverCertificate;

    //-------------------------------------------------------------------------------------------------------
    private int ISUState;//终端运行状态
    private String timeLimit = "0000000000";//时间限制 YYYYMMDDHH
    private int numberLimit = 0;//次数限制
    private int RFU = 0;

    public T_HeartBeat(){}

    public T_HeartBeat(String businessLicense, int ISUState, String timeLimit, int numberLimit) {
        this.businessLicense = businessLicense;
        this.ISUState = ISUState;
        this.timeLimit = timeLimit;
        this.numberLimit = numberLimit;
    }

    public byte[] getDataBytes2() {
        byte[] dates = null;
        int falg = 0;
        ByteArrayOutputStream strem = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(strem);
        try {
            if (carState == 1) {
                falg = falg | 0x01;
            } else {
                falg = (falg & 0xfe);
            }
            if (oprateState == 1) {
                falg = falg | (0x01 << 1);
            } else {
                falg = (falg & 0xfd);
            }
            if (openState == 1) {
                falg = falg | (0x01 << 2);
            } else {
                falg = (falg & 0xfb);
            }
            if (closeState == 1) {
                falg = falg | (0x01 << 3);
            } else {
                falg = (falg & 0xf7);
            }
            if (speedInfo == 1) {
                falg = falg | (0x01 << 7);
            } else {
                falg = (falg & 0x7f);
            }
            if (D)
                Log.d(TAG, "--T_HeartBeat 计价器状态222--" + ProtocolTool.getBit(falg, 0) + "," + ProtocolTool.getBit(falg, 1)
                        + "," + ProtocolTool.getBit(falg, 2) + "," + ProtocolTool.getBit(falg, 3) + "," + ProtocolTool.getBit(falg, 7));

            out.writeByte(falg);
            byte[] businessid = new byte[16];
            System.arraycopy(ProtocolTool.str2Bcd(businessLicense), 0, businessid, 0, ProtocolTool.str2Bcd(businessLicense).length);
            out.write(businessid);
            byte[] driverCertificateid = new byte[19];
            System.arraycopy(ProtocolTool.str2Bcd(driverCertificate), 0, driverCertificateid, 0, ProtocolTool.str2Bcd(driverCertificate).length);
            out.write(driverCertificateid);
            out.flush();

            dates = strem.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (strem != null) {
                    strem.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dates;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] dates = null;
        ByteArrayOutputStream strem = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(strem);
        try {
            out.writeShort(ISUState);
            byte[] bcdtime = ProtocolTool.str2Bcd(timeLimit);
            out.write(bcdtime);
            out.writeByte(ProtocolTool.intToBcd(numberLimit));
            out.writeByte(RFU);
            out.flush();
            dates = strem.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (strem != null) {
                    strem.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dates;
    }

    @Override
    public T_HeartBeat getDataPacket(byte[] datas) {
        ByteArrayInputStream strem = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(strem);
        int state = 0;
        try {
            state = in.readByte();

            if (ProtocolTool.getBit(state, 0) == 0) {
                carState = 0;
            } else {
                carState = 1;
            }
            if (ProtocolTool.getBit(state, 1) == 0) {
                oprateState = 0;
            } else {
                oprateState = 1;
            }
            if (ProtocolTool.getBit(state, 2) == 0) {
                openState = 0;
            } else {
                openState = 1;
            }
            if (ProtocolTool.getBit(state, 3) == 0) {
                closeState = 0;
            } else {
                closeState = 1;
            }

            if (ProtocolTool.getBit(state, 8) == 0) {
                speedInfo = 0;
            } else {
                speedInfo = 1;
            }

            if (D)
                Log.d(TAG, "--T_HeartBeat 计价器状态--carState:" + carState + ",oprateState:" + oprateState
                        + ",openState:" + openState + ",closeState:" + closeState + ",speedInfo:" + speedInfo);

            byte[] d = new byte[16];
            in.read(d);
            businessLicense = ProtocolTool.byteToString(d, ProtocolTool.CHARSET_905);
            byte[] e = new byte[19];
            in.read(e);
            driverCertificate = ProtocolTool.byteToString(e, ProtocolTool.CHARSET_905);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (strem != null) {
                    strem.close();
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

    public int getCarState() {
        return carState;
    }

    public void setCarState(int carState) {
        this.carState = carState;
    }

    public int getOprateState() {
        return oprateState;
    }

    public void setOprateState(int oprateState) {
        this.oprateState = oprateState;
    }

    public int getOpenState() {
        return openState;
    }

    public void setOpenState(int openState) {
        this.openState = openState;
    }

    public int getCloseState() {
        return closeState;
    }

    public void setCloseState(int closeState) {
        this.closeState = closeState;
    }

    public int getSpeedInfo() {
        return speedInfo;
    }

    public void setSpeedInfo(int speedInfo) {
        this.speedInfo = speedInfo;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getDriverCertificate() {
        return driverCertificate;
    }

    public void setDriverCertificate(String driverCertificate) {
        this.driverCertificate = driverCertificate;
    }

    public int getISUState() {
        return ISUState;
    }

    public void setISUState(int ISUState) {
        this.ISUState = ISUState;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getNumberLimit() {
        return numberLimit;
    }

    public void setNumberLimit(int numberLimit) {
        this.numberLimit = numberLimit;
    }

    public int getRFU() {
        return RFU;
    }

    public void setRFU(int RFU) {
        this.RFU = RFU;
    }
}
