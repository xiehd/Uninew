package com.uninew.maintanence.model;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RecoverySystem;
import android.text.TextUtils;
import android.util.Log;

import com.uninew.bean.AppInfo;
import com.uninew.maintanence.presenter.IApkCheckCallBack;
import com.uninew.maintanence.presenter.IMsgsCallBack;
import com.uninew.maintanence.presenter.IResultCallBack;
import com.uninew.maintanence.presenter.IUpdate;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class UpDateManager implements IUpdate {
	private Context mContext;
	private WakeLock mWakelock;
	//private static final String UpdateFilePath = "/mnt/media_rw/extsd/";
    private static final String UpdateFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
	private static final String mCacheUpdatePackageLocation = "/cache/update.zip";

	public UpDateManager(Context mContext) {
		super();
		this.mContext = mContext;
		PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
		mWakelock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "OTA Wakelock");
	}

	@Override
	public void updateApk(String apkAbsolutePath, IResultCallBack resultCallBack) {
		String[] args = { "pm", "install", "-r", apkAbsolutePath };
		String result = "";
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		Process process = null;
		InputStream errIs = null;
		InputStream inIs = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			process = processBuilder.start();
			errIs = process.getErrorStream();
			Log.i("update", "----errIs=" + errIs);
			while ((read = errIs.read()) != -1) {
				baos.write(read);
				// Log.i("update", "----errIS---read="+read);
			}
			baos.write('\n');
			inIs = process.getInputStream();
			Log.i("update", "----inIs=" + inIs);
			while ((read = inIs.read()) != -1) {
				baos.write(read);
				// Log.i("update", "----inIs---read="+read);
			}
			byte[] data = baos.toByteArray();
			result = new String(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (errIs != null) {
					errIs.close();
				}
				if (inIs != null) {
					inIs.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (process != null) {
				process.destroy();
			}
		}
		if (result.toString().contains("Success") || result.toString().contains("success")) {
			Log.i("update", "update success");
			if (resultCallBack != null) {
				resultCallBack.resultCallBack(true);
			}
		} else {
			Log.i("update", "update error");
			if (resultCallBack != null) {
				resultCallBack.resultCallBack(false);
			}
		}
	}

	@Override
	public void updateOS(final String osAbsolutePath, final IResultCallBack resultCallBack) {
		//broadCastTool.sendOSUpdateNotify();
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean inCache = copyFile(osAbsolutePath, mCacheUpdatePackageLocation);
				if (inCache) {
					if (startInstallUpgradePackage(mCacheUpdatePackageLocation)) {
						if (resultCallBack != null) {
							resultCallBack.resultCallBack(true);
						}
					} else {
						if (resultCallBack != null) {
							resultCallBack.resultCallBack(false);
						}
					}
				} else {
					if (resultCallBack != null) {
						resultCallBack.resultCallBack(false);
					}
				}
			}
		}).start();
	}

	private boolean copyFile(String oldPath, String newPath) {
		boolean isok = true;
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024 * 4];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				fs.flush();
				fs.close();
				inStream.close();
			} else {
				isok = false;
			}
		} catch (Exception e) {
			// System.out.println("复制单个文件操作出错");
			e.printStackTrace();
			isok = false;
		}
		return isok;
	}

	private boolean startInstallUpgradePackage(String path) {
		File recoveryFile = new File(path);
		try {
			mWakelock.acquire();
			RecoverySystem.verifyPackage(recoveryFile, null, null);
		} catch (IOException e1) {
			// reportInstallError(OTAStateChangeListener.ERROR_PACKAGE_VERIFY_FALIED);
			e1.printStackTrace();
			Log.e("OSUpdate", "无效升级包" + e1.getMessage());
			return false;
		} catch (GeneralSecurityException e1) {
			// reportInstallError(OTAStateChangeListener.ERROR_PACKAGE_VERIFY_FALIED);
			e1.printStackTrace();
			Log.e("OSUpdate", "无效升级包" + e1.getMessage());
			Log.e("OSUpdate", "GeneralSecurityException	" + e1.getMessage());
			return false;
		} finally {
			mWakelock.release();
		}
		// then install package
		try {
			mWakelock.acquire();
			RecoverySystem.installPackage(mContext, recoveryFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// reportInstallError(OTAStateChangeListener.ERROR_PACKAGE_INSTALL_FAILED);
			e.printStackTrace();
			Log.d("OSUpdate", "IOException	" + e.getMessage());
			return false;
		} catch (SecurityException e) {
			e.printStackTrace();
			// reportInstallError(OTAStateChangeListener.ERROR_PACKAGE_INSTALL_FAILED);
			return false;
		} finally {
			mWakelock.release();
		}
		return true;
		// cannot reach here...

	}

	@Override
	public int compareVersion(String version1, String version2) {
		return 0;
	}

	@Override
	public void findMsgsFromSD(String fileStart, String fileEnd, IMsgsCallBack apkMsgsCallBack) {

	}

	/**
	 * 获取SD卡更新包下面的所有MCU升级文件信息
	 * 
	 * @return
	 */
	public List<File> findMCUUpdateFile() {
		List<File> mcuFiles = new ArrayList<>();
		try {
			// 获得升级文件夹
			File file = new File(UpdateFilePath);
			if (file.exists()) {
				if (file.isDirectory()) {
					File[] uFile = file.listFiles();
					for (File f : uFile) {
						String name = f.getName();
						if (name.startsWith("Mcu") && name.endsWith(".bin")) {
							mcuFiles.add(f);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return mcuFiles;
	}
	/**
	 * 获取SD卡更新包下面的所有OS升级文件
	 * @return
	 */
	public List<File> findOsUpdateFile() {
		Log.i("xhd","升级OS文件根目录:"+UpdateFilePath);
		List<File> osFile = new ArrayList<>();
		try {
			File file = new File(UpdateFilePath);
			//File file = new File(usbpath);
			if (file.exists()) {
				if (file.isDirectory()) {
					File[] osFiles = file.listFiles();
					for (File f : osFiles) {
						String name = f.getName();
						if (name.startsWith("update") && name.endsWith(".zip")) {
							osFile.add(f);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return osFile;
	}

	/**
	 * 查询SD卡当中的os系统升级文件版本号
	 * 
	 * @param path OS系统更新包文件路径
	 * @return
	 */
	public String findOsUpdateFileVersion(String path) {
		String version = null;
		try {
			ZipFile zipFile = new ZipFile(path);
			InputStream is = null;
			InputStreamReader isr = null;
			BufferedReader br = null;
			Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zipFile.entries();
			while (en.hasMoreElements()) {
				ZipEntry zipEntry = en.nextElement();
				if (!zipEntry.isDirectory()) {
					if (zipEntry.getName().equals("system/build.prop")) {
						is = zipFile.getInputStream(zipEntry);
						isr = new InputStreamReader(is);
						br = new BufferedReader(isr);
						String line = null;
						while ((line = br.readLine()) != null) {
							if (!TextUtils.isEmpty(line)) {
								int index = line.indexOf("ro.build.id");
								if (index != -1) {
									version = line.split("=")[1];
								}
							}
						}
					}
				}
			}
			if (null != br) {
				br.close();
			}
			if (null != isr) {
				isr.close();
			}
			if (is != null) {
				is.close();
			}
			if (zipFile != null) {
				zipFile.close();
			}
		} catch (ZipException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return version;
	}

	/**
	 * 查找已安装的apk和apk更新包
	 * 
	 * @param path apk更新包路径
	 * @param apkCheckCallBack
	 */
	public void getApkUpdateFile(String path, final IApkCheckCallBack apkCheckCallBack) {
		try {
			final PackageManager packageManager = mContext.getPackageManager();
			// 获取手机内所有应用
			final List<PackageInfo> paklist = packageManager.getInstalledPackages(0);
			final List<AppInfo> installApkInfos = new ArrayList<>();
			File file = new File(path);
			if (file.exists()) {
				if (file.isDirectory()) {
					final File[] files = file.listFiles();
					if (files != null && files.length > 0) {
						final List<AppInfo> updateApkInfos = new ArrayList<>();
						new Thread() {
							@Override
							public void run() {
								for (File f : files) {
									String name = f.getName();
									if (name.endsWith(".apk")) {
										String apkPath = f.getAbsolutePath();
										PackageInfo packageInfo = packageManager
												.getPackageArchiveInfo(apkPath,
														PackageManager.GET_ACTIVITIES);
										ApplicationInfo aInfo = packageInfo.applicationInfo;
										if (null != aInfo) {
											aInfo.sourceDir = apkPath;
											aInfo.publicSourceDir = apkPath;
										}
										AppInfo appInfo = new AppInfo(
												aInfo.loadIcon(packageManager), aInfo.loadLabel(
														packageManager).toString(),
												aInfo.packageName, packageInfo.versionCode, 0,
												packageInfo.versionName, null, apkPath);
										updateApkInfos.add(appInfo);
									}
								}

								for (int i = 0; i < paklist.size(); i++) {
									PackageInfo pak = paklist.get(i);
									// 判断是否为非系统预装的应用程序,大于0说明是系统预装apk
									if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
										ApplicationInfo applicationInfo = pak.applicationInfo;
										AppInfo appInfo = new AppInfo(
												applicationInfo.loadIcon(packageManager),
												applicationInfo.loadLabel(packageManager)
														.toString(), applicationInfo.packageName,
												0, pak.versionCode, null, pak.versionName, null);
										installApkInfos.add(appInfo);
									}
								}

								if (null != apkCheckCallBack) {
									apkCheckCallBack.apkCheckCallBack(installApkInfos,updateApkInfos);
								}
							}

						}.start();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
