package com.uninew.net.JT905.comm.common;

import android.content.Context;
import android.content.Intent;

public class BroadCastTool {

	private static final String TAG = "BroadCastTool";
	private Context mContext;

	public BroadCastTool(Context mContext) {
		super();
		this.mContext = mContext;
	}

	/**
	 * 发送通讯连接状态广播
	 * 
	 * @param state
	 *            0：未连接 1：未注册 2：正常 3：未鉴权
	 */
	public void sendStateNotify(int state) {
		Intent intent = new Intent();
		intent.setAction(ActionDefine.ServerLinkState);
		intent.putExtra("State", state);
		mContext.sendBroadcast(intent);
	}
	
	/**
	 * 发送定位状态广播（发送至SystemUI）
	 * 
	 * @param state
	 *            0：未定位 1：已定位
	 */
	public void sendLocateStateNotify(int state) {
		Intent intent = new Intent();
		intent.setAction(ActionDefine.LocationState);
		intent.putExtra("State", state);
		mContext.sendBroadcast(intent);
	}
}
