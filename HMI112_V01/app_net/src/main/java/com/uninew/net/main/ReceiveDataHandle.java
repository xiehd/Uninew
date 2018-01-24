package com.uninew.net.main;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.uninew.car.db.contacts.Contact;
import com.uninew.car.db.contacts.ContactLocalDataSource;
import com.uninew.car.db.paramSet.ParamSetLocalDataSource;
import com.uninew.car.db.paramSet.ParamSetLocalSource;
import com.uninew.car.db.paramSet.ParamSetting;
import com.uninew.car.db.settings.BaseLocalSource;
import com.uninew.net.JT905.bean.P_AskQuestion;
import com.uninew.net.JT905.bean.P_Callback;
import com.uninew.net.JT905.bean.P_CamaraShoot;
import com.uninew.net.JT905.bean.P_DriverAnswerOrderAns;
import com.uninew.net.JT905.bean.P_EventSet;
import com.uninew.net.JT905.bean.P_GeneralResponse;
import com.uninew.net.JT905.bean.P_LocationTrackControl;
import com.uninew.net.JT905.bean.P_OrderSendDown;
import com.uninew.net.JT905.bean.P_ParamQuery;
import com.uninew.net.JT905.bean.P_ParamSet;
import com.uninew.net.JT905.bean.P_PhoneBookSet;
import com.uninew.net.JT905.bean.P_PlatformCancleOrder;
import com.uninew.net.JT905.bean.P_TerminalControl;
import com.uninew.net.JT905.bean.P_TextIssued;
import com.uninew.net.JT905.bean.T_GeneralResponse;
import com.uninew.net.JT905.bean.T_ParamQueryAns;
import com.uninew.net.JT905.bean.T_VehicleControlAns;
import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.IpInfo;
import com.uninew.net.JT905.protocol.ProtocolPacket;
import com.uninew.net.camera.CameraManage;
import com.uninew.net.camera.RequestCamera;
import com.uninew.net.location.LocationReportHandle;
import com.uninew.net.location.TempLocationReport;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 接收平台数据分发处理类
 * Created by Administrator on 2017/8/30 0030.
 */

public class ReceiveDataHandle {

    private static final String TAG = "ReceiveDataHandle";
    private static final boolean D = true;
    private LinkService service;
    private static volatile ReceiveDataHandle INSTANCE;
    private CameraManage cameraManage;

    private ReceiveDataHandle(LinkService service) {
        this.service = service;
    }

