package com.uninew.car.db.settings;

/**
 * Created by Administrator on 2017/9/4 0004.
 */

public final class SettingsDefaultValue {
    public static abstract class BaseDefaultValue {
        //---------------------------------------内容默认值-----------------------------------------
        /* 车牌号*/
        public static final String PLATE_NUMBER_DEFAUL = "粤B00005";
        /* 终端号*/
        public static final String TERMINAL_NUMBER_DEFAUL = "13200000005";
        /* 公司名称*/
        public static final String COMPANY_NUMBER_DEFAUL = "深圳市智慧联芯科技有限公司";
        /* 录像机序列号*/
        public static final String DVRSERIAL_NUMBER_DEFAUL = "";
        /* 操作超时退出*/
        public static final int OUTTIME_EXITE_DEFAUL = 1;
        /*打印机灵敏度*/
        public static final int PRINT_SENSITIVITY_DEFAUL = -1;
        /* 预录时间 */
        public static final int PRERECORD_TIME_DEFAUL = 30;
        /* 延录时间 */
        public static final int DELAY_TIME_DEFAUL = 30;

        //---------------------------------------名字默认值-----------------------------------------
        /* 车牌号*/
        public static final String PLATE_NUMBER_NAME = "车牌号";
        /* 终端号*/
        public static final String TERMINAL_NUMBER_NAME = "终端号";
        /* 公司名称*/
        public static final String COMPANY_NUMBER_NAME = "公司名称";
        /* 录像机序列号*/
        public static final String DVRSERIAL_NUMBER_NAME = "录像机序列号";
        /* 操作超时退出*/
        public static final String OUTTIME_EXITE_NAME = "操作超时退出";
        /*打印机灵敏度*/
        public static final String PRINT_SENSITIVITY_NAME = "打印机灵敏度";
        /* 预录时间 */
        public static final String PRERECORD_TIME_NAME = "预录时间";
        /* 延录时间 */
        public static final String DELAY_TIME_NAME = "延录时间";
    }


    public static abstract class SpeedSettingsValue {
        public static final int ALARM_SPEED_MAX_VALUE = 80;
        public static final int ALARM_SPEED_TIME_VALUE = 60;
        public static final int PRE_ALARM_SPEED_MAX_VALUE = 70;
        public static final int PRE_ALARM_SPEED_TIME_VALUE = 30;
        public static final int SPEED_SOURCE_VALUE = 0;//0:卫星，1：脉冲，2：CAN
        public static final int SPEED_UNIT_VALUE = 0;//0：km/h，1：英里/小时

        public static final String ALARM_SPEED_MAX_NAME = "超速报警值";
        public static final String ALARM_SPEED_TIME_NAME = "超速报警持续时间";
        public static final String PRE_ALARM_SPEED_MAX_NAME = "预超速报警值";
        public static final String PRE_ALARM_SPEED_TIME_NAME = "预超速报警持续时间";
        public static final String SPEED_SOURCE_NAME = "速度来源";
        public static final String SPEED_UNIT_NAME = "速度单位";
    }


    public static abstract class PowerSettingsValue {
        /* ACC功能模式*/
        public static final int ACC_MODE_VALUE = 0;
        /* 开机模式*/
        public static final int OPEN_MODE_VALUE = 0;
        /* 关机延时*/
        public static final int SHUTDOWN_DELAY_VALUE = 10;
        /* 低电压保护*/
        public static final boolean LOWVOLTAGE_PROTECTION_VALUE = true;
        /* 蓄电池低压保护*/
        public static final float LOWVOLTAGE_VALUE = 9.0f;
        /* 恢复开机电压值*/
        public static final float RESTORE_VOLTAGE_VALUE = 12.0f;
        /* 蓄电池欠压报警*/
        public static final boolean UNDERVOLTAGE_ALARM_VALUE = true;


        /* ACC功能模式*/
        public static final String ACC_MODE_NAME = "ACC功能模式";
        /* 开机模式*/
        public static final String OPEN_MODE_NAME = "开机模式";
        /* 关机延时*/
        public static final String SHUTDOWN_DELAY_NAME = "关机延时";
        /* 低电压保护*/
        public static final String LOWVOLTAGE_PROTECTION_NAME = "低电压保护";
        /* 蓄电池低压保护*/
        public static final String LOWVOLTAGE_NAME = "蓄电池低压保护";
        /* 恢复开机电压值*/
        public static final String RESTORE_VOLTAGE_NAME = "恢复开机电压值";
        /* 蓄电池欠压报警*/
        public static final String UNDERVOLTAGE_ALARM_NAME = "蓄电池欠压报警";
    }

    public static abstract class PlatformDefaultValue {
        /* 出租车服务器ID*/
        public static final int SERVICE_PORT_1_ID = 0x00;
        /* 出租车服务器地址*/
        public static final String SERVICE_IP_1_DEFAUL = "114.55.55.149";
        /* 出租车服务器端口*/
        public static final int SERVICE_PORT_1_DEFAUL = 8905;
//        /* 备用出租车服务器地址*/
//        public static final String SPARE_SERVICE_IP_1_DEFAUL = "192.168.1.2";
//        /* 备用出租车服务器端口*/
//        public static final int SPARE_SERVICE_PORT_1_DEFAUL = 8888;
//        /* 出租车服务器地址2*/
//        public static final String SERVICE_IP_2_DEFAUL = "192.168.1.3";
//        /* 出租车服务器端口2*/
//        public static final int SERVICE_PORT_2_DEFAUL = 8888;
//        /* 备用出租车服务器地址2*/
//        public static final String SPARE_SERVICE_IP_2_DEFAUL = "192.168.1.4";
//        /* 备用出租车服务器端口2*/
//        public static final int SPARE_SERVICE_PORT_2_DEFAUL = 8888;


    }
}
