package com.uninew.net.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.uninew.net.JT905.bean.P_Callback;
import com.uninew.net.JT905.bean.P_CamaraShoot;
import com.uninew.net.JT905.bean.P_GeneralResponse;
import com.uninew.net.JT905.bean.P_LocationQuery;
import com.uninew.net.JT905.bean.P_LocationTrackControl;
import com.uninew.net.JT905.bean.P_OrderSendDown;
import com.uninew.net.JT905.bean.P_ParamSet;
import com.uninew.net.JT905.bean.P_TerminalControl;
import com.uninew.net.JT905.bean.ParamKey;
import com.uninew.net.JT905.bean.T_LocationReport;
import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.Define;
import com.uninew.net.JT905.common.IpInfo;
import com.uninew.net.JT905.common.ProtocolTool;
import com.uninew.net.JT905.manage.IPlatformLinkCallBack;
import com.uninew.net.JT905.manage.IPlatformLinkManage;
import com.uninew.net.JT905.manage.PlatformLinkState;
import com.uninew.net.JT905.net.INetBase;
import com.uninew.net.JT905.net.INetBaseCallBack;
import com.uninew.net.JT905.protocol.IProtocolCallBack;
import com.uninew.net.JT905.protocol.IProtocolManage;
import com.uninew.net.JT905.protocol.ProtocolPacket;
import com.uninew.net.JT905.tcp.TCPLinkErrorEnum;
import com.uninew.net.JT905.tcp.TCPRunStateEnum;
import com.uninew.net.JT905.util.SDCardUtils;
import com.uninew.net.R;
import com.uninew.net.audio.TtsUtil;
import com.uninew.net.audio.VoicePlayerManager;
import com.uninew.net.camera.CameraManage;
import com.uninew.net.record.AudioRecordManage;
import com.uninew.net.record.ErrorCode;
import com.uninew.net.record.RecordHander;
import com.uninew.net.record.RequestRecord;

