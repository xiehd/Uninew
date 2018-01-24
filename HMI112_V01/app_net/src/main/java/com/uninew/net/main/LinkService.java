package com.uninew.net.main;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.uninew.car.db.audio.AudioData;
import com.uninew.car.db.audio.AudioLocalDataSource;
import com.uninew.car.db.audio.AudioLocalSource;
import com.uninew.car.db.revenue.Revenue;
import com.uninew.car.db.revenue.RevenueLocalDataSource;
import com.uninew.car.db.settings.BaseLocalDataSource;
import com.uninew.car.db.settings.BaseLocalSource;
import com.uninew.car.db.settings.PlatformLocalDataSource;
import com.uninew.car.db.settings.PlatformLocalSource;
import com.uninew.car.db.settings.PlatformSettings;
import com.uninew.car.db.settings.SpeedSettingsLocalSource;
import com.uninew.car.db.signs.SignLocalDataSource;
import com.uninew.location.DefineLocation;
import com.uninew.location.GpsInfo;
import com.uninew.location.GpsInfoManage;
import com.uninew.location.IGpsInfoListener;
import com.uninew.location.IGpsInfoManage;
import com.uninew.net.Alarm.AlarmManager;
import com.uninew.net.Alarm.interfaces.IAudioManager;
import com.uninew.net.JT905.bean.BaseBean;
import com.uninew.net.JT905.bean.CameraPhoto;
import com.uninew.net.JT905.bean.Location_AlarmFlag;
import com.uninew.net.JT905.bean.Location_TerminalState;
import com.uninew.net.JT905.bean.T_AskQuestionAns;
import com.uninew.net.JT905.bean.T_DriverAnswerOrder;
import com.uninew.net.JT905.bean.T_DriverCancleOrder;
import com.uninew.net.JT905.bean.T_EventReport;
import com.uninew.net.JT905.bean.T_OperationDataReport;
import com.uninew.net.JT905.bean.T_OrderFinishEnsure;
import com.uninew.net.JT905.bean.T_SignInReport;
import com.uninew.net.JT905.bean.T_SignOutReport;
import com.uninew.net.JT905.comm.client.ClientReceiveManage;
import com.uninew.net.JT905.comm.client.IClientReceiveListener;
import com.uninew.net.JT905.comm.client.IClientReceiveManage;
import com.uninew.net.JT905.comm.server.IServerReceiveListener;
import com.uninew.net.JT905.comm.server.IServerReceiveManage;
import com.uninew.net.JT905.comm.server.IServerSendManage;
import com.uninew.net.JT905.comm.server.ServerReceiveManage;
import com.uninew.net.JT905.comm.server.ServerSendManage;
import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.Define;
import com.uninew.net.JT905.common.IpInfo;
import com.uninew.net.JT905.common.ProtocolTool;
import com.uninew.net.JT905.common.TimeTool;
import com.uninew.net.JT905.manage.IPlatformLinkCallBack;
import com.uninew.net.JT905.manage.IPlatformLinkManage;
import com.uninew.net.JT905.manage.PlatformLinkManage;
import com.uninew.net.JT905.manage.PlatformLinkState;
import com.uninew.net.JT905.protocol.ProtocolHead;
import com.uninew.net.JT905.protocol.ProtocolPacket;
import com.uninew.net.Taximeter.bean.P_TaxiOperationDataReport;
import com.uninew.net.camera.CameraHandle;
import com.uninew.net.camera.PictureUploadHandler;
import com.uninew.net.location.LocationReportHandle;
import com.uninew.net.location.LocationReportReplenish;
import com.uninew.net.record.AudioUploadHandler;
import com.uninew.net.record.RecordHander;
import com.uninew.net.record.RequestRecord;
import com.uninew.net.utils.DefineNetAction;
import com.uninew.net.utils.LogTool;
import com.uninew.net.utils.NetBroadCastTool;
import com.uninew.net.utils.SignOrSignOutTools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import static com.uninew.net.JT905.common.BaseMsgID.PLATFORM_CONFIRM_ALARM;
import static com.uninew.net.JT905.common.BaseMsgID.PLATFORM_RELEASE_ALARM;

