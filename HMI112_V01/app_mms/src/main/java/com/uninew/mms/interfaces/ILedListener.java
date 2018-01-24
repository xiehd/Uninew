package com.uninew.mms.interfaces;

import com.uninew.net.Taximeter.bean.L_LedQuery;

/**
 * 智能顶灯监听接口(905)
 * Created by Administrator on 2018/1/10.
 */

public interface ILedListener {
    /**
     * 顶灯状态应答
     *
     * @param mL_LedQuery
     */
    void LedStateResponse(L_LedQuery mL_LedQuery);

    /**
     * ----------- 通用应答-------------------------
     * @param msgId     消息ID
     * @param result    结果
     */

    void LedGeneralResponse(int msgId,int result);
}
