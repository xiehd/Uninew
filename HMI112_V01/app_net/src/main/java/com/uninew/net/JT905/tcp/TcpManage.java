package com.uninew.net.JT905.tcp;

import android.content.Context;
import android.util.Log;

import com.uninew.net.JT905.util.ByteTools;

import java.util.HashMap;
import java.util.Map;

import tools.TimeTool;

/**
 * 管理多平台使用
 * Created by Administrator on 2017/8/17 0010.
 */

public class TcpManage implements ITcpManage, ITcpCallBack {

    private static final String TAG = "TcpManage";
    private static boolean D = true;
    private Context mContext;
    private static Map<Integer, TCPLink> tcpMaps;
    private TCPLink tcpLink;
    private ITcpManagerCallBack mTcpManagerListener;
    private static TcpManage instance;

    private TcpManage(Context mContext) {
        this.mContext = mContext;
        tcpMaps = new HashMap<>();
    }

    @Override
    public void setTcpManagerListener(ITcpManagerCallBack mTcpManagerListener) {
        this.mTcpManagerListener = mTcpManagerListener;
    }

    public static TcpManage getInstance(Context mContext) {
        if (instance != null) {

        } else {
            synchronized (TcpManage.class) {
                if (instance == null) {
                    instance = new TcpManage(mContext);
                }
            }
        }
        return instance;
    }

    @Override
    public boolean createSocket(int tcpId, String ip, int port) {
        if (tcpMaps != null) {
            tcpLink = tcpMaps.get(tcpId);
            if (tcpLink != null) {
                closeSocket(tcpId);
                tcpMaps.remove(tcpId);
            }
        }
        tcpLink = new TCPLink(mContext, tcpId);
        tcpLink.setTcpBackInterface(this);
        tcpMaps.put(tcpId, tcpLink);
        tcpLink.openSocket(ip, port);
        return true;
    }

    @Override
    public boolean closeSocket(int tcpId) {
        if (tcpMaps == null && !tcpMaps.containsKey(tcpId)) {
            Log.e(TAG, "--closeSocket--tcpMaps is NULL");
            return false;
        }
        tcpLink = tcpMaps.get(tcpId);
        if (tcpLink == null) {
            Log.e(TAG, "--closeSocket--tcpLink is NULL");
            return false;
        }
        tcpLink.closeSocket();
        tcpMaps.remove(tcpId);
        Log.e(TAG, "--closeSocket--tcpMaps.size=" + tcpMaps.size());
        return true;
    }

    @Override
    public void sendMsg(int tcpId, byte[] datas) {
        if (tcpMaps == null) {
            Log.e(TAG, "--sendMsg--tcpMaps is NULL");
            return;
        }
        tcpLink = tcpMaps.get(tcpId);
        if (tcpLink == null) {
            Log.e(TAG, "--sendMsg--tcpLink is NULL,tcpId=" + tcpId);
            return;
        }
        if (datas != null && datas.length > 0)
            Log.d(TAG, ByteTools.logBytes(datas));
        tcpLink.sendMsg(datas);
    }

    //------------------------------------上传--------------------------------------------

    @Override
    public void onTCPRunState(int tcpId, TCPRunStateEnum vValue, TCPLinkErrorEnum error) {
        if (D)Log.d(TAG, "onTCPRunState,tcpId=" + tcpId + ",TCPRunStateEnum="
                + vValue + " ,TCPLinkErrorEnum=" + error);
        mTcpManagerListener.onTCPRunState(tcpId, vValue, error);
    }

    @Override
    public void onTCPReceiveDatas(int tcpId, byte[] vBuffer) {
        mTcpManagerListener.onTCPReceiveDatas(tcpId, vBuffer);
        if (D) Log.d(TAG, "tcpId=" + tcpId + " ,vBuffer=" + ByteTools.logBytes(vBuffer));
    }

}
