package com.uninew.settings.presenter;

import com.uninew.settings.interfaces.IVoiceModel;
import com.uninew.settings.interfaces.IVoicePresenter;
import com.uninew.settings.interfaces.IVoiceView;
import com.uninew.settings.model.VoiceModel;

/**
 * Created by Administrator on 2017/9/12.
 */

public class VoicePresenter implements IVoicePresenter {

    private IVoiceView mIVoiceView;
    private IVoiceModel mIVoiceModel;
    public VoicePresenter(IVoiceView mVoiceView) {
        this.mIVoiceView = mVoiceView;
        this.mIVoiceModel = new VoiceModel();
    }

    @Override
    public void setTochVoice(boolean mode) {

    }

    @Override
    public void addVoice() {

    }

    @Override
    public void subVoice() {

    }

    @Override
    public void setVoice(int voice) {

    }

    @Override
    public void ShowTochVoice(boolean mode) {
        mIVoiceView.ShowTochVoice(mode);
    }

    @Override
    public void ShowCurrentVoice(int voice) {
        mIVoiceView.ShowCurrentVoice(voice);
    }
}
