package com.uninew.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileText {

	private static String path = "/mnt/sdcard/auto.text";
	private static InputStream in = null;
	private static BufferedReader bufferedReader = null;
	private static FileWriter out = null;

	public synchronized static int writeText(String text) {
		File file = new File(path);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			if (out == null) {
				out = new FileWriter(file, true);
			}
			if(text.endsWith("进站")){
			out.write("\r\n             "+text + "\r\n");
			out.flush();
			}else{
				out.write("\r\r             "+text + "\r\n");
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	public static void close() {
		if (out != null) {
			try {
				out.close();
				out = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void deletefile(){
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
	}

}
