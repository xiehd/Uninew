package com.uninew.car.db.settings;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class PowerSetting {
    /* ACC功能模式*/
    private int acc_mode = -1;
    /* 开机模式*/
    private int open_mode = -1;
    /* 关机延时*/
    private int shutdown_Delay = -1;
    /* 低电压保护*/
    private boolean lowVoltage_protection = true;
    /* 蓄电池低压保护*/
    private float lowVoltage = -1;
    /* 恢复开机电压值*/
    private float restore_voltage = -1;
    /* 蓄电池欠压报警*/
    private boolean undervoltage_alarm = true;

    public PowerSetting() {
    }

    /**
     * ACC功能模式
     *
     * @return
     */
    public int getAcc_mode() {
        return acc_mode;
    }

    /**
     * 开机模式
     *
     * @return
     */
    public int getOpen_mode() {
        return open_mode;
    }

    /**
     * 关机延时
     *
     * @return
     */
    public int getShutdown_Delay() {
        return shutdown_Delay;
    }

    /**
     * 低电压保护
     *
     * @return
     */
    public boolean isLowVoltage_protection() {
        return lowVoltage_protection;
    }

    /**
     * 蓄电池低压保护
     *
     * @return
     */
    public float getLowVoltage() {
        return lowVoltage;
    }

    /**
     * 恢复开机电压值
     *
     * @return
     */
    public float getRestore_voltage() {
        return restore_voltage;
    }

    /**
     * 蓄电池欠压报警
     *
     * @return
     */
    public boolean isUndervoltage_alarm() {
        return undervoltage_alarm;
    }

    /**
     * ACC功能模式
     *
     * @param acc_mode {@link SettingsKeyValue.PowerAccModeKeyValue}
     */
    public void setAcc_mode(int acc_mode) {
        this.acc_mode = acc_mode;
    }

    /**
     * 开机模式
     *
     * @param open_mode
     */
    public void setOpen_mode(int open_mode) {
        this.open_mode = open_mode;
    }

    /**
     * 关机延时
     *
     * @param shutdown_Delay
     */
    public void setShutdown_Delay(int shutdown_Delay) {
        this.shutdown_Delay = shutdown_Delay;
    }

    /**
     * 低电压保护
     *
     * @param lowVoltage_protection
     */
    public void setLowVoltage_protection(boolean lowVoltage_protection) {
        this.lowVoltage_protection = lowVoltage_protection;
    }

    /**
     * 蓄电池低压保护
     *
     * @param lowVoltage
     */
    public void setLowVoltage(float lowVoltage) {
        this.lowVoltage = lowVoltage;
    }

    /**
     * 恢复开机电压值
     *
     * @param restore_voltage
     */
    public void setRestore_voltage(float restore_voltage) {
        this.restore_voltage = restore_voltage;
    }

    /**
     * 蓄电池欠压报警
     *
     * @param undervoltage_alarm
     */
    public void setUndervoltage_alarm(boolean undervoltage_alarm) {
        this.undervoltage_alarm = undervoltage_alarm;
    }

    @Override
    public String toString() {
        return "PowerSetting{" +
                "acc_mode=" + acc_mode +
                ", open_mode=" + open_mode +
                ", shutdown_Delay=" + shutdown_Delay +
                ", lowVoltage_protection=" + lowVoltage_protection +
                ", lowVoltage=" + lowVoltage +
                ", restore_voltage=" + restore_voltage +
                ", undervoltage_alarm=" + undervoltage_alarm +
                '}';
    }
}
