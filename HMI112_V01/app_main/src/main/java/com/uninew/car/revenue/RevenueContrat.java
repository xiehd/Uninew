package com.uninew.car.revenue;

import com.uninew.car.base.BasePresenter;
import com.uninew.car.base.BaseView;
import com.uninew.car.db.revenue.Revenue;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public interface RevenueContrat {

    interface View extends BaseView<Presenter> {

        void showWaitTime(int waitTime);

        void setDrivingMileage(float drivingMileage);

        void showBareMileage(float bareMileage);

        void showTimes(int times);

        void showBareRate(int rate);

        void setAmount(float amount);

        void showCardAmount(float amount);

        void showDetailsRevenue(Revenue revenue);

        void showRevenues(List<Revenue> revenues);
    }

    interface Presenter extends BasePresenter {

        void setHistoryRevenue();

        void setCurrentRevenue();

        void showDetailsRevenue(Revenue revenue);
    }
}
