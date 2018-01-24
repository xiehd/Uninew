package com.uninew.mangaement.interfaces;

/**
 * Created by Administrator on 2017/9/12.
 */

public interface IBaseSettingView {

     /* *
     *  初始化界面
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


    void ShowCarNumber(String carNumber);

    void ShowTerminal(String terminal);

    void ShowCompanyName(String companyName);

    void ShowDvrsenNumber(String dvrNumber);

    void ShowOutTime(String outTime);

    void ShowPinter(String pinter);

    void ShowPretime(String Pretime);

    void ShowDelaytime(String delaytime);


}
