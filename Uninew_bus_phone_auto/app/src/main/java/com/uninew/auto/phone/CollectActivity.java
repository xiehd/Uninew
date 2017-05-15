package com.uninew.auto.phone;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;
import com.uninew.adpater.AbstractSpinerAdapter;
import com.uninew.adpater.AbstractSpinerAdapter.IOnItemSelectListener;
import com.uninew.adpater.SpinerPopWindow;
import com.uninew.file.dao.MarkerDao;
import com.uninew.file.dao.StationDao;
import com.uninew.json.AutoState;
import com.uninew.json.JsonUpdate;
import com.uninew.utils.Map_Utils;
import com.uninew.utils.SPTools;

import java.util.ArrayList;

public class CollectActivity extends Activity implements OnClickListener,
        IOnItemSelectListener, OnCheckedChangeListener {

    private static boolean IsStation = true;// 站点，标记点；
    private RadioGroup collect_radiogroup;
    private Button collect_btn_startsave, collect_btn_startclear;
    private TextView tv_station_name_collect, tv_collect_imgleft,
            tv_collect_imgright, collect_txt_state, collect_txt_long,
            collect_txt_speech, collect_txt_lat, collect_txt_direction,
            top_title_txt;
    private ImageView img_back;
    private SPTools sp;
    private static AutoState mAutoState;
    private TextView title_switch;

    Handler mcollectHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            // TODO Auto-generated method stub
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:

                    latitude = MainApplication.latitude;
                    longitude = MainApplication.longitude;
                    time = MainApplication.time;
                    speed = MainApplication.speed;
                    direction = MainApplication.direction;

                    collect_txt_state.setText(mAutoState.getmLocationState());
                    collect_txt_long.setText(longitude + "");
                    collect_txt_speech.setText(speed + "");
                    collect_txt_lat.setText(latitude + "");
                    collect_txt_direction.setText(direction + "");
                    mcollectHandler.sendEmptyMessageDelayed(0, 1000);
                    break;

                default:
                    break;
            }
        }
    };
    private com.baidu.mapapi.map.MapView mMapView;
    private Map_Utils mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_collect);
        mAutoState = MainApplication.getInstance().getmAutoState();
        mSpinerPopWindow = new SpinerPopWindow(getApplicationContext());
        mAdapter = new AbstractSpinerAdapter(this);
        mSpinerPopWindow.setItemListener(this);
        sp = new SPTools(this);
        mMapView = (MapView) findViewById(R.id.bmapview);
        mMap = new Map_Utils(this, mMapView.getMap(), mAutoState);

        initView();
        init();
        setPos(index);

    }

    private static int index = 0;

    private void init() {
        // TODO Auto-generated method stub
        if (mAutoState.getmRundirection() == 1) {
            for (StationDao stationname : mAutoState.getUpstation()) {
                windowDatas.add(stationname.getStationName());
            }

        } else {
            for (StationDao linename : mAutoState.getDownstation()) {
                windowDatas.add(linename.getStationName());
            }

        }
        for (int i = 0; i < windowDatas.size(); i++) {
            if (mAutoState.getmCurrentStation() != null) {
                if (windowDatas.get(i).equals(mAutoState.getmCurrentStation().getStationName())) {
                    index = i;
                }
            } else {

            }
        }
    }

    private void initView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        tv_collect_imgright = (TextView) findViewById(R.id.tv_collect_imgright);
        tv_collect_imgright.setOnClickListener(this);
        tv_collect_imgleft = (TextView) findViewById(R.id.tv_collect_imgleft);
        tv_collect_imgleft.setOnClickListener(this);

        top_title_txt = (TextView) findViewById(R.id.top_title_txt);
        top_title_txt.setText("采集");
        collect_radiogroup = (RadioGroup) findViewById(R.id.collect_radiogroup);
        collect_radiogroup.setOnCheckedChangeListener(this);

        collect_btn_startsave = (Button) findViewById(R.id.collect_btn_startsave);
        collect_btn_startsave.setOnClickListener(this);
        collect_btn_startclear = (Button) findViewById(R.id.collect_btn_startclear);
        collect_btn_startclear.setOnClickListener(this);

        tv_station_name_collect = (TextView) findViewById(R.id.tv_station_name_collect);
        tv_station_name_collect.setOnClickListener(this);

        collect_txt_state = (TextView) findViewById(R.id.collect_txt_state);
        collect_txt_long = (TextView) findViewById(R.id.collect_txt_long);
        collect_txt_speech = (TextView) findViewById(R.id.collect_txt_speech);
        collect_txt_lat = (TextView) findViewById(R.id.collect_txt_lat);
        collect_txt_direction = (TextView) findViewById(R.id.collect_txt_direction);

        title_switch = (TextView) findViewById(R.id.title_switch_txtmode);
        title_switch.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // TODO Auto-generated method stub
        switch (checkedId) {
            case R.id.collect_biaoji_radiobtn:
                IsStation = false;
                windowDatas.clear();
                if (mAutoState.getDownMarkerDaolist() != null && mAutoState.getUpMarkerDaolist() != null) {
                    if (mAutoState.getmRundirection() == 1) {
                        for (MarkerDao markerDao : mAutoState.getUpMarkerDaolist()) {
                            windowDatas.add(markerDao.getName());
                        }
                    } else {
                        for (MarkerDao markerDao : mAutoState.getDownMarkerDaolist()) {
                            windowDatas.add(markerDao.getName());
                        }
                    }
                }
                index = 0;
                setPos(index);
                break;
            case R.id.collect_station_radiobtn:
                IsStation = true;
                windowDatas.clear();
                if (mAutoState.getUpstation() != null && mAutoState.getDownstation() != null) {
                    init();
                }
                setPos(index);
                break;

            default:
                break;
        }
    }

    private SpinerPopWindow mSpinerPopWindow;
    private AbstractSpinerAdapter mAdapter;
    private ArrayList<String> windowDatas = new ArrayList<>();

    private void showSpinWindowstate() {
        // windowDatas.clear();
        Log.e("xhd", "showSpinWindowstate");
        if (mSpinerPopWindow != null) {
            mAdapter.refreshData(windowDatas, 0);
            mSpinerPopWindow.setAdatper(mAdapter);
            mSpinerPopWindow.setWidth(tv_station_name_collect.getWidth());
            mSpinerPopWindow.showAsDropDown(tv_station_name_collect);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_station_name_collect:
                showSpinWindowstate();
                break;
            case R.id.tv_collect_imgleft:
                if (index <= 0) {
                    return;
                }
                index--;
                setPos(index);
                break;
            case R.id.tv_collect_imgright:
                if (index >= windowDatas.size() - 1) {
                    return;
                }
                index++;
                setPos(index);
                break;
            case R.id.img_back:
                finish();
                break;

            case R.id.collect_btn_startsave:// 采集
                if (mAutoState.getUpstation() == null || mAutoState.getUpstation().size() <= 0 || mAutoState.getDownstation() == null || mAutoState.getDownstation().size() <= 0) {
                    Toast.makeText(this, "采集失败,线路不存在", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mAutoState.getmCurrenlineName() != null && !mAutoState.getmCurrenlineName().equals("")) {
                    Toast.makeText(this, "采集成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "采集失败,重新采集", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ////////////////////////////
                if (mMap != null) {
                    // mMap.initMap();
                    mMap.setLocate(latitude, longitude);
                    mMap.setCenterLang(latitude, longitude);
                }
                if (IsStation) {// 站点
                    if (mAutoState.getmRundirection() == 1) {// 上行
                        mAutoState.getUpstation().get(index).setLatitude(latitude);
                        mAutoState.getUpstation().get(index).setLongitude(longitude);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                JsonUpdate.updateStationFile(
                                        mAutoState.getUpstation(),
                                        mAutoState.getmCurrenlineName() + "上行");
                            }
                        }).start();
                    } else {// 下行

                        mAutoState.getDownstation().get(index).setLatitude(latitude);
                        mAutoState.getDownstation().get(index).setLongitude(longitude);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                JsonUpdate.updateStationFile(
                                        mAutoState.getDownstation(),
                                        mAutoState.getmCurrenlineName() + "下行");
                            }
                        }).start();
                    }
                } else {// 标记点
                    if (mAutoState.getUpMarkerDaolist() == null || mAutoState.getUpMarkerDaolist().size() <= 0 || mAutoState.getDownMarkerDaolist() == null || mAutoState.getDownMarkerDaolist().size() <= 0) {
                        Toast.makeText(this, "采集失败,标记点不存在", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (mAutoState.getmRundirection() == 1) {//上行拐点
                        mAutoState.getUpMarkerDaolist().get(index).setLatitude(latitude);
                        mAutoState.getUpMarkerDaolist().get(index).setLongitude(longitude);
                        if (mAutoState.getUpMarkerDaolist().size() >= 0) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    JsonUpdate.updateCornersFile(
                                            mAutoState.getUpMarkerDaolist(),
                                            mAutoState.getmCurrenlineName() + "上行");
                                }
                            }).start();

                        }
                    } else {//下行拐点
                        mAutoState.getDownMarkerDaolist().get(index).setLatitude(latitude);
                        mAutoState.getDownMarkerDaolist().get(index).setLongitude(longitude);
                        if (mAutoState.getDownMarkerDaolist().size() >= 0) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    JsonUpdate.updateCornersFile(
                                            mAutoState.getDownMarkerDaolist(),
                                            mAutoState.getmCurrenlineName() + "下行");
                                }
                            }).start();
                        }
                    }
                }

                break;

            case R.id.collect_btn_startclear:
                // collect_txt_state.setText(mAutoState.getmLocationState());
                collect_txt_long.setText("");
                collect_txt_speech.setText("");
                collect_txt_lat.setText("");
                collect_txt_direction.setText("");
                break;
            default:
                break;
        }

    }

    @Override
    public void onItemClick(int pos) {
        // TODO Auto-generated method stub
        index = pos;
        setPos(index);
    }

    private void setPos(int pos) {
        // TODO Auto-generated method stub
        if (windowDatas.size() > 0) {
            tv_station_name_collect.setText(windowDatas.get(pos));
        } else {
            tv_station_name_collect.setText("");
        }
    }

    private static double latitude = 22.6485;
    private static double longitude = 113.52648;
    private static long time = 00000;
    private static int speed = 0;
    private static int direction = 0;

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mMapView.onResume();

        // latitude = MainApplication.latitude;
        // longitude = MainApplication.longitude;
        // time = MainApplication.time;
        // speed = MainApplication.speed;
        // direction = MainApplication.direction;

        mcollectHandler.sendEmptyMessageDelayed(0, 10);
        // if (mAutoState.getTrackRecord()) {
        // mAutoState.setTrackRecord(true);
        // collect_btn_startsave.setText("停止");
        // collect_btn_startsave.setBackground(getResources().getDrawable(
        // R.drawable.btn_down));
        // } else {
        // mAutoState.setTrackRecord(false);
        // collect_btn_startsave.setText("开始");
        // collect_btn_startsave.setBackground(getResources().getDrawable(
        // R.drawable.btn_up));
        // }

        if (mMap != null) {
            mMap.initMap();
            mMap.setLocate(latitude, longitude);
            mMap.setCenterLang(latitude, longitude);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mMapView.onPause();
    }

}
