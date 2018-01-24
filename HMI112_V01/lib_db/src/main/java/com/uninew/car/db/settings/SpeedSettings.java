package com.uninew.car.db.settings;

/**
 * Created by Administrator on 2017/12/20 0020.
 */

public class SpeedSettings {
    /* 超速报警值*/
    private int alarm_speed_max = -1;
    /* 超速报警持续时间*/
    private int alarm_speed_time = -1;
    /* 预超速报警值*/
    private int pre_alarm_speed_max = -1;
    /* 预超速报警持续时间*/
    private int pre_alarm_speed_time = -1;
    /* 速度来源*/
    private int speed_source = -1;
    /* 速度单位*/
    private int speed_unit = -1;

    public SpeedSettings() {
    }

    /**
     * 获取超速报警值
     */
    public int getAlarm_speed_max() {
        return alarm_speed_max;
    }

    /**
     * 获取超速报警持续时间
     */
    public int getAlarm_speed_time() {
        return alarm_speed_time;
    }

    /**
     * 获取预超速报警值
     */
    public int getPre_alarm_speed_max() {
        return pre_alarm_speed_max;
    }

    /**
     * 获取预超速报警持续时间
     */
    public int getPre_alarm_speed_time() {
        return pre_alarm_speed_time;
    }

    /**
     * 获取速度来源
     *
     * @return {@link SettingsKeyValue.SpeedSourceKeyValue}
     */
    public int getSpeed_source() {
        return speed_source;
    }

    /**
     * 获取速度单位
     *
     * @return {@link SettingsKeyValue.SpeedUnitKeyValue}
     */
    public int getSpeed_unit() {
        return speed_unit;
    }

    /**
     * 设置超速报警值
     *
     * @param alarm_speed_max
     */
    public void setAlarm_speed_max(int alarm_speed_max) {
        this.alarm_speed_max = alarm_speed_max;
    }

    /**
     * 设置超速报警持续时间
     *
     * @param alarm_speed_time
     */
    public void setAlarm_speed_time(int alarm_speed_time) {
        this.alarm_speed_time = alarm_speed_time;
    }

    /**
     * 设置预超速报警值
     *
     * @param pre_alarm_speed_max
     */
    public void setPre_alarm_speed_max(int pre_alarm_speed_max) {
        this.pre_alarm_speed_max = pre_alarm_speed_max;
    }

    /**
     * 设置预超速报警持续时间
     *
     * @param pre_alarm_speed_time
     */
    public void setPre_alarm_speed_time(int pre_alarm_speed_time) {
        this.pre_alarm_speed_time = pre_alarm_speed_time;
    }

    /**
     * 设置速度来源
     *
     * @param speed_source {@link SettingsKeyValue.SpeedSourceKeyValue}
     */
    public void setSpeed_source(int speed_source) {
        this.speed_source = speed_source;
    }

    /**
     * 设置速度单位
     *
     * @param speed_unit {@link SettingsKeyValue.SpeedUnitKeyValue}
     */
    public void setSpeed_unit(int speed_unit) {
        this.speed_unit = speed_unit;
    }

    @Override
    public String toString() {
        return "SpeedSettings{" +
                "alarm_speed_max=" + alarm_speed_max +
                ", alarm_speed_time=" + alarm_speed_time +
                ", pre_alarm_speed_max=" + pre_alarm_speed_max +
                ", pre_alarm_speed_time=" + pre_alarm_speed_time +
                ", speed_source=" + speed_source +
                ", speed_unit=" + speed_unit +
                '}';
    }
}
