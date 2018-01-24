package com.uninew.maintanence.presenter;


import com.uninew.bean.AppInfo;

import java.util.List;

/**
 * apk升级请求回调接口
 *
 * @author lusy
 *         创建日期:2017-1-12
 */
public interface IApkCheckCallBack {
    /**
     * @param installAppInfos 已安装的apk集合
     * @param updateAppInfos  apk更新包集合
     */
    public void apkCheckCallBack(List<AppInfo> installAppInfos, List<AppInfo> updateAppInfos);

}
