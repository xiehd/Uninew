package com.uninew.location.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.uninew.location.GpsInfo;
import com.uninew.location.GpsInfoManage;
import com.uninew.location.IGpsInfoListener;
import com.uninew.location.IGpsInfoManage;
import com.uninew.location.R;

public class MainActivity extends Activity {

	private IGpsInfoManage mGpsInfoManage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		startService(new Intent(this, LocationService.class));
		
		mGpsInfoManage=new GpsInfoManage();
		mGpsInfoManage.registerGpsInfoListener(0x01, this, mGpsInfoListener);
		
	}
	
	private IGpsInfoListener mGpsInfoListener=new IGpsInfoListener() {
		
		@Override
		public void gpsInfo(GpsInfo gpsInfo) {
			// TODO Auto-generated method stub
			System.err.println(gpsInfo.toString());
		}
	};
}
