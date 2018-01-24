package com.uninew.settings.presenter;

import com.uninew.settings.interfaces.IHostModel;
import com.uninew.settings.interfaces.IHostPresenter;
import com.uninew.settings.interfaces.IHostView;

/**
 * Created by Administrator on 2017/9/12.
 */

public class HostPresentet implements IHostPresenter {

    private IHostView mHostView;
    private IHostModel mHostModel;

    public HostPresentet(IHostView mHostView) {
        this.mHostView = mHostView;
    }

    @Override
    public void SetHostOFF(boolean state, String name, String passworld) {
    }

    @Override
    public void ShowHostOFF(boolean state, String name, String passworld) {
            mHostView.ShowHostOFF(state,name,passworld);
    }

    @Override
    public void ShowHostConnectNuber(int number) {
        mHostView.ShowHostConnectNuber(number);
    }
}
