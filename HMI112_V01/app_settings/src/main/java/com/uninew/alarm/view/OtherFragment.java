package com.uninew.alarm.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uninew.settings.R;

/**
 * Created by Administrator on 2017/9/16.
 */

public class OtherFragment extends Fragment {

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_other, container, false);
        initView();
        SetListener();
        return view;
    }

    private void initView(){}
    private void SetListener() {
    }
}
