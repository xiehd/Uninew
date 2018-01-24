package com.uninew.net.Taximeter.bean;

import com.uninew.net.Taximeter.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * 运价参数查询数据类
 * Created by Administrator on 2017/10/16.
 */

public class P_TaxiFreight extends BaseMcuBean {
    private String parameterStartime;//参数启用时间：YYMMDDhh
    private String dayMoney;//白天往返单价 XX.XX；单位（元/km）
    private String nightMoney;//晚上往返单价 XX.XX；单位（元/km）
    private String daySingleMoney;//白天单程单价
    private String nightSingleMoney;//晚上单程单价
    private String dayTwoMoney;//白天二次空貼单价
    private String nightTwoMoney;//晚上二次空贴单价
    private String dayStartMoney;//白天起步价
    private String nightStartMoney;//晚上起步价
    private String continuedMileage;//续程里程数 格式：XX.XX 单位：千米（km）
    private String startDistance;//启程公里
    private String singleDisance;//单程公里
    private String twoDistance;//二次空贴公里
    private String daywaitTimeMoney;//白天等候时间单价 XX.XX  元
    private String nightwaitTimeMoney;//晚上等候时间单价
    private String freewaitTime;//免费等候时间 mmss
    private String addMoney;//加价时间 mmss
    private String nightstartTime;//晚上开始时间
    private String nightendTime;//晚上结束时间
    private byte[] RFU = new byte[22];//系统预留
    private byte[] comcanyData = new byte[64];//厂商自定义参数区（由厂商自定义扩展）


