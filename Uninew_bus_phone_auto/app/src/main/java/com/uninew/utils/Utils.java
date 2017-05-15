package com.uninew.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.DisplayMetrics;
import android.util.Log;

import com.uninew.json.JsonFileContants;
import com.uninew.util.auto.TrackPointStruct;

public class Utils {

	private static String AGT = "Utils";

	/**
	 * 读取模拟GPS轨迹路径
	 * 
	 * @param filePath
	 * @return 返回轨迹点数目
	 */
	public static int readGPSFile(String filePath,
			ArrayList<TrackPointStruct> pLocationClassList) {
		try {
			pLocationClassList.clear();
			File file = new File(JsonFileContants.FILE_POINT
					+ filePath);
			String encoding = "GBK";
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read, 1024);
				String lineTxt = null;
				StringBuilder buffer = new StringBuilder();
				while ((lineTxt = bufferedReader.readLine()) != null) {
					// buffer.append(lineTxt);
					String[] array = lineTxt.split("\t");
					if (array.length >= 5) {
						pLocationClassList.add(new TrackPointStruct(lineTxt));
					} else {
						Log.e(AGT, "轨迹文件格式不对");
					}
				}
				read.close();
				return pLocationClassList.size();
			} else {
				Log.e(AGT, "文件找不到!!!" + filePath);
			}
		} catch (Exception e) {
			Log.e(AGT,
					"读取文件内容出错!!!" + filePath + e.getMessage() + ","
							+ e.toString());
		}
		return -1;

	}

	/**
	 * 读取所有轨迹点文件名
	 * 
	 * @param list
	 * @return 返回轨迹点文件个数
	 */
	public static int getPointFiles(ArrayList<String> list) {
		list.clear();
		File file = new File(JsonFileContants.FILE_POINT);
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File f : files) {
					if (f.isFile()) {
						list.add(f.getName().toString());
					}
				}
				return files.length;
			}
		} else {
			Log.e(AGT, "轨迹文件不存在");
		}
		return -1;
	}

	/**
	 * 读取所有线路文件名
	 * 
	 * @param list
	 * @return 返回下路总数
	 */
	public static int getLineFiles(ArrayList<String> list) {
		list.clear();
		File file = new File(JsonFileContants.FILE_LINE);
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File f : files) {
					if (f.isFile() && f.getName().endsWith("上行.txt")) {
						list.add(f.getName().substring(0,
								f.getName().length() - 6));
					}
				}
				return files.length;
			}
		} else {
			Log.e(AGT, "线路文件不存在");
		}

		return -1;

	}

	// 读取当前轨迹路径
	public static String readGPSFile(String path,
			List<TrackPointStruct> pLocationClassList) {
		try {
			pLocationClassList.clear();
			File file = new File(JsonFileContants.FILE_POINT + path);
			String encoding = "GBK";
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read, 1024);
				String lineTxt = null;
				StringBuilder buffer = new StringBuilder();
				while ((lineTxt = bufferedReader.readLine()) != null) {
					// buffer.append(lineTxt);
					String[] array = lineTxt.split("\t");
					if (array.length >= 5) {
						pLocationClassList.add(new TrackPointStruct(lineTxt));
					} else {
						Log.e("Utils", "轨迹文件格式不对");
					}
				}
				read.close();
				return buffer.toString();
			} else {
				// LogTool.logE(TAG, filePath+" 文件找不到!!!");
				Log.e("Utils", "轨迹文件找不到!!!");
			}
		} catch (Exception e) {
			Log.e("Utils", "轨迹文件内容出错!!!" + e.getMessage() + "," + e.toString());
		}
		return null;

	}
//设置侧滑宽度
	public static void setDrawerLeftEdgeSize(Activity activity,
			DrawerLayout drawerLayout, float displayWidthPercentage) {
		if (activity == null || drawerLayout == null)
			return;
		try {
			// find ViewDragHelper and set it accessible
			Field leftDraggerField = drawerLayout.getClass().getDeclaredField(
					"mLeftDragger");
			leftDraggerField.setAccessible(true);
			ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField
					.get(drawerLayout);
			// find edgesize and set is accessible
			Field edgeSizeField = leftDragger.getClass().getDeclaredField(
					"mEdgeSize");
			edgeSizeField.setAccessible(true);
			int edgeSize = edgeSizeField.getInt(leftDragger);
			// set new edgesize
			// Point displaySize = new Point();
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			edgeSizeField.setInt(leftDragger, Math.max(edgeSize,
					(int) (dm.widthPixels * displayWidthPercentage)));
		} catch (NoSuchFieldException e) {
			// ignore
		} catch (IllegalArgumentException e) {
			// ignore
		} catch (IllegalAccessException e) {
			// ignore
		}
	}

	public static double getScale(int i,double value){
		BigDecimal bigDecimal = new BigDecimal(value);
		return bigDecimal.setScale(i,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
