package com.baselib.helper;

import android.content.Context;
import android.os.Environment;


import com.baselib.DevApp;

import java.io.File;

/**
 * @作者： ton
 * @创建时间： 2018\5\17 0017
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class FileHelper {
    /**
     * 不需要读写权限了
     */
    private static File getCacheDir(Context context,String dirName){
        if(context == null) context = DevApp.getContext();
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }
        cacheDir = new File(cacheDir,dirName);
        cacheDir.mkdirs();
        return cacheDir;
    }

    /**
     * 需要文件的写入权限
     */
    private static File getSdcardRootDir(Context context){
        File sdcardDir = null;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            sdcardDir = Environment.getExternalStorageDirectory();
        }else{
            sdcardDir = context.getCacheDir();
        }
        return sdcardDir;
    }
    /**
     * 图片存放目录位置
     */
    public static File getImageDir(Context context){
        return getCacheDir(context,"image");
    }
    /**
     * 需要文件的写入权限
     * @return SDCard--/DCIM
     */
    public static File getDCIMDir(Context context) {
        File dcimDir = new File(getSdcardRootDir(context),"DCIM");
        dcimDir.mkdirs();
        return dcimDir;
    }

    public static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    public static boolean isDir(final String dirPath) {
        return isDir(getFileByPath(dirPath));
    }
    public static boolean isDir(final File file) {
        return file != null && file.exists() && file.isDirectory();
    }
    public static boolean isFile(final String filePath) {
        return isFile(getFileByPath(filePath));
    }
    public static boolean isFile(final File file) {
        return file != null && file.exists() && file.isFile();
    }
    public static String getDirSize(final String dirPath) {
        return getDirSize(getFileByPath(dirPath));
    }
    public static String getDirSize(final File dir) {
        long len = getDirLength(dir);
        return len == -1 ? "" : byte2FitMemorySize(len);
    }

    public static String getFileSize(final String filePath) {
        return getFileSize(getFileByPath(filePath));
    }
    public static String getFileSize(final File file) {
        if(file == null) return "";
        long len = file.length();
        return len == -1 ? "" : byte2FitMemorySize(len);
    }

    public static long getDirLength(final String dirPath) {
        return getDirLength(getFileByPath(dirPath));
    }
    public static long getDirLength(final File dir) {
        if (!isDir(dir)) return -1;
        long len = 0;
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    len += getDirLength(file);
                } else {
                    len += file.length();
                }
            }
        }
        return len;
    }

    private static String byte2FitMemorySize(final long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < 1024) {
            return String.format("%.3fB", (double) byteNum);
        } else if (byteNum < 1048576) {
            return String.format("%.3fKB", (double) byteNum / 1024);
        } else if (byteNum < 1073741824) {
            return String.format("%.3fMB", (double) byteNum / 1048576);
        } else {
            return String.format("%.3fGB", (double) byteNum / 1073741824);
        }
    }
    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
