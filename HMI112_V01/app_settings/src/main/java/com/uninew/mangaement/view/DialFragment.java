package com.uninew.mangaement.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uninew.settings.R;


/**
 * Created by Administrator on 2017/8/30.
 */


public class DialFragment extends Fragment{
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mangae_dial, container, false);
        initView();
        initListener();
        return view;
    }

    private void initView() {

    }

    private void initListener(){

    }

}
