package com.uninew.net.Alarm;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.uninew.car.db.alarm.AlarmLocalDataSource;
import com.uninew.car.db.alarm.AlarmLocalSource;
import com.uninew.car.db.alarm.AlarmMessage;
import com.uninew.net.Alarm.devicefailure.DevicefailureJudge;
import com.uninew.net.Alarm.devicefailure.IDevicefaiureListener;
import com.uninew.net.Alarm.illegalalram.RegoinJudge;
import com.uninew.net.Alarm.interfaces.IAudioManager;
import com.uninew.net.Alarm.overspeed.DefineOverSpeed;
import com.uninew.net.Alarm.overspeed.IOverSpeedJudgeListener;
import com.uninew.net.Alarm.overspeed.OverSpeedJudge;
import com.uninew.net.Alarm.overtime.IOverTimeJudgeListener;
import com.uninew.net.Alarm.overtime.OverTimeJudge;
import com.uninew.net.JT905.bean.Location_AlarmFlag;
import com.uninew.net.JT905.comm.client.ClientSendManage;
import com.uninew.net.JT905.comm.client.IClientSendManage;
import com.uninew.net.JT905.util.TimeTool;
import com.uninew.net.audio.TtsUtil;

import java.sql.Timestamp;

public class AlarmManager implements IOverSpeedJudgeListener, IOverTimeJudgeListener, IDevicefaiureListener {

    private static final String TAG = "AlarmManager";
    public OverSpeedJudge mOverSpeedJudge;//超速处理
    public OverTimeJudge mOverTimeJudge;//超时处理
    public DevicefailureJudge mDevicefailureJudge;//设备故障
    public RegoinJudge mRegoinJudge;//区域判断
    private IClientSendManage mClientSendManage;
    private IAudioManager.IAudioListener mIAudioListener;//录音监听
    private IAudioManager.IVideoListener mIVideoListener;//拍照监听

    private AlarmLocalSource db;
    //    private AudioRecordManage recordManage;
    private Context service;
    private TtsUtil ttsUtil;

    public AlarmManager(Context service) {
        super();
//        mContext = con;
        this.service = service;
        mOverSpeedJudge = new OverSpeedJudge(this);
        mOverTimeJudge = new OverTimeJudge(this);
        mDevicefailureJudge = new DevicefailureJudge(this);
        mRegoinJudge = new RegoinJudge(this);
        mClientSendManage = new ClientSendManage(service);
        db = AlarmLocalDataSource.getInstance(service);
        ttsUtil = new TtsUtil(service);
//        recordManage = AudioRecordManage.getInstance();
    }

    public void setIAudioListener(IAudioManager.IAudioListener mIAudioListener) {
        this.mIAudioListener = mIAudioListener;
    }

    public void setIAudioListener(IAudioManager.IVideoListener mIVideoListener) {
        this.mIVideoListener = mIVideoListener;
    }

    @Override
    public void overSpeed(int state, long time) {
        // TODO Auto-generated method stub
        overSpeedManager(state, time);
    }

    @Override
    public void preOverSpeed(int state, long time) {
        // TODO Auto-generated method stub
        preOverSpeedManager(state, time);
    }

    /**
     * 超速处理
     *
     * @param state
     * @param time
     */
    private void overSpeedManager(int state, long time) {
        // TODO Auto-generated method stub
        switch (state) {
            case DefineOverSpeed.State_Start:
                //超速开始

                //超速开始，写入数据库

                //状态提示
                //			AppUtils.messageRemind(mainService, R.drawable.message, "超速报警", "您已经超速，请减速行驶！！", null, 103);
                //Intent intent = new Intent("Com.Uninew.AlarmOpen");

                //上报平台
                Location_AlarmFlag alarmFlag = new Location_AlarmFlag();
                alarmFlag.setOverSpeeding(true);
                mClientSendManage.sendWarnMsg(alarmFlag);
                break;
            case DefineOverSpeed.State_Persist:
                //超速持续，语音提示
                startSpeech("您已超速，请减速行驶！");
                break;
            case DefineOverSpeed.State_End:
                //超速结束
                //超速结束，超速图标关闭

                //保存数据库
                String day = TimeTool.timestampTormat(new Timestamp(System.currentTimeMillis()));
                String startT = TimeTool.timestampTormat(new Timestamp(mOverSpeedJudge.getStartOverSpeedTime()));
                String endT = TimeTool.timestampTormat(new Timestamp(System.currentTimeMillis()));
                long continuedT = System.currentTimeMillis() - mOverSpeedJudge.getStartOverSpeedTime();
                db.saveDBData(new AlarmMessage(day, startT, endT, Integer.parseInt(continuedT + ""), "超速报警", 0));
                //Intent intent3 = new Intent("Com.Uninew.AlarmClose");

                //上报平台
                Location_AlarmFlag alarmFlag2 = new Location_AlarmFlag();
                alarmFlag2.setOverSpeeding(false);
                mClientSendManage.sendWarnMsg(alarmFlag2);

                break;
            default:
                break;
        }
    }

