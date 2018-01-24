package com.uninew.mangaement.interfaces;

import com.uninew.net.JT905.common.IpInfo;

/**
 * Created by Administrator on 2017/9/13.
 */

public interface IParameterView {
    /**
     * @param ipInfo 连接状态显示
     */
    void ShowlinkStateNotify(IpInfo ipInfo);

    /**
     * 显示服务器1信息
     * @param address 地址
     * @param port   端口
     */
    void ShowServer1(String address,String port);
    /**
     * 显示备用服务器1信息
     * @param address 地址
     * @param port   端口
     */
    void ShowSpareServer1(String address,String port);
    /**
     * 显示服务器2信息
     * @param address 地址
     * @param port   端口
     */
    void ShowServer2(String address,String port);
    /**
     * 显示备用服务器2信息
     * @param address 地址
     * @param port   端口
     */
    void ShowSpareServer2(String address,String port);
}
