package com.uninew.car.phone;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.uninew.car.R;
import com.uninew.car.adapter.MyFragmentPagerAdapter;
import com.uninew.car.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/1 0001.
 */

public class PhoneActivity extends FragmentActivity implements View.OnClickListener {

    private LinearLayout ll_keyboard;
    private LinearLayout ll_contact;
    private LinearLayout ll_recording;
    private DialerFragment mDialerFragment;
    private ContactsFragment mContactsFragment;
    private RecordFragment mRecordFragment;
    private int select;
    //    private FragmentTransaction mTransaction;
    private NoScrollViewPager vp_show;
    private MyFragmentPagerAdapter mPagerAdapter;
    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(R.layout.activity_phone);
        init();
    }

    private void init() {
        initView();
        initFragment();
        initClickListener();
    }

    private void initClickListener() {
        ll_contact.setOnClickListener(this);
        ll_keyboard.setOnClickListener(this);
        ll_recording.setOnClickListener(this);
    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        FragmentManager fm = getSupportFragmentManager();
        mDialerFragment = DialerFragment.getInstance();
        mFragments.add(mDialerFragment);
        mContactsFragment = ContactsFragment.getInstance();
        mFragments.add(mContactsFragment);
        mRecordFragment = RecordFragment.getInstance();
        mFragments.add(mRecordFragment);
        mPagerAdapter = new MyFragmentPagerAdapter(fm, mFragments);
        select = ll_keyboard.getId();
        vp_show.setAdapter(mPagerAdapter);
        vp_show.setCurrentItem(0);
    }

    private void initView() {
        ll_contact = (LinearLayout) findViewById(R.id.ll_phone_contact);
        ll_keyboard = (LinearLayout) findViewById(R.id.ll_phone_keyboard);
        ll_recording = (LinearLayout) findViewById(R.id.ll_phone_recording);
        vp_show = (NoScrollViewPager) findViewById(R.id.vp_show_view);
        vp_show.setScroll(false);
        ll_recording.setBackgroundResource(R.mipmap.button_s);
        ll_keyboard.setBackgroundResource(R.mipmap.button_p);
        ll_contact.setBackgroundResource(R.mipmap.button_s);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (select == id) {
            return;
        }
        switch (id) {
            case R.id.ll_phone_contact:
                if (mFragments.size() > 1)
                    vp_show.setCurrentItem(1);
//                mTransaction.commitNow();
                ll_recording.setBackgroundResource(R.mipmap.button_s);
                ll_keyboard.setBackgroundResource(R.mipmap.button_s);
                ll_contact.setBackgroundResource(R.mipmap.button_p);
                select = id;
                break;
            case R.id.ll_phone_keyboard:
                if (mFragments.size() > 0)
                    vp_show.setCurrentItem(0);
//                mTransaction.commitNow();
                ll_recording.setBackgroundResource(R.mipmap.button_s);
                ll_keyboard.setBackgroundResource(R.mipmap.button_p);
                ll_contact.setBackgroundResource(R.mipmap.button_s);
                select = id;
                break;
            case R.id.ll_phone_recording:
                if (mFragments.size() > 2)
                    vp_show.setCurrentItem(2);
//                mTransaction.commitNow();
                ll_recording.setBackgroundResource(R.mipmap.button_p);
                ll_keyboard.setBackgroundResource(R.mipmap.button_s);
                ll_contact.setBackgroundResource(R.mipmap.button_s);
                select = id;
                break;
        }
    }

}
