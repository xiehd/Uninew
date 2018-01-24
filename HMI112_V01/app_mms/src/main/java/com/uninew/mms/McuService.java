package com.uninew.mms;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.uninew.mms.ChuanJiLed.protocol.CJLedProtocolPacket;
import com.uninew.mms.aidl.interfaces.IMcuReceiveListener;
import com.uninew.mms.aidl.interfaces.IMcuSend;
import com.uninew.mms.aidl.interfaces.Initialize;
import com.uninew.mms.interactive.MMSBroadCastTool;
import com.uninew.mms.interfaces.ILedListener;
import com.uninew.mms.interfaces.ITaxiListener;
import com.uninew.mms.protocol.ProtocolManage;
import com.uninew.mms.util.LogTool;
import com.uninew.net.Alarm.AlarmManager;
import com.uninew.net.JT905.comm.client.ClientSendManage;
import com.uninew.net.JT905.comm.client.IClientSendManage;
import com.uninew.net.JT905.comm.server.IServerReceiveListener;
import com.uninew.net.JT905.comm.server.IServerReceiveManage;
import com.uninew.net.JT905.comm.server.IServerSendManage;
import com.uninew.net.JT905.comm.server.ServerReceiveManage;
import com.uninew.net.JT905.comm.server.ServerSendManage;
import com.uninew.net.Taximeter.bean.L_LedQuery;
import com.uninew.net.Taximeter.bean.P_TaxiCloseBoot;
import com.uninew.net.Taximeter.bean.P_TaxiFreight;
import com.uninew.net.Taximeter.bean.P_TaxiOpenBoot;
import com.uninew.net.Taximeter.bean.P_TaxiOperationDataReport;
import com.uninew.net.Taximeter.bean.P_TaxiQuery;
import com.uninew.net.Taximeter.bean.T_HeartBeat;
import com.uninew.net.Taximeter.common.DefineID;


public class McuService extends Service implements IMcuReceiveListener, ITaxiListener, ILedListener {
    private static final String TAG = "McuService";
    private static final boolean D = true;
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private McuService mContext;
    private long mCurrentTime, mLastTime;
    private MMSBroadCastTool mMMSBroadCastTool;

    public IServerSendManage mServerSendManage;
    public IServerReceiveManage mServerReceiveManage;

    public IClientSendManage clientSendManage;

    public IMcuSend mIMcuSend;
    public ProtocolManage mProtocolManage;
    private SystemInfoManage mSystemInfoManage;//系统信息管理
    public AlarmManager mAlarmManager;//报警

    @Override
    public IBinder onBind(Intent arg0) {
        return new MyBinder();
    }


    public class MyBinder extends Binder {
        public McuService getService() {
            if (mContext != null) {
                return mContext;
            }
            return McuService.this;
        }
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        Log.d(TAG, "--------------onCreate-----------------");
        super.onCreate();
        mContext = this;

        //初始化其他参数
        init();
        initOthers();
    }


    private void initOthers() {
        //MMS 初始化
        Initialize.setMcuReceiveListener(this);
        mIMcuSend = Initialize.getMcuManage();
        mIMcuSend.screenBrightness((byte) 80);

        mSystemInfoManage = new SystemInfoManage(mContext);
        mAlarmManager = new AlarmManager(mContext);

        //沾包协议测试
        mProtocolManage = new ProtocolManage(mContext);
        //
        //        byte[] test1 = {0x55,(byte)0xAA, 0x00, 0x0a, 0x00, 0x01, 0x02, 0x03};
        //        byte[] test_1 = {0x04,0x05,0x06,0x07,0x08,0x09,(byte)0xbb,0x55,(byte)0xAA};
        //        byte[] test2 = {0x55,(byte)0xAA, 0x00, 0x03, 0x00, 0x01, 0x02,(byte)0xbb,0x55,(byte)0xAA};
        //
        //        try {
        //            Thread.sleep(100);
        //            mProtocolManage.onReceiveTcpDatas(0x00,test1);
        //            mProtocolManage.onReceiveTcpDatas(0x00,test_1);
        //            //Thread.sleep(50);
        //            mProtocolManage.onReceiveTcpDatas(0x00,test2);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }


    }