    /**
     * 预超速处理
     *
     * @param state
     * @param time
     */
    private void preOverSpeedManager(int state, long time) {
        // TODO Auto-generated method stub
        switch (state) {
            case DefineOverSpeed.State_Start:
                //预超速开始
                startSpeech("您即将超速，请控制车速！");
                //预超速开始

                //状态提示

                break;
            case DefineOverSpeed.State_Persist:
                //预超速持续
                startSpeech("您即将超速，请控制车速！");
                break;
            case DefineOverSpeed.State_End:
                //预超速结束
                //写入数据库
                String day = TimeTool.timestampTormat(new Timestamp(System.currentTimeMillis()));
                String startT = TimeTool.timestampTormat(new Timestamp(mOverSpeedJudge.getStartPreOverSpeedTime()));
                String endT = TimeTool.timestampTormat(new Timestamp(System.currentTimeMillis()));
                long continuedT = System.currentTimeMillis() - mOverSpeedJudge.getStartPreOverSpeedTime();
                db.saveDBData(new AlarmMessage(day, startT, endT, Integer.parseInt(continuedT + ""), "超速预警", 0));
                //状态提示
                break;
            default:
                break;
        }
    }

    /**
     * 语音播报
     *
     * @param message
     */
    public void startSpeech(String message) {
        ttsUtil.setAudioStreamType(AudioManager.STREAM_ALARM);
        ttsUtil.speak(message);
    }

    ///////////////////////////////////////////////////超时驾驶////////////////////////////////////////////////////////////////////////
    @Override
    public void overTime(int type, int state, long time) {
        switch (type) {
            case 0x01://连续驾驶超时
                Location_AlarmFlag alarmFlag1 = new Location_AlarmFlag();
                String day = TimeTool.timestampTormat(new Timestamp(System.currentTimeMillis()));
                String startT = TimeTool.timestampTormat(new Timestamp(System.currentTimeMillis()));
                String endT = TimeTool.timestampTormat(new Timestamp(System.currentTimeMillis()));
                if (state == 0x00) {//结束
                    //写入数据库
                    endT = TimeTool.timestampTormat(new Timestamp(System.currentTimeMillis()));
                    db.saveDBData(new AlarmMessage(day, startT, endT, Integer.parseInt(time + ""), "连续驾驶超时", 2));
                    //上报平台
                    alarmFlag1.setContinuousDriving(false);
                    mClientSendManage.sendWarnMsg(alarmFlag1);
                } else if (state == 0x01) {//开始

                    startT = TimeTool.timestampTormat(new Timestamp(System.currentTimeMillis()));
                    startSpeech("你已经连续驾驶超过4小时，请控制驾驶时间！");
                    //写入数据库

                    //上报平台
                    alarmFlag1.setContinuousDriving(true);
                    mClientSendManage.sendWarnMsg(alarmFlag1);
                }

                break;
            case 0x02://当天累计超时
                Location_AlarmFlag alarmFlag2 = new Location_AlarmFlag();
                if (state == 0x00) {//结束
                    //写入数据库

                    //上报平台
                    alarmFlag2.setAccumulatedOvertimeDriving(false);
                    mClientSendManage.sendWarnMsg(alarmFlag2);
                } else if (state == 0x01) {//开始
                    String day2 = TimeTool.timestampTormat(new Timestamp(System.currentTimeMillis()));
                    String startT2 = TimeTool.timestampTormat(new Timestamp(System.currentTimeMillis()));
                    String endT2 = TimeTool.timestampTormat(new Timestamp(System.currentTimeMillis()));
                    startSpeech("当班累计驾驶超过24小时，请控制驾驶时间！");
                    //写入数据库
                    db.saveDBData(new AlarmMessage(day2, startT2, endT2, Integer.parseInt(time + ""), "当天累计驾驶超时", 2));
                    //上报平台
                    alarmFlag2.setAccumulatedOvertimeDriving(true);
                    mClientSendManage.sendWarnMsg(alarmFlag2);
                }
                break;
        }
    }

