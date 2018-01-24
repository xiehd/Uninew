package com.uninew.maintanence.model;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/11/15 0015.
 */

public class DataModel {

    private static final String TAG = "DataModel";
    private static final boolean D = true;
    private static final int WHAT_COPY_RESULT = 0x01;
    private static final int WHAT_COPY_FILE = 0x02;
    private static final String KEY_SAVE_FILE = "save";
    private static final String KEY_COPY_FILE = "copy";

    private FileThreadPool fileThreadPool = null;
    private ConcurrentMap<String, String> copyFileMap = null;
    private ConcurrentMap<String, String> waitCopyFileMap = null;
    private Object copyLock = null;
    private CopyCallBack copyCallBack;
//    private ExecutorService executorService;

    public interface CopyCallBack {
        /**
         * 文件拷贝结果
         *
         * @param path   文件路径
         * @param result -2：没有此类文件；-1：拷贝异常；0：失败；1：成功
         */
        void copyResult(String path, int result);

        /**
         * 拷贝完成
         */
        void copyFinish();
    }

    public void setCopyCallBack(CopyCallBack copyCallBack) {
        this.copyCallBack = copyCallBack;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case WHAT_COPY_RESULT:
                    int result = msg.arg1;
                    String key = (String) msg.obj;
                    if (D) Log.d(TAG, "file:" + key + ",result:" + result);
                    if (copyCallBack != null) {
                        copyCallBack.copyResult(key, result);
                    }
                    if (copyFileMap.isEmpty() && waitCopyFileMap.isEmpty()) {
                        //完成
                        if (D) Log.d(TAG, "拷贝完成！");
                        if (copyCallBack != null) {
                            copyCallBack.copyFinish();
                        }
                    }
                    break;
                case WHAT_COPY_FILE:
                    Bundle b = msg.getData();
                    if (b != null && !b.isEmpty()) {
                        File save = new File(b.getString(KEY_SAVE_FILE));
                        File file = new File(b.getString(KEY_COPY_FILE));
                        copyFile(file, save);
                    }
                    break;
            }
        }
    };

    public DataModel() {
    }

    private void startCopFiles() {
        fileThreadPool = FileThreadPool.getThreadPool();
//        executorService = Executors.newFixedThreadPool(5);
//        executorService.
        copyFileMap = new ConcurrentHashMap<>();
        waitCopyFileMap = new ConcurrentHashMap<>();
        copyLock = new Object();
    }


    public void startCopyFiles(final File save, final File... copy) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startCopFiles();
                synchronized (copyLock) {
                    copyDirectory(save, copy);
                }
            }
        }).start();
    }

    private int fileChannelCopy(File file, File saveFile) {
        int result = 0;
        if (file != null && file.exists() && saveFile != null) {
            FileInputStream fi = null;
            FileOutputStream fo = null;
            FileChannel in = null;
            FileChannel out = null;
            try {
                if (!saveFile.exists()) {
                    saveFile.createNewFile();
                }
                fi = new FileInputStream(file);
                fo = new FileOutputStream(saveFile);
                in = fi.getChannel();//得到对应的文件通道
                out = fo.getChannel();//得到对应的文件通道
                in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
                fo.flush();
                result = 1;
            } catch (IOException e) {
                e.printStackTrace();
                result = -1;
            } finally {
                try {
                    fi.close();
                    in.close();
                    fo.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            result = -2;
        }
        return result;
    }


    private void copyFile(File file, File save) {
        if (copyFileMap.containsKey(file.getAbsolutePath())) {
            waitCopyFileMap.putIfAbsent(file.getAbsolutePath(), save.getAbsolutePath());
        } else {
            copyFileMap.putIfAbsent(file.getAbsolutePath(), save.getAbsolutePath());
            fileThreadPool.execute(new CopyFileRunnable(file, save));
        }
    }

    private void copyDirectory(File saveFile, File... files) {
        if (saveFile != null && files != null) {
            if (!saveFile.exists()) // 如果文件夹不存在
                saveFile.mkdirs(); // 建立新的文件夹
            int size = files.length;
            for (int i = 0; i < size; i++) {
                File file = files[i];
                if (file.isDirectory()) { // 如果是文件夹类型
                    File des = new File(saveFile.getAbsolutePath() + File.separator
                            + file.getName());
                    des.mkdirs(); // 在目标文件夹中创建相同的文件夹
                    File[] copyFile = file.listFiles();
                    if (copyFile != null && copyFile.length > 0) {
                        copyDirectory(des, copyFile); // 递归调用方法本身
                    }
                }
                if (file.isFile()) { // 如果是文件类型就复制文件
                    File save = new File(saveFile, file.getName());
                    if (save.exists()) {
                        save.delete();
                    }
                    copyFile(file, save);
                }
            }
        }
    }

    private class CopyFileRunnable implements Runnable {

        private File file;
        private File saveFile;

        public CopyFileRunnable(File file, File saveFile) {
            this.file = file;
            this.saveFile = saveFile;
        }

        @Override
        public void run() {
            int result = fileChannelCopy(file, saveFile);
            String key = file.getAbsolutePath();
            if (D) Log.d(TAG, "file:" + key + ",result:" + result);
            synchronized (copyLock) {
                copyFileMap.remove(key);
                if (copyFileMap.isEmpty() && waitCopyFileMap.isEmpty()) {
                    //完成
                    if (D) Log.d(TAG, "拷贝完成！");
                    fileThreadPool.destroy();
                } else {
                    if (!waitCopyFileMap.isEmpty()) {
                        Set<String> keys = waitCopyFileMap.keySet();
                        if (keys != null) {
                            for (String k : keys) {
                                if (!copyFileMap.containsKey(k)) {
                                    File file = new File(k);
                                    File saveFile = new File(waitCopyFileMap.get(k));
                                    fileThreadPool.execute(new CopyFileRunnable(file, saveFile));
                                    copyFileMap.putIfAbsent(k, waitCopyFileMap.get(k));
                                    waitCopyFileMap.remove(k);
                                }
                            }
                        }
                    }
                }
            }
            Message msg = Message.obtain();
            msg.what = WHAT_COPY_RESULT;
            msg.arg1 = result;
            msg.obj = file.getAbsolutePath();
            mHandler.sendMessage(msg);
        }
    }
}
