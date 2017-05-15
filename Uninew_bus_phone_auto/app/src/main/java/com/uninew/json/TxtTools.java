package com.uninew.json;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * txt文件操作类
 * 
 * @author Administrator
 * 
 */
public class TxtTools {
	private static final String TAG = "TxtTools";

	public String readTxtFile(String filePath) {
		try {
			File file = new File(JsonFileContants.FILE_LINE + filePath);
			String encoding = "GBK";
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				StringBuilder buffer = new StringBuilder();
				while ((lineTxt = bufferedReader.readLine()) != null) {
					buffer.append(lineTxt);
				}
				read.close();
				// LogTool.logD(TAG, buffer.toString());
				return buffer.toString();
			} else {
				// LogTool.logE(TAG, filePath+" 文件找不到!!!");
				Log.e("Test", "文件找不到!!!" + filePath);
			}
		} catch (Exception e) {
			Log.e("Test", "读取文件内容出错!!!");
		}
		return null;

	}

	public boolean writerTxtFile(String filePath, String content) {
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				OutputStreamWriter writer = new OutputStreamWriter(
						new FileOutputStream(file), encoding);// 考虑到编码格式
				BufferedWriter bufferedWriter = new BufferedWriter(writer);
				bufferedWriter.write(content);
				bufferedWriter.flush();
				writer.close();
				return true;
			} else {
				// LogTool.logE(TAG, filePath+"，找不到指定的文件!!!");
				file.createNewFile();
				OutputStreamWriter writer = new OutputStreamWriter(
						new FileOutputStream(file), encoding);
				BufferedWriter bufferedWriter = new BufferedWriter(writer);
				bufferedWriter.write(content);
				bufferedWriter.flush();
				writer.close();
			}
		} catch (Exception e) {
			// LogTool.logE(TAG, "写入文件内容出错!!!");
		}
		return false;
	}

	public boolean writeFile(String filePath, String content) {
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(filePath, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void writeGpsTrackPoint(double latitude, double longitude,
			int speed, int direction, String routeName) {

		String content = latitude + "\t" + longitude + "\t" + direction + "\t"
				+ speed + "\t" + System.currentTimeMillis() + "\r\n";

		File filedir = new File(JsonFileContants.FILE_CONNCTLINE);
		if(!filedir.exists()){
			filedir.mkdir();
		}
		writeTrackFile(JsonFileContants.FILE_CONNCTLINE + routeName
				+ "gps.txt", content);

	}

	public void writeTrackFile(String filePath, String content) {

		File file = new File(filePath);

		try {
			if (!file.isFile() || !file.exists()) {
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(file,true);
			fileWriter.write(content);
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
