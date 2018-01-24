package com.uninew.net.JT905.common;

public class BaseMsgID {

    /**
     * 转义
     */
    public static final byte ESCAPE = 0x7d;

    /**
     * 转义 0X7E->0X7D 0X02
     */
    public static final byte ESCAPE_E = 0x02;

    /**
     * 转义 0X7D->0X7D 0X01
     */
    public static final byte ESCAPE_D = 0x01;

    /**
     * 标识位
     */
    public static final byte MSG_FLAG = 0x7e;
    /**
     * 终端通用应答
     */
    public static final int TERMINAL_GENERAL_ANS = 0x0001;
    /**
     * 平台通用应答
     */
    public static final int PLATFORM_GENERAL_ANS = 0x8001;
    /**
     * 终端心跳
     */
    public static final int TERMINAL_HEARBEAT = 0x0002;

    /**
     * 设置终端参数
     */
    public static final int TERMINAL_PARAM_SET = 0x8103;
    /**
     * 查询终端参数
     */
    public static final int TERMINAL_PARAM_QUERY = 0x8104;
    /**
     * 查询终端参数应答
     */
    public static final int TERMINAL_PARAM_QUERY_ANS = 0x0104;
    /**
     * 终端控制
     */
    public static final int TERMINAL_CONTROL = 0x8105;

    /**
     * 终端升级结果报告消息
     */
    public static final int TERMINAL_UPDATE_ANS = 0x0105;

    /**
     * 查询指定终端参数
     */
    public static final int TERMINAL_SPECIFIC_PARAM_QUERY = 0x8106;
    /**
     * 查询终端属性
     */
    public static final int TERMINAL_ATTR_QUERY = 0x8107;
    /**
     * 查询终端属性应答
     */
    public static final int TERMINAL_ATTR_QUERY_ANS = 0x0107;
    /**
     * 位置信息汇报
     */
    public static final int LOCATION_INFORMATION_REPORT = 0x0200;

    /**
     * 位置信息查询
     */
    public static final int LOCATION_INFORMATION_QUERY = 0x8201;

    /**
     * 位置信息查询应答
     */
    public static final int LOCATION_INFORMATION_QUERY_ANS = 0x0201;

    /**
     * 临时位置跟踪
     */
    public static final int TEMP_LOCATION_QUERY = 0x8202;

    /**
     * 临时位置跟踪汇报
     */
    public static final int TEMP_LOCATION_QUERY_ANS = 0x0202;

    /**
     * 文本信息下发
     */
    public static final int TEXT_MSG_DOWNLOAD = 0x8300;
    /**
     * 盲区数据上报
     */
    public static final int BLAND_DATA_REPORT = 0x0203;
    /**
     * 事件设置
     */
    public static final int EVENT_SET = 0x8301;
    /**
     * 事件报告
     */
    public static final int EVENT_REPORT = 0x0301;
    /**
     * 提问下发
     */
    public static final int ASK_QUESTION = 0x8302;
    /**
     * 提问应答
     */
    public static final int ASK_QUESTION_ANS = 0x0302;
    /**
     * 电话回拨
     */
    public static final int CALL_BACK = 0x8400;
    /**
     * 设置电话本
     */
    public static final int SET_PHONE_BOOK = 0x8401;
    /**
     * 车辆控制
     */
    public static final int VEHICLE_CONTROL = 0x8500;
    /**
     * 车辆控制应答
     */
    public static final int VEHICLE_CONTROL_ANS = 0x0500;
    /**
     * 摄像头图像上传
     */
    public static final int CAMERA_PICTURE_UPLOAD = 0x0800;
    /**
     * 摄像头立即拍摄
     */
    public static final int CAMERA_SHOOT_NOW_COMMOND = 0x8801;
    /**
     * 存储图像检索
     */
    public static final int STORE_IMAGE_SEARCH = 0x8802;
    /**
     * 存储图像检索应答
     */
    public static final int STORE_IMAGE_SEARCH_ANS = 0x0802;
    /**
     * 存储图像、音视频上传命令
     */
    public static final int STORE_IMAGE_UPLOAD_COMMOND = 0x8803;

