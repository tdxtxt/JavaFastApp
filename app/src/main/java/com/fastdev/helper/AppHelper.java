package com.fastdev.helper;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.fastdev.app.TondxApp;
import com.fastdev.bean.update.Market;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @作者： ton
 * @创建时间： 2018\7\2 0002
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class AppHelper {
    /**
     * 获取首席系统中的应用市场
     */
    public static List<Market> getAppStores(){
        List<Market> markets = new ArrayList<>();
        if(TondxApp.getContext() == null){
            return markets;
        }
        //目前只支持google市场、应用宝
        List<String> supportMarkets = Arrays.asList("com.android.vending","com.tencent.android.qqdownloader");

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("market://details?id="));
        PackageManager manager = TondxApp.getContext().getPackageManager();
        List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
        if(infos == null || infos.size() == 0) return markets;

        for(ResolveInfo info : infos){
            ActivityInfo activityInfo = info.activityInfo;
            String packageName = activityInfo.packageName;
            if(supportMarkets.contains(packageName)){
                // 获取APP名称
                String name;
                try {
                    name = manager.getApplicationLabel(manager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
                } catch (PackageManager.NameNotFoundException e) {
                    name = "";
                }
                Market marketItem = new Market(activityInfo.loadIcon(manager), name, packageName);
                markets.add(marketItem);
            }
        }
        return markets;
    }
}
