package com.uninew.mms.aidl.interfaces;

public interface AidlConstant {

    /**
     * 系统状态
     */
    interface SystemState{
        int system_powerStart = 0x00;//系统电源状态开启状态
        int system_powerInit = 0x01;//系统电源初始化
        int system_powerWaitNormal = 0x02;//系统电源待转入正常状体
        int system_powerNormal = 0x03;//系统电源正常状态(acc on)
        int system_powerWaitDormant = 0x04;//系统电源待转入休眠状态
        int system_powerDormant = 0x05;//系统电源休眠状态
        int system_powerWait_STBY = 0x06;//系统电源待转入STBY状态
        int system_power_STBY = 0x07;//系统电源STBY状态
        int system_power_WaitACC_OFF = 0x08;//系统待转入ACC OFF
        int system_power_ACC_OFF = 0x09;//系统ACC OFF
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
