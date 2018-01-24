package com.uninew.net.JT905.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * SharedPreferences帮助类
 * 
 * @author Administrator
 * 
 */
public class SpHelper {

	private boolean D=true;
	private static final String TAG="SpHelper";
	private static SharedPreferences preferences;
	private static String SP_NAME = "settings";
	private Context context;

	public SpHelper(Context context) {
		if (preferences == null) {
			getSharedPreferences(context);
		}
		this.context = context;
	}
	
	/**
	 * 对象初始化
	 * 
	 * @param context
	 * @return
	 */
	public SharedPreferences getSharedPreferences(Context context) {
		if (preferences == null) {
			preferences = context.getSharedPreferences(SP_NAME,
					Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE);
		}
		return preferences;
	}

	/**
	 * 设置参数值
	 * 
	 * @param key
	 * @param Value
	 *            字符串
	 */
	public boolean setParameter(String key, String Value) {
		boolean result;
		if (preferences != null) {
			result = preferences.edit().putString(key, Value).commit();
		} else {
			result = getSharedPreferences(context).edit().putString(key, Value).commit();
		}
		return result;
	}

	/**
	 * 设置参数值
	 * 
	 * @param key
	 * @param Value
	 *            布尔值
	 */
	public boolean setParameter(String key, boolean Value) {
		boolean result;
		if (preferences != null) {
			result = preferences.edit().putBoolean(key, Value).commit();
		} else {
			result = getSharedPreferences(context).edit().putBoolean(key, Value).commit();
		}
		return result;
	}

	/**
	 * 设置参数值
	 * 
	 * @param key
	 * @param Value
	 *            整型
	 */
	public boolean setParameter(String key, int Value) {
		boolean result;
		if (preferences != null) {
			result = preferences.edit().putInt(key, Value).commit();
		} else {
			result = getSharedPreferences(context).edit().putInt(key, Value).commit();
		}
		return result;
	}

	/**
	 * 设置参数值
	 * 
	 * @param maps
	 *            键值对
	 */
	public boolean setParameter(Map<String, String> maps) {
		boolean result = false;
		for (String key : maps.keySet()) {
			if (preferences != null) {
				result = preferences.edit().putString(key, maps.get(key)).commit();
			} else {
				result = getSharedPreferences(context).edit().putString(key, maps.get(key)).commit();
			}
			if (result == false) {
				return false;
			}
		}
		return result;
	}

	/**
	 * 获取参数值
	 * 
	 * @param key
	 * @return
	 */
	public String getParameter(String key) {
		if (preferences != null) {
			return preferences.getString(key, null);
		} else {
			return getSharedPreferences(context).getString(key, null);
		}
	}

	/**
	 * 获取参数值
	 * 
	 * @param key
	 * @return
	 */
	public int getIntParameter(String key) {
		if (preferences != null) {
			return preferences.getInt(key, -1);
		} else {
			return getSharedPreferences(context).getInt(key, -1);
		}
	}

	/**
	 * 获取参数值
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getMapParameter() {
		if (preferences != null) {
			return (Map<String, String>) preferences.getAll();
		} else {
			return (Map<String, String>) getSharedPreferences(context).getAll();
		}
	}
	
}