/**
 * Created by Administrator on 2017/8/30 0030.
 */

public class LinkService extends Service {

    private static final boolean D = true;
    private static final String TAG = "LinkService";
    public static IPlatformLinkManage platformLinkManage;
    private ReceiveDataHandle receiveDataHandle;
    public LocationReportHandle mLocationReportHandle;
    private IGpsInfoManage mGpsInfoManage;
    private LocationReportReplenish locationReplenish;
    public AlarmManager mAlarmManager;//报警
    private AlarmHandle mAlarmHandle;
//    private SpeedSettingsLocalSource speedLocalSourceListener;

    private NetBroadCastTool mNetBroadCastTool;
    public RecordHander recordHander;

    public IServerSendManage mServerSendManage;
    public IServerReceiveManage mServerReceiveManage;
    private PlatformLocalSource platformLocalSource;
    public BaseLocalSource baseLocalSource;
    private RevenueLocalDataSource revenuedb;

    public String deviceId = Define.Default_DeviceId;
    public ParamSetHandle mParamSetHandle;
    public CameraHandle mCameraHandle;
    public PictureUploadHandler pictureUploadHandler;
    public AudioUploadHandler audioUploadHandler;

    public SignLocalDataSource Signdb;

    private AudioLocalSource audioLocalSource;
    private Context mContext;

    public static ConcurrentHashMap<Integer, IpInfo> connectStateMap;
    private IClientReceiveManage clientReceiveManage;


    @Override
    public IBinder onBind(Intent intent) {

        return new LinkBinder();
    }


    public class LinkBinder extends Binder {

        public LinkService getService() {
            return LinkService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "--------------onCreate------------");
        mContext = this;
        //初始化905通讯接口
        initJT905();
        //初始化内部通讯接口
        initCom();
        //初始化其他参数
        initOthers();
    }

    private void initCom() {
        mServerReceiveManage = new ServerReceiveManage(this);
        mServerSendManage = new ServerSendManage(this);
        mServerReceiveManage.registerNetReceiveListener(netReceiveListener);

        clientReceiveManage = new ClientReceiveManage(this);
        clientReceiveManage.registerTaxiOperateStateListener(mTaxiOperateStateListener);//运营状态监听
    }

    private void initOthers() {
        baseLocalSource = BaseLocalDataSource.getInstance(this);
        baseLocalSource.registerNotify(changeBaseSettingsListener);
        baseLocalSource.registerNotify(mChangeSpeedSettingsListener);
        baseLocalSource.getTerminalNumber(getBaseSettingCallBack);
        revenuedb = RevenueLocalDataSource.getInstance(this);
//        speedLocalSourceListener = SpeedSettingsLocalDataSource.getInstance(this);

        receiveDataHandle = ReceiveDataHandle.getInstance(this);
        audioUploadHandler = AudioUploadHandler.getInstance(this);
        connectStateMap = new ConcurrentHashMap<>();
        mGpsInfoManage = new GpsInfoManage();
        recordHander = RecordHander.getInstance();
        recordHander.setCallBack(recordingCallBack);
        mGpsInfoManage.registerGpsInfoListener(DefineLocation.Location_ReadType_Android,
                this, gpsListener);
        mLocationReportHandle = new LocationReportHandle(this);
        locationReplenish = LocationReportReplenish.getInstance(this);
        mParamSetHandle = ParamSetHandle.getInstance(this);
        mAlarmManager = new AlarmManager(this);
        mAlarmHandle = new AlarmHandle(this);
        mAlarmManager.setIAudioListener(audioListener);
        mAlarmManager.setIAudioListener(videoListener);
        mNetBroadCastTool = new NetBroadCastTool(this);
        mCameraHandle = CameraHandle.getInstance(this);
        mCameraHandle.setCameraUploadCallBack(cameraUploadCallBack);
        platformLocalSource = PlatformLocalDataSource.getInstance(this);
        pictureUploadHandler = PictureUploadHandler.getInstance(this);
        //设置初始化参数
        platformLocalSource.getService(Define.TCP_ONE
                , getPlaformCallBack);
        audioLocalSource = AudioLocalDataSource.getInstance(this.getApplicationContext());
        platformLocalSource.getService(Define.TCP_ONE, getPlaformCallBack);
        Signdb = SignLocalDataSource.getInstance(this);
    }

