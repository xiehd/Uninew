package com.uninew.car.db.dialler;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public final class DialerKey {
    private DialerKey() {

    }

    public static abstract class DialerStateKey {
        /*未接电话*/
        public static final int MISSEDE_CALLS = 0;
        /*正常接电话*/
        public static final int NORMAL_CALLS = 1;
        /*拨出电话*/
        public static final int DIALOUT_CALLS = 2;
    }
}
