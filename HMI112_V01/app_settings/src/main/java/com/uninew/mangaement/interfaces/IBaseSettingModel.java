package com.uninew.mangaement.interfaces;

/**
 * Created by Administrator on 2017/9/12.
 */

public interface IBaseSettingModel {
    /* *
     *保存数据
     * @param carNumber       车牌号
     * @param terminal        终端号
     * @param companyName     公司名称
     * @param videoNumber      录像主机序列号
     * @param timeOuet          操作超时退出时间
     * @param pinter            打印机灵敏度
     * @param Pretime           预录时间
     * @param delaytime         延录时间
     *
     * */

    void SaveInitData(String carNumber,String terminal,String companyName,
                      String videoNumber,String timeOuet,String pinter,String Pretime,String delaytime);

    /**
     * 注册
     * @param terminal 终端号
     */
    void SetRegister(String terminal);
    /**
     * 恢复默认
     */
    void Setdefault();
}
