package com.uninew.net.Alarm.interfaces;

/**
 * 拍照录音监听
 * Created by Administrator on 2018/1/19.
 */

public interface IAudioManager {

    interface IAudioListener {
        /**
         * 开始录音
         */
        void starAudio();
        /**
         * 关闭录音
         */
        void stopAudio();
    }

    interface IVideoListener {
        /**
         * 开始拍照
         */
        void starVideo();
        /**
         * 取消拍照
         */
        void stopVideo();
    }


}