    public P_TaxiFreight(String parameterStartime, float dayMoney, float nightMoney) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        this.parameterStartime = parameterStartime;
        this.dayMoney = decimalFormat.format(dayMoney);
        this.nightMoney = decimalFormat.format(nightMoney);
    }

    @Override
    public byte[] getDataBytes() {

        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.write(ProtocolTool.str2Bcd(parameterStartime));
            out.write(ProtocolTool.number3tobcd(dayMoney));
            out.write(ProtocolTool.number3tobcd(nightMoney));
            out.write(ProtocolTool.number3tobcd(daySingleMoney));
            out.write(ProtocolTool.number3tobcd(nightSingleMoney));
            out.write(ProtocolTool.number3tobcd(dayTwoMoney));
            out.write(ProtocolTool.number3tobcd(nightTwoMoney));
            out.write(ProtocolTool.number3tobcd(dayStartMoney));
            out.write(ProtocolTool.number3tobcd(nightStartMoney));
            out.write(ProtocolTool.number3tobcd(continuedMileage));
            out.write(ProtocolTool.number3tobcd(startDistance));
            out.write(ProtocolTool.number3tobcd(singleDisance));
            out.write(ProtocolTool.number3tobcd(twoDistance));
            out.write(ProtocolTool.number3tobcd(daywaitTimeMoney));
            out.write(ProtocolTool.number3tobcd(nightwaitTimeMoney));
            out.write(ProtocolTool.number3tobcd(freewaitTime));
            out.write(ProtocolTool.number3tobcd(addMoney));
            out.write(ProtocolTool.number3tobcd(nightstartTime));
            out.write(ProtocolTool.number3tobcd(nightendTime));
            out.write(RFU);
            out.write(comcanyData);
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
    public P_TaxiFreight getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            byte[] a = new byte[5];
            byte[] bcd = new byte[2];
            in.read(a);
            parameterStartime = ProtocolTool.bcd2Str(a);
            in.read(bcd);
            dayMoney = ProtocolTool.bcdToStringX(bcd);
            in.read(bcd);
            nightMoney = ProtocolTool.bcdToStringX(bcd);
            in.read(bcd);
            daySingleMoney = ProtocolTool.bcdToStringX(bcd);
            in.read(bcd);
            nightSingleMoney = ProtocolTool.bcdToStringX(bcd);
            in.read(bcd);
            dayTwoMoney = ProtocolTool.bcdToStringX(bcd);
            in.read(bcd);
            nightTwoMoney = ProtocolTool.bcdToStringX(bcd);
            in.read(bcd);
            dayStartMoney = ProtocolTool.bcdToStringX(bcd);
            in.read(bcd);
            nightStartMoney = ProtocolTool.bcdToStringX(bcd);
            in.read(bcd);
            continuedMileage = ProtocolTool.bcdToStringX(bcd);
            in.read(bcd);
            startDistance = ProtocolTool.bcdToStringX(bcd);
            in.read(bcd);
            singleDisance = ProtocolTool.bcdToStringX(bcd);
            in.read(bcd);
            twoDistance = ProtocolTool.bcdToStringX(bcd);
            in.read(bcd);
            daywaitTimeMoney = ProtocolTool.bcdToStringX(bcd);
            in.read(bcd);
            nightwaitTimeMoney = ProtocolTool.bcdToStringX(bcd);
            in.read(bcd);
            freewaitTime = ProtocolTool.bcdToStringX(bcd);
            in.read(bcd);
            addMoney = ProtocolTool.bcdToStringX(bcd);
            in.read(bcd);
            nightstartTime = ProtocolTool.bcdToStringX(bcd);
            in.read(bcd);
            nightendTime = ProtocolTool.bcdToStringX(bcd);
            byte[] b = new byte[22];
            in.read(b);
            RFU = b;
            byte[] c = new byte[64];
            in.read(c);
            comcanyData = c;

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

    public String getParameterStartime() {
        return parameterStartime;
    }

    public void setParameterStartime(String parameterStartime) {
        this.parameterStartime = parameterStartime;
    }

    public String getDayMoney() {
        return dayMoney;
    }

    public void setDayMoney(String dayMoney) {
        this.dayMoney = dayMoney;
    }

    public String getNightMoney() {
        return nightMoney;
    }

    public void setNightMoney(String nightMoney) {
        this.nightMoney = nightMoney;
    }

    public String getDaySingleMoney() {
        return daySingleMoney;
    }

    public void setDaySingleMoney(String daySingleMoney) {
        this.daySingleMoney = daySingleMoney;
    }

    public String getNightSingleMoney() {
        return nightSingleMoney;
    }

    public void setNightSingleMoney(String nightSingleMoney) {
        this.nightSingleMoney = nightSingleMoney;
    }

    public String getDayTwoMoney() {
        return dayTwoMoney;
    }

    public void setDayTwoMoney(String dayTwoMoney) {
        this.dayTwoMoney = dayTwoMoney;
    }

    public String getNightTwoMoney() {
        return nightTwoMoney;
    }

    public void setNightTwoMoney(String nightTwoMoney) {
        this.nightTwoMoney = nightTwoMoney;
    }

    public String getDayStartMoney() {
        return dayStartMoney;
    }

    public void setDayStartMoney(String dayStartMoney) {
        this.dayStartMoney = dayStartMoney;
    }

    public String getNightStartMoney() {
        return nightStartMoney;
    }

    public void setNightStartMoney(String nightStartMoney) {
        this.nightStartMoney = nightStartMoney;
    }

    public String getContinuedMileage() {
        return continuedMileage;
    }

    public void setContinuedMileage(String continuedMileage) {
        this.continuedMileage = continuedMileage;
    }

    public String getStartDistance() {
        return startDistance;
    }

    public void setStartDistance(String startDistance) {
        this.startDistance = startDistance;
    }

    public String getSingleDisance() {
        return singleDisance;
    }

    public void setSingleDisance(String singleDisance) {
        this.singleDisance = singleDisance;
    }

    public String getTwoDistance() {
        return twoDistance;
    }

    public void setTwoDistance(String twoDistance) {
        this.twoDistance = twoDistance;
    }

    public String getDaywaitTimeMoney() {
        return daywaitTimeMoney;
    }

    public void setDaywaitTimeMoney(String daywaitTimeMoney) {
        this.daywaitTimeMoney = daywaitTimeMoney;
    }

    public String getNightwaitTimeMoney() {
        return nightwaitTimeMoney;
    }

    public void setNightwaitTimeMoney(String nightwaitTimeMoney) {
        this.nightwaitTimeMoney = nightwaitTimeMoney;
    }

    public String getFreewaitTime() {
        return freewaitTime;
    }

    public void setFreewaitTime(String freewaitTime) {
        this.freewaitTime = freewaitTime;
    }

    public String getAddMoney() {
        return addMoney;
    }

    public void setAddMoney(String addMoney) {
        this.addMoney = addMoney;
    }

    public String getNightstartTime() {
        return nightstartTime;
    }

    public void setNightstartTime(String nightstartTime) {
        this.nightstartTime = nightstartTime;
    }

    public String getNightendTime() {
        return nightendTime;
    }

    public void setNightendTime(String nightendTime) {
        this.nightendTime = nightendTime;
    }

    public byte[] getRFU() {
        return RFU;
    }

    public void setRFU(byte[] RFU) {
        this.RFU = RFU;
    }

    public byte[] getComcanyData() {
        return comcanyData;
    }

    public void setComcanyData(byte[] comcanyData) {
        this.comcanyData = comcanyData;
    }
}