    /**
     * 订单任务下发
     */
    public static final int ORDER_SEND_DOWN = 0x8B00;
    /**
     * 驾驶员抢答
     */
    public static final int DRIVER_ANSWER_ORDER = 0x0B00;
    /**
     * 下发抢答结果信息
     */
    public static final int DRIVER_ANSWER_ORDER_MSG = 0x8B01;
    /**
     * 驾驶员电召任务完成确认
     */
    public static final int ORDER_FINISH_ENSURE = 0x0B07;
    /**
     * 驾驶员取消订单
     */
    public static final int DRIVER_CANCLE_ORDER = 0x0B08;
    /**
     * 中心取消订单
     */
    public static final int PLATFORM_CANCLE_ORDER = 0x8B08;

    /**
     * 上报签到信息上传
     */
    public static final int SIGN_IN_REPORT = 0x0B03;

    /**
     * 上班签退信息上传
     */
    public static final int SIGN_OUT_REPORT = 0x0B04;
    /**
     * 营运数据上传
     */
    public static final int OPERATION_DATA_REPORT = 0x0B05;
    /**
     * 外围设备指令下行透传
     */
    public static final int EXTERNAL_DEVICE_DOWN_PENETRATE = 0x8B10;
    /**
     * 外围设备指令上行透传
     */
    public static final int EXTERNAL_DEVICE_UP_PENETRATE = 0x0B10;
    /**
     * 音频检索
     */
    public static final int STORE_AUDIO_SEARCH = 0X8805;
    /**
     * 存储音频检索应答
     */
    public static final int STORE_AUDIO_SEARCH_ANS = 0X0805;
    /**
     * 音视频上传
     */
    public static final int STORE_AUDIOVIDEO_UPLOAD = 0X0806;
    /**
     * 中心确认报警
     */
    public static final int PLATFORM_CONFIRM_ALARM = 0X8B0A;
    /**
     * 中心解除报警
     */
    public static final int PLATFORM_RELEASE_ALARM = 0X8B0B;
    /**
     * 中心巡检设备
     */
    public static final int PLATFORM_CHECK_DEVICE = 0X8B11;
    /**
     * 设备巡检应答
     */
    public static final int PLATFORM_CHECK_DEVICE_ANS = 0X0B11;


//---------------------以下为深标独有协议-----------------------

    /**
     * 实时音视频传输请求
     */
    public static final int REALTIME_AUDIOVIDEO_TRANS_REQUEST = 0X9501;
    /**
     * 音视频传输控制
     */
    public static final int AUDIOVIDEO_TRANS_CONTTROL = 0X9102;
    /**
     * 实时音视频媒体通道注册
     */
    public static final int REALTIME_AUDIOVIDEO_CHANNEL_REGISTER = 0X1209;
    /**
     * 存储音视频检索
     */
    public static final int STROE_AUDIO_SEARCH_SB = 0X9505;
    /**
     * 终端上传音视频资源列表
     */
    public static final int AUDIOVIDEO_LIST_UPLOAD = 0X1505;
    /**
     * 终端搜索月历信息列表
     */
    public static final int SEARCH_CALENDAR_LIST = 0X9508;
    /**
     * 月历查询应答
     */
    public static final int SEARCH_CALENDAR_LIST_ANS = 0X1508;
    /**
     * 文件上传指令
     */
    public static final int FILE_UPLOAD_COMMOND = 0X950B;
    /**
     * 文件传输注册
     */
    public static final int FILE_TRANS_REGISTER = 0X120A;
    /**
     * 存储音视频回放请求
     */
    public static final int STORE_AUDIOVIDEO_PLAYBACK_REQUEST = 0X9509;

    /**
     * 存储音视频回放控制
     */
    public static final int STORE_AUDIOVIDEO_PLAYBACK_CONTROL = 0X950A;
    /**
     * 防伪密标
     */
    public static final int ANTIFAKE_LABELS = 0X8C01;
    /**
     * 司机卡黑名单查询
     */
    public static final int DRIVER_BLACKLIST_QUERY = 0X0B50;
    /**
     * 司机卡黑名单信息查询响应
     */
    public static final int DRIVER_BLACKLIST_QUERY_ANS = 0X8B50;
    /**
     * 司机卡黑名单信息下载请求
     */
    public static final int DRIVER_BLACKLIST_DOWNLOAD_REQUEST = 0X0B51;
    /**
     * 司机卡黑名单下载响应
     */
    public static final int DRIVER_BLACKLIST_DOWNLOAD_RESPONSE = 0X8B51;
    /**
     * 司机卡黑名单更新通知
     */
    public static final int DRIVER_BLACKLIST_UPDATE_NOTIFY = 0X8B52;



