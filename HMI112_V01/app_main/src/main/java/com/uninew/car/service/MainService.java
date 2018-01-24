package com.uninew.car.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.ndk.led.ReadCardThread;
import com.uninew.car.Main.MainActivity;
import com.uninew.car.Main.MainContract;
import com.uninew.car.Main.MainPresenter;
import com.uninew.car.MainApplication;
import com.uninew.car.constant.DefineActivityAction;
import com.uninew.car.messages.MessagesModel;
import com.uninew.car.orders.OrderModel;
import com.uninew.car.phone.PhoneModel;
import com.uninew.net.JT905.bean.T_LocationReport;
import com.uninew.net.JT905.bean.T_OperationDataReport;
import com.uninew.net.JT905.comm.client.ClientReceiveManage;
import com.uninew.net.JT905.comm.client.ClientSendManage;
import com.uninew.net.JT905.comm.client.IClientReceiveListener;
import com.uninew.net.JT905.comm.client.IClientReceiveManage;
import com.uninew.net.JT905.comm.client.IClientSendManage;
import com.uninew.net.JT905.common.DeviceState;
import com.uninew.net.JT905.common.LogTool;
import com.uninew.net.JT905.common.ProtocolTool;
import com.uninew.net.JT905.common.TimeTool;
import com.uninew.net.Taximeter.bean.P_TaxiOperationDataReport;

/**
 * Created by Administrator on 2017/10/26.
 */

public class MainService extends Service {

    private static final boolean D = true;
    private static final String TAG = "MainService";
    private MainService service;
    private IClientReceiveManage clientReceiveManage;
    private IClientSendManage clientSendManage;
    private OrderModel orderModel;
    private MessagesModel messagesModel;
    //读卡线程
    private ReadCardThread mReadCardThread;
    private PhoneModel phoneModel;

    private static DeviceState mDeviceState;

    @Override
    public IBinder onBind(Intent intent) {
        return new MainBiner();
    }

    public class MainBiner extends Binder {
        public MainService getService() {
            if (service == null) {
                service = MainService.this;
            }
            return service;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        service = this;
        Log.d(TAG,"-----onCreate------");
        init();
    }


    private void init(){

        registerBroadCast();

//        mReadCardThread = new ReadCardThread(mHandler);
//        mReadCardThread.start();

        mDeviceState = DeviceState.getInstant();

        clientReceiveManage = new ClientReceiveManage(this);
        clientSendManage = new ClientSendManage(this);
        orderModel = new OrderModel(this);
        messagesModel = new MessagesModel(this);
        phoneModel = PhoneModel.getInstance(this.getApplicationContext());
        phoneModel.setReceiveManage(clientReceiveManage);
        orderModel.initReceive(clientReceiveManage);
        orderModel.initSendManage(clientSendManage);
        messagesModel.initReceiveManage(clientReceiveManage);
        messagesModel.initSendManage(clientSendManage);
        phoneModel.registerPhoneListener();
    }



    Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            // TODO Auto-generated method stub
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    //HashMap<String, Object> hash = (HashMap<String, Object>) msg.obj;
                    String number = (String) msg.obj;
                    Readok(number);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 读卡成功
     * @param number
     */
    private void Readok(String number){
        Bundle bundle = new Bundle();
        bundle.putString("car_number",number);
        Intent intent = new Intent("com.uninew.car.SignActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        service.startActivity(intent);
    }

    private MainServiceBroadcastReceiver mBroadcastReceiver;
    private void registerBroadCast() {
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(DefineActivityAction.Action_MainReSet);//复位
        iFilter.addAction(DefineActivityAction.Action_Device_State);//设备状态
        iFilter.addAction(DefineActivityAction.Action_StateDate);
        mBroadcastReceiver = new MainServiceBroadcastReceiver();
        this.registerReceiver(mBroadcastReceiver, iFilter);
    }

    public class MainServiceBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case DefineActivityAction.Action_MainReSet://复位
                    ResetApp();
                    break;
                case DefineActivityAction.Action_Device_State://设备更新
                    int type = intent.getIntExtra(DefineActivityAction.DeviceState.Device_Type,-1);
                    int state = intent.getIntExtra(DefineActivityAction.DeviceState.Device_State,-1);
                    Log.d(TAG,"---Device_State___"+"type="+type+",state="+state);
                    UpdateDeviceState(type,state);
                    sendDeviceReceice();
                    break;
                case DefineActivityAction.Action_StateDate://发送设备对象
                    sendDeviceReceice();
                    break;
                default:
                    break;
            }
        }
    }

    private void sendDeviceReceice(){
        if (mDeviceState != null){
            Intent state = new Intent("Action_Device_StateDateResponse");
            state.putExtra("device_obj",mDeviceState);
            this.sendBroadcast(state);
        }
    }
    /**
     * 更新设备状态
     * @param type
     * @param state
     */
    private void UpdateDeviceState(int type,int state){
        switch (type){
            case 0x00://摄像头
                mDeviceState.setDvr_state(state);
                break;
            case 0x01://计价器
                mDeviceState.setTaximeter_state(state);
                break;
            case 0x02://视频
                mDeviceState.setVedio_state(state);
                break;
            case 0x03://空车屏
                mDeviceState.setCarscreen_state(state);
                break;
            case 0x04://GPS天线
                mDeviceState.setGps_state(state);
                break;
            case 0x05://通讯模块
                mDeviceState.setNet_state(state);
                break;
            case 0x06://供电
                mDeviceState.setPower_state(state);
                break;
            case 0x07://出租服务
                mDeviceState.setRentserver_state(state);
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"-----onDestroy------");
        if (orderModel != null) {
            orderModel.unRegister();
        }
        if (messagesModel != null) {
            messagesModel.unRegisterListener();
        }
        if(mReadCardThread != null){
            mReadCardThread.stop();
        }
        if(phoneModel != null){
            phoneModel.unRegisterPhoneListener();
        }

    }
    //复位重启
    public void ResetApp(){
        Log.e("MainService","---MainService---reset");
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent restartIntent = PendingIntent.getActivity(
                this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //退出程序
        AlarmManager mgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                restartIntent); // 1秒钟后重启应用
        MainApplication.getInstance().exit();
    }

    //恢复出厂设置
    public void systemReset(){
        //Settings.System.putInt(this.getContentResolver(),"",1);
        this.sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
    }

}
