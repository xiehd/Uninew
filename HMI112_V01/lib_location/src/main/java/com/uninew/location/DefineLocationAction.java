package com.uninew.location;

public interface DefineLocationAction {
	
	/** 查询定位服务 */
	String LocationServiceRequest = "Com.LocationService.Request";
	/** 报告定位服务 */
	String LocationServiceResponse = "Com.LocationService.Response";

	public interface Key_LocationResponse {
		/** 状态 */
		String GpsInfo = "GpsInfo";
		/** 状态 */
		String State = "State";
		/** 经度 */
		String Longitude = "Longitude";
		/** 纬度 */
		String Latitude = "Latitude";
		/** 速度 */
		String Speed = "Speed";
		/** 海拔高度 */
		String Elevation = "Elevation";
		/** 方向 */
		String Direction = "Direction";
		/** 时间 */
		String Time = "Time";
	}
	/** 已定位 */
	int Location_State_Positioned = 0x01;
	/** 未定位*/
	int Location_State_NotPositioned =0x00;
	
	
	/** GPS天线状态 */
	String LocationServiceAntenna = "Com.LocationService.Antenna";
	public interface Key_Antenna {
		/** 状态 ：0-断开，1-连接*/
		String State = "State";
	}
	/** GPS天线连接正常*/
	int GpsAntenna_State_Connected = 0x01;
	/** GPS天线断开 */
	int GpsAntenna_State_DisConnected = 0x00;
	
	/** 可见卫星数 */
	
	String LocationServiceVisiableSatellite = "Com.LocationService.VisiableSatellite";
	/** 有效卫星数 */
	String LocationServiceEffectiveSatellite = "Com.LocationService.EffectiveSatellite";
	public interface Key_SatelliteNumber {
		/**卫星数目*/
		String Number = "Number";
	}
	
}
