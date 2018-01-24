package com.uninew.settings.presenter;

import com.uninew.settings.interfaces.ITimeModel;
import com.uninew.settings.interfaces.ITimePresenter;
import com.uninew.settings.interfaces.ITimeView;
import com.uninew.settings.model.TimeModel;

/**
 * Created by Administrator on 2017/9/12.
 */

public class TimePresenter implements ITimePresenter {

    private ITimeView mITimeView;
    private ITimeModel mITimeModel;

    public TimePresenter(ITimeView mITimeView) {
        this.mITimeView = mITimeView;
        this.mITimeModel = new TimeModel();
    }

    @Override
    public void setTimeFormat(String dayformat, String timeformat, String timezone) {

    }

    @Override
    public void setCurrentTime(String time) {

    }

    @Override
    public void setTiming(int state) {

    }

    @Override
    public void ShowTimeFormat(String dayformat, String timeformat, String timezone) {
        mITimeView.ShowTimeFormat(dayformat,timeformat,timezone);
    }

    @Override
    public void ShowCurrentTime(String time) {
        mITimeView.ShowCurrentTime(time);
    }

    @Override
    public void ShowTiming(int state) {
        mITimeView.ShowTiming(state);
    }
}
