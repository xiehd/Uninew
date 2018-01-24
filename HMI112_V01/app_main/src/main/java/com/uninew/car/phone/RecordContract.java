package com.uninew.car.phone;

import android.support.annotation.NonNull;

import com.uninew.car.base.BasePresenter;
import com.uninew.car.base.BaseView;
import com.uninew.car.db.dialler.Dialer;

import java.util.List;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public interface RecordContract {

    interface View extends BaseView<Presenter>{
        void showDailers(List<Dialer> dialers);
    }

    interface Presenter extends BasePresenter{
        void delectDailer(int id);
        void call(@NonNull String phone);
    }
}
