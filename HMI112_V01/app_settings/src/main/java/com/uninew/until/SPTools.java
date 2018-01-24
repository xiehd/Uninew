package com.uninew.until;

import android.content.Context;
import android.content.SharedPreferences;

public class SPTools {
	
	private static SharedPreferences preferences;

	public static final String PARAMTER_HOST_KEY = "paramter_host";//域名

	public SPTools(Context context) {
		// 2．获得SharedPreferences
		preferences = context.getSharedPreferences("settings_data",
				Context.MODE_PRIVATE);
	}
	//String
	public void putSharedString(String key,String value){
		preferences.edit().putString(key, value).commit();
	}
	
	public String getSharedString(String key){
		return preferences.getString(key, "-1");
	}

	public int getSharedInt(String key){
		return preferences.getInt(key, 0);
	}

	//String
	public void putSharedInt(String key,int value){
		preferences.edit().putInt(key, value).commit();
	}

	//boolean
	public void putSharBoolean(String key,Boolean value){
		preferences.edit().putBoolean(key, value).commit();
	}
	public boolean getSharBoolean(String key){
		return preferences.getBoolean(key, false);
	}
}
