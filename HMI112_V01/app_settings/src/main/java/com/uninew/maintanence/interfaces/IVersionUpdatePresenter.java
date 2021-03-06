package com.uninew.maintanence.interfaces;

/**
 * Created by Administrator on 2017/9/12.
 */

public interface IVersionUpdatePresenter {
    /**
     * 升级应用
     */
    void UpdateApp();
    /**
     * 升级OS
     */
    void UpdateOS();
    /**
     * 升级MCU
     */
    void UpdateMCU();
    /**
     * 升级摄像机
     */
    void UpdateDVR();
    /**
     * 升级空车屏
     */
    void UpdateCarSrceen();
    /**
     * 升级地图
     */
    void UpdateMap();
    /**
     * 升级高德地图
     */
    void UpdateGaoDe();
    /** *******************************下传****************************************/

    /**
     * 显示当前应用版本
     */
    void ShowAppVersion(String version);
    /**
     * 显示当前OS版本
     */
    void ShowOSVersion(String version);
    /**
     * 显示当前MCU版本
     */
    void ShowMCUVersion(String version);
    /**
     * 显示当前摄像机版本
     */
    void ShowDVRVersion(String version);
    /**
     * 显示当前空车屏版本
     */
    void ShowCarSrceenVersion(String version);
    /**
     * 显示当前地图版本
     */
    void ShowMapVersion(String version);
    /**
     * 显示当前高德版本
     */
    void ShowGaoDeVersion(String version);
}
