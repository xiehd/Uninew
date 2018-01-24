package com.uninew.settings.interfaces;

/**
 * Created by Administrator on 2017/9/12.
 */

public interface IHostView {

    /**
     * 显示热点开关
     * @param state  true：开；false：关
     */
    void ShowHostOFF(boolean state,String name,String passworld);

    /**
     * 显示热点连接人数
     * @param number  连接个数
     */
    void ShowHostConnectNuber(int number);
}
