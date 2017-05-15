package com.uninew.auto.phone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.iflytek.cloud.SpeechUtility;
import com.uninew.file.dao.StationDao;
import com.uninew.json.AutoState;
import com.uninew.json.JsonFileContants;
import com.uninew.json.JsonParse;
import com.uninew.json.TxtTools;
import com.uninew.utils.FileText;
import com.uninew.utils.Map_Utils;
import com.uninew.utils.TtsUtil;
import com.uninew.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import uninew.bus.report.auto.AutoReportStation;
import uninew.bus.report.auto.IAutoReportListener;


public class MainActivity extends FragmentActivity implements OnClickListener,
        IAutoReportListener {

    private static String TGA = "MainActivity";
    private MainApplication app;
    private static AutoState mAutoState;
    private static AutoReportStation pAlgorithmClass;
    private TtsUtil tts;

    private JsonParse jsonParse;
    private static int sinmutalespeech = 200;// 模拟速度
    private static int pIndex = 0;// 当前序号
    private TextView main_msg;
    private ImageView img_back;
    private Button main_btn_mode;
    private TextView top_title_txt, mode_imge;
    private TextView title_switch_txtmode;
    // 地图
    private com.baidu.mapapi.map.MapView mMapView;
    private Map_Utils mMap;
    // GPS
    private LocationManager locationManager;
    private PowerManager pm;
    private PowerManager.WakeLock mWakeLock;

    private Handler mhander = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            // TODO Auto-generated method stub
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:// 模拟报站 连续
                    if (pIndex < mAutoState.getCurrentPointlist().size()) {

                        if (MainApplication.logo)
                            Log.e(TGA, "模拟运行");
                        MainApplication.latitude = mAutoState.getCurrentPointlist()
                                .get(pIndex).getLatitude();
                        MainApplication.longitude = mAutoState
                                .getCurrentPointlist().get(pIndex).getLongitude();
                        MainApplication.time = mAutoState.getCurrentPointlist()
                                .get(pIndex).getTime();
                        mMap.setCenterLang(MainApplication.latitude,
                                MainApplication.longitude);
                        pAlgorithmClass.setGpsInfo(1, MainApplication.longitude,
                                MainApplication.latitude, 5.0f, 6,
                                MainApplication.time);
                        pIndex++;
                        mhander.removeMessages(0);
                        mhander.sendEmptyMessageDelayed(0, sinmutalespeech);
                    } else {
                        Toast.makeText(getApplicationContext(), "运行结束或站点有误", Toast.LENGTH_LONG)
                                .show();
                        Log.e(TGA, "运行结束或站点有误");
                        tts.startSpeak("自动模拟报站结束");
                        FileText.close();
                        pAlgorithmClass = null;
                        pIndex = 0;
                        main_btn_mode.setText("开始");
                        title_switch_txtmode.setText("开始");
                        mAutoState.setAutoMode(0);
                    }
                    break;
                case 1:// 模拟报站 单步
                    if (pIndex < mAutoState.getCurrentPointlist().size()) {
                        MainApplication.latitude = mAutoState.getCurrentPointlist()
                                .get(pIndex).getLatitude();
                        MainApplication.longitude = mAutoState
                                .getCurrentPointlist().get(pIndex).getLongitude();
                        MainApplication.time = mAutoState.getCurrentPointlist()
                                .get(pIndex).getTime();

                        pAlgorithmClass.setGpsInfo(1, MainApplication.longitude,
                                MainApplication.latitude, 5.0f, 6,
                                MainApplication.time);
                        pIndex++;
                    } else {
                        Toast.makeText(getApplicationContext(), "运行结束或站点有误", Toast.LENGTH_LONG)
                                .show();
                        Log.e(TGA, "运行结束或站点有误");
                        tts.startSpeak("自动模拟报站结束");
                        FileText.close();
                        pAlgorithmClass = null;
                        pIndex = 0;
                        main_btn_mode.setText("开始");
                    }
                    ;
                    break;
                case 2:// gps
                    if (pAlgorithmClass == null) {
                        return;
                    }
                    if (MainApplication.logo)
                        Log.e(TGA, "GPS运行");
                    pAlgorithmClass.setGpsInfo(1, MainApplication.longitude,
                            MainApplication.latitude, 5.0f, 6,
                            MainApplication.time);
                    mhander.removeMessages(2);
                    mhander.sendEmptyMessageDelayed(2, 1000);
                    break;
                case 3:// 地图线路初始化
                    if (mMap != null) {
                        mMap.initMap();
                        if (mAutoState.getDownstation().size() > 0) {
                            mMap.setLocate(mAutoState.getDownstation().get(0)
                                    .getLatitude(), mAutoState.getDownstation()
                                    .get(0).getLongitude());
                            mMap.setCenterLang(mAutoState.getDownstation().get(0)
                                    .getLatitude(), mAutoState.getDownstation()
                                    .get(0).getLongitude());
                        } else {
                            mMap.setLocate(MainApplication.latitude, MainApplication.longitude);
                            mMap.setCenterLang(MainApplication.latitude, MainApplication.longitude);
                        }
                    }
                    break;
                case 4://
                    main_msg.setText(msg.obj.toString());
                    break;
                case 5://
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:// 进站/出站语音播报
                    String msgMesag = msg.obj.toString();
                    if (msg != null) {
                        tts.startSpeak(msgMesag);
                    }
                    // Intent up = new Intent();
                    // up.setAction("Test.INOUTmassge");
                    // up.putExtra("state", msg.obj.toString());
                    // sendBroadcast(up);
                    break;
                case 9:

                    break;
                case 10:// 页面更新
                    break;
                default:
                    break;
            }
        }
    };

    private TextView left_title_txt_conncet, left_title_txt_line,
            left_title_txt_ftp;
    // private SlidingMenu menu;

    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        SDKInitializer.initialize(getApplicationContext());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕不变黑

        setContentView(R.layout.activity_main);
        SpeechUtility.createUtility(getApplicationContext(), "appid="
                + getString(R.string.app_id));

        tts = TtsUtil.getInstance(getApplicationContext());
        app = MainApplication.getInstance();
        mAutoState = MainApplication.getInstance().getmAutoState();

