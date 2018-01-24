package com.uninew.net.Alarm.overtime;

/**
 * Created by Administrator on 2017/10/18.
 */

public interface IOverTimeJudgeListener {

    /**
     * 超时驾驶
     * @param type 1：连续驾驶超时；2：当天累计驾驶超时
    * @param state 0:结束。1：开始
     * @param time 时间 （单位：秒）
     */
    void overTime(int type,int state,long time);
}
