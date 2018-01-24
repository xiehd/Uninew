package com.uninew.net.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.uninew.car.db.photo.PhotoData;
import com.uninew.car.db.photo.PhotoLocalDataSource;
import com.uninew.car.db.photo.PhotoLocalSource;
import com.uninew.net.JT905.bean.CameraPhoto;
import com.uninew.net.location.LocationReportHandle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Administrator on 2017/11/3 0003.
 */

public class CameraHandle implements CameraManage.CameraCallBack {
    private static final String TAG = "CameraHandle";
    private static final boolean D = true;
    private static volatile CameraHandle INSTANCE;
    private Context mContext;
    private CameraManage cameraManage;
    private Queue<RequestCamera> cameraQueue;
    private Object cameraLock;
    private String savePath;
    private byte[] buffers = null;
    private static final int WHAT_CAMERA_START = 0x01;
    private static final int WHAT_CAMERA_STOP = 0x02;
    private static final int WHAT_CAMERA_FINISH = 0x03;
    private static final int WHAT_CAMERA_ERROR = 0x04;
    private static final int WHAT_CAMERA_SUCCESS = 0x05;
    private CameraThread cameraThread;
    private boolean isCamera = false;
    private PhotoLocalSource photoLocalSource;
    private CameraUploadCallBack mCallBack;
    private File photoFile;
    private Bitmap photoBitmap;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case WHAT_CAMERA_START:
                    cameraThread.cameraNotify();
                    break;
                case WHAT_CAMERA_STOP:
                    setCameraState(CameraState.finish);
                    break;
                case WHAT_CAMERA_SUCCESS:
                    RequestCamera camera = (RequestCamera) msg.obj;
                    photoCallBack(camera);
                    break;
                case WHAT_CAMERA_ERROR:

