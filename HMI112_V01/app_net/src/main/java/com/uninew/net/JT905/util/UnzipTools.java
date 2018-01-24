package com.uninew.net.JT905.util;

import android.util.Log;

import com.file.zip.ZipEntry;
import com.file.zip.ZipFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;


public class UnzipTools {
	private ZipFile zipFile;
//	private ZipInputStream zipIn;
	private File outFile;
	private ZipEntry entry;
	private InputStream inputStream;
	private OutputStream outStream;
	private volatile static UnzipTools instance;
	private static final int maxbufCah = 1024*2;
	private IUnzipFinishCallBack mCallBack;
    private IUnzipGbkFinishCallBack mGbkCallBack;
	
	private UnzipTools(){
		
	}
	
	public void setCallBack(IUnzipFinishCallBack mCallBack) {
		this.mCallBack = mCallBack;
	}

	public void setGbkCallBack(IUnzipGbkFinishCallBack mGbkCallBack) {
		this.mGbkCallBack = mGbkCallBack;
	}


	public interface IUnzipFinishCallBack{
		/**
		 * 解压完成
		 * @param isSuccess true：成功
		 * @param path 保存地址
		 */
		void onUnzipFinish(boolean isSuccess, String path);
	}

	public interface IUnzipGbkFinishCallBack{
		/**
		 * 解压完成
		 * @param isSuccess true：成功
		 * @param path 保存地址
		 */
		void onUnzipFinish(boolean isSuccess, String path);
	}
	public static UnzipTools getInstance(){
		if(instance != null){
			
		}else{
			synchronized (UnzipTools.class) {
				if(instance == null){
					instance = new UnzipTools();
				}
			}
		}
		return instance;
	}
	
	public synchronized boolean unZipFile(File file,String outPath){
		boolean isSuccess = false;
		try {
			File file2 = new File(outPath);
			if(!file2.exists()){
				file2.mkdirs();
			}
			zipFile = new ZipFile(file,"UTF-8");
//			zipIn = new ZipInputStream(new FileInputStream(file));
			Enumeration<ZipEntry> entris = zipFile.getEntries();  
	        ZipEntry zipEntry = null;  
	        byte[] buf = new byte[maxbufCah];
	        int temp = 0;
			while (entris.hasMoreElements()) {
				zipEntry = entris.nextElement();
				Log.d("zip", zipEntry.getName());
				outFile = new File(outPath + File.separator + zipEntry.getName());
				if (zipEntry.isDirectory()) {// 当前文件为目录
					if (!outFile.exists()) {
						outFile.mkdirs();
					}
				} else {
					if (!outFile.exists()) {
						outFile.createNewFile();
					}
					inputStream = zipFile.getInputStream(zipEntry);
					outStream = new FileOutputStream(outFile);
					while ((temp = inputStream.read(buf)) != -1) {
						outStream.write(buf,0,temp);
					}
					outStream.flush();
				}
				isSuccess = true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			isSuccess = false;
		} catch (IOException e) {
			e.printStackTrace();
			isSuccess = false;
		}finally {
			try {
				if(outStream != null){
					outStream.close();
				}
				if(inputStream != null){
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			mCallBack.onUnzipFinish(isSuccess, outFile.getParentFile().getAbsolutePath());
		}
		return isSuccess;
	}

	public synchronized boolean unZipFileByGbk(File file,String outPath){
		boolean isSuccess = false;
		try {
			File file2 = new File(outPath);
			if(!file2.exists()){
				file2.mkdirs();
			}
			zipFile = new ZipFile(file,"GBK");
//			zipIn = new ZipInputStream(new FileInputStream(file));
			Enumeration<ZipEntry> entris = zipFile.getEntries();
			ZipEntry zipEntry = null;
			byte[] buf = new byte[maxbufCah];
			int temp = 0;
			while (entris.hasMoreElements()) {
				zipEntry = entris.nextElement();
				Log.d("zip", zipEntry.getName());
				outFile = new File(outPath + File.separator + zipEntry.getName());
				if (zipEntry.isDirectory()) {// 当前文件为目录
					if (!outFile.exists()) {
						outFile.mkdirs();
					}
				} else {
					if (!outFile.exists()) {
						if(!outFile.getParentFile().exists()){
							outFile.getParentFile().mkdirs();
						}
						outFile.createNewFile();
					}
					inputStream = zipFile.getInputStream(zipEntry);
					outStream = new FileOutputStream(outFile);
					while ((temp = inputStream.read(buf)) != -1) {
						outStream.write(buf,0,temp);
					}
					outStream.flush();
				}
				isSuccess = true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			isSuccess = false;
		} catch (IOException e) {
			e.printStackTrace();
			isSuccess = false;
		}finally {
			try {
				if(outStream != null){
					outStream.close();
				}
				if(inputStream != null){
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			mGbkCallBack.onUnzipFinish(isSuccess, outFile.getParentFile().getAbsolutePath());
		}
		return isSuccess;
	}

}


