package com.uninew.settings.interfaces;

/**
 * Created by Administrator on 2017/9/12.
 */

public interface ITimePresenter {

    /**
     * 修改系统时间格式
     *
     * @param dayformat  日期格式
     * @param timeformat 时间格式
     * @param timezone   时区选择
     */
    void setTimeFormat(String dayformat, String timeformat, String timezone);

    /**
     * 修改当前系统时间
     *
     * @param time
     */
    void setCurrentTime(String time);

    /*
    * 校时*
     * @param state 0：卫星校时；1：NTP校时
     */
    void setTiming(int state);

    /***********************下传*********************/

    /**
     * 显示系统时间格式
     *
     * @param dayformat  日期格式
     * @param timeformat 时间格式
     * @param timezone   时区选择
     */
    void ShowTimeFormat(String dayformat, String timeformat, String timezone);

    /**
     * 显示当前系统时间
     *
     * @param time
     */
    void ShowCurrentTime(String time);

    /**
     * 显示当前校时
     *
     * @param state 0：卫星校时；1：NTP校时
     */
    void ShowTiming(int state);
}
