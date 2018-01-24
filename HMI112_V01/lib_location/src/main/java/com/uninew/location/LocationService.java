package com.uninew.location;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus.NmeaListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

/**
 * 定位服务
 * 
 * @author Administrator
 */
public class LocationService extends Service {
	
	private boolean D = false;
	private static final String TAG = "LocationService";
	public static GPSManager gpsManager;
	public static boolean mLocationValid = false;
	private LocationManager locationManager;
	private LocationBroadCastTools mBroadCastTools;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if(D)Log.d(TAG, "-------------onCreate-------------");
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		mBroadCastTools = new LocationBroadCastTools(this);
		if(gpsManager == null){
			gpsManager = new GPSManager(this);
			gpsManager.setGpsStateListener();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(D)Log.d(TAG, "-------------onStartCommand-------------");
		return super.onStartCommand(intent, flags, startId);
	}

	
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		if(D)Log.d(TAG, "-------------onUnbind-------------");
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (gpsManager!=null) {
			gpsManager.colseGPSManager();
		}
		if(D)Log.d(TAG, "-------------onDestroy-------------");
	}
	
	
	// ------------------------------解析NEMA方法------------------------------------------------------

	/**
	 * GPS位置监听类
	 * 
	 * @author Administrator
	 * 
	 */
	public class GPSManager {
		private static final String TAG = "GPSManager";

		public GPSManager(Context mContext) {
			super();
			locationManager = (LocationManager) mContext
					.getSystemService(Context.LOCATION_SERVICE);
		}

		public void colseGPSManager() {
			if (locationManager != null) {
				locationManager.removeNmeaListener(nmeaListener);
				locationManager = null;
			}
		}

		/**
		 * 处理GPS
		 */
		public void setGpsStateListener() {
			// 判断GPS是否正常启动
			if (!locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				if(D)Log.d(TAG, "未开启GPS导航...");
				return;
			}
			// 监听状态
			locationManager.addNmeaListener(nmeaListener);
		}

		private NmeaListener nmeaListener = new NmeaListener() {

			@Override
			public void onNmeaReceived(long timestamp, String nmea) {
				String[] datas = nmea.split(",");
				switch (datas[0]) {
				case "$GNGGA":
					parseGNGGA(datas);
					break;
				case "$GNRMC":
					parseGNRMC(datas);
					break;
				case "$GPGSV":
					parseGPGSV(datas);
					break;
				case "$GNGSA":
				case "$GPGSA":
					parseGNGSA(datas);
					break;
				case "$GPTXT":
					parseGPTXT(datas);
					break;
				default:
					break;
				}
			}
		};
	}

	/**
	 * 解析GNRMC数据
	 * 
	 * @param gpgsa
	 */
	private int effectiveCount;// 有效卫星
	private int maxSatellites;// 可见卫星
	private int antennaState;// 天线状态
	private double latitude;
	private double longitude;
	private float speed;
	private double elevation ;
	private float direction;
	private long time;
	
	public void parseGNGGA(String data[]) {
		if (!data[6].equals("0")) {// 有效定位
			// 海拔高度
			if (data[9] != null && !data[9].equals("")) {
				elevation=Double.parseDouble(data[9]);
			}
		} else {
			//未定位
		}
	}
	
	public void parseGNRMC(String data[]) {
		if (data[2].equals("A")) {// 有效定位
			// 时间
			StringBuffer time_str = new StringBuffer(data[9] + data[1]);
			time_str.insert(4, "0").insert(4, "2");
			time = parseTime(time_str.toString()) + 8 * 60 * 60 * 1000;
			// 纬度
			if (data[3] != null && !data[3].equals("") && data[3].length() > 3) {
				String d1 = data[3].substring(0, 2);
				String d2 = data[3].substring(2, data[3].length());
				latitude = Double.parseDouble(d2) / 60.0f
						+ Double.parseDouble(d1);
				latitude=((int)(1000000*latitude))/1000000.0;
			}
			// 经度
			if (data[5] != null && !data[5].equals("") && data[5].length() > 4) {
				String d3 = data[5].substring(0, 3);
				String d4 = data[5].substring(3, data[5].length());
				longitude = Double.parseDouble(d4) / 60.0f
						+ Double.parseDouble(d3);
				longitude=((int)(1000000*longitude))/1000000.0;
			}
			// 速度
			if (data[7] != null && !data[7].equals("")) {
				speed = Float.parseFloat(data[7]) * 1.842f;// 海里*1.842/小时
															// <===>千米/小时
			}
			// 方向
			if (data[8] != null && !data[8].equals("")
					&& isInteger(data[8])) {
				direction = Float.parseFloat(data[8]);
			}
			mBroadCastTools.sendLocationResponse(
					DefineLocationAction.Location_State_Positioned, longitude,
					latitude, speed, elevation,(int) direction, time);
		} else {
			//
			mBroadCastTools.sendLocationResponse(
					DefineLocationAction.Location_State_NotPositioned,
					0, 0, 0, 0,(int) 0, 0);
		}
	}
	
	/**
	  * 判断字符串是否是整数
	  */
	public static boolean isInteger(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * 解析时间
	 * 
	 * @param time_str
	 * @return
	 */
	private long parseTime(String time_str) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss.SSS");
		Date date = null;
		try {
			if (time_str.length() >= 17) {
				date = dateFormat.parse(time_str);
			} else {
				date = new Date();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}

	/**
	 * 可见卫星
	 * 
	 * @param datas
	 */
	private void parseGPGSV(String[] datas) {
		if (!TextUtils.isEmpty(datas[3]) && datas[3].matches("[0-9]+")) {
			maxSatellites = Integer.parseInt(datas[3]);
		}
		mBroadCastTools.sendVisiableSatelliteNumber(maxSatellites);
	}

	/**
	 * 有效卫星
	 * 
	 * @param datas
	 */
	private void parseGNGSA(String[] datas) {

		if (!TextUtils.isEmpty(datas[3])) {
			if (Integer.parseInt(datas[3]) > 32) {
				return;
			} else {
				effectiveCount = 0;
				for (int i = 3; i < 15; i++) {
					if (!TextUtils.isEmpty(datas[i])) {
						effectiveCount++;
					}
				}
			}
		}
		mBroadCastTools.sendEffectiveSatelliteNumber(effectiveCount);
	}

	/**
	 * 获取天线状态
	 * 
	 * @param datas
	 */
	private void parseGPTXT(String[] datas) {
		if ("01".equals(datas[3])) {
			if (datas[4].indexOf("OPEN") != -1) {
				if (D)
					Log.d(TAG, "天线开路,未接天线");
				antennaState = 0;
			} else if (datas[4].indexOf("OK") != -1) {
				if (D)
					Log.d(TAG, "天线连接良好");
				antennaState = 1;
			} else if (datas[4].indexOf("SHORT") != -1) {
				if (D)
					Log.d(TAG, "天线短路");
				antennaState = 2;
			}
			mBroadCastTools.sendGpsAntennaState(antennaState);
		}
		
	}
	// --------------------------------------------------------------------------------------
}
