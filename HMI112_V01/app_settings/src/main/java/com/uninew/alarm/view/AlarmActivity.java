package com.uninew.alarm.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.uninew.adapter.AlarmAdapter;
import com.uninew.car.db.alarm.AlarmLocalDataSource;
import com.uninew.car.db.alarm.AlarmLocalSource;
import com.uninew.car.db.alarm.AlarmMessage;
import com.uninew.settings.R;

import java.util.ArrayList;
import java.util.List;

public class AlarmActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup rg_buttons;
    private RadioButton more_radiobutton_speed, more_radiobutton_device, more_radiobutton_other;
    private Fragment fragment;
    private FragmentManager fm;
    private Context mContext;
    private AlarmAdapter mAlarmAdapter;
    private List<AlarmMessage> allAlarms = new ArrayList<>();
    private AlarmLocalSource db;
    private int type = 0;//适配器数据源类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_alarm);
        fm = getSupportFragmentManager();
        db = AlarmLocalDataSource.getInstance(this);
        initView();
        mContext = this;
        init();

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private void init(){
        db.getAllDBDatas(new AlarmLocalSource.LoadAlarmsCallBack() {
            @Override
            public void onDBBaseDataLoaded(List<AlarmMessage> buffers) {
               allAlarms = buffers;
            }
            @Override
            public void onDataNotAailable() {

            }
        });
    }
    public AlarmAdapter getAdapter(int type) {
        if (mAlarmAdapter == null) {//初次加载所有数据
            mAlarmAdapter = new AlarmAdapter(mContext, type);
            mAlarmAdapter.setAlarmDate(allAlarms);
        }else{
            mAlarmAdapter.setType(type);
        }
        return mAlarmAdapter;
    }

    private void initView() {
        more_radiobutton_speed = (RadioButton) findViewById(R.id.more_radiobutton_speed);
        more_radiobutton_device = (RadioButton) findViewById(R.id.more_radiobutton_device);
        more_radiobutton_other = (RadioButton) findViewById(R.id.more_radiobutton_other);
        rg_buttons = (RadioGroup) findViewById(R.id.more_alarm_radiogroup);
        rg_buttons.setOnCheckedChangeListener(this);
        if (fragment == null) {
            fragment = new SpeedFragment();
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.im_framelayout, fragment);
        ft.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        try {
            if (fragment != null) {
                fragmentTransaction.remove(fragment);
                fragment = null;
            }


        } finally {
            switch (checkedId) {
                case R.id.more_radiobutton_speed:
                    type = 0;
                    if (fragment == null) {
                        fragment = new SpeedFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout, fragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.more_radiobutton_device:
                    type = 1;
                    if (fragment == null) {
                        fragment = new DeviceFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout, fragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.more_radiobutton_other://其他
                    type = 2;
                    if (fragment == null) {
                        fragment = new DeviceFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout, fragment);
                    fragmentTransaction.commit();
                    break;
            }

        }

    }
}
