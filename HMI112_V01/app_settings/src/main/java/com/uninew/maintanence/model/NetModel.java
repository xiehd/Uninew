package com.uninew.maintanence.model;

import android.content.Context;

import com.uninew.maintanence.interfaces.INetInfoModel;
import com.uninew.maintanence.interfaces.INetInfoPresenter;
import com.uninew.settings.R;

/**
 * Created by Administrator on 2017/9/25.
 */

public class NetModel implements INetInfoModel ,INetInfoModel.INetSignalListener{
    private INetInfoPresenter mINetInfoPresenter;
    private Context mContext;
    private NetManager mNetManager;
    public NetModel(Context mContext,INetInfoPresenter mINetInfoPresenter) {
        this.mINetInfoPresenter = mINetInfoPresenter;
        this.mContext = mContext;
        mNetManager = new NetManager(mContext,this);
        init();
    }

    private void init(){
        //SIM状态
        if(mNetManager.getSimState() == 0){
            mINetInfoPresenter.ShowSIMState(mContext.getString(R.string.net_txt_unknowstate));
        }else if(mNetManager.getSimState() == 1){
            mINetInfoPresenter.ShowSIMState(mContext.getString(R.string.net_txt_nocardstate));
        }else if(mNetManager.getSimState() == 2){
            mINetInfoPresenter.ShowSIMState(mContext.getString(R.string.net_txt_lockedstate));
        }else if(mNetManager.getSimState() == 3){
            mINetInfoPresenter.ShowSIMState(mContext.getString(R.string.net_txt_readystate));
        }
        //服务商
        if(mNetManager.getSimOperator() == 0){
            mINetInfoPresenter.ShowFacilitator(mContext.getString(R.string.net_txt_unknowstate));
        }else if(mNetManager.getSimOperator() == 1){
            mINetInfoPresenter.ShowFacilitator(mContext.getString(R.string.net_txt_cmobile));
        }else if(mNetManager.getSimOperator() == 2){
            mINetInfoPresenter.ShowFacilitator(mContext.getString(R.string.net_txt_cunicom));
        }else if(mNetManager.getSimOperator() == 3){
            mINetInfoPresenter.ShowFacilitator(mContext.getString(R.string.net_txt_ctelecommunication));
        }

        String txt;
        if (mNetManager.isNetWorkRoaming()) {
            //漫游
            txt=mContext.getResources().getString(R.string.txt_yes);
        }else{
            //非漫游
            txt=mContext.getResources().getString(R.string.txt_no);
        }
        mINetInfoPresenter.ShowRoam(txt);
        //网络类型
        mINetInfoPresenter.ShowNetType(mNetManager.getNetType());
        //网络状态
        mNetManager.getNetState(new INetStateCallBack() {
            @Override
            public void netStateCallBack(int state) {
                // TODO Auto-generated method stub
                if(state == 0){
                    mINetInfoPresenter.ShowNetState(mContext.getString(R.string.net_txt_netno));
                }else if(state == 1){
                    mINetInfoPresenter.ShowNetState(mContext.getString(R.string.net_txt_netok));
                }
            }
        });
        //IP
        mINetInfoPresenter.ShowIPAddress(mNetManager.getLocalIpAddress());
    }
    @Override
    public void startPhoneListener() {
        mNetManager.setPhoneListener();
    }

    @Override
    public void stopPhoneListener() {
        mNetManager.stopPhoneListener();
    }

    @Override
    public void getSignal(int signal) {
        mINetInfoPresenter.ShowSignal(signal);
    }
}
