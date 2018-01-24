package com.uninew.mms.interfaces;

import com.uninew.net.Taximeter.bean.T_LedUpdate;


/**
 * 智能顶灯发送接口(905)
 * Created by Administrator on 2017/12/29.
 */

public interface ILedSend {
    /**
     * 顶灯状态查询
     *
     */
    void quryLedState();

    /**
     * 顶灯复位
     */
    void resetLed();

    /**
     * 顶灯波特率设置
     *
     * @param id 0x00 : 2400
     *           0x01 : 4800
     *           0x02 : 9600(默认)
     *           0x03 : 14400
     *           0x04 : 19200
     *           0x05 : 38400
     *           0x06 : 57600
     *           0x07 : 115200
     *           0x08 : 128000
     */
    void setLedBaudRate(int id);

    /**
     * 顶灯固件升级
     *
     * @param mT_LedUpdate
     */
    void LedUpdate(T_LedUpdate mT_LedUpdate);

    /**
     * 运营状态或者星级显示
     *
     * @param msgID 消息ID
     * @param state 0x00 : 空车     未评定
     *              0x01 : 载客      一星
     *              0x02 : 停运      二星
     *              0x03 : 电召      三星
     *              0x04 : 报警      四星
     *              0x05 : 显示防伪密标 五星
     *              0x06 : 换班
     */
    void setLedOperateOrStarState(int msgID, int state);

    /**
     * 显示防伪密标
     *
     * @param bid
     */
    void ShowBID(byte[] bid);

    /**
     * 取消防伪密标
     */
    void DissBID();

    /**
     * 设置夜间模式
     *
     * @param type 0x00:关闭夜间模式
     *             0x01:开启夜间模式
     */
    void setNightMode(int type);

    /**
     * 设置夜间模式参数
     *
     * @param starTime 开始时间
     * @param endTime  结束时间
     */
    void setNightModeData(String starTime, String endTime);
}
