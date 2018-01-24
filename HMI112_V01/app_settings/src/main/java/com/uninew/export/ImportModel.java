package com.uninew.export;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.uninew.car.db.driverMessage.DriverMessage;
import com.uninew.car.db.driverMessage.DriverMsgLocalDataSource;
import com.uninew.car.db.driverMessage.DriverMsgLocalSource;
import com.uninew.car.db.paramSet.ParamSetKey;
import com.uninew.car.db.paramSet.ParamSetLocalDataSource;
import com.uninew.car.db.paramSet.ParamSetLocalSource;
import com.uninew.car.db.paramSet.ParamSetting;
import com.uninew.car.db.settings.BaseLocalDataSource;
import com.uninew.car.db.settings.BaseLocalSource;
import com.uninew.car.db.settings.PlatformLocalDataSource;
import com.uninew.car.db.settings.PlatformLocalSource;
import com.uninew.car.db.settings.PlatformSettings;
import com.uninew.net.JT905.comm.client.IClientSendManage;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Administrator on 2017/11/23 0023.
 */

public class ImportModel implements ReadParamSetFiles.ReadFileCallBack {
    private static final String TAG = "ImportModel";
    private static final boolean D = true;
    private ReadParamSetFiles readParamSetFiles;
    private static final String USB_PATH = "/mnt/media_rw/udisk";
    public static final String PARAM_FILE_NAME = "paramSet.csv";
    public static final String DRIVER_FILE_NAME = "driverMessage.csv";
    public static final String WRAP = "\r\n";
    public static final String COMMA = ",";
    public static final String CHARSET_GBK = "GBK";
    private Queue<byte[]> buffersQueue = null;
    private Queue<String> namesQueue = null;
    private Object lock = null;
    //    private PlatformLocalSource platformLocalSource;
    private ParamSetLocalSource paramSetLocalSource;
    private BaseLocalSource baseLocalSource;
    private ImportThread mThread;
    private ImportCallBack mCallBack;
    private DriverMsgLocalSource driverMsgLocalSource;
    private static final int WHAT_IMPORT_SUCCESS = 0x01;
    private static final int WHAT_IMPORT_FAILURE = 0x02;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case WHAT_IMPORT_SUCCESS:
                    if (mCallBack != null) {
                        mCallBack.onImportSuccess();
                    }
                    break;
                case WHAT_IMPORT_FAILURE:
                    if (mCallBack != null) {
                        mCallBack.onImportFailure(1);
                    }
                    break;
            }
        }
    };

    public interface ImportCallBack {
        /**
         * 导出成功
         */
        void onImportSuccess();

        /**
         * 失败
         *
         * @param erroe 0：文件不存在，1：导入出错
         */
        void onImportFailure(int erroe);
    }

    public ImportModel(Context context) {
        readParamSetFiles = ReadParamSetFiles.getInstance();
        readParamSetFiles.setReadFileCallBack(this);
        buffersQueue = new LinkedList<>();
        namesQueue = new LinkedList<>();
        lock = new Object();
        driverMsgLocalSource = DriverMsgLocalDataSource.getInstance(context);
        paramSetLocalSource = ParamSetLocalDataSource.getInstance(context);
        baseLocalSource = BaseLocalDataSource.getInstance(context);
        mThread = new ImportThread();
        mThread.start();
    }

    public void setImportCallBack(ImportCallBack callBack) {
        this.mCallBack = callBack;
    }

    public void startImportParam() {
        File file = new File(USB_PATH + File.separator + PARAM_FILE_NAME);
        if (file.exists()) {
            Log.d(TAG, "文件存在！name:" + file.getName());
            readParamSetFiles.readFile(file);
        } else {
            Log.d(TAG, "文件不存在！name:" + file.getName());
            if (mCallBack != null) {
                mCallBack.onImportFailure(0);
            }
        }
    }

    public void startImportDriverMsg() {
        File file = new File(USB_PATH + File.separator + DRIVER_FILE_NAME);
        if (file.exists()) {
            readParamSetFiles.readFile(file);
        } else {
            if (mCallBack != null) {
                mCallBack.onImportFailure(0);
            }
        }
    }

    public void destroy() {
        readParamSetFiles.destroy();
        if (!buffersQueue.isEmpty()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mThread.stopThread();
        buffersQueue.clear();
        readParamSetFiles = null;
    }

    @Override
    public void onContentBuffer(String name, byte[] buffer) {
        if (!TextUtils.isEmpty(name) && buffer != null && buffer.length > 0) {
            synchronized (lock) {
                buffersQueue.offer(buffer);
                namesQueue.offer(name);
                mThread.importNotify();
            }
        } else {
            if (mCallBack != null) {
                mCallBack.onImportFailure(0);
            }
        }
    }

    private class ImportThread extends Thread {
        private boolean isRunning = true;
        private boolean isFinish = true;

        public void importNotify() {
            if (this.getState() == State.WAITING) {
                synchronized (this) {
                    this.notify();
                }
            }
        }

        public void importWait() {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public ImportThread() {
            super("ImportThread");
        }

        @Override
        public void run() {
            super.run();
            while (isRunning) {
                if (isFinish && buffersQueue != null && !buffersQueue.isEmpty()
                        ) {
                    importHandler();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    importWait();
                }
            }
        }

        private void importHandler() {
            isFinish = false;
            if (!buffersQueue.isEmpty() && !namesQueue.isEmpty()) {
                byte[] buffer = null;
                String name = null;
                synchronized (lock) {
                    buffer = buffersQueue.poll();
                    name = namesQueue.poll();
                }
                if (!TextUtils.isEmpty(name) && buffer != null && buffer.length > 0) {
                    Log.d(TAG, "name:" + name);
                    if (PARAM_FILE_NAME.equals(name)) {
                        importPeramSet(buffer);
                    } else if (DRIVER_FILE_NAME.equals(name)) {
                        importDriverMsg(buffer);
                    }
                }
            }
            isFinish = true;
        }

        public void stopThread() {
            isRunning = false;
            importNotify();
        }

    }

    private void importDriverMsg(byte[] buffer) {
        try {
            String src = new String(buffer, CHARSET_GBK);
            String[] params = src.split("\n");
            if (params != null && params.length > 1) {
                int size = params.length;
                DriverMessage[] driverMessages = new DriverMessage[size - 1];
                for (int j = 1; j < size; j++) {
                    String[] paramSets = params[j].split(COMMA);
                    if (paramSets != null) {
                        int length = paramSets.length;
                        DriverMessage driverMessage = new DriverMessage();
                        for (int i = 0; i < length; i++) {
                            String s = paramSets[i];
                            if (!TextUtils.isEmpty(s)) {

                                if (i == 0) {
                                    driverMessage.setDriverName(s);
                                }
                                if (i == 1) {
                                    driverMessage.setDriverCertificate(s);
                                }
                                if (i == 2) {
                                    driverMessage.setDriverPicture(s);
                                }
                                if (i == 3) {
                                    driverMessage.setDriverStar(srtToInt(s));
                                }
                            }
                        }
                        driverMessages[j - 1] = driverMessage;
                    }
                    driverMsgLocalSource.saveDBData(driverMessages);
                }
            }
            mHandler.sendEmptyMessage(WHAT_IMPORT_SUCCESS);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(WHAT_IMPORT_FAILURE);
        }
    }

    private void importPeramSet(byte[] buffer) {
        try {
            String src = new String(buffer, CHARSET_GBK);
            Log.d(TAG, "file,conntent:" + src);
            String[] params = src.split(WRAP);
            if (params != null) {
                if (params.length > 1) {
                    Log.d(TAG, "params:" + Arrays.toString(params));
                    String[] paramSets = params[1].split(COMMA);
                    if (paramSets != null) {
                        int length = paramSets.length;
                        for (int i = 0; i < length; i++) {
                            String s = paramSets[i];
                            Log.d(TAG, "s:" + s);
                            if (!TextUtils.isEmpty(s)) {
                                if (i == 0) {
                                    saveParam(ParamSetKey.MainIpOrDomain, s, (s.getBytes(CHARSET_GBK)).length);
                                }
                                if (i == 1) {
                                    saveParam(ParamSetKey.MainTcpPort, s, 4);
                                }
                                if (i == 2) {
                                    saveParam(ParamSetKey.SpareIpOrDomain, s, (s.getBytes(CHARSET_GBK)).length);
                                }
                                if (i == 3) {
                                    saveParam(ParamSetKey.SpareTcpPort, s, 4);
                                }
                                if (i == 4)
                                    baseLocalSource.setPlateNumber(paramSets[i]);
                                if (i == 5)
                                    baseLocalSource.setTerminalNumber(paramSets[i]);
                                if (i == 6) {
                                    saveParam(ParamSetKey.MaximumSpeed, s, 4);
                                }
                            }
                        }
                    }
                }
                if (params.length > 3) {
                    String[] paramSets1 = params[3].split(COMMA);
                    if (paramSets1 != null) {
                        int length1 = paramSets1.length;
                        for (int i = 0; i < length1; i++) {
                            String s = paramSets1[i];
                            Log.d(TAG, "s:" + s);
                            if (i == 0) {
                                saveParam(ParamSetKey.HeartBeat, s, 4);
                            }
                            if (i == 1) {
                                saveParam(ParamSetKey.TcpResponseTimeOut, s, 4);
                            }
                            if (i == 2) {
                                saveParam(ParamSetKey.TcpResendTime, s, 4);
                            }
                            if (i == 3) {
                                saveParam(ParamSetKey.MaximumCallTimePerOne, s, 4);
                            }
                            if (i == 4) {
                                saveParam(ParamSetKey.ResetPhoneNumber, s, 4);
                            }
                            if (i == 5) {
                                saveParam(ParamSetKey.RestoreSettingsPhoneNumber, s, 4);
                            }
                        }
                    }
                }
                if (params.length > 5) {
                    String[] paramSets_lacation = params[5].split(COMMA);
                    if (paramSets_lacation != null) {
                        int length_l = paramSets_lacation.length;
                        for (int i = 0; i < length_l; i++) {
                            String s = paramSets_lacation[i];
                            Log.d(TAG, "s:" + s);
                            if (i == 0) {
                                saveParam(ParamSetKey.LocationReportStratege, s, 4);
                            }
                            if (i == 1) {
                                saveParam(ParamSetKey.LocationReportPlan, s, 4);
                            }
                            if (i == 2) {
                                saveParam(ParamSetKey.UnLoginReportIntervalTime, s, 4);
                                saveParam(ParamSetKey.UnLoginReportIntervalDistance, s, 4);
                            }
                            if (i == 3) {
                                saveParam(ParamSetKey.AccOffReportIntervalTime, s, 4);
                                saveParam(ParamSetKey.AccOffReportIntervalDistance, s, 4);
                            }
                            if (i == 4) {
                                saveParam(ParamSetKey.AccOnfReportIntervalTime, s, 4);
                                saveParam(ParamSetKey.AccOnfReportIntervalTime, s, 4);
                            }
                            if (i == 5) {
                                saveParam(ParamSetKey.EmptyReportIntervalTime, s, 4);
                                saveParam(ParamSetKey.EmptyReportIntervalDistance, s, 4);
                            }
                            if (i == 6) {
                                saveParam(ParamSetKey.NonEmptyReportIntervalTime, s, 4);
                                saveParam(ParamSetKey.NonEmptyReportIntervalDistance, s, 4);
                            }
                            if (i == 7) {
                                saveParam(ParamSetKey.SleepReportIntervalTime, s, 4);
                                saveParam(ParamSetKey.SleepReportIntervalDistance, s, 4);
                            }
                            if (i == 8) {
                                saveParam(ParamSetKey.EmergencyReportIntervalTime, s, 4);
                                saveParam(ParamSetKey.EmergencyReportIntervalDistance, s, 4);
                            }
                        }
                    }
                }
                mHandler.sendEmptyMessage(WHAT_IMPORT_SUCCESS);
            } else {
                mHandler.sendEmptyMessage(WHAT_IMPORT_FAILURE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(WHAT_IMPORT_FAILURE);
        }
    }

    private void saveParam(int key, String value, int length) {
        ParamSetting paramSetting = new ParamSetting();
        paramSetting.setKey(key);
        paramSetting.setLength(length);
        paramSetting.setValue(value);
        paramSetLocalSource.saveDBData(paramSetting);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int srtToInt(String s) {
        int i = -1;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }
}
