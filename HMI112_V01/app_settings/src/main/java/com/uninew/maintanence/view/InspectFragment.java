package com.uninew.maintanence.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uninew.export.Utils;
import com.uninew.main.SettingsApplication;
import com.uninew.maintanence.interfaces.IDeviceInsperctView;
import com.uninew.maintanence.presenter.DevicePresenter;
import com.uninew.net.JT905.common.DeviceState;
import com.uninew.settings.R;


/**
 * Created by Administrator on 2017/8/30.
 */


public class InspectFragment extends Fragment implements IDeviceInsperctView{
    private View view;
    private TextView maintance_inpecte_dvr, maintance_inpecte_taximeter, maintance_inpecte_carscreen, maintance_inpecte_gps,
            maintance_inpecte_net, maintance_inpecte_power, maintance_inpecte_rentserver, maintance_inpecte_vedioserver;
    private ImageView image_dvr, image_taximeter, image_carscreen, image_gsp, image_net, image_power, image_rentserver, image_vedioserver;
    private DevicePresenter mDevicePresenter;
    private SettingsApplication app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_device_inspect, container, false);
        initView();
        SetListener();
        mDevicePresenter = new DevicePresenter(this,this.getActivity());
        app = (SettingsApplication) Utils.getApp();
        return view;
    }

    private void initView() {

        maintance_inpecte_dvr = (TextView) view.findViewById(R.id.maintance_inpecte_dvr);
        maintance_inpecte_taximeter = (TextView) view.findViewById(R.id.maintance_inpecte_taximeter);
        maintance_inpecte_carscreen = (TextView) view.findViewById(R.id.maintance_inpecte_carscreen);
        maintance_inpecte_gps = (TextView) view.findViewById(R.id.maintance_inpecte_gps);
        maintance_inpecte_net = (TextView) view.findViewById(R.id.maintance_inpecte_net);
        maintance_inpecte_power = (TextView) view.findViewById(R.id.maintance_inpecte_power);
        maintance_inpecte_rentserver = (TextView) view.findViewById(R.id.maintance_inpecte_rentserver);
        maintance_inpecte_vedioserver = (TextView) view.findViewById(R.id.maintance_inpecte_vedioserver);

        image_dvr = (ImageView) view.findViewById(R.id.image_dvr);
        image_taximeter = (ImageView) view.findViewById(R.id.image_taximeter);
        image_carscreen = (ImageView) view.findViewById(R.id.image_carscreen);
        image_gsp = (ImageView) view.findViewById(R.id.image_gsp);
        image_net = (ImageView) view.findViewById(R.id.image_net);
        image_power = (ImageView) view.findViewById(R.id.image_power);
        image_rentserver = (ImageView) view.findViewById(R.id.image_rentserver);
        image_vedioserver = (ImageView) view.findViewById(R.id.image_vedioserver);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (app != null){
            app.unDeviceState();
        }
    }

    private void SetListener() {
    }

    @Override
    public void ShowDvrState(int state) {
        if (state == 0x01) {
            maintance_inpecte_dvr.setText(this.getActivity().getResources().getString(R.string.mangae_connect));
            image_dvr.setVisibility(View.GONE);
        } else {
            maintance_inpecte_dvr.setText(this.getActivity().getResources().getString(R.string.mangae_unconnect));
            image_dvr.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void ShowTaximeterState(int state) {
        if (state == 0x01) {
            maintance_inpecte_taximeter.setText(this.getActivity().getResources().getString(R.string.mangae_connect));
            image_taximeter.setVisibility(View.GONE);
        } else {
            maintance_inpecte_taximeter.setText(this.getActivity().getResources().getString(R.string.mangae_unconnect));
            image_taximeter.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void ShowCarScreenState(int state) {
        if (state == 0x01) {
            maintance_inpecte_carscreen.setText(this.getActivity().getResources().getString(R.string.mangae_connect));
            image_carscreen.setVisibility(View.GONE);
        } else {
            maintance_inpecte_carscreen.setText(this.getActivity().getResources().getString(R.string.mangae_unconnect));
            image_carscreen.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void ShowGPSInfo(int result) {
        if (result == 0x01) {
            maintance_inpecte_gps.setText(this.getActivity().getResources().getString(R.string.mangae_connect));
            image_gsp.setVisibility(View.GONE);
        } else {
            maintance_inpecte_gps.setText(this.getActivity().getResources().getString(R.string.mangae_unconnect));
            image_gsp.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void ShowNetModel(int result) {
        if (result == 0x01) {
            maintance_inpecte_net.setText(this.getActivity().getResources().getString(R.string.mangae_connect));
            image_net.setVisibility(View.GONE);
        } else {
            maintance_inpecte_net.setText(this.getActivity().getResources().getString(R.string.mangae_unconnect));
            image_net.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void ShowPowerInfo(int result) {
        if (result == 0x01) {
            maintance_inpecte_power.setText(this.getActivity().getResources().getString(R.string.mangae_normal));
            image_power.setVisibility(View.GONE);
        } else {
            maintance_inpecte_power.setText(this.getActivity().getResources().getString(R.string.maintance_inpecte_power_fuiler));
            image_power.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void ShowRentServerState(int state) {
        if (state == 0x01) {
            maintance_inpecte_rentserver.setText(this.getActivity().getResources().getString(R.string.mangae_connect));
            image_rentserver.setVisibility(View.GONE);
        } else {
            maintance_inpecte_rentserver.setText(this.getActivity().getResources().getString(R.string.mangae_unconnect));
            image_rentserver.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void ShowVideoServerState(int state) {
        if (state == 0x01) {
            maintance_inpecte_vedioserver.setText(this.getActivity().getResources().getString(R.string.mangae_connect));
            image_vedioserver.setVisibility(View.GONE);
        } else {
            maintance_inpecte_vedioserver.setText(this.getActivity().getResources().getString(R.string.mangae_unconnect));
            image_vedioserver.setVisibility(View.VISIBLE);
        }
    }

}
