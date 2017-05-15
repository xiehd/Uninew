package com.qr.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;

public class SPTools {
	
	private static SharedPreferences preferences;

	public SPTools(Context context) {
		// 2．获得SharedPreferences
		preferences = context.getSharedPreferences("qr_text",
				Context.MODE_WORLD_READABLE |Context.MODE_WORLD_WRITEABLE);
	}
	//String
	public void putSharedString(String key,String value){
		preferences.edit().putString(key, value).commit();
	}
	
	public String getSharedString(String key){
		return preferences.getString(key, "-1");
	}
	
	//boolean
	public void putSharBoolean(String key,Boolean value){
		preferences.edit().putBoolean(key, value).commit();
	}
	public boolean getSharBoolean(String key){
		return preferences.getBoolean(key, false);
	}
}
