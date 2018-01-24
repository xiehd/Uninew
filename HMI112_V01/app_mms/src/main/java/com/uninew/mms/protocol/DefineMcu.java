package com.uninew.mms.protocol;
/**
 * mcu的常量
 * @author Administrator
 *
 */
public class DefineMcu {
	
	/**
	 * 数据包的开始
	 */
	public static final short MCU_FLAG = (short) 0xAA55;
	/**
	 * 默认的mcu指令超时时间为1秒
	 */
	public static final int MCU_TIME_OUT = 3000;
	/**
	 * 默认的的最多请求次数为3次
	 */
	public static final int MCU_TIME_OUT_COUNT = 3;
	/**
	 * 模组类型数据ID
	 * @author Administrator
	 *
	 */
	public class GroupId {
		/**
		 * 系统指令
		 */
		public static final byte SYSTEM = 0x01;
		/**
		 * 显示单元指令
		 */
		public static final byte DISPLAY = 0x02;
		/**
		 * DVR单元指令
		 */
		public static final byte DVR = 0x03;
		/**
		 * 传感I/O指令
		 */
		public static final byte SENSOR = 0x04;
		/**
		 * TPMS指令
		 */
		public static final byte TPMS = 0x05;
		/**
		 * OBD模块指令
		 */
		public static final byte OBD = 0x06;
		/**
		 * RS485通讯
		 */
		public static final byte RS485 = 0x07;
		/**
		 * RS232通讯
		 */
		public static final byte RS232 = 0x08;
		/**
		 * 车油宝通讯
		 */
		public static final byte CYB   = 0x09;
		/**
		 * 参数设置。
		 */
		public static final byte PARM_SET = 0x10;
		/**
		 * mcu升级
		 */
		public static final byte MCU_UPDATA=0x11;
	}
	
	/**
	 * 数据包类型ID
	 * @author Administrator
	 *
	 */
	public class TypeId {
		/** 命令报文**/
		public static final byte GID_CMD = 0x01;
		/** 请求报文**/
		public static final byte GID_ERQ = 0x02;
		/** 事件报文**/
		public static final byte GID_EVT = 0x03;
		/** 信息状态**/
		public static final byte GID_STATE = 0x04;
	}
	
	/**
	 * 系统信息CommandId
	 * @author Administrator
	 *
	 */
	public class SystemCommandId{
		/** 系统电源状态同步**/
		public static final byte System_PS = 0x01;
		/** 模块电源状态**/
		public static final byte Module_PS = 0x02;
		/** LED状态指示**/
		public static final byte State_D = 0x03;
		/** 电池充电状态**/
		public static final byte Charge_BS = 0x04;
		/** 系统信息**/
		public static final byte System_Info = 0x05;
		/** 音频信号控制**/
		public static final byte Audio_CTR = 0x06;
	}
	
	public class SystemInfo{
		/** 心跳**/
		public static final byte SystemInfo_HeartBeat = 0x01;
		/** MCU版本**/
		public static final byte SystemInfo_McuVersion = 0x02;
		/** MCU发布日期**/
		public static final byte SystemInfo_McuPublicDate = 0x03;
		/** 时间校验**/
		public static final byte SystemInfo_McuCheckTime = 0x04;
		/** 主板版本号**/
		public static final byte SystemInfo_MainBoardVersion = 0x05;
	}
	public class SystemPs{
		/** 正常状态**/
		public static final byte SystemPs_normalworlk = 0x01;
		/** 休眠状态**/
		public static final byte SystemPs_sleep = 0x02;
		/** 断ACC工作状态**/
		public static final byte SystemPs_offACC = 0x03;
		/** 断BAT状态（也就是电池工作状态）**/
		public static final byte SystemPs_offBAT = 0x04;
		/** 异常工作状态。**/
		public static final byte SystemPs_error = 0x05;
		/** 每5分钟定时ARM上传定位信息状态 **/
		public static final byte SystemPs_locatinInfo = 0x06;
		/** ACC _ON 工作状态**/
		public static final byte SystemPs_ACC_ON = 0x07;
	}
	
	
	
	/**
	 * 显示信息CommandId
	 * @author Administrator
	 *
	 */
	public class DiaplayCommandId{
		/** 触摸屏座标信息**/
		public static final byte Touch = 0x01;
		/** 按键信息**/
		public static final byte Key = 0x02;
		/** RFID信息**/
		public static final byte Rfid = 0x03;
		/** 显示屏电源控制，背光亮度，背光开关信息**/
		public static final byte Power = 0x04;
		/** 麦克风处理芯片的控制信息**/
		public static final byte Mic = 0x05;
		/** 显示单元的系统信息**/
		public static final byte System = 0x06;
	}
	
	/**
	 * RS485CommandId
	 * @author Administrator
	 *
	 */
	public class RS485CommandId{
		/** 油压检测设备**/
		public static final byte OilPressure_Detection = 0x01;
		/** 温度检测设备**/
		public static final byte Temperature_Detection = 0x02;
		/** 人流量检测设备**/
		public static final byte Traffic_Detection = 0x03;
		/** 路牌**/
		public static final byte Road_Signs = 0x04;
		/** 广告屏**/
		public static final byte Ad_Screen = 0x05;
	}
	/**
	 * 升级命令ID
	 * @author Administrator
	 *
	 */
	public class UpdateCommandId{
		/** MCU升级命令**/
		public static final byte MCU_UPDATE = 0x01;
		/** MCU升级包发送命令**/
		public static final byte MCU_SEND = 0x02;
		/** OS升级通知命令**/
		public static final byte OS_UPDATE = 0x03;
	}
	
	/**
	 * 参数设置ID
	 * @author Administrator
	 *
	 */
	public class ParamSetId{
		/** 休眠设置命令**/
		public static final byte MCU_SLEEP = 0x01;
	}
	
	
}
