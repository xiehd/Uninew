package com.uninew.net.JT905.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.uninew.net.JT905.transport.Transport;
import com.uninew.net.JT905.common.Define;
import com.uninew.net.JT905.tcp.TCPLinkErrorEnum;
import com.uninew.net.JT905.tcp.TCPRunStateEnum;
import com.uninew.net.JT905.transport.ITransport;
import com.uninew.net.JT905.transport.ITransportCallBack;

/**
 *  对接收的数据进行粘包处理。
 * Created by Administrator on 2017/8/17 0017.
 *
 */

public class NetBase implements INetBase, ITransportCallBack,StickyBagHandle.IDatasCallBack {

    private static final boolean D=true;
    private static final String TAG="NetBase";
    private Context mContext;
    private ITransport mTransport;
    private INetBaseCallBack mNetbaseCallBack;

    private StickyBagHandle mStickyBagHandle1, mStickyBagHandle2;
    private static NetBase instance;

    private NetBase(Context mContext) {
        this.mContext = mContext;
        mTransport= Transport.getInstance(mContext);
        mTransport.setTransportListener(this);
        mStickyBagHandle1 =new StickyBagHandle(Define.TCP_ONE,this);
        mStickyBagHandle2 =new StickyBagHandle(Define.TCP_TWO,this);
        if(D)Log.d(TAG," NetBase init !!!!");
    }

    public synchronized static NetBase getInstance(Context mContext){
        if (instance==null){
            instance=new NetBase(mContext);
        }
        return instance;
    }


    @Override
    public void setNetBaseListener(INetBaseCallBack mNetbaseCallBack){
        this.mNetbaseCallBack = mNetbaseCallBack;
    }

//---------------------------------------上报------------------------------------------------
    @Override
    public void onTCPRunState(int tcpId, TCPRunStateEnum vValue, TCPLinkErrorEnum error) {
        mNetbaseCallBack.onTCPRunState(tcpId, vValue, error);
    }

    @Override
    public void onReceiveTcpDatas(int tcpId, byte[] datas) {
        switch (tcpId){
            case Define.TCP_ONE:
                mStickyBagHandle1.dataHandleRunnable.dataput(datas,datas.length);
                break;
            case Define.TCP_TWO:
                mStickyBagHandle2.dataHandleRunnable.dataput(datas,datas.length);
                break;
        }
    }

    @Override
    public void onReceiveSMSDatas(String phoneNumber, byte[] datas) {
        mNetbaseCallBack.onReceiveSMSDatas(phoneNumber,datas);
    }

    @Override
    public void datasCallBack(int tcpId, byte[] datas) {
       //接收粘包处理结果
        mNetbaseCallBack.onReceiveTcpDatas(tcpId,datas);
    }

//--------------------------------------消息发送-------------------------------------------
    @Override
    public boolean createSocket(int tcpId, String ip, int port) {
        if(D)Log.d(TAG," createSocket   ip="+ip+" ,port="+port);
        if (TextUtils.isEmpty(ip) || port == 0) {
            mNetbaseCallBack.onTCPRunState(tcpId,TCPRunStateEnum.OPEN_FAILURE,TCPLinkErrorEnum.ERROR_IPORPORT);
            Log.e(TAG, "输入的IP地址或者端口号为空");
            return false;
        }
        return mTransport.createSocket(tcpId,ip,port);
    }

    @Override
    public boolean closeSocket(int tcpId) {
        return mTransport.closeSocket(tcpId);
    }

    @Override
    public void sendMsgByTcp(int tcpId, byte[] datas) {
        mTransport.sendMsgByTcp(tcpId,datas);
    }

    @Override
    public void sendMsgBySms(String phoneNumber, byte[] datas) {
        mTransport.sendMsgBySms(phoneNumber,datas);
    }
}
