package com.uninew.until;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.format.Formatter;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;

import static android.content.ContentValues.TAG;
import static android.content.Context.STORAGE_SERVICE;

public class SDCardUtils {

    private Context context;

    public SDCardUtils(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    /**
     * @param state 0:获取SD卡可用空间大小 1：获取手机内部可用空间大小
     * @return 空间大小（兆为单位）
     */

    public long getAvailableExternalMemorySize(int state) {
        StatFs stat = null;
        if (state == 0) {
            File path = new File("/mnt/media_rw/extsd");// 获取SDCard根目录
            if (!path.exists()) {
                return -1;
            }
            stat = new StatFs("/mnt/media_rw/extsd");
        } else if (state == 1) {
            File path = Environment.getExternalStorageDirectory();
            stat = new StatFs(path.getPath());
        }
        if (stat == null)
            return -1;

        long blockSize = stat.getBlockSizeLong();// 每个区块大小
        long availableBlocks = stat.getAvailableBlocksLong();
        long size = availableBlocks * blockSize / 1024 / 1024;// 以MB为单位
        return size;

    }

    /**
     * 获取手机内部可用空间大小
     *
     * @return
     */

    public String getAvailableInternalMemorySize() {

        File path = Environment.getDataDirectory();

        Log.i("zzz", path.getAbsolutePath());

        StatFs stat = new StatFs(path.getPath());

        long blockSize = stat.getBlockSize();

        long availableBlocks = stat.getAvailableBlocks();

        return Formatter.formatFileSize(context, availableBlocks * blockSize);
    }

    /**
     * 获取手机内部空间大小
     *
     * @return
     */

    public String getTotalInternalMemorySize() {

        File path = Environment.getDataDirectory();// Gets the Android data
        // directory

        Log.i("zzz", path.getAbsolutePath());

        StatFs stat = new StatFs(path.getPath());

        long blockSize = stat.getBlockSize(); // 每个block 占字节数

        long totalBlocks = stat.getBlockCount(); // block总数

        return Formatter.formatFileSize(context, totalBlocks * blockSize);
    }

    /**
     * 判断U盘路径
     *
     * @return
     */
    public String[] getPrimaryStoragePath() {
        try {
            StorageManager sm = (StorageManager) context.getSystemService(STORAGE_SERVICE);
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", new  Class[ 0 ]);
            String[] paths = (String[]) getVolumePathsMethod.invoke(sm, new  Object[]{});
            // first element in paths[] is primary storage path
            return paths;

        } catch (Exception e) {
            Log.e(TAG, "getPrimaryStoragePath() failed");
        }

        return null;
    }


    // 获得挂载的USB设备的存储空间使用情况

    public String[] getUSBStorage() {
        // USB Storage
        String[] str = new String[2];
        // mnt/udisk为USB设备在Android设备上的挂载路径.不同厂商的Android设备路径不同。
        // 这样写同样适合于SD卡挂载。 /mnt/media_rw/udisk or /storage/udisk
        File path = new File("/mnt/media_rw/udisk");
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        long availableBlocks = stat.getAvailableBlocks();
        String totalSize = Formatter.formatFileSize(context, (totalBlocks)
                * blockSize);
        // String usedSize = Formatter.formatFileSize(context,
        // (totalBlocks-availableBlocks) * blockSize);
        String availableSize = Formatter.formatFileSize(context,
                availableBlocks * blockSize);
        str[0] = totalSize;
        str[1] = availableSize;
        return str;// 空间:总共/可用的
    }

    /***
     * usb 是否可用 判断容量小于等于0 表示不可以用
     *
     * @return
     */
    public static boolean isUSBEnable(String paths) {

        File path = new File(paths);
        if (!path.exists()){
            return false;
        }
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return blockSize * totalBlocks <= 0 ? false : true;
    }

    /***
     * sd 是否可用 判断容量小于等于0 表示不可以用
     *
     * @return
     */
    public static boolean isSDEnable() {
        File path = new File("/mnt/media_rw/extsd");
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return blockSize * totalBlocks <= 0 ? false : true;
    }


    public boolean isExternalStorageState() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    // 获得挂载的USB设备的存储空间使用情况

    public String[] getSDStorage(String sdpath) {
        // sd Storage
        String[] str = new String[2];
        File path = new File(sdpath);
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        long availableBlocks = stat.getAvailableBlocks();
        String totalSize = Formatter.formatFileSize(context, (totalBlocks)
                * blockSize);
        String availableSize = Formatter.formatFileSize(context,
                availableBlocks * blockSize);
        str[0] = totalSize;
        str[1] = availableSize;
        return str;// 空间:总共/可用的
    }

    /**
     * 获取当前版本号
     *
     * @return
     */
    public int getPackageNum() {
        PackageManager manager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
        // TODO Auto-generated catch block
        int version = packInfo.versionCode;
        Log.i("Num", version + "");
        return version;
    }

    /**
     * 获取当前版本名称
     *
     * @return
     */
    public String getVersionName() {
        PackageManager manager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO Auto-generated catch block
        String version = packInfo.versionName;
        Log.i("Num", version + "");
        return version;
    }

    /**
     * 获取版本名称
     *
     * @return
     */
    public String getVersionName(String packageName) {
        Log.i("xhd","aaaaaaaaaa");
        PackageManager manager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            Log.i("xhd","bbbbbbbbbbbbb");
            packInfo = manager.getPackageInfo(packageName, 0);
            Log.i("xhd","cccccccccccccccccccc");
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        // TODO Auto-generated catch block
        String version = packInfo.versionName;
        Log.i("Num", version + "");
        return version;
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public int getPackageNum(String packageName) {
        PackageManager manager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = manager.getPackageInfo(packageName, 0);

        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
        // TODO Auto-generated catch block

        int version = packInfo.versionCode;
        Log.i("Num", version + "");
        return version;
    }



    /**
     * 计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好文件大小 单位（MB）
     */
    public static long getFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return blockSize / 1024 / 1024;
    }

    /**
     * 获取指定文件大小
     *
     * @param
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 获取SD卡APK版本号
     *
     * @param filePath
     */
    public int getSDpackageNum(String filePath) {
        int code = 0;
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(filePath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            code = info.versionCode;
        }
        return code;
    }

    /**
     * 获取SD卡APK版本名称
     *
     * @param filePath
     */
    public String getSDVersionName(String filePath) {
        String name = "";
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(filePath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            name = info.versionName;
        }
        return name;
    }

    /**
     * 获取SD卡APK包名
     *
     * @param filePath
     * @return
     */
    public String getSDpackageName(String filePath) {
        String name = null;
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(filePath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            // name = info.versionName;
            name = info.packageName;
        }
        return name;
    }

    /**
     * 安装APK
     *
     * @param absolutePath
     */
    public void updateAPK(String absolutePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + absolutePath),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