    private void init() {
        // TODO Auto-generated method stub
        //        registerBroadCast();
        if (mHandlerThread == null) {
            mHandlerThread = new HandlerThread("McuService");
            mHandlerThread.start();
            mHandler = new MyHandler(mHandlerThread.getLooper());
        }
        mMMSBroadCastTool = new MMSBroadCastTool(this);

        //作为服务端发送
        mServerReceiveManage = new ServerReceiveManage(this);
        mServerSendManage = new ServerSendManage(this);
        mServerReceiveManage.SetAction(true);
        mServerReceiveManage.registerMmsReceiveListener(mIMmsReceiveListener);
        mServerReceiveManage.registerTaxiReceiveListener(mITaxiReceiveListener);
        //作为客户端发送
        clientSendManage = new ClientSendManage(this);

    }

    @Override
    public boolean systemStateNotify(byte state) {//系统状态通知
        if (D)
            Log.d(TAG, "-----mms listener--systemStateNotify--" + state);
        mSystemInfoManage.SystemState(state);
        return true;
    }

    @Override
    public void mcuVersionNotify(String version) {//MCU版本号
        if (D)
            Log.d(TAG, "-----mms listener--mcuVersionNotify--" + version);
        mServerSendManage.mcuVersionNotify(version);

    }

    @Override
    public boolean electricity(byte type, byte electricity) {//电池电量信息 type：0x01-蓄电池，0x02-自带电池电量,0x03:脉冲信号
        if (D)
            Log.d(TAG, "-----mms listener--electricity--" + type + ",value:" + electricity);
        mServerSendManage.electricity(type, electricity);
        return true;
    }

    @Override
    public void handleMcuKey(byte key, byte action) {//按键
        Log.i(TAG, "收到按键信息:");
        keyReceive(key, action);
    }

    @Override
    public boolean receiveCanDatas(byte[] canDatas) {//CAN数据透传上报
        if (D)
            Log.d(TAG, "-----mms listener--receiveCanDatas--");
        LogTool.logBytes(TAG, "---receiveCanDatas---", canDatas);
        return true;
    }

    @Override
    public boolean receiveRS232(byte id, byte[] rs232Datas) {//RS232数据透传,id(0x01:RS232_1,0x02:RS232_2,0x03:RS232_3)
        if (D)
            Log.d(TAG, "-----mms listener--receiveRS232--" + id + ",length:" + rs232Datas.length);
        LogTool.logBytes(TAG, "---receiveRS232---", rs232Datas);
        mProtocolManage.onReceiveTcpDatas(id, rs232Datas);
        return true;
    }

    @Override
    public boolean receiveRS485(byte id, byte[] rs485Datas) {//RS485数据透传
        if (D)
            Log.d(TAG, "-----mms listener--receiveRS485--" + id + ",length:" + rs485Datas.length);
        LogTool.logBytes(TAG, "---receiveRS485---", rs485Datas);
        return true;
    }

    @Override
    public boolean receiveBaudRate(byte id, byte baudRate) {//波特率设置信息
        // id(0x00:RS485,0x01:RS232_1,0x02:RS232_2,0x03:RS232_3)
        if (D)
            Log.d(TAG, "-----mms listener--receiveBaudRate--" + id);
        return true;
    }

    @Override
    public boolean receiveIOState(byte id, byte state) {//IO输出信号
        if (D)
            Log.d(TAG, "-----mms listener--receiveIOState--" + id + ",state:" + state);
        return true;
    }

    @Override
    public boolean receivePulseSignal(int speed) {//接收脉冲测速信号(已忽略)
        if (D)
            Log.d(TAG, "-----mms listener--receivePulseSignal--" + speed);
        return true;
    }


    //-----------------------------------与外部应用通讯-------------------------------------------------------------