                    break;
                case WHAT_CAMERA_FINISH:
                    RequestCamera camera1 = (RequestCamera) msg.obj;
                    photoCallBack(camera1);
                    break;
            }
        }
    };


    private void photoCallBack(RequestCamera camera) {
        if (camera != null) {
            if (photoBitmap != null) {
                mapToByte(photoBitmap, camera.getImageVideoQuality());
            } else if (photoFile != null) {
                fileToByte(photoFile);
            }
            if (buffers != null && buffers.length > 0) {
                PhotoData photoData = new PhotoData();
                photoData.setCameraId(camera.getCameraId());
                photoData.setTime(camera.getTime());
                photoData.setRevenueId(camera.getRevenueId());
                photoData.setReason(camera.getReason());
                photoData.setCarName(camera.getCarName());
                photoData.setCodingType(camera.getCodingType());
                photoData.setFileId(camera.getFileId());
                photoData.setIsuId(camera.getIsuId());
                photoData.setLatitude(camera.getLatitude());
                photoData.setLongitude(camera.getLongitude());
                photoData.setPhotoLength(buffers.length);
                photoData.setPhotoBuffers(buffers);
                photoLocalSource.saveDBData(photoData);
                if (mCallBack != null) {
                    CameraPhoto cameraPhoto = new CameraPhoto();
                    cameraPhoto.setCameraId(camera.getCameraId());
                    cameraPhoto.setTime(camera.getTime());
                    cameraPhoto.setRevenueId(camera.getRevenueId());
                    cameraPhoto.setReason(camera.getReason());
                    cameraPhoto.setCarName(camera.getCarName());
                    cameraPhoto.setCodingType(camera.getCodingType());
                    cameraPhoto.setFileId(camera.getFileId());
                    cameraPhoto.setIsuId(camera.getIsuId());
                    cameraPhoto.setLatitude(camera.getLatitude());
                    cameraPhoto.setLongitude(camera.getLongitude());
                    cameraPhoto.setPhotoLength(buffers.length);
                    cameraPhoto.setPhotoBuffers(buffers);
                    cameraPhoto.setPromptlyCamera(true);
                    mCallBack.onCameraPhoto(cameraPhoto);
                }
                buffers = null;
            }
        }
        photoBitmap = null;
        photoFile = null;
    }

    public void setCameraUploadCallBack(CameraUploadCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface CameraUploadCallBack {
        void onCameraPhoto(CameraPhoto cameraPhoto);
    }

    private CameraHandle(Context context) {
        this.mContext = context;
        cameraLock = new Object();
        cameraManage = CameraManage.getInstance(context);
        photoLocalSource = PhotoLocalDataSource.getInstance(context);
        cameraManage.setCallBack(this);
        cameraQueue = new LinkedList<>();
        cameraThread = new CameraThread();
        cameraThread.setName("CameraThread");
        cameraThread.start();
    }

    public void stopCamera() {
        if (isCamera) {
            setCameraState(CameraState.finish);
        }
    }

    public static CameraHandle getInstance(Context context) {
        if (INSTANCE != null) {

        } else {
            synchronized (CameraHandle.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CameraHandle(context);
                }
            }
        }
        return INSTANCE;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public void requestCamera(RequestCamera camera) {
        synchronized (cameraLock) {
            cameraQueue.offer(camera);
            if (isCamera == false) {
                mHandler.sendEmptyMessageDelayed(WHAT_CAMERA_START, 500);
            } else {
                mHandler.sendEmptyMessageDelayed(WHAT_CAMERA_STOP, 500);
            }
        }
    }

    @Override
    public void openSuccess() {
        setCameraState(CameraState.wait);
    }

    @Override
    public void onFailure(int error) {
        setCameraState(CameraState.error);
    }

    @Override
    public void onSuccess(int surplusTimes, File file) {
        photoFile = file;
        if (surplusTimes == 0) {
            setCameraState(CameraState.finish);
        } else {
            setCameraState(CameraState.success);
        }
    }

    @Override
    public void onSuccess(int surplusTimes, Bitmap bitmap) {
        photoBitmap = bitmap;
        if (surplusTimes == 0) {
            setCameraState(CameraState.finish);
        } else {
            setCameraState(CameraState.success);
        }
    }

    private void mapToByte(Bitmap bitmap, int imageVideoQuality) {
        if (buffers != null) {
            buffers = null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, imageVideoQuality * 10, baos);
        buffers = baos.toByteArray();
        try {
            baos.flush();
            baos.close();
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fileToByte(File file) {
        if (buffers != null) {
            buffers = null;
        }
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            bos.flush();
            buffers = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFinish() {
//        setCameraState(CameraState.finish);
    }

    private enum CameraState {
        init,
        start,
        wait,
        success,
        error,
        finish
    }

    private CameraState mState = CameraState.init;

    private CameraState getCameraState() {
        return mState;
    }

    private void setCameraState(CameraState mState) {
        this.mState = mState;
        Log.d(TAG, "拍照状态！" + mState.name());
    }

    private class CameraThread extends Thread {
        private boolean isFinish = true;
        private boolean isRunning = true;
        private RequestCamera camera;


        public void cameraNotify() {
            if (this.getState() == State.WAITING) {
                synchronized (this) {
                    this.notify();
                }
            }
        }

        public void cameraWait() {
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
                if (cameraQueue != null && cameraQueue.size() > 0 && isFinish) {
                    cameraHandler();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    cameraWait();
                }
            }
        }

        public void stopThread(){
            isRunning = false;
            cameraNotify();
        }

        private synchronized void cameraHandler() {
            isFinish = false;
            if (!cameraQueue.isEmpty()) {
                synchronized (cameraLock) {
                    camera = cameraQueue.peek();
                    switch (getCameraState()) {
                        case init:
                            if (camera != null) {
                                if (cameraManage == null) {
                                    cameraManage = CameraManage.getInstance(mContext);
                                    cameraManage.setCallBack(CameraHandle.this);
                                }
                                cameraManage.setViedo(camera.isViedo());
                                cameraManage.setShootInterval(camera.getShootInterval());
                                cameraManage.setScale(camera.getmWidth(), camera.getmHeight());
                                cameraManage.setContrast(camera.getContrast());
                                cameraManage.setBrightness(camera.getBrightness());
                                cameraManage.setChroma(camera.getChroma());
                                cameraManage.setSaturation(camera.getSaturation());
                                cameraManage.setShootTimes(camera.getShootTimes());
                                cameraManage.setImageVideoQuality(camera.getImageVideoQuality());
                                cameraManage.setShootInterval(camera.getShootInterval());
                                cameraManage.setSavePath(savePath);
                                cameraManage.setPhotoName(camera.getFileId() + "");
                                cameraManage.setSaveFile(camera.isSaveFile());
                                setCameraState(CameraState.start);
                            } else {
                                setCameraState(CameraState.finish);
                            }
                            break;
                        case start:
                            if (cameraManage.isOpenning() == false) {
                                cameraManage.start();
                                isCamera = true;
                                setCameraState(CameraState.wait);
                            } else {
                                setCameraState(CameraState.finish);
                            }
                            break;
                        case wait:

                            break;
                        case success:
                            if (camera != null) {
                                camera.setTime(System.currentTimeMillis());
                                camera.setLatitude(LocationReportHandle.mGpsInfo.getLatitude());
                                camera.setLongitude(LocationReportHandle.mGpsInfo.getLongitude());
                            }
                            Message msg = Message.obtain();
                            msg.obj = camera;
                            msg.what = WHAT_CAMERA_SUCCESS;
                            mHandler.sendMessage(msg);
                            setCameraState(CameraState.wait);
                            break;
                        case error:
                            mHandler.sendEmptyMessage(WHAT_CAMERA_ERROR);
                            setCameraState(CameraState.finish);
                            break;

                        case finish:
                            if (camera != null) {
                                cameraQueue.poll();
                                camera.setTime(System.currentTimeMillis());
                                camera.setLatitude(LocationReportHandle.mGpsInfo.getLatitude());
                                camera.setLongitude(LocationReportHandle.mGpsInfo.getLongitude());
                                Message msg1 = Message.obtain();
                                msg1.obj = camera;
                                msg1.what = WHAT_CAMERA_FINISH;
                                mHandler.sendMessage(msg1);
                            }
                            cameraManage.stop();
                            isCamera = false;
                            setCameraState(CameraState.init);
                            break;
                    }
                }
            }
            isFinish = true;
        }
    }

    public void destroy(){
        if(!cameraQueue.isEmpty()){
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        cameraThread.stopThread();
        cameraQueue.clear();
    }
}