    ///////////////////////////////////////////////////设备故障和紧急报警////////////////////////////////////////////////////////////////////////
    @Override
    public void devicefailure(int type, int state) {
        Location_AlarmFlag alarmFlag = new Location_AlarmFlag();
        String day = TimeTool.timestampTormat3(new Timestamp(System.currentTimeMillis()));
        String startTime = TimeTool.timestampTormat(new Timestamp(System.currentTimeMillis()));
        String endTime = TimeTool.timestampTormat(new Timestamp(System.currentTimeMillis()));
        String context = "";
        switch (type) {
            case 0x00://紧急报警
                context = "紧急报警";
                if (state == 0x00) {//触发报警
                    alarmFlag.setEmergency(true);
                    //开始录音

                    if (mIAudioListener != null) {
                        mIAudioListener.starAudio();
                    }
//                    recordManage.startRecordAndFile();
                    //开始拍照
                    if (mIVideoListener != null) {
                        mIVideoListener.starVideo();
                    }

                } else if (state == 0x01) {//解除报警

                    //取消录音
                    if (mIAudioListener != null) {
                        mIAudioListener.stopAudio();
                    }
                    //取消拍照
                    if (mIVideoListener != null) {
                        mIVideoListener.stopVideo();
                    }
                    alarmFlag.setEmergency(false);
                }
                //
                break;
            case 0x01://预警
                context = "预警";
                if (state == 0x00) {//触发报警
                    alarmFlag.setPreWarn(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setPreWarn(false);
                }
                break;
            case 0x02://GNSS故障
                context = "GNSS故障";
                if (state == 0x00) {//触发报警
                    alarmFlag.setGNSSFault(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setGNSSFault(false);
                }
                break;
            case 0x03://GNSS天线未接或被剪断
                context = "GNSS天线未接或被剪断";
                if (state == 0x00) {//触发报警
                    alarmFlag.setGNSSAerial(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setGNSSAerial(false);
                }
                sendDeviceState(0x04, state);

                break;
            case 0x04://GNSS天线短路bit4
                context = "GNSS天线短路";
                if (state == 0x00) {//触发报警
                    alarmFlag.setGNSSShortCircuit(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setGNSSShortCircuit(false);
                }
                break;
            case 0x05://主电源欠压报警bit5
                context = "主电源欠压";
                if (state == 0x00) {//触发报警
                    alarmFlag.setMainUndervoltage(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setMainUndervoltage(false);
                }
                sendDeviceState(0x06, state);
                break;
            case 0x06://主电源掉电bit6
                context = "主电源掉电";
                if (state == 0x00) {//触发报警
                    alarmFlag.setMainPowerDown(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setMainPowerDown(false);
                }
                break;
            case 0x07://主显示屏故障bit7
                context = "主显示屏故障";
                if (state == 0x00) {//触发报警
                    alarmFlag.setMainLCDError(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setMainLCDError(false);
                }
                break;
            case 0x08://TTS模块故障bit8
                context = "TTS模块故障";
                if (state == 0x00) {//触发报警
                    alarmFlag.setTTSError(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setTTSError(false);
                }
                break;
            case 0x09://摄像头故障bit9
                context = "摄像头故障";
                if (state == 0x00) {//触发报警
                    alarmFlag.setCameraError(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setCameraError(false);
                }
                sendDeviceState(0x00, state);
                break;
            case 10://计价器故障bit10
                context = "计价器故障";
                if (state == 0x00) {//触发报警
                    alarmFlag.setMeterError(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setMeterError(false);
                }
                sendDeviceState(0x01, state);
                break;
            case 11://服务评价器故障bit11
                context = "服务评价器故障";
                if (state == 0x00) {//触发报警
                    alarmFlag.setEvaluatorError(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setEvaluatorError(false);
                }
                break;
            case 12://广告牌故障bit12
                context = "广告牌故障";
                if (state == 0x00) {//触发报警
                    alarmFlag.setADError(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setADError(false);
                }
                break;
            case 13://LED显示屏故障bit13
                context = "LED显示屏故障";
                if (state == 0x00) {//触发报警
                    alarmFlag.setLEDScreenError(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setLEDScreenError(false);
                }
                break;
            case 14://安全模块故障bit14
                context = "安全模块故障";
                if (state == 0x00) {//触发报警
                    alarmFlag.setSecurityModulError(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setSecurityModulError(false);
                }
                break;
            case 15://LED顶灯故障bit15
                context = "LED顶灯故障";
                if (state == 0x00) {//触发报警
                    alarmFlag.setLEDLightError(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setLEDLightError(false);
                }
                break;
            case 19://超时停车bit19
                context = "超时停车";
                if (state == 0x00) {//触发报警
                    alarmFlag.setOverTimeParking(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setOverTimeParking(false);
                }
                break;
            case 20://进出区域/路线bit20
                if (state == 0x00) {//触发报警
                    context = "进入设定区域";
                    alarmFlag.setInOutAreaOrLine(true);
                } else if (state == 0x01) {//解除报警
                    context = "出设定区域";
                    alarmFlag.setInOutAreaOrLine(false);
                }
                break;
            case 21://路段行驶时间不足/过长bit21
                break;
            case 22://禁行路段行驶bit22
                break;
            case 23://速度传感器故障bit23
                break;
            case 24://车辆非法点火bit24
                context = "车辆非法点火";
                if (state == 0x00) {//触发报警
                    alarmFlag.setIllegalAccOn(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setIllegalAccOn(false);
                }
                break;
            case 25://车辆非法移位bit25
                context = "车辆非法移位";
                if (state == 0x00) {//触发报警
                    alarmFlag.setIllegalMoveCar(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setIllegalMoveCar(false);
                }
                break;
            case 26://ISU存储异常bit26
                context = "ISU存储异常";
                if (state == 0x00) {//触发报警
                    alarmFlag.setISUStrorageError(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setISUStrorageError(false);
                }
                break;
            case 27://录音异常bit27
                context = "录音异常";
                if (state == 0x00) {//触发报警
                    alarmFlag.setRecordingError(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setRecordingError(false);
                }
                break;
            case 28://计价器实时时钟超过规定的误差范围bit28
                context = "计价器实时时钟超过规定的误差范围";
                if (state == 0x00) {//触发报警
                    alarmFlag.setMeterClockOverError(true);
                } else if (state == 0x01) {//解除报警
                    alarmFlag.setMeterClockOverError(false);
                }
                break;
        }
        //写入数据库
        if (state == 0x00) {//报警触发时保存数据库
            if (type == 0 || type == 1 || type == 20 || type == 24 || type == 25 || type == 19 || type == 21
                    || type == 22) {//其他类型
                db.saveDBData(new AlarmMessage(day, startTime, "", 0, context, 2));
            } else {
                db.saveDBData(new AlarmMessage(day, startTime, "", 0, context, 1));
            }
        }
        //上报平台
        mClientSendManage.sendWarnMsg(alarmFlag);
        //终端状态处理
        startSpeech(context);
    }

    /**
     * 发送设备状态
     *
     * @param type
     * @param state
     */
    public void sendDeviceState(int type, int state) {
        Intent intent = new Intent();
        intent.setAction("receiver_device_state");
        intent.putExtra("device_state", state);
        intent.putExtra("device_type", type);
        service.sendBroadcast(intent);

    }
}
