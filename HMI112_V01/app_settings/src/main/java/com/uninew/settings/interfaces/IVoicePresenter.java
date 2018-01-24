package com.uninew.settings.interfaces;

/**
 * Created by Administrator on 2017/9/12.
 */

public interface IVoicePresenter {

    /**
     * 触屏声
     *
     * @param mode true：打开；false：关闭
     */
    void setTochVoice(boolean mode);

    /**
     * 音量自加
     */
    void addVoice();

    /**
     * 音量自减
     */
    void subVoice();

    /**
     * 设置音量
     */
    void setVoice(int voice);

    /*************************下传***********************************/
    /**
     * 显示触屏调节模式
     *
     * @param mode true：打开；false：关闭
     */
    void ShowTochVoice(boolean mode);

    /**
     * 显示当前音量
     *
     * @param voice
     */
    void ShowCurrentVoice(int voice);

}
