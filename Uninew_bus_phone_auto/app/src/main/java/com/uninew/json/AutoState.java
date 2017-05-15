package com.uninew.json;

import java.util.ArrayList;
import java.util.List;

import com.uninew.file.dao.MarkerDao;
import com.uninew.file.dao.StationDao;
import com.uninew.util.auto.TrackPointStruct;

/**
 * 该类保存自动报站设置的数据
 * 
 * @author Administrator
 * 
 */
public class AutoState {

	// setting
	private int autoMode = 0;// 自动报站模式：1 模拟轨迹点；2 Gps;0 无
	private ArrayList<String> pointFilelist = new ArrayList<>();// 轨迹点文件
	private ArrayList<String> lineFilelist = new ArrayList<>();// 线路文件
	private ArrayList<TrackPointStruct> currentPointlist = new ArrayList<>();// 当前轨迹点集合
	private ArrayList<StationDao> upstation = new ArrayList<>();// 上行站点
	private ArrayList<StationDao> downstation = new ArrayList<>();// 下行站点
	
	private ArrayList<MarkerDao> UpMarkerDaolist = new ArrayList<>();// 上行拐点
	private ArrayList<MarkerDao> DownMarkerDaolist = new ArrayList<>();// 下行行拐点
	
	private  int mCurrenlineIndex = 0;//当前线路编号
	private int mRundirection = 0; //运行方向 0：未知；1：上行；2：下行；
	
	private String mCurrenlineName = "";//当前线路名称
	private StationDao mCurrentStation = null;//当前站点
	private Boolean isTrackRecord = false; //是否采集轨迹点
	
	//GPS
	private Boolean mLocationValid = false;//是否定位成功
	private String mLocationState = "未定位";//定位状态
	
	private int baiduMode = 0;//显示模式 0：标准图 1：卫星图
	//ftp
	private String FTP_user = "";
	private String FTP_id = "";
	private String FTP_password = "";
	
	
	
	public String getFTP_user() {
		return FTP_user;
	}
	public void setFTP_user(String fTP_user) {
		FTP_user = fTP_user;
	}
	public String getFTP_id() {
		return FTP_id;
	}
	public void setFTP_id(String fTP_id) {
		FTP_id = fTP_id;
	}
	public String getFTP_password() {
		return FTP_password;
	}
	public void setFTP_password(String fTP_password) {
		FTP_password = fTP_password;
	}
	
	
	
	
	public ArrayList<MarkerDao> getDownMarkerDaolist() {
		return DownMarkerDaolist;
	}
	public void setDownMarkerDaolist(ArrayList<MarkerDao> downMarkerDaolist) {
		DownMarkerDaolist = downMarkerDaolist;
	}
	
	public ArrayList<MarkerDao> getUpMarkerDaolist() {
		return UpMarkerDaolist;
	}
	public void setUpMarkerDaolist(ArrayList<MarkerDao> upmarkerDaolist) {
		UpMarkerDaolist = upmarkerDaolist;
	}
	
	public StationDao getmCurrentStation() {
		return mCurrentStation;
	}
	public void setmCurrentStation(StationDao mCurrentStation) {
		this.mCurrentStation = mCurrentStation;
	}
	
	public int getBaiduMode() {
		return baiduMode;
	}
	public void setBaiduMode(int baiduMode) {
		this.baiduMode = baiduMode;
	}
	public String getmLocationState() {
		return mLocationState;
	}
	public void setmLocationState(String mLocationState) {
		this.mLocationState = mLocationState;
	}
	public String getmCurrenlineName() {
		return mCurrenlineName;
	}
	public void setmCurrenlineName(String mCurrenlineName) {
		this.mCurrenlineName = mCurrenlineName;
	}
	public boolean getTrackRecord() {
		return isTrackRecord;
	}
	public void setTrackRecord(boolean isTrackRecord) {
		this.isTrackRecord = isTrackRecord;
	}
	public int getmRundirection() {
		return mRundirection;
	}
	public void setmRundirection(int mRundirection) {
		this.mRundirection = mRundirection;
	}
	
	public Boolean getmLocationValid() {
		return mLocationValid;
	}
	public void setmLocationValid(Boolean mLocationValid) {
		this.mLocationValid = mLocationValid;
	}
	public int getmCurrenlineIndex() {
		return mCurrenlineIndex;
	}
	public void setmCurrenlineIndex(int mCurrenlineIndex) {
		this.mCurrenlineIndex = mCurrenlineIndex;
	}
	public int getAutoMode() {
		return autoMode;
	}
	public void setAutoMode(int autoMode) {
		this.autoMode = autoMode;
	}
	public ArrayList<String> getPointFilelist() {
		return pointFilelist;
	}
	public void setPointFilelist(ArrayList<String> pointFilelist) {
		this.pointFilelist = pointFilelist;
	}
	public ArrayList<String> getLineFilelist() {
		return lineFilelist;
	}
	public void setLineFilelist(ArrayList<String> lineFilelist) {
		this.lineFilelist = lineFilelist;
	}
	public ArrayList<TrackPointStruct> getCurrentPointlist() {
		return currentPointlist;
	}
	public void setCurrentPointlist(ArrayList<TrackPointStruct> currentPointlist) {
		this.currentPointlist = currentPointlist;
	}
	public ArrayList<StationDao> getUpstation() {
		return upstation;
	}
	public void setUpstation(ArrayList<StationDao> upstation) {
		this.upstation = upstation;
	}
	public ArrayList<StationDao> getDownstation() {
		return downstation;
	}
	public void setDownstation(ArrayList<StationDao> downstation) {
		this.downstation = downstation;
	}
	
	
}