import java.io.File;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity implements INetBaseCallBack, View.OnClickListener,
        IProtocolCallBack, IPlatformLinkCallBack ,RecordHander.RecordingCallBack{

    private static final String TAG = "MainActivity";
    private static final boolean D = true;
    private static INetBase mNetBase;
    private static IProtocolManage mProtocolManager;
    private static IPlatformLinkManage platformLinkManage;
    private Button btn_open, btn_open2, btn_close, btn_close2;
    private Button btn_platformResponse, btn_order;
    private TextView tv_text;
//    private AudioRecordManage recordManage;
    private FrameLayout camera_view;
    private int i = 0;
    private RecordHander recordHander;

    private String mainIp = "114.55.55.149";
    private int mainPort = 8905;
    private TtsUtil ttsUtil;
    private VoicePlayerManager playerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recordHander = RecordHander.getInstance();
//        mNetBase=new NetBase(this, this);
//        mProtocolManager=new ProtocolManage(this,this);
//        platformLinkManage=PlatformLinkManage.getInstance(this);
//        platformLinkManage.setPlatformLinkCallBack(this);
        camera_view = (FrameLayout) findViewById(R.id.camera_view);
        tv_text = (TextView) findViewById(R.id.tv_text);
        btn_open = (Button) findViewById(R.id.btn_open);
        btn_open2 = (Button) findViewById(R.id.btn_open2);
        btn_close = (Button) findViewById(R.id.btn_close);
        btn_close2 = (Button) findViewById(R.id.btn_close2);
        btn_platformResponse = (Button) findViewById(R.id.btn_platformResponse);
        btn_order = (Button) findViewById(R.id.btn_order);
        btn_open.setOnClickListener(this);
        btn_open2.setOnClickListener(this);
        btn_close.setOnClickListener(this);
        btn_close2.setOnClickListener(this);
        btn_platformResponse.setOnClickListener(this);
        btn_order.setOnClickListener(this);

//        recordManage = AudioRecordManage.getInstance();
        registerBroadCast();
        ttsUtil = new TtsUtil(this.getApplicationContext());
        playerManager = new VoicePlayerManager(this.getApplicationContext());
    }

    private LinkService mService;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((LinkService.LinkBinder) service).getService();
            if (D) Log.d(TAG, "Binder LinkService Success!!! mService="
                    + mService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (D) Log.d(TAG, "LinkService onServiceDisconnected!!!");
        }
    };


    @Override
    public void onTCPRunState(int tcpId, TCPRunStateEnum vValue, TCPLinkErrorEnum error) {
        Log.d(TAG, "onTCPRunState , tcpId=" + tcpId + ",TCPRunStateEnum=" + vValue + ",TCPLinkErrorEnum=" + error);
    }

    @Override
    public void onTCPLinkState(final int tcpId, final boolean isMainIpLinked, final IpInfo ipInfo, final PlatformLinkState linkState) {
        Log.d(TAG, "onTCPLinkState , tcpId=" + tcpId + ",isMainIpLinked=" + isMainIpLinked + ",linkState=" + linkState + ",ipInfo=" + ipInfo.mainIp);
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                tv_text.setText("tcpId=" + tcpId + ",isMainIpLinked=" + isMainIpLinked + ",linkState=" + linkState + ",ipInfo=" + ipInfo.mainIp);
            }
        });
    }

    @Override
    public void onReceiveDatas(ProtocolPacket packet) {
        Log.d(TAG, "onReceiveDatas , msgId=0X" + Integer.toHexString((packet.getMsgId()) & 0xFF) + ",body=" + Arrays.toString(packet.getBody()));
    }

    @Override
    public void onGeneralResponse(int tcpId, int responseId, int responseSerialNumber, int result) {

    }

    @Override
    public void onReceiveTcpDatas(int tcpId, byte[] datas) {
        Log.d(TAG, "onReceiveTcpDatas , tcpId=" + tcpId + " ,datas=" + Arrays.toString(datas));
    }

    @Override
    public void onReceiveSMSDatas(String phoneNumber, byte[] datas) {
        Log.d(TAG, " onReceiveSMSDatas ,phoneNumber=" + phoneNumber + " ,datas=" + Arrays.toString(datas));
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.btn_alarm) {
            Intent intent = new Intent(this, AlarmTestActivity.class);
            startActivity(intent);
        }
        new Thread() {
            @Override
            public void run() {
                switch (v.getId()) {
                    case R.id.btn_open:
                        Log.e(TAG, "btn_open");
//                        mNetBase.createSocket(Define.TCP_ONE,"192.168.1.119",8888);
//                        mProtocolManager.createSocket(Define.TCP_ONE,"192.168.1.119",8888);
//                        platformLinkManage.setTcpParams(Define.TCP_ONE,mainIp,mainPort,"",0);
//                        platformLinkManage.createSocket(Define.TCP_ONE);
                        mService.platformLinkManage.setTcpParams(Define.TCP_ONE, mainIp, mainPort, "", 0);
                        mService.platformLinkManage.createSocket(Define.TCP_ONE);
                        break;
                    case R.id.btn_close:
                        Log.e(TAG, "btn_close");
//                        mNetBase.closeSocket(Define.TCP_ONE);
//                        mProtocolManager.closeSocket(Define.TCP_ONE);
//                        platformLinkManage.closeSocket(Define.TCP_ONE);
                        mService.platformLinkManage.closeSocket(Define.TCP_ONE);
                        break;
                    case R.id.btn_open2:
                        Log.e(TAG, "btn_open2");
//                        mNetBase.createSocket(Define.TCP_TWO,"192.168.1.119",9999);
//                        mProtocolManager.createSocket(Define.TCP_TWO,"192.168.1.119",9999);
//                        platformLinkManage.setTcpParams(Define.TCP_TWO,"192.168.1.119",9999,"192.168.1.119",2222);
//                        platformLinkManage.createSocket(Define.TCP_TWO);

                        mService.platformLinkManage.setTcpParams(Define.TCP_TWO, "192.168.1.119", 9999, "192.168.1.119", 2222);
                        mService.platformLinkManage.createSocket(Define.TCP_TWO);
                        break;
                    case R.id.btn_close2:
                        Log.e(TAG, "btn_close2");
//                        mNetBase.closeSocket(Define.TCP_TWO);
//                        mProtocolManager.closeSocket(Define.TCP_TWO);
//                        platformLinkManage.closeSocket(Define.TCP_TWO);
                        mService.platformLinkManage.closeSocket(Define.TCP_TWO);
                        break;
                    case R.id.btn_platformResponse:

                        break;
                    case R.id.btn_order:
                        P_GeneralResponse pg = new P_GeneralResponse(1, BaseMsgID.LOCATION_INFORMATION_REPORT, 0);
                        pg.setTcpId(Define.TCP_TWO);
                        pg.setTransportId(Define.Transport_TCP);
                        pg.setSmsPhoneNumber("15818613619");
//                      mProtocolManager.sendMsgByTcp(pg);
                        mService.platformLinkManage.sendMsg(pg);
                        break;
                }
            }
        }.start();

    }


    public void platformGeneralResponse(View view) {
        if (D) Log.v(TAG, "platformGeneralResponse");
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    Log.v(TAG, "----------------------------------------");
                    T_LocationReport locationReport2 = new T_LocationReport(113.902401, 22.554188, 12, 33f, 123, System.currentTimeMillis());
                    locationReport2.setTcpId(Define.TCP_ONE);
                    locationReport2.setTransportId(Define.Transport_TCP);
                    locationReport2.setSmsPhoneNumber("15818613619");
//                        mProtocolManager.sendMsgByTcp(locationReport2);
                    mService.platformLinkManage.sendMsg(locationReport2);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void initService(View view) {
//        Intent service=new Intent(this,LinkService.class);
//        this.startService(service);

        testSendBroadCaset();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "-----------onStop--------------");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "-----------onStart--------------");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "-----------onResume--------------");
        Intent service = new Intent(this, LinkService.class);
        startService(service);
        bindService(service, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(conn);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mBroadcastReceiver);
        Log.d(TAG, "-----------onDestroy--------------");
    }

    private void testSendBroadCaset() {
        System.err.println("----------testSendBroadCaset----------");
        Intent intent = new Intent("com.test");
//        intent.setPackage("com.JT905");
        sendBroadcast(intent, "com.broadcast.hmi112");
    }

    private BroadcastReceiver mBroadcastReceiver;

    /**
     * 广播注册
     */
    private void registerBroadCast() {
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction("com.test");
        mBroadcastReceiver = new FileBroadCastReceiver();
        this.registerReceiver(mBroadcastReceiver, iFilter);
    }

    @Override
    public void onFailure(int error) {
        Log.d(TAG,"录音失败："+ ErrorCode.getErrorInfo(getApplicationContext(),error));
    }

    @Override
    public void onSuccess(RequestRecord record, byte[] buffers) {
        Log.d(TAG,"录音成功！");
        if (record != null && buffers != null && buffers.length > 0) {
            record.setAudioLength(buffers.length);
            record.setAudioBuffers(buffers);
            mService.audioUploadHandler.pictureupload(record);
        }
    }

    public class FileBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.err.println("----------onReceive----------" + intent.getAction());
        }

    }

    public void OnButtonClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.settings:
                P_ParamSet.Param param = new P_ParamSet.Param();
                param.paramId = ParamKey.HeartBeat;
                param.paramLength = 4;
                param.paramValue = 25;
                P_ParamSet.Param param1 = new P_ParamSet.Param();
                param1.paramId = ParamKey.TcpResponseTimeOut;
                param1.paramLength = 4;
                param1.paramValue = 30;
                P_ParamSet.Param param2 = new P_ParamSet.Param();
                param2.paramId = ParamKey.TcpResendTime;
                param2.paramLength = 4;
                param2.paramValue = 10;
                List<P_ParamSet.Param> params = new ArrayList<>();
                params.add(param);
                params.add(param1);
                params.add(param2);
                P_ParamSet p_paramSet = new P_ParamSet();
                p_paramSet.setParams(params);
                mService.sendToJT905(p_paramSet);
                break;
            case R.id.settings_1:
                P_ParamSet.Param param3 = new P_ParamSet.Param();
                param3.paramId = ParamKey.MainIpOrDomain;
                param3.paramValue = "192.168.1.1";
                try {
                    param3.paramLength = "192.168.1.1".getBytes(ProtocolTool.CHARSET_905).length;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                P_ParamSet.Param param4 = new P_ParamSet.Param();
                param4.paramId = ParamKey.SpareIpOrDomain;
                param4.paramValue = "192.168.1.2";
                try {
                    param4.paramLength = "192.168.1.2".getBytes(ProtocolTool.CHARSET_905).length;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                P_ParamSet.Param param5 = new P_ParamSet.Param();
                param5.paramId = ParamKey.MainTcpPort;
                param5.paramLength = 4;
                param5.paramValue = 8080;
                P_ParamSet.Param param6 = new P_ParamSet.Param();
                param6.paramId = ParamKey.SpareTcpPort;
                param6.paramLength = 4;
                param6.paramValue = 8088;
                List<P_ParamSet.Param> params1 = new ArrayList<>();
                params1.add(param3);
                params1.add(param4);
                params1.add(param5);
                params1.add(param6);
                P_ParamSet p_paramSet1 = new P_ParamSet();
                p_paramSet1.setParams(params1);
                mService.sendToJT905(p_paramSet1);
                break;
            case R.id.settings_2:
                P_ParamSet.Param param13 = new P_ParamSet.Param();
                param13.paramId = ParamKey.LocationReportStratege;
                param13.paramValue = 1;
                param13.paramLength = 4;
                P_ParamSet.Param param14 = new P_ParamSet.Param();
                param14.paramId = ParamKey.LocationReportPlan;
                param14.paramValue = 1;
                param14.paramLength = 4;
                P_ParamSet.Param param15 = new P_ParamSet.Param();
                param15.paramId = ParamKey.EmptyReportIntervalDistance;
                param15.paramLength = 4;
                param15.paramValue = 40;
                P_ParamSet.Param param16 = new P_ParamSet.Param();
                param16.paramId = ParamKey.NonEmptyReportIntervalDistance;
                param16.paramLength = 4;
                param16.paramValue = 20;
                List<P_ParamSet.Param> params11 = new ArrayList<>();
                params11.add(param13);
                params11.add(param14);
                params11.add(param15);
                params11.add(param16);
                P_ParamSet p_paramSet11 = new P_ParamSet();
                p_paramSet11.setParams(params11);
                mService.sendToJT905(p_paramSet11);
                break;
            case R.id.query_location:
                P_LocationQuery locationQuery = new P_LocationQuery();
                mService.sendToJT905(locationQuery);
                break;
            case R.id.track_location:
                P_LocationTrackControl trackControl = new P_LocationTrackControl();
                trackControl.setAttribute(0);
                trackControl.setContinuedTimeOrDistance(100);
                trackControl.setTimeOrDistance(10);
                mService.sendToJT905(trackControl);
                break;
            case R.id.order_issued:
                P_OrderSendDown orderSendDown = new P_OrderSendDown();
                orderSendDown.setBusinessId(14);
                orderSendDown.setBusinessType(1);
                orderSendDown.setNeedTime("171017113000");
                orderSendDown.setBusinessDescription("从南头小关到创业天虹！");
                mService.sendToJT905(orderSendDown);
                break;
            case R.id.start_record://开始录音
                RequestRecord record = new RequestRecord();
                record.setDuration(60);
                record.setFileId(i++);
                recordHander.setCallBack(this);
                recordHander.requestRecording(record);
//                recordManage.startRecordAndFile();
                break;
            case R.id.stop_record://结束录音
//                recordManage.stopRecordAndFile();
                recordHander.stop();
                break;
            case R.id.btn_reset://复位
                Intent intent = new Intent();
                intent.setAction("receiver_main_reset");
                this.sendBroadcast(intent);
                break;
            case R.id.btn_reset_fact://恢复出厂设置(系统权限)
                this.sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
//                Intent it = new Intent();
//                it.setClassName("com.android.settings", "com.android.settings.MasterClear");
//                ComponentName cn=new ComponentName("com.android.settings","com.android.settings.MasterClear");
//                it.setComponent(cn);
//                startActivity(it);
                break;
            case R.id.btn_pinjia:
//                Intent i = new Intent();
//                i.setAction("com.uninew.car.EvaluationActivity");
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                this.startActivity(i);
                break;

            case R.id.camera:
                try {
                    FileWriter cameraFw = new FileWriter(
                            FILEPATH_CAMERA_SWITCH);
                    cameraFw.write("2");
                    cameraFw.close();
                    Thread.sleep(350);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                P_CamaraShoot camaraShoot = new P_CamaraShoot();
                camaraShoot.setShootCmd(5);
                camaraShoot.setShootInterval(3);
//                camaraShoot.setChroma(80);
                camaraShoot.setBrightness(100);
//                camaraShoot.setContrast(95);
                camaraShoot.setResolution(1);
//                camaraShoot.setSaturation(85);
                camaraShoot.setSaveFlag(0);
                camaraShoot.setCameraId(12);
                camaraShoot.setImageVideoQuality(9);
                mService.sendToJT905(camaraShoot);
                break;
            case R.id.video:
                try {
                    FileWriter cameraFw = new FileWriter(
                            FILEPATH_CAMERA_SWITCH);
                    cameraFw.write("2");
                    cameraFw.close();
                    Thread.sleep(350);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                P_CamaraShoot camaraShoot1 = new P_CamaraShoot();
                camaraShoot1.setShootCmd(0xFFFF);
                camaraShoot1.setShootInterval(50);
                camaraShoot1.setChroma(80);
                camaraShoot1.setBrightness(100);
                camaraShoot1.setContrast(95);
                camaraShoot1.setResolution(1);
                camaraShoot1.setSaturation(85);
                camaraShoot1.setSaveFlag(0);
                camaraShoot1.setCameraId(12);
                camaraShoot1.setImageVideoQuality(1);
                mService.sendToJT905(camaraShoot1);
                break;
            case R.id.play_tts:
                ttsUtil.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                ttsUtil.speak("Android语音合成与语音识别\n" +
                        "这里调用科大讯飞语音的API,语音云开放平台介绍：http://open.voicecloud.cn/\n" +
                        "调用科大讯飞语音的API,需要加添库文件Msc.jar,添加libmsc.so文件,还需添加权限,具体步骤可参看SDK里的文档\n" +
                        "参看开发的文档写了一个简单的语音合成和识别demo,图示如下");
                break;
            case R.id.play_music:
                List<String> paths = new ArrayList<>();
                paths.add(SDCardUtils.getSDCardPaths(false)+ File.separator
                        +"Music"+File.separator+"原来你什么都不要 - 张惠妹.mp3");
                playerManager.setVoice(paths);
                playerManager.player();
                break;
            case R.id.stop_music:
                playerManager.stop();
                break;
            case R.id.stop_tts:
                ttsUtil.stopSpeak();
                break;
            case R.id.reboot:
                P_TerminalControl terminalControl = new P_TerminalControl(P_TerminalControl.Cmd_Reset,null);
                mService.sendToJT905(terminalControl);
                break;
            case R.id.restoreSettings:
                P_TerminalControl terminalControl1= new P_TerminalControl(P_TerminalControl.Cmd_RestoreSettings,null);
                mService.sendToJT905(terminalControl1);
                break;
            case R.id.shutdown:
                P_TerminalControl terminalControl2= new P_TerminalControl(P_TerminalControl.Cmd_ShutDown,null);
                mService.sendToJT905(terminalControl2);
                break;
            case R.id.closeSocket:
                P_TerminalControl terminalControl3= new P_TerminalControl(P_TerminalControl.Cmd_CloseDataCommunication,null);
                mService.sendToJT905(terminalControl3);
                break;
            case R.id.closeAllCommunication:
                P_TerminalControl terminalControl4= new P_TerminalControl(P_TerminalControl.Cmd_CloseAllCommunication,null);
                mService.sendToJT905(terminalControl4);
                break;
            case R.id.call:
                P_Callback callback = new P_Callback(0,"15876999260");
                mService.sendToJT905(callback);
                break;
        }
    }
    String FILEPATH_CAMERA_SWITCH = "/sys/adv7180_camera_switch/camera_switch/";
}
