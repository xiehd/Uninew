package com.uninew.car.db.order;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public final class OrderKey {

    private OrderKey() {
    }

    public static abstract class OrderStateKey {
        /*其他*/
        public static final int OTHER_STATE = 5;
        /*已中标*/
        public static final int BADE_STATE = 1;
        /*已完成*/
        public static final int FINISH_STATE = 3;
        /*乘客取消*/
        public static final int PASSENGER_CANCEL_STATE = 2;
        /*司机取消*/
        public static final int DRIVER_CANCEL_STATE = 4;
        /*抢单失败*/
        public static final int FAILURE_STATE = 0;
    }

    public static abstract class OrderTypeKey {
        /*临时召车*/
        public static final int TEMPORARY_TYPE = 0;
        /*预约召车*/
        public static final int BESPEAK_TYPE = 1;
        /*车辆指派*/
        public static final int ASSIGN_TYPE = 2;
    }
}
