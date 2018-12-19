package com.baselib.helper;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import com.baselib.DevApp;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @作者： ton
 * @创建时间： 2018\12\19 0019
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class InstallHelper {
    public static boolean isInstallApk(String packageName){
        if(DevApp.getContext() == null){
            return false;
        }
        PackageManager manager = DevApp.getContext().getPackageManager();
        List<PackageInfo> pkgList = manager.getInstalledPackages(0);
        for (int i = 0; i < pkgList.size(); i++) {
            PackageInfo pI = pkgList.get(i);
            if (pI.packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    public static void startInstall() {

    }
}
