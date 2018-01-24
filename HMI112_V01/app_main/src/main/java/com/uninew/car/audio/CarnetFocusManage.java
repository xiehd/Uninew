package com.uninew.car.audio;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/3 0003.
 */

public class CarnetFocusManage implements VoicePlayerManager.OnVoicePlayingListener {

    private static final String TAG = "CarnetFocusManage";
    /**
     * Google 原生申请声音焦点
     */
    private AudioManager	am;
    private static volatile CarnetFocusManage INSTANCE;
    private Context mContext;
    private boolean isTTS = false;
    private TtsUtil ttsUtil;
    private VoicePlayerManager playerManager;
    private boolean isPlaying = false;
    private boolean isPausing = false;

    private CarnetFocusManage(Context context){
        this.mContext = context;
        ttsUtil = TtsUtil.getInstance(context);
        playerManager = VoicePlayerManager.getInstance();
        ttsUtil.setOnVoicePlayingListener(this);
        playerManager.setOnVoicePlayingListener(this);
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
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

                    break;

                case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
                    // 暂时失去Audio Focus，并会很快再次获得
                    //例如：打完电话后恢复播放
                    Log.i(TAG, "暂时失去Audio Focus，并会很快再次获得");
                    pausePlay();
//                    isPlayMusic = false;
                    break;
                case (AudioManager.AUDIOFOCUS_LOSS):
                    // 失去了Audio Focus，并将会持续很长的时间
                    //例如：打开收音机，当前会失去焦点，只有重新进入播放界面
                    Log.i(TAG, "失去了Audio Focus，并将会持续很长的时间");
                    pausePlay();
//                    isPlayMusic = false;
                    break;

                case (AudioManager.AUDIOFOCUS_GAIN):
                    // 获得了Audio Focus
                    //打完电话后，再次通知获得焦点并播放
                    Log.i(TAG, "获得了Audio Focus");
//                    isPlayMusic = true;
                    //播放
//                    play();
                    resumePlay();

                    break;
                default:
                    break;
            }

        }

    };

    public void stop(){
        // TODO Auto-generated method stub
        isPlaying =false;
        if(isTTS){
            ttsUtil.stopSpeak();
        }else {
            playerManager.setStop(true);
            playerManager.stopPlayer();
        }
        //释放声音焦点
        am.abandonAudioFocus(mAudioFocusListener);
    }

    /**
     * 暂停播放
     */
    private void pausePlay(){
        if(isPlaying && isPausing == false){
            if(isTTS){
                ttsUtil.pauseSpeaking();
            }else {
                playerManager.pause();
            }
            isPausing = true;
        }
    }

    private void resumePlay(){
        if(isPausing){
            isPausing = false;
            if(isTTS){
                ttsUtil.resumeSpeaking();
            }else {
                playerManager.rePlaying();
            }
        }
    }

    /**
     * 申请声音焦点后，播放
     */
    public void play(String... src){

        if(src != null && src.length > 0) {
            // 播放的时候申请焦点
            int isRequestSucess = am.requestAudioFocus(mAudioFocusListener, AudioManager.STREAM_ALARM, AudioManager.AUDIOFOCUS_GAIN);
            Log.i(TAG, "播放的时候申请焦点=" + isRequestSucess);
            if (isRequestSucess == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
                Log.i(TAG, "申请焦点失败，无法播放");
                return;
            }
            Log.i(TAG, "申请焦点成功，即将播放");
            if(isTTS){
                Log.i(TAG, "tts播放");
                ttsUtil.speak(src[0]);
            }else {
                Log.i(TAG, "媒体播放");
                List<String> paths = new ArrayList<>();
                for (String s:src){
                    paths.add(s);
                }
                playerManager.setVoice(paths);
                playerManager.player();
            }
            isPlaying = true;
        }
    }

    @Override
    public void onCompletion() {
        isPlaying = false;
    }
}
