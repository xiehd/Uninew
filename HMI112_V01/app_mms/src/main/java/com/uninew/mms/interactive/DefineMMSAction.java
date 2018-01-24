package com.uninew.mms.interactive;


public interface DefineMMSAction {
    /** 向MCU发送数据 */
    String MCULinkSendData = "Com.MCULink.SendData";

    /** 从MCU接受数据 */
    String MCULinkReceiveData = "Com.MCULink.ReceiveData";

    /** MCULink状态 */
    String MCULinkState = "Com.MCULink.ReceiveData";

    /** 发送键盘值 */
    String MCULinkKeys = "uninew.key";

    /** 发送信息 */
    String TCPLinkSendData = "Com.TCPLink.SendData";


    /** MCU版本查询 */
    String MCULinkVersionRequest = "Com.MCULink.Version.Request";

    /** ACC状态通知 */
    String MCULinkAccState = "Com.MCULink.AccState";

    /**  MCU版本查询应答 */
    String MCULinkVersionResponse = "Com.MCULink.Version.Response";

    /** MCU升级 */
    String MCULinkUpdateCommand="Com.MCULink.Update.Command";

    /** OS升级通知 */
    String MCULinkOSUpdateNotify = "Com.MCULink.OSUpdate.Notify";

    /**  OS升级通知应答 */
    String MCULinkOSUpdateResponse = "Com.MCULink.OSUpdate.Response";

    /**  参数设置 */
    String MCULinkParamTypeSet = "Com.MCULink.ParamType.Set";
    /**  参数查询 */
    String MCULINKQUERYMCUPARAM = "Com.MCULink.ParamType.Query";

    /** 定位状态 */
    String LocationState = "Com.LocationService.RespondState";
    /** 通讯状态 */
    String ServerLinkState = "Com.ServerLink.State";


    /**
     * MCU版本信息
     *
     * @author Administrator
     *
     */
    public interface MCUVersion {
        /** 线路名称*/
        String KEY_VERSION = "version";
    }

    /**
     * ACC状态信息
     *
     * @author Administrator
     *
     */
    public interface AccState {
        /** 状态*/
        String KEY_STATE = "state";
    }

    /**
     * 参数设置/参数查询
     *
     * @author Administrator
     *
     */
    public interface ParamType {

        String PARAM_TYPE = "type";
        String PARAM_VALUE = "value";
    }

}
