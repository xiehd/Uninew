package com.uninew.net.Alarm.illegalalram;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface IRegoinJudge {

    /**
     * 发送区域信息
     * @param id 要判断的非法类型
     *           20 ：进出区域/路线（电子围栏）
     *           24：车辆非法点火
     *           25：车辆非法移位
     * @param lon
     * @param lat
     * @param mRegoinInfo
     */
    void sendRegoinInfo(int id,double lon, double lat, RegionInfo mRegoinInfo);
}
