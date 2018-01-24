package com.uninew.net.JT905.bean;

/**
 * 参数设置ID值表
 * Created by Administrator on 2017/10/13 0013.
 */

public interface ParamKey {
    int HeartBeat=0x0001;//心跳发送间隔
    int TcpResponseTimeOut=0x0002;//TCP消息应答超时时间
    int TcpResendTime=0x0003;//TCP消息重传次数
    int SmsResponseTimeOut=0x0004;//SMS消息应答超时时间
    int SmsResendTime=0x0005;//SMS消息重传次数

    int MainApn=0x0010;//主服务器APN,无线通信拨号访问点
    int MainWirelessDialName=0x0011;//主服务器无线通信拨号用户名
    int MainWrielessDialPwd=0x0012;//主服务器无线通信拨号密码
    int MainIpOrDomain=0x0013;//主服务器地址，IP或者域名
    int SpareApn=0x0014;//备份服务器APN,无线通信拨号访问点
    int SpareWirelessDialName=0x0015;//备份服务器无线通信拨号用户名
    int SpareWrielessDialPwd=0x0016;//备份服务器无线通信拨号密码
    int SpareIpOrDomain=0x0017;//备份服务器地址，IP或者域名

    int MainTcpPort=0x0018;//主服务器TCP端口
    int SpareTcpPort=0x0019;//备份服务器TCP端口
    int CardOrPaymentMainIpOrDomain=0x001A;//一卡通或支付平台主服务器地址，IP或者域名
    int CardOrPaymentMainTcpPort=0x001B;//一卡通或支付平台主服务器TCP端口
    int CardOrPaymentSpareIpOrDomain=0x001C;//一卡通或支付平台备份服务器地址，IP或者域名
    int CardOrPaymentSpareTcpPort=0x001D;//一卡通或支付平台备份服务器TCP端口

    int LocationReportStratege=0x0020;//位置汇报策略
    int LocationReportPlan=0x0021;//位置汇报方案
    int UnLoginReportIntervalTime=0x0022;//未登录汇报时间间隔
    int AccOffReportIntervalTime=0x0023;//ACC OFF时间间隔（秒）
    int AccOnfReportIntervalTime=0x0024;//ACC ON时间间隔（秒）
    int EmptyReportIntervalTime=0x0025;//空车时间间隔（秒）
    int NonEmptyReportIntervalTime=0x0026;//重车时间间隔（秒）
    int SleepReportIntervalTime=0x0027;//休眠时时间间隔（秒）
    int EmergencyReportIntervalTime=0x0028;//紧急报警时时间间隔（秒）

    int UnLoginReportIntervalDistance=0x0029;//登录距离间隔（米）
    int AccOffReportIntervalDistance=0x002A;//ACC OFF距离间隔（米）
    int AccOnfReportIntervalDistance=0x002B;//ACC ON距离间隔（米）
    int EmptyReportIntervalDistance=0x002C;//空车距离间隔（米）
    int NonEmptyReportIntervalDistance=0x002D;//重车距离间隔（米）
    int SleepReportIntervalDistance=0x002E;//休眠时距离间隔（米）
    int EmergencyReportIntervalDistance=0x002F;//紧急报警时距离间隔（米）

    int CornerReissueAngle=0x0030;//拐点补传角度
    int ControlCenterPhoneNumber=0x0040;//监控中心电话号码
    int ResetPhoneNumber=0x0041;//复位电话号码
    int RestoreSettingsPhoneNumber=0x0042;//恢复出厂设置电话号码

    int ControlCenterSmsPhoneNumber=0x0043;//监控中心SMS电话号码
    int ReceiveISUSmsAlarmPhoneNumber=0x0044;//接受sms报警信号平台
    int PhoneAnswerStrategy=0x0045;//电话接听策略0：自动监听，1：ACC ON时自动接听，OFF时手动接听
    int MaximumCallTimePerOne=0x0046;//当次最长通话时间
    int MaximumCallTimePerMonth=0x0047;//当月累计最长通话时间
    int TelephoneCornetLength=0x0048;//电话短号长度
    int MonitorPhoneNumber=0x0049;//监听电话号码
    int DeviceMaintenancePWD=0x004A;//设备维护密码
    int TTSVolumeControl=0x004B;//语音播报饮料控制0-9

    int AlarmShieldSwitch=0x0050;//报警屏蔽字
    int AlarmSendSMSTextSwitch=0x0051;//报警发送文本SMS开关
    int AlarmShootSwitch=0x0052;//报警拍摄开关
    int AlarmShootStorageSwitch=0x0053;//报警拍摄开关

    int MaximumSpeed=0x0055;//最高速度
    int SpeedingDuration=0x0056;//超速持续时间
    int ContinuousDrivingThreshold=0x0057;//连续驾驶时间门限
    int MinimumBreakTime=0x0058;//最小消息时间
    int MaximumParkingTime=0x0059;//最长停车时间
    int ToatalDrivingTimeThreshold=0x005A;//当天累计驾驶时间门限

    int PcitureVideoQuality=0x0070;//图像、视频质量1-10；0最好
    int Brightness=0x0071;//亮度
    int Contrast=0x0072;//对比度
    int Saturation=0x0073;//饱和度
    int Chroma=0x0074;//色度

    int CarOdometerReading=0x0080;//车辆里程表读数
    int CarProvinceId=0x0081;//车辆所在省ID；
    int CarCityId=0x0082;//车辆所在市ID

    int MeterOperationNumberLimit=0x0090;//计价器营运次数限制0-9999
    int MeterOperationTimeLimit=0x0091;//计价器营运时间限制YYYYMMDDhh，0000000000表示不做限制
    int BusinessOperationLicense=0x0092;//出租车企业运营许可证
    int TaxiBusinessName=0x0093;//出租车企业名称
    int TaxiLicensePlate=0x0094;//出租车车牌号

    int ISURecordingMode=0x00A0;//ISU录音模式
    int ISURecordingFileMaxLength=0x00A1;//录音文件最大长度
    int LCDHeartBeatInterval=0x00A2;//LCD心跳时间间隔
    int LEDHeartBeatInterval=0x00A3;//LED心跳时间间隔
    int ACCOFFIntoSleepTime=0x00AF;//ACC OFF以后进入休眠模式的时间
    int VideoServerProtocolMode=0x00B0;//视频服务器协议模式0x00:TCP，0x01:UDP
    int VideoServerAPN=0x00B1;//视频服务器APN
    int VideoServerWirelessDialName=0x00B2;//视频服务器无线通信拨号用户名
    int VideoSeverrWirelessDialPwd=0x00B3;//视频服务器无线通信拨号密码
    int VideoServerIpOrDomain=0x00B4;//视频服务器地址，IP或域名
    int VideoServerPort=0x00B5;//视频服务器端口


}
