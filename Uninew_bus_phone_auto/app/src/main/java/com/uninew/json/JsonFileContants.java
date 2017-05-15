package com.uninew.json;

import android.os.Environment;

public class JsonFileContants {
	/** 手机SD卡根目录目录 */
	public static final String SDcard = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	/** 手机轨迹点目录 */
	public static final String FILE_POINT = SDcard+"/公交资源文件/轨迹点文件/";
	
	/** 手机线路目录 */
	public static final String FILE_LINE = SDcard+"/公交资源文件/线路/";
	/** 采集轨迹点目录 */
	public static final String FILE_CONNCTLINE = SDcard+"/采集轨迹点/";
	
	/** 线路根目录 */
	//public static final String PATH_CORNERS_FILE = SDcard+"/公交资源文件/线路/";
	
	
	
	//////////////////////////////////FTP//////////////////////////////////////////////////////
	/** FTP 服务器上传目录 */
	public static final String FTP_FILE_UPREMOTEPATH = "/MiddleData/Up/公交资源文件";
	/** FTP 服务器测试目录 */
	public static final String FTP_FILE_TESTREMOTEPATH = "/MiddleData/My/公交资源文件";
	/** FTP 服务器下载目录 */
	public static final String FTP_FILE_DOWNREMOTEPATH = "/MiddleData/Down/公交资源文件";
	
	/** FTP 客户端目录 */
	public static final String FTP_FILE_PHONE = SDcard+"/FTP";
	public static final String FTP_FILE_LINE = SDcard+"/公交资源文件";
	
	
	
	/** 拐弯提醒文件路径 */
	public static final String PATH_CORNERS_FILE_0 ="拐弯提醒.txt";
	
	
	public static final String FILE_ROOT = "mnt/sdcard/公交资源文件/";
	
	////////////////////////////FTP///////////////////////////////////////////////////
	public static final String FTP_CONNECT_SUCCESSS = "FTP连接成功";  
    public static final String FTP_CONNECT_FAIL = "FTP连接失败";  
    public static final String FTP_DISCONNECT_SUCCESS = "FTP断开连接";  
    public static final String FTP_FILE_NOTEXISTS = "FTP上文件不存在";  
      
    public static final String FTP_UPLOAD_SUCCESS = "FTP文件上传成功";  
    public static final String FTP_UPLOAD_FAIL = "FTP文件上传失败";  
    public static final String FTP_UPLOAD_LOADING = "FTP文件正在上传";  
  
    public static final String FTP_DOWN_LOADING = "FTP文件正在下载";  
    public static final String FTP_DOWN_SUCCESS = "FTP文件下载成功";  
    public static final String FTP_DOWN_FAIL = "FTP文件下载失败"; 
      
    public static final String FTP_DELETEFILE_SUCCESS = "FTP文件删除成功";  
    public static final String FTP_DELETEFILE_FAIL = "FTP文件删除失败";
    
    public static final String FTP_UPLOAD_FAIL_NO = "FTP文件不存在";
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	
public static final String FILE_EXTEND_TXT=".txt";
	
	/** 系统运行文件路径 */
	public static final String PATH_SYS_RUN_FILE = FILE_ROOT + "系统运行.txt";
	/** 线路列表信息文件路径 */
	public static final String PATH_ROUTES_FILE = FILE_ROOT + "线路列表.txt";
	/** 线路根目录 */
	public static final String PATH_ROUTES_ROOT = FILE_ROOT + "线路/";
	/** 语音文件所在目录 */
	public static final String PATH_VOICE_ROOT = FILE_ROOT + "语音/";
	/** 服务语音文件所在目录 */
	public static final String PATH_SERVICE_VOICE_ROOT = FILE_ROOT + "语音/"+"服务语/";
	/** 公共语音文件所在目录 */
	public static final String PATH_PUBLIC_VOICE_ROOT = FILE_ROOT + "语音/"+"公共语/";
	/** 语音播报顺序设置文件路径 */
	public static final String PATH_VOICE_RULE_FILE = FILE_ROOT + "语音播报顺序设置文件.txt";
	/** 系统设置文件路径 */
	public static final String PATH_SETTINGS_FILE = FILE_ROOT + "系统设置.txt";
	/** MP3*/
	public static final String MP3_FILE = ".mp3";
	/** wav*/
	public static final String WAV_FILE = ".wav";
	/** 进站广告名称*/
	public static final String ADNAME_INTOSTATION = "进站广告";
	/** 出站广告名称*/
	public static final String ADNAME_OUTSTATION = "出站广告";
	
