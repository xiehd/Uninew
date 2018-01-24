package com.uninew.car.db.settings;

/**
 * Created by Administrator on 2017/9/4 0004.
 */

public final class SettingsKeyValue {
    public static abstract class BaseKeyValue {
        /* 车牌号*/
        public static final int PLATE_NUMBER = 0x01;
        /* 终端号*/
        public static final int TERMINAL_NUMBER = 0x02;
        /* 公司名称*/
        public static final int COMPANY_NUMBER = 0x03;
        /* 录像机序列号*/
        public static final int DVRSERIAL_NUMBER = 0x04;
        /* 操作超时退出*/
        public static final int OUTTIME_EXITE = 0x05;
        /*打印机灵敏度*/
        public static final int PRINT_SENSITIVITY = 0x06;
        /* 预录时间 */
        public static final int PRERECORD_TIME = 0x07;
        /* 延录时间 */
        public static final int DELAY_TIME = 0x08;
    }

    public static abstract class PowerSettingsKeyValue {
        /* ACC功能模式*/
        public static final int ACC_MODE = 0x30;
        /* 开机模式*/
        public static final int OPEN_MODE = -0x31;
        /* 关机延时*/
        public static final int SHUTDOWN_DELAY = 0x32;
        /* 低电压保护*/
        public static final int LOWVOLTAGE_PROTECTION = 0x33;
        /* 蓄电池低压保护*/
        public static final int LOWVOLTAGE = 0x34;
        /* 恢复开机电压值*/
        public static final int RESTORE_VOLTAGE = 0x35;
        /* 蓄电池欠压报警*/
        public static final int UNDERVOLTAGE_ALARM = 0x36;
    }

    public static abstract class PowerAccModeKeyValue {
        /* 开关机*/
        public static final int OPEN_SHUTDOWN = 0;
        /* 休眠*/
        public static final int SLEEP = 1;
    }

    public static abstract class SpeedSettingsKeyValue {
        /* 超速报警值*/
        public static final int ALARM_SPEED_MAX = 0x20;
        /* 超速报警持续时间*/
        public static final int ALARM_SPEED_TIME = 0x21;
        /* 预超速报警值*/
        public static final int PRE_ALARM_SPEED_MAX = 0x22;
        /* 预超速报警持续时间*/
        public static final int PRE_ALARM_SPEED_TIME = 0x23;
        /* 速度来源*/
        public static final int SPEED_SOURCE = 0x24;
        /* 速度单位*/
        public static final int SPEED_UNIT = 0x25;
    }

    public static abstract class PlatformKeyValue {
        /* 出租车服务器*/
        public static final int SERVICE_1 = 0x10;
        /* 备用出租车服务器*/
        public static final int SPARE_SERVICE_1 = 0x11;
        /* 出租车服务器2*/
        public static final int SERVICE_2 = 0x12;
        /* 备用出租车服务器2*/
        public static final int SPARE_SERVICE_2 = 0x13;

    }

    public static abstract class SpeedUnitKeyValue {
        /* km/h 千米/小时*/
        public static final int SPEED_UNIT_KM = 0;
        /* 英里/小时*/
        public static final int SPEED_UNIT_MILE = 1;
    }

    public static abstract class SpeedSourceKeyValue {
        /*卫星*/
        public static final int SPEED_SOURCE_SATELLITE = 0;
        /*脉冲*/
        public static final int SPEED_SOURCE_PULSE = 1;
        /*CAN*/
        public static final int SPEED_SOURCE_CAN = 2;
    }
}
