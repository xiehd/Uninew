package com.uninew.car.revenue;

import com.uninew.car.base.BasePresenter;
import com.uninew.car.base.BaseView;

/**
 * Created by Administrator on 2017/9/18 0018.
 */

public interface DetailsRevenueContrat {

    interface View extends BaseView<Presenter> {
        void showBoardingTime(String time);

        void showFinishTime(String time);

        void showDriningTime(String time);

        void showDrivingMileage(String Mileage);

        void showBareMileage(String Mileage);

        void showtRavelType(String type);

        void showWaitTime(String time);

        void showSurcharge(String surcharge);

        void showTrips(int trips);
    }

    interface Presenter extends BasePresenter {
        void setwBoardingTime(String time);

        void setFinishTime(String time);

        void setDriningTime(String time);

        void setDrivingMileage(double Mileage);

        void setBareMileage(double Mileage);

        void setRavelType(int type);

        void setTrips(int trips);

        void setWaitTime(String time);

        void setSurcharge(int surcharge);
    }
}
