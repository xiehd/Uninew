package com.uninew.net.Taximeter.bean;

import com.uninew.net.Taximeter.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static com.uninew.net.JT905.common.ProtocolTool.byteToString;


/**
 * 计价器查询参数
 * Created by Administrator on 2017/10/14.
 */

public class P_TaxiQuery extends BaseMcuBean {
    private static final long serialVersionUID = -1L;
    /**
     * 设备编号
     */
    private String deviceNumber;
    /**
     * 厂商编号
     */
    private int companyNumber;
    /**
     * 设备类型
     */
    private int deviceType;
    /**
     * 设备序列号
     */
    private String deviceSerialNumber;
    /**
     * 设备硬件版本号
     */
    private int hardwareVersion;
    /**
     * 软件主版本号
     */
    private int softwaremainVersion;
    /**
     * 软件次版本号
     */
    private int softwaresecondVersion;
    /**
     * 设备状态
     */
    private int deviceState;
    /**
     * 计价器工作状态
     */
    private int taxiState;
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
    private int operationNumber;


    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            byte[] id = ProtocolTool.str2Bcd(deviceNumber);
            byte[] id2 = new byte[5];
            if (id.length >= 5) {
                System.arraycopy(id, 0, id2, 0, 5);
            } else {
                System.arraycopy(id, 0, id2, 5 - id.length, id.length);
            }
            out.write(id2);
            out.write(ProtocolTool.intToBcd(hardwareVersion));
            out.write(ProtocolTool.intToBcd(softwaremainVersion));
            out.write(ProtocolTool.intToBcd(softwaresecondVersion));
            out.writeByte(deviceState);
            out.writeByte(taxiState);
            byte[] carid = ProtocolTool.str2Bcd(carNumber);
            byte[] carid2 = new byte[6];
            System.arraycopy(carid, 0, carid2, 0, carid.length);
            out.write(carid2);
            byte[] businessid = new byte[16];
            System.arraycopy(ProtocolTool.str2Bcd(businessLicense), 0, businessid, 0, ProtocolTool.str2Bcd(businessLicense).length);
            out.write(businessid);
            byte[] driverCertificateid = new byte[19];
            System.arraycopy(ProtocolTool.str2Bcd(driverCertificate), 0, driverCertificateid, 0, ProtocolTool.str2Bcd(driverCertificate).length);
            out.write(driverCertificateid);
            out.writeInt(operationNumber);
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
    public P_TaxiQuery getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            byte[] b = new byte[5];
            in.read(b);
            deviceNumber = ProtocolTool.bcd2Str(b);
            companyNumber = ProtocolTool.BCDToInt(b[0]);
            deviceType = ProtocolTool.BCDToInt(b[1]);
            deviceSerialNumber = deviceNumber.substring(2);
            hardwareVersion = ProtocolTool.BCDToInt(in.readByte());
            softwaremainVersion = ProtocolTool.BCDToInt(in.readByte());
            softwaresecondVersion = ProtocolTool.BCDToInt(in.readByte());
            deviceState = in.readByte();
            taxiState = in.readByte();
            byte[] c = new byte[6];
            in.read(c);
            carNumber = byteToString(c, ProtocolTool.CHARSET_905);
            byte[] d = new byte[16];
            in.read(d);
            businessLicense = byteToString(d, ProtocolTool.CHARSET_905);
            byte[] e = new byte[19];
            in.read(e);
            driverCertificate = byteToString(e, ProtocolTool.CHARSET_905);
            operationNumber = in.readInt();

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

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public int getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(int companyNumber) {
        this.companyNumber = companyNumber;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public int getHardwareVersion() {
        return hardwareVersion;
    }

    public void setHardwareVersion(int hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public int getSoftwaremainVersion() {
        return softwaremainVersion;
    }

    public void setSoftwaremainVersion(int softwaremainVersion) {
        this.softwaremainVersion = softwaremainVersion;
    }

    public int getSoftwaresecondVersion() {
        return softwaresecondVersion;
    }

    public void setSoftwaresecondVersion(int softwaresecondVersion) {
        this.softwaresecondVersion = softwaresecondVersion;
    }

    public int getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(int deviceState) {
        this.deviceState = deviceState;
    }

    public int getTaxiState() {
        return taxiState;
    }

    public void setTaxiState(int taxiState) {
        this.taxiState = taxiState;
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
}
