package com.uninew.mms.interfaces;

import com.uninew.net.Taximeter.bean.P_TaxiFreight;
import com.uninew.net.Taximeter.bean.P_TaxiOpenBoot;
import com.uninew.net.Taximeter.bean.T_HeartBeat;
import com.uninew.net.Taximeter.bean.T_TaxiUpdate;

/**
 * 计价器发送接口(905)
 * Created by Administrator on 2017/11/17.
 */

public interface ITaxiSend {
    /**
     * ISU通用结果应答
     * @param msgID
     * @param result 0x90 正确     0xFF 校验错误
     */
    void currencyResponse(int msgID,int result,String time);
    /**
     * 计价器状态查询
     * @param time ISU当前时间 ：YYYYMMDDhhmmss
     */
    void quryTaxiState(String time);
    /**
     * 运价参数查询
     */
    void quryFreight();

    /**
     * 运价参数设置
     */
    void quryFreightSet(P_TaxiFreight mP_Freight);

    /**
     * 开/关机应答
     * @param mP_TaxiOpenBoot
     */
    void openOrCloseResponse(int type,P_TaxiOpenBoot mP_TaxiOpenBoot);


    /**
     * 心跳发送
     * @param mT_HeartBeat
     */
    void heartResponse(T_HeartBeat mT_HeartBeat);

    /**
     * 运营记录查询
     * @param trip  运营车次
     */
    void operateQuery(int trip);
    /**
     * 计价器时钟误差查询
     * @param time ISU当前时间 ：YYYYMMDDhhmmss
     */
    void quryTaxiClock(String time);

    /**
     * 固件升级
     * @param mT_TaxiUpdate
     */
    void taxiUpdate(T_TaxiUpdate mT_TaxiUpdate);

}
