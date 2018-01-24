package com.uninew.mangaement.presenter;

import android.content.Context;

import com.uninew.mangaement.interfaces.IBaseSettingModel;
import com.uninew.mangaement.interfaces.IBaseSettingView;
import com.uninew.mangaement.model.BaseModel;

/**
 * Created by Administrator on 2017/11/7.
 */

public class BasePresenter implements IBaseSettingModel, IBaseSettingView {
    private IBaseSettingView mIBaseSettingView;
    private IBaseSettingModel mIBaseSettingModel;

    public BasePresenter(IBaseSettingView mIBaseSettingView, Context con) {
        this.mIBaseSettingView = mIBaseSettingView;
        mIBaseSettingModel = new BaseModel(this,con);
    }

    @Override
    public void SaveInitData(String carNumber, String terminal, String companyName, String videoNumber,
                             String timeOuet, String pinter, String Pretime, String delaytime) {

        mIBaseSettingModel.SaveInitData(carNumber,terminal,companyName,videoNumber,timeOuet,pinter,Pretime,delaytime);
    }

    @Override
    public void SetRegister(String terminal) {
        mIBaseSettingModel.SetRegister(terminal);
    }

    @Override
    public void Setdefault() {
        mIBaseSettingModel.Setdefault();
    }

    @Override
    public void ShowCarNumber(String carNumber) {
        mIBaseSettingView.ShowCarNumber(carNumber);
    }

    @Override
    public void ShowTerminal(String terminal) {
        mIBaseSettingView.ShowTerminal(terminal);
    }

    @Override
    public void ShowCompanyName(String companyName) {
        mIBaseSettingView.ShowCompanyName(companyName);
    }

    @Override
    public void ShowDvrsenNumber(String dvrNumber) {
        mIBaseSettingView.ShowDvrsenNumber(dvrNumber);
    }

    @Override
    public void ShowOutTime(String outTime) {
        mIBaseSettingView.ShowOutTime(outTime);
    }

    @Override
    public void ShowPinter(String pinter) {
        mIBaseSettingView.ShowPinter(pinter);
    }

    @Override
    public void ShowPretime(String Pretime) {
        mIBaseSettingView.ShowPretime(Pretime);
    }

    @Override
    public void ShowDelaytime(String delaytime) {
        mIBaseSettingView.ShowDelaytime(delaytime);
    }
}
