package com.uninew.mms.protocol;

import android.util.Log;

import com.uninew.mms.McuService;
import com.uninew.net.Taximeter.bean.L_LedQuery;
import com.uninew.net.Taximeter.common.DefineID;
import com.uninew.net.Taximeter.protocol.McuProtocolPacket;

/**
 * 接收顶灯数据分发处理类（905）
 * Created by Administrator on 2017/12/28.
 */

public class LedReceiveDataHandle {
    private static final String TAG = "LedReceiveDataHandle";
    private static final boolean D = true;
    private McuService service;
    private static volatile LedReceiveDataHandle INSTANCE;

    private LedReceiveDataHandle(McuService service) {
        this.service = service;
    }

    public static LedReceiveDataHandle getInstance(McuService service) {
        if (INSTANCE != null) {

        } else {
            synchronized (TaxiReceiveDataHandle.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LedReceiveDataHandle(service);
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
        if (packet.getDeviceType() != 0x05) {
            Log.e(TAG, "---onLEDReceiveDatas--error------DeviceType = " + packet.getDeviceType());
            return;
        }
        int msgID = packet.getMsgId();
        switch (msgID) {
            case DefineID.LED_QUERY_STATE://顶灯状态查询
                L_LedQuery mL_LedQuery = new L_LedQuery();
                mL_LedQuery.getDataPacket(packet.getBody());
                service.LedStateResponse(mL_LedQuery);
                break;
            case DefineID.LED_RESET://顶灯复位
                if (packet.getBody() != null && packet.getBody().length > 0) {
                    int result = packet.getBody()[0];
                    service.LedGeneralResponse(DefineID.LED_RESET, result);
                }
                break;
            case DefineID.LED_SET_BAUDRATE://顶灯波特率
                if (packet.getBody() != null && packet.getBody().length > 0) {
                    int result = packet.getBody()[0];
                    service.LedGeneralResponse(DefineID.LED_SET_BAUDRATE, result);
                }
                break;
            case DefineID.LED_FIRMWARE_STATE://顶灯固件升级
                if (packet.getBody() != null && packet.getBody().length > 0) {
                    int result = packet.getBody()[0];
                    service.LedGeneralResponse(DefineID.LED_FIRMWARE_STATE, result);
                }
                break;
            case DefineID.LED_OPERATE_STATE://顶灯运营状态
                if (packet.getBody() != null && packet.getBody().length > 0) {
                    int result = packet.getBody()[0];
                    service.LedGeneralResponse(DefineID.LED_OPERATE_STATE, result);
                }
                break;
            case DefineID.LED_STAR_STATE://顶灯星级状态
                if (packet.getBody() != null && packet.getBody().length > 0) {
                    int result = packet.getBody()[0];
                    service.LedGeneralResponse(DefineID.LED_STAR_STATE, result);
                }
                break;
            case DefineID.LED_BID_SHOW://顶灯防伪密标显示
                if (packet.getBody() != null && packet.getBody().length > 0) {
                    int result = packet.getBody()[0];
                    service.LedGeneralResponse(DefineID.LED_BID_SHOW, result);
                }
                break;
            case DefineID.LED_BID_DISS://顶灯防伪密标取消
                if (packet.getBody() != null && packet.getBody().length > 0) {
                    int result = packet.getBody()[0];
                    service.LedGeneralResponse(DefineID.LED_BID_DISS, result);
                }
                break;
            case DefineID.LED_SET_NIGHTMODE://夜间工作模式
                if (packet.getBody() != null && packet.getBody().length > 0) {
                    int result = packet.getBody()[0];
                    service.LedGeneralResponse(DefineID.LED_SET_NIGHTMODE, result);
                }
                break;
            case DefineID.LED_NIGHTMODE_PARAMETER://夜间工作模式参数设置
                if (packet.getBody() != null && packet.getBody().length > 0) {
                    int result = packet.getBody()[0];
                    service.LedGeneralResponse(DefineID.LED_NIGHTMODE_PARAMETER, result);
                }
                break;
        }
    }
}
