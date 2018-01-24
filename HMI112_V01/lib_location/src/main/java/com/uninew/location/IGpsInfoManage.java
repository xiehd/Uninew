package com.uninew.location;

import android.content.Context;

public interface IGpsInfoManage {

	/**
	 * 监听GPS信息
	 * 
	 * @param readType 类型：0x00：Android系统接口读取定位信息；0x01：解析NMEA数据
	 * @param mContext
	 * @param mGpsInfoListener
	 */
	public void registerGpsInfoListener(int readType,Context mContext,
			IGpsInfoListener mGpsInfoListener);

	/**
	 * 取消监听GPS信息
	 * @param readType 类型：0x00：Android系统接口读取定位信息；0x01：解析NMEA数据
	 */
	public void unRegisterGpsInfoListener(int readType);
	
	
}
