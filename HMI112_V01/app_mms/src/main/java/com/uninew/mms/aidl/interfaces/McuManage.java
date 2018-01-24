package com.uninew.mms.aidl.interfaces;


import android.os.RemoteException;

import com.uninew.mms.aidl.aidl.RecorderServicePolicy;

public class McuManage implements IMcuSend {
    IMcuReceiveListener mcuReceiveListener;
    private static RecorderServicePolicy mRecorderServicePolicy;
    private boolean isRegister = false;


    public McuManage(IMcuReceiveListener mcuReceiveListener) {
        this.mcuReceiveListener = mcuReceiveListener;
        // 初始化
    }


    public boolean getIsRegister() {
        if (mRecorderServicePolicy != null) {
            isRegister = mRecorderServicePolicy.getIsRegister();
        }
        return isRegister;
    }

    @Override
    public void ARMstateSynchro(byte state) {
        if ((mRecorderServicePolicy = RecorderServicePolicy.getPolicy()) != null && getIsRegister()) {
            try {
                mRecorderServicePolicy.ARMstateSynchro(state);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void screenBacklight(byte control) {
        if ((mRecorderServicePolicy = RecorderServicePolicy.getPolicy()) != null && getIsRegister()) {
            try {
                mRecorderServicePolicy.screenBacklight(control);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void screenBrightness(byte brightness) {
        if ((mRecorderServicePolicy = RecorderServicePolicy.getPolicy()) != null && getIsRegister()) {
            try {
                mRecorderServicePolicy.screenBrightness(brightness);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void powerAmplifier(byte control) {
        if ((mRecorderServicePolicy = RecorderServicePolicy.getPolicy()) != null && getIsRegister()) {
            try {
                mRecorderServicePolicy.powerAmplifier(control);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean sendCanDatas(byte[] canDatas) {
        boolean result = true;
        if ((mRecorderServicePolicy = RecorderServicePolicy.getPolicy()) != null && getIsRegister()) {
            try {
                result = mRecorderServicePolicy.sendCanDatas(canDatas);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean sendRS232(byte id, byte[] rs232Datas) {
        boolean result = true;
        if ((mRecorderServicePolicy = RecorderServicePolicy.getPolicy()) != null && getIsRegister()) {
            try {
                result = mRecorderServicePolicy.sendRS232(id, rs232Datas);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean sendRS485(byte id, byte[] rs485Datas) {
        boolean result = true;
        if ((mRecorderServicePolicy = RecorderServicePolicy.getPolicy()) != null && getIsRegister()) {
            try {
                result = mRecorderServicePolicy.sendRS485(id, rs485Datas);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean setBaudRate(byte id, byte baudRate) {
        boolean result = true;
        if ((mRecorderServicePolicy = RecorderServicePolicy.getPolicy()) != null && getIsRegister()) {
            try {
                result = mRecorderServicePolicy.setBaudRate(id, baudRate);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean setIOState(byte id, byte state) {
        boolean result = true;
        if ((mRecorderServicePolicy = RecorderServicePolicy.getPolicy()) != null && getIsRegister()) {
            try {
                result = mRecorderServicePolicy.setIOState(id, state);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean setAccOffTime(int watiTime) {
        boolean result = true;
        if ((mRecorderServicePolicy = RecorderServicePolicy.getPolicy()) != null && getIsRegister()) {
            try {
                result = mRecorderServicePolicy.setAccOffTime(watiTime);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean setWakeupFrequency(int intervalTime) {
        boolean result = true;
        if ((mRecorderServicePolicy = RecorderServicePolicy.getPolicy()) != null && getIsRegister()) {
            try {
                result = mRecorderServicePolicy.setWakeupFrequency(intervalTime);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void queryElectricity(byte type) {
        if ((mRecorderServicePolicy = RecorderServicePolicy.getPolicy()) != null && getIsRegister()) {
            try {
                mRecorderServicePolicy.queryElectricity(type);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void queryIOState(byte id) {
        if ((mRecorderServicePolicy = RecorderServicePolicy.getPolicy()) != null && getIsRegister()) {
            try {
                mRecorderServicePolicy.queryIOState(id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void queryPulseSignal() {
        if ((mRecorderServicePolicy = RecorderServicePolicy.getPolicy()) != null && getIsRegister()) {
            try {
                mRecorderServicePolicy.queryPulseSignal();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
