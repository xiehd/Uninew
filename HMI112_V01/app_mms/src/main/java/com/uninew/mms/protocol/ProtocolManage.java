package com.uninew.mms.protocol;

import com.uninew.mms.ChuanJiLed.protocol.CJLedProtocolPacket;
import com.uninew.mms.ChuanJiLed.protocol.CJLedReceiveDataHandle;
import com.uninew.mms.McuService;
import com.uninew.mms.interfaces.ILedSend;
import com.uninew.mms.interfaces.IMMsCallBackListener;
import com.uninew.mms.interfaces.IProtocolCallBack;
import com.uninew.mms.interfaces.IProtocolManage;
import com.uninew.mms.interfaces.ITaxiSend;
import com.uninew.mms.util.LogTool;
import com.uninew.mms.util.TimeTool;
import com.uninew.net.Taximeter.bean.GeneralResponse;
import com.uninew.net.Taximeter.bean.P_TaxiFreight;
import com.uninew.net.Taximeter.bean.P_TaxiOpenBoot;
import com.uninew.net.Taximeter.bean.T_HeartBeat;
import com.uninew.net.Taximeter.bean.T_LedUpdate;
import com.uninew.net.Taximeter.bean.T_TaxiUpdate;
import com.uninew.net.Taximeter.common.DefineID;
import com.uninew.net.Taximeter.common.ProtocolTool;
import com.uninew.net.Taximeter.protocol.McuProtocolPacket;

/**
 * 发送和接收控制
 * Created by Administrator on 2017/11/10.
 */

public class ProtocolManage implements IProtocolManage, IMMsCallBackListener.ITaxiDatasCallBack, ITaxiSend, ILedSend {
    private MmsBase mIMmsBase;
    private IProtocolCallBack mProtocolCallBack;
    private McuService service;
    private TaxiReceiveDataHandle mTaxiReceiveDataHandle;//计价器(905)
    private LedReceiveDataHandle mLedReceiveDataHandle;//顶灯(905)
    private CJLedReceiveDataHandle mCJLedReceiveDataHandle;//led()川基

    public ProtocolManage(McuService service) {
        this.service = service;
        mIMmsBase = new MmsBase();
        mIMmsBase.setITaxiDatasCallBack(this);

        mTaxiReceiveDataHandle = TaxiReceiveDataHandle.getInstance(service);
        mLedReceiveDataHandle = LedReceiveDataHandle.getInstance(service);
        mCJLedReceiveDataHandle = CJLedReceiveDataHandle.getInstance(service);
    }


    @Override
    public void setProtocolCallBack(IProtocolCallBack mProtocolCallBack) {
        this.mProtocolCallBack = mProtocolCallBack;
    }

    @Override
    public void sendTaxi(int id, byte[] bytes) {
        service.mIMcuSend.sendRS232((byte) id, bytes);
    }


    //接收数据协议分类
    @Override
    public void onReceiveTcpDatas(int ID, byte[] datas) {
        if (ID == 0x01) {//川基LED协议测试
            CJLedProtocolPacket mCJLedProtocolPacket = new CJLedProtocolPacket();
            mCJLedProtocolPacket.getDataPacket(datas);
            mCJLedReceiveDataHandle.msgHandle(mCJLedProtocolPacket);
        }
        mIMmsBase.onReceiveTcpDatas(ID, datas);//沾包处理
    }

    //-------------------------------------------计价器905协议-------------------------------------------------------------------------------

    //**************接收************
    @Override
    public void taxiDateCallBack(byte[] bytes) {
        LogTool.logBytes("-------taxiDateCallBack----", bytes);
        McuProtocolPacket mMcuProtocolPacket = new McuProtocolPacket();
        mMcuProtocolPacket.getProtocolPacket(bytes);
        int deviceID = mMcuProtocolPacket.getDeviceType();
        switch (deviceID) {
            case 0x00://ISU
                break;
            case 0x01://通讯模块
                break;
            case 0x02://计价器
                if (mTaxiReceiveDataHandle != null) {
                    mTaxiReceiveDataHandle.msgHandle(mMcuProtocolPacket);
                }
                break;
            case 0x03://出租汽车安全模块
                break;
            case 0x04://LED显示屏
                break;
            case 0x05://智能顶灯
                if (mLedReceiveDataHandle != null) {
                    mLedReceiveDataHandle.msgHandle(mMcuProtocolPacket);
                }
                break;
            case 0x06://服务评价器（后排）
                break;
            case 0x07://摄像装置
                break;
            case 0x08://卫星定位设备
                break;
            case 0x09://液晶多媒体屏
                break;
            case 0x10://ISU人机交互设备
                break;
            case 0x11://服务评价器（前排）
                break;
        }

    }

