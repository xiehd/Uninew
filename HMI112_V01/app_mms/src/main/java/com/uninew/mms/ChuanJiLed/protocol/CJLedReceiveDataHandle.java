package com.uninew.mms.ChuanJiLed.protocol;

import android.util.Log;

import com.uninew.mms.McuService;
import com.uninew.mms.util.LogTool;
import com.uninew.net.Taximeter.bean.P_TaxiQuery;
import com.uninew.net.Taximeter.common.DefineID;
import com.uninew.net.Taximeter.protocol.McuProtocolPacket;

/**
 * 川基LED协议接收分包处理
 * Created by Administrator on 2018/1/11.
 */

public class CJLedReceiveDataHandle {
    private static final String TAG = "CJLedReceiveDataHandle";
    private static final boolean D = true;
    private McuService service;
    private static volatile CJLedReceiveDataHandle INSTANCE;



    private CJLedReceiveDataHandle(McuService service) {
        this.service = service;
    }

    public static CJLedReceiveDataHandle getInstance(McuService service) {
        if (INSTANCE != null) {

        } else {
            synchronized (CJLedReceiveDataHandle.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CJLedReceiveDataHandle(service);
                }
            }
        }
        return INSTANCE;
    }

    //数据分类
    public void msgHandle(CJLedProtocolPacket packet) {
        if (packet == null) {
            return;
        }
        int msgID = packet.getMsgID();
        switch (msgID) {
            case 0x4E://心跳
                LogTool.logBytes(TAG,"心跳：",packet.getDataBytes());
                break;
            case 0x49://顶灯状态
                LogTool.logBytes(TAG,"状态设置应答：",packet.getDataBytes());
                break;
        }
    }
}
