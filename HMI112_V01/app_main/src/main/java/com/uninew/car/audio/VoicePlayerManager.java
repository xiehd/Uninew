package com.uninew.car.audio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * Created by rong on 2017-06-21.
 */

public class VoicePlayerManager {

    private static final String TAG = "VoicePlayerManager";
    private static final boolean D = true;
    private static volatile VoicePlayerManager instance;
    private MediaPlayer mPlayer;
    private boolean isRelease = false;//清除数据状态
    private List<String> mVoicePaths;//播放列表
    private boolean isPlaying = false;//播放状态
    private int currentPlaying = 0;//播放文件列表中的第几个文件记录
    private OnVoiceCompletionListener mVoiceCompletionListener;//播放单个文件完成监听器
    private OnVoiceErrorListener mVoiceErrorListener;//播放单个文件异常监听器
    private boolean isPause = false;//暂停状态
    private int curentPosition = 0;//播放进度
    private OnVoicePreparedListener mVoicePreparedListener;//播放单个文件准备完成监听器
    private OnVoicePlayingListener mVoicePlayingListener;//自定义播放文件监听器
    private boolean isStop = false;//停止状态
    private int audioStreamType = AudioManager.STREAM_MUSIC;

    private VoicePlayerManager() {
        isRelease = true;
        initVoice();
    }

    /**
     * Sets the audio stream type for this MediaPlayer. See {@link AudioManager}
     * for a list of stream types. Must call this method before prepare() or
     * prepareAsync() in order for the target stream type to become effective
     * thereafter.
     *
     * @param audioStreamType the audio stream type
     * @see android.media.AudioManager
     */
    public void setAudioStreamType(int audioStreamType) {
        this.audioStreamType = audioStreamType;
    }

    /**
     * 设置播放监听器
     *
     * @param voicePlayingListener
     */
    public void setOnVoicePlayingListener(OnVoicePlayingListener voicePlayingListener) {
        this.mVoicePlayingListener = voicePlayingListener;
    }

    /**
     * 播放监听器
     */
    public interface OnVoicePlayingListener {
        /**
         * 播放列表播放完成
         */
        void onCompletion();
    }

    public static VoicePlayerManager getInstance() {
        if (instance != null) {

        } else {
            synchronized (VoicePlayerManager.class) {
                if (instance == null) {
                    instance = new VoicePlayerManager();
                }
            }
        }
        return instance;
    }

    /**
     * 设置播放文件
     *
     * @param voicePaths
     */
    public void setVoice(List<String> voicePaths) {
        this.mVoicePaths = voicePaths;
        curentPosition = 0;
        currentPlaying = 0;
    }


    /**
     * 开始播放
     */
    public void player() {
        if (mVoicePaths != null && !mVoicePaths.isEmpty() && isPlaying == false) {
            try {
                reSet();
                if (D)
                    Log.d(TAG, mVoicePaths.get(currentPlaying) + ",currentPlaying:" + currentPlaying);
                mPlayer.setAudioStreamType(audioStreamType);
                mPlayer.setDataSource(mVoicePaths.get(currentPlaying));
                mPlayer.prepareAsync();
            } catch (IOException e) {//文件出错异常
                e.printStackTrace();
                nextPlayer();
            } catch (Exception e) {//播放异常
                e.printStackTrace();
                nextPlayer();
            }
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (isPlaying == true && isPause == false) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                curentPosition = mPlayer.getCurrentPosition();
                isPause = true;
                isPlaying = false;
            }
        }
    }

    /**
     * 暂停后重新播放
     */
    public void rePlaying() {
        if (isPause == true) {
            mPlayer.seekTo(curentPosition);
            mPlayer.start();
            isPlaying = true;
        }
    }

    /**
     * 重播
     */
    public void reReadVoice() {
        if (isPlaying == true || isPause == true) {
            stopPlayer();
        }
        curentPosition = 0;
        currentPlaying = 0;
        player();
    }

    /**
     * 停止播放
     */
    public void stopPlayer() {
        if (D) Log.d(TAG, "播放停止");
        if (isPlaying == true) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            isPlaying = false;
        }
    }

    /**
     * 播放下一个文件
     */
    private void nextPlayer() {
        currentPlaying++;
        if (D) Log.d(TAG, "currentPlaying:" + currentPlaying);
        if (mVoicePaths != null && currentPlaying < mVoicePaths.size() && isStop == false) {
            stopPlayer();
            curentPosition = 0;
            player();
        } else {
            stopPlayer();
            curentPosition = 0;
            currentPlaying = 0;
            if (mVoicePlayingListener != null) {
                mVoicePlayingListener.onCompletion();
            }
        }
    }

    /**
     * 单文件播放完成
     */
    private class OnVoiceCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            if (D) Log.e(TAG, "播放完成！！");
            if (isStop == false) {
                nextPlayer();
            }else {
                stopPlayer();
                curentPosition = 0;
                currentPlaying = 0;
                if (mVoicePlayingListener != null) {
                    mVoicePlayingListener.onCompletion();
                }
            }
        }
    }

    /**
     * 播放出错
     */
    private class OnVoiceErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            if (D) Log.e(TAG, "播放出错了！！" + "What:" + what + ",extra:" + extra);
            stopPlayer();//进行stop方法后，就会进入到OnCompletionListener。
            return false;
        }
    }

    /**
     * 在调用了prepareAsync方法后就会进入该监听器
     * 在该监听器中进行开始播放
     */
    private class OnVoicePreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            try {
                mPlayer.seekTo(curentPosition);
                mPlayer.start();
            } catch (IllegalStateException e) {
                nextPlayer();
                e.fillInStackTrace();
            }
        }
    }

    /**
     * 初始化MediaPlayer
     */
    public void initVoice() {
        if (isRelease) {
            mPlayer = new MediaPlayer();
            mVoiceCompletionListener = new OnVoiceCompletionListener();
            mVoiceErrorListener = new OnVoiceErrorListener();
            mVoicePreparedListener = new OnVoicePreparedListener();
            mPlayer.setOnCompletionListener(mVoiceCompletionListener);
            mPlayer.setOnErrorListener(mVoiceErrorListener);
            mPlayer.setOnPreparedListener(mVoicePreparedListener);
            isRelease = false;
        }
    }

    /**
     * 清空MediaPlayer
     */
    public void release() {
        if (D) Log.d(TAG, "播放结束");
        if (isRelease == false) {
//            mVoicePreparedListener = null;
//            mVoiceErrorListener = null;
//            mVoiceCompletionListener = null;
            mVoicePaths = null;
            try {
                if (D) Log.d(TAG, "播放结束1");
                mPlayer.release();
                mPlayer = null;
                isRelease = true;
                if (D) Log.d(TAG, "播放结束2");
            } catch (Exception e) {
                e.printStackTrace();
                isRelease = false;
            }
        }
        if (D) Log.d(TAG, "播放结束3");
    }

    /**
     * 重新播放调用
     */
    public void reSet() {
        try {
            mPlayer.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否清除MediaPlayer
     *
     * @return
     */
    public boolean isRelease() {
        return isRelease;
    }

    /**
     * 是否正在播放
     *
     * @return
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * 是否处于暂停状态
     *
     * @return
     */
    public boolean isPause() {
        return isPause;
    }

    /**
     * 播放是否处于停止状态
     *
     * @return
     */
    public boolean isStop() {
        return isStop;
    }

    /**
     * 设置当前播发状态
     *
     * @param stop
     */
    public void setStop(boolean stop) {
        isStop = stop;
    }
}
