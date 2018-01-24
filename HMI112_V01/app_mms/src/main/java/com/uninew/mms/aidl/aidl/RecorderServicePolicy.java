package com.uninew.mms.aidl.aidl;

import android.os.RemoteException;
import android.util.Log;

import com.cookoo.car_terminal.aidl.IUninewAppSend;
import com.uninew.mms.util.LogTool;

/**
 * @author hej 发送消息处理类
 */
public class RecorderServicePolicy {


    private static final String TAG = "RecorderServicePolicy";
    private static final boolean D = true;
    private byte[] arr;

    private static IUninewAppSend commSender = null;

    private boolean isRegister = false;

    private static RecorderServicePolicy recorderServicePolicy = null;

    private RecorderServicePolicy() {
    }


    public static RecorderServicePolicy getPolicy() {
        if (recorderServicePolicy == null)
            recorderServicePolicy = new RecorderServicePolicy();
        return recorderServicePolicy;
    }

    public RecorderServicePolicy(byte[] arr) {
        super();
        this.arr = arr;
    }

    public byte[] getArr() {
        return arr;
    }

    public void setArr(byte[] arr) {
        this.arr = arr;
    }

    /**
     * 设置远程发送对象
     *
     * @param commSender void
     * @author:tang
     * @createTime: 2016-10-19 下午5:31:25
     * @history:
     */
    public void setRecorderSender(IUninewAppSend commSender) {
        // TODO Auto-generated method stub
        this.commSender = commSender;
    }

    /**
     * 每次取得发送对象时检测是否为空
     */
    public static IUninewAppSend getSender() {
        if (commSender == null) {
//            if (D)
//                Log.e(TAG, "----send----getSender--null---");
            throw new Error("IUninewAppSend  Sender is NULL!!");
        }
        return commSender;
    }

    public void setIsRegister(boolean isRegister) {
        this.isRegister = isRegister;

    }

    public boolean getIsRegister() {
        return isRegister;
    }


    public void ARMstateSynchro(byte state) throws RemoteException {
        if (D)
            Log.d(TAG, "----send----ARMstateSynchro-----" + state);
        getSender().ARMstateSynchro(state);
    }

    public void screenBacklight(byte control) throws RemoteException {
        if (D)
            Log.d(TAG, "----send----screenBacklight-----" + control);
        getSender().screenBacklight(control);
    }

    public void screenBrightness(byte brightness) throws RemoteException {
        if (D)
            Log.d(TAG, "----send----screenBrightness-----" + brightness);
        getSender().screenBrightness(brightness);
    }

    public void powerAmplifier(byte control) throws RemoteException {
        if (D)
            Log.d(TAG, "----send----powerAmplifier-----" + control);
        getSender().powerAmplifier(control);
    }

    public boolean sendCanDatas(byte[] canDatas) throws RemoteException {
        if (D)
            Log.d(TAG, "----send----sendCanDatas-----");
        return getSender().sendCanDatas(canDatas);
    }

    public boolean sendRS232(byte id, byte[] rs232Datas) throws RemoteException {
        if (D)
            Log.d(TAG, "----send----sendRS232-----" + id);
        LogTool.logBytes(TAG,"发送232数据：--id = "+id+":",rs232Datas);
        return getSender().sendRS232(id, rs232Datas);
    }

    public boolean sendRS485(byte id, byte[] rs485Datas) throws RemoteException {
        if (D)
            Log.d(TAG, "----send----sendRS485-----" + id);
        return getSender().sendRS485(id, rs485Datas);
    }

    public boolean setBaudRate(byte id, byte baudRate) throws RemoteException {
        if (D)
            Log.d(TAG, "----send----setBaudRate-----" + baudRate);
        return getSender().setBaudRate(id, baudRate);
    }

    public boolean setIOState(byte id, byte state) throws RemoteException {
        if (D)
            Log.d(TAG, "----send----setIOState-----" + state);
        return getSender().setIOState(id, state);
    }

    public boolean setAccOffTime(int watiTime) throws RemoteException {
        if (D)
            Log.d(TAG, "----send----setAccOffTime-----" + watiTime);
        return getSender().setAccOffTime(watiTime);
    }

    public boolean setWakeupFrequency(int intervalTime) throws RemoteException {
        if (D)
            Log.d(TAG, "----send----setWakeupFrequency-----" + intervalTime);
        return getSender().setWakeupFrequency(intervalTime);
    }

    public void queryElectricity(byte type) throws RemoteException {
        if (D)
            Log.d(TAG, "----send----queryElectricity-----" + type);
        getSender().queryElectricity(type);
    }
    public void queryIOState(byte id) throws RemoteException {
        if (D)
            Log.d(TAG, "----send----queryIOState-----" + id);
        getSender().queryIOState(id);
    }
    public void queryPulseSignal() throws RemoteException {
        if (D)
            Log.d(TAG, "----send----queryPulseSignal-----" );
        getSender().queryPulseSignal();
    }
}
