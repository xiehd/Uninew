package com.uninew.car.orders;

import com.uninew.car.base.BasePresenter;
import com.uninew.car.base.BaseView;
import com.uninew.car.db.order.Order;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public interface DetailsOrderContrat {

    interface View extends BaseView<Presenter> {
        void showState(int state);

        void showId(String id);

        void showTime(String time);

        void showCallTime(String time);

        void showPhone(String phone);

        void showServiceCharge(double i);

        void showDetails(String details);

    }

    interface Presenter extends BasePresenter {
        void setState(int state);

        void setId(String id);

        void setTime(String time);

        void setCallTime(String time);

        void setPhone(String phone);

        void setServiceCharge(double i);

        void setDetails(String details);

        void onBack();

        void onCancel(Order order);

        void onFinish(Order order);
    }
}
