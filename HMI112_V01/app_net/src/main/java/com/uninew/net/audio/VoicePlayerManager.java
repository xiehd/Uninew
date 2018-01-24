package com.uninew.net.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * Created by rong on 2017-06-21.
 */

public class VoicePlayerManager {

    private static final String TAG = "AudioFocusChangeListenerM";
    private static final boolean D = true;
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

    /**
     * Google 原生申请声音焦点
     */
    private static AudioManager	am;


    public VoicePlayerManager(Context context) {
        isRelease = true;
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        initVoice();
    }

    /**
     * 实现音频焦点监听器，如果有其他竞争者一起争夺AudioFocus，通过监听AudioFocus，APP这方做出相应的变化
     */
    private AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {

        public void onAudioFocusChange(int focusChange){

            Log.i(TAG, "onAudioFocusChange focusChange=" + focusChange);

            switch (focusChange) {
                case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
                    // 暂时失去AudioFocus，但是可以继续播放
                    Log.i(TAG, "获得了Audio Focus，AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    break;

                case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
                    // 暂时失去Audio Focus，并会很快再次获得
                    //例如：打完电话后恢复播放
                    Log.i(TAG, "暂时失去Audio Focus，并会很快再次获得");
                    pause();
                    break;

                case (AudioManager.AUDIOFOCUS_LOSS):
                    // 失去了Audio Focus，并将会持续很长的时间
                    //例如：打开收音机，当前会失去焦点，只有重新进入播放界面
                    Log.i(TAG, "失去了Audio Focus，并将会持续很长的时间");
                    pause();
                    break;
                case (AudioManager.AUDIOFOCUS_GAIN):
                    // 获得了Audio Focus
                    //打完电话后，再次通知获得焦点并播放
                    Log.i(TAG, "获得了Audio Focus，AUDIOFOCUS_GAIN");
                    rePlaying();
                    break;
                case (AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK):
                    Log.i(TAG, "获得了AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK");
                    rePlaying();
                    break;
                case (AudioManager.AUDIOFOCUS_GAIN_TRANSIENT):
                    Log.i(TAG, "获得了AUDIOFOCUS_GAIN_TRANSIENT");
//                    rePlaying();
                    break;
                case (AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE):
                    Log.i(TAG, "获得了AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE");
//                    rePlaying();
                    break;
//                case (AudioManager.AUDIOFOCUS_REQUEST_FAILED):
//                    Log.i(TAG, "获得了AUDIOFOCUS_REQUEST_FAILED");
////                    rePlaying();
//                    break;
//                case (AudioManager.AUDIOFOCUS_REQUEST_GRANTED):
//                    Log.i(TAG, "获得了AUDIOFOCUS_REQUEST_GRANTED");
////                    rePlaying();
//                    break;
                default:
                    break;
            }

        }

    };

    /**
     * Sets the audio stream type for this MediaPlayer. See {@link AudioManager}
     * for a list of stream types. Must call this method before prepare() or
     * prepareAsync() in order for the target stream type to become effective
     * thereafter.
     *
     * @param audioStreamType the audio stream type
     * @see AudioManager
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


    /**
     * 设置播放文件
     *
     * @param voicePaths
     */
    public void setVoice(List<String> voicePaths) {
        stop();
        this.mVoicePaths = voicePaths;
        curentPosition = 0;
        currentPlaying = 0;
    }


    /**
     * 开始播放
     */
    public void player() {
        // 播放的时候申请焦点
        int isRequestSucess = am.requestAudioFocus(mAudioFocusListener,audioStreamType,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        Log.i(TAG, "播放的时候申请焦点=" + isRequestSucess);

        if (isRequestSucess == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
            Log.i(TAG, "申请焦点失败，无法播放");
            return;
        }
        Log.i(TAG, "申请焦点成功，即将播放");
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
     * 停止播放
     */
    public void stop(){
        this.setStop(true);
        this.stopPlayer();
        this.release();
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (isPlaying == true && isPause == false) {
            if (mPlayer.isPlaying()) {
                Log.d(TAG,"暂停播放");
                mPlayer.pause();
                curentPosition = mPlayer.getCurrentPosition();
                isPause = true;
            }
        }
    }

    /**
     * 暂停后重新播放
     */
    public void rePlaying() {
        if (isPause == true && mPlayer != null) {
            Log.d(TAG,"暂停后重新播放");
            mPlayer.seekTo(curentPosition);
            mPlayer.start();
            isPause = false;
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
            int isRequestSucess = am.abandonAudioFocus(mAudioFocusListener);
            if (isRequestSucess == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
                Log.i(TAG, "释放声音焦点失败，无法播放");
            }else {
                Log.i(TAG, "释放声音焦点成功，即将播放");
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
                //释放声音焦点
                am.abandonAudioFocus(mAudioFocusListener);
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
                isPlaying  = true;
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
            if(mPlayer == null){
               initVoice();
            }
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
