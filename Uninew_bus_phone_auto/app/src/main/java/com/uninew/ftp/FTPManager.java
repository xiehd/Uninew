package com.uninew.ftp;

import android.text.TextUtils;
import android.util.Log;

import com.uninew.json.JsonFileContants;
import com.uninew.utils.TimeTools;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * Created by rong on 2017-04-14.
 */

public class FTPManager implements UpdateCallBack{
    private static final String TAG = "FTPManager";
    private static final boolean D = true;
    FTPClient ftpClient;
    private String mHost;
    private int mPort;
    private String mUserName;
    private String mPassword;
    private boolean isLogin = false;
    private FTPProgressListener mListener;
    private static volatile FTPManager instance;
    private volatile Queue<UpdateFile> updateQueue = null;
    private Object updateLock = null;
    private static final String UPDATEFILEPATH = "/MiddleData/Up/";
    private UpdateThread mUpdateThread;
    private UpdateCallBack updateCallBack;


    private FTPManager() {
        ftpClient = new FTPClient();
        updateCallBack =  this;
        updateQueue = new LinkedList<>();
        updateLock = new Object();
        mUpdateThread = new UpdateThread();
        mUpdateThread.start();

//        srcName = UPDATEFILEPATH + "公交资源文件"+ TimeTools.getCurrentTime()+"/";
    }

    public void setListener(FTPProgressListener listener) {
        this.mListener = listener;
    }

    public static FTPManager getInstance(String host, int port, String uersname, String password) {
        if (null != instance) {
        } else {
            synchronized (FTPManager.class) {
                if (null == instance) {
                    instance = new FTPManager();
                }
            }
        }
        instance.mHost = host;
        instance.mPassword = password;
        instance.mPort = port;
        instance.mUserName = uersname;
        return instance;
    }