    //**************发送通用应答************
    @Override
    public void currencyResponse(int msgID, int result, String time) {
        GeneralResponse mGeneralResponse = new GeneralResponse();
        mGeneralResponse.setInTime(time);
        mGeneralResponse.setResult(result);
        switch (msgID) {
            case DefineID.TAXI_SINGLEOPERATE_STAR://进入重车（单次运营开始应答）
            case DefineID.TAXI_CLOSE_OK://关机成功后通用应答
                McuProtocolPacket mcuProtocolPacket = new McuProtocolPacket(msgID, mGeneralResponse.getDataBytes(), 0x02);
                sendTaxi(0x02, mcuProtocolPacket.getProtocolPacketBytes());
                break;
            case DefineID.TAXI_OPERATEDATA_SUPPL://运营数据补传
            case DefineID.TAXI_SINGLEOPERATE_END://进入空车（单次运营结束应答）
                McuProtocolPacket mcuProtocolPacket2 = new McuProtocolPacket(msgID, mGeneralResponse.getDataBytes(), 0x02);
                sendTaxi(0x02, mcuProtocolPacket2.getProtocolPacketBytes());
                break;

        }
    }

    @Override
    public void quryTaxiState(String time) {
        byte[] body = ProtocolTool.str2Bcd(time);
        McuProtocolPacket mcuProtocolPacket = new McuProtocolPacket(DefineID.TAXI_STATE, body, 0x02);
        mcuProtocolPacket.setCompanyNumber(0x01);
        sendTaxi(0x02, mcuProtocolPacket.getProtocolPacketBytes());
    }

    @Override
    public void quryFreight() {
        McuProtocolPacket mcuProtocolPacket = new McuProtocolPacket(DefineID.TAXI_FREIGHT_QURY, null, 0x02);
        mcuProtocolPacket.setCompanyNumber(0x01);
        sendTaxi(0x02, mcuProtocolPacket.getProtocolPacketBytes());
    }

    @Override
    public void quryFreightSet(P_TaxiFreight mP_Freight) {
        McuProtocolPacket mcuProtocolPacket = new McuProtocolPacket(DefineID.TAXI_FIEIGHT_SET, mP_Freight.getDataBytes(), 0x02);
        mcuProtocolPacket.setCompanyNumber(0x01);
        sendTaxi(0x02, mcuProtocolPacket.getProtocolPacketBytes());
    }

    @Override
    public void openOrCloseResponse(int type, P_TaxiOpenBoot mP_TaxiOpenBoot) {
        switch (type) {
            case 0x00://开机

                break;
            case 0x01://关机

                break;
        }
    }

    @Override
    public void heartResponse(T_HeartBeat mT_HeartBeat) {
        McuProtocolPacket mcuProtocolPacket = new McuProtocolPacket(DefineID.TAXI_HEART, mT_HeartBeat.getDataBytes(), 0x02);
        mcuProtocolPacket.setCompanyNumber(0x01);
        sendTaxi(0x02, mcuProtocolPacket.getProtocolPacketBytes());

    }

    @Override
    public void operateQuery(int car) {
        byte[] body = new byte[4];
        body[0] = (byte) (car >> 24 & 0xff);
        body[1] = (byte) (car >> 16 & 0xff);
        body[2] = (byte) (car >> 8 & 0xff);
        body[3] = (byte) (car & 0xff);
        McuProtocolPacket mcuProtocolPacket = new McuProtocolPacket(DefineID.TAXI_HISTORY_OPERA, body, 0x02);
        mcuProtocolPacket.setCompanyNumber(0x01);
        sendTaxi(0x02, mcuProtocolPacket.getProtocolPacketBytes());

    }

