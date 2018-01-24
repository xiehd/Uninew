package com.uninew.net.Taximeter.bean;

import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/8/20 0020.
 */

public class P_TaxiOperationDataReport extends BaseMcuBean {
    private static final long serialVersionUID = -1L;

    /**
     * 类型：0x00：当班数据；0x01：补传数据
     */
    private int id_type = 0x00;

    public int getId_type() {
        return id_type;
    }

    public void setId_type(int id_type) {
        this.id_type = id_type;
    }

    /**
     * 企业经营许可证号
     */
    private String businessLicense;
    /**
     * 驾驶员从业资格证
     */
    private String driverCertificate;
    /**
     * 车牌号
     */
    private String carNumber;
    /**
     * 上车时间：YYYYMMDDhhmm
     */
    private String upCarTime;
    /**
     * 下车时间：hhmm
     */
    private String downCarTime;
    /**
     * 计程里程: xxxxx.xkm
     */
    private float mileage;
    /**
     * 空使里程: xxx.xKm
     */
    private float emptyMileage;
    /**
     * 附加费:xxxxx.x元
     */
    private float surcharge;
    /**
     * 等待计时时间: hhmm
     */
    private String waitTimingTime;
    /**
     * 交易金额 xxxxx.x元
     */
    private float transactionIncome;
    /**
     * 车次
     */
    private int trips;
    /**
     * 交易类型
     */
    private int transactionType;
    /**
     * 一卡通交易数据
     */
    private byte[] carData;


    public P_TaxiOperationDataReport() {
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        try {
            out.write(ProtocolTool.stringToByte(carNumber, ProtocolTool.CHARSET_905, 6));
            out.write(ProtocolTool.stringToByte(businessLicense, ProtocolTool.CHARSET_905, 16));
            out.write(ProtocolTool.stringToByte(driverCertificate, ProtocolTool.CHARSET_905, 19));

            out.write(ProtocolTool.str2Bcd(upCarTime, 5));
            out.write(ProtocolTool.str2Bcd(downCarTime, 2));
            //int m = (int) (mileage * 10);
            String m = String.valueOf((int)(mileage*10));
            StringBuilder str1 = new StringBuilder();
            for (int i = m.length();i < 6;i++ ){
                str1.append("0");
            }
            str1.append(m);
            out.write(ProtocolTool.str2Bcd(str1.toString(), 3));
           // int e = (int) (emptyMileage * 10);
            String e = String.valueOf((int)(emptyMileage*10));
            StringBuilder str2 = new StringBuilder();
            for (int i = e.length();i < 4;i++ ){
                str2.append("0");
            }
            str2.append(e);
            out.write(ProtocolTool.str2Bcd(str2.toString(), 2));
           // int s = (int) (surcharge * 10);
            String s = String.valueOf((int)(surcharge*10));
            StringBuilder str3 = new StringBuilder();
            for (int i = s.length();i < 6;i++ ){
                str3.append("0");
            }
            str3.append(s);
            out.write(ProtocolTool.str2Bcd(str3.toString(), 3));
            out.write(ProtocolTool.str2Bcd(waitTimingTime, 2));
          //  int i = (int) (transactionIncome * 10);
            String t = String.valueOf((int)(transactionIncome*10));
            StringBuilder str4 = new StringBuilder();
            for (int i = t.length();i < 6;i++ ){
                str4.append("0");
            }
            str4.append(t);
            out.write(ProtocolTool.str2Bcd(str4.toString(),3));
            out.writeInt(trips);
            out.writeByte(transactionType);
            out.write(carData);
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
    public P_TaxiOperationDataReport getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            byte[] c = new byte[6];
            in.read(c);
            carNumber = ProtocolTool.byteToString(c, ProtocolTool.CHARSET_905);
            byte[] d = new byte[16];
            in.read(d);
            businessLicense = ProtocolTool.byteToString(d, ProtocolTool.CHARSET_905);
            byte[] e = new byte[19];
            in.read(e);
            driverCertificate = ProtocolTool.byteToString(e, ProtocolTool.CHARSET_905);
            byte[] uptime = new byte[5];
            in.read(uptime);
            upCarTime = ProtocolTool.bcd2Str(uptime);
            byte[] downtime = new byte[2];
            in.read(downtime);
            downCarTime = ProtocolTool.bcd2Str(downtime);
            byte[] mileageid = new byte[3];
            in.read(mileageid);
            mileage = Float.parseFloat(ProtocolTool.bcd2Str(mileageid))/10;
            byte[] emptyMileageid = new byte[2];
            in.read(emptyMileageid);
            emptyMileage = Float.parseFloat(ProtocolTool.bcd2Str(emptyMileageid))/10;
            byte[] surchargeid = new byte[3];
            in.read(surchargeid);
            surcharge = Float.parseFloat(ProtocolTool.bcd2Str(surchargeid))/10;
            byte[] waitTimingTimeid = new byte[2];
            in.read(waitTimingTimeid);
            waitTimingTime = ProtocolTool.bcd2Str(waitTimingTimeid);
           in.read(surchargeid);//交易金额
            transactionIncome = Float.parseFloat(ProtocolTool.bcd2Str(surchargeid))/10;
            trips = in.readInt();
            transactionType = in.readByte();
            byte[] carDate = new byte[1];//其数据长度各地市可根据实际情况而定
            in.read(carDate);
            this.carData = carDate;

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

    public String getBusinessLicense() {
        return businessLicense;
    }

    public String getDriverCertificate() {
        return driverCertificate;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String getUpCarTime() {
        return upCarTime;
    }

    public String getDownCarTime() {
        return downCarTime;
    }

    public float getMileage() {
        return mileage;
    }

    public float getEmptyMileage() {
        return emptyMileage;
    }

    public float getSurcharge() {
        return surcharge;
    }

    public String getWaitTimingTime() {
        return waitTimingTime;
    }

    public float getTransactionIncome() {
        return transactionIncome;
    }

    public int getTrips() {
        return trips;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public byte[] getCarData() {
        return carData;
    }

    public byte[] getTestBytes(){
        byte[] datas = null;
        return datas;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public void setDriverCertificate(String driverCertificate) {
        this.driverCertificate = driverCertificate;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public void setUpCarTime(String upCarTime) {
        this.upCarTime = upCarTime;
    }

    public void setDownCarTime(String downCarTime) {
        this.downCarTime = downCarTime;
    }

    public void setMileage(float mileage) {
        this.mileage = mileage;
    }

    public void setEmptyMileage(float emptyMileage) {
        this.emptyMileage = emptyMileage;
    }

    public void setSurcharge(float surcharge) {
        this.surcharge = surcharge;
    }

    public void setWaitTimingTime(String waitTimingTime) {
        this.waitTimingTime = waitTimingTime;
    }

    public void setTransactionIncome(float transactionIncome) {
        this.transactionIncome = transactionIncome;
    }

    public void setTrips(int trips) {
        this.trips = trips;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public void setCarData(byte[] carData) {
        this.carData = carData;
    }
}