    /**
     * 连接服务器
     *
     * @return
     */
    public synchronized final boolean connect() {
        boolean result = false;
//        FTPClientConfig config = new FTPClientConfig();
//        config.setServerTimeZoneId(TimeZone.getDefault().getID());
        ftpClient.setControlEncoding("GBK");
//        ftpClient.configure(config);
        ftpClient.setConnectTimeout(10 * 1000);
        if (TextUtils.isEmpty(mHost) || TextUtils.isEmpty(mUserName) || TextUtils.isEmpty(mPassword)) {
            return result;
        }
        try {
            if (mPort > 0) {
                ftpClient.connect(mHost, mPort);
            } else {
                ftpClient.connect(mHost);
            }
//            int reply = ftpClient.getReplyCode();
//            if (FTPReply.isPositiveCompletion(reply)) {
//                ftpClient.disconnect();
//                return result;
//            }
            boolean login = ftpClient.login(mUserName, mPassword);
            if (login) {
                // 获取登录信息
                FTPClientConfig config = new FTPClientConfig(ftpClient
                        .getSystemType().split(" ")[0]);
                config.setServerLanguageCode("zh");
                ftpClient.configure(config);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.setBufferSize(2 * 1024);
                ftpClient.setDataTimeout(30 * 1000);
                ftpClient.setDefaultTimeout(5 * 1000);
                ftpClient.setSoTimeout(30 * 1000);
                ftpClient.setPassiveNatWorkaround(true);
//                ftpClient.enterLocalPassiveMode();
                isLogin = true;
                result = true;
            } else {
                isLogin = false;
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 断开服务器
     *
     * @return
     */
    public synchronized final boolean unConnect() {
        boolean result = false;
        if (null != ftpClient && ftpClient.isConnected()) {
            try {
                if (isLogin) {
                    boolean islogout = ftpClient.logout();
                    if (islogout) {

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 上传单文件
     *
     * @param localFile         本地文件路径
     * @param remoteUpLoadePath 服务器工作路径
     * @return
     */
    public final boolean updateFile(String localFile, String remoteUpLoadePath) {
        boolean result = false;
        BufferedInputStream bis = null;
        Log.i(TAG, "下载文件是：" + localFile);
        Log.i(TAG, "下载文件是：" + remoteUpLoadePath);
        File file = new File(localFile);
//        ProgressInputStream pis = null;
        if (file.exists() && file.isFile()) {
            try {
                boolean isChange = ftpClient.changeWorkingDirectory(remoteUpLoadePath);
                if (!isChange) {
                    if (D) Log.e(TAG, "改变工作文件夹失败");
                    updateCallBack.onFinish(false);
                    return false;
                }
                bis = new BufferedInputStream(new FileInputStream(file));
//                pis = new ProgressInputStream(bis, mListener, file);
//                ftpClient.enterLocalPassiveMode();
                boolean success = ftpClient.storeUniqueFile(file.getName(), bis);
                if (success) {
                    if (D) Log.e(TAG, "上传成功");
                    result = true;
                } else {
                    if (D) Log.e(TAG, "上传失败");
                }
                updateCallBack.onFinish(true);
            } catch (IOException e) {
                e.printStackTrace();
                updateCallBack.onFinish(false);
            } finally {
                try {
                    if (null != bis) {
                        bis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            updateCallBack.onFinish(false);
        }
        return result;
    }

    /**
     * 上传整个文件夹内容
     *
     * @param localDirectory      本地文件夹路径
     * @param remoteDirectoryPath 服务器工作路径
     */
    public final void updateDirectory(String localDirectory, String remoteDirectoryPath) {
        File directory = new File(localDirectory);
        if (directory.exists() && directory.isDirectory()) {
            try {
                remoteDirectoryPath = remoteDirectoryPath + directory.getName() + "/";
                ftpClient.makeDirectory(remoteDirectoryPath);
            } catch (IOException e) {
                if (D) Log.e(TAG, "创建目录失败");
                e.printStackTrace();
            }
            File[] files = directory.listFiles();
            if (null != files && files.length > 0) {
                for (File file : files) {
                    if (!file.isDirectory()) {
                        String srcName = file.getAbsolutePath();
                        if (D) Log.i(TAG, "上传文件" + srcName);
                        boolean isSuccess = updateFile(srcName, remoteDirectoryPath);
                        if (null != mListener) {
                            if (!isSuccess) {
                                mListener.onUploadProgress(
                                        FTPState.UpdateFile, 0, file);
                                return;
                            } else {
                                mListener.onUploadProgress(
                                        FTPState.UpdateError, 0, file);
                            }
                        }
                    } else {
                        if (D) Log.i(TAG, "上传文件夹" + file.getName());
                        updateDirectory(file.getAbsolutePath(), remoteDirectoryPath);
                    }
                }
            }
        }
    }

    /**
     * 下载单个文件
     *
     * @param remoteFileName     下载的文件名
     * @param localDires         保存路径
     * @param remoteDownLoadPath 下载路径
     * @return
     */
    public synchronized final boolean downloadFile(String remoteFileName, String localDires, String remoteDownLoadPath) {
        boolean result = false;
        File dir = new File(localDires);
        BufferedOutputStream bos = null;
//        InputStream is = null;

        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (dir.exists()) {
            try {
                boolean isChange = ftpClient.changeWorkingDirectory(remoteDownLoadPath);
                if (!isChange) {
                    return false;
                }
                File file = new File(dir.getAbsolutePath() + "/" + remoteFileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                Log.i(TAG, file.getAbsolutePath());
                bos = new BufferedOutputStream(new FileOutputStream(file, false));
//                FTPFile ftpFile = ftpClient.mlistFile(remoteFileName);
                ftpClient.retrieveFile(remoteFileName, bos);
                bos.flush();
                result = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != bos) {
                        bos.close();
                    }
//                    if (null != is) {
//                        is.close();
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 下载文件夹
     *
     * @param localDirectoryPath 保存路径
     * @param remoteDirectory    下载路径
     */
    public void downloadDirectory(String localDirectoryPath, String remoteDirectory) {
        try {
            String srcName = new File(remoteDirectory).getName();
            localDirectoryPath = localDirectoryPath + "/" + srcName + "/";
            new File(localDirectoryPath).mkdirs();
            Log.i(TAG, "" + localDirectoryPath);
            ftpClient.changeWorkingDirectory(remoteDirectory);
            Log.i(TAG, "" + remoteDirectory);
            FTPFile[] ftpFiles = ftpClient.listFiles(remoteDirectory);
            Log.i(TAG, "" + ftpFiles.length);
            if (null != ftpFiles && ftpFiles.length > 0) {
                for (FTPFile ftpFile : ftpFiles) {
                    Log.i(TAG, ftpFile.getName());
                    if (!ftpFile.isDirectory()) {
                        boolean isFinish = downloadFile(ftpFile.getName(), localDirectoryPath, remoteDirectory);
                        if (null != mListener) {
                            if (isFinish) {
                                mListener.onDownLoadProgress(
                                        FTPState.DownLoadFile, 0, null);
                            } else {
                                mListener.onDownLoadProgress(
                                        FTPState.DownLoadError, 0, null);
                            }
                        }
                    } else {
                        String nextRemoteDirectory = remoteDirectory + "/" + ftpFile.getName();
                        downloadDirectory(localDirectoryPath, nextRemoteDirectory);
                    }
                }
            } else {
                Log.i(TAG, "获取的目录为空");
                mListener.onDownLoadProgress(
                        FTPState.DownLoadError, 0, null);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadFiles(String localDirectoryPath, String remoteDirectory) {
        boolean isConnect = connect();
        if (null != mListener) {
            if (isConnect) {
                mListener.onDownLoadProgress(
                        FTPState.connect, 0, null);
            } else {
                mListener.onDownLoadProgress(FTPState.connectError,
                        0, null);
                return;
            }
        }
        downloadDirectory(localDirectoryPath, remoteDirectory);
        if (null != mListener) {
            mListener.onDownLoadProgress(FTPState.FinishDownLoad,
                    0, null);
        }
        unConnect();
    }

    public void updateFiles(UpdateFile file) {
//        boolean isConnect = connect();
//        if (null != mListener) {
//            if (isConnect) {
//                mListener.onUploadProgress(
//                        FTPState.connect, 0, null);
//            } else {
//                mListener.onUploadProgress(FTPState.connectError,
//                        0, null);
//                return;
//            }
//        }
//        updateDirectory(localDirectory, remoteDirectoryPath);
//        if (null != mListener) {
//            mListener.onUploadProgress(FTPState.FinishUpdate, 0,
//                    null);
//        }
//        unConnect();
        synchronized (updateLock) {
            updateQueue.offer(file);
            mUpdateThread.notifyThread();
        }
    }

    private volatile UpdateState mUpdateState = UpdateState.connect;

    private UpdateState getUpdateState() {
        return mUpdateState;
    }

    private void setUpdateState(UpdateState mUpdateState) {
        this.mUpdateState = mUpdateState;
        Log.i(TAG, mUpdateState.name());
    }

    @Override
    public void onFinish(boolean isSuccess) {
        synchronized (updateLock) {
            mUpdateThread.cunnentTimes = 0;
            if (isSuccess) {
                setUpdateState(UpdateState.UpdateFileFinish);
            } else {
                setUpdateState(UpdateState.UpdateFileError);
            }
        }
    }

    private enum UpdateState {
        connect,
        connectError,
        UpdateFile,
        UpdateFileing,
        UpdateFileError,
        UpdateFileFinish
    }

    private boolean isUpdateRunning = true;
//    private String srcName = UPDATEFILEPATH;


    private class UpdateThread extends Thread {
        private boolean isFinish = true;
        private boolean isUpdate = false;
        private long cunnentTimes = 0;

        public void notifyThread() {
            synchronized (this) {
                this.notify();
            }
        }

        public void waitThread() {
            try {
                synchronized (this) {
                    this.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


        @Override
        public void run() {
            super.run();
            if (isInterrupted()) {
                return;
            }
            while (isUpdateRunning) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (isFinish && !updateQueue.isEmpty()) {
                        updateHandler();
                    } else {
                        unConnect();
                        waitThread();
                    }
                }
            }
        }

        private synchronized void updateHandler() {
            isFinish = false;
            if (null != updateQueue && !updateQueue.isEmpty()) {
                synchronized (updateLock) {
                    UpdateFile updateFile = updateQueue.peek();
                    switch (getUpdateState()) {
                        case connect:
                            if (!ftpClient.isConnected()) {
                                boolean isConnect = connect();
                                if (isConnect) {
                                    setUpdateState(UpdateState.UpdateFile);
                                } else {
                                    setUpdateState(UpdateState.connectError);
                                }
                            } else {
                                setUpdateState(UpdateState.UpdateFile);
                            }
                            break;
                        case connectError:
                            try {
                                Thread.sleep(5 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } finally {
                                setUpdateState(UpdateState.connect);
                            }
                            break;
                        case UpdateFile:
                            File file = updateFile.getFile();
                            if (file.isDirectory()) {
                                Log.i(TAG, file.getName());
                                try {
                                    String srcName = updateFile.getRemotePath();
                                    boolean isChange = ftpClient.changeWorkingDirectory(srcName);
                                    if (!isChange) {
                                        ftpClient.makeDirectory(srcName);
//                                        ftpClient.changeWorkingDirectory(updateFile.getRemotePath());
                                    } else {
                                        srcName = updateFile.getRemotePath() + "/" + file.getName();
                                        ftpClient.makeDirectory(srcName);
                                    }
                                    File[] files = file.listFiles();
                                    for (File file1 : files) {
                                        UpdateFile updateFile1 = new UpdateFile();
                                        updateFile1.setFile(file1);
                                        updateFile1.setRemotePath(srcName);
                                        updateQueue.offer(updateFile1);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    updateQueue.poll();
                                    setUpdateState(UpdateState.UpdateFile);
                                }
                            } else {
                                ftpClient.enterLocalPassiveMode();
                                cunnentTimes = System.currentTimeMillis();
                                setUpdateState(UpdateState.UpdateFileing);
                                File file1 = updateFile.getFile();
                                updateFile(file1.getAbsolutePath(), updateFile.getRemotePath());
                            }
                            break;
                        case UpdateFileing:
                            long time = System.currentTimeMillis();
                            if ((time - cunnentTimes) > 1000 * 30) {
                                setUpdateState(UpdateState.UpdateFileError);
                            }
                            break;
                        case UpdateFileError:
                            unConnect();
                            setUpdateState(UpdateState.connect);
                            if (mListener != null)
                                mListener.onUploadProgress(
                                        FTPState.UpdateFile, 0, updateFile.getFile());
                            break;
                        case UpdateFileFinish:
                            updateQueue.poll();
                            if (mListener != null)
                                mListener.onUploadProgress(
                                        FTPState.UpdateFile, 0, updateFile.getFile());
                            setUpdateState(UpdateState.connect);
                            break;
                    }
                }
            }
            isFinish = true;
        }

    }


}
