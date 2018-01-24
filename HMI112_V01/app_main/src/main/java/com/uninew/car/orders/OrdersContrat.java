package com.uninew.car.orders;

import com.uninew.car.base.BasePresenter;
import com.uninew.car.base.BaseView;
import com.uninew.car.db.order.Order;

import java.util.List;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public interface OrdersContrat {

    interface View extends BaseView<Presenter> {
        void showOrders(List<Order> orders);

        void showDetailedOrder(Order order);

        void answerFinish();
    }

    interface Presenter extends BasePresenter {

        void changeShowOrderState(int... state);

        void changeOrderState(int workId,int state,String finishTime);

        void confirmFinish(int position);

        void cancelOrder(int position);

        void showDetailedOrder(Order order);
    }
}
