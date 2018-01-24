package com.uninew.mangaement.presenter;

import android.content.Context;

import com.uninew.mangaement.interfaces.IParameterModel;
import com.uninew.mangaement.interfaces.IParameterPresenter;
import com.uninew.mangaement.interfaces.IParameterView;
import com.uninew.mangaement.model.ParameterModel;
import com.uninew.net.JT905.common.IpInfo;

/**
 * Created by Administrator on 2017/9/21.
 */

public class ParameterPresenter implements IParameterPresenter {

    private IParameterView mIParameterView;
    private IParameterModel mIParameterModel;
    private Context mContext;

    public ParameterPresenter(Context con, IParameterView mIParameterView) {
        this.mIParameterView = mIParameterView;
        this.mContext = con;
        this.mIParameterModel = new ParameterModel(con, this);
    }

    @Override
    public void registerConnectStateListener() {
        mIParameterModel.registerConnectStateListener();
    }

    @Override
    public void unregisterConnectStateListener() {
        mIParameterModel.unregisterConnectStateListener();
    }

    @Override
    public void createLink(int tcpid, String mainIp, int mainPort, String spareIp, int sparePort) {
        mIParameterModel.createLink(tcpid, mainIp, mainPort, spareIp, sparePort);
    }

    @Override
    public void disConnectLink(int tcpId) {
        mIParameterModel.disConnectLink(tcpId);
    }

    @Override
    public void queryLinkState(int tcpId) {
        mIParameterModel.queryLinkState(tcpId);
    }

    @Override
    public void saveServerDB(int tcpID,String server1Address, int server1Port, String serverPare1Address,int serverParePort) {
            mIParameterModel.saveServerDB(tcpID,server1Address,server1Port,serverPare1Address,serverParePort);
    }

    @Override
    public void getNameConnectLink(String name) {
        mIParameterModel.getNameConnectLink(name);
    }

    @Override
    public void ShowlinkStateNotify(IpInfo ipInfo) {
        mIParameterView.ShowlinkStateNotify(ipInfo);
    }

    @Override
    public void ShowServer1(String address, String port) {
        mIParameterView.ShowServer1(address, port);
    }

    @Override
    public void ShowSpareServer1(String address, String port) {
        mIParameterView.ShowSpareServer1(address, port);
    }

    @Override
    public void ShowServer2(String address, String port) {
        mIParameterView.ShowServer2(address, port);
    }

    @Override
    public void ShowSpareServer2(String address, String port) {
        mIParameterView.ShowSpareServer2(address, port);
    }
}