    @Override
    public void quryTaxiClock(String time) {
        byte[] body = ProtocolTool.str2Bcd(time);
        McuProtocolPacket mcuProtocolPacket = new McuProtocolPacket(DefineID.TAXI_CLOCK, body, 0x02);
        mcuProtocolPacket.setCompanyNumber(0x01);
        sendTaxi(0x02, mcuProtocolPacket.getProtocolPacketBytes());

    }

    @Override
    public void taxiUpdate(T_TaxiUpdate mT_TaxiUpdate) {

    }


    //--------------------------------------智能顶灯-------------------------------------------------------------------


    //***********发送*******************
    @Override
    public void quryLedState() {
        GeneralResponse mGeneralResponse = new GeneralResponse();
        mGeneralResponse.setInTime(TimeTool.timestampTormat2(TimeTool.getCurrentTimestamp()));

        McuProtocolPacket mcuProtocolPacket = new McuProtocolPacket(DefineID.LED_QUERY_STATE, mGeneralResponse.getDataBytes(), 0x05);
        mcuProtocolPacket.setCompanyNumber(0x01);
        sendTaxi(0x02, mcuProtocolPacket.getProtocolPacketBytes());
    }

    @Override
    public void resetLed() {
        McuProtocolPacket mcuProtocolPacket = new McuProtocolPacket(DefineID.LED_RESET, null, 0x05);
        mcuProtocolPacket.setCompanyNumber(0x01);
        sendTaxi(0x02, mcuProtocolPacket.getProtocolPacketBytes());
    }

    @Override
    public void setLedBaudRate(int id) {
        byte[] body = new byte[1];
        body[0] = (byte) id;
        McuProtocolPacket mcuProtocolPacket = new McuProtocolPacket(DefineID.LED_SET_BAUDRATE, body, 0x05);
        mcuProtocolPacket.setCompanyNumber(0x01);
        sendTaxi(0x02, mcuProtocolPacket.getProtocolPacketBytes());

    }

    @Override
    public void LedUpdate(T_LedUpdate mT_LedUpdate) {
        T_LedUpdate mT_LedUpdate2 = new T_LedUpdate();
    }

    @Override
    public void setLedOperateOrStarState(int msgID, int state) {
        byte[] body = ProtocolTool.intToBcd(state, 1);
        McuProtocolPacket mcuProtocolPacket = new McuProtocolPacket(DefineID.LED_OPERATE_STATE, body, 0x05);
        mcuProtocolPacket.setCompanyNumber(0x01);
        if (msgID == 0x00) {//设置运营状态
            mcuProtocolPacket.setMsgId(DefineID.LED_OPERATE_STATE);

        } else if (msgID == 0x01) {//设置星级状态
            mcuProtocolPacket.setMsgId(DefineID.LED_STAR_STATE);
        }
        sendTaxi(0x02, mcuProtocolPacket.getProtocolPacketBytes());
    }

    @Override
    public void ShowBID(byte[] bid) {
        McuProtocolPacket mcuProtocolPacket = new McuProtocolPacket(DefineID.LED_BID_SHOW, bid, 0x05);
        mcuProtocolPacket.setCompanyNumber(0x01);
        sendTaxi(0x02, mcuProtocolPacket.getProtocolPacketBytes());
    }

    @Override
    public void DissBID() {
        McuProtocolPacket mcuProtocolPacket = new McuProtocolPacket(DefineID.LED_BID_DISS, null, 0x05);
        mcuProtocolPacket.setCompanyNumber(0x01);
        sendTaxi(0x02, mcuProtocolPacket.getProtocolPacketBytes());

    }

    @Override
    public void setNightMode(int type) {
        byte[] body = new byte[1];
        body[0] = (byte) type;
        McuProtocolPacket mcuProtocolPacket = new McuProtocolPacket(DefineID.LED_SET_NIGHTMODE, body, 0x05);
        mcuProtocolPacket.setCompanyNumber(0x01);
        sendTaxi(0x02, mcuProtocolPacket.getProtocolPacketBytes());
    }

    @Override
    public void setNightModeData(String starTime, String endTime) {
        String time = starTime + endTime;
        McuProtocolPacket mcuProtocolPacket = new McuProtocolPacket(DefineID.LED_NIGHTMODE_PARAMETER,
                ProtocolTool.str2Bcd(time), 0x05);
        mcuProtocolPacket.setCompanyNumber(0x01);
        sendTaxi(0x02, mcuProtocolPacket.getProtocolPacketBytes());
    }

}
