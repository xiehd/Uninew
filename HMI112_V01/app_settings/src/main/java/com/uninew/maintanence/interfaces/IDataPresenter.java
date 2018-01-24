package com.uninew.maintanence.interfaces;

/**
 * Created by Administrator on 2017/9/12.
 */

public interface IDataPresenter {

    /**
     * 数据导入
     *
     * @param id 数据id
     *           0：司机信息
     *           1：参数导入
     */
    void DataImport(int id);

    /**
     * 数据导出
     *
     * @param id 数据id
     *           1：参数导出
     *           2：日志导出
     *           3：运营数据导出
     *           4：签到签退导出
     *           5：报警数据导出
     */
    void DataExport(int id);

    /**
     * 清空数据空
     */
    void EmptyDB();

    /**
     * 参数恢复
     */
    void DataRecovery();

    void stop();

    void start();
}
