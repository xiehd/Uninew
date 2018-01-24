package com.uninew.car.db.settings;

import android.support.annotation.NonNull;

import com.uninew.car.db.main.DBBaseDataSource;

/**
 * Created by Administrator on 2017/9/2 0002.
 */

public interface BaseLocalSource extends DBBaseDataSource<BaseSettings> {
    interface LoadBaseSettingsCallBack extends LoadDBDataCallBack<BaseSettings> {

    }

    interface GetBaseSettingCallBack extends GetDBDataCallBack<String> {

    }

    interface ChangeBaseSettingsListener {
        /**
         * @param id    {@link SettingsKeyValue.BaseKeyValue}
         * @param value
         */
        void onChangeBaseSettings(int id, String value);

        /**
         * 获取车牌号
         */
        void getPlateNumber(@NonNull String value);

        /**
         * 获取终端号
         */
        void getTerminalNumber(@NonNull String value);

        /**
         * 获取公司名称
         */
        void getCompanyNumber(@NonNull String value);

        /**
         * 获取录像机序列号
         */
        void getDvrSerialNumber(@NonNull String value);

        /**
         * 获取操作超时退出
         */
        void getOutTimeExite(@NonNull String value);

        /**
         * 获取打印机灵敏度
         */
        void getPrintSensitivity(@NonNull String value);

        /**
         * 获取预录时间
         */
        void getPrerecordTime(@NonNull String value);

        /**
         * 获取延录时间
         */
        void getDelayTime(@NonNull String value);

    }


    interface ChangePowerSettingsListener {
        /**
         * @param id    {@link SettingsKeyValue.PowerSettingsKeyValue}
         * @param value
         */
        void onChangeSpeedSettings(int id, String value);

        /**
         * ACC功能模式
         *
         * @param acc_mode {@link SettingsKeyValue.PowerAccModeKeyValue}
         */
        void setAcc_mode(int acc_mode);

        /**
         * 开机模式
         *
         * @param open_mode
         */
        void setOpen_mode(int open_mode);

        /**
         * 关机延时
         *
         * @param shutdown_Delay
         */
        void setShutdown_Delay(int shutdown_Delay);

        /**
         * 低电压保护
         *
         * @param lowVoltage_protection
         */
        void setLowVoltage_protection(boolean lowVoltage_protection);

        /**
         * 蓄电池低压保护
         *
         * @param lowVoltage
         */
        void setLowVoltage(float lowVoltage);

        /**
         * 恢复开机电压值
         *
         * @param restore_voltage
         */
        void setRestore_voltage(float restore_voltage);

        /**
         * 蓄电池欠压报警
         *
         * @param undervoltage_alarm
         */
        void setUndervoltage_alarm(boolean undervoltage_alarm);
    }

    interface ChangeSpeedSettingsListener {
        /**
         * @param id    {@link SettingsKeyValue.SpeedSettingsKeyValue}
         * @param value
         */
        void onChangeSpeedSettings(int id, String value);
        /**
         * 设置超速报警值
         * @param alarm_speed_max
         */
        void setAlarm_speed_max(int alarm_speed_max);
        /**
         * 设置超速报警持续时间
         * @param alarm_speed_time
         */
        void setAlarm_speed_time(int alarm_speed_time);
        /**
         * 设置预超速报警值
         * @param pre_alarm_speed_max
         */
        void setPre_alarm_speed_max(int pre_alarm_speed_max);
        /**
         * 设置预超速报警持续时间
         * @param pre_alarm_speed_time
         */
        void setPre_alarm_speed_time(int pre_alarm_speed_time);
        /**
         * 设置速度来源
         * @param speed_source {@link SettingsKeyValue.SpeedSourceKeyValue}
         */
        void setSpeed_source(int speed_source);
        /**
         * 设置速度单位
         * @param speed_unit {@link SettingsKeyValue.SpeedUnitKeyValue}
         */
        void setSpeed_unit(int speed_unit);
    }


    void registerNotify(@NonNull ChangeSpeedSettingsListener listener);

    void registerNotify(@NonNull ChangePowerSettingsListener listener);

    void registerNotify(@NonNull ChangeBaseSettingsListener listener);

    void unregisterNotify();

    /**
     * @param id    {@link SettingsKeyValue.BaseKeyValue}
     * @param value
     */
    void saveBaseSettings(int id, @NonNull String value);

    void restoringDefault();

    /**
     * 获取车牌号
     */
    void getPlateNumber(@NonNull GetBaseSettingCallBack callBack);

    /**
     * 获取终端号
     */
    void getTerminalNumber(@NonNull GetBaseSettingCallBack callBack);

    /**
     * 获取公司名称
     */
    void getCompanyNumber(@NonNull GetBaseSettingCallBack callBack);

    /**
     * 获取录像机序列号
     */
    void getDvrSerialNumber(@NonNull GetBaseSettingCallBack callBack);

    /**
     * 获取操作超时退出
     */
    void getOutTimeExite(@NonNull GetBaseSettingCallBack callBack);

    /**
     * 获取打印机灵敏度
     */
    void getPrintSensitivity(@NonNull GetBaseSettingCallBack callBack);

    /**
     * 获取预录时间
     */
    void getPrerecordTime(@NonNull GetBaseSettingCallBack callBack);

    /**
     * 获取延录时间
     */
    void getDelayTime(@NonNull GetBaseSettingCallBack callBack);

