package com.uninew.mangaement.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uninew.constant.DefineDefault;
import com.uninew.mangaement.interfaces.IParameterPresenter;
import com.uninew.mangaement.interfaces.IParameterView;
import com.uninew.mangaement.presenter.ParameterPresenter;
import com.uninew.net.JT905.common.IpInfo;
import com.uninew.settings.R;
import com.uninew.until.SPTools;
import com.uninew.until.TextTools;
import com.uninew.until.ToastCommon;


/**
 * Created by Administrator on 2017/8/30.
 */


public class ParameterFragment extends Fragment implements View.OnClickListener, IParameterView {
    private View view;
    private static final boolean D = true;
    private static final String TAG = "ParameterFragment";

    private EditText mangae_parameter_rentalServer, mangae_parameter_rentalServerPort, mangae_parameter_Spare_rentalServer,
            mangae_parameter_Spare_rentalServerPort, mangae_parameter_rentalServer2, mangae_parameter_rentalServerPort2,
            mangae_parameter_Spare_rentalServer2, mangae_parameter_Spare_rentalServerPort2,mangae_parameter_edidomain;
    private Button parameter_btn_defult, parameter_btn_save,parmeter_text_domain;
    private CheckBox parmeter_chekbox_address1, parmeter_chekbox_beiyongaddress1, parmeter_chekbox_address2, parmeter_chekbox_beiyongaddress2;
    private TextView parmeter_text_connect1, parmeter_text_beiyongconnect1,
            parmeter_text_connect2, parmeter_text_beiyongconnect2;
    //以上是布局相关
    private IParameterPresenter mIParameterPresenter;
    private SPTools mSPTools;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mangae_parameter, container, false);
        initView();
        mIParameterPresenter = new ParameterPresenter(this.getActivity(), this);
        mSPTools = new SPTools(this.getActivity());
        SetListener();
        init();//初始化基本操作
        return view;
    }

    private void initView() {

        mangae_parameter_rentalServer = (EditText) view.findViewById(R.id.mangae_parameter_rentalServer);
        mangae_parameter_rentalServerPort = (EditText) view.findViewById(R.id.mangae_parameter_rentalServerPort);
        mangae_parameter_rentalServerPort2 = (EditText) view.findViewById(R.id.mangae_parameter_rentalServerPort2);
        mangae_parameter_Spare_rentalServer2 = (EditText) view.findViewById(R.id.mangae_parameter_Spare_rentalServer2);
        mangae_parameter_Spare_rentalServerPort2 = (EditText) view.findViewById(R.id.mangae_parameter_Spare_rentalServerPort2);
        mangae_parameter_Spare_rentalServer = (EditText) view.findViewById(R.id.mangae_parameter_Spare_rentalServer);
        mangae_parameter_Spare_rentalServerPort = (EditText) view.findViewById(R.id.mangae_parameter_Spare_rentalServerPort);
        mangae_parameter_rentalServer2 = (EditText) view.findViewById(R.id.mangae_parameter_rentalServer2);
        mangae_parameter_edidomain = (EditText) view.findViewById(R.id.mangae_parameter_edidomain);

        parameter_btn_defult = (Button) view.findViewById(R.id.parameter_btn_defult);
        parameter_btn_save = (Button) view.findViewById(R.id.parameter_btn_save);
        parmeter_text_domain = (Button) view.findViewById(R.id.parmeter_text_domain);

        parmeter_chekbox_address1 = (CheckBox) view.findViewById(R.id.parmeter_chekbox_address1);
        parmeter_chekbox_beiyongaddress1 = (CheckBox) view.findViewById(R.id.parmeter_chekbox_beiyongAddress1);
        parmeter_chekbox_address2 = (CheckBox) view.findViewById(R.id.parmeter_chekbox_address2);
        parmeter_chekbox_beiyongaddress2 = (CheckBox) view.findViewById(R.id.parmeter_chekbox_beiyongAddress2);

        parmeter_text_connect1 = (TextView) view.findViewById(R.id.parmeter_text_connect1);
        parmeter_text_beiyongconnect1 = (TextView) view.findViewById(R.id.parmeter_text_beiyongconnect1);
        parmeter_text_connect2 = (TextView) view.findViewById(R.id.parmeter_text_connect2);
        parmeter_text_beiyongconnect2 = (TextView) view.findViewById(R.id.parmeter_text_beiyongconnect2);
    }

    private void SetListener() {
        Log.d(TAG, "---onCreateView:SetListener---registerConnectStateListener");
        mIParameterPresenter.registerConnectStateListener();
        parameter_btn_save.setOnClickListener(this);
        parameter_btn_defult.setOnClickListener(this);
        parmeter_text_domain.setOnClickListener(this);

    }

    private void init() {
        mIParameterPresenter.queryLinkState(DefineDefault.TCP_ONE);
        mIParameterPresenter.queryLinkState(DefineDefault.TCP_TWO);
        mangae_parameter_edidomain.setText(mSPTools.getSharedString(SPTools.PARAMTER_HOST_KEY));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.parmeter_text_domain:
                String name = mangae_parameter_edidomain.getText().toString().trim();
                mIParameterPresenter.getNameConnectLink("http://"+name);
                mSPTools.putSharedString(SPTools.PARAMTER_HOST_KEY,name);
                break;
            case R.id.parameter_btn_defult:
                if (parmeter_chekbox_address1.isChecked()) {
                    mIParameterPresenter.disConnectLink(DefineDefault.TCP_ONE);
                }
                if (parmeter_chekbox_address2.isChecked()) {
                    mIParameterPresenter.disConnectLink(DefineDefault.TCP_TWO);
                }

                break;
            case R.id.parameter_btn_save:
                //mIParameterPresenter.createLink();
                try {
                    String server1 = null;
                    int serverport1 = 0000;
                    String Spareserver1 = null;
                    int Spareserverport1 = 0000;
                    String server2 = null;
                    int serverport2 = 0000;
                    String Spareserver2 = null;
                    int Spareserverport2 = 0000;
                    if (parmeter_chekbox_address1.isChecked()) {
                        server1 = mangae_parameter_rentalServer.getText().toString();
                        if (!TextUtils.isEmpty(server1) && !TextTools.IsIPformat(server1)) {
                            ToastCommon.ToastShow(this.getActivity().getApplicationContext(),
                                    this.getActivity().getResources().getString(R.string.settings_toast_ipformat) + server1);
                            break;
                        }
                        if (!TextUtils.isEmpty(server1)) {
                            serverport1 = Integer.parseInt(mangae_parameter_rentalServerPort.getText().toString());
                        }
                        Spareserver1 = mangae_parameter_Spare_rentalServer.getText().toString();
                        if (!TextUtils.isEmpty(Spareserver1) && !TextTools.IsIPformat(Spareserver1)) {
                            ToastCommon.ToastShow(this.getActivity().getApplicationContext(),
                                    this.getActivity().getResources().getString(R.string.settings_toast_ipformat) + Spareserver1);
                            break;
                        }
                        if (!TextUtils.isEmpty(Spareserver1)) {
                            Spareserverport1 = Integer.parseInt(mangae_parameter_Spare_rentalServerPort.getText().toString());
                        }
                        mIParameterPresenter.saveServerDB(DefineDefault.TCP_ONE, server1, serverport1, Spareserver1, Spareserverport1);
                    }

                    if (parmeter_chekbox_address2.isChecked()) {
                        server2 = mangae_parameter_rentalServer2.getText().toString();
                        if (!TextUtils.isEmpty(server2) && !TextTools.IsIPformat(server2)) {
                            ToastCommon.ToastShow(this.getActivity().getApplicationContext(),
                                    this.getActivity().getResources().getString(R.string.settings_toast_ipformat) + server2);
                            break;
                        }
                        if (!TextUtils.isEmpty(server2)) {
                            serverport2 = Integer.parseInt(mangae_parameter_rentalServerPort2.getText().toString());
                        }
                        Spareserver2 = mangae_parameter_Spare_rentalServer2.getText().toString();
                        if (!TextUtils.isEmpty(Spareserver2) && !TextTools.IsIPformat(Spareserver2)) {
                            ToastCommon.ToastShow(this.getActivity().getApplicationContext(),
                                    this.getActivity().getResources().getString(R.string.settings_toast_ipformat) + Spareserverport2);
                            break;
                        }
                        if (!TextUtils.isEmpty(Spareserver2)) {
                            Spareserverport2 = Integer.parseInt(mangae_parameter_Spare_rentalServerPort2.getText().toString());
                        }
                        mIParameterPresenter.saveServerDB(DefineDefault.TCP_TWO, server2, serverport2, Spareserver2, Spareserverport2);
                    }
                } catch (Exception e) {
                    Toast.makeText(this.getActivity().getApplicationContext(), this.getActivity().getResources().getString(R.string.settings_toast_portformat), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void ShowlinkStateNotify(IpInfo ipInfo) {
        if (D)
            Log.d(TAG, "---ShowlinkStateNotify---ipInfo=" + ipInfo.toString());
        //查询连接状况
        switch (ipInfo.tcpId) {
            case DefineDefault.TCP_ONE://平台1
                if (ipInfo.linkState == 1) {
                    if (ipInfo.isLinkMain) {
                        parmeter_text_connect1.setVisibility(View.VISIBLE);
                        parmeter_text_beiyongconnect1.setVisibility(View.GONE);
                        parmeter_text_connect1.setText(this.getActivity().getResources().getString(R.string.mangae_connect));
                        mangae_parameter_rentalServer.setText(ipInfo.mainIp);
                        mangae_parameter_rentalServerPort.setText(ipInfo.mainPort+"");
                    } else {
                        parmeter_text_connect1.setVisibility(View.GONE);
                        parmeter_text_beiyongconnect1.setVisibility(View.VISIBLE);
                        parmeter_text_beiyongconnect1.setText(this.getActivity().getResources().getString(R.string.mangae_connect));
                        mangae_parameter_Spare_rentalServer.setText(ipInfo.spareIp);
                        mangae_parameter_Spare_rentalServerPort.setText(ipInfo.sparePort+"");
                    }
                } else {
                    parmeter_text_connect1.setVisibility(View.VISIBLE);
                    parmeter_text_beiyongconnect1.setVisibility(View.GONE);
                    parmeter_text_connect1.setText(this.getActivity().getResources().getString(R.string.mangae_unconnect));

                }
                break;
            case DefineDefault.TCP_TWO://平台2
                if (ipInfo.linkState == 1) {
                    if (ipInfo.isLinkMain) {
                        parmeter_text_connect2.setVisibility(View.VISIBLE);
                        parmeter_text_beiyongconnect2.setVisibility(View.GONE);
                        parmeter_text_connect2.setText(this.getActivity().getResources().getString(R.string.mangae_connect));
                        mangae_parameter_rentalServer2.setText(ipInfo.mainIp);
                        mangae_parameter_rentalServerPort2.setText(ipInfo.mainPort+"");
                    } else {
                        parmeter_text_connect2.setVisibility(View.GONE);
                        parmeter_text_beiyongconnect2.setVisibility(View.VISIBLE);
                        parmeter_text_beiyongconnect2.setText(this.getActivity().getResources().getString(R.string.mangae_connect));
                        mangae_parameter_Spare_rentalServer2.setText(ipInfo.spareIp);
                        mangae_parameter_Spare_rentalServerPort2.setText(ipInfo.sparePort+"");
                    }
                } else {
                    parmeter_text_connect2.setVisibility(View.VISIBLE);
                    parmeter_text_beiyongconnect2.setVisibility(View.GONE);
                    parmeter_text_connect2.setText(this.getActivity().getResources().getString(R.string.mangae_unconnect));

                }
                break;
        }

    }

    @Override
    public void ShowServer1(String address, String port) {
        mangae_parameter_rentalServer.setText(address);
        mangae_parameter_rentalServerPort.setText(port);
    }

    @Override
    public void ShowSpareServer1(String address, String port) {
        mangae_parameter_Spare_rentalServer.setText(address);
        mangae_parameter_Spare_rentalServerPort.setText(port);
    }

    @Override
    public void ShowServer2(String address, String port) {
        mangae_parameter_rentalServer2.setText(address);
        mangae_parameter_rentalServerPort2.setText(port);
    }

    @Override
    public void ShowSpareServer2(String address, String port) {
        mangae_parameter_Spare_rentalServer2.setText(address);
        mangae_parameter_Spare_rentalServerPort2.setText(port);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "---onDestroy---unregisterConnectStateListener");
        mIParameterPresenter.unregisterConnectStateListener();
    }
}
