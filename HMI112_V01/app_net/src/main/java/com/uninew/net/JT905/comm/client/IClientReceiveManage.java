package com.uninew.net.JT905.comm.client;

public interface IClientReceiveManage {

    /**
     * 修改监听类型 true mms；false：net
     * 默认net
     * @param actionType
     */
    void SetAction(boolean actionType);

    /**
     * 监听连接状态
     */
    public void registerConnectStateListener(
            IClientReceiveListener.IConnectListener mConnectListener);

    /**
     * 注销连接状态监听
     */
    public void unRegisterConnectStateListener();


    /**
     * 电召服务数据监听
     */
    public void registerCallListener(
            IClientReceiveListener.ICallListener callListener);

    /**
     * 注销电召服务数据监听
     */
    public void unRegisterCallListener();

    /**
     * 消息服务数据监听
     */
    public void registerMsgListener(
            IClientReceiveListener.IMsgListener msgListener);

    /**
     * 注销消息服务数据监听
     */
    public void unRegisterMsgListener();


    /**
     * 通话服务数据监听
     */
    public void registerPhoneListener(
            IClientReceiveListener.IPhoneListener phoneListener);

    /**
     * 通话服务数据监听
     */
    public void unRegisterPhoneListener();

    /**
     * 考勤服务数据监听
     */
    public void registerPushCardListener(
            IClientReceiveListener.IPushCardListener pushCardListener);

    /**
     * 注销考勤服务数据监听
     */
    public void unRegisterPushCardListener();

    /**
     * 运营数据监听
     */
    public void registerOperationListener(
            IClientReceiveListener.IOperationListener operationListener);

    /**
     * 注销运营数据监听
     */
    public void unRegisterOperationListener();

    /**
     * MMS数据监听
     */
    public void registerMmsListener(
            IClientReceiveListener.IMmsDataListener mmsDataListener);

    /**
     * 注销MMS数据监听
     */
    public void unRegisterMmsListener();

    /**
     * 注册计价器状态查询监听
     */
    public void registerTaxiStateListener(
            IClientReceiveListener.ITaxiStateListener mTaxiSateListener);

    /**
     * 注销计价器状态查询监听
     */
    public void unRegisterTaxiSateListener();

    /**
     * 注册计价器运营状态监听
     */
    public void registerTaxiOperateStateListener(
            IClientReceiveListener.ITaxiOperateStateListener mTaxiOperateStateListener);

    /**
     * 注销计价器运营状态监听
     */
    public void unRegisterTaxiOperateSateListener();

    /**
     * 注册计价器心跳状态监听
     */
    public void registerTaxiHeartStateListener(
            IClientReceiveListener.ITaxiHeartStateLister mTaxiHeartStateLister);

    /**
     * 注销计价器心跳状态监听
     */
    public void unRegisterTaxiHeartSateListener();

    /**
     * 注册计价器运价参数查询监听
     */
    public void registerTaxiTaxiFreightLister(
            IClientReceiveListener.ITaxiTaxiFreightLister mTaxiTaxiFreightLister);

    /**
     * 注销计价器运价参数查询监听
     */
    public void unRegisterTaxiFreightLister();




}
