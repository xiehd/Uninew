package com.uninew.mms.protocol;

import android.util.Log;

import com.uninew.mms.McuService;
import com.uninew.net.Taximeter.bean.P_TaxiCloseBoot;
import com.uninew.net.Taximeter.bean.P_TaxiFreight;
import com.uninew.net.Taximeter.bean.P_TaxiOpenBoot;
import com.uninew.net.Taximeter.bean.P_TaxiOperationDataReport;
import com.uninew.net.Taximeter.bean.P_TaxiQuery;
import com.uninew.net.Taximeter.bean.T_HeartBeat;
import com.uninew.net.Taximeter.common.DefineID;
import com.uninew.net.Taximeter.common.ProtocolTool;
import com.uninew.net.Taximeter.protocol.McuProtocolPacket;

/**
 * 接收计价器数据分发处理类(905)
 * Created by Administrator on 2017/11/17.
 */

public class TaxiReceiveDataHandle {

    private static final String TAG = "TaxiReceiveDataHandle";
    private static final boolean D = true;
    private McuService service;
    private static volatile TaxiReceiveDataHandle INSTANCE;


    private TaxiReceiveDataHandle(McuService service) {
        this.service = service;
    }

    public static TaxiReceiveDataHandle getInstance(McuService service) {
        if (INSTANCE != null) {

        } else {
            synchronized (TaxiReceiveDataHandle.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TaxiReceiveDataHandle(service);
                }
            }
        }
        return INSTANCE;
    }

    //数据分类
    public void msgHandle(McuProtocolPacket packet) {
        if (packet == null) {
            return;
        }
        if (packet.getDeviceType() != 0x02) {
            Log.e(TAG, "---onTaxiReceiveDatas--error------DeviceType = " + packet.getDeviceType());
            return;
        }
        int msgID = packet.getMsgId();
        switch (msgID) {
            case DefineID.TAXI_STATE://状态查询应答
                P_TaxiQuery mP_TaxiQuery = new P_TaxiQuery();
                mP_TaxiQuery.getDataPacket(packet.getBody());
                service.quryTaxiStateResponse(mP_TaxiQuery);
                break;
            case DefineID.TAXI_FREIGHT_QURY://运价参数查询应答
                P_TaxiFreight mP_Freight = new P_TaxiFreight("20171226",5f,5f);
                mP_Freight.getDataPacket(packet.getBody());
                service.quryFreightResponse(mP_Freight);
                break;
            case DefineID.TAXI_FIEIGHT_SET://运价参数设置应答
                byte[] boyd = packet.getBody();
                if (boyd != null && boyd.length > 0) {
                    service.GeneralResponse(boyd[0]);
                }
                break;
            case DefineID.TAXI_SINGLEOPERATE_STAR://单次运营开始
                String time = ProtocolTool.bcd2Str(packet.getBody());
                service.sendOperteStart(time);
                break;
            case DefineID.TAXI_SINGLEOPERATE_END://单次运营结束
                P_TaxiOperationDataReport p_singleOperate = new P_TaxiOperationDataReport();
                p_singleOperate.getDataPacket(packet.getBody());
                p_singleOperate.setId_type(0x00);
                service.sendSingleOperate(p_singleOperate);
                break;
            case DefineID.TAXI_OPERATEDATA_SUPPL://运营数据补传
                P_TaxiOperationDataReport p_singleOperate2 = new P_TaxiOperationDataReport();
                p_singleOperate2.getDataPacket(packet.getBody());
                p_singleOperate2.setId_type(0x01);
                service.sendSingleOperate(p_singleOperate2);
                break;
            case DefineID.TAXI_OPEN://开机指令
                int falg = packet.getBody()[0];
                service.sendTaxiOpen(falg);
                break;
            case DefineID.TAXI_OPEN_OK://开机成功应答
                P_TaxiOpenBoot p_taxiOpenBoot = new P_TaxiOpenBoot();
                p_taxiOpenBoot.getDataPacket(packet.getBody());
                service.TaxiOpenOk(p_taxiOpenBoot);
                break;
            case DefineID.TAXI_CLOSE://关机指令
                int falg_close = packet.getBody()[0];
                service.sendTaxiClose(falg_close);
                break;
            case DefineID.TAXI_CLOSE_OK://关机成功应答
                P_TaxiCloseBoot p_taxiCloseBoot = new P_TaxiCloseBoot();
                p_taxiCloseBoot.getDataPacket(packet.getBody());
                service.TaxiCloseOk(p_taxiCloseBoot);
                break;
            case DefineID.TAXI_CURRENTOPERATDATA_SUUPP://计价器当班运营数据补传
                break;
            case DefineID.TAXI_HEART://心跳
                T_HeartBeat mT_HeartBeat = new T_HeartBeat();
                mT_HeartBeat.getDataPacket(packet.getBody());
                //心跳回复
                service.HeartBeat(mT_HeartBeat);
                break;
            case DefineID.TAXI_HISTORY_OPERA://运营记录查询
                P_TaxiOperationDataReport p_singleOperate3 = new P_TaxiOperationDataReport();
                if (packet.getBody() != null && packet.getBody().length > 0) {
                    p_singleOperate3.getDataPacket(packet.getBody());
                } else {
                    p_singleOperate3 = null;
                }
                service.sendSingleOperate(p_singleOperate3);
                break;
            case DefineID.TAXI_CLOCK://时钟误差
                break;
            case DefineID.TAXI_FIRMWARE_UPDATE://固件升级
                break;
        }
    }
}
