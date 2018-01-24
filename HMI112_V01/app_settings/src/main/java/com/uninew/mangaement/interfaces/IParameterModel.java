package com.uninew.mangaement.interfaces;

/**
 * Created by Administrator on 2017/9/13.
 */

public interface IParameterModel {

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

}