	///////////////////////////////////////////备份目录///////////////////////////////////////////////////////////////

	public static final String FILE_ROOT_0 = "mnt/sdcard/公交资源文件_备份/";
	/** 系统运行文件路径 */
	public static final String PATH_SYS_RUN_FILE_0 = FILE_ROOT_0 + "系统运行.txt";
	/** 线路列表信息文件路径 */
	public static final String PATH_ROUTES_FILE_0 = FILE_ROOT_0 + "线路列表.txt";
	/** 线路根目录 */
	public static final String PATH_ROUTES_ROOT_0 = FILE_ROOT_0 + "线路/";
	/** 语音文件所在目录 */
	public static final String PATH_VOICE_ROOT_0 = FILE_ROOT_0 + "语音/";
	/** 服务语音文件所在目录 */
	public static final String PATH_SERVICE_VOICE_ROOT_0 = FILE_ROOT_0 + "语音/"+"服务语/";
	/** 公共语音文件所在目录 */
	public static final String PATH_PUBLIC_VOICE_ROOT_0 = FILE_ROOT + "语音/"+"公共语/";
	
	/** 语音播报顺序设置文件路径 */
	public static final String PATH_VOICE_RULE_FILE_0 = FILE_ROOT_0 + "语音播报顺序设置文件.txt";
	/** 系统设置文件路径 */
	public static final String PATH_SETTINGS_FILE_0 = FILE_ROOT_0 + "系统设置.txt";

	// 系统运行文件key
	/** 上行站点 */
	public static final String STATION_LIST = "站点列表";
	/** 站点标识 */
	public static final String STATION_MARK = "站点标识";
	/** 站点编号 */
	public static final String STATION_ID = "站点编号";
	/** 站点名称 */
	public static final String STATION_NAME = "站点名称";
	/** 站点语音 */
	public static final String STATION_VOICE = "站点语音";
	/** 经度 */
	public static final String STATION_LONGITUDE = "经度";
	/** 纬度 */
	public static final String STATION_LATITUDE = "纬度";
	/** 角度 */
	public static final String STATION_ANGLE = "角度";
	/** 限速 */
	public static final String STATION_LIMITSPEED = "限速";
	/** 站前里程 */
	public static final String STATION_FRONTMILEAGE = "站前里程";
	/** 站点类型 */
	public static final String STATION_STYLE = "站点类型";
	/** 进站语音顺序 */
	public static final String STATION_INSTATIONVOICE = "进站语音顺序";
	/** 出站语音顺序 */
	public static final String STATION_OUTSTATIONVOICE = "出站语音顺序";
	/** 是否播报 */
	public static final String STATION_ISBROADCAST = "是否播报";
	/** 是否播报 */
	public static final String STATION_DEFAULT_VOICE = "默认";
	/** 票价 */
	public static final String STATION_TICKET = "票价";
	/** 音频名 */
	public static final String STATION_TTSNAME = "音频名";
	/** 站点间距 */
	public static final String STATION_SPACING = "站点间距";

	// 线路列表信息文件key
	/** 线路列表 */
	public static final String ROUTE_LIST = "线路列表";
	/** 线路名称 */
	public static final String ROUTE_NAME = "线路名称";
	/** 首班时间 */
	public static final String ROUTE_START_NAME = "首班时间";
	/** 末班时间 */
	public static final String ROUTE_END_TIME = "末班时间";
	/** 线路标识 */
	public static final String ROUTE_LINEMARK = "线路标识";
	/** 线路版本 */
	public static final String ROUTE_VERSION = "线路版本";
	/** 音频路径 */
	public static final String ROUTE_TTSPATH = "音频路径";
	/** 线路音频名*/
	public static final String ROUTE_TTSNAME = "线路音频名";
	/** 起始站 */
	public static final String ROUTE_START_STATION = "起始站";
	/** 终点站 */
	public static final String ROUTE_END_STATION = "终点站";
	
