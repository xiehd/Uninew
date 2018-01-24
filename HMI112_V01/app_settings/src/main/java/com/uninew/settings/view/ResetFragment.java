package com.uninew.settings.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uninew.settings.R;
import com.uninew.settings.interfaces.IResetPresenter;
import com.uninew.settings.presenter.ResetPresenter;


/**
 * Created by Administrator on 2017/8/30.
 */


public class ResetFragment extends Fragment {
    private View view;
    private IResetPresenter mIResetPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reset, container, false);
        initView();
        mIResetPresenter = new ResetPresenter();
        initListener();
        return view;
    }

    private void initView() {

    }

    private void initListener() {
    }
}
