package com.baselib.helper;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.baselib.DevApp;

/**
 * @作者： ton
 * @创建时间： 2018\12\18 0018
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class VersionHelper {
    public static int getApkVersionCode (){
        PackageManager pm = DevApp.getContext().getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(DevApp.getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo.versionCode;
    }

    public static String getApkVersionName(){
        PackageManager pm = DevApp.getContext().getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(DevApp.getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo.versionName;
    }
}