	// 站点文件key
	/** 本班次 */
	public static final String RUN_ROUTE_NAME = "本班次";
	/** 线路标识 */
	public static final String RUN_LINEMARK = "线路标识";
	/** 开车时间 */
	public static final String RUN_START_TIME = "开车时间";
	/** 收车时间 */
	public static final String RUN_END_TIME = "收车时间";
	/** 起始站 */
	public static final String RUN_START_STATION = "起始站";
	/** 终点站 */
	public static final String RUN_END_STATION = "终点站";

	// 拐弯提醒文件key
	/** 拐弯提醒 */
	public static final String CORNER_NOTIFY = "拐弯提醒";
	/** 编号 */
	public static final String CORNER_CORNERID = "编号";
	/** 标识 */
	public static final String CORNER_MARK = "标识";
	/** 语音 */
	public static final String CORNER_CORNERVOICE = "语音";
	/** 类型 */
	public static final String CORNER_TYPE = "属性";
	/** 经度 */
	public static final String CORNER_LONGITUDE = "经度";
	/** 纬度 */
	public static final String CORNER_LATITUDE = "纬度";
	/** 角度 */
	public static final String CORNER_ANGLE = "角度";
	/** 限速 */
	public static final String CORNER_LIMITSPEED = "限速";
	/** 站前里程 */
	public static final String CORNER_FRONTMILEAGE = "站前里程";
	/** 是否播报 */
	public static final String CORNER_ISBROADCAST = "是否播报";
	/** 是否上报 */
	public static final String CORNER_ISREPORT = "是否上报";
	/** 名称 */
	public static final String CORNER_NAME = "名称";
	/** 状态*/
	public static final String CORNER_STATE = "状态";

	// 语音播报顺序设置文件key
	/** 起点出站 */
	public static final String VOICE_STARTOUT = "起点出站";
	/** 进站播报 */
	public static final String VOICE_ARRIVESTATION = "进站播报";
	/** 出站播报 */
	public static final String VOICE_OUTSTATION = "出站播报";
	/** 终点前站出站播报 */
	public static final String VOICE_LASTSENCONDOUT = "终点前站出站播报";
	/** 终点进站播报 */
	public static final String VOICE_ARRIVETHEEND = "终点进站播报";
	/** 起点外音 */
	public static final String VOICE_STARTOUTVOICE = "起点外音";
	/** 进站外音 */
	public static final String VOICE_ARRIVEOUTVOICE = "进站外音";
	/** 出站外音 */
	public static final String VOICE_OUTSTATIONVOICE = "出站外音";

	// 系统设置文件key
	/** 车牌号 */
	public static final String SETTING_LICENSEPLATE = "车牌号";
	/** 驾驶员 */
	public static final String SETTING_DRIVER = "驾驶员";
	/** 车辆编号 */
	public static final String SETTING_VEHICLENUMBER = "车辆编号";
	/** 乘务员 */
	public static final String SETTING_TICKETSELLER = "乘务员";
	/** 限乘人数 */
	public static final String SETTING_LIMITEDNUMBER = "限乘人数";

	// 轨迹点文件key
	/** 轨迹点列表 */
	public static final String POINTS_LIST = "轨迹点列表";
	/** 编号 */
	public static final String POINTS_ID = "编号";
	/** 上站点编号 */
	public static final String POINTS_LAST_STATIONID = "上站点编号";
	/** 下站点编号 */
	public static final String POINTS_NEXT_STATIONID = "下站点编号";
	/** 经度 */
	public static final String POINTS_LONGITUDE = "经度";
	/** 纬度 */
	public static final String POINTS_LATITUDE = "纬度";
	/** 属性*/
	public static final String POINTS_CROSSTYPE = "属性";
	/** 路口标识*/
	public static final String POINTS_CROSS_ID = "路口标识";
}
