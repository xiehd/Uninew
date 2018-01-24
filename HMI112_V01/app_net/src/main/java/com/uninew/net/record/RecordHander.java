package com.uninew.net.record;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.uninew.net.JT905.util.SDCardUtils;
import com.uninew.net.location.LocationReportHandle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Administrator on 2017/11/2 0002.
 */

public class RecordHander {
    private static final String TAG = "RecordHander";
    private static final boolean D = true;
    private static final String SDCARD_PATH = SDCardUtils.getSDCardPaths(false);
    private Queue<RequestRecord> records;
    private static volatile RecordHander INSTANCE;
    private AudioRecordManage recordManage;
    private static final int WHAT_RECORDING_START = 0x01;
    private static final int WHAT_RECORDING_STOP = 0x02;
    private static final int WHAT_RECORDING_FINISH = 0x03;
    private static final int WHAT_RECORDING_ERROR = 0x04;
    private static final String AUDIO_FORMAT_MP3 = ".mp3";
    private Object recordingLock;
    private RecordingThread mThread;
    private RecordingCallBack mCallBack;
    private boolean isRecording = false;
    private String savePath = SDCARD_PATH + File.separator + "audio";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case WHAT_RECORDING_START:
                    mThread.recordingNotify();
                    if (isRecording) {
                        stop();
                    }
                    break;
                case WHAT_RECORDING_STOP:
                    setRecordingState(RecordingState.finish);
                    break;
                case WHAT_RECORDING_FINISH:
                    RequestRecord record = (RequestRecord) msg.obj;
                    if (mCallBack != null && record != null) {
                        mCallBack.onSuccess(record, fileToByte(savePath + File.separator
                                + record.getFileId() + AUDIO_FORMAT_MP3));
                    }
                    break;
                case WHAT_RECORDING_ERROR:
                    int error = msg.arg1;
                    if (mCallBack != null) {
                        mCallBack.onFailure(error);
                    }
                    break;
            }
        }
    };

    public interface RecordingCallBack {
        void onFailure(int error);

        void onSuccess(RequestRecord record, byte[] buffers);
    }

    private RecordHander() {
        records = new LinkedList<>();
        recordManage = AudioRecordManage.getInstance();
        recordingLock = new Object();
        mThread = new RecordingThread();
        mThread.setName("RecordingThread");
        mThread.start();

    }

    public void setCallBack(RecordingCallBack callBack) {
        this.mCallBack = callBack;
    }


    public static RecordHander getInstance() {
        if (INSTANCE != null) {

        } else {
            synchronized (RecordHander.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RecordHander();
                }
            }
        }
        return INSTANCE;
    }

    private enum RecordingState {
        init,
        start,
        wait,
        finish,
    }

    private RecordingState mState = RecordingState.init;

    private RecordingState getRecordingState() {
        return mState;
    }

    private void setRecordingState(RecordingState mState) {
        this.mState = mState;
        if (D) Log.d(TAG, "当前录音状态：" + mState.name());
    }

    private class RecordingThread extends Thread {
        private boolean isFinish = true;
        private RequestRecord record;
        private int duration;
        private String audioName;
        private boolean isRunning = true;
        private long startTime;
        private double startLatitude;
        private double startLongitude;
        private long endTime;
        private double endLatitude;
        private double endLongitude;

        public void recordingNotify() {
            if (this.getState() == State.WAITING) {
                synchronized (this) {
                    this.notify();
                }
            }
        }

        public void recordingWait() {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            super.run();
            while (isRunning) {
                if (records != null && records.size() > 0 && isFinish) {
                    recordingHandler();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    recordingWait();
                }
            }
        }

        public void stopThread(){
            isRunning = false;
            recordingNotify();
        }

        private synchronized void recordingHandler() {
            isFinish = false;
            if (!records.isEmpty()) {
                synchronized (recordingLock) {
                    record = records.peek();
                    switch (getRecordingState()) {
                        case init:
                            if (record != null) {
                                if (recordManage == null) {
                                    recordManage = AudioRecordManage.getInstance();
                                }
                                duration = record.getDuration();
                                audioName = record.getFileId() + AUDIO_FORMAT_MP3;
                                recordManage.setSaveAudioPath(savePath + File.separator + audioName);
                                setRecordingState(RecordingState.start);
                            } else {
                                setRecordingState(RecordingState.finish);
                            }
                            break;
                        case start:
                            int result = recordManage.startRecordAndFile();
                            if (result == ErrorCode.SUCCESS) {
                                if (duration > 0) {
                                    mHandler.sendEmptyMessageDelayed(WHAT_RECORDING_STOP, duration * 1000);
                                }
                                startTime = System.currentTimeMillis();
                                startLatitude = LocationReportHandle.mGpsInfo.getLatitude();
                                startLongitude = LocationReportHandle.mGpsInfo.getLongitude();
                                isRecording = true;
                                setRecordingState(RecordingState.wait);
                            } else {
                                Message msg = Message.obtain();
                                msg.what = WHAT_RECORDING_ERROR;
                                msg.arg1 = result;
                                mHandler.sendMessage(msg);
                                setRecordingState(RecordingState.finish);
                            }
                            break;
                        case wait:
                            break;
                        case finish:
                            recordManage.stopRecordAndFile();
                            endTime = System.currentTimeMillis();
                            endLatitude = LocationReportHandle.mGpsInfo.getLatitude();
                            endLongitude = LocationReportHandle.mGpsInfo.getLongitude();
                            if (record != null) {
                                records.poll();
                                record.setStartTime(startTime);
                                record.setStartLatitude(startLatitude);
                                record.setStartLongitude(startLongitude);
                                record.setEndLatitude(endLatitude);
                                record.setEndLongitude(endLongitude);
                                record.setEndTime(endTime);
                                Message msg = Message.obtain();
                                msg.obj = record;
                                msg.what = WHAT_RECORDING_FINISH;
                                mHandler.sendMessage(msg);
                            }
                            isRecording = false;
                            setRecordingState(RecordingState.init);
                            break;
                    }
                }
            }
            isFinish = true;
        }
    }

    private byte[] fileToByte(String filePath) {
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            bos.flush();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer;
    }

    public void requestRecording(RequestRecord record) {
        synchronized (recordingLock) {
            records.offer(record);
            mHandler.sendEmptyMessageDelayed(WHAT_RECORDING_START,200);
        }
    }

    public void stop() {
        mHandler.sendEmptyMessageDelayed(WHAT_RECORDING_STOP, 1000);
    }

    public void destroy(){
        if(!records.isEmpty()){
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mThread.stopThread();
        records.clear();
    }
}
