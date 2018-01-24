package com.uninew.location;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocationBroadCastTools {
	private static final String TAG = "LocationBroadCastTools";
	private static boolean D = false;
	private Context mContext;

	public LocationBroadCastTools(Context mContext) {
		super();
		this.mContext = mContext;
	}

// -----------------------------以下为LocationServer发送方法-------------------------------------
	/**
	 * 定位信息应答
	 * 
	 * @param state
	 * @param longitude
	 * @param latitude
	 * @param speed
	 * @param direction
	 * @param time
	 */
	public void sendLocationResponse(GpsInfo mGpsInfo) {
		if (D)
			Log.d(TAG, "sendLocationResponse");
		Intent intent = new Intent(DefineLocationAction.LocationServiceResponse);
		intent.putExtra(DefineLocationAction.Key_LocationResponse.GpsInfo, mGpsInfo);
		mContext.sendBroadcast(intent);
	}
	
	/**
	 * 定位信息应答
	 * 
	 * @param state
	 * @param longitude
	 * @param latitude
	 * @param speed
	 * @param direction
	 * @param time
	 */
	public void sendLocationResponse(int state, double longitude,
			double latitude, float speed,double elevation, int direction, long time) {
		if (D)
			Log.d(TAG, "sendLocationResponse");
		Intent intent = new Intent(DefineLocationAction.LocationServiceResponse);
		intent.putExtra(DefineLocationAction.Key_LocationResponse.State, state);
		intent.putExtra(DefineLocationAction.Key_LocationResponse.Longitude,
				longitude);
		intent.putExtra(DefineLocationAction.Key_LocationResponse.Latitude,
				latitude);
		intent.putExtra(DefineLocationAction.Key_LocationResponse.Speed, speed);
		intent.putExtra(DefineLocationAction.Key_LocationResponse.Elevation, elevation);
		intent.putExtra(DefineLocationAction.Key_LocationResponse.Direction,
				direction);
		intent.putExtra(DefineLocationAction.Key_LocationResponse.Time, time);
		mContext.sendBroadcast(intent);
	}

	/**
	 * 发送GPS天线状态
	 * 
	 * @param state
	 *            0-断开，1-正常,2-天线短路
	 */
	public void sendGpsAntennaState(int state) {
		if (D)
			Log.d(TAG, "sendGpsAntennaState");
		Intent intent = new Intent(DefineLocationAction.LocationServiceAntenna);
		intent.putExtra(DefineLocationAction.Key_Antenna.State, state);
		mContext.sendBroadcast(intent);
	}

	/**
	 * 发送可见卫星数
	 * 
	 * @param state
	 *            0-断开，1-正常
	 */
	public void sendVisiableSatelliteNumber(int number) {
		if (D)
			Log.d(TAG, "sendVisiableSatelliteNumber");
		Intent intent = new Intent(DefineLocationAction.LocationServiceVisiableSatellite);
		intent.putExtra(DefineLocationAction.Key_SatelliteNumber.Number, number);
		mContext.sendBroadcast(intent);
	}
	
	/**
	 * 发送有效卫星数
	 * 
	 * @param state
	 *            0-断开，1-正常
	 */
	public void sendEffectiveSatelliteNumber(int number) {
		if (D)
			Log.d(TAG, "sendEffectiveSatelliteNumber");
		Intent intent = new Intent(DefineLocationAction.LocationServiceEffectiveSatellite);
		intent.putExtra(DefineLocationAction.Key_SatelliteNumber.Number, number);
		mContext.sendBroadcast(intent);
	}
	
	
	// -----------------------------以下为LocationClient发送方法-------------------------------------

	public void sendLocationRequest() {
		if (D)
			Log.d(TAG, "sendLocationRequest");
		Intent intent = new Intent(DefineLocationAction.LocationServiceRequest);
		mContext.sendBroadcast(intent);
	}
}
