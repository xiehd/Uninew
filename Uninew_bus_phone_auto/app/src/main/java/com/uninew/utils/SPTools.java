package com.uninew.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;

public class SPTools {
	
	private static SharedPreferences preferences;

	public static final String ROUTE_NAME_KEY = "route_name";

	public SPTools(Context context) {
		// 2．获得SharedPreferences
		preferences = context.getSharedPreferences("ftp_text",
				Context.MODE_WORLD_READABLE |Context.MODE_WORLD_WRITEABLE);
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
