package com.uninew.maintanence.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uninew.maintanence.interfaces.INetInfoPresenter;
import com.uninew.maintanence.interfaces.INetInfoView;
import com.uninew.maintanence.presenter.NetPresent;
import com.uninew.settings.R;


/**
 * Created by Administrator on 2017/8/30.
 */


public class NetFragment extends Fragment implements INetInfoView {
    private View view;
    private INetInfoPresenter mINetInfoPresenter;
    private static Context mContext;
    //界面相关
    private TextView maintance_net_sim, maintance_net_facilitator, maintance_net_signal, maintance_net_roam, maintance_net_type,
            maintance_net_state, maintance_net_ip;
    private ImageView image_sim, image_facilitator, image_signal, image_roam, image_type, image_state, image_ip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_net, container, false);
        initView();
        mContext = this.getActivity();
        mINetInfoPresenter = new NetPresent(mContext, this);
        SetListener();
        return view;
    }

    private void initView() {
        maintance_net_sim = (TextView) view.findViewById(R.id.maintance_net_sim);
        maintance_net_facilitator = (TextView) view.findViewById(R.id.maintance_net_facilitator);
        maintance_net_signal = (TextView) view.findViewById(R.id.maintance_net_signal);
        maintance_net_roam = (TextView) view.findViewById(R.id.maintance_net_roam);
        maintance_net_type = (TextView) view.findViewById(R.id.maintance_net_type);
        maintance_net_state = (TextView) view.findViewById(R.id.maintance_net_state);
        maintance_net_ip = (TextView) view.findViewById(R.id.maintance_net_ip);

        image_sim = (ImageView) view.findViewById(R.id.image_sim);
        image_facilitator = (ImageView) view.findViewById(R.id.image_facilitator);
        image_signal = (ImageView) view.findViewById(R.id.image_signal);
        image_roam = (ImageView) view.findViewById(R.id.image_roam);
        image_type = (ImageView) view.findViewById(R.id.image_type);
        image_state = (ImageView) view.findViewById(R.id.image_state);
        image_ip = (ImageView) view.findViewById(R.id.image_ip);

    }

    private void SetListener() {
        mINetInfoPresenter.startPhoneListener();
    }


    @Override
    public void ShowSIMState(String state) {
        maintance_net_sim.setText(state);
        if (state.equals(this.getActivity().getResources().getString(R.string.net_txt_unknowstate)) ||
                state.equals(this.getActivity().getResources().getString(R.string.net_txt_nocardstate))) {
            image_sim.setVisibility(View.VISIBLE);
        } else {
            image_sim.setVisibility(View.GONE);
        }
    }

    @Override
    public void ShowFacilitator(String msg) {

        maintance_net_facilitator.setText(msg);
        if (msg.equals(this.getActivity().getResources().getString(R.string.net_txt_unknowstate))) {
            image_facilitator.setVisibility(View.VISIBLE);
        } else {
            image_facilitator.setVisibility(View.GONE);
        }

    }

    @Override
    public void ShowSignal(int signal) {
        //        if (signal == -60) {
        //            image_signal.setVisibility(View.VISIBLE);
        //        } else {
        //            image_signal.setVisibility(View.GONE);
        //        }
        image_signal.setVisibility(View.GONE);
        maintance_net_signal.setText(String.valueOf(signal));
    }

    @Override
    public void ShowRoam(String state) {
        maintance_net_roam.setText(state);
        image_roam.setVisibility(View.GONE);
    }

    @Override
    public void ShowNetType(String type) {
        maintance_net_type.setText(type);
        if (type.equals(this.getActivity().getResources().getString(R.string.net_txt_unknowstate))) {
            image_type.setVisibility(View.VISIBLE);
        } else {
            image_type.setVisibility(View.GONE);
        }
    }
    @Override
    public void ShowNetState(String state) {
        maintance_net_state.setText(state);
        if (state.equals(mContext.getResources().getString(R.string.net_txt_netno))) {
            image_state.setVisibility(View.VISIBLE);
        } else {
            image_state.setVisibility(View.GONE);
        }

    }

    @Override
    public void ShowIPAddress(String ip) {
        maintance_net_ip.setText(ip);
        if (TextUtils.isEmpty(ip)) {
            image_ip.setVisibility(View.VISIBLE);
            maintance_net_ip.setText(this.getActivity().getResources().getString(R.string.txt_emty));
        } else {
            image_ip.setVisibility(View.GONE);
        }
    }
}