    private IServerReceiveListener.IMmsReceiveListener mIMmsReceiveListener = new IServerReceiveListener.IMmsReceiveListener() {

        @Override
        public void ARMstateSynchro(byte state) {
            mIMcuSend.ARMstateSynchro(state);
        }

        @Override
        public void screenBacklight(byte contrl) {
            mIMcuSend.screenBacklight(contrl);
        }

        @Override
        public void screenBrightness(byte b) {
            mIMcuSend.screenBrightness(b);
        }

        @Override
        public void powerAmplifier(byte b) {
            mIMcuSend.powerAmplifier(b);
        }

        @Override
        public boolean sendCanDatas(byte[] bytes) {
            mIMcuSend.sendCanDatas(bytes);
            return true;
        }

        @Override
        public boolean sendRS232(byte id, byte[] bytes) {
            mIMcuSend.sendRS232(id, bytes);
            return true;
        }

        @Override
        public boolean sendRS485(byte id, byte[] bytes) {
            mIMcuSend.sendRS485(id, bytes);
            return true;
        }

        @Override
        public boolean setBaudRate(byte id, byte value) {
            mIMcuSend.setBaudRate(id, value);
            return true;
        }

        @Override
        public boolean setIOState(byte id, byte value) {
            mIMcuSend.setIOState(id, value);
            return true;
        }

        @Override
        public boolean setAccOffTime(int time) {
            mIMcuSend.setAccOffTime(time);
            return true;
        }

        @Override
        public boolean setWakeupFrequency(int intervalTime) {
            mIMcuSend.setWakeupFrequency(intervalTime);
            return true;
        }

        @Override
        public void queryElectricity(byte id) {
            mIMcuSend.queryElectricity(id);
        }

        @Override
        public void queryIOState(byte id) {
            mIMcuSend.queryIOState(id);
        }

        @Override
        public void queryPulseSignal() {
            mIMcuSend.queryPulseSignal();
        }
    };
    //------------------------------------------------------计价器------------------------------------------------------------

    private IServerReceiveListener.ITaxiReceiveListener mITaxiReceiveListener = new IServerReceiveListener.ITaxiReceiveListener() {
        @Override
        public void quryTaxiState(String time) {
            mProtocolManage.quryTaxiState(time);
        }

        @Override
        public void quryTaxiFreight() {
            mProtocolManage.quryFreight();
        }

        @Override
        public void quryTaxiHistory(int car) {
            mProtocolManage.operateQuery(car);

        }

        @Override
        public void ChekTaxiClock(String time) {
            mProtocolManage.quryTaxiClock(time);

        }
    };
    /**
     * 发送键值
     */
    private static final int WHAT_SENDKEY = 0;
    /**
     * ACC状态
     */
    private static final int WHAT_ACCSTATE = 1;
    /**
     * 发送客流量
     */
    private static final int WHAT_MCUPARMRESPONSE = 2;
    /**
     * 门状态查询
     */
    private static final int WHAT_DOORSTATEREQUEST = 3;
    /**
     * 客流量查询
     */
    private static final int WHAT_PASSREQUEST = 4;
    /**
     * 路牌下发
     */
    private static final int WHAT_ROADSIGNS = 5;
    /**
     * MCU版本查询
     */
    private static final int WHAT_MCU_VERSION_REQUEST = 6;
    /**
     * MCU版本升级
     */
    private static final int WHAT_MCU_UPDATE = 7;
    /**
     * 休眠时间设置
     */
    private static final int WHAT_SLEEPTIME_SET = 8;


