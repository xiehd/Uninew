package com.uninew.settings.presenter;

import com.uninew.settings.interfaces.IResetModel;
import com.uninew.settings.interfaces.IResetPresenter;
import com.uninew.settings.model.ResetModel;

/**
 * Created by Administrator on 2017/9/12.
 */

public class ResetPresenter implements IResetPresenter{

    private IResetModel mIResetModel;

    public ResetPresenter() {
        mIResetModel = new ResetModel();
    }

    @Override
    public void RefactorySettings() {

    }

    @Override
    public void ReSystemRestart() {

    }
}