    private IAudioManager.IAudioListener audioListener = new IAudioManager.IAudioListener() {
        @Override
        public void starAudio() {
            RequestRecord record = new RequestRecord();
            record.setDuration(0);
            record.setFileId(110);
            recordHander.requestRecording(record);
        }

        @Override
        public void stopAudio() {
            recordHander.stop();
        }
    };

    private IAudioManager.IVideoListener videoListener = new IAudioManager.IVideoListener() {
        @Override
        public void starVideo() {

        }

        @Override
        public void stopVideo() {

        }
    };

    private BaseLocalSource.ChangeSpeedSettingsListener mChangeSpeedSettingsListener = new BaseLocalSource.ChangeSpeedSettingsListener() {

        @Override
        public void onChangeSpeedSettings(int id, String value) {
            if (mAlarmManager != null) {

            }
        }

        @Override
        public void setAlarm_speed_max(int alarm_speed_max) {
            if (mAlarmManager != null) {
                 Log.i(TAG,"max="+alarm_speed_max);
                mAlarmManager.mOverSpeedJudge.setSpeedLimit(alarm_speed_max);
            }
        }

        @Override
        public void setAlarm_speed_time(int alarm_speed_time) {
            if (mAlarmManager != null) {
                Log.i(TAG,"time="+alarm_speed_time);
                mAlarmManager.mOverSpeedJudge.setWarnIntervalTime(alarm_speed_time);
            }
        }

        @Override
        public void setPre_alarm_speed_max(int pre_alarm_speed_max) {
            if (mAlarmManager != null) {
                Log.i(TAG,"max2="+pre_alarm_speed_max);
                mAlarmManager.mOverSpeedJudge.setPreInterverSpeed(pre_alarm_speed_max);
            }
        }

        @Override
        public void setPre_alarm_speed_time(int pre_alarm_speed_time) {
            Log.i(TAG,"time="+pre_alarm_speed_time);
            if (mAlarmManager != null) {
                mAlarmManager.mOverSpeedJudge.setPreWarnIntervalTime(pre_alarm_speed_time);
            }
        }

        @Override
        public void setSpeed_source(int speed_source) {
            if (mAlarmManager != null) {
            }
        }

        @Override
        public void setSpeed_unit(int speed_unit) {
            if (mAlarmManager != null) {
            }
        }
    };

    private CameraHandle.CameraUploadCallBack cameraUploadCallBack = new CameraHandle.CameraUploadCallBack() {
        @Override
        public void onCameraPhoto(CameraPhoto cameraPhoto) {
            if (cameraPhoto != null && cameraPhoto.getDataBytes() != null
                    && cameraPhoto.getDataBytes().length > 0) {
                pictureUploadHandler.pictureupload(cameraPhoto);
            }
        }
    };


    private BaseLocalSource.GetBaseSettingCallBack getBaseSettingCallBack = new BaseLocalSource.GetBaseSettingCallBack() {
        @Override
        public void onDBBaseDataLoaded(String s) {
            platformLinkManage.setDeviceId(s);
            deviceId = s;
        }

        @Override
        public void onDataNotAailable() {

        }
    };

    private PlatformLocalSource.GetPlaformCallBack getPlaformCallBack = new PlatformLocalSource.GetPlaformCallBack() {
        @Override
        public void onDBBaseDataLoaded(final PlatformSettings platformSettings) {
            Log.d(TAG, platformSettings.toString());
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
            platformLinkManage.setTcpParams(platformSettings.getTvpId(),
                    platformSettings.getMainIp(), platformSettings.getMainPort(),
                    platformSettings.getSpareIp(), platformSettings.getSparePort());
            platformLinkManage.createSocket(platformSettings.getTvpId());
//                }
//            }).start();

        }

        @Override
        public void onDataNotAailable() {

        }
    };

