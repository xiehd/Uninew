package com.uninew.mms;

import com.uninew.mms.aidl.interfaces.AidlConstant;
import com.uninew.net.JT905.bean.Location_TerminalState;

/**
 * Created by Administrator on 2017/11/23.
 */

public class SystemInfoManage {
    private McuService mMcuService;

    public SystemInfoManage(McuService mMcuService) {
        this.mMcuService = mMcuService;
    }

    public void SystemState(int state){
        switch (state){
            case AidlConstant.SystemState.system_powerStart:
                break;
            case AidlConstant.SystemState.system_powerInit:
                break;
            case AidlConstant.SystemState.system_powerWaitNormal:
                break;
            case AidlConstant.SystemState.system_powerNormal://acc on
                Location_TerminalState terminalState1 = new Location_TerminalState();
                terminalState1.setAccOn(true);
                mMcuService.clientSendManage.sendStateMsg(terminalState1);
                mMcuService.mAlarmManager.mDevicefailureJudge.sendfailure(0x05,0x01);
                break;
            case AidlConstant.SystemState.system_powerWaitDormant:
                break;
            case AidlConstant.SystemState.system_powerDormant:
                break;
            case AidlConstant.SystemState.system_powerWait_STBY:
                break;
            case AidlConstant.SystemState.system_power_STBY:
                break;
            case AidlConstant.SystemState.system_power_WaitACC_OFF:
                break;
            case AidlConstant.SystemState.system_power_ACC_OFF:
                //发送平台
                Location_TerminalState terminalState = new Location_TerminalState();
                terminalState.setAccOn(false);
                mMcuService.clientSendManage.sendStateMsg(terminalState);
                break;
        }
    }
}
