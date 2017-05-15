package com.uninew.ftp;

/**
 * Created by rong on 2017-04-14.
 */

public enum FTPState {
    /** 连接成功**/
    connect,
    /** 断开连接*/
    disConnect,
    /** 连接出错**/
    connectError,
    /** 开始上传**/
    startUpdate,
    /** 上传文件**/
    UpdateFile,
    /** 上传出错**/
    UpdateError,
    /** 上传完成 **/
    FinishUpdate,
    /** 开始下载**/
    startDownLoad,
    /** 下载文件**/
    DownLoadFile,
    /** 下载完成 **/
    FinishDownLoad,
    /** 下载出错**/
    DownLoadError,
}