//		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");


        mMapView = (MapView) findViewById(R.id.bmapview);
        mMap = new Map_Utils(this, mMapView.getMap(), mAutoState);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        // Utils.setDrawerLeftEdgeSize(this,drawerLayout,0.2f);//

        // drawerLayout.openDrawer(GravityCompat.START);
        // drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        jsonParse = new JsonParse();
        // 监听位置变化
        locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        // /////////////////////////////////////////////////////////////////////////////////////////

		/*
         * menu = new SlidingMenu(this); menu.setMode(SlidingMenu.LEFT); //
		 * 设置触摸屏幕的模式 menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		 * menu.setShadowWidthRes(R.dimen.shadow_width); // 设置滑动菜单视图的宽度
		 * menu.setBehindOffsetRes(R.dimen.slidingmenu_offset); //
		 * menu.setShadowDrawable(R.drawable.shadow); // 设置渐入渐出效果的值
		 * menu.setFadeDegree(0.35f); menu.attachToActivity(this,
		 * SlidingMenu.SLIDING_CONTENT); // 为侧滑菜单设置布局
		 * menu.setMenu(R.layout.leftmenu); //menu.showMenu();
		 */
        // ///////////////////////////////////////////////////////////////////////////////////
        initView();
        init();
        initDate();

    }

    private void startThisActivity(Class clazz) {
        try {
            Intent intent = new Intent(this, clazz);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void initView() {
        top_title_txt = (TextView) findViewById(R.id.top_title_txt);
        top_title_txt.setText("主页");
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setImageDrawable(getResources()
                .getDrawable(R.mipmap.imgmain));
        img_back.setOnClickListener(this);

        left_title_txt_conncet = (TextView) findViewById(R.id.left_title_txt_conncet);
        left_title_txt_line = (TextView) findViewById(R.id.left_title_txt_line);
        left_title_txt_ftp = (TextView) findViewById(R.id.left_title_txt_ftp);

        main_msg = (TextView) findViewById(R.id.main_msg);
        main_btn_mode = (Button) findViewById(R.id.main_btn_mode);
        main_btn_mode.setOnClickListener(this);

        left_title_txt_conncet.setOnClickListener(this);
        left_title_txt_line.setOnClickListener(this);
        left_title_txt_ftp.setOnClickListener(this);

        mode_imge = (TextView) findViewById(R.id.mode_imge);
        mode_imge.setOnClickListener(this);

        title_switch_txtmode = (TextView) findViewById(R.id.title_switch_txtmode);
        title_switch_txtmode.setVisibility(View.VISIBLE);

//		title_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO Auto-generated method stub
//				if(isChecked){
//					Toast.makeText(getApplicationContext(), "开启轨迹点采集", Toast.LENGTH_SHORT).show();
//					mAutoState.setTrackRecord(true);
//				}else{
//					Toast.makeText(getApplicationContext(), "关闭轨迹点采集", Toast.LENGTH_SHORT).show();
//					mAutoState.setTrackRecord(false);
//				}
//				
//				
//			}
//		});

    }

    // 初始化数据
    private void initDate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Utils.getPointFiles(mAutoState.getPointFilelist());
                Utils.getLineFiles(mAutoState.getLineFilelist());
                int i = mAutoState.getmCurrenlineIndex();
                Log.d(TGA, "route_num:" + i);
                if (mAutoState.getLineFilelist() != null && !mAutoState.getLineFilelist().isEmpty()) {
                    mAutoState.setmCurrenlineName(mAutoState.getLineFilelist().get(i));

                    mAutoState.setUpstation(jsonParse.getStations(mAutoState
                            .getmCurrenlineName() + "上行"));
                    mAutoState.setDownstation(jsonParse.getStations(mAutoState
                            .getmCurrenlineName() + "下行"));
                    mAutoState.setUpMarkerDaolist(jsonParse.getCorners(mAutoState
                            .getmCurrenlineName() + "上行"));
                    mAutoState.setDownMarkerDaolist(jsonParse.getCorners(mAutoState
                            .getmCurrenlineName() + "下行"));
                    Utils.readGPSFile(mAutoState.getmCurrenlineName() + "gps.txt",
                            mAutoState.getCurrentPointlist());

                }
            }
        }).start();
    }

    private void init() {
        // fragment_list.add(new )
    }

    private void changeTitle(int id) {
        switch (id) {
            case 0:// gps
                break;
            case 1:// setting
                break;
            case 2:// simulate
                break;
            case 3:
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            // case R.id.title_setting:
            // changeTitle(1);
            // break;
            // case R.id.title_GPS:
            // changeTitle(0);
            // break;
            // case R.id.title_simulate:
            // changeTitle(2);
            // break;
            case R.id.left_title_txt_ftp:
                startThisActivity(FtpActivity.class);
                break;
            case R.id.left_title_txt_conncet:
                if (!locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(getApplicationContext(), "未开启GPS导航...",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                startThisActivity(CollectActivity.class);
                break;
            case R.id.left_title_txt_line:
                startThisActivity(LineActivity.class);
                break;

            case R.id.main_btn_mode:
                String mode = main_btn_mode.getText().toString();
                if (mode.equals("模拟")) {
                    if (!locationManager
                            .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Toast.makeText(getApplicationContext(), "未开启GPS导航，不能实时报站",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    starGpsFragmentAuto();

                } else if (mode.equals("GPS")) {
                    starFragmentSimulateAuto(pIndex, sinmutalespeech, 3);
                    starFragmentSimulateAuto(pIndex, sinmutalespeech, 0);
                } else if (mode.equals("开始")) {
                    if (starGpsFragmentAuto()) {

                    } else {
                        starFragmentSimulateAuto(pIndex, sinmutalespeech, 3);
                        starFragmentSimulateAuto(pIndex, sinmutalespeech, 0);
                    }
                }
                break;
            case R.id.img_back:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.mode_imge:
                if (mAutoState.getBaiduMode() == 1) {
                    mAutoState.setBaiduMode(0);
                    mode_imge.setBackgroundResource(
                            R.mipmap.baidumode1);
                    mMap.setMapShowMode(0, null);
                } else {
                    mAutoState.setBaiduMode(1);
                    mode_imge.setBackgroundResource(
                            R.mipmap.baidumode2);
                    mMap.setMapShowMode(1, null);
                }
                break;
            case R.id.btn_loggo:// 定位到当前
                mMap.setLocate(MainApplication.latitude, MainApplication.longitude);
//			if (row == 0) {
//				new Thread(mLog_Send_Runnable).start();// 开启日志记录
//			}
                break;
            default:
                break;
        }
    }

    class MyPagerChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
        }

    }

    public boolean starFragmentSimulateAuto(int starindex, int sinmutalespeech,
                                            int mode) {
        // TODO Auto-generated method stub
        if (mAutoState.getUpstation().size() <= 0
                || mAutoState.getDownstation().size() <= 0
                || mAutoState.getCurrentPointlist().size() <= 0) {
            Toast.makeText(getApplicationContext(), "当前轨迹点或线路未加载，请先加载",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        main_btn_mode.setText("模拟");
        title_switch_txtmode.setText("模拟");
        mAutoState.setAutoMode(1);
        stopGpsFragmentAuto();
        if (mode == 3) {
            pAlgorithmClass = new AutoReportStation(this);
            pAlgorithmClass.setStations(true, 0, mAutoState.getUpstation());
            pAlgorithmClass.setStations(true, 1, mAutoState.getDownstation());
            pIndex = starindex;
            this.sinmutalespeech = sinmutalespeech;
        } else if (mode == 0) {// 连续
            if (pAlgorithmClass == null) {
                Toast.makeText(this, "请先开始...", Toast.LENGTH_SHORT).show();
            }
            mhander.removeMessages(0);
            mhander.removeMessages(1);
            mhander.sendEmptyMessageDelayed(0, 10);
        } else if (mode == 1) {// 单步
            if (pAlgorithmClass == null) {
                Toast.makeText(this, "请先开始...", Toast.LENGTH_SHORT).show();
            }
            mhander.removeMessages(0);
            mhander.removeMessages(1);
            mhander.sendEmptyMessageDelayed(1, 10);
        }
        return true;
    }

    public boolean starGpsFragmentAuto() {
        // TODO Auto-generated method stub
        if (mAutoState.getUpstation().size() <= 0
                || mAutoState.getDownstation().size() <= 0) {
            Toast.makeText(getApplicationContext(), "当前线路未加载，请先加载",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        mhander.removeMessages(0);
        mhander.removeMessages(1);


        setGpsStateListener();
        main_btn_mode.setText("GPS");
        title_switch_txtmode.setText("GPS");
        mAutoState.setAutoMode(2);
        pAlgorithmClass = new AutoReportStation(this);
        pAlgorithmClass.setStations(true, 0, mAutoState.getUpstation());
        pAlgorithmClass.setStations(true, 1, mAutoState.getDownstation());
        openTimer();
        return true;
    }

    public void stopGpsFragmentAuto() {
        // TODO Auto-generated method stub
        closeTimer();
        locationManager.removeGpsStatusListener(gpsStatelistener);
        locationManager.removeUpdates(locationListener);
       // mAutoState.setTrackRecord(false);
        mAutoState.setmLocationValid(false);
        mAutoState.setmLocationState("未定位");
        // pAlgorithmClass = null;
    }

    private Timer timer;
    private TimerTask task;

    private void openTimer() {

        // mhander.removeMessages(2);
        // mhander.sendEmptyMessageDelayed(2, 10);

        if (timer == null) {
            timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    if (pAlgorithmClass == null) {
                        return;
                    }

                    // if (MainApplication.logo)
//                    Log.e("Test", "定时器运行");
                    mMap.setCenterLang(MainApplication.latitude,
                            MainApplication.longitude);
                    pAlgorithmClass.setGpsInfo(1, MainApplication.longitude,
                            MainApplication.latitude, 5.0f, 6,
                            MainApplication.time);

					/*
                     * if (pIndex < mAutoState.getCurrentPointlist().size()) {
					 * 
					 * if (MainApplication.logo) Log.e(TGA, "模拟运行");
					 * MainApplication.latitude =
					 * mAutoState.getCurrentPointlist()
					 * .get(pIndex).getLatitude(); MainApplication.longitude =
					 * mAutoState
					 * .getCurrentPointlist().get(pIndex).getLongitude();
					 * MainApplication.time = mAutoState.getCurrentPointlist()
					 * .get(pIndex).getTime();
					 * mMap.setCenterLang(MainApplication.latitude,
					 * MainApplication.longitude); pAlgorithmClass.setGpsInfo(1,
					 * MainApplication.longitude, MainApplication.latitude,
					 * 5.0f, 6, MainApplication.time); pIndex++; }
					 */

                }
            };
            timer.schedule(task, 1000, 1000);
        }
    }

    private void closeTimer() {
        // mhander.removeMessages(2);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void switchUpDownNotify(int upDown) {
        // TODO Auto-generated method stub

    }

    @Override
    public void inOutStation(int mupDownState, int stationType,
                             int minOutState, StationDao mJsonPointStruct,
                             StationDao mJsonPointStruct1) {

        String mTemp = "";
        switch (mupDownState) {
            case 0:
                mTemp += "上行";
                mAutoState.setmRundirection(1);
                break;
            case 1:
                mTemp += "下行";
                mAutoState.setmRundirection(2);
                break;
            case 2:
                mTemp += "环形";
                break;
        }

        mTemp += " " + mJsonPointStruct.getStationName() + " ";

        switch (minOutState) {
            case 0:
                mTemp += "进站";

                tts.startSpeak(mTemp);

                // Message msg = new Message();
                // msg.what = 8;
                // msg.obj = mTemp;
                // mhander.removeMessages(8);
                // mhander.sendMessage(msg);

                break;
            case 1:
                mTemp += "到站";
                break;
            case 2:
                mTemp += "出站";

                tts.startSpeak(mTemp);

                // Message msg2 = new Message();
                // msg2.what = 8;
                // msg2.obj = mTemp;
                // mhander.removeMessages(8);
                // mhander.sendMessage(msg2);

                break;
        }
        Log.i(TGA, "车辆信息：" + mTemp.toString());
        if (mAutoState.getAutoMode() == 1) {// 模拟
        } else if (mAutoState.getAutoMode() == 2) {// GPS

        }

        // main_msg.setText(mTemp);
        mAutoState.setmCurrentStation(mJsonPointStruct);
        Message msg = new Message();
        msg.what = 4;
        msg.obj = mTemp;
        mhander.removeMessages(4);
        mhander.sendMessage(msg);

        // StartThread(text);
    }

    @Override
    public void exceptionInfo(int exceptionState, String message) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mMapView.onDestroy();
        app.exit();
        if (locationManager != null) {
//            locationManager.
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        mhander.sendEmptyMessageDelayed(3, 10);
       // setGpsStateListener();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mMapView.onPause();
//        if (locationManager != null)
//            locationManager.removeUpdates(locationListener);
    }

    // ////////////////////////////GPS位置监听//////////////////////////////////////////////////

    /**
     * 处理GPS
     */
    public void setGpsStateListener() {
        // 判断GPS是否正常启动
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getApplicationContext(), "未开启GPS导航...",
                    Toast.LENGTH_SHORT).show();
            return;
        }
//        // 监听状态
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Log.e(TGA, "没有找到权限");
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationProvider provider =locationManager.getProvider(LocationManager.GPS_PROVIDER);
//        provider.
        // 绑定监听，有4个参数
        // 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
        // 参数2，位置信息更新周期，单位毫秒
        // 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
        // 参数4，监听
        // 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

        // 1秒更新一次，或最小位移变化超过1米更新一次；
        // 注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000, 0, locationListener);

        locationManager.addGpsStatusListener(gpsStatelistener);
    }

    private float accuracy = 0;
    private long mLastLocationMillis = 0;
    /**
     * GPS位置监听
     */
    // 位置监听
    private LocationListener locationListener = new LocationListener() {

        /**
         * 位置信息变化时触发
         */
        public void onLocationChanged(Location location) {
            if (location != null) {
                mLastLocationMillis = SystemClock.elapsedRealtime();
                if (!mAutoState.getmLocationValid()) {
                    mAutoState.setmLocationValid(true);
                    mAutoState.setmLocationState("定位成功");
                    Log.i(TGA, "定位成功");
                }
                MainApplication.latitude = Utils.getScale(6, location.getLatitude());
                MainApplication.longitude = Utils.getScale(6, location.getLongitude());
                MainApplication.speed = (int) location.getSpeed();
                MainApplication.direction = (int) location.getBearing();
                // accuracy = location.getAccuracy();

                if (mAutoState.getTrackRecord()) {// 记录轨迹点
//                    Log.i("Test","记录轨迹点");
                    new TxtTools().writeGpsTrackPoint(MainApplication.latitude,
                            MainApplication.longitude, MainApplication.speed,
                            MainApplication.direction,
                            mAutoState.getmCurrenlineName());

                }

            }
        }

        /**
         * GPS状态变化时触发
         */
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                // GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    Log.i("xhd", "当前GPS状态为可见状态");
                    break;
                // GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    Log.i("xhd", "当前GPS状态为服务区外状态");
                    break;
                // GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.i("xhd", "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        /**
         * GPS开启时触发
         */
        public void onProviderEnabled(String provider) {
        }

        /**
         * GPS禁用时触发
         */
        public void onProviderDisabled(String provider) {
        }

    };

    private GpsStatus.Listener gpsStatelistener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                // 第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    mAutoState.setmLocationState("第一次定位");
                    Log.i("xhd", "第一次定位");
                    break;
                // 卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    if (mAutoState.getmLocationValid()) {
                        Log.i("xhd", "有效定位");

                        mAutoState.setmLocationState("有效定位");
                    } else {
                        Log.i("xhd", "无效定位");
                        mAutoState.setmLocationState("无效定位");
                    }
                    break;
                // 定位启动
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.i("xhd", "定位启动");
                    break;
                // 定位结束
                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.i("xhd", "定位结束");
                    mAutoState.setmLocationState("定位结束");
                    break;
            }
            if (mAutoState.getmLocationValid()
                    && SystemClock.elapsedRealtime() - mLastLocationMillis > 10000) {
                Log.i("xhd", "定位失败");
                mAutoState.setmLocationState("定位失败");
                mAutoState.setmLocationValid(false);
            }
        }

        ;
    };

    // ///////////////////////////////日志管理////////////////////////////////////////////////////////////
    private static int row = 0;
    private static boolean isLog_exported = true;
    static Runnable mLog_Send_Runnable = new Runnable() {

        BufferedReader bufferedReader = null;
        InputStreamReader in = null;
        FileWriter out1 = null;
        File file = null;

        @Override
        public void run() {
            Process logcatProcess = null;
            String[] running = null;
            try {
                /** 获取系统logcat日志信息 */
                // 相当于在命令行运行 logcat -s dalvikm , -s表示过滤，
                // 第三个参数表示过滤的条件。如果没有第三个参数，数组长度2，肯定也是可以的。下面有logcat的使 用方法
                // String[] running=new String[]{ "logcat","-s","dalvikvm" };

                running = new String[]{"logcat", "-v", "time"};
                logcatProcess = Runtime.getRuntime().exec(running);
                in = new InputStreamReader(logcatProcess.getInputStream());
                bufferedReader = new BufferedReader(in);

                // bufferedReader = ShellUtils.execCommand("logcat", true);
                String line;
                String LogFilename = "";

                while (isLog_exported) {
                    if (((line = bufferedReader.readLine()) != null)) {
                        // Log.i("MCU", "日志线程：line="+line);
                        // row++;
                        if (row == 0) {
                            row = 1;
                            LogFilename = JsonFileContants.SDcard + "/mylog/"
                                    + SystemClock.elapsedRealtime() + "Log.txt";

                            file = new File(LogFilename);

                            if (!file.getParentFile().exists()) {
                                file.getParentFile().mkdirs();
                            }

                            file.createNewFile();

                            out1 = new FileWriter(file, true);
                        }
                        // Log_Write_53(line,Log_53Filename);
                        out1.write(line + "\r\n");
                        out1.flush();
                        // out1.close();
                        row++;
                    } else {
                        // 读出的LOG日志异常 ，关闭线程
                        isLog_exported = false;
                        // Log.i("MCU", "日志线程异常：line="+line);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 关闭流。
                stop_Read();
            }
        }

        void stop_Read() {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out1 != null) {
                try {
                    out1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 向53指定USB路径下开始写入Log..
         *
         * @param logs
         */

        @SuppressWarnings("unused")
        void Log_Write(String logs, String fileName) {
            File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            // TODO 这个FileWriter 应该作为Runnable的成员变量声明，并将构造和close动作放在run方法中执行。
            FileWriter out1 = null;
            try {
                file.createNewFile();
                out1 = new FileWriter(file, true);
                out1.write(logs + "\r\n");
                out1.flush();
                out1.close();
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }

    };

}
