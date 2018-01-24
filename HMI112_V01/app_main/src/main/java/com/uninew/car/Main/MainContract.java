package com.uninew.car.Main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.uninew.car.base.BasePresenter;
import com.uninew.car.base.BaseView;

/**
 * Created by Administrator on 2017/9/1 0001.
 */

public interface MainContract {

    int CAR_STATE_EMPTY = 0;
    int CAR_STATE_HEAVY = 1;
    int CAR_STATE_PAUSE = 2;

    interface View extends BaseView<Presenter> {
        /**
         * 显示单价
         * @param price
         */
        void showPrice(float price);

        /**
         * 显示里程
         * @param mileage
         */
        void showMileage(float mileage);

        /**
         * 显示时间
         * @param time
         */
        void showTiming(String time);

        /**
         * 显示总额度
         * @param total
         */
        void showTotal(float total);

        /**
         * 显示司机姓名
         * @param name
         */
        void showDriverName(String name);

        /**
         * 司机头像
         * @param bitmap
         */
        void showHeadPortrait(Bitmap bitmap);

        /**
         * 监督电话
         * @param phone
         */
        void showControlPhone(String phone);

        /**
         * 资格证件号
         * @param certificate
         */
        void showCertificate(String certificate);

        /**
         * 服务评分
         * @param evaluation
         */
        void showEvaluation(float evaluation);

        /**
         * 显示车辆状态
         * @param state
         */
        void showCarState(int state);

    }

    interface Presenter extends BasePresenter {
        /**
         * 切换车辆状态
         * @param state
         */
        void setCarState(int state);

        /**
         * 切换界面
         * @param action
         * @param context
         */
        void changeActivityView(String action, Context context);

        /**
         * 却换导航界面
         */
        void changeNavigation(Context context);

        void onDestroy();
    }
}
