package com.uninew.net.camera;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.uninew.net.JT905.util.SDCardUtils;
import com.uninew.net.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/10/30 0030.
 */

public class CameraManage {


    private static final String TAG = "CameraManage";
    private static final boolean D = true;
    private static volatile CameraManage INSTANCE;
    private Camera mCamera;
    private MediaRecorder mRecorder;
    /* 相机显示view */
    private CameraPreview mPreview;
    private FrameLayout mSurfaceViewFrame;
    /* SD卡根目录*/
    protected static final String SDCARD_PATH = SDCardUtils.getSDCardPaths(false);
    /* 存储地址*/
    private String savePath = SDCARD_PATH + File.separator + "photo" + File.separator;
    /*照片高*/
    private int mHeight = 840;
    /*照片宽*/
    private int mWidth = 840;
    private Context mContext;
    private CamremaHandler mHandler;
    /* 拍照次数*/
    private int shootTimes = 1;
    /* 拍照状态*/
    private boolean isCamera = false;
    /* 拍照时间间隔 单位秒*/
    private int shootInterval = 5;
    /**
     * 小悬浮窗View的实例
     */
    private PhotoWindowSmallView smallWindow;

    /*是否保存成文件*/
    private boolean isSaveFile = false;

    private static final String PHOTO_FORMAT_PNG = ".png";
    private static final String VIDEO_FORMAT_MP4 = ".mp4";
    private String photoName = "test";

    /**
     * 小悬浮窗View的参数
     */
    private WindowManager.LayoutParams smallWindowParams;

    private static int MAX_VALUE = 255;
    private static int MID_VALUE = 127;

    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private WindowManager mWindowManager;
    /*色调*/
    private float chroma = 127;
    /*饱和度*/
    private float saturation = 63;
    /*亮度*/
    private float brightness = 127;
    /* 对比度*/
    private float contrast = 63;
    /* 图片/视频质量*/
    private int imageVideoQuality = 100;

    private CameraCallBack mCallBack;

    private boolean isRecording = false;

    private boolean isViedo = false;

    private boolean isOpenning = false;

//    private CameraDialog cameraDialog;

    ////////////////////错误参数值//////////////////////////////////////////
    /* 没有发现拍照设备*/
    protected static final int NO_FIND_CAMERA = 0x01;
    /* 打开相机失败*/
    protected static final int OPEN_FAILURE_CAMERA = 0x02;
    /* 聚焦失败*/
    protected static final int AUTOFOCUS_FAILURE = 0x03;
    /* 拍照失败*/
    protected static final int PHOTOGRAPH_FAILURE = 0x04;
    /* 拍照正在进行中*/
    protected static final int PHOTOGRAPH_ING = 0x05;


    private CameraManage(Context context) {
        this.mContext = context;
        mHandler = new CamremaHandler(Looper.getMainLooper());
    }

    protected static CameraManage getInstance(Context context) {
        if (null != INSTANCE) {

        } else {
            synchronized (CameraManage.class) {
                if (null == INSTANCE) {
                    INSTANCE = new CameraManage(context);
                }
            }
        }
        return INSTANCE;
    }

    protected void setCallBack(CameraCallBack callBack) {
        this.mCallBack = callBack;
    }


    /**
     * 设置生成照片的色度
     *
     * @param chroma
     */
    protected void setChroma(float chroma) {
        if (chroma > 0) {
            this.chroma = chroma;
        }
    }

    /**
     * 拍照状态
     *
     * @return true:处于拍照中，false：相机关闭状态
     */
    protected boolean isCamera() {
        return isCamera;
    }

    /**
     * 是否正在开启摄像头
     *
     * @return
     */
    protected boolean isOpenning() {
        return isOpenning;
    }

    protected void setViedo(boolean viedo) {
        isViedo = viedo;
    }

    /**
     * 设置生成照片的饱和度
     *
     * @param saturation
     */
    protected void setSaturation(float saturation) {
        if (saturation > 0) {
            this.saturation = saturation;
        }
    }

    /**
     * 设置生成照片的亮度
     *
     * @param brightness
     */
    protected void setBrightness(float brightness) {
        if (brightness > 0)
            this.brightness = brightness;
    }

    /**
     * 设置生成照片的对比度
     *
     * @param contrast
     */
    protected void setContrast(float contrast) {
        if (contrast > 0)
            this.contrast = contrast;
    }

    protected void setSaveFile(boolean saveFile) {
        isSaveFile = saveFile;
    }