    private BaseLocalSource.ChangeBaseSettingsListener changeBaseSettingsListener = new BaseLocalSource.ChangeBaseSettingsListener() {
        @Override
        public void onChangeBaseSettings(int id, String value) {

        }

        @Override
        public void getPlateNumber(@NonNull String value) {

        }

        @Override
        public void getTerminalNumber(@NonNull String value) {
            platformLinkManage.setDeviceId(value);
            deviceId = value;
            Enumeration<IpInfo> ipInfos = connectStateMap.elements();
            if (ipInfos != null) {
                while (ipInfos.hasMoreElements()) {
                    final IpInfo mIpInfo = ipInfos.nextElement();
                    if (mIpInfo != null) {
                        platformLinkManage.setTcpParams(mIpInfo.tcpId,
                                mIpInfo.mainIp, mIpInfo.mainPort,
                                mIpInfo.spareIp, mIpInfo.sparePort);
                        platformLinkManage.createSocket(mIpInfo.tcpId);
                    }
                }
            }
        }

        @Override
        public void getCompanyNumber(@NonNull String value) {

        }

        @Override
        public void getDvrSerialNumber(@NonNull String value) {

        }

        @Override
        public void getOutTimeExite(@NonNull String value) {

        }

        @Override
        public void getPrintSensitivity(@NonNull String value) {

        }

        @Override
        public void getPrerecordTime(@NonNull String value) {

        }

        @Override
        public void getDelayTime(@NonNull String value) {

        }
    };

    private RecordHander.RecordingCallBack recordingCallBack = new RecordHander.RecordingCallBack() {
        @Override
        public void onFailure(int error) {

        }

        @Override
        public void onSuccess(RequestRecord record, byte[] buffers) {
            if (record != null && buffers != null && buffers.length > 0) {
                record.setAudioLength(buffers.length);
                record.setAudioBuffers(buffers);
                audioUploadHandler.pictureupload(record);
                AudioData audioData = new AudioData();
                audioData.setEndTime(record.getEndTime());
                audioData.setAudioBuffers(record.getAudioBuffers());
                audioData.setEndLongitude(record.getEndLongitude());
                audioData.setAudioLength(record.getAudioLength());
                audioData.setEndLatitude(record.getEndLatitude());
                audioData.setCarName(record.getCarName());
                audioData.setCodingType(record.getCodingType());
                audioData.setFileId(record.getFileId());
                audioData.setIsuId(record.getIsuId());
                audioData.setReason(record.getReason());
                audioData.setRevenueId(record.getRevenueId());
                audioData.setStartLatitude(record.getStartLatitude());
                audioData.setStartLongitude(record.getStartLongitude());
                audioData.setStartTime(record.getStartTime());
                audioLocalSource.saveDBData(audioData);
            }
        }
    };

    // -------------------------------905接口通讯部分--------------------------------------------------
    private void initJT905() {
        // TODO Auto-generated method stub

        platformLinkManage = PlatformLinkManage.getInstance(this);
        platformLinkManage.setPlatformLinkCallBack(platformLinkCallBack);

    }

//    private byte[] imgBuffer = null;
//    private int imgItem = 0;
//
//    /* SD卡根目录*/
//    protected static final String SDCARD_PATH = SDCardUtils.getSDCardPaths(false);
//    /* 存储地址*/
//    private String savePath = SDCARD_PATH + File.separator + "photo1" + File.separator;

