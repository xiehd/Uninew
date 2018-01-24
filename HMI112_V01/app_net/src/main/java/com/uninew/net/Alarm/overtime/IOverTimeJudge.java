package com.uninew.net.Alarm.overtime;

/**
 * Created by Administrator on 2017/10/18.
 */

public interface IOverTimeJudge {

    /**
     * 单次驾驶 (根据ACC状态判断)
     * @param type 0:开始；1：结束
     */
    void sendSingleTime(int type);

    /**
     * 打开关闭超时驾驶报警
     * @param type  0:开启；1：关闭
     */
    void sendTimeAlarm(int type);
}
