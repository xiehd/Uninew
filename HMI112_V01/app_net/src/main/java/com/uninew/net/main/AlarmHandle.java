package com.uninew.net.main;

import android.content.Context;

import com.uninew.net.JT905.bean.Location_AlarmFlag;
import com.uninew.net.utils.DefineNetAction;
import com.uninew.net.utils.NetBroadCastTool;

/**
 * 报警状态报告控制
 * Created by Administrator on 2017/10/17.
 */

public class AlarmHandle {
    private Context mContext;
    private Location_AlarmFlag mCurrentAlarm;
    private static int alarmNumber = 0;//报警数目
    private static int alarmFailureNumber = 0;//报警故障数目
    private NetBroadCastTool mNetBroadCastTool;

    public AlarmHandle(Context mContext) {
        this.mContext = mContext;
        mNetBroadCastTool = new NetBroadCastTool(mContext);
        init();
    }

    private void init() {
    }

    public Location_AlarmFlag getmCurrentAlarm() {
        return mCurrentAlarm;
    }

    public void setmCurrentAlarm(Location_AlarmFlag mCurrentAlarm) {
        this.mCurrentAlarm = mCurrentAlarm;

        //状态报告
        if (mCurrentAlarm.warnBitNumber == 0 || mCurrentAlarm.warnBitNumber == 1 || mCurrentAlarm.warnBitNumber == 16
                || mCurrentAlarm.warnBitNumber == 17 || mCurrentAlarm.warnBitNumber == 18
                || mCurrentAlarm.warnBitNumber == 19 || mCurrentAlarm.warnBitNumber == 20
                || mCurrentAlarm.warnBitNumber == 21 || mCurrentAlarm.warnBitNumber == 22
                || mCurrentAlarm.warnBitNumber == 23 || mCurrentAlarm.warnBitNumber == 24
                || mCurrentAlarm.warnBitNumber == 25) { //报警状态报告

            if (mCurrentAlarm.warnBitValue == 0x01) {
                mNetBroadCastTool.sendState(DefineNetAction.ALARMSTATE, 0x01);
                alarmNumber++;
            } else {
                if (alarmNumber > 0) {
                    alarmNumber--;
                    if (alarmNumber <= 0) {//所有报警都解除，状态为无警告
                        mNetBroadCastTool.sendState(DefineNetAction.ALARMSTATE, 0x00);
                    }
                }
            }

        } else { //故障状态报告

            if (mCurrentAlarm.warnBitValue == 0x01) {
                mNetBroadCastTool.sendState(DefineNetAction.FAILURESTATE, 0x01);
                alarmFailureNumber++;
            } else {
                if (alarmFailureNumber > 0) {
                    alarmFailureNumber--;
                    if (alarmFailureNumber <= 0) {//所有故障都解除，状态为无故障
                        mNetBroadCastTool.sendState(DefineNetAction.FAILURESTATE, 0x00);
                    }
                }
            }
        }
    }


}
