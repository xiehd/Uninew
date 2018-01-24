package com.uninew.mangaement.model;

import android.content.Context;

import com.uninew.car.db.settings.BaseLocalDataSource;
import com.uninew.car.db.settings.BaseLocalSource;
import com.uninew.mangaement.interfaces.IBaseSettingModel;
import com.uninew.mangaement.presenter.BasePresenter;

/**
 * Created by Administrator on 2017/11/7.
 */

public class BaseModel implements IBaseSettingModel {
    private BasePresenter mBasePresenter;
    private Context mContext;
    private BaseLocalSource db;


    public BaseModel(BasePresenter mBasePresenter, Context mContext) {
        this.mBasePresenter = mBasePresenter;
        this.mContext = mContext;
        db = BaseLocalDataSource.getInstance(mContext);

        init();
    }

    private void init() {
        if (db != null) {
            db.getPlateNumber(new BaseLocalSource.GetBaseSettingCallBack() {
                @Override
                public void onDBBaseDataLoaded(String s) {
                    mBasePresenter.ShowCarNumber(s);
                }

                @Override
                public void onDataNotAailable() {

                }
            });

            db.getTerminalNumber(new BaseLocalSource.GetBaseSettingCallBack() {
                @Override
                public void onDBBaseDataLoaded(String s) {
                    mBasePresenter.ShowTerminal(s);
                }

                @Override
                public void onDataNotAailable() {

                }

            });

            db.getCompanyNumber(new BaseLocalSource.GetBaseSettingCallBack() {
                @Override
                public void onDBBaseDataLoaded(String s) {
                    mBasePresenter.ShowCompanyName(s);
                }

                @Override
                public void onDataNotAailable() {

                }
            });

            db.getDvrSerialNumber(new BaseLocalSource.GetBaseSettingCallBack() {
                @Override
                public void onDBBaseDataLoaded(String s) {
                    mBasePresenter.ShowDvrsenNumber(s);
                }

                @Override
                public void onDataNotAailable() {

                }
            });
            db.getOutTimeExite(new BaseLocalSource.GetBaseSettingCallBack() {
                @Override
                public void onDBBaseDataLoaded(String s) {

                    mBasePresenter.ShowOutTime(s);
                }

                @Override
                public void onDataNotAailable() {

                }
            });
            db.getPrintSensitivity(new BaseLocalSource.GetBaseSettingCallBack() {
                @Override
                public void onDBBaseDataLoaded(String s) {
                    mBasePresenter.ShowPinter(s);
                }

                @Override
                public void onDataNotAailable() {

                }
            });
            db.getPrerecordTime(new BaseLocalSource.GetBaseSettingCallBack() {
                @Override
                public void onDBBaseDataLoaded(String s) {
                    mBasePresenter.ShowPretime(s);
                }

                @Override
                public void onDataNotAailable() {

                }
            });
            db.getDelayTime(new BaseLocalSource.GetBaseSettingCallBack() {
                @Override
                public void onDBBaseDataLoaded(String s) {
                    mBasePresenter.ShowDelaytime(s);
                }

                @Override
                public void onDataNotAailable() {

                }
            });
        }
    }

    @Override
    public void SaveInitData(String carNumber, String terminal, String companyName, String videoNumber,
                             String timeOuet, String pinter, String Pretime, String delaytime) {
        if (db != null) {
            db.setPlateNumber(carNumber);
            db.setTerminalNumber(terminal);
            db.setCompanyNumber(companyName);
            db.setDvrSerialNumber(videoNumber);
            db.setOutTimeExite(timeOuet);
            db.setPrintSensitivity(pinter);
            db.setPrerecordTime(Pretime);
            db.setDelayTime(delaytime);
        }
    }

    @Override
    public void SetRegister(String terminal) {

    }

    @Override
    public void Setdefault() {
        if (db != null) {
            db.restoringDefault();
            init();
        }
    }
}
