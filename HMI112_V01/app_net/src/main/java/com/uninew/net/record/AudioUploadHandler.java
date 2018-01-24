package com.uninew.net.record;

import android.util.Log;

import com.uninew.net.JT905.bean.T_AudioVideoUpload;
import com.uninew.net.JT905.common.Define;
import com.uninew.net.main.LinkService;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Administrator on 2017/11/10 0010.
 */

public class AudioUploadHandler {

    private static final String TAG = "AudioUploadHandler";
    private static final boolean D = true;
    private static volatile AudioUploadHandler INSTANCE;

    private Queue<RequestRecord> recordQueue;
    private Object lock;
    private LinkService mService;
    private AudioUploadThread uploadThread;

    private AudioUploadHandler(LinkService service) {
        this.mService = service;
        recordQueue = new LinkedList<>();
        lock = new Object();
        uploadThread = new AudioUploadThread();
        uploadThread.setName("AudioUploadThread");
        uploadThread.start();
    }


    public static AudioUploadHandler getInstance(LinkService service) {
        if (INSTANCE != null) {

        } else {
            synchronized (AudioUploadHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AudioUploadHandler(service);
                }
            }
        }
        return INSTANCE;
    }


    public void sendSuccess() {
        synchronized (lock) {
            setAudioUploadState(AudioUploadState.success);
        }
    }

