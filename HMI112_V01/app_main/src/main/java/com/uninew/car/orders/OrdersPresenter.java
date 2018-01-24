package com.uninew.car.orders;

import android.content.Context;

import com.uninew.car.R;
import com.uninew.car.db.order.Order;
import com.uninew.car.db.order.OrderLocalDataSource;
import com.uninew.car.db.order.OrderLocalSource;
import com.uninew.car.db.order.OrderKey;

import java.util.List;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class OrdersPresenter implements OrdersContrat.Presenter, OrderLocalSource.LoadOrdersCallback {

    private OrdersContrat.View mView;
    private Context mContext;
    private OrderLocalSource mDBOrders;
    private int[] mState;

    public OrdersPresenter(OrdersContrat.View view, Context context) {
        this.mContext = context;
        this.mView = view;
        mDBOrders = OrderLocalDataSource.getInstance(mContext);
        mView.setPresenter(this);
        Order order = new Order();
        order.setBusinessType(0);
        order.setOrderState(1);
        order.setBusinessId(0);
        order.setServiceCharge(0);
        order.setPassengerPhoneNumber("15876999260");
        order.setNeedTime("2017-9-13 18:10:00");
        order.setReceiveTime("2017-9-13 18:10:00");
        order.setBusinessDescription(context.getString(R.string.order_default_address));
        Order order1 = new Order();
        order1.setBusinessType(1);
        order1.setOrderState(2);
        order1.setBusinessId(1);
        order1.setServiceCharge(0);
        order1.setPassengerPhoneNumber("15876999260");
        order1.setNeedTime("2017-9-13 18:10:00");
        order1.setReceiveTime("2017-9-13 18:10:00");
        order1.setBusinessDescription(context.getString(R.string.order_default_address));
        Order order2 = new Order();
        order2.setBusinessType(2);
        order2.setOrderState(3);
        order2.setBusinessId(2);
        order2.setServiceCharge(0);
        order2.setPassengerPhoneNumber("15876999260");
        order2.setNeedTime("2017-9-13 18:10:00");
        order2.setReceiveTime("2017-9-13 18:10:00");
        order2.setBusinessDescription(context.getString(R.string.order_default_address));
        Order order3 = new Order();
        order3.setBusinessType(1);
        order3.setOrderState(4);
        order3.setBusinessId(3);
        order3.setServiceCharge(0);
        order3.setPassengerPhoneNumber("15876999260");
        order3.setNeedTime("2017-9-13 18:10:00");
        order3.setReceiveTime("2017-9-13 18:10:00");
        order3.setBusinessDescription(context.getString(R.string.order_default_address));
        Order order4 = new Order();
        order4.setBusinessType(1);
        order4.setOrderState(5);
        order4.setBusinessId(4);
        order4.setServiceCharge(0);
        order4.setPassengerPhoneNumber("15876999260");
        order4.setNeedTime("2017-9-13 18:10:00");
        order4.setReceiveTime("2017-9-13 18:10:00");
        order4.setBusinessDescription(context.getString(R.string.order_default_address));
        mDBOrders.saveDBData(new Order[]{order,order1,order2,order3,order4});
    }

    @Override
    public void start() {
        mDBOrders.getOrdersByState(this, OrderKey.OrderStateKey.BADE_STATE);
    }

    @Override
    public void stop() {

    }

    @Override
    public void changeShowOrderState(int... state) {
        mDBOrders.getOrdersByState(this, state);
        mState = state;
    }

    @Override
    public void changeOrderState(int workId, int state, String finishTime) {
        mDBOrders.changeState(workId, state, finishTime);
        mDBOrders.getOrdersByState(this, mState);
    }

    @Override
    public void confirmFinish(int position) {

    }

    @Override
    public void cancelOrder(int position) {

    }

    @Override
    public void showDetailedOrder(Order order) {
        mView.showDetailedOrder(order);
    }

    @Override
    public void onDBBaseDataLoaded(List<Order> buffers) {
        mView.showOrders(buffers);
    }

    @Override
    public void onDataNotAailable() {
        mView.showOrders(null);
    }
}
