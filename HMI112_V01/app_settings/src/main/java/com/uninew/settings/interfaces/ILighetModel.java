package com.uninew.settings.interfaces;

/**
 * Created by Administrator on 2017/8/31.
 */

public interface ILighetModel {

    /**
     * 判断是否开启了自动亮度调节
     */
    boolean isAutoBrightness();

    /**
     * 获取屏幕亮度
     */
    int getScreenBrightness();

    /**
     * 设置亮度 0-255
     */
    void setBrightness(int brightness);

    /**
     * 修改亮度模式
     * @param mode 1:自动调节  0：手动调节
     */
    void setAutoBrightness(int mode);
}
