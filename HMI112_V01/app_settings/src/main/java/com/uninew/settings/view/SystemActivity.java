package com.uninew.settings.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.uninew.settings.R;


public class SystemActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {


    private RadioGroup rg_buttons;
    private RadioButton more_radiobutton_voice, more_radiobutton_light, more_radiobutton_wifi,
            more_radiobutton_host, more_radiobutton_time,more_radiobutton_reset;
    private Fragment fragment;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);
        fm = getSupportFragmentManager();
       // initView();
    }

    private void initView() {
        more_radiobutton_host = (RadioButton) findViewById(R.id.more_radiobutton_host);
        more_radiobutton_light = (RadioButton) findViewById(R.id.more_radiobutton_light);
        more_radiobutton_voice = (RadioButton) findViewById(R.id.more_radiobutton_voice);
        more_radiobutton_wifi = (RadioButton) findViewById(R.id.more_radiobutton_wifi);
        more_radiobutton_time = (RadioButton) findViewById(R.id.more_radiobutton_time);
        more_radiobutton_reset = (RadioButton) findViewById(R.id.more_radiobutton_reset);
        rg_buttons = (RadioGroup) findViewById(R.id.more_system_radiogroup);
        rg_buttons.setOnCheckedChangeListener(this);
        if (fragment == null){
            fragment = new VoiceFragment();
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
                case R.id.more_radiobutton_host:
                    if (fragment == null){
                        fragment = new HostFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout,fragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.more_radiobutton_light:
                    if (fragment == null){
                        fragment = new LightFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout,fragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.more_radiobutton_reset:
                    if (fragment == null){
                        fragment = new ResetFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout,fragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.more_radiobutton_time:
                    if (fragment == null){
                        fragment = new TimeFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout,fragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.more_radiobutton_voice:
                    if (fragment == null){
                        fragment = new VoiceFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout,fragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.more_radiobutton_wifi:
                    if (fragment == null){
                        fragment = new WifiFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout,fragment);
                    fragmentTransaction.commit();
                    break;
            }

        }

    }
}
