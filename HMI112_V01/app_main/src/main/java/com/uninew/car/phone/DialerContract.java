package com.uninew.car.phone;

import com.uninew.car.base.BasePresenter;
import com.uninew.car.base.BaseView;

/**
 * Created by Administrator on 2017/9/2 0002.
 */

public interface DialerContract {

    interface View extends BaseView<Presenter>{
        void showPhone(String number);
    }

    interface Presenter extends BasePresenter{
        void cleared();
        void delete(int index);
        void call(String phone);
        void enterPhone(int index,String number);
        void init(String number);
    }
}