    /**
     * 发送到平台
     *
     * @param baseData
     */
    public void sendToJT905(BaseBean baseData) {

        //        byte[] body = baseData.getDataBytes();
        //        int id = baseData.getMsgId();
        //        String log = "上报平台数据 \r\n消息ID:"+ ByteTools.ByteToStr(ByteTools.intToBytes(id))+"\r\n消息内容:"
        //                +ByteTools.ByteToStr(baseData.getDataBytes())+"   "
        //                + TimeTool.timestampTormat2(TimeTool.getCurrentTimestamp()) +"\r\n";
        //        FileText.writeText(log);
        //
        //        platformLinkManage.sendMsg(baseData);

        //模拟平台下发
        if (baseData.getMsgId() == BaseMsgID.LOCATION_INFORMATION_QUERY || baseData.getMsgId() == BaseMsgID.TEMP_LOCATION_QUERY
                || baseData.getMsgId() == BaseMsgID.ORDER_SEND_DOWN
                || baseData.getMsgId() == BaseMsgID.DRIVER_ANSWER_ORDER_MSG
                || baseData.getMsgId() == PLATFORM_CONFIRM_ALARM//中心确认报警
                || baseData.getMsgId() == BaseMsgID.CAMERA_SHOOT_NOW_COMMOND//中心确认报警
                || baseData.getMsgId() == PLATFORM_RELEASE_ALARM
                || baseData.getMsgId() == BaseMsgID.TERMINAL_CONTROL
                || baseData.getMsgId() == BaseMsgID.CALL_BACK) {
            Log.d(TAG, "位置查询！");
            ProtocolPacket messagePack = new ProtocolPacket();
            byte[] tempDatas = baseData.getDataBytes();
            messagePack.setHead(new ProtocolHead(baseData.getMsgId(),
                    tempDatas.length, Define.Default_DeviceId, 010));
            messagePack.setTcpId(baseData.getTcpId());
            messagePack.setTransportId(baseData.getTransportId());
            messagePack.setBody(tempDatas);
            platformLinkCallBack.onReceiveDatas(messagePack);
            platformLinkManage.sendMsg(baseData);
        } else {
            //此处不是模拟上报，以后处理代码时候勿删
            if (connectStateMap.containsKey(Define.TCP_ONE)
                    && connectStateMap.get(Define.TCP_ONE) != null
                    && connectStateMap.get(Define.TCP_ONE).linkState == 0x01) {
                platformLinkManage.sendMsg(baseData);
            }
        }
    }

