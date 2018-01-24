package com.uninew.settings.interfaces;

/**
 * Created by Administrator on 2017/9/12.
 */

public interface IVoiceView {

    /**
     * 显示触屏音调节模式
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
