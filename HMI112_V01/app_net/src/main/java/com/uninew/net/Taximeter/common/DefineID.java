package com.uninew.net.Taximeter.common;

/**
 * Created by Administrator on 2017/10/14.
 */

public class DefineID {
    //////////////////////////////////////计价器////////////////////////////////////////////////////
    /**
     * 起始位
     */
    public static final short TAXI_FLAG = (short) 0x55AA;
    public static final short TAXI_STATE = (short) 0x0000;//状态查询
    public static final short TAXI_FREIGHT_QURY = (short) 0x0004;//运价参数查询
    public static final short TAXI_FIEIGHT_SET = (short) 0x0005;//运价参数设置
    public static final short TAXI_SINGLEOPERATE_STAR = (short) 0x000E7;//单次运营开始
    public static final short TAXI_SINGLEOPERATE_END = (short) 0x000E8;//单次运营结束
    public static final short TAXI_OPERATEDATA_SUPPL = (short) 0x000F2;//计价器运营数据补传
    public static final short TAXI_OPEN = (short) 0x000E0;//计价器开机指令
    public static final short TAXI_OPEN_OK = (short) 0x000E1;//开机成功应答
    public static final short TAXI_CLOSE = (short) 0x000E3;//计价器关机指令
    public static final short TAXI_CLOSE_OK = (short) 0x000E4;//关机成功应答
    public static final short TAXI_CURRENTOPERATDATA_SUUPP = (short) 0x000F1;//计价器当班运营数据补传
    public static final short TAXI_HEART = (short) 0x000E9;//计价器心跳
    public static final short TAXI_HISTORY_OPERA = (short) 0x00006;//计价器历史运营查询
    public static final short TAXI_CLOCK = (short) 0x00001;//计价器时钟误差查询
    public static final short TAXI_FIRMWARE_UPDATE = (short) 0x000FF;//计价器固件升级

    //智能顶灯
    public static final short LED_QUERY_STATE = 0x0000;//顶灯状态查询
    public static final short LED_RESET = 0x0001;//顶灯复位
    public static final short LED_SET_BAUDRATE = 0x0003;//顶灯波特率
    public static final short LED_FIRMWARE_STATE = 0x00FF;//顶灯固件升级
    public static final short LED_OPERATE_STATE = 0x0010;//顶灯运营状态
    public static final short LED_STAR_STATE = 0x0011;//顶灯星级状态
    public static final short LED_BID_SHOW = 0x0013;//顶灯防伪密标显示
    public static final short LED_BID_DISS = 0x0014;//顶灯防伪密标取消
    public static final short LED_SET_NIGHTMODE = 0x0012;//夜间工作模式
    public static final short LED_NIGHTMODE_PARAMETER = 0x0020;//夜间工作模式参数设置

}
