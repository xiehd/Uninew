package com.uninew.net.camera;


import android.util.Log;

import com.uninew.net.JT905.bean.CameraPhoto;
import com.uninew.net.JT905.bean.T_PictureUpload;
import com.uninew.net.JT905.common.Define;
import com.uninew.net.main.LinkService;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Administrator on 2017/11/8 0008.
 */

public class PictureUploadHandler {

    private static final String TAG = "PictureUploadHandler";
    private static final boolean D = true;
    private static volatile PictureUploadHandler INSTANCE;
    private LinkService mService;
    private Queue<CameraPhoto> photoQueue;
    private Object lock;
    private PictureUploadThread uploadThread;
    private PictureUploadHandler(LinkService service){
        this.mService = service;
        photoQueue = new LinkedList<>();
        lock = new Object();
        uploadThread = new PictureUploadThread();
        uploadThread.setName("PictureUploadThread");
        uploadThread.start();
    }

    public static PictureUploadHandler getInstance(LinkService service){
        if(INSTANCE != null){

        }else {
            synchronized (PictureUploadHandler.class){
                if(INSTANCE == null){
                    INSTANCE = new PictureUploadHandler(service);
                }
            }
        }
        return INSTANCE;
    }


    public void sendSuccess(){
        synchronized (lock) {
            setPictureUploadState(PictureUploadState.success);
        }
    }

//    public void stopUpload(){
//        if(uploadThread != null){
//            uploadThread.isRunning = false;
//            uploadThread.interrupt();
//            uploadThread = null;
//        }
//    }

    public void pictureupload(CameraPhoto cameraPhoto){
        synchronized (lock){
            photoQueue.offer(cameraPhoto);
            if(uploadThread != null){
                uploadThread.pictureUploadNotify();
            }
        }
    }

    private enum PictureUploadState{
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

    private PictureUploadState mState = PictureUploadState.init;

    private PictureUploadState getPictureUploadState() {
        return mState;
    }

    private void setPictureUploadState(PictureUploadState mstate) {
        this.mState = mstate;
        if(D) Log.d(TAG,"照片上传状态："+mstate.name());
    }

    private class PictureUploadThread extends Thread{
        private boolean isFinish = true;
        private boolean isRunning = true;
        private Queue<byte[]> bufferQueue;
        private int totalLength = 0;
        private int sendedLength = 0;
        private CameraPhoto mPhoto = null;
        private byte[] sendBuffer = null;
        private int packNumber = 0;
        private long sendTime = 0;
        private long errorTime = 0;
        private static final int ONCE_PACK = 900;
        private static final long MAX_WAIT_SEND_TIME = 1000 * 20;
        private static final long MAX_WAIT_RESEND_TIME = 1000 * 5;

        public PictureUploadThread(){
            bufferQueue = new LinkedList<>();
            isRunning = true;
            isFinish = true;
            totalLength = 0;
            sendedLength = 0;
        }

        public void pictureUploadNotify(){
            if(this.getState() == State.WAITING){
                synchronized (this){
                    this.notify();
                }
            }
        }

