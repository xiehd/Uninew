package com.uninew.net.Taximeter.bean;

import com.uninew.net.Taximeter.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 开/关机数据
 * Created by Administrator on 2017/10/16.
 */

public class P_TaxiOpenBoot extends BaseMcuBean {
    private static final long serialVersionUID = -1L;
    /**
     * 车牌号
     */
    private String carNumber;
    /**
     * 企业经营许可证号
     */
    private String businessLicense;
    /**
     * 驾驶员从业资格证
     */
    private String driverCertificate;
    /**
     * 总运营次数
     */
    private int operationNumber = -101;
    /**
     * 操作结果
     */
    private int result;
//-----------------------------------------------------------------------------------------------------
    /**
     * 刷卡时间
     */
    private String payCarTime;
    /**
     * ISU状态
     */
    private int ISUstate;
    /**
     * 时间限制
     */
    private String timeLimit;
    /**
     * 次数限制
     */
    private int NumberLimit;
    /**
     * 开机时间
     */
    private String openbootTime = null;

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            byte[] businessid = new byte[16];
            System.arraycopy(ProtocolTool.str2Bcd(businessLicense), 0, businessid, 0, ProtocolTool.str2Bcd(businessLicense).length);
            out.write(businessid);
            byte[] driverCertificateid = new byte[19];
            System.arraycopy(ProtocolTool.str2Bcd(driverCertificate), 0, driverCertificateid, 0, ProtocolTool.str2Bcd(driverCertificate).length);
            out.write(driverCertificateid);
            byte[] carid = ProtocolTool.str2Bcd(carNumber);
            byte[] carid2 = new byte[6];
            System.arraycopy(carid, 0, carid2, 0, carid.length);
            out.write(carid2);
            carid = ProtocolTool.str2Bcd(payCarTime);
            out.write(carid);
            out.writeShort(ISUstate);
            byte[] time = new byte[5];
            time = ProtocolTool.str2Bcd(timeLimit);
            out.write(time);
            ProtocolTool.intToBcd(NumberLimit, 2);
            out.writeByte(result);
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
    public P_TaxiOpenBoot getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            byte[] d = new byte[16];
            in.read(d);
            businessLicense = ProtocolTool.byteToString(d, ProtocolTool.CHARSET_905);
            byte[] e = new byte[19];
            in.read(e);
            driverCertificate = ProtocolTool.byteToString(e, ProtocolTool.CHARSET_905);
            byte[] c = new byte[6];
            in.read(c);
            carNumber = ProtocolTool.byteToString(c, ProtocolTool.CHARSET_905);
            in.read(c);
            openbootTime = ProtocolTool.bcd2Str(c);
            operationNumber = in.readInt();
            result = in.readByte();

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

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
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

    public int getOperationNumber() {
        return operationNumber;
    }

    public void setOperationNumber(int operationNumber) {
        this.operationNumber = operationNumber;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getPayCarTime() {
        return payCarTime;
    }

    public void setPayCarTime(String payCarTime) {
        this.payCarTime = payCarTime;
    }

    public int getISUstate() {
        return ISUstate;
    }

    public void setISUstate(int ISUstate) {
        this.ISUstate = ISUstate;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getNumberLimit() {
        return NumberLimit;
    }

    public void setNumberLimit(int numberLimit) {
        NumberLimit = numberLimit;
    }

    public String getOpenbootTime() {
        return openbootTime;
    }

    public void setOpenbootTime(String openbootTime) {
        this.openbootTime = openbootTime;
    }
}
