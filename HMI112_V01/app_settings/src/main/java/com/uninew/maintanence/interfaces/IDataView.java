package com.uninew.maintanence.interfaces;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public interface IDataView {
    /**
     * 数据操作成功
     *
     * @param type 数据id
     *           1：参数导出
     *           2：日志导出
     *           3：运营数据导出
     *           4：签到签退导出
     *           5：报警数据导出
     */
    void onSuccess(int type);

    /**
     * 数据操作失败
     *
     * @param type 数据id
     *           1：参数导出
     *           2：日志导出
     *           3：运营数据导出
     *           4：签到签退导出
     *           5：报警数据导出
     */
    void onFailure(int type,int error);
}
