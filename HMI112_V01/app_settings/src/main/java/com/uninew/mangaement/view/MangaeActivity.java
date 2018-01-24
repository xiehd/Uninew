package com.uninew.mangaement.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;
import android.widget.RadioGroup;

import com.uninew.settings.R;

public class MangaeActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup rg_buttons;
    private Fragment fragment;
    private FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mangae);
        fm = getSupportFragmentManager();
        Log.i("xhd","cccccccccccc");
        initView();
    }
    private void initView(){
        rg_buttons = (RadioGroup) findViewById(R.id.more_mangae_radiogroup);
        rg_buttons.setOnCheckedChangeListener(this);
        if (fragment == null){
            fragment = new BaseSettingFragment();
        }
        FragmentTransaction ft  = fm.beginTransaction();
        ft.replace(R.id.im_framelayout,fragment);
        ft.commit();
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fragmentTransaction  = fm.beginTransaction();
        try{
            if (fragment != null){
                fragmentTransaction.remove(fragment);
                fragment = null;
            }
        }finally {
            switch (checkedId){
                case R.id.more_radiobutton_base:
                    if (fragment == null){
                        fragment = new BaseSettingFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout,fragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.more_radiobutton_parameter:
                    if (fragment == null){
                        fragment = new ParameterFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout,fragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.more_radiobutton_dial:
//                    if (fragment == null){
//                        fragment = new DialFragment();
//                    }
//                    fragmentTransaction.replace(R.id.im_framelayout,fragment);
//                    fragmentTransaction.commit();
                    break;
                case R.id.more_radiobutton_serialPort:
                    if (fragment == null){
                        fragment = new SerialFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout,fragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.more_radiobutton_sensor:
                    if (fragment == null){
                        fragment = new SensorFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout,fragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.more_radiobutton_power:
                    if (fragment == null){
                        fragment = new PowerFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout,fragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.more_radiobutton_speed:
                    if (fragment == null){
                        fragment = new SpeedFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout,fragment);
                    fragmentTransaction.commit();
                    break;
            }

        }

    }
}
