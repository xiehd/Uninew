package com.uninew.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.ArcOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption.DrivingPolicy;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.uninew.auto.phone.R;
import com.uninew.file.dao.StationDao;
import com.uninew.json.AutoState;

public class Map_Utils {
	private LocationClient locationClient;
	private BitmapDescriptor geo;
	private Context mContext;
	private BaiduMap mBaiduMap;
	private ArrayList<LatLng> points;// 当前绘制路线
	private AutoState mAutoState;

	public Map_Utils() {

	}

	public Map_Utils(Context cn, BaiduMap map, AutoState mAutoState) {
		mContext = cn;
		mBaiduMap = map;
		this.mAutoState = mAutoState;

	}

	public static BaiduMapOptions setClsat() {
		BaiduMapOptions mapOptions = new BaiduMapOptions();
		mapOptions.scaleControlEnabled(false); // 隐藏比例尺控件
		mapOptions.zoomControlsEnabled(false);// 隐藏缩放按钮
		mapOptions.compassEnabled(true); // 设置是否允许指南针，默认允许。
		// BaiduMapOptions mapStatus(MapStatus status) //设置地图初始化时的地图状态，
		// 默认地图中心点为北京天安门，缩放级别为 12.0f
		// BaiduMapOptions mapType(int mapType) //设置地图模式，默认普通地图
		// BaiduMapOptions overlookingGesturesEnabled(boolean enabled)
		// //设置是否允许俯视手势，默认允许
		// BaiduMapOptions rotateGesturesEnabled(boolean enabled)
		// //设置是否允许旋转手势，默认允许
		// BaiduMapOptions scaleControlEnabled(boolean enabled) //设置是否显示比例尺控件
		// BaiduMapOptions scrollGesturesEnabled(boolean enabled)
		// //设置是否允许拖拽手势，默认允许
		// BaiduMapOptions zoomControlsEnabled(boolean enabled) //设置是否显示缩放控件
		// BaiduMapOptions zoomGesturesEnabled(boolean enabled) //设置是否允许缩放手势
		// 通过控制BaiduMapOptions的相应选项
		return mapOptions;
	}

	public void setMapShowMode(int state, BaiduMap baiduMap) {
		if (mBaiduMap == null) {
			return;
		}
		if (state == 0) {// 标准图 实时路况图
			mBaiduMap.setTrafficEnabled(true);
			mBaiduMap.setMyLocationEnabled(true);
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		} else if (state == 1) {// 卫星图
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			// mBaiduMap.setBaiduHeatMapEnabled(true);
		} else if (state == 2) {// 百度城市热力图
			mBaiduMap.setBaiduHeatMapEnabled(true);
		} else if (state == 3) {// 定位图层
			mBaiduMap.setMyLocationEnabled(true);
		}
	}

