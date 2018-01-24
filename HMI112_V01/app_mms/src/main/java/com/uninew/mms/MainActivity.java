package com.uninew.mms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.uninew.mms.interactive.MMSBroadCastTool;
import com.uninew.mms.util.LogTool;
import com.uninew.mms.util.TimeTool;
import com.uninew.net.JT905.comm.client.ClientSendManage;
import com.uninew.net.JT905.comm.client.IClientSendManage;
import com.uninew.net.Taximeter.bean.P_TaxiFreight;
import com.uninew.net.Taximeter.bean.P_TaxiOperationDataReport;
import com.uninew.net.Taximeter.common.DefineID;
import com.uninew.net.Taximeter.common.ProtocolTool;
import com.uninew.net.Taximeter.protocol.McuProtocolPacket;


public class MainActivity extends Activity implements OnClickListener
        , OnCheckedChangeListener {
    private static final String TAG = "MainActivity";
    private McuService mService;
    private Context mContext;
    private Button btn_openSocket, btn_closeSocket, btn_send,
            btn_mcuupdate, btn_osupdate, txt_LED_NO, txt_LED_sendmsg,
            txt_LED_OFF, btn_close, btn_version;
    private EditText et_sleepTime, edi_leddate;
    private Button btn_setSleepTime, led_quryState, led_reset, led_operateState, led_starState, led_baudrate,
            led_bid, led_nightMode, led_nightdata, led_update;
    private MyBroadCast mMyBroadcastReceiver;
    //页面选择
    private RadioGroup radiogroup;
    //页面
    private LinearLayout ll_passenger, ll_roadsigns, ll_mcu, ll_led;

    private IClientSendManage mclientSendManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mclientSendManage = new ClientSendManage(this);
        findViews();
        setListeners();
        startService();
        registerBroadCast();
    }

    Handler myhand = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }
    };

    /**
     * 注册广播
     */
    private void registerBroadCast() {
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction("com.uninew.mcutest.date");
        mMyBroadcastReceiver = new MyBroadCast();
        this.registerReceiver(mMyBroadcastReceiver, iFilter);
    }

    /**
     * 开启服务
     */
    Intent serviceintent;

    private void startService() {
        // TODO Auto-generated method stub
        serviceintent = new Intent("com.uninew.mms.McuService");
        startService(serviceintent);
        bindService(serviceintent, conn, Context.BIND_AUTO_CREATE);
    }

    private void stopService() {
        if (serviceintent != null) {
            stopService(serviceintent);
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((McuService.MyBinder) service).getService();
            //mHandler.sendEmptyMessageAtTime(3, 10);
            LogTool.logI(TAG, "Binder MainService Success!!! mService=" + mService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogTool.logI(TAG, "MainService onServiceDisconnected!!!");
        }
    };


    private void findViews() {
        // TODO Auto-generated method stub
        btn_openSocket = (Button) findViewById(R.id.btn_openSocket);
        btn_closeSocket = (Button) findViewById(R.id.btn_closeSocket);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_openSocket.setOnClickListener(this);
        btn_closeSocket.setOnClickListener(this);
        btn_send.setOnClickListener(this);

        btn_version = (Button) findViewById(R.id.btn_version);
        btn_version.setOnClickListener(this);
        btn_mcuupdate = (Button) findViewById(R.id.btn_mcuupdate);
        btn_mcuupdate.setOnClickListener(this);
        btn_osupdate = (Button) findViewById(R.id.btn_osupdate);
        btn_osupdate.setOnClickListener(this);
        // textView = (TextView) findViewById(R.id.textView1);

        txt_LED_NO = (Button) findViewById(R.id.txt_LED_NO);
        txt_LED_OFF = (Button) findViewById(R.id.txt_LED_OFF);
        txt_LED_sendmsg = (Button) findViewById(R.id.txt_LED_sendmsg);

        txt_LED_NO.setOnClickListener(this);
        txt_LED_OFF.setOnClickListener(this);
        txt_LED_sendmsg.setOnClickListener(this);

        edi_leddate = (EditText) findViewById(R.id.edi_leddate);


        btn_close = (Button) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);

        et_sleepTime = (EditText) findViewById(R.id.et_sleepTime);
        btn_setSleepTime = (Button) findViewById(R.id.btn_setSleepTime);
        btn_setSleepTime.setOnClickListener(this);

        //页面控制
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);

        radiogroup.setOnCheckedChangeListener(this);

        //页面
        ll_mcu = (LinearLayout) findViewById(R.id.ll_mcu);
        ll_passenger = (LinearLayout) findViewById(R.id.ll_passenger);
        ll_roadsigns = (LinearLayout) findViewById(R.id.ll_roadsigns);
        ll_led = (LinearLayout) findViewById(R.id.ll_led);
        mFormat = false;
        mInvalid = false;
        mLastText = "";
        //edi_mcudate.addTextChangedListener(mEdiTextLisenter);
    }

    private static byte[] mEdibyte;

    private boolean mFormat;

    private boolean mInvalid;

    private int mSelection;

    private String mLastText;

    //EdiText改变监听
    private TextWatcher mEdiTextLisenter = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start,
                                      int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before,
                                  int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            mEdibyte = editable.toString().getBytes();
        }


    };


    private void setListeners() {
        // TODO Auto-generated method stub
    }

    //清除页面
    private void cleanLinlayout() {
        ll_mcu.setVisibility(View.GONE);
        ll_passenger.setVisibility(View.GONE);
        ll_roadsigns.setVisibility(View.GONE);
        ll_led.setVisibility(View.GONE);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int arg1) {
        switch (group.getCheckedRadioButtonId()) {
            case R.id.page_passenger://mcu调试
                cleanLinlayout();
                ll_mcu.setVisibility(View.VISIBLE);
                break;
            case R.id.page_mcu://计价器
                cleanLinlayout();
                ll_roadsigns.setVisibility(View.VISIBLE);
                break;
            case R.id.page_roadsigns://
                cleanLinlayout();
                ll_passenger.setVisibility(View.VISIBLE);
                break;
            case R.id.page_led://
                cleanLinlayout();
                ll_led.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_openSocket:
                //			pm.openSocket();
                //startService();
                break;
            case R.id.btn_closeSocket:
                //			pm.closeSocket();
                // stopService();
                android.os.Process.killProcess(Process.myPid());
                break;
            case R.id.btn_send:
                stopService();
                break;
            case R.id.btn_version:
                LogTool.logE("yzb", "btn_version");
                new MMSBroadCastTool(this).sendMCUVersionRequest();
                break;
            case R.id.btn_mcuupdate:
                // pm.sendMcuUpdate();
                LogTool.logE("meij", "btn_mcuupdate");
                break;
            case R.id.btn_osupdate:
                new MMSBroadCastTool(this).sendOSUpdateNotify();
                break;
            case R.id.btn_setSleepTime:
                // 设置MCU休眠时间
                String sTime = et_sleepTime.getText().toString();
                if (sTime == null || "".equals(sTime)) {
                    Toast.makeText(this, "请先设置休眠时间！", Toast.LENGTH_SHORT).show();
                    break;
                }
                int time = Integer.parseInt(sTime);
                mclientSendManage.setAccOffTime(time);
                //  pm.setSleepTime(time);
                break;
            case R.id.btn_armstate://arm同步

                mclientSendManage.ARMstateSynchro((byte) 0x01);
                break;
            case R.id.btn_mcuARMinter://ARM唤醒频率
                String numbery = et_sleepTime.getText().toString();
                if (!TextUtils.isEmpty(numbery)) {
                    int i = Integer.parseInt(numbery);
                    mclientSendManage.setWakeupFrequency(i);
                }
                break;
            case R.id.btn_mcuhigte://亮度调节
                String number = et_sleepTime.getText().toString();
                if (!TextUtils.isEmpty(number)) {
                    int i = Integer.parseInt(number);
                    mclientSendManage.screenBrightness((byte) i);
                }
                break;
            case R.id.btn_mcupowerAmp://功放开关
                String number2 = et_sleepTime.getText().toString();
                if (!TextUtils.isEmpty(number2)) {
                    int i = Integer.parseInt(number2);
                    mclientSendManage.powerAmplifier((byte) i);
                }
                break;
            case R.id.btn_mcuIOinGao://IO输出高
                String ioIndex = et_sleepTime.getText().toString();
                if (!TextUtils.isEmpty(ioIndex)) {
                    int i = Integer.parseInt(ioIndex);
                    mclientSendManage.setIOState((byte) i, (byte) 1);
                }
                break;
            case R.id.btn_mcuIOinDi://IO输出底
                String ioIndex2 = et_sleepTime.getText().toString();
                if (!TextUtils.isEmpty(ioIndex2)) {
                    int i = Integer.parseInt(ioIndex2);
                    mclientSendManage.setIOState((byte) i, (byte) 0);
                }
                break;
            case R.id.btn_mcuPulseSignal://脉冲测速
                mclientSendManage.queryPulseSignal();
                break;
            case R.id.btn_mcuPower://电源信息
                // mclientSendManage.queryElectricity(0x00);
                String numberp = et_sleepTime.getText().toString();
                if (!TextUtils.isEmpty(numberp)) {
                    int i = Integer.parseInt(numberp);
                    mclientSendManage.queryElectricity((byte) i);
                }
                break;
            case R.id.btn_srceenOpen://屏幕开
                mclientSendManage.screenBacklight((byte) 0x01);
                break;
            case R.id.btn_seriver://串口
                byte[] bytes = {0, 1, 2};
                try {
                    mclientSendManage.sendRS232((byte) 0x01, bytes);
                    Thread.sleep(10);
                    mclientSendManage.sendRS232((byte) 0x02, bytes);
                    Thread.sleep(10);
                    mclientSendManage.sendRS232((byte) 0x03, bytes);
                    Thread.sleep(10);
                    mclientSendManage.sendRS485((byte) 0x02, bytes);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_seriver2://串口2
                String number3 = et_sleepTime.getText().toString();
                byte[] bytes2 = null;
                if (!TextUtils.isEmpty(number3)) {
                    int i = Integer.parseInt(number3);
                    bytes2 = new byte[i];
                } else {
                    bytes2 = new byte[40];
                }
                for (int j = 0; j < bytes2.length; j++) {
                    if (j < 10) {
                        bytes2[j] = 0x11;
                    } else if (j >= bytes2.length - 10) {
                        bytes2[j] = 0x22;
                    } else {
                        bytes2[j] = 0x00;
                    }
                }
                mclientSendManage.sendRS232((byte) 0x02, bytes2);
                break;
            case R.id.btn_seriver1://串口1
                String number1 = et_sleepTime.getText().toString();
                byte[] bytes1 = null;
                if (!TextUtils.isEmpty(number1)) {
                    int i = Integer.parseInt(number1);
                    bytes1 = new byte[i];
                } else {
                    bytes1 = new byte[40];
                }
                for (int j = 0; j < bytes1.length; j++) {
                    if (j < 10) {
                        bytes1[j] = 0x11;
                    } else if (j >= bytes1.length - 10) {
                        bytes1[j] = 0x22;
                    } else {
                        bytes1[j] = 0x00;
                    }
                }
                mclientSendManage.sendRS232((byte) 0x01, bytes1);
                break;
            case R.id.btn_seriver3://串口3
                String number3x = et_sleepTime.getText().toString();
                byte[] bytes3 = null;
                if (!TextUtils.isEmpty(number3x)) {
                    int i = Integer.parseInt(number3x);
                    bytes3 = new byte[i];
                } else {
                    bytes3 = new byte[40];
                }
                for (int j = 0; j < bytes3.length; j++) {
                    if (j < 10) {
                        bytes3[j] = 0x11;
                    } else if (j >= bytes3.length - 10) {
                        bytes3[j] = 0x22;
                    } else {
                        bytes3[j] = 0x00;
                    }
                }
                mclientSendManage.sendRS232((byte) 0x03, bytes3);
                break;
            case R.id.btn_mcu485://串口 485
                String number485 = et_sleepTime.getText().toString();
                byte[] bytes485 = null;
                if (!TextUtils.isEmpty(number485)) {
                    int i = Integer.parseInt(number485);
                    bytes485 = new byte[i];
                } else {
                    bytes485 = new byte[40];
                }
                for (int j = 0; j < bytes485.length; j++) {
                    if (j < 10) {
                        bytes485[j] = 0x11;
                    } else if (j >= bytes485.length - 10) {
                        bytes485[j] = 0x22;
                    } else {
                        bytes485[j] = 0x00;
                    }
                }
                mclientSendManage.sendRS485((byte) 0x02, bytes485);
                break;
            case R.id.btn_srceenClose://屏幕关
                mclientSendManage.screenBacklight((byte) 0x00);
                break;
            case R.id.btn_taxi_kongche://
                byte[] startime = ProtocolTool.str2Bcd("20171208104455");
                McuProtocolPacket mcuProtocolPacket = new McuProtocolPacket(DefineID.TAXI_SINGLEOPERATE_STAR, startime, 0x02);
                mService.receiveRS232((byte) 0x02, mcuProtocolPacket.getProtocolPacketBytes());
                break;
            case R.id.btn_taxi_zhongche://
                P_TaxiOperationDataReport mP_TaxiOperationDataReport = new P_TaxiOperationDataReport();
                mP_TaxiOperationDataReport.setBusinessLicense("123456789");
                mP_TaxiOperationDataReport.setDriverCertificate("123123123456456456");
                mP_TaxiOperationDataReport.setCarNumber("粤A520");
                mP_TaxiOperationDataReport.setUpCarTime("1710201500");
                mP_TaxiOperationDataReport.setDownCarTime("0800");
                mP_TaxiOperationDataReport.setMileage(100.2f);
                mP_TaxiOperationDataReport.setEmptyMileage(50.5f);
                mP_TaxiOperationDataReport.setSurcharge(30.5f);
                mP_TaxiOperationDataReport.setTransactionIncome(20.3f);
                mP_TaxiOperationDataReport.setWaitTimingTime("1022");
                mP_TaxiOperationDataReport.setTrips(11);
                mP_TaxiOperationDataReport.setTransactionType(0x00);
                mP_TaxiOperationDataReport.setCarData(new byte[2]);
                McuProtocolPacket mcuProtocolPacket2 = new McuProtocolPacket(DefineID.TAXI_SINGLEOPERATE_END,
                        mP_TaxiOperationDataReport.getDataBytes(), 0x02);
                mService.receiveRS232((byte) 0x02, mcuProtocolPacket2.getProtocolPacketBytes());
                break;
            case R.id.btn_taxi_quryState://计价器状态查询 yyyyMMddHHmmss
                mclientSendManage.quryTaxiState(TimeTool.timestampTormat2(TimeTool.getCurrentTimestamp()));
                break;
            case R.id.btn_taxi_money://计价器运价参数查询
                mclientSendManage.quryTaxiFreight();
                break;
            case R.id.btn_taxi_hismoney://计价器历史运价参数查询
                mclientSendManage.quryTaxiHistory(0x3b);
                break;
            case R.id.btn_taxi_time://计价器时钟校验 yyyyMMddHHmmss
                mclientSendManage.ChekTaxiClock(TimeTool.timestampTormat2(TimeTool.getCurrentTimestamp()));
                break;
            case R.id.btn_taxi_feightSet://运价参数设置
                P_TaxiFreight m = new P_TaxiFreight(TimeTool.timestampTormat2(TimeTool.getCurrentTimestamp()), 5f, 10f);
                mService.mProtocolManage.quryFreightSet(m);
                break;
            case R.id.led_baudrate://LED波特率设置
                String lednumber1 = edi_leddate.getText().toString();
                int i = 0;
                if (!TextUtils.isEmpty(lednumber1)) {
                    i = Integer.parseInt(lednumber1);
                }
                mService.mProtocolManage.setLedBaudRate(i);
                break;
            case R.id.led_bid://防伪密标
                String lednumber2 = edi_leddate.getText().toString();
                int show = 0;
                if (!TextUtils.isEmpty(lednumber2)) {
                    show = Integer.parseInt(lednumber2);
                }
                if (show == 0) {
                    mService.mProtocolManage.ShowBID(new byte[]{0x00, 0x01, 0x02});
                } else {
                    mService.mProtocolManage.DissBID();
                }
                break;
            case R.id.led_nightdata://夜间数据
                mService.mProtocolManage.setNightModeData("0900", "2200");
                break;
            case R.id.led_nightMode://夜间模式
                String lednumber3 = edi_leddate.getText().toString();
                int night = 0;
                if (!TextUtils.isEmpty(lednumber3)) {
                    night = Integer.parseInt(lednumber3);
                }
                mService.mProtocolManage.setNightMode(night);
                break;
            case R.id.led_operateState://运营状态
                String lednumber4 = edi_leddate.getText().toString();
                int operat = 0;
                if (!TextUtils.isEmpty(lednumber4)) {
                    operat = Integer.parseInt(lednumber4);
                }
                mService.mProtocolManage.setLedOperateOrStarState(DefineID.LED_OPERATE_STATE, operat);
                break;
            case R.id.led_quryState://查询状态
                mService.mProtocolManage.quryLedState();
                break;
            case R.id.led_reset://复位
                mService.mProtocolManage.resetLed();
                break;
            case R.id.led_starState://星级状态
                String lednumber5 = edi_leddate.getText().toString();
                int operat2 = 0;
                if (!TextUtils.isEmpty(lednumber5)) {
                    operat2 = Integer.parseInt(lednumber5);
                }
                mService.mProtocolManage.setLedOperateOrStarState(DefineID.LED_STAR_STATE, operat2);
                break;
            case R.id.led_update://固件升级
                break;
            case R.id.led_CJState://设置LED星级状态和运营状态（川基）
                String lednumberCJ = edi_leddate.getText().toString();
                int operatCJ = 0;
                if (!TextUtils.isEmpty(lednumberCJ)) {
                    operatCJ = Integer.parseInt(lednumberCJ);
                }
                mService.setCJLedState(operatCJ, operatCJ);
                break;
            case R.id.led_CJalarm://报警（川基）
                String lednumberCJ2 = edi_leddate.getText().toString();
                int alarmCJ = 0;
                if (!TextUtils.isEmpty(lednumberCJ2)) {
                    alarmCJ = Integer.parseInt(lednumberCJ2);
                }
                mService.setAlarmAtate(alarmCJ);
                break;
            case R.id.led_CJalarmData://报警内容
                mService.setAlarmDate(edi_leddate.getText().toString());
                break;
            default:
                break;
        }
    }


    //MCU数据显示
    private void getMcuDate(byte[] date) {
        String msg = HextoString(date);
        // txt_mcudate.setText(msg);
    }

    //16进制字符串
    private String HextoString(byte[] msgs) {
        if (msgs == null) {
            return "-101";
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0, length = msgs.length; i < length; i++) {
            int v = msgs[i] & 0xFF;
            String hv = null;
            if (v <= 0x0F) {
                hv = "0" + Integer.toHexString(v);
            } else {
                hv = Integer.toHexString(v);
            }
            buffer.append(hv + " ");
        }
        return buffer.toString();
    }

    /**
     * 广播接收器
     *
     * @author jiami
     */
    class MyBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
            }

        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (conn != null) {
            unbindService(conn);
        }
        unregisterReceiver(mMyBroadcastReceiver);
    }


    public void keyReceive(int keyValue) {
        // TODO Auto-generated method stub
        LogTool.logD("yzb",
                "keyReceive,keyValue=0x" + Integer.toHexString(keyValue & 0xff));
    }

    ///////////////////////////////////MCU////////////////////////////////////////////////////////////////
    /**
     * MCU版本升级
     */
    private static final int WHAT_MCU_UPDATE = 7;
    String str;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            // textView.setText(str);
            switch (msg.what) {
                case WHAT_MCU_UPDATE:
                    int arg = msg.arg1;
                    if (arg == 0) {
                        Toast.makeText(getApplicationContext(), "mcu升级失败！", Toast.LENGTH_SHORT)
                                .show();
                    } else if (arg == 1) {
                        Toast.makeText(getApplicationContext(), "mcu升级成功！", Toast.LENGTH_SHORT)
                                .show();
                    } else if (arg == 2) {
                        Toast.makeText(getApplicationContext(), "sd卡或者mcu升级文件不存在！",
                                Toast.LENGTH_SHORT).show();
                    } else if (arg == 3) {
                        Toast.makeText(getApplicationContext(), "mcu版本太低，无法升级！", Toast.LENGTH_SHORT)
                                .show();
                    }
                    break;
                case 3:
                    break;

                default:
                    break;
            }
        }

    };


    public void McuVersion(String version) {
        // TODO Auto-generated method stub

    }

    public void mcuUpdataResponse(byte[] datas) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        LogTool.logBytes("meij__", datas);
        // TODO Auto-generated method stub
        Log.i("meij", "mcurespons--1");

        //        mUpdata.setReceivedTime(System.currentTimeMillis());
        //        switch (datas[0]) {
        //            case 0x01:
        //                if (datas[1] == 0x01) {
        //                    mUpdata.setMcuUpdateState(McuUpdataState.updataInit);
        //                    mUpdata.createMcuUpdateThread();
        //                } else {
        //                    if (datas[1] == 0x00) {
        //                        mUpdata.setMcuUpdateState(McuUpdataState.updataEnd);
        //                    }
        //                }
        //                break;
        //            case 0x02:
        //                mUpdata.addMcuUpdataQuene(datas);
        //                break;
        //            default:
        //                break;
        //        }

    }

    public void onSleepTime(int sleepTime) {
        // TODO Auto-generated method stub
        str = "onSleepTime,sleepTime=" + sleepTime;
        mHandler.sendEmptyMessage(0);
    }

    public void onAccState(int state) {
        // TODO Auto-generated method stub

    }

}
