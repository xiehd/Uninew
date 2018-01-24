package com.uninew.mangaement.interfaces;

import com.uninew.net.JT905.common.IpInfo;

/**
 * Created by Administrator on 2017/9/13.
 */

public interface IParameterPresenter {

    /**
     * 打开平台监听
     */
    void registerConnectStateListener();
    /**
     * 注册平台监听
     */
    void unregisterConnectStateListener();

    /**
     * @param tcpid    平台编号 0x00,0x01
     * @param mainIp    主服务器IP
     * @param mainPort  主服务器端口
     * @param spareIp   备用服务器IP
     * @param sparePort 备用服务器端口
     */
    void createLink(int tcpid,String mainIp,int mainPort,String spareIp,int sparePort);

    /**
     * @param tcpId 平台编号 0x00,0x01
     */
    void disConnectLink(int tcpId);

    /**
     * @param tcpId 平台编号 0x00,0x01
     */
    void queryLinkState(int tcpId);

    /**
     * 保存数据到数据库
     * @param serverAddress
     * @param serverPort
     * @param serverPareAddress
     * @param serverParePort
     */
    void saveServerDB(int tcpID,String serverAddress,int serverPort,String serverPareAddress,int serverParePort);
    /**
     * 域名解析连接
     * @param name
     */
    void getNameConnectLink(String name);

    /**************************下传****************************************/

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
