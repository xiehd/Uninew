package com.uninew.maintanence.presenter;

import android.content.Context;
import android.util.Log;

import com.uninew.maintanence.model.UpdateModel;


/***********************************************************************
 * Module: UpdatePresenter.java Author: Administrator Purpose: Defines the Class
 * UpdatePresenter
 ***********************************************************************/

public class UpdatePresenter implements IUpdatePresenter, IUpdateListener {

	private UpdateModel mUpdateModel;
	private IUpdateView mUpdateView;

	public UpdatePresenter(Context mContext, IUpdateView mUpdateView) {
		this.mUpdateView = mUpdateView;
		this.mUpdateModel = new UpdateModel(mContext, this);
	}

	@Override
	public void updateApk(String apkAbsolutePath) {
		mUpdateModel.updateApk(apkAbsolutePath);
	}

	@Override
	public void updateOS(String osAbsolutePath, IResultCallBack resultCallBack) {
		mUpdateModel.updateOS(osAbsolutePath, resultCallBack);
	}

	@Override
	public void updateApkRequest(IUpdateResultCallBack mCallBack) {
		mUpdateModel.updateApkRequest(mCallBack);
	}

	@Override
	public void updateOsRequest(IUpdateResultCallBack mCallBack) {
		mUpdateModel.updateOsRequest(mCallBack);
	}

	@Override
	public void updateMcuRequest(IUpdateResultCallBack mCallBack) {
		mUpdateModel.updateMcuRequest(mCallBack);
	}

	@Override
	public void updateMcu(String mcuAbsolutePath, IResultCallBack resultCallBack) {
		mUpdateModel.updateMcu(mcuAbsolutePath, resultCallBack);
	}

	@Override
	public boolean isSdCardEnable() {
		return mUpdateModel.isSdCardEnable();
	}

	@Override
	public void registerListener() {
		mUpdateModel.registerListener();
	}

	@Override
	public void unRegisterListener() {
		mUpdateModel.unRegisterListener();
	}

	// --------------------------监听方法----------------------------------------

	@Override
	public void setSDChange(boolean isEnable) {
		mUpdateView.SDChange(isEnable);
	}

	@Override
	public void onApkInsatll(boolean isSuccess) {
		mUpdateView.onApkInsatll(isSuccess);
	}

	@Override
	public void ShowAppVersion(String version) {
        try{
            mUpdateView.ShowAppVersion(version);
        }catch (Exception e){
            Log.e("---erorr---",e.getMessage()+","+e.toString());
        }
	}

	@Override
	public void onUpdateState(int state, String apk, boolean openState, boolean setLauncherState) {
		mUpdateView.onUpdateState(state,apk,openState,setLauncherState);
	}

	@Override
	public void ShowOSVersion(String version) {
		mUpdateView.ShowOSVersion(version);
	}

	@Override
	public void ShowMcuVersion(String version) {
		mUpdateView.ShowMcuVersion(version);
	}

	@Override
	public void ShowDvrVersion(String version) {
		mUpdateView.ShowDvrVersion(version);
	}

	@Override
	public void ShowCarSrceenVersion(String version) {
		mUpdateView.ShowCarSrceenVersion(version);
	}

	@Override
	public void ShowMapVersion(String version) {
		mUpdateView.ShowMapVersion(version);
	}

	@Override
	public void ShowGaoDeVersion(String version) {
		mUpdateView.ShowGaoDeVersion(version);
	}

}