    public static ReceiveDataHandle getInstance(LinkService service) {
        if (INSTANCE != null) {

        } else {
            synchronized (ReceiveDataHandle.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ReceiveDataHandle(service);
                }
            }
        }
        return INSTANCE;
    }

    public void msgHandle(ProtocolPacket packet) {
        byte[] body = packet.getBody();
        int id = packet.getMsgId();
        switch (id) {
            case BaseMsgID.PLATFORM_GENERAL_ANS://平台通用应答
                P_GeneralResponse gp = new P_GeneralResponse(body);
                if (D) Log.d(TAG, "平台通用应答：" + gp.toString());
                if (gp.getResult() == 0) {
                    ansJudgment(gp.getId());
                }
                break;
            case BaseMsgID.TEXT_MSG_DOWNLOAD://文本下发
                P_TextIssued textMessage = new P_TextIssued(body);
                if (D)
                    Log.d(TAG, "文本下发：" + textMessage);
                generalResponse(BaseMsgID.TEXT_MSG_DOWNLOAD, textMessage.getSerialNumber());
                service.mServerSendManage.textSendDown(textMessage);
                break;
            case BaseMsgID.TERMINAL_PARAM_SET://终端参数设置
                P_ParamSet ps = new P_ParamSet(body);
                if (D)
                    Log.d(TAG, "终端参数设置：" + ps.toString());
                List<P_ParamSet.Param> params = ps.getParams();
                if (params != null && !params.isEmpty()) {
                    int size = params.size();
                    ParamSetting[] paramSettings = new ParamSetting[size];
                    for (int i = 0; i < size; i++) {
                        P_ParamSet.Param param = params.get(i);
                        ParamSetting paramSetting = new ParamSetting();
                        paramSetting.setValue(String.valueOf(param.paramValue));
                        paramSetting.setLength(param.paramLength);
                        paramSetting.setKey(param.paramId);
                        paramSettings[i] = paramSetting;
                    }
                    service.mParamSetHandle.saveParamSetting(paramSettings);
                }
                generalResponse(BaseMsgID.TERMINAL_PARAM_SET, packet.getSerialNumber());
                break;
            case BaseMsgID.TERMINAL_PARAM_QUERY://终端参数查询
                P_ParamQuery paramQuery = new P_ParamQuery(body);
                //此处执行参数查询、应答动作
                paramQueryAns(paramQuery, packet.getSerialNumber());
                break;
            case BaseMsgID.TERMINAL_CONTROL://终端控制
                P_TerminalControl pt = new P_TerminalControl(body);
                generalResponse(BaseMsgID.TERMINAL_CONTROL, packet.getSerialNumber());
                terminalControl(pt);
                break;
            case BaseMsgID.LOCATION_INFORMATION_QUERY://位置信息查询
                //消息体为空，直接应答当前位置信息
                service.sendToJT905(LocationReportHandle.currentLocation);
                break;
            case BaseMsgID.TEMP_LOCATION_QUERY://临时位置跟踪
                P_LocationTrackControl locationTrackControl = new P_LocationTrackControl(body);
                generalResponse(BaseMsgID.TEMP_LOCATION_QUERY, locationTrackControl.getSerialNumber());
                TempLocationReport.getInstance(service).setParam(locationTrackControl.getAttribute(),
                        locationTrackControl.getTimeOrDistance(), locationTrackControl.getContinuedTimeOrDistance());
                break;
            case BaseMsgID.EVENT_SET://事件设置
                P_EventSet pe = new P_EventSet(body);
                generalResponse(BaseMsgID.EVENT_SET, packet.getSerialNumber());
                service.mServerSendManage.eventSet(pe);
                if (D)
                    Log.d(TAG, "事件设置：" + pe.toString());
                break;
            case BaseMsgID.ASK_QUESTION://提问下发
                P_AskQuestion question = new P_AskQuestion(body);
                generalResponse(BaseMsgID.ASK_QUESTION, packet.getSerialNumber());
                service.mServerSendManage.askQuestion(question);
                if (D)
                    Log.d(TAG, "提问下发：" + question.toString());
                break;
            case BaseMsgID.CALL_BACK://电话回拨
                generalResponse(BaseMsgID.CALL_BACK, packet.getSerialNumber());
                P_Callback callback = new P_Callback(body);
                Log.d(TAG, "电话回拨：" + callback.toString());
                service.mServerSendManage.callBack(callback);
                break;
            case BaseMsgID.SET_PHONE_BOOK://设置电话本
                generalResponse(BaseMsgID.SET_PHONE_BOOK, packet.getSerialNumber());
                P_PhoneBookSet phoneBookSet = new P_PhoneBookSet(body);
                savePhoneBook(phoneBookSet);
                break;
            case BaseMsgID.VEHICLE_CONTROL://车辆控制
                generalResponse(BaseMsgID.VEHICLE_CONTROL, packet.getSerialNumber());
                T_VehicleControlAns controlAns = new T_VehicleControlAns(packet.getSerialNumber(),
                        LocationReportHandle.currentLocation);
                service.sendToJT905(controlAns);
                break;
            case BaseMsgID.CAMERA_SHOOT_NOW_COMMOND://摄像头立即拍摄命令
                P_CamaraShoot camaraShoot = new P_CamaraShoot(body);
                if (camaraShoot.getShootCmd() == 0) {
                    service.mCameraHandle.stopCamera();
                } else {
                    final RequestCamera camera = new RequestCamera();
                    camera.setSerialNumber(camaraShoot.getSerialNumber());
                    if (camaraShoot.getShootCmd() == 0xffff) {
                        camera.setViedo(true);
                    } else {
                        camera.setShootTimes(camaraShoot.getShootCmd());
                        camera.setViedo(false);
                    }
                    if (camaraShoot.getShootInterval() > 0) {
                        camera.setShootInterval(camaraShoot.getShootInterval());
                    }
                    int resolution = camaraShoot.getResolution();
                    switch (resolution) {
                        case 0:
                            camera.setmWidth(320);
                            camera.setmHeight(240);
                            break;
                        case 1:
                            camera.setmWidth(640);
                            camera.setmHeight(480);
                            break;
                        case 2:
                            camera.setmWidth(800);
                            camera.setmHeight(600);
                            break;
                    }
                    if (camaraShoot.getChroma() > 0)
                        camera.setChroma(camaraShoot.getChroma());
                    if (camaraShoot.getBrightness() > 0)
                        camera.setBrightness(camaraShoot.getBrightness());
                    if (camaraShoot.getContrast() > 0)
                        camera.setContrast(camaraShoot.getContrast());
                    if (camaraShoot.getShootInterval() > 0)
                        camera.setShootInterval(camaraShoot.getShootInterval());
                    if (camaraShoot.getImageVideoQuality() > 0)
                        camera.setImageVideoQuality(camaraShoot.getImageVideoQuality());
                    camera.setSaveFile(true);
                    camera.setIsuId(service.deviceId);
                    service.baseLocalSource.getPlateNumber(new BaseLocalSource.GetBaseSettingCallBack() {
                        @Override
                        public void onDBBaseDataLoaded(String s) {
                            camera.setCarName(s);
                        }

                        @Override
                        public void onDataNotAailable() {
                            camera.setCarName("粤B00005");
                        }
                    });
                    service.mCameraHandle.requestCamera(camera);
                }
                generalResponse(BaseMsgID.CAMERA_SHOOT_NOW_COMMOND, packet.getSerialNumber());
                break;
            case BaseMsgID.STORE_IMAGE_SEARCH:// 存储图像检索
                generalResponse(BaseMsgID.STORE_IMAGE_SEARCH, packet.getSerialNumber());
                break;
            case BaseMsgID.STORE_IMAGE_UPLOAD_COMMOND://存储图像上传命令
                generalResponse(BaseMsgID.STORE_IMAGE_UPLOAD_COMMOND, packet.getSerialNumber());
                break;
            case BaseMsgID.ORDER_SEND_DOWN://订单任务下发
                P_OrderSendDown order = new P_OrderSendDown(body);
                generalResponse(BaseMsgID.ORDER_SEND_DOWN, packet.getSerialNumber());
                service.mServerSendManage.orderSendDown(order);
                break;
            case BaseMsgID.DRIVER_ANSWER_ORDER_MSG://下发抢答结果信息
                P_DriverAnswerOrderAns answerOrderAns = new P_DriverAnswerOrderAns(body);
                generalResponse(BaseMsgID.DRIVER_ANSWER_ORDER_MSG, packet.getSerialNumber());
                service.mServerSendManage.answerOrderMsg(answerOrderAns);
                break;
            case BaseMsgID.PLATFORM_CANCLE_ORDER://中心取消订单
                P_PlatformCancleOrder cancleOrder = new P_PlatformCancleOrder(body);
                generalResponse(BaseMsgID.PLATFORM_CANCLE_ORDER, packet.getSerialNumber());
                service.mServerSendManage.platformcancelOrder(cancleOrder.getBusinessId());
                break;
            case BaseMsgID.EXTERNAL_DEVICE_DOWN_PENETRATE://外设设备指令下行透传

                break;
            case BaseMsgID.STORE_AUDIO_SEARCH://音频检索
                generalResponse(BaseMsgID.STORE_AUDIO_SEARCH, packet.getSerialNumber());
                break;
            case BaseMsgID.PLATFORM_CONFIRM_ALARM://中心确认报警
                if (D)
                    Log.d(TAG, "中心确认报警：");
                generalResponse(BaseMsgID.PLATFORM_CONFIRM_ALARM, packet.getSerialNumber());
                service.mAlarmManager.mDevicefailureJudge.sendfailure(0x00, 0x00);
                break;
            case BaseMsgID.PLATFORM_RELEASE_ALARM://中心解除报警
                if (D)
                    Log.d(TAG, "中心解除报警：");
                generalResponse(BaseMsgID.PLATFORM_RELEASE_ALARM, packet.getSerialNumber());
                service.mAlarmManager.mDevicefailureJudge.sendfailure(0x00, 0x01);
                break;
            case BaseMsgID.PLATFORM_CHECK_DEVICE://中心巡检设备

                break;
            default:
                break;
        }
    }

    private void generalResponse(int MsgId, int serialNumber) {
        service.sendToJT905(new T_GeneralResponse(MsgId, serialNumber, T_GeneralResponse.Result_Success));
    }

    /**
     * 通用应答接口判断
     *
     * @param asnId
     */
    private void ansJudgment(int asnId) {
        switch (asnId) {
            case BaseMsgID.CAMERA_PICTURE_UPLOAD:
                Log.d(TAG, "照片数据包上传成功");
                service.pictureUploadHandler.sendSuccess();//照片数据包上传成功
                break;
            case BaseMsgID.STORE_AUDIOVIDEO_UPLOAD:
                Log.d(TAG, "录音数据包上传成功");
                service.audioUploadHandler.sendSuccess();//录音数据包上传成功
                break;
        }
    }

    private void savePhoneBook(P_PhoneBookSet phoneBookSet) {
        if (phoneBookSet != null) {
            List<P_PhoneBookSet.Contact> contacts = phoneBookSet.getContactList();
            if (contacts != null && !contacts.isEmpty()) {
                int size = contacts.size();
                Contact[] contactsDB = new Contact[size];
                for (int i = 0; i < size; i++) {
                    P_PhoneBookSet.Contact contact = contacts.get(i);
                    contactsDB[i] = new Contact();
                    contactsDB[i].setSign(contact.getMark());
                    contactsDB[i].setContact(contact.getContactName());
                    contactsDB[i].setTelephone(contact.getPhoneNumber());
                }
                ContactLocalDataSource.getInstance(service).saveDBData(contactsDB);
            }
        }
    }

    private void paramQueryAns(P_ParamQuery paramQuery, int serialNumber) {
        final List<P_ParamSet.Param> params = new ArrayList<>();
        if (paramQuery != null) {
            final List<Integer> paramKeys = paramQuery.getParamIds();
            if (paramKeys != null && !paramKeys.isEmpty()) {
                int paramSize = paramKeys.size();
                int[] keys = new int[paramSize];
                ParamSetLocalDataSource.getInstance(service.getApplicationContext()).getParamSettesByKey(new ParamSetLocalSource.LoadParamSettesCallBack() {
                    @Override
                    public void onDBBaseDataLoaded(List<ParamSetting> buffers) {
                        for (ParamSetting paramSetting : buffers) {
                            int paramSize = paramKeys.size();
                            for (int i = 0; i < paramSize; i++) {
                                int key = paramKeys.get(i);
                                if (key == paramSetting.getKey()) {
                                    P_ParamSet.Param param = new P_ParamSet.Param();
                                    param.paramId = paramSetting.getKey();
                                    param.paramLength = paramSetting.getLength();
                                    if (P_ParamSet.isBCDType(paramSetting.getKey())
                                            || P_ParamSet.isStringType(paramSetting.getKey())) {
                                        param.paramValue = paramSetting.getValue();
                                    } else {
                                        param.paramValue = Integer.parseInt(paramSetting.getValue().trim());
                                    }
                                    params.add(param);
                                    paramKeys.remove(i);
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onDataNotAailable() {

                    }
                }, keys);
                paramSize = paramKeys.size();
                if (paramSize > 0) {
                    for (int key : paramKeys) {
                        P_ParamSet.Param param = new P_ParamSet.Param();
                        param.paramId = key;
                        param.paramValue = getDefaultParamValue(key);
                        params.add(param);
                    }
                }
            }
        }
        if (!params.isEmpty()) {
            T_ParamQueryAns paramQueryAns = new T_ParamQueryAns();
            paramQueryAns.setParams(params);
            paramQueryAns.setResponseSerialNumber(serialNumber);
            service.sendToJT905(paramQueryAns);
        }
    }

    private Object getDefaultParamValue(int key) {
        Object value = 0;
        if (P_ParamSet.isStringType(key) || P_ParamSet.isBCDType(key)) {
            value = "0";
        } else {

        }
        return value;
    }

    private void terminalControl(P_TerminalControl terminalControl) {
        int cmd = terminalControl.getCmd();
        switch (cmd) {
            case P_TerminalControl.Cmd_ShutDown:
                shutdown();
                break;
            case P_TerminalControl.Cmd_CloseDataCommunication:
                closeAllSocket();
                break;
            case P_TerminalControl.Cmd_CloseAllCommunication:
                setAirplaneModeOn(service, true);
                break;
            case P_TerminalControl.Cmd_Reset:
                reboot(service);
                break;
            case P_TerminalControl.Cmd_RestoreSettings:
                restoreSettings(service);
                break;
        }
    }

    private void reboot(Context context) {
        Intent intent = new Intent();
        intent.setAction("receiver_main_reset");
        context.sendBroadcast(intent);
    }

    private void restoreSettings(Context context) {

        context.sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
    }

    private void closeAllSocket() {
        new Thread() {
            @Override
            public void run() {
                Enumeration<IpInfo> ipInfoEnumeration = service.connectStateMap.elements();
                if (ipInfoEnumeration != null) {
                    while (ipInfoEnumeration.hasMoreElements()) {
                        IpInfo ipInfo = ipInfoEnumeration.nextElement();
                        LinkService.platformLinkManage.closeSocket(ipInfo.tcpId);
                    }
                }
            }
        }.start();
    }

    /**
     * 设置手机飞行模式
     *
     * @param context
     * @param enabling true:设置为飞行模式 false:取消飞行模式
     */
    private void setAirplaneModeOn(Context context, boolean enabling) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, enabling ? 1 : 0);
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", enabling);
        context.sendBroadcast(intent);
    }

    /**
     * 判断手机是否是飞行模式
     *
     * @param context
     * @return
     */
    private boolean getAirplaneMode(Context context) {
        int isAirplaneMode = Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0);
        return (isAirplaneMode == 1) ? true : false;
    }

    private void shutdown() {
        try {
            //获得ServiceManager类
            Class ServiceManager = Class
                    .forName("android.os.ServiceManager");
            //获得ServiceManager的getService方法
            Method getService = ServiceManager.getMethod("getService", java.lang.String.class);
            //调用getService获取RemoteService
            Object oRemoteService = getService.invoke(null, Context.POWER_SERVICE);
            //获得IPowerManager.Stub类
            Class cStub = Class
                    .forName("android.os.IPowerManager$Stub");
            //获得asInterface方法
            Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
            //调用asInterface方法获取IPowerManager对象
            Object oIPowerManager = asInterface.invoke(null, oRemoteService);
            //获得shutdown()方法
            Method shutdown = oIPowerManager.getClass().getMethod("shutdown", boolean.class, boolean.class);
            //调用shutdown()方法
            shutdown.invoke(oIPowerManager, false, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