    /**
     * 图片/视频质量
     *
     * @param imageVideoQuality 0-100
     */
    protected void setImageVideoQuality(int imageVideoQuality) {
        if (imageVideoQuality > 0)
            this.imageVideoQuality = imageVideoQuality;
    }

    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    private void createSmallWindow(Context context) {
        mContext = context;
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (smallWindow == null) {
            smallWindow = new PhotoWindowSmallView(context);
            if (smallWindowParams == null) {
                smallWindowParams = new WindowManager.LayoutParams();
                smallWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                smallWindowParams.format = PixelFormat.RGBA_8888;
                smallWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                smallWindowParams.width = PhotoWindowSmallView.viewWidth;
                smallWindowParams.height = PhotoWindowSmallView.viewHeight;
                smallWindowParams.x = screenWidth;
                smallWindowParams.y = screenHeight / 2;
            }
            smallWindow.setParams(smallWindowParams);
            windowManager.addView(smallWindow, smallWindowParams);
            mSurfaceViewFrame = (FrameLayout) smallWindow.findViewById(R.id.percent);
        }
    }

    /**
     * 将小悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    protected void removeSmallWindow(Context context) {
        if (smallWindow != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(smallWindow);
            smallWindow = null;
        }
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     *
     * @param context 必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }


    /**
     * 设置照片存储地址
     *
     * @param savePath
     */
    protected void setSavePath(String savePath) {
        if (!TextUtils.isEmpty(savePath))
            this.savePath = savePath;
    }

    /**
     * 设置照片名称
     *
     * @param photoName
     */
    protected void setPhotoName(String photoName) {
        if (!TextUtils.isEmpty(photoName))
            this.photoName = photoName;
    }

    /**
     * 设置相片大小
     *
     * @param width
     * @param height
     */
    protected void setScale(int width, int height) {
        if (width > 0 && height > 0) {
            this.mHeight = height;
            this.mWidth = width;
        }
    }

    /**
     * 开始拍照
     */
    protected void start() {
        isOpenning = true;
        //3s 后开始启动相机
        mHandler.sendEmptyMessageDelayed(1, 3 * 1000);
    }

    /**
     * 停止拍照
     */
    protected void stop() {
        if (isViedo) {
            releaseMediaRecorder();
        } else {
//            releaseCarema();
            shootTimes = 0;
//            removeSmallWindow(mContext);
        }
    }

    /**
     * 拍照次数
     *
     * @param shootTimes
     */
    protected void setShootTimes(int shootTimes) {
        this.shootTimes = shootTimes;
    }

    /**
     * 拍照时间间隔
     *
     * @param shootInterval
     */
    protected void setShootInterval(int shootInterval) {
        this.shootInterval = shootInterval;
    }

    /**
     * 启动定时拍照并上传功能
     */
    private class CamremaHandler extends Handler {

        protected CamremaHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.v(TAG, "开始拍照");
                    initCarema();
                    break;
                case 2:
                    if (isViedo) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                initMediaRecorder();
                            }
                        }).start();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                                    @Override
                                    public void onAutoFocus(boolean success, Camera camera) {
                                        // 从Camera捕获图片
                                        Log.v(TAG, "自动聚焦:" + success);
                                        if (success) {
                                            mCamera.takePicture(null, null, mPicureCallback);
                                        } else {
                                            isCamera = false;
                                            if (mCallBack != null) {
                                                mCallBack.onFailure(AUTOFOCUS_FAILURE);
                                            }
                                        }
                                    }
                                });
                            }
                        }).start();
                    }
                    break;
                case 3:
                    if (isViedo) {
                        releaseMediaRecorder();
//                        if(cameraDialog != null){
//                            cameraDialog.cancel();
//                            cameraDialog = null;
//                        }
                    }
//                    else {
                    removeSmallWindow(mContext);
