package com.baselib.helper;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.baselib.DevApp;

/**
 * @作者： ton
 * @创建时间： 2018\5\24 0024
 * @功能描述： 打开系统界面
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class OpenOsActHelper {
    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     * <uses-permission android:name="android.permission.CALL_PHONE" />
     */
    public static void openCallPhone(String phoneNum){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        DevApp.getContext().startActivity(intent);
    }

    /**
     * 打开系统权限设置界面
     */
    public static void openAppSetting(){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", DevApp.getContext().getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", DevApp.getContext().getPackageName());
        }
        DevApp.getContext().startActivity(intent);
    }

    /**
     * 打开系统gps服务设置界面
     */
    public static void openLocationSetting(){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        DevApp.getContext().startActivity(intent);
    }
    public static void openAppStore(String marketPackageName,String targetPakageName){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(!TextUtils.isEmpty(marketPackageName)) intent.setPackage(marketPackageName);
        intent.setData(Uri.parse("market://details?id=" + targetPakageName)); //跳转到应用市场，非Google Play市场一般情况也实现了这个接口
        try {
            DevApp.getContext().startActivity(intent);
        } catch (Exception e) {
            ToastHelper.showToast("您的手机安装的google市场App无法打开");
            e.printStackTrace();
        }
    }
    /**
     * 打开市场的下载页面
     */
    public static void openGoogleStore(String packageName,String url){
        if(TextUtils.isEmpty(packageName)) return;
        String defaultUrl = "https://play.google.com/store/apps/details?id=" + packageName;
        url = TextUtils.isEmpty(url) ? defaultUrl : url;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName)); //跳转到应用市场，非Google Play市场一般情况也实现了这个接口

            if(InstallHelper.isInstallApk("com.android.vending")){
                openAppStore("com.android.vending", packageName);
            }else{
                if (intent.resolveActivity(DevApp.getContext().getPackageManager()) != null) {//有三方市场
                    OpenOsActHelper.openBrowser(url);
                }else{
                    //天哪,连浏览器都没有，玩个屁呀
                }
            }
        } catch (Exception e) {
            OpenOsActHelper.openBrowser(url);
        }
    }

    public static void openBrowser(String url){
        try{
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            DevApp.getContext().startActivity(intent);
        }catch(Exception e){
            ToastHelper.showToast("您的手机连浏览器都没有，快去安装一个吧！");
        }
    }

    /**
     * 设置通知栏显示开关
     */
    public static void openNotifySetting(){
        try{
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);

            //8.0及以后版本使用这两个extra.  >=API 26
            intent.putExtra(Settings.EXTRA_APP_PACKAGE,DevApp.getContext().getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID,DevApp.getContext().getApplicationInfo().uid);

            //5.0-7.1 使用这两个extra.  <= API 25, >=API 21
            intent.putExtra("app_package", DevApp.getContext().getPackageName());
            intent.putExtra("app_uid", DevApp.getContext().getApplicationInfo().uid);

            DevApp.getContext().startActivity(intent);
        }catch (Exception e){
            OpenOsActHelper.openAppSetting();
        }
    }

}