        public void pictureUploadWait(){
            synchronized (this){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stopThread(){
            isRunning = false;
            pictureUploadNotify();
        }

        @Override
        public void run() {
            super.run();
            while (isRunning){
                if(isFinish && photoQueue != null && !photoQueue.isEmpty()){
                    uploadHandler();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    pictureUploadWait();
                }
            }
        }

        private synchronized void uploadHandler(){
            isFinish = false;
            if(!photoQueue.isEmpty()){
                synchronized (lock){
                    switch (getPictureUploadState()){
                        case init:
                            mPhoto = photoQueue.peek();
                            if(mPhoto != null && mPhoto.getDataBytes() != null
                                    &&  mPhoto.getDataBytes().length > 0){
                                Log.d(TAG,mPhoto.toString());
                                totalLength = mPhoto.getDataBytes().length;
                                Log.d(TAG,"totalLength:"+totalLength);
                                splitAry(mPhoto.getDataBytes(),ONCE_PACK);
                                setPictureUploadState(PictureUploadState.start);
                            }else {
                                setPictureUploadState(PictureUploadState.finish);
                            }
                            break;
                        case start:
                            if(bufferQueue.isEmpty() || totalLength <= 0){
                                setPictureUploadState(PictureUploadState.finish);
                            }else {
                                setPictureUploadState(PictureUploadState.send);
                            }
                            break;
                        case send:
                            if(sendBuffer == null || sendBuffer.length <= 0){
                                sendBuffer = bufferQueue.peek();
                            }
                            if (mService.connectStateMap.get(Define.TCP_ONE) != null
                                    &&mService.connectStateMap.get(Define.TCP_ONE).linkState
                                    == 0x01) {
                                T_PictureUpload pictureUpload = new T_PictureUpload();
                                if(packNumber == 0 && sendedLength == 0){
                                    pictureUpload.setUpReason(mPhoto.getReason());
                                }else {
                                    if(mPhoto.isPromptlyCamera()) {
                                        pictureUpload.setUpReason(mPhoto.getSerialNumber());
                                    }else {
                                        pictureUpload.setUpReason(0);
                                    }
                                }
                                pictureUpload.setCarmeraId(mPhoto.getCameraId());
                                pictureUpload.setImage(packNumber);
                                pictureUpload.setImageSize(totalLength);
                                pictureUpload.setStartPosition(sendedLength);
                                pictureUpload.setDatas(sendBuffer);
                                sendTime = System.currentTimeMillis();
                                if(D) Log.d(TAG,"上传照片数据内容："+pictureUpload.toString());
                                setPictureUploadState(PictureUploadState.wait);
                                mService.sendToJT905(pictureUpload);
                            }else {
                                sendTime = System.currentTimeMillis();
                                setPictureUploadState(PictureUploadState.noLinkService);
                            }
                            break;
                        case wait:
                            long time = System.currentTimeMillis();
                            if((time - sendTime) > MAX_WAIT_SEND_TIME){
                                errorTime = System.currentTimeMillis();
                                if(D) Log.e(TAG,"上传照片等待应答超时");
                                setPictureUploadState(PictureUploadState.error);
                            }
                            break;
                        case success:
                            if(D) Log.e(TAG,"上传照片数据包成功,packNumber:"+packNumber);
                            bufferQueue.poll();
                            sendedLength += sendBuffer.length;
                            packNumber ++;
                            sendTime = 0;
                            sendBuffer = null;
                            setPictureUploadState(PictureUploadState.next);
                            break;
                        case error:
                            long time1 = System.currentTimeMillis();
                            if((time1 - errorTime) > MAX_WAIT_RESEND_TIME){
                                if(D) Log.e(TAG,"重新上传照片");
                                errorTime = 0;
                                setPictureUploadState(PictureUploadState.send);
                            }
                            break;
                        case noLinkService:
                            long time2 = System.currentTimeMillis();
                            if((time2 - sendTime) > MAX_WAIT_RESEND_TIME){
                                sendTime = 0;
                                if (mService.connectStateMap.get(Define.TCP_ONE) != null
                                        &&mService.connectStateMap.get(Define.TCP_ONE).linkState
                                        == 0x01) {
                                    setPictureUploadState(PictureUploadState.send);
                                }
                            }
                            break;
                        case next:
                            if(bufferQueue.isEmpty() && totalLength == sendedLength){
                                setPictureUploadState(PictureUploadState.finish);
                            }else {
                                setPictureUploadState(PictureUploadState.send);
                            }
                            break;
                        case finish:
                            if(mPhoto != null && totalLength > 0){
                                photoQueue.poll();
                                mPhoto = null;
                            }
                            sendedLength = 0;
                            packNumber = 0;
                            totalLength = 0;
                            bufferQueue = null;
                            setPictureUploadState(PictureUploadState.init);
                            break;
                    }
                }
            }
            isFinish = true;
        }

        private void splitAry(byte[] buffers,int subSize){
            if(bufferQueue == null){
                bufferQueue = new LinkedList<>();
            }
            int count = buffers.length % subSize == 0
                    ? buffers.length / subSize: buffers.length / subSize + 1;
            for(int i = 0; i < count ; i++){
                if(i == count -1){
                    if(D)Log.d(TAG,"subsize:"+i*subSize+",length:"+buffers.length);
                    byte[]  b = Arrays.copyOfRange(buffers,i*subSize,buffers.length);
                    bufferQueue.offer(b);
                }else {
                    byte[] b = Arrays.copyOfRange(buffers,i*subSize,(i+1)*subSize);
                    bufferQueue.offer(b);
                }
            }
        }
    }


    public void destroy(){
        if(!photoQueue.isEmpty()){
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        uploadThread.stopThread();
        photoQueue.clear();
    }

}
