package com.uninew.net.Taximeter.bean;

import com.uninew.net.Taximeter.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * 关机成功应答
 * Created by Administrator on 2017/10/16.
 */

public class P_TaxiCloseBoot extends BaseMcuBean {
    private String carNumber;//车牌号
    private String businessLicense;//经营许可证号
    private String driverCertificate;//驾驶员从业资格证
    private String TaxiK;//计价器K值 格式：XXXX
    private String currentOpenTime;//当班开机时间
    private String currentCloseTime;//当班关机时间
    private float currentmileage;//当班里程  XXXXX.X
    private float currentOperatemileage;//当班运营里程
    private String getCarNumber;//车次
    private String timingTime;//hhmmss 计时时间
    private float allMoney;//总计金额  XXXXX.X
    private float cardMoney;//卡收金额 XXXXX.X
    private String cardNumber;//卡次 XXXX
    private float betweenmileage;//班间里程 XXX.X
    private float allmileage;//总里程 XXXX.X
    private float allOperatemileage;//总运营里程 XXXXX.X
    private float priceMoney;//单价 XX.XX
    private int allOperate;//总运营次数


    @Override
    public byte[] getDataBytes() {
        return new byte[0];
    }

    @Override
    public P_TaxiCloseBoot getDataPacket(byte[] datas) {
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
            byte[] k = new byte[2];
            TaxiK = ProtocolTool.bcd2Str(k);
            in.read(c);
            currentOpenTime = ProtocolTool.bcd2Str(c);
            in.read(c);
            currentCloseTime = ProtocolTool.bcd2Str(c);
            byte[] km = new byte[3];
            in.read(km);
            currentmileage = Float.parseFloat(ProtocolTool.bcd2Str(km)) / 10;
            in.read(km);
            currentOperatemileage = Float.parseFloat(ProtocolTool.bcd2Str(km)) / 10;
            in.read(k);
            carNumber = ProtocolTool.bcd2Str(k);
            in.read(km);
            timingTime = ProtocolTool.bcd2Str(km);
            in.read(km);
            allMoney = Float.parseFloat(ProtocolTool.bcd2Str(km)) / 10;
            in.read(km);
            cardMoney = Float.parseFloat(ProtocolTool.bcd2Str(km)) / 10;
            in.read(k);
            cardNumber = ProtocolTool.bcd2Str(k);
            in.read(k);
            betweenmileage = Float.parseFloat(ProtocolTool.bcd2Str(k)) / 10;
            byte[] allkm = new byte[4];
            in.read(allkm);
            allmileage = Float.parseFloat(ProtocolTool.bcd2Str(allkm)) / 10;
            in.read(allkm);
            allOperatemileage = Float.parseFloat(ProtocolTool.bcd2Str(allkm)) / 10;
            in.read(k);
            priceMoney = Float.parseFloat(ProtocolTool.bcd2Str(k)) / 100;
            allOperate = in.readInt();
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
}