    class MyHandler extends Handler {
        public MyHandler(Looper lp) {
            super(lp);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO 该方法运行于子线程，可执行耗时操作
            switch (msg.what) {
                case WHAT_SENDKEY:// 键值

                    Log.i(TAG, "key=" + msg.arg2 + ",----state=" + msg.arg1);
                    if ((msg.arg1) == 0x00) {
                        //抬起
                        mCurrentTime = System.currentTimeMillis();
                        if (mCurrentTime - mLastTime < 500) {
                            mLastTime = mCurrentTime;
                            Log.e(TAG, "按键太快了哦！！！！");
                            break;
                        }
                        mLastTime = mCurrentTime;
                        //按键类型
                        switch (msg.arg2) {
                            case 0x03://Home
                                Intent mIntent = new Intent();
                                mIntent.setAction(Intent.ACTION_MAIN);
                                mIntent.addCategory(Intent.CATEGORY_HOME);
                                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(mIntent);
                                break;
                            case 0x30://导航
                                //高德地图车机版本 使用该包名/
                                String pkgName = "com.autonavi.amapauto";
                                Intent launchIntent = new Intent();
                                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                launchIntent.setComponent(new ComponentName(pkgName,
                                        "com.autonavi.auto.remote.fill.UsbFillActivity"));
                                startActivity(launchIntent);
                                break;
                            case 0x31://录像
                                break;
                            case 0x32://电召
                                break;
                            case 0x33://满意（评价）
                                Intent intentKey = new Intent("com.uninew.evaluat.key");
                                intentKey.putExtra("key", 0x01);
                                mContext.sendBroadcast(intentKey);
                                break;
                            case 0x34://一般（评价）
                                Intent intentKey2 = new Intent("com.uninew.evaluat.key");
                                intentKey2.putExtra("key", 0x02);
                                mContext.sendBroadcast(intentKey2);
                                break;
                            case 0x35://不满意（评价）
                                Intent intentKey3 = new Intent("com.uninew.evaluat.key");
                                intentKey3.putExtra("key", 0x03);
                                mContext.sendBroadcast(intentKey3);
                                break;
                        }

                    }
                    break;

                case WHAT_ACCSTATE://状态
                    if (msg.arg2 == 0x00) {//关
                        switch (msg.arg1) {
                            case 0x00://ACC
                                //mMMSBroadCastTool.sendAccStateNotify(0x00);
                                break;
                        }
                    } else if (msg.arg2 == 0x01) {//开
                        switch (msg.arg1) {
                            case 0x00://ACC
                                //mMMSBroadCastTool.sendAccStateNotify(0x01);
                                break;
                        }
                    }
                    break;
                case WHAT_MCUPARMRESPONSE://参数设置应答
                    if (msg.arg2 == 0x00) {//成功

                    } else if (msg.arg2 == 0x01) {//失败

                    }
                    break;
                case WHAT_MCU_VERSION_REQUEST:
                    break;
                case WHAT_MCU_UPDATE:
                    int arg = msg.arg1;
                    Log.i("meij", "mcu sheng ji:what:" + msg.what + "arg1:" + msg.arg1);
                    if (arg == 0) {
                        Toast.makeText(getApplicationContext(), "mcu升级失败！", Toast.LENGTH_SHORT).show();
                    } else if (arg == 1) {
                        Toast.makeText(getApplicationContext(), "mcu在升级，请勿断电和操作！", Toast.LENGTH_SHORT).show();
                        if (pd == null) {
                            progressDialogShow();
                        } else {
                            pd.show();
                        }
                    } else if (arg == 2) {
                        Toast.makeText(getApplicationContext(), "sd卡或者mcu升级文件不存在！", Toast.LENGTH_SHORT).show();
                    } else if (arg == 3) {
                        Toast.makeText(getApplicationContext(), "mcu版本太低，无法升级！", Toast.LENGTH_SHORT).show();
                    } else if (arg == 4) {
                        Toast.makeText(getApplicationContext(), "mcu升级成功", Toast.LENGTH_SHORT).show();
                        if (pd != null) {
                            pd.dismiss();
                        }
                    }
                    break;
                case WHAT_SLEEPTIME_SET:
                    //休眠时间设置
                    break;
                default:
                    break;
            }
        }

    }


    /**
     * progressDialog显示
     */
    protected ProgressDialog pd;

