package com.uninew.car.evaluation;

import com.uninew.car.base.BasePresenter;
import com.uninew.car.base.BaseView;
import com.uninew.net.JT905.bean.T_OperationDataReport;

/**
 * Created by Administrator on 2017/9/25 0025.
 */

public interface EvaluationContract {

    interface View extends BaseView<Presenter> {
        /*显示金额*/
        void showAmount(String amount);

        /*显示时间*/
        void showTime(String time);

        /*显示里程*/
        void showMileage(String mileage);

        /*显示附加费*/
        void showSurcharge(String surcharge);

        /*显示电召费*/
        void showOrder(String order);
    }

    interface Presenter extends BasePresenter {
        /*评价星级*/
        void evaluateStar(String time ,int star);

        /*显示金额*/
        void setAmount(double amount);

        /*显示时间*/
        void setTime(String time);

        void sendOperationDataReport(T_OperationDataReport operationDataReport,int extended);

        /*显示里程*/
        void setMileage(double mileage);

        /*显示附加费*/
        void setSurcharge(double surcharge);

        /*显示电召费*/
        void setOrder(float order);
    }
}
