package com.uninew.mms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartBroadCastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d("xhd", "Boot complete!!!!");
		Intent mmsService = new Intent(context,McuService.class);
		context.startService(mmsService);
	}

}
