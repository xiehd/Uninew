package com.uninew.settings.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.uninew.settings.R;
import com.uninew.settings.interfaces.IHostPresenter;
import com.uninew.settings.interfaces.IHostView;
import com.uninew.settings.presenter.HostPresentet;


/**
 * Created by Administrator on 2017/8/30.
 */


public class HostFragment extends Fragment implements IHostView,View.OnClickListener {
    private View view;
    private IHostPresenter mIHostPresenter;
    private Switch more_host_switch;
    private TextView more_host_name,more_host_pws,more_host_number;
    private String hostName,hostPassWold;
    private int mConnectNumber = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_host, container, false);
        initView();
        mIHostPresenter = new HostPresentet(this);
        SetListener();
        return view;
    }

    private void initView() {
        more_host_number = (TextView) view.findViewById(R.id.more_host_number);
        more_host_name = (TextView) view.findViewById(R.id.more_host_name);
        more_host_pws = (TextView) view.findViewById(R.id.more_host_pws);
        more_host_switch = (Switch) view.findViewById(R.id.more_host_switch);


    }

    private void SetListener(){
        more_host_number.setOnClickListener(this);
        more_host_name.setOnClickListener(this);
        more_host_pws.setOnClickListener(this);

        more_host_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIHostPresenter.SetHostOFF(isChecked,hostName,hostPassWold);
            }
        });
    }

    @Override
    public void ShowHostOFF(boolean state, String name, String passworld) {
        hostName = name;
        hostPassWold = passworld;
        more_host_switch.setChecked(state);
    }

    @Override
    public void ShowHostConnectNuber(int number) {
            mConnectNumber = number;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more_host_name:
                break;
            case R.id.more_host_pws:
                break;

        }
    }
}
