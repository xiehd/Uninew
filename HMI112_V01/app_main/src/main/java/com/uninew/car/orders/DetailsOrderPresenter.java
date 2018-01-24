package com.uninew.car.orders;

import android.content.Context;

import com.uninew.car.db.order.Order;
import com.uninew.net.JT905.comm.client.ClientReceiveManage;
import com.uninew.net.JT905.comm.client.ClientSendManage;
import com.uninew.net.JT905.comm.client.IClientReceiveManage;
import com.uninew.net.JT905.comm.client.IClientSendManage;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class DetailsOrderPresenter implements DetailsOrderContrat.Presenter {

    private DetailsOrderContrat.View mView;
    private Context mContext;
    //    private IClientReceiveManage receiveManage;
    private IClientSendManage sendManage;
    private OrderModel orderModel;

    public DetailsOrderPresenter(DetailsOrderContrat.View view, Context context) {
        this.mContext = context;
        this.mView = view;
        mView.setPresenter(this);
//        receiveManage = new ClientReceiveManage(mContext);
        sendManage = new ClientSendManage(mContext);
        orderModel = new OrderModel(mContext);
    }

    @Override
    public void start() {
        orderModel.initSendManage(sendManage);
    }

    @Override
    public void stop() {

    }

    @Override
    public void setState(int state) {
        mView.showState(state);
    }

    @Override
    public void setId(String id) {
        mView.showId(id);
    }

    @Override
    public void setTime(String time) {
        mView.showTime(time);
    }

    @Override
    public void setCallTime(String time) {
        mView.showCallTime(time);
    }

    @Override
    public void setPhone(String phone) {
        mView.showPhone(phone);
    }

    @Override
    public void setServiceCharge(double i) {
        mView.showServiceCharge(i);
    }

    @Override
    public void setDetails(String details) {
        mView.showDetails(details);
    }

    @Override
    public void onBack() {

    }

    @Override
    public void onCancel(Order order) {
        orderModel.showCancelOrder(order.getBusinessId());
    }

    @Override
    public void onFinish(Order order) {
        orderModel.finishOrder(order.getBusinessId());
    }
}