	public void initMap() {

		points = new ArrayList<LatLng>();
		// 初始化地图
		// 缩放and移动视角
		if (mBaiduMap == null)
			return;
		mBaiduMap.clear();
		MapStatusUpdate u2 = MapStatusUpdateFactory.zoomTo(16);
		mBaiduMap.animateMapStatus(u2);

		if (mAutoState.getBaiduMode() == 0) {
			setMapShowMode(0, null);
		} else {
			setMapShowMode(1, null);
		}
		addLine(mAutoState.getDownstation());
		showLine(Color.BLUE);
		addLine(mAutoState.getUpstation());
		showLine(Color.YELLOW);
		// 界面加载时添加绘制图层,测试
		// addCustomElementsDemo();

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Looper.prepare();
				offLineInit();// 加载离线地图
				Looper.loop();
			}
		}).start();

		// Marker 点击监听事件
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker mark) {
				// 创建InfoWindow展示的view
				Button button = new Button(mContext.getApplicationContext());
				// button.setBackgroundResource(R.drawable.popup);
				// 定义用于显示该InfoWindow的坐标点
				LatLng pt = mark.getPosition();
				button.setText(mark.getTitle().toString());
				// 创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
				InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
				// 显示InfoWindow
				mBaiduMap.showInfoWindow(mInfoWindow);
				// mBaiduMap.hideInfoWindow();
				return true;
			}
		});

	}

	/**
	 * 修改中心点
	 */
	public void setLocate(double lat, double lon) {
		if (mBaiduMap != null) {
			// 显示当前位置
			LatLng point = new LatLng(lat, lon);
			LatLng changLocation = changLocation(point);
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(changLocation);
			mBaiduMap.setMapStatus(u);
		}
	}

	private Marker marker;

	
	/**
	 *  重绘marker
	 * @param lat
	 * @param lon
	 */
	public void setCenterLang(double lat, double lon) {
		if (mBaiduMap != null) {
			LatLng point = new LatLng(lat, lon);
			LatLng changLocation = changLocation(point);
			// 构建MarkerOption，用于在地图上添加Marker
			if (marker != null) {
				marker.remove();
			}
			BitmapDescriptor bitmap = BitmapDescriptorFactory
					.fromResource(R.mipmap.bus_run2);
			OverlayOptions option = new MarkerOptions().position(changLocation)
					.title("纬度：" + lat + ",经度：" + lon).icon(bitmap);
			// 生成GEO类型坐标并在地图上定位到该坐标标示的地点

			// System.out.println("---"+location.getLatitude()
			// +":"+location.getLongitude());
			// 在地图上添加Marker，并显示
			marker = (Marker) mBaiduMap.addOverlay(option);
		}
	}

	// 绘制站点
	private void addLine(List<StationDao> line) {
		if (line == null || line.size() <= 0) {
			Toast.makeText(mContext.getApplicationContext(), "地图轨迹为空", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (line.size() > 0) {
			for (int i = 0; i < line.size(); i++) {
				LatLng latLng = new LatLng(line.get(i).getLatitude(), line.get(
						i).getLongitude());
				LatLng latLng2 = changLocation(latLng);
				points.add(latLng2);

				BitmapDescriptor bitmap_start = BitmapDescriptorFactory
						.fromResource(R.mipmap.icon_bus_station);
				OverlayOptions option_stste = new MarkerOptions()
						.position(latLng2).title(line.get(i).getStationName())
						.icon(bitmap_start);
				mBaiduMap.addOverlay(option_stste);
			}
		}
	}

	// 显示站点路线
	private void showLine(int color) {
		if (points.size() > 2 && points != null) {
			OverlayOptions ooPolyline = new PolylineOptions().width(10)
					.color(color).points(points);
			mBaiduMap.addOverlay(ooPolyline);
			points.clear();
		}
	}

	/**
	 * GPS坐标转换成百度坐标
	 * 
	 * @param sourceLatLng
	 * @return
	 */
	private LatLng changLocation(LatLng sourceLatLng) {
		CoordinateConverter converter = new CoordinateConverter();
		converter.from(CoordType.GPS);
		// sourceLatLng待转换坐标
		converter.coord(sourceLatLng);
		LatLng desLatLng = converter.convert();
		return desLatLng;
	}

	/**
	 * 添加点、线、多边形、圆、文字
	 */
	public void addCustomElementsDemo() {
		// 添加折线
		LatLng p1 = new LatLng(39.97923, 116.357428);
		LatLng p2 = new LatLng(39.94923, 116.397428);
		LatLng p3 = new LatLng(39.97923, 116.437428);
		List<LatLng> points = new ArrayList<LatLng>();
		points.add(p1);
		points.add(p2);
		points.add(p3);
		OverlayOptions ooPolyline = new PolylineOptions().width(10)
				.color(0xAAFF0000).points(points);
		mBaiduMap.addOverlay(ooPolyline);
		// 添加弧线
		OverlayOptions ooArc = new ArcOptions().color(0xAA00FF00).width(4)
				.points(p1, p2, p3);
		mBaiduMap.addOverlay(ooArc);
		// 添加圆
		LatLng llCircle = new LatLng(39.90923, 116.447428);
		OverlayOptions ooCircle = new CircleOptions().fillColor(0x000000FF)
				.center(llCircle).stroke(new Stroke(5, 0xAA000000))
				.radius(1400);
		mBaiduMap.addOverlay(ooCircle);

		LatLng llDot = new LatLng(39.98923, 116.397428);
		OverlayOptions ooDot = new DotOptions().center(llDot).radius(6)
				.color(0xFF0000FF);
		mBaiduMap.addOverlay(ooDot);
		// 添加多边形
		LatLng pt1 = new LatLng(39.93923, 116.357428);
		LatLng pt2 = new LatLng(39.91923, 116.327428);
		LatLng pt3 = new LatLng(39.89923, 116.347428);
		LatLng pt4 = new LatLng(39.89923, 116.367428);
		LatLng pt5 = new LatLng(39.91923, 116.387428);
		List<LatLng> pts = new ArrayList<LatLng>();
		pts.add(pt1);
		pts.add(pt2);
		pts.add(pt3);
		pts.add(pt4);
		pts.add(pt5);
		OverlayOptions ooPolygon = new PolygonOptions().points(pts)
				.stroke(new Stroke(5, 0xAA00FF00)).fillColor(0xAAFFFF00);
		mBaiduMap.addOverlay(ooPolygon);
		// 添加文字
		LatLng llText = new LatLng(39.86923, 116.397428);
		OverlayOptions ooText = new TextOptions().bgColor(0xAAFFFF00)
				.fontSize(24).fontColor(0xFFFF00FF).text("百度地图SDK").rotate(-30)
				.position(llText);
		mBaiduMap.addOverlay(ooText);
	}

	/**
	 * 离线地图加载
	 */
	private MKOfflineMap mOffline;

	private void offLineInit() {
		// TODO Auto-generated method stub
		mOffline = new MKOfflineMap();

		mOffline.init(new MKOfflineMapListener() {
			public void onGetOfflineMapState(int type, int state) {
				switch (type) {
				case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
					MKOLUpdateElement update = mOffline.getUpdateInfo(state);
					// mText.setText(String.format("%s : %d%%", update.cityName,
					// update.ratio));
				}
					break;
				case MKOfflineMap.TYPE_NEW_OFFLINE:
					Log.d("OfflineDemo",
							String.format("add offlinemap num:%d", state));
					break;
				case MKOfflineMap.TYPE_VER_UPDATE:
					Log.d("OfflineDemo", String.format("new offlinemap ver"));
					break;
				}
			}
		});

		/** 离线地图导入离线包 **/
		int num = mOffline.importOfflineData();

		String msg = "";
		if (num == 0) {
			msg = "没有导入离线包，这可能是离线包放置位置不正确，或离线包已经导入过";
		} else {
			msg = String.format("成功导入 %d 个离线包，可以在下载管理查看", num);
		}
		Log.i("MapActivity", "offOlineMap:" + msg);
	}

	// ////////////////////////////////定位//////////////////////////////////////////////////////////////////
	public void lacate() {
		locationClient = new LocationClient(mContext);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd0911");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		locationClient.setLocOption(option);
		locationClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation result) {
				if (result != null) {
					MyLocationData data = new MyLocationData.Builder()
							.latitude(result.getLatitude())
							.longitude(result.getLongitude()).build();
					mBaiduMap.setMyLocationData(data);
				}
			}
		});
		geo = BitmapDescriptorFactory.fromResource(R.mipmap.icon_bus_station);
		MyLocationConfiguration configuration = new MyLocationConfiguration(
				MyLocationConfiguration.LocationMode.FOLLOWING, true, geo);
		mBaiduMap.setMyLocationConfigeration(configuration);// 设置定位模式
		mBaiduMap.setMyLocationEnabled(true);// 打开定位图层
	}

	// //////////////////////////路线规划//////////////////////////////////////////////////////////////////
	/**
	 * 驾车路线
	 * 
	 * @author mwp
	 * 
	 */
	private RoutePlanSearch routePlanSearch;

	public void search(String start, String end) {
		routePlanSearch = RoutePlanSearch.newInstance();

		DrivingRoutePlanOption drivingOption = new DrivingRoutePlanOption();
		// PlanNode from = PlanNode.withLocation(start);//创建起点
		// PlanNode to = PlanNode.withLocation(end);//创建终点

		PlanNode from = PlanNode.withCityNameAndPlaceName("深圳", start);// 创建起点
		PlanNode to = PlanNode.withCityNameAndPlaceName("深圳", end);// 创建终点
		drivingOption.from(from);// 设置起点
		drivingOption.to(to);// 设置终点
		Log.i("My", "线路规划111");
		List<PlanNode> nodes = new ArrayList<PlanNode>();
		nodes.add(PlanNode.withCityNameAndPlaceName("深圳", "世界之窗"));
		drivingOption.passBy(nodes);
		drivingOption.policy(DrivingPolicy.ECAR_DIS_FIRST);// 设置策略，驾乘检索策略常量：最短距离
		Log.i("My", "线路规划000");
		routePlanSearch.drivingSearch(drivingOption);

		routePlanSearch
				.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {

					@Override
					public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
						Log.i("My", "线路规划333");
					}

					@Override
					public void onGetTransitRouteResult(TransitRouteResult arg0) {
						Log.i("My", "线路规划444");
					}

					// 自驾搜索结果回调
					@Override
					public void onGetDrivingRouteResult(
							DrivingRouteResult result) {
						if (result == null
								|| SearchResult.ERRORNO.RESULT_NOT_FOUND == result.error) {
							Toast.makeText(mContext, "未搜索到结果", Toast.LENGTH_SHORT).show();
							Log.i("My", "未搜索到结果");
							return;
						}
						if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
							// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
							// result.getSuggestAddrInfo()
							Log.d("My", "起终点或途经点地址有岐义");
							return;
						}
						if (result.error == SearchResult.ERRORNO.PERMISSION_UNFINISHED) {
							// 权限鉴定未完成则再次尝试
							Log.d("My", "权限鉴定未完成,再次尝试");
							return;
						}
						Log.i("My", "线路规划222");
						if (result.error == SearchResult.ERRORNO.NO_ERROR) {
							DrivingRouteOverlay drivingOverlay = new MyDrivingOverlay(
									mBaiduMap);
							mBaiduMap.setOnMarkerClickListener(drivingOverlay);
							drivingOverlay.setData(result.getRouteLines()
									.get(0));// 设置线路为搜索结果的第一条
							drivingOverlay.addToMap();
							drivingOverlay.zoomToSpan();
							Log.i("My", "一共搜索到："
									+ result.getRouteLines().size() + "条路线");
						}
					}

				});
	}

	class MyDrivingOverlay extends DrivingRouteOverlay {
		public MyDrivingOverlay(BaiduMap arg0) {
			super(arg0);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			// 覆写此方法以改变默认起点图标
			return BitmapDescriptorFactory.fromResource(R.mipmap.icon_start);
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			// 覆写此方法以改变默认终点图标
			return BitmapDescriptorFactory.fromResource(R.mipmap.icon_end);
		}
	}

}
