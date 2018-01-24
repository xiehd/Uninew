package com.uninew.maintanence.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.util.Log;

import com.uninew.maintanence.presenter.ISDCard;
import com.uninew.maintanence.presenter.ISDCardListener;
import com.uninew.until.SDCardUtils;

/**
 * SD卡插拔判定管理类
 * @author Administrator
 *
 */
public class SDManager implements ISDCard {

	private ISDCardListener msdCardListener;
	private Context mContext;
	
	private boolean isEnable;

	public SDManager(Context mContext, ISDCardListener msdCardListener) {
		super();
		this.msdCardListener = msdCardListener;
		this.mContext = mContext;
	}

	@Override
	public boolean isSdcardEnable() {
		// TODO Auto-generated method stub
		return  SDCardUtils.isUSBEnable(Environment.getExternalStorageDirectory().getAbsolutePath());
	}

	@Override
	public void registerListener() {
		// TODO Auto-generated method stub
		registerBroadCast();
	}

	@Override
	public void unRegisterListener() {
		// TODO Auto-generated method stub
		mContext.unregisterReceiver(mBroadcastReceiver);
	}

	
	private USBBroadCastReceiver mBroadcastReceiver;
//	private static final String usbPath = "/storage/udisk";
//	private static final String sdcardPath = "/storage/extsd";
	private static final String usbFilePath = "file:///storage/udisk";
//	private static final String sdFilePath = "file:///storage/extsd";
	private void registerBroadCast() {
		// 监听usb设备
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
		iFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		iFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
		iFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		iFilter.addDataScheme("file");
		mBroadcastReceiver = new USBBroadCastReceiver();
		mContext.registerReceiver(mBroadcastReceiver, iFilter);
	}

	public class USBBroadCastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String dataString = intent.getDataString();
			String action = intent.getAction();
			Log.d("temp", "dataString= " + dataString);
			// 0 表示sd卡
			// 1 表示usb
			int currentDevice = 0;
			if (dataString.equals(usbFilePath)) {
				currentDevice = 1;
			}
			Log.i("zzz", "currentDevice=" + currentDevice);
			if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
				isEnable = false;
			} else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
				isEnable = true;
			} else if (action.equals(Intent.ACTION_MEDIA_REMOVED)) {
				isEnable = false;
			} else if (action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
				isEnable = false;
			}
			msdCardListener.SDChange(isEnable);
		}
	}
}
