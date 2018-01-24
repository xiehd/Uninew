package com.uninew.settings.interfaces;

/**
 * Created by Administrator on 2017/9/12.
 */

public interface ILightView {

    /*************************下传***********************************/
    /**
     * 显示亮度调节模式
     *
     * @param mode true：自动；false：手动
     */
    void ShowTochLight(boolean mode);

    /**
     * 显示当前亮度值
     *
     * @param light
     */
    void ShowCurrentLight(int light);
}