    /**
     * 设置车牌号
     */
    void setPlateNumber(@NonNull String value);

    /**
     * 设置终端号
     */
    void setTerminalNumber(@NonNull String value);

    /**
     * 设置公司名称
     */
    void setCompanyNumber(@NonNull String value);

    /**
     * 设置录像机序列号
     */
    void setDvrSerialNumber(@NonNull String value);

    /**
     * 设置操作超时退出
     */
    void setOutTimeExite(@NonNull String value);

    /**
     * 设置打印机灵敏度
     */
    void setPrintSensitivity(@NonNull String value);

    /**
     * 设置预录时间
     */
    void setPrerecordTime(@NonNull String value);

    /**
     * 设置延录时间
     */
    void setDelayTime(@NonNull String value);


    interface LoadSpeedSettingsCallBack extends LoadDBDataCallBack<SpeedSettings> {

    }

    interface GetSpeedSettingCallBack extends GetDBDataCallBack<String> {

    }

    /**
     * @param id    {@link SettingsKeyValue.SpeedSettingsKeyValue}
     * @param value
     */
    void saveSpeedSettings(int id, @NonNull String value);

    void restoringSpeedDefault();

    /**
     * 获取超速报警值
     * @param callBack
     */
    void getAlarm_speed_max(@NonNull GetSpeedSettingCallBack callBack);
    /**
     * 获取超速报警持续时间
     * @param callBack
     */
    void getAlarm_speed_time(@NonNull GetSpeedSettingCallBack callBack);
    /**
     * 获取预超速报警值
     * @param callBack
     */
    void getPre_alarm_speed_max(@NonNull GetSpeedSettingCallBack callBack);
    /**
     * 获取预超速报警持续时间
     * @param callBack
     */
    void getPre_alarm_speed_time(@NonNull GetSpeedSettingCallBack callBack);
    /**
     * 获取速度来源
     * @param callBack
     */
    void getSpeed_source(@NonNull GetSpeedSettingCallBack callBack);
    /**
     * 获取速度单位
     * @param callBack
     */
    void getSpeed_unit(@NonNull GetSpeedSettingCallBack callBack);

    /**
     * 设置超速报警值
     * @param alarm_speed_max
     */
    void setAlarm_speed_max(int alarm_speed_max);
    /**
     * 设置超速报警持续时间
     * @param alarm_speed_time
     */
    void setAlarm_speed_time(int alarm_speed_time);
    /**
     * 设置预超速报警值
     * @param pre_alarm_speed_max
     */
    void setPre_alarm_speed_max(int pre_alarm_speed_max);
    /**
     * 设置预超速报警持续时间
     * @param pre_alarm_speed_time
     */
    void setPre_alarm_speed_time(int pre_alarm_speed_time);
    /**
     * 设置速度来源
     * @param speed_source {@link SettingsKeyValue.SpeedSourceKeyValue}
     */
    void setSpeed_source(int speed_source);
    /**
     * 设置速度单位
     * @param speed_unit {@link SettingsKeyValue.SpeedUnitKeyValue}
     */
    void setSpeed_unit(int speed_unit);



    interface LoadPowerSettingsCallBack extends LoadDBDataCallBack<PowerSetting> {

    }

    interface GetPowerSettingCallBack extends GetDBDataCallBack<String> {

    }

    /**
     * @param id    {@link SettingsKeyValue.PowerSettingsKeyValue}
     * @param value
     */
    void savePowerSettings(int id, @NonNull String value);

    void restoringPowerDefault();

    /**
     * ACC功能模式
     *
     * @return
     */
    void getAcc_mode(GetPowerSettingCallBack callBack);

    /**
     * 开机模式
     *
     * @return
     */
    void getOpen_mode(GetPowerSettingCallBack callBack);

    /**
     * 关机延时
     *
     * @return
     */
    void getShutdown_Delay(GetPowerSettingCallBack callBack);

    /**
     * 低电压保护
     *
     * @return
     */
    void isLowVoltage_protection(GetPowerSettingCallBack callBack);

    /**
     * 蓄电池低压保护
     *
     * @return
     */
    void getLowVoltage(GetPowerSettingCallBack callBack);

    /**
     * 恢复开机电压值
     *
     * @return
     */
    void getRestore_voltage(GetPowerSettingCallBack callBack);

    /**
     * 蓄电池欠压报警
     *
     * @return
     */
    void isUndervoltage_alarm(GetPowerSettingCallBack callBack);

    /**
     * ACC功能模式
     *
     * @param acc_mode {@link SettingsKeyValue.PowerAccModeKeyValue}
     */
    void setAcc_mode(int acc_mode);

    /**
     * 开机模式
     *
     * @param open_mode
     */
    void setOpen_mode(int open_mode);

    /**
     * 关机延时
     *
     * @param shutdown_Delay
     */
    void setShutdown_Delay(int shutdown_Delay);

    /**
     * 低电压保护
     *
     * @param lowVoltage_protection
     */
    void setLowVoltage_protection(boolean lowVoltage_protection);

    /**
     * 蓄电池低压保护
     *
     * @param lowVoltage
     */
    void setLowVoltage(float lowVoltage);

    /**
     * 恢复开机电压值
     *
     * @param restore_voltage
     */
    void setRestore_voltage(float restore_voltage);

    /**
     * 蓄电池欠压报警
     *
     * @param undervoltage_alarm
     */
    void setUndervoltage_alarm(boolean undervoltage_alarm);

}
