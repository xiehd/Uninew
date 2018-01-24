package com.uninew.mangaement.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.uninew.car.db.settings.PlatformLocalDataSource;
import com.uninew.car.db.settings.PlatformLocalSource;
import com.uninew.car.db.settings.PlatformSettings;
import com.uninew.constant.DefineDefault;
import com.uninew.mangaement.interfaces.IParameterModel;
import com.uninew.mangaement.interfaces.IParameterPresenter;
import com.uninew.net.JT905.comm.client.ClientReceiveManage;
import com.uninew.net.JT905.comm.client.ClientSendManage;
import com.uninew.net.JT905.comm.client.IClientReceiveListener;
import com.uninew.net.JT905.comm.client.IClientReceiveManage;
import com.uninew.net.JT905.comm.client.IClientSendManage;
import com.uninew.net.JT905.common.IpInfo;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2017/9/21.
 */

public class ParameterModel implements IParameterModel {
    private String TAG = "ParameterModel";
    private boolean D = true;
    private IClientReceiveManage mClientReceiveManage;
    private IClientSendManage mClientSendManage;
    private Context mContext;
    private IParameterPresenter mIParameterPresenter;
    private PlatformLocalSource db;

    public ParameterModel(Context context, IParameterPresenter mIParameterPresenter) {

        this.mContext = context;
        mClientReceiveManage = new ClientReceiveManage(mContext);
        mClientSendManage = new ClientSendManage(mContext);
        this.mIParameterPresenter = mIParameterPresenter;
        db = PlatformLocalDataSource.getInstance(mContext);
        init();
    }

    private void init() {

        db.getService(DefineDefault.TCP_ONE, new PlatformLocalSource.GetPlaformCallBack() {
            @Override
            public void onDBBaseDataLoaded(PlatformSettings platformSettings) {
                mIParameterPresenter.ShowServer1(platformSettings.getMainIp(), String.valueOf(platformSettings.getMainPort()));
                mIParameterPresenter.ShowSpareServer1(platformSettings.getSpareIp(), String.valueOf(platformSettings.getSparePort()));
            }

            @Override
            public void onDataNotAailable() {
                mIParameterPresenter.ShowServer1("", "");
                mIParameterPresenter.ShowSpareServer1("", "");
            }
        });

        db.getService(DefineDefault.TCP_TWO, new PlatformLocalSource.GetPlaformCallBack() {
            @Override
            public void onDBBaseDataLoaded(PlatformSettings platformSettings) {
                mIParameterPresenter.ShowServer2(platformSettings.getMainIp(), String.valueOf(platformSettings.getMainPort()));
                mIParameterPresenter.ShowSpareServer2(platformSettings.getSpareIp(), String.valueOf(platformSettings.getSparePort()));
            }

            @Override
            public void onDataNotAailable() {
                mIParameterPresenter.ShowSpareServer2("", "");
                mIParameterPresenter.ShowServer2("", "");
            }
        });

    }

    private IClientReceiveListener.IConnectListener connectListener = new IClientReceiveListener.IConnectListener() {
        @Override
        public void linkStateNotify(IpInfo ipInfo) {
            mIParameterPresenter.ShowlinkStateNotify(ipInfo);
        }
    };

    @Override
    public void registerConnectStateListener() {
        if (mClientReceiveManage != null) {
            mClientReceiveManage.registerConnectStateListener(connectListener);
        }

    }

    @Override
    public void unregisterConnectStateListener() {
        if (mClientReceiveManage != null) {
            mClientReceiveManage.unRegisterCallListener();
        }
    }

    @Override
    public void createLink(int tcpid, String mainIp, int mainPort, String spareIp, int sparePort) {
        if (mClientSendManage != null) {
            IpInfo ipInfo1 = new IpInfo(tcpid, mainIp, mainPort, spareIp, sparePort);
            mClientSendManage.createLink(ipInfo1);
        }
    }

    @Override
    public void disConnectLink(int tcpId) {
        if (mClientSendManage != null) {
            mClientSendManage.disConnectLink(tcpId);
        }
    }

    @Override
    public void queryLinkState(int tcpId) {
        if (mClientSendManage != null) {
            mClientSendManage.queryLinkState(tcpId);
        }
    }

    @Override
    public void saveServerDB(int tcpID, String server1Address, int server1Port, String server1Pare1Address, int server1ParePort) {

        Log.d(TAG, "-----saveServerDB------" + "tcpID=" + tcpID + ",server1Address=" + server1Address + "," + "server1Port=" +
                server1Port + "," + "server1Pare1Address=" + server1Pare1Address + "," + "server1ParePort=" + server1ParePort);
        if (!TextUtils.isEmpty(server1Address) || !TextUtils.isEmpty(server1Pare1Address)) {
            db.setService(new PlatformSettings(tcpID, server1Address, server1Port, server1Pare1Address, server1ParePort));
            mClientSendManage.createLink(new IpInfo(tcpID, server1Address, server1Port, server1Pare1Address, server1ParePort));
        }
    }

    @Override
    public void getNameConnectLink(String name) {
        try {
            final URL url = new URL(name);
            Log.d(TAG, url.getAuthority() + "**" + url.getDefaultPort() + "**" + url.getHost() + "**" + url.getPath() + "**" + url.getPort());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    getIP(url.getHost(),url.getPort(),url.getDefaultPort());
                }
            }).start();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void getIP(String host,int port,int defaultPort) {
        String ip_devdiv = null;
        int ip_port = 80;
        try {
            InetAddress x = java.net.InetAddress.getByName(host);
            ip_devdiv = x.getHostAddress();//得到字符串形式的ip地址
            Log.d(TAG, "解析IP地址为：" + ip_devdiv);
            if (port == -1){
                ip_port = defaultPort;
            }else{
                ip_port = port;
            }

        } catch (UnknownHostException e){
            e.printStackTrace();
            Log.e(TAG, "域名解析出错:"+e.getMessage());

        }
        mClientSendManage.createLink(new IpInfo(0x00, ip_devdiv, ip_port, "", 0));
    }
}