//    public void stopUpload() {
//        if (uploadThread != null) {
//            uploadThread.isRunning = false;
//            uploadThread.interrupt();
//            uploadThread = null;
//        }
//    }

    public void pictureupload(RequestRecord record) {
        synchronized (lock) {
            recordQueue.offer(record);
            if (uploadThread != null) {
                uploadThread.audioUploadNotify();
            }
        }
    }


    private enum AudioUploadState {
        init,
        start,
        send,
        wait,
        error,
        noLinkService,
        success,
        next,
        finish,
    }

    private AudioUploadState mState = AudioUploadState.init;

    private AudioUploadState getAudioUploadState() {
        return mState;
    }

    private void setAudioUploadState(AudioUploadState mstate) {
        this.mState = mstate;
        if (D) Log.d(TAG, "录音上传状态：" + mstate.name());
    }

    private class AudioUploadThread extends Thread {
        private boolean isFinish = true;
        private boolean isRunning = true;
        private Queue<byte[]> bufferQueue;
        private int totalLength = 0;
        private int sendedLength = 0;
        private RequestRecord mRecord = null;
        private byte[] sendBuffer = null;
        private int packNumber = 0;
        private long sendTime = 0;
        private long errorTime = 0;
        private static final int ONCE_PACK = 900;
        private static final long MAX_WAIT_SEND_TIME = 1000 * 20;
        private static final long MAX_WAIT_RESEND_TIME = 1000 * 5;

        public AudioUploadThread() {
            bufferQueue = new LinkedList<>();
            isRunning = true;
            isFinish = true;
            totalLength = 0;
            sendedLength = 0;
        }

        public void audioUploadNotify() {
            if (this.getState() == State.WAITING) {
                synchronized (this) {
                    this.notify();
                }
            }
        }

        public void audioUploadWait() {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stopThread(){
            isRunning = false;
            audioUploadNotify();
        }

        @Override
        public void run() {
            super.run();
            while (isRunning) {
                if (isFinish && recordQueue != null && !recordQueue.isEmpty()) {
                    uploadHandler();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    audioUploadWait();
                }
            }
        }

        private synchronized void uploadHandler() {
            isFinish = false;
            if (!recordQueue.isEmpty()) {
                synchronized (lock) {
                    switch (getAudioUploadState()) {
                        case init:
                            mRecord = recordQueue.peek();
                            if (mRecord != null && mRecord.getDataBytes() != null
                                    && mRecord.getDataBytes().length > 0) {
                                Log.d(TAG, mRecord.toString());
                                totalLength = mRecord.getDataBytes().length;
                                Log.d(TAG, "totalLength:" + totalLength);
                                splitAry(mRecord.getDataBytes(), ONCE_PACK);
                                setAudioUploadState(AudioUploadState.start);
                            } else {
                                setAudioUploadState(AudioUploadState.finish);
                            }
                            break;
                        case start:
                            if (bufferQueue.isEmpty() || totalLength <= 0) {
                                setAudioUploadState(AudioUploadState.finish);
                            } else {
                                setAudioUploadState(AudioUploadState.send);
                            }
                            break;
                        case send:
                            if (sendBuffer == null || sendBuffer.length <= 0) {
                                sendBuffer = bufferQueue.peek();
                            }
                            if (mService.connectStateMap.get(Define.TCP_ONE) != null
                                    && mService.connectStateMap.get(Define.TCP_ONE).linkState
                                    == 0x01) {
                                T_AudioVideoUpload videoUpload = new T_AudioVideoUpload();
                                videoUpload.setResponseSerialNumber(mRecord.getSerialNumber());
                                videoUpload.setFileId(packNumber);
                                videoUpload.setStartOffset(sendedLength);
                                videoUpload.setPacketSize(sendBuffer.length);
                                videoUpload.setAudioVideoDatas(sendBuffer);
                                sendTime = System.currentTimeMillis();
                                if (D) Log.d(TAG, "上传录音数据内容：" + videoUpload.toString());
                                setAudioUploadState(AudioUploadState.wait);
                                mService.sendToJT905(videoUpload);
                            } else {
                                sendTime = System.currentTimeMillis();
                                setAudioUploadState(AudioUploadState.noLinkService);
                            }
                            break;
                        case wait:
                            long time = System.currentTimeMillis();
                            if ((time - sendTime) > MAX_WAIT_SEND_TIME) {
                                errorTime = System.currentTimeMillis();
                                if (D) Log.e(TAG, "上传录音等待应答超时");
                                setAudioUploadState(AudioUploadState.error);
                            }
                            break;
                        case success:
                            if(sendBuffer != null) {
                                if (D) Log.e(TAG, "上传录音数据包成功,packNumber:" + packNumber);
                                bufferQueue.poll();
                                sendedLength += sendBuffer.length;
                                packNumber++;
                                sendTime = 0;
                                sendBuffer = null;
                            }
                            setAudioUploadState(AudioUploadState.next);
                            break;
                        case error:
                            long time1 = System.currentTimeMillis();
                            if ((time1 - errorTime) > MAX_WAIT_RESEND_TIME) {
                                if (D) Log.e(TAG, "重新上传录音");
                                errorTime = 0;
                                setAudioUploadState(AudioUploadState.send);
                            }
                            break;
                        case noLinkService:
                            long time2 = System.currentTimeMillis();
                            if ((time2 - sendTime) > MAX_WAIT_RESEND_TIME) {
                                sendTime = 0;
                                if (mService.connectStateMap.get(Define.TCP_ONE) != null
                                        && mService.connectStateMap.get(Define.TCP_ONE).linkState
                                        == 0x01) {
                                    setAudioUploadState(AudioUploadState.send);
                                }
                            }
                            break;
                        case next:
                            if (bufferQueue.isEmpty() && totalLength == sendedLength) {
                                setAudioUploadState(AudioUploadState.finish);
                            } else {
                                setAudioUploadState(AudioUploadState.send);
                            }
                            break;
                        case finish:
                            if (mRecord != null && totalLength > 0) {
                                recordQueue.poll();
                                mRecord = null;
                            }
                            sendedLength = 0;
                            packNumber = 0;
                            totalLength = 0;
                            bufferQueue = null;
                            setAudioUploadState(AudioUploadState.init);
                            break;
                    }
                }
            }
            isFinish = true;
        }

        private void splitAry(byte[] buffers, int subSize) {
            if (bufferQueue == null) {
                bufferQueue = new LinkedList<>();
            }
            int count = buffers.length % subSize == 0
                    ? buffers.length / subSize : buffers.length / subSize + 1;
            for (int i = 0; i < count; i++) {
                if (i == count - 1) {
                    if (D) Log.d(TAG, "subsize:" + i * subSize + ",length:" + buffers.length);
                    byte[] b = Arrays.copyOfRange(buffers, i * subSize, buffers.length);
                    bufferQueue.offer(b);
                } else {
                    byte[] b = Arrays.copyOfRange(buffers, i * subSize, (i + 1) * subSize);
                    bufferQueue.offer(b);
                }
            }
        }
    }

    public void destroy(){
        if(!recordQueue.isEmpty()){
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        uploadThread.stopThread();
        recordQueue.clear();
    }

}