    private IPlatformLinkCallBack platformLinkCallBack = new IPlatformLinkCallBack() {
        @Override
        public void onTCPLinkState(int tcpId, boolean isMainIpLinked, IpInfo ipInfo, PlatformLinkState linkState) {
            if (linkState == PlatformLinkState.NORMAL) {
                ipInfo.linkState = 0x01;
                Log.v(TAG, "----onTCPLinkState---TCP：" + tcpId + ",已连接");
                locationReplenish.startReplenish();
                mNetBroadCastTool.sendDeviceState(0x07, 0x01);
            } else {
                ipInfo.linkState = 0x00;
                Log.v(TAG, "----onTCPLinkState---TCP：" + tcpId + ",已断开");
                locationReplenish.stopReplenish();
                if (tcpId == Define.TCP_ONE) {
                    mNetBroadCastTool.sendState(DefineNetAction.CONNETSTATE, 0x10);
                } else {
                    mNetBroadCastTool.sendState(DefineNetAction.CONNETSTATE, 0x20);
                }
                mNetBroadCastTool.sendDeviceState(0x07, 0x00);
            }
            ipInfo.isLinkMain = isMainIpLinked;
            mServerSendManage.linkStateNotify(ipInfo);
            if (connectStateMap.containsKey(tcpId)) {
                connectStateMap.replace(tcpId, ipInfo);
            } else {
                connectStateMap.put(tcpId, ipInfo);
            }
        }

        @Override
        public void onReceiveDatas(ProtocolPacket packet) {
            if (receiveDataHandle == null) {
                receiveDataHandle = ReceiveDataHandle.getInstance(LinkService.this);
            }
            receiveDataHandle.msgHandle(packet);
            LogTool.logBytes("onReceiveDatas", packet.getProtocolPacketBytes());
        }

        @Override
        public void onGeneralResponse(int tcpId, int responseId, int responseSerialNumber, int result) {
            switch (responseId) {
                case BaseMsgID.OPERATION_DATA_REPORT://运营数据
                    mServerSendManage.operationDataReportAns(result);
                    break;
                case BaseMsgID.TERMINAL_HEARBEAT://心跳应答
                    if (result == 0x00) {//成功
                        if (tcpId == Define.TCP_ONE) {
                            mNetBroadCastTool.sendState(DefineNetAction.CONNETSTATE, 0x11);
                        } else {
                            mNetBroadCastTool.sendState(DefineNetAction.CONNETSTATE, 0x21);
                        }

                    } else {//失败和消息有误
                        if (tcpId == Define.TCP_ONE) {
                            mNetBroadCastTool.sendState(DefineNetAction.CONNETSTATE, 0x10);
                        } else {
                            mNetBroadCastTool.sendState(DefineNetAction.CONNETSTATE, 0x20);
                        }
                    }
                    break;
                case BaseMsgID.SIGN_IN_REPORT://签到通用应答
                    mServerSendManage.signInReportAns(result);
                    break;
                case BaseMsgID.SIGN_OUT_REPORT://签退通用应答
                    mServerSendManage.signOutReportAns(result);
                    break;
            }
        }
    };
    //-----------------------------------与外部应用通讯-----------------------------------------
    private IServerReceiveListener.INetReceiveListener netReceiveListener = new IServerReceiveListener.INetReceiveListener() {
        @Override
        public void createLink(IpInfo ipInfo) {
            if (D)
                Log.v(TAG, "--createLink---" + ipInfo.toString());
            platformLinkManage.setTcpParams(ipInfo.tcpId, ipInfo.mainIp, ipInfo.mainPort, ipInfo.spareIp, ipInfo.sparePort);
            platformLinkManage.createSocket(ipInfo.tcpId);
        }

        @Override
        public void disConnectLink(final int tcpId) {
            if (D)
                Log.v(TAG, "--disConnectLink---tcpId=" + tcpId);
//            new Thread() {
//                @Override
//                public void run() {
            platformLinkManage.closeSocket(tcpId);
//                }
//            }.start();
        }

        @Override
        public void queryLinkState(int tcpId) {
            if (D)
                Log.v(TAG, "--queryLinkState---tcpId=" + tcpId);
            if (connectStateMap.containsKey(tcpId)) {
                mServerSendManage.linkStateNotify(connectStateMap.get(tcpId));
            }
        }

        @Override
        public void driverAnswerOrder(int businessId) {
            if (D)
                Log.v(TAG, "--driverAnswerOrder---businessId=" + businessId);
            T_DriverAnswerOrder answerOrder = new T_DriverAnswerOrder(businessId);
            sendToJT905(answerOrder);
        }

        @Override
        public void driverCancelOrder(T_DriverCancleOrder driverCancleOrder) {
            if (D)
                Log.v(TAG, "--driverCancelOrder---");
            sendToJT905(driverCancleOrder);
        }

        @Override
        public void orderFinishEnsure(int businessId) {
            if (D)
                Log.v(TAG, "--orderFinishEnsure---businessId:" + businessId);
            sendToJT905(new T_OrderFinishEnsure(businessId));
        }

        @Override
        public void operationDataReport(T_OperationDataReport operationDataReport) {
            if (operationDataReport == null)
                return;
            sendToJT905(operationDataReport);
            Revenue mRevenue = new Revenue();
            mRevenue.setUpCarLocation(operationDataReport.getUpCarLocation().getDataBytes());
            mRevenue.setDownLocation(operationDataReport.getDownCarLocation().getDataBytes());
            mRevenue.setRevenueId(operationDataReport.getOperationId());
            mRevenue.setEvaluationId(operationDataReport.getEvaluationId());
            mRevenue.setEvaluation(operationDataReport.getEvaluationOption());
            mRevenue.setEvaluationExtended(operationDataReport.getEvaluationExtended());
            mRevenue.setOrderId(operationDataReport.getOrderId());
            mRevenue.setRevenueDatas(operationDataReport.getOperationDatas());
            mRevenue.setTime(TimeTool.timestampTormat2(TimeTool.getCurrentTimestamp()));
            revenuedb.saveDBData(mRevenue);
        }

        @Override
        public void eventReport(int eventId) {
            sendToJT905(new T_EventReport(eventId));
        }

        @Override
        public void askQuestionAns(T_AskQuestionAns askQuestionAns) {
            sendToJT905(askQuestionAns);
        }

        @Override
        public void signInReport(T_SignInReport signInMsg) {
            if (signInMsg == null) {
                Log.e(TAG, "----signInReport error:----signInMsg = null--");
                return;
            }
            signInMsg.setLocationReport(mLocationReportHandle.currentLocation);
            sendToJT905(signInMsg);
            //保存数据库
            Signdb.saveDBData(SignOrSignOutTools.getSignDB(signInMsg));

        }

        @Override
        public void signOutReport(T_SignOutReport signOutMsg) {
            if (signOutMsg == null) {
                Log.e(TAG, "----signOutReport error:----signOutMsg = null--");
                return;
            }
            signOutMsg.setLocationReport(mLocationReportHandle.currentLocation);
            sendToJT905(signOutMsg);

            Signdb.saveDBData(SignOrSignOutTools.getSignOutDB(signOutMsg));
        }

        @Override
        public void sendWarnMsg(Location_AlarmFlag alarmFlag) {
            if (D)
                Log.v(TAG, "--sendWarnMsg---");
            mLocationReportHandle.currentWarnFlag = alarmFlag.setWarnFlag(mLocationReportHandle.currentWarnFlag);
            mLocationReportHandle.currentLocation.setAlarmIndication(mLocationReportHandle.currentWarnFlag);
            sendToJT905(mLocationReportHandle.currentLocation);
            if (alarmFlag.warnBitNumber == 0) {
                if (alarmFlag.warnBitValue == 0x01) {//紧急报警开始
                    mLocationReportHandle.setEmergencyState(true);
                } else {//紧急报警结束
                    mLocationReportHandle.setEmergencyState(false);
                }
            }
            //状态报告(System UI状态更新)
            mAlarmHandle.setmCurrentAlarm(alarmFlag);
        }

        @Override
        public void sendStateMsg(Location_TerminalState StateFlag) {
            if (D)
                Log.v(TAG, "--sendStateMsg---");
            mLocationReportHandle.currentState = StateFlag.setState(mLocationReportHandle.currentState);
            mLocationReportHandle.currentLocation.setState(mLocationReportHandle.currentState);
            sendToJT905(mLocationReportHandle.currentLocation);
            //获取部分标志位
            switch (StateFlag.stateBitNumber) {
                case 8:
                    if (StateFlag.stateBitValue == 0x01) {
                        PublicVar.ACC_State = PublicVar.ACC_ON;
                    } else {
                        PublicVar.ACC_State = PublicVar.ACC_OFF;
                    }
                    mLocationReportHandle.setReportParams();
                    break;
                case 9://空重车状态变化
                    if (StateFlag.stateBitValue == 0x01) {
                        PublicVar.Car_State = PublicVar.Car_NonEmpty;
                    } else {
                        PublicVar.Car_State = PublicVar.Car_Empty;
                    }
                    mLocationReportHandle.setReportParams();
                    break;
            }
        }

        @Override
        public void sendMsg(BaseBean msg) {
            sendToJT905(msg);
        }
    };
    //-------------------------------------GPS监听-------------------------------------------
    private IGpsInfoListener gpsListener = new IGpsInfoListener() {
        @Override
        public void gpsInfo(GpsInfo gpsInfo) {
            //            if(D)Log.d(TAG,"gpsListener,gpsInfo="+gpsInfo.toString());
            LocationReportHandle.mGpsInfo = gpsInfo;

            if (gpsInfo.isPositioned()) {
                mAlarmManager.mDevicefailureJudge.sendfailure(0x03, 0x01);
                mNetBroadCastTool.sendState(DefineNetAction.LOCATIONSTATE, 0x01);
            } else {
                mAlarmManager.mDevicefailureJudge.sendfailure(0x03, 0x00);
                mNetBroadCastTool.sendState(DefineNetAction.LOCATIONSTATE, 0x00);
            }

            //            if (gpsInfo.getAntennaState() == 0){
            //            mAlarmManager.mDevicefailureJudge.sendfailure(3,0x00);
            //            }else if(gpsInfo.getAntennaState() == 1){
            //                mAlarmManager.mDevicefailureJudge.sendfailure(3,0x01);
            //            }
        }
    };
    //------------------------------------------------------------------------------------

//-----------------------------------运营状态监听---------------------------------------------------

