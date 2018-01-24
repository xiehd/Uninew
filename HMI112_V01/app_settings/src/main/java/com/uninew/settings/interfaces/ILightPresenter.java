package com.uninew.settings.interfaces;

/**
 * Created by Administrator on 2017/9/1.
 */

public interface ILightPresenter {

    /**
     * 修改亮度模式
     * @param mode 1:自动调节  0：手动调节
     */
    void setAutoBrightness(int mode);

    /**
     * 亮度自加
     */
    void addLight();

    /**
     * 亮度自减
     */
    void subLight();

    /**
     * 修改亮度值
     * @param light
     */
    void setLight(int light);

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
