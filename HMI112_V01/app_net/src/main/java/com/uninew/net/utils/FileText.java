package com.uninew.net.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class FileText {

    /**
     * 手机SD卡根目录目录
     */
    public static final String SDcard = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "service_buffer";
    private static final String SEND_DATA_SAVE_FILE = SDcard + File.separator + "send_service_message.txt";
    private static final String RECEIVE_DATA_SAVE_FILE = SDcard + File.separator + "receive_service_message.txt";

    public static int writeText(String text, boolean isSend) {
//		Log.i("xhd","sd:"+SDcard);
        FileWriter out = null;
        File file = null;
        if (isSend) {
            file = new File(SEND_DATA_SAVE_FILE);
        } else {
            file = new File(RECEIVE_DATA_SAVE_FILE);
        }
        if (file != null) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                if (out == null) {
                    out = new FileWriter(file, true);
                }
                out.write("\r\n" + text + "\r\n");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }else {
            return -1;
        }
        return 0;
    }


    public static void deletefile() {
        delAllFile(SDcard);
    }

    //删除文件夹
//param folderPath 文件夹完整绝对路径

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除指定文件夹下所有文件
//param path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

}