    private static int operateID = 0x00;//运营ID更新
    private T_OperationDataReport operationDataReport = new T_OperationDataReport();
    IClientReceiveListener.ITaxiOperateStateListener mTaxiOperateStateListener = new IClientReceiveListener.ITaxiOperateStateListener() {
        @Override
        public void TaxiOperateStateStart(String time) {//进入重车（单次运营开始）
            if (D)
                Log.d(TAG, "------------进入重车------------" + time);
            if (operationDataReport == null) {
                operationDataReport = new T_OperationDataReport();
            }
            SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMddhhmmss");

            try {
                Date timedate = df1.parse(time);
                java.sql.Timestamp timestamp = new java.sql.Timestamp(timedate.getTime());
                operateID = ProtocolTool.setBit905(timestamp);
                Log.v(TAG, "运营ID = " + (operateID >> 26) + "-" + (operateID >> 22 & 0x0f)
                        + "-" + (operateID >> 17 & 0x1f) + ":" + (operateID & 0x1f));
                operationDataReport.setOperationId(operateID);
                operationDataReport.setUpCarLocation(mLocationReportHandle.currentLocation);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void TaxiOperateStateEnd(P_TaxiOperationDataReport p_taxiOperationDataReport) {//进入空车（单次运营结束）
            if (D)
                Log.d(TAG, "------------进入空车------------");
            if (p_taxiOperationDataReport == null) {
                return;
            }

            if (operationDataReport == null) {
                operationDataReport = new T_OperationDataReport();
            }

            if (p_taxiOperationDataReport.getId_type() == 0x00) {//当班
                operationDataReport.setDownCarLocation(mLocationReportHandle.currentLocation);
                operationDataReport.setOperationDatas(p_taxiOperationDataReport.getDataBytes());
                operationDataReport.setEvaluationExtended(0x00);
                operationDataReport.setOrderId(0);//电召ID
                //评价
                Intent i = new Intent();
                i.setAction("com.uninew.car.EvaluationActivity");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("revenue", operationDataReport);
                mContext.startActivity(i);
            } else if (p_taxiOperationDataReport.getId_type() == 0x01) {//补传

                operationDataReport.setDownCarLocation(mLocationReportHandle.currentLocation);
                operationDataReport.setUpCarLocation(mLocationReportHandle.currentLocation);
                operationDataReport.setOperationId(ProtocolTool.setBit905(TimeTool.getCurrentTimestamp()));
                operationDataReport.setOperationDatas(p_taxiOperationDataReport.getDataBytes());
                operationDataReport.setEvaluationExtended(0x00);
                operationDataReport.setEvaluationOption(0x00);
                operationDataReport.setEvaluationId(ProtocolTool.setBit905(TimeTool.getCurrentTimestamp()));
                operationDataReport.setOrderId(0);//电召ID

                netReceiveListener.operationDataReport(operationDataReport);
            }
        }
    };

    @Override
    public boolean onUnbind(Intent intent) {
        //        platformLinkManage.unRegisterNetWork();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (D)
            Log.d(TAG, "onDestroy");
        mNetBroadCastTool.sendState(DefineNetAction.CONNETSTATE, 0x10);
        mNetBroadCastTool.sendState(DefineNetAction.CONNETSTATE, 0x20);
        platformLinkManage.unRegisterNetWork();
        mServerReceiveManage.unRegisterNetReceiveListener();
        mGpsInfoManage.unRegisterGpsInfoListener(DefineLocation.Location_ReadType_Android);
        mParamSetHandle.unregisterNotify();
        baseLocalSource.unregisterNotify();
        //添加线程结束的方法，避免产生内存泄漏的问题
        mCameraHandle.destroy();
        pictureUploadHandler.destroy();
        audioUploadHandler.destroy();
        recordHander.destroy();

        if (mTaxiOperateStateListener != null) {
            clientReceiveManage.unRegisterOperationListener();
        }
    }
}
