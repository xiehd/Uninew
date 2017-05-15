package com.uninew.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uninew.file.dao.MarkerDao;
import com.uninew.file.dao.StationDao;
import com.uninew.util.auto.TrackPointStruct;

import android.util.Log;

/**
 * 
 * Json文件解析类
 * 
 * @author Administrator
 * 
 */
public class JsonParse {
	private String json = null;
	private JSONObject object = null;
	private static final String TAG = "JsonParse";
	private TxtTools txtTools;

	public JsonParse() {
		super();
		txtTools = new TxtTools();
	}

	/**
	 * 获取站点信息
	 * 
	 * @param routeName
	 *            路径名称，如 9路上行 或9路下行
	 * @return
	 */
	public ArrayList<StationDao> getStations(String routeName) {
		// {
		// "上行站点":
		// [
		// {"站点编号":0,"站点名称":"人民医院南院北门","站点语音":"医院北门","经度":"","纬度":"","角度":"","限速":"50","站前里程":"25","站点类型":"起点站","出站语音顺序":"默认","进站语音顺序":"默认","是否播报":"是"},
		// {"站点编号":1,"站点名称":"人民医院临停站","站点语音":"人民临停","经度":"","纬度":"","角度":"","限速":"50","站前里程":"20","站点类型":"小站","出站语音顺序":"默认","进站语音顺序":"进站音1.mp3+人民临停.mp3+
		// 进站音2.mp3+进站音3.mp3","是否播报":"是"},
		// {"站点编号":2,"站点名称":"武警支队","站点语音":"武警支队","经度":"","纬度":"","角度":"","限速":"50","站前里程":"20","站点类型":"小站","出站语音顺序":"默认","进站语音顺序":"默认","是否播报":"是"},
		// {"站点编号":3,"站点名称":"军分区行政中心","站点语音":"军分区行","经度":"","纬度":"","角度":"","限速":"50","站前里程":"20","站点类型":"小站","出站语音顺序":"默认","进站语音顺序":"默认","是否播报":"是"},
		// {"站点编号":4,"站点名称":"市政府东门","站点语音":"市政府东","经度":"","纬度":"","角度":"","限速":"50","站前里程":"20","站点类型":"小站","出站语音顺序":"默认","进站语音顺序":"默认","是否播报":"是"},
		// {"站点编号":5,"站点名称":"市政府","站点语音":"市政府","经度":"","纬度":"","角度":"","限速":"50","站前里程":"20","站点类型":"大站","出站语音顺序":"默认","进站语音顺序":"默认","是否播报":"是"},
		// {"站点编号":6,"站点名称":"盛世家和园","站点语音":"盛世家和","经度":"","纬度":"","角度":"","限速":"50","站前里程":"20","站点类型":"终点站","出站语音顺序":"默认","进站语音顺序":"默认","是否播报":"是"}
		// ]
		// }
		json = txtTools.readTxtFile(routeName + ".txt");
		if (json == null || json.equals("")) {
			Log.i("Test", "读取文件为空"+routeName);
			return null;
		}
		ArrayList<StationDao> stations = new ArrayList<StationDao>();
		StationDao sd = null;
		try {
			object = new JSONObject(json);
			JSONArray ja = object.getJSONArray(JsonFileContants.STATION_LIST);
			for (int i = 0; i < ja.length(); i++) {
				sd = new StationDao();
				JSONObject jo = ja.getJSONObject(i);
				sd.setStationMark(jo.getInt(JsonFileContants.STATION_MARK)); // 站点标识
				sd.setStationId(jo.getInt(JsonFileContants.STATION_ID)); // 站点编号
				sd.setStationName(jo.getString(JsonFileContants.STATION_NAME)); // 站点名称
				sd.setStationVoice(jo.getString(JsonFileContants.STATION_VOICE)); // 站点语音
				sd.setLongitude(jo
						.getDouble(JsonFileContants.STATION_LONGITUDE)); // 经度
				sd.setLatitude(jo.getDouble(JsonFileContants.STATION_LATITUDE)); // 纬度
				sd.setAngle(jo.getInt(JsonFileContants.STATION_ANGLE)); // 角度
				sd.setLimitSpeed(jo.getInt(JsonFileContants.STATION_LIMITSPEED)); // 限速
				sd.setFrontMileage(jo
						.getInt(JsonFileContants.STATION_FRONTMILEAGE)); // 站前里程
				sd.setStationStyle(jo.getString(JsonFileContants.STATION_STYLE)); // 站点类型
				sd.setOutStationVoice(jo
						.getString(JsonFileContants.STATION_OUTSTATIONVOICE)); // 出站语音顺序
				sd.setInStationVoice(jo
						.getString(JsonFileContants.STATION_INSTATIONVOICE)); // 进站语音顺序
				sd.setIsBroadcast(jo
						.getString(JsonFileContants.STATION_ISBROADCAST)); // 是否播报
				sd.setTicket(jo.getDouble(JsonFileContants.STATION_TICKET));// 票价
				sd.setTTSName(jo.getString(JsonFileContants.STATION_TTSNAME));// 音频名
				sd.setSpacing(jo.getInt(JsonFileContants.STATION_SPACING));// 站点间距
				stations.add(sd);
				// LogTool.logD(TAG, "stations ,station=" + sd.toString());
			}
			// System.out.println(crd.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stations;
	}

	/**
	 * 获取轨迹点信息
	 * 
	 * @param routeName
	 *            路径名称，如 9路上行 或9路下行
	 * @return
	 */
	public List<TrackPointStruct> getPoints(String routeName) {
		json = txtTools.readTxtFile(routeName+".txt");
		if (json == null || json.equals("")) {
			Log.i("Test", "读取文件为空");
			return null;
		}
		Log.i("Test", "读取文件json:" + json);
		List<TrackPointStruct> points = new ArrayList<TrackPointStruct>();
		TrackPointStruct pd = null;
		return null;
	}
	
	/**
	 * 获取拐点信息
	 * 
	 * @return
	 */
	public ArrayList<MarkerDao> getCorners(String routeName) {
		// {
		// "拐弯提醒":
		// [
		// {"编号":0,"语音":"拐弯提醒","经度":"","纬度":"","角度":"","限速":"50","站前里程":"25","是否播报":"是"},
		// {"编号":1,"语音":"拐弯提醒","经度":"","纬度":"","角度":"","限速":"50","站前里程":"25","是否播报":"是"},
		// {"编号":2,"语音":"拐弯提醒","经度":"","纬度":"","角度":"","限速":"50","站前里程":"25","是否播报":"是"},
		// {"编号":3,"语音":"拐弯提醒","经度":"","纬度":"","角度":"","限速":"50","站前里程":"25","是否播报":"是"},
		// {"编号":4,"语音":"拐弯提醒","经度":"","纬度":"","角度":"","限速":"50","站前里程":"25","是否播报":"是"},
		// {"编号":5,"语音":"拐弯提醒","经度":"","纬度":"","角度":"","限速":"50","站前里程":"25","是否播报":"是"},
		// {"编号":6,"语音":"拐弯提醒","经度":"","纬度":"","角度":"","限速":"50","站前里程":"25","是否播报":"是"}
		// ]
		// }
		json = txtTools.readTxtFile(JsonFileContants.FILE_LINE + routeName + "拐弯提醒.txt");

		if (json == null || json.equals("")) {
			return null;
		}
		ArrayList<MarkerDao> corners = new ArrayList<MarkerDao>();
		MarkerDao cd = null;
		try {
			object = new JSONObject(json);
			JSONArray ja = object.getJSONArray(JsonFileContants.CORNER_NOTIFY);
			for (int i = 0; i < ja.length(); i++) {
				cd = new MarkerDao();
				JSONObject jo = ja.getJSONObject(i);
				cd.setCornerId(jo.getInt(JsonFileContants.CORNER_CORNERID));
				cd.setCornerOrder(jo.getInt(JsonFileContants.CORNER_MARK));
				cd.setCornerVoice(jo.getString(JsonFileContants.CORNER_CORNERVOICE));
				cd.setName(jo.getString(JsonFileContants.CORNER_NAME));
				cd.setType(jo.getInt(JsonFileContants.CORNER_TYPE));
				cd.setLongitude(jo.getDouble(JsonFileContants.CORNER_LONGITUDE));// jo.getDouble("经度");
				cd.setLatitude(jo.getDouble(JsonFileContants.CORNER_LATITUDE));// jo.getDouble("经度");
				cd.setAngle(jo.getInt(JsonFileContants.CORNER_ANGLE));// jo.getDouble("角度");
				cd.setLimitSpeed(jo.getInt(JsonFileContants.CORNER_LIMITSPEED));// jo.getDouble("限速");
				cd.setFrontMileage(jo.getInt(JsonFileContants.CORNER_FRONTMILEAGE));// jo.getDouble("站前里程");
				cd.setIsBroadcast(jo.getString(JsonFileContants.CORNER_ISBROADCAST));
				cd.setIsReport(jo.getString(JsonFileContants.CORNER_ISREPORT));
				cd.setState(jo.getString(JsonFileContants.CORNER_STATE));
				corners.add(cd);
				// LogTool.logD(TAG, "corners ,corner=" + cd.toString());
			}
			// System.out.println(crd.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return corners;
	}

}
