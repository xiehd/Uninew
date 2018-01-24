package com.uninew.car.phone;

import android.graphics.Bitmap;
import android.telephony.PhoneStateListener;

import com.uninew.car.base.BasePresenter;
import com.uninew.car.base.BaseView;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public interface CallContract {

    interface View extends BaseView<Presenter> {
        /**
         * 显示联系人头像
         * @param bitmap
         */
        void showAvatar(Bitmap bitmap);

        /**
         * 显示联系人名称
         * @param name
         */
        void showName(String name);

        /**
         * 显示通话的电话号码
         * @param phone
         */
        void showPhone(String phone);

        /**
         * 通话状态
         * @param state 0：拨号中 1：通话中 2：呼入
         */
        void showCallState(int state);

        /**
         * 通话时长
         * @param time 秒为单位
         */
        void showCallDuration(int time);

        /**
         * 显示归属地
         * @param attribution
         */
        void showAttribution(String attribution);

        /**
         * 挂断
         */
        void endCall();
    }

    interface Presenter extends BasePresenter {
        /**
         * 挂电话
         */
        void hangUp();

        /**
         * 接听电话
         */
        void answer();

        /**
         * 拨号通话
         * @param number
         */
        void callNumber(String number);


        /**
         * 设置通话状态
         * @param state 0：拨号中 1：通话中 2：呼入
         */
        void setCallState(int state);


        /**
         * 设置通话的电话号码
         * @param phone
         */
        void setPhone(String phone);
    }
}
