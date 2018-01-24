package com.uninew.settings.view;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Switch;

import com.uninew.adapter.WifiAdapter;
import com.uninew.settings.R;
import com.uninew.settings.interfaces.IWifiPresenter;
import com.uninew.settings.interfaces.IWifiView;
import com.uninew.settings.presenter.WifiPresenter;

import java.util.List;

/**
 * Created by Administrator on 2017/8/30.
 */


public class WifiFragment extends Fragment implements IWifiView {
    private View view;
    private IWifiPresenter mIWifiPresenter;
    private WifiAdapter mWifiAdapter;

    private Switch more_wifi_swith;
    private ListView more_wifi_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wifi, container, false);
        initView();
        mIWifiPresenter = new WifiPresenter(this, this.getActivity());
        initListener();
        return view;
    }

    private void initView() {
    }

    private void initListener() {
    }

    @Override
    public void ShowWifiList(List<ScanResult> mWifiList) {

    }
}
