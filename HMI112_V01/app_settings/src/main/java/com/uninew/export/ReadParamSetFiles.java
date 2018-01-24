package com.uninew.export;

import android.util.Log;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Administrator on 2017/11/23 0023.
 */

public class ReadParamSetFiles {
    private static final String TAG = "ReadParamSetFiles";
    private static final boolean D = true;
    private static volatile ReadParamSetFiles INSTANCE;
    private Queue<File> fileQueue = null;
    private Object lock = null;
    private ReadFileCallBack mCallBack;
    private ReadFileThread mThread;

    public interface ReadFileCallBack{
        void onContentBuffer(String name,byte[] buffer);
    }

    private ReadParamSetFiles() {
        fileQueue = new LinkedList<>();
        lock = new Object();
        mThread = new ReadFileThread();
        mThread.start();
    }

    public static ReadParamSetFiles getInstance() {
        if (INSTANCE != null) {

        } else {
            synchronized (ReadParamSetFiles.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ReadParamSetFiles();
                }
            }
        }
        return INSTANCE;
    }

    public void readFile(File file){
        if(file != null) {
            synchronized (lock) {
                fileQueue.offer(file);
                mThread.readNotify();
            }
        }
    }

    public void destroy(){
        if(!fileQueue.isEmpty()){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(mThread != null){
            mThread.stopThread();
            mThread = null;
        }
        INSTANCE = null;
    }

    public void setReadFileCallBack(ReadFileCallBack callBack){
        this.mCallBack = callBack;
    }

    private class ReadFileThread extends Thread {
        private boolean isFinish = true;
        private boolean isRunning = true;

        public ReadFileThread() {
            super("ReadFileThread");
            isRunning = true;
            isFinish = true;
        }

        public void readNotify() {
            if (this.getState() == State.WAITING) {
                synchronized (this) {
                    this.notify();
                }
            }
        }

        public void readWait() {
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
            readNotify();
        }

        @Override
        public void run() {
            super.run();
            while (isRunning) {
                if (fileQueue != null && isFinish && !fileQueue.isEmpty()) {
                    readHandler();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    readWait();
                }
            }
        }

        private void readHandler() {
            isFinish = false;
            if (!fileQueue.isEmpty()) {
                File file = null;
                synchronized (lock) {
                    file = fileQueue.poll();
                }
                if (file != null && file.exists() && file.canRead()) {
                    Log.d(TAG,"file:"+file.getName());
                    byte[] buffer = FileIOUtils.readFile2BytesByChannel(file);
                    if(mCallBack != null){
                        mCallBack.onContentBuffer(file.getName(),buffer);
                    }
                }
            }
            isFinish = true;
        }
    }
}
