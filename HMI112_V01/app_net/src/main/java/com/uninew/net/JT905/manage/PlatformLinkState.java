package com.uninew.net.JT905.manage;

public enum PlatformLinkState {

    /**
     * 正常
     */
    NORMAL,
    /**
     * 无网络
     */
    ERROR_NO_NET,

    /**
     * 人为关闭
     */
    ERROR_HAND_CLOSED,
    /**
     * 服务器断开监听/注销
     */
    ERROR_SERVER_CLOSED,
    /**
     * 未知异常
     */
    ERROR_OTHERS,

}