//                    }
                    break;
            }
        }
    }

    /**
     * 官方建议的安全地访问摄像头的方法
     **/
    protected Camera getCameraInstance() {
        if (checkCameraHardware(mContext)) {
            Camera c = null;
            try {
                c = Camera.open();
                c.setDisplayOrientation(90);
                if (isViedo) {
                    Camera.Parameters parameters = c.getParameters();
                    /**
                     * Camera自动对焦
                     */
                    List<String> focusModes = parameters.getSupportedFocusModes();
                    if (focusModes != null) {
                        for (String mode : focusModes) {
                            mode.contains("continuous-video");
                            parameters.setFocusMode("continuous-video");
                        }
                    }
                    c.setParameters(parameters);
                } else {
                    Camera.Parameters mParameters = c.getParameters();
                    //可以用得到当前所支持的照片大小，然后
                    List<Camera.Size> ms = mParameters.getSupportedPictureSizes();
                    //默认最大拍照取最大清晰度的照片
                    mParameters.setPictureSize(ms.get(0).width, ms.get(0).height);
                    c.setParameters(mParameters);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (mCallBack != null) {
                    mCallBack.onFailure(OPEN_FAILURE_CAMERA);
                }
                if (D) Log.d(TAG, "Error is " + e.getMessage());
            }
            return c;
        } else {
            if (mCallBack != null) {
                mCallBack.onFailure(NO_FIND_CAMERA);
            }
            return null;
        }
    }

    /**
     * 检查设备是否支持摄像头
     **/
    private boolean checkCameraHardware(Context mContext) {
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // 摄像头存在
            return true;
        } else {
            // 摄像头不存在
            return false;
        }
    }

    /**
     * 拍照回调接口
     **/
    private Camera.PictureCallback mPicureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] mData, Camera camera) {
            if (mData == null || mData.length < 0) {
                if (mCallBack != null) {
                    mCallBack.onFailure(PHOTOGRAPH_FAILURE);
                }
                return;
            }
            // 根据拍照所得的数据创建位图
            final Bitmap bitmap = BitmapFactory.decodeByteArray(mData, 0,
                    mData.length);
            Bitmap bm = handleImageEffect(bitmap, chroma, saturation, brightness, mWidth, mHeight, contrast);
            File file = new File(savePath + photoName + "_" + shootTimes + PHOTO_FORMAT_PNG);
            if (isSaveFile) {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                FileOutputStream outStream = null;
                try {
                    if (file.exists()) {
                        file.delete();
                        file.createNewFile();
                    } else {
                        file.createNewFile();
                    }
                    // 打开指定文件对应的输出流
                    outStream = new FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.JPEG, imageVideoQuality * 10, outStream);
                    outStream.flush();
                    bm.recycle();
                } catch (IOException e) {
                    if (mCallBack != null) {
                        mCallBack.onFailure(PHOTOGRAPH_FAILURE);
                    }
                    e.printStackTrace();
                } finally {
                    if (outStream != null) {
                        try {
                            outStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            Log.v(TAG, "获取照片！！》》》成功");
            bitmap.recycle();
            releaseCarema();
            isOpenning = false;
            shootTimes--;
            if (mCallBack != null)
                if (shootTimes < 1) {
                    mHandler.sendEmptyMessage(3);
                    if (mCallBack != null) {
                        if (isSaveFile)
                            mCallBack.onSuccess(shootTimes, file);
                        else
                            mCallBack.onSuccess(shootTimes, bm);
                        mCallBack.onFinish();
                    }
                    shootTimes = 0;
                } else {
                    if (mCallBack != null) {
                        if (isSaveFile)
                            mCallBack.onSuccess(shootTimes, file);
                        else
                            mCallBack.onSuccess(shootTimes, bm);
                    }
                    mHandler.sendEmptyMessageDelayed(1, shootInterval * 1000);
                }
        }
    };

    /**
     * 初始化相机参数
     */
    private void initCarema() {
        Log.v(TAG, "initCarema");
        if (mCamera == null) {
            Log.v(TAG, "camera=null");
            mCamera = getCameraInstance();
        }
        Log.v(TAG, mCamera == null ? "mCamera is null" : "mCamera is not null");
        if (mCamera != null && isCamera == false) {
            mPreview = new CameraPreview(mContext, mCamera);
//            mCamera.setDisplayOrientation(20);
//            if(isViedo){
//                cameraDialog = new CameraDialog(mContext);
//                cameraDialog.show();
//                mSurfaceViewFrame = cameraDialog.getCamera_view();
//            }else {
            removeSmallWindow(mContext);
            createSmallWindow(mContext);
//            }
            mSurfaceViewFrame.removeAllViews();
            mSurfaceViewFrame.addView(mPreview);
            mSurfaceViewFrame.setVisibility(View.INVISIBLE);
            mHandler.sendEmptyMessageDelayed(2, 3 * 1000); //3s后拍照
            isCamera = true;
            if (mCallBack != null) {
                mCallBack.openSuccess();
            }
        } else {
            if (isCamera == false) {
                Log.e(TAG, "获取相机失败");
                if (mCallBack != null) {
                    mCallBack.onFailure(OPEN_FAILURE_CAMERA);
                }
            } else {
                Log.e(TAG, "获取相机真正拍照中请稍后再拍！");
                if (mCallBack != null) {
                    mCallBack.onFailure(PHOTOGRAPH_ING);
                }
            }
        }
    }

    /**
     * 清除缓存，关闭相机
     */
    protected void releaseCarema() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            isCamera = false;
        }
    }

    protected interface CameraCallBack {

        /**
         * 相机打开成功
         */
        void openSuccess();

        /**
         * 拍照失败
         *
         * @param error
         */
        void onFailure(int error);

        /**
         * 拍照成功
         *
         * @param surplusTimes 剩余张数
         */
        void onSuccess(int surplusTimes, File file);

        /**
         * 拍照成功
         *
         * @param surplusTimes 剩余张数
         */
        void onSuccess(int surplusTimes, Bitmap bitmap);

        void onFinish();
    }

    /**
     * 照片修改
     *
     * @param bm         图像 （不可修改）
     * @param hue        色相
     * @param saturation 饱和度
     * @param lum        亮度
     * @param width      宽度
     * @param height     高度
     * @param contrast   对比度
     * @return
     */
    private Bitmap handleImageEffect(Bitmap bm, float hue, float saturation,
                                     float lum, int width, int height, float contrast) {

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmp);
        // 把位图输出到指定文件中
        Matrix matrix = new Matrix();
        matrix.setScale(
                (float) bmp.getWidth() / (float) bm.getWidth(),
                (float) bmp.getHeight() / (float) bm.getHeight());// 注意参数一定是float哦
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //色调
        ColorMatrix hueMatrix = new ColorMatrix();
        hueMatrix.setRotate(0, (hue - MID_VALUE) * 1.0F / MID_VALUE * 180); // R
        hueMatrix.setRotate(1, (hue - MID_VALUE) * 1.0F / MID_VALUE * 180); // G
        hueMatrix.setRotate(2, (hue - MID_VALUE) * 1.0F / MID_VALUE * 180); // B
        //饱和度
        ColorMatrix saturationMatrix = new ColorMatrix();
        saturationMatrix.setSaturation(saturation * 1.0F / MID_VALUE);
        //亮度
        ColorMatrix lumMatrix = new ColorMatrix();
        lumMatrix.setScale(lum * 1.0F / MID_VALUE, lum * 1.0F / MID_VALUE, lum * 1.0F / MID_VALUE, 1);

        //对比度
        float c = (float) ((contrast + 64) / 128.0);
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[]{
                c, 0, 0, 0,
                0, 0, c, 0,
                0, 0, 0, 0,
                c, 0, 0, 0,
                0, 0, 1, 0});
        //融合
        ColorMatrix imageMatrix = new ColorMatrix();
        imageMatrix.postConcat(hueMatrix);
        imageMatrix.postConcat(saturationMatrix);
        imageMatrix.postConcat(lumMatrix);
        imageMatrix.postConcat(cMatrix);
        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(bm, matrix, paint);
        return bmp;
    }

    private void initMediaRecorder() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
        }
        if (mCamera != null) {
            mCamera.unlock();
            mRecorder.setCamera(mCamera);
        }
        try {
//            //设置视频源
//            mRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
//            //设置音频源
//            mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
//            //设置文件输出格式
//            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);

//     mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//            //音频编码格式对应应为AAC
//            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            //视频编码格式对应应为H264
            mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

//            //设置预览画面
//            mRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
            //设置输出路径
            mRecorder.setAudioChannels(1);
//            mRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());//预览显示的控件
            File file = new File(savePath + photoName + VIDEO_FORMAT_MP4);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            //设置视频高和宽,注意文档的说明:
            //Must be called after setVideoSource().
            //Call this after setOutFormat() but before prepare().
            mRecorder.setVideoSize(mWidth, mHeight);
            //设置录制的视频帧率,注意文档的说明:
            //Must be called after setVideoSource().
            //Call this after setOutFormat() but before prepare().
            mRecorder.setVideoFrameRate(20);
            // Step 5: Set the preview output
//            //设置视频编码方式
//            mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
//            //设置音频编码方式
//            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mRecorder.setOutputFile(file.getAbsolutePath());//输出文件路径
            mRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
//            try {
//                mRecorder.reset();
//            }catch (RuntimeException e){
//                e.printStackTrace();
//            }
            try {
                mRecorder.prepare();//准备
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();//开始
            isRecording = true;//录像开始
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHandler.sendEmptyMessageDelayed(3, shootInterval * 1000);
    }

    private void releaseMediaRecorder() {
        if (mRecorder != null && isRecording) {
            mRecorder.stop();//停止
            mRecorder.reset();//重置，设置为空闲状态
            mRecorder.release();//释放
            mRecorder = null;
            //释放相机
            if (mCamera != null) {
                mCamera = null;
                isCamera = false;
            }
        }
        isRecording = false;
    }

}
