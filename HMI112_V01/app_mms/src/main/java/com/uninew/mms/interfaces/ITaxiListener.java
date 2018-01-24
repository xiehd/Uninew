package com.uninew.mms.interfaces;

import com.uninew.net.Taximeter.bean.P_TaxiCloseBoot;
import com.uninew.net.Taximeter.bean.P_TaxiFreight;
import com.uninew.net.Taximeter.bean.P_TaxiOpenBoot;
import com.uninew.net.Taximeter.bean.P_TaxiOperationDataReport;
import com.uninew.net.Taximeter.bean.P_TaxiQuery;
import com.uninew.net.Taximeter.bean.T_HeartBeat;

/**
 * 计价器监听接口
 * Created by Administrator on 2017/11/27.
 */

public interface ITaxiListener {
    /**
     *----------- 通用应答-------------------------
     */

    void GeneralResponse(int result);

    /**
     *----------- 状态应答-------------------------
     */
    void quryTaxiStateResponse(P_TaxiQuery mP_TaxiQuery);
    /**
     *----------- 运价参数应答-------------------------
     */

    void quryFreightResponse(P_TaxiFreight mP_Freight);

    /**
     *----------- 单次运营-------------------------
     */
    void sendOperteStart(String time);

    void sendSingleOperate(P_TaxiOperationDataReport mP_SingleOperate);
    /**
     *----------- 开机-------------------------
     */

    void sendTaxiOpen(int falg);

    void TaxiOpenOk(P_TaxiOpenBoot mP_TaxiOpenBoot);


    /**
     *----------- 关机-------------------------
     */
    void sendTaxiClose(int falg);

    void TaxiCloseOk(P_TaxiCloseBoot mP_TaxiCloseBoot);

    /**
     *----------- 心跳-------------------------
     */
    void HeartBeat(T_HeartBeat mT_HeartBeat);


}
