package com.uninew.json;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uninew.file.dao.MarkerDao;
import com.uninew.file.dao.PointsDao;
import com.uninew.file.dao.RoutesDao;
import com.uninew.file.dao.RunRoutesDao;
import com.uninew.file.dao.SettingsDao;
import com.uninew.file.dao.StationDao;


/**
 * 修改Json文件帮助类
 * 
 * @author Administrator
 * 
 */
public class JsonUpdate {
	private JsonParse parse;

	public JsonUpdate() {
		super();
		parse = new JsonParse();
	}

	/**
	 * 修改系统运行
	 */
	public boolean updateRunFile(RunRoutesDao rrd) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\r\n")
				// 本班次
				.append("\"" + JsonFileContants.RUN_ROUTE_NAME + "\"").append(":")
				.append("\"" + rrd.getRouteName() + "\"").append(",\r\n")
				// 线路标识
				.append("\"" + JsonFileContants.ROUTE_LINEMARK + "\"").append(":")
				.append("\"" + rrd.getLineMark() + "\"").append(",\r\n")
				// 开车时间
				.append("\"" + JsonFileContants.RUN_START_TIME + "\"").append(":")
				.append("\"" + rrd.getStartTime() + "\"").append(",\r\n")
				// 收车时间
				.append("\"" + JsonFileContants.RUN_END_TIME + "\"").append(":").append("\"" + rrd.getEndTime() + "\"")
				.append(",\r\n")
				//起始站
				.append("\"" + JsonFileContants.RUN_START_STATION + "\"").append(":")
				.append("\"" + rrd.getStartStation() + "\"").append(",\r\n")
				//终点站
				.append("\"" + JsonFileContants.RUN_END_STATION + "\"").append(":")
				.append("\"" + rrd.getEndStation() + "\"")
				.append("\r\n}")
				;
		return new TxtTools().writerTxtFile(JsonFileContants.PATH_SYS_RUN_FILE, sb.toString());
	}

	/**
	 * 修改系统设置
	 */
	public boolean updateSettingsFile(SettingsDao sd) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\r\n")
				// 车牌号
				.append("\"" + JsonFileContants.SETTING_LICENSEPLATE + "\"").append(":")
				.append("\"" + sd.getLicensePlate() + "\"").append(",\r\n")
				// 驾驶员
				.append("\"" + JsonFileContants.SETTING_DRIVER + "\"").append(":").append("\"" + sd.getDriver() + "\"")
				.append(",\r\n")
				// 车辆编号
				.append("\"" + JsonFileContants.SETTING_VEHICLENUMBER + "\"").append(":")
				.append("\"" + sd.getJobNumber() + "\"").append(",\r\n")
				// 乘务员
				.append("\"" + JsonFileContants.SETTING_TICKETSELLER + "\"").append(":")
				.append("\"" + sd.getTicketSeller() + "\"").append(",\r\n")
				// 限乘人数
				.append("\"" + JsonFileContants.SETTING_LIMITEDNUMBER + "\"").append(":").append(sd.getLimitedNumber())
				.append("\r\n}");
		return new TxtTools().writerTxtFile(JsonFileContants.PATH_SETTINGS_FILE, sb.toString());
	}

	/**
	 * 经纬度文件
	 */
	public boolean updateLocationFile(int id, double lat, double lon) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\r\n")
				// 序号
				.append("\"" + "编号" + "\"").append(":").append("\"" + id + "\"").append(",\r\n")

				// 经度
				.append("\"" + "纬度" + "\"").append(":").append("\"" + lat + "\"").append(",\r\n")
				// 经度
				.append("\"" + "经度" + "\"").append(":").append("\"" + lon + "\"").append(",\r\n").append("\r\n}");
		return new TxtTools().writeFile(JsonFileContants.FILE_ROOT + "location.txt", sb.toString());
	}

	/**
	 * 站点更新
	 * 
	 * @param stations
	 *            站点列表
	 * @param routeName
	 *            当前路线名称-如：704路上行
	 */
	public static boolean updateStationFile(List<StationDao> stations, String routeName) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\r\n")
				//
				.append("\"" + "站点列表" + "\"").append(":").append("\r\n").append("[").append("\r\n");
		for (int i = 0; i < stations.size(); i++) {
			// 第一个站点
			sb.append("{")
					// 站点编号
					.append("\"" + "站点编号" + "\"").append(":").append(stations.get(i).getStationId()).append(",")
					// 站点标识
					.append("\"" + "站点标识" + "\"").append(":").append(stations.get(i).getStationMark()).append(",")
					
					// 站点名称
					.append("\"" + "站点名称" + "\"").append(":").append("\"" + stations.get(i).getStationName() + "\"")
					.append(",")
					// 站点语音
					.append("\"" + "站点语音" + "\"").append(":").append("\"" + stations.get(i).getStationVoice() + "\"")
					.append(",")
					// 经度
					.append("\"" + "经度" + "\"").append(":")
					.append(new DecimalFormat(".000000").format(stations.get(i).getLongitude())).append(",")
					// 纬度
					.append("\"" + "纬度" + "\"").append(":")
					.append(new DecimalFormat(".000000").format(stations.get(i).getLatitude())).append(",")
					// 角度
					.append("\"" + "角度" + "\"").append(":").append(stations.get(i).getAngle()).append(",")
					// 限速
					.append("\"" + "限速" + "\"").append(":").append(stations.get(i).getLimitSpeed()).append(",")
					// 站前里程
					.append("\"" + "站前里程" + "\"").append(":").append(stations.get(i).getFrontMileage()).append(",")
					// 站点类型
					.append("\"" + "站点类型" + "\"").append(":").append("\"" + stations.get(i).getStationStyle() + "\"")
					.append(",")
					// 出站语音顺序
					.append("\"" + "出站语音顺序" + "\"").append(":")
					.append("\"" + stations.get(i).getOutStationVoice() + "\"").append(",")
					// 进站语音顺序
					.append("\"" + "进站语音顺序" + "\"").append(":")
					.append("\"" + stations.get(i).getInStationVoice() + "\"").append(",")
					// 票价
					.append("\"" + "票价" + "\"").append(":").append("\"" + stations.get(i).getTicket() + "\"")
					.append(",")
					// 音频名
					.append("\"" + "音频名" + "\"").append(":").append("\"" + stations.get(i).getTTSName() + "\"")
					.append(",")
					// 站点间距
					.append("\"" + "站点间距" + "\"").append(":").append("\"" + stations.get(i).getSpacing() + "\"")
					.append(",")
					// 是否播报
					.append("\"" + "是否播报" + "\"").append(":").append("\"" + stations.get(i).getIsBroadcast() + "\"");
			if (i == stations.size() - 1) {
				// 最后一个
				sb.append("}");
			} else {
				sb.append("},");
			}
			sb.append("\r\n");
		}
		sb.append("]").append("\r\n").append("}");
		return new TxtTools().writerTxtFile(JsonFileContants.FILE_LINE + routeName + ".txt", sb.toString());
	}

	/**
	 * 轨迹点更新
	 */
	public static boolean updatePointsFile(List<PointsDao> points, String routeName) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\r\n")
				//
				.append("\"" + "轨迹点列表" + "\"").append(":").append("\r\n").append("[").append("\r\n");
		for (int i = 0; i < points.size(); i++) {
			// 第一个轨迹点
			sb.append("{").append("\"" + "编号" + "\"").append(":").append(i).append(",")
					// 路口标识
					.append("\"" + "路口标识" + "\"").append(":").append(points.get(i).getCrossID()).append(",")
					// 路口名称
					.append("\"" + "路口名称" + "\"").append(":").append(points.get(i).getCrossName()).append(",")
					// 上站点编号
					.append("\"" + "上站点编号" + "\"").append(":").append(points.get(i).getLastStationId()).append(",")
					// 下站点编号
					.append("\"" + "下站点编号" + "\"").append(":").append(points.get(i).getNextStationId()).append(",")
					// 属性
					.append("\"" + "属性" + "\"").append(":").append(points.get(i).getCrossType()).append(",")
					// 经度
					.append("\"" + "经度" + "\"").append(":")
					.append(new DecimalFormat(".000000").format(points.get(i).getLongitude())).append(",")
					// 纬度
					.append("\"" + "纬度" + "\"").append(":")
					.append(new DecimalFormat(".000000").format(points.get(i).getLatitude()));
			if (i == points.size() - 1) {
				// 最后一个
				sb.append("}");
			} else {
				sb.append("},");
			}
			sb.append("\r\n");
		}
		sb.append("]").append("\r\n").append("}");
		return new TxtTools().writerTxtFile(JsonFileContants.FILE_LINE + routeName + "gps.txt", sb.toString());
	}

	/**
	 * 拐弯点更新
	 */
	public static boolean updateCornersFile(ArrayList<MarkerDao> corners, String routeName) {
		JSONArray array = new JSONArray();
		JSONObject o1 = null;
		JSONObject jo2 = new JSONObject();
		MarkerDao cd;
		try {
			for (int j = 0; j < corners.size(); j++) {
				cd = corners.get(j);
				o1 = new JSONObject();
				o1.put(JsonFileContants.CORNER_CORNERID, cd.getCornerId());//标识
				o1.put(JsonFileContants.CORNER_MARK, cd.getCornerOrder());//编号
				o1.put(JsonFileContants.CORNER_CORNERVOICE, cd.getCornerVoice());//播报语音
				o1.put(JsonFileContants.CORNER_NAME, cd.getName());//播报语音
				o1.put(JsonFileContants.CORNER_TYPE, cd.getType());//属性
				o1.put(JsonFileContants.CORNER_LONGITUDE, new DecimalFormat(".000000").format(cd.getLongitude()));//经度
				o1.put(JsonFileContants.CORNER_LATITUDE, new DecimalFormat(".000000").format(cd.getLatitude()));//纬度
				o1.put(JsonFileContants.CORNER_ANGLE, cd.getAngle());//角度
				o1.put(JsonFileContants.CORNER_LIMITSPEED, cd.getLimitSpeed());//限速
				o1.put(JsonFileContants.CORNER_FRONTMILEAGE, cd.getFrontMileage());//站前里程
				o1.put(JsonFileContants.CORNER_ISBROADCAST, cd.getIsBroadcast());//是否播报
				o1.put(JsonFileContants.CORNER_ISREPORT, cd.getIsReport());//是否上报
				o1.put(JsonFileContants.CORNER_STATE, cd.getState());//状态
				array.put(j, o1);
			}
			jo2.putOpt(JsonFileContants.CORNER_NOTIFY, array);
			// LogTool.logD("json", jo2.toString());
			return new TxtTools().writerTxtFile(
					JsonFileContants.FILE_LINE + routeName + JsonFileContants.CORNER_NOTIFY + ".txt",
					jo2.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 更新线路列表
	 */
	public boolean updateRoute(ArrayList<RoutesDao> routes) {
		JSONArray array = new JSONArray();
		JSONObject o1 = null;
		JSONObject jo2 = new JSONObject();
		RoutesDao rd;
		try {
			for (int j = 0; j < routes.size(); j++) {
				rd = routes.get(j);
				o1 = new JSONObject();
				o1.put(JsonFileContants.ROUTE_NAME, rd.getRouteName());
				o1.put(JsonFileContants.ROUTE_START_NAME, rd.getStartTime());
				o1.put(JsonFileContants.ROUTE_END_TIME, rd.getEndTime());
				o1.put(JsonFileContants.ROUTE_LINEMARK, rd.getLineMark());
				o1.put(JsonFileContants.ROUTE_VERSION, rd.getVersion());
				o1.put(JsonFileContants.ROUTE_TTSPATH, rd.getTtsPath());
				o1.put(JsonFileContants.ROUTE_TTSNAME, rd.getTtsName());
				o1.put(JsonFileContants.ROUTE_START_STATION, rd.getStartStation());
				o1.put(JsonFileContants.ROUTE_END_STATION, rd.getEndStation());
				array.put(o1);
			}
			jo2.putOpt(JsonFileContants.ROUTE_LIST, array);
			// LogTool.logD("json", jo2.toString());
			return new TxtTools().writerTxtFile(JsonFileContants.PATH_ROUTES_FILE, jo2.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {

	}
}
