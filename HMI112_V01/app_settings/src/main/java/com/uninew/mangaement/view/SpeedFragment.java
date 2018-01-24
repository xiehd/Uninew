package com.uninew.mangaement.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.uninew.car.db.settings.BaseLocalDataSource;
import com.uninew.car.db.settings.BaseLocalSource;
import com.uninew.car.db.settings.SettingsKeyValue;
import com.uninew.settings.R;


/**
 * Created by Administrator on 2017/8/30.
 */


public class SpeedFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "SpeedFragment";
    private static final boolean D = true;
    private View view;
    private Spinner spinner_manggae_speed_km, spinner_mangae_speed_source;

    private EditText mangae_speed_alarmNumber, mangae_speed_time, mangae_speed_pre_alarmNumber, mangae_speed_pre_time;
    private Button speed_btn_defult, speed_btn_save;
    private BaseLocalDataSource db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mangae_speed, container, false);
        db = BaseLocalDataSource.getInstance(this.getActivity());
        initView();
        SetListener();
        init();
        return view;
    }

    private void initView() {
        spinner_manggae_speed_km = (Spinner) view.findViewById(R.id.spinner_manggae_speed_km);
        spinner_mangae_speed_source = (Spinner) view.findViewById(R.id.spinner_mangae_speed_source);

        mangae_speed_alarmNumber = (EditText) view.findViewById(R.id.mangae_speed_alarmNumber);
        mangae_speed_time = (EditText) view.findViewById(R.id.mangae_speed_time);
        mangae_speed_pre_alarmNumber = (EditText) view.findViewById(R.id.mangae_speed_pre_alarmNumber);
        mangae_speed_pre_time = (EditText) view.findViewById(R.id.mangae_speed_pre_time);

        speed_btn_defult = (Button) view.findViewById(R.id.speed_btn_defult);
        speed_btn_save = (Button) view.findViewById(R.id.speed_btn_save);
    }

    private void SetListener() {

        ArrayAdapter<String> speed_km = new ArrayAdapter<String>(getContext(), R.layout.spinner_item,
                this.getActivity().getResources().getStringArray(R.array.speed_km_spinner));
        spinner_manggae_speed_km.setAdapter(speed_km);
        ArrayAdapter<String> speed_source = new ArrayAdapter<String>(getContext(), R.layout.spinner_item,
                this.getActivity().getResources().getStringArray(R.array.speed_source_spinner));
        spinner_mangae_speed_source.setAdapter(speed_source);

        speed_btn_defult.setOnClickListener(this);
        speed_btn_save.setOnClickListener(this);
    }

    private String alarmSpeed = "0";
    private String alarmSpeedTime = "0";
    private String alarm_speed_max = "0";
    private String Pre_alarm_speed_time = "0";
    private int speed_source = 0;
    private int speed_unit = 0;

    private void init() {
        db.getAlarm_speed_max(new BaseLocalSource.GetSpeedSettingCallBack() {
            @Override
            public void onDBBaseDataLoaded(String s) {
                alarmSpeed = s;
            }

            @Override
            public void onDataNotAailable() {

            }
        });
        db.getAlarm_speed_time(new BaseLocalSource.GetSpeedSettingCallBack() {
            @Override
            public void onDBBaseDataLoaded(String s) {
                alarmSpeedTime = s;
            }

            @Override
            public void onDataNotAailable() {

            }
        });
        db.getPre_alarm_speed_max(new BaseLocalSource.GetSpeedSettingCallBack() {
            @Override
            public void onDBBaseDataLoaded(String s) {
                alarm_speed_max = s;
            }

            @Override
            public void onDataNotAailable() {

            }
        });
        db.getPre_alarm_speed_time(new BaseLocalSource.GetSpeedSettingCallBack() {
            @Override
            public void onDBBaseDataLoaded(String s) {
                Pre_alarm_speed_time = s;
            }

            @Override
            public void onDataNotAailable() {

            }
        });
        db.getSpeed_source(new BaseLocalSource.GetSpeedSettingCallBack() {
            @Override
            public void onDBBaseDataLoaded(String s) {
                speed_source = Integer.parseInt(s);
            }

            @Override
            public void onDataNotAailable() {

            }
        });
        db.getSpeed_unit(new BaseLocalSource.GetSpeedSettingCallBack() {
            @Override
            public void onDBBaseDataLoaded(String s) {
                speed_unit = Integer.parseInt(s);
            }

            @Override
            public void onDataNotAailable() {

            }
        });
        mangae_speed_alarmNumber.setText(alarmSpeed + "");
        mangae_speed_time.setText(alarmSpeedTime + "");
        mangae_speed_pre_alarmNumber.setText(alarm_speed_max + "");
        mangae_speed_pre_time.setText(Pre_alarm_speed_time + "");

        if (speed_source == SettingsKeyValue.SpeedSourceKeyValue.SPEED_SOURCE_SATELLITE) {//卫星
            spinner_mangae_speed_source.setSelection(0);
        } else if (speed_source == SettingsKeyValue.SpeedSourceKeyValue.SPEED_SOURCE_PULSE) {//脉冲
            spinner_mangae_speed_source.setSelection(1);
        } else if (speed_source == SettingsKeyValue.SpeedSourceKeyValue.SPEED_SOURCE_CAN) {
            spinner_mangae_speed_source.setSelection(2);
        }

        if (speed_unit == SettingsKeyValue.SpeedUnitKeyValue.SPEED_UNIT_KM) {/* km/h 千米/小时*/
            spinner_manggae_speed_km.setSelection(0);
        } else if (speed_unit == SettingsKeyValue.SpeedUnitKeyValue.SPEED_UNIT_MILE) {/* 英里/小时*/
            spinner_manggae_speed_km.setSelection(1);
        }
//            }
//
//            @Override
//            public void onDataNotAailable() {
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.speed_btn_defult:
                db.restoringDefault();
                init();
                break;
            case R.id.speed_btn_save:
                String alarm_speed_max = mangae_speed_alarmNumber.getText().toString();
                String alarm_speed_time = mangae_speed_time.getText().toString();
                String pre_alarm_speed_max = mangae_speed_pre_alarmNumber.getText().toString();
                String pre_alarm_speed_time = mangae_speed_pre_time.getText().toString();
                if (!TextUtils.isEmpty(alarm_speed_max)) {
                    db.setAlarm_speed_max(Integer.valueOf(alarm_speed_max).intValue());
                }
                if (!TextUtils.isEmpty(alarm_speed_time)) {
                    db.setAlarm_speed_time(Integer.parseInt(alarm_speed_time));
                }
                if (!TextUtils.isEmpty(pre_alarm_speed_max)) {
                    db.setPre_alarm_speed_max(Integer.parseInt(pre_alarm_speed_max));
                }
                if (!TextUtils.isEmpty(pre_alarm_speed_time)) {
                    db.setPre_alarm_speed_time(Integer.parseInt(pre_alarm_speed_time));
                }
                db.setSpeed_source(spinner_mangae_speed_source.getSelectedItemPosition());
                db.setSpeed_unit(spinner_manggae_speed_km.getSelectedItemPosition());
                break;
        }
    }
}