    protected void progressDialogShow() {

        if (pd == null) {
            pd = new ProgressDialog(getApplicationContext());
        }
        pd.setTitle("升级中...");
        pd.setCancelable(false);
        pd.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        pd.show();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d(TAG, "----onDestroy---MMS尝试重启---");

        if (mServerReceiveManage != null) {
            mServerReceiveManage.unRegisterMmsReceiveListener();
            mServerReceiveManage.unRegisterTaxiReceiveListener();
            mServerReceiveManage = null;
        }

        if (mHandlerThread != null) {
            mHandlerThread.quit();
            mHandlerThread = null;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        Log.d(TAG, "----onUnbind----");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        // TODO Auto-generated method stub
        Log.d(TAG, "----onRebind----");
        super.onRebind(intent);
    }

    /**
     * 按键
     *
     * @param keyValue 键值
     * @param keyState 按键状态
     */
    private void keyReceive(int keyValue, int keyState) {
        // TODO Auto-generated method stub
        Message msg = new Message();
        msg.what = WHAT_SENDKEY;
        msg.arg1 = keyState;
        msg.arg2 = keyValue;
        mHandler.sendMessage(msg);
    }

    private void startThisActivity(String action) {
        try {
            Intent intent = new Intent(action);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        } catch (Exception e) {
        }
    }

    //--------------------------------------------计价器监听--------------------------------------------------------------------------

    @Override
    public void GeneralResponse(int result) {

    }

    @Override
    public void quryTaxiStateResponse(P_TaxiQuery mP_TaxiQuery) {
        //mServerSendManage.quryTaxiStateResponse();
            if (mP_TaxiQuery.getTaxiState() == 0x00 || mP_TaxiQuery.getTaxiState() == 0x10){
                mAlarmManager.mDevicefailureJudge.sendfailure(0x0a,0x01);
            }else{
                mAlarmManager.mDevicefailureJudge.sendfailure(0x0a,0x00);
            }
    }

    @Override
    public void quryFreightResponse(P_TaxiFreight mP_Freight) {
        mServerSendManage.quryTaxiFreightResponse(mP_Freight);
    }

    @Override
    public void sendOperteStart(String time) {
        mServerSendManage.OperteStart(time);
        mProtocolManage.currencyResponse(DefineID.TAXI_SINGLEOPERATE_STAR, 0x90, null);
    }

    @Override
    public void sendSingleOperate(P_TaxiOperationDataReport mP_SingleOperate) {
        if (mP_SingleOperate.getId_type() == 0x00) {//当班数据
            mServerSendManage.OperteEnd(0x00, mP_SingleOperate);
//            Log.i("xhd", mP_SingleOperate.getMileage() + "," + mP_SingleOperate.getDownCarTime() + ","
//                    + mP_SingleOperate.getUpCarTime() + "," + mP_SingleOperate.getMileage() + "," +
//                    mP_SingleOperate.getTransactionIncome());
            mProtocolManage.currencyResponse(DefineID.TAXI_SINGLEOPERATE_END, 0x90, null);
        } else {//补传数据
            mServerSendManage.OperteEnd(0x01, mP_SingleOperate);
            mProtocolManage.currencyResponse(DefineID.TAXI_OPERATEDATA_SUPPL, 0x90, null);
        }
    }

    @Override
    public void sendTaxiOpen(int falg) {
        if (falg == 0x90) {
            mProtocolManage.openOrCloseResponse(0x00, new P_TaxiOpenBoot());
        }

    }

    @Override
    public void TaxiOpenOk(P_TaxiOpenBoot mP_TaxiOpenBoot) {

        mAlarmManager.mDevicefailureJudge.sendfailure(0x0a,0x01);
    }

    @Override
    public void sendTaxiClose(int falg) {
        if (falg == 0x90) {
            mProtocolManage.openOrCloseResponse(0x01, new P_TaxiOpenBoot());
        }
    }

    @Override
    public void TaxiCloseOk(P_TaxiCloseBoot mP_TaxiCloseBoot) {
        mProtocolManage.currencyResponse(DefineID.TAXI_CLOSE_OK, 0x90, null);
        mAlarmManager.mDevicefailureJudge.sendfailure(0x0a,0x00);
    }

    //-------心跳--------------
    @Override
    public void HeartBeat(T_HeartBeat mT_HeartBeat) {
        mServerSendManage.TaxiHeartState(mT_HeartBeat);
        mProtocolManage.heartResponse(mT_HeartBeat);
    }

    //****顶灯监听****
    @Override
    public void LedStateResponse(L_LedQuery mL_LedQuery) {

    }

    @Override
    public void LedGeneralResponse(int msgId, int result) {

        switch (msgId) {
            case DefineID.LED_RESET://顶灯复位
                break;
            case DefineID.LED_SET_BAUDRATE://顶灯波特率
                break;
            case DefineID.LED_FIRMWARE_STATE://顶灯固件升级
                break;
            case DefineID.LED_OPERATE_STATE://顶灯运营状态
                break;
            case DefineID.LED_STAR_STATE://顶灯星级状态
                break;
            case DefineID.LED_BID_SHOW://顶灯防伪密标显示
                break;
            case DefineID.LED_BID_DISS://顶灯防伪密标取消
                break;
            case DefineID.LED_SET_NIGHTMODE://夜间工作模式
                break;
            case DefineID.LED_NIGHTMODE_PARAMETER://夜间工作模式参数设置
                break;
        }
    }

    //******************川基LED协议******************************************
    public void setCJLedState(int operate, int start) {
//        if (operate != 0x01) {
//            CJLedState mCJLedState = new CJLedState(0x00, 0x05);
//            CJLedProtocolPacket mCJLedProtocolPacket = new CJLedProtocolPacket(0x49, "00000000", mCJLedState.getDataBytes());
//            mIMcuSend.sendRS232((byte) 0x01, mCJLedProtocolPacket.getDataBytes());
//        } else {
//            byte[] body = new byte[7];
//            body[0] = 0x00;
//            body[1] = 0x12;
//            body[2] = 0x02;
//            body[3] = 0x02;
//            body[4] = 0x01;
//            body[5] = 0x01;
//            body[6] = 0x01;
//            CJLedProtocolPacket mCJLedProtocolPacket = new CJLedProtocolPacket(0x41, "00000000", body);
//            mIMcuSend.sendRS232((byte) 0x01, mCJLedProtocolPacket.getDataBytes());
//        }
        byte[] test = {(byte) 0xAA, 0x55, 0x00, 0x1D, 0x00, 0x00, 0x00, 0x01, 0x00, 0x14, 0x44, (byte) 0xDC, 0x49, 0x00, 0x00, 0x00, 0x39,
                0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x31, 0x00, 0x05, 0x04, 0x29, (byte) 0x88};
        mIMcuSend.sendRS232((byte) 0x01, test);
    }

    /**
     * 报警
     *
     * @param type 0x00:触发报警  0x01:取消报警
     */
    public void setAlarmAtate(int type) {
        CJLedProtocolPacket mCJLedProtocolPacket = new CJLedProtocolPacket(0x50, "00000000", new byte[1]);
        if (type == 0x00) {
            mCJLedProtocolPacket.setMsgID((byte) 0x50);

        } else if (type == 0x01) {
            mCJLedProtocolPacket.setMsgID((byte) 0x4F);
        }
        mIMcuSend.sendRS232((byte) 0x01, mCJLedProtocolPacket.getDataBytes());
    }

    /**
     * 设置报警内容
     */
    public void setAlarmDate(String data) {

        if (data.equals("")) {
            byte[] body = {(byte) 0xAA, 0x55, 0x00, 0x30, 0x00, 0x00, 0x00, 0x01, 0x00, (byte) 0x93, 0x2C, (byte) 0xFB, 0x3A, 0x00, 0x00, 0x00, 0x39,
                    0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x31, 0x00, 0x01, (byte) 0xCB, 0x10, 0x05, 0x1B, 0x14, 0x01, 0x01, 0x00,
                    0x01, 0x00, 0x03, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x10, 0x02, 0x05, (byte) 0xD7, (byte) 0x88, (byte) 0xAA, 0x55, 0x00, 0x3A, 0x00,
                    0x00, 0x00, 0x02, 0x00, (byte) 0x93, 0x2C, (byte) 0xFB, 0x3A, 0x00, 0x00, 0x00, 0x39, 0x30, 0x30, 0x30, 0x30,
                    0x30, 0x30, 0x31, 0x01, 0x01, (byte) 0xCB, 0x01, 0x01, 0x00, 0x01, 0x01, 0x02, 0x00, 0x03, 0x00, 0x00, 0x00, 0x10, (byte) 0xCE,
                    (byte) 0xD2, (byte) 0xB1, (byte) 0xBB, (byte) 0xB4, (byte) 0xF2, (byte) 0xBD, (byte) 0xD9, (byte) 0xA3, (byte) 0xAC,
                    (byte) 0xC7, (byte) 0xEB, (byte) 0xB1, (byte) 0xA8, (byte) 0xBE, (byte) 0xAF, 0x11, (byte) 0xAE, (byte) 0x88};
            mIMcuSend.sendRS232((byte) 0x01, body);
        } else {
            CJLedProtocolPacket mCJLedProtocolPacket = new CJLedProtocolPacket(0x3a, "00000000", data.getBytes());
            mIMcuSend.sendRS232((byte) 0x01, mCJLedProtocolPacket.getDataBytes());
        }

    }

}