    //----------------------------应用间通信自定义ID--------------------------------------------
    /**连接平台*/
    public static final int PLATFORM_CONNECT=0x7111;

    /**断开平台*/
    public static final int PLATFORM_DISCONNECT=0x7112;

    /**连接状态查询*/
    public static final int CONNECTSTATE_REQUEST=0x7113;

    /**连接状态应答*/
    public static final int CONNECTSTATE_RESPONSE=0x7114;

    /**驾驶员抢答结果*/
    public static final int DRIVER_ANSWER_ORDER_RESPONSE=0x7115;

    /**
     * 驾驶员取消订单结果
     */
    public static final int DRIVER_CANCLE_ORDER_ANS = 0x7116;

    /**
     * 驾驶员电召任务完成确认结果
     */
    public static final int ORDER_FINISH_ENSURE_ANS = 0x7117;

    /**
     * 签到应答
     */
    public static final int SIGN_IN_ANS = 0x7118;

    /**
     * 签退应答
     */
    public static final int SIGN_OUT_ANS = 0x7119;

    /**报警上报*/
    public static final int WARN_REPORT=0x711A;

    /**状态变化上报*/
    public static final int STATE_REPORT=0x711B;

    //----------------------------MMS自定义ID--------------------------------------------
    /**系统状态通知*/
    public static final int MMS_SYSTEMSTATE=0xA001;
    /**MCU版本号*/
    public static final int MMS_MCUVERSION=0xA002;
    /**电池电量信息*/
    public static final int MMS_ELECTRICITY=0xA003;
    /**物理按键通知*/
    public static final int MMS_HANDLEMCUKEY=0xA004;
    /**CAN数据透传上报*/
    public static final int MMS_CANDATAS=0xA005;
    /**RS232数据透传*/
    public static final int MMS_RECEIVERS232=0xA006;
    /**RS485数据透传*/
    public static final int MMS_RECEIVERS485=0xA010;
    /**波特率设置信息*/
    public static final int MMS_BAUDRATE=0xA011;
    /**IO输出信号*/
    public static final int MMS_IOSTATE=0xA007;
    /**脉冲测速信号*/
    public static final int MMS_PULSESIGNAL=0xA008;


    /**ARM状态同步*/
    public static final int MMS_ARMSTATESYNCHRO=0xA009;
    /**屏幕背光开关控制*/
    public static final int MMS_SCREENBACKLIGHT=0xA00A;
    /**屏幕亮度调节*/
    public static final int MMS_SCREENBRIGHTNESS=0xA00B;
    /**功放开关控制*/
    public static final int MMS_POWERAMPLIFIER=0xA00C;
    /**CAN数据下发*/
    public static final int MMS_SENDCANDATAS=0xA00D;
    /**RS232数据透传*/
    public static final int MMS_SENDRS232=0xA00E;
    /**RS485数据透传*/
    public static final int MMS_SENDRS485 =0xA00F;
    /**波特率设置*/
    public static final int MMS_SETBAUDRATE=0xA100;
    /**IO输入信号*/
    public static final int MMS_SETIOSTATE=0xA200;
    /**ACC OFF后等待关机时间*/
    public static final int MMS_SETACCOFFTIME=0xA300;
    /**ARM唤醒频率*/
    public static final int MMS_SETWAKEUPFREQUENCY =0xA400;
    //-------------------------------------------计价器--------------------------------------------------------------------
    /**计价器状态查询*/
    public static final int TAXI_STATEQUREY =0xB100;
    /**运营开始（进入重车）*/
    public static final int TAXI_OPERATE_STATR =0xB101;
    /**运营结束（进入空车）*/
    public static final int TAXI_OPERATE_END =0xB102;
    /**运价参数*/
    public static final int TAXI_FREIGHT =0xB103;
    /**历史数据查询*/
    public static final int TAXI_HISTORY_OPERA =0xB104;
    /**时钟校验*/
    public static final int TAXI_CLOCK =0xB105;
    /**心跳*/
    public static final int TAXI_HEART =0xB106;
}
