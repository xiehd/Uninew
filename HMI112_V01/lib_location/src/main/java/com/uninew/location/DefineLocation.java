package com.uninew.location;

public interface DefineLocation {

	/** 已定位 */
	int Location_State_Positioned = 0x01;
	/** 未定位*/
	int Location_State_NotPositioned =0x00;
	
	/** GPS天线连接正常*/
	int GpsAntenna_State_Connected = 0x01;
	/** GPS天线断开 */
	int GpsAntenna_State_DisConnected = 0x00;
	
	/**Androd常用接口读取*/
	int Location_ReadType_Android=0x00;
	/**0183原始数据读取*/
	int	Location_ReadType_NMEA=0x01;
	
	
}
