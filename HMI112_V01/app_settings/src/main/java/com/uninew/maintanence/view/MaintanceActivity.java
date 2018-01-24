package com.uninew.maintanence.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.uninew.settings.R;

public class MaintanceActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup rg_buttons;
    private Fragment fragment;
    private FragmentManager fm;
    private Animation animation_in;
    private Animation animation_out;
    private FrameLayout im_framelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_maintance);
        //设备状态查询
//        Intent intent = new Intent("Action_Device_StateDate");
//        this.sendBroadcast(intent);

        fm = getSupportFragmentManager();
        initView();

    }

    private void initView() {

        im_framelayout = (FrameLayout) findViewById(R.id.im_framelayout);
        rg_buttons = (RadioGroup) findViewById(R.id.more_maintance_radiogroup);
        rg_buttons.setOnCheckedChangeListener(this);
        if (fragment == null) {
            fragment = new InspectFragment();
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.im_framelayout, fragment);
        ft.commit();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        try {
            if (fragment != null) {
//                animation_out = AnimationUtils.loadAnimation(this,R.anim.out_to_left);
//                im_framelayout.startAnimation(animation_out);
                fragmentTransaction.remove(fragment);
                fragment = null;
            }


        } finally {
            switch (checkedId) {
                case R.id.more_radiobutton_deviceInspect:
                    if (fragment == null) {
                        fragment = new InspectFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout, fragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.more_radiobutton_version:
                    if (fragment == null) {
                        fragment = new VersionFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout, fragment);
                    fragmentTransaction.commit();

                    break;
                case R.id.more_radiobutton_data:
                    if (fragment == null) {
                        fragment = new DataFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout, fragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.more_radiobutton_gps:
                    if (fragment == null) {
                        fragment = new GPSFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout, fragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.more_radiobutton_net:
                    if (fragment == null) {
                        fragment = new NetFragment();
                    }
                    fragmentTransaction.replace(R.id.im_framelayout, fragment);
                    fragmentTransaction.commit();
                    break;
            }
//            animation_in = AnimationUtils.loadAnimation(this,R.anim.in_from_right);
//            im_framelayout.startAnimation(animation_in);
        }

    }

}
