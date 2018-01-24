package com.uninew.net.JT905.transport;

import android.content.Context;

import com.uninew.net.JT905.sms.ISmsManage;
import com.uninew.net.JT905.sms.ISmsManagerCallBack;
import com.uninew.net.JT905.sms.SmsManage;
import com.uninew.net.JT905.tcp.ITcpManage;
import com.uninew.net.JT905.tcp.ITcpManagerCallBack;
import com.uninew.net.JT905.tcp.TCPLinkErrorEnum;
import com.uninew.net.JT905.tcp.TCPRunStateEnum;
import com.uninew.net.JT905.tcp.TcpManage;

/**
 * 管理不同通讯方式
 * Created by Administrator on 2017/8/11 0011.
 */

public class Transport implements ITransport,ITcpManagerCallBack,ISmsManagerCallBack {

    private static final String TAG="Transport";
    private static final boolean D=true;
    private ITransportCallBack mTransportListener;
    private Context mContext;
    private ITcpManage tcpManager;
    private ISmsManage smsManager;
    private static Transport instance;

    private Transport(Context mContext) {
        this.mContext = mContext;
        tcpManager=TcpManage.getInstance(mContext);
        tcpManager.setTcpManagerListener(this);
        smsManager= SmsManage.getInstance(mContext);
        smsManager.setSmsManagerListener(this);
    }

    public synchronized static Transport getInstance(Context mContext){
        if (instance==null){
            instance=new Transport(mContext);
        }
        return instance;
    }

    @Override
    public void setTransportListener(ITransportCallBack mTransportListener){
        this.mTransportListener = mTransportListener;
    }

    @Override
    public boolean createSocket(int tcpId, String ip, int port) {
        return  tcpManager.createSocket(tcpId,ip,port);
    }

    @Override
    public boolean closeSocket(int tcpId) {
        return tcpManager.closeSocket(tcpId);
    }

    @Override
    public void sendMsgByTcp(int tcpId, byte[] datas) {
        tcpManager.sendMsg(tcpId,datas);
    }

    @Override
    public void sendMsgBySms(String phoneNumber, byte[] datas) {
        smsManager.sendSmsMsg(phoneNumber,datas);
    }

    //---------------------------上传--------------------------------------

    @Override
    public void onTCPRunState(int tcpId, TCPRunStateEnum vValue, TCPLinkErrorEnum error) {
        mTransportListener.onTCPRunState(tcpId,vValue,error);
    }

    @Override
    public void onTCPReceiveDatas(int tcpId, byte[] vBuffer) {
        mTransportListener.onReceiveTcpDatas(tcpId,vBuffer);
    }

    @Override
    public void onSMSReceiveDatas(String phoneNumber, byte[] vBuffer) {
        mTransportListener.onReceiveSMSDatas(phoneNumber,vBuffer);
    }

}
