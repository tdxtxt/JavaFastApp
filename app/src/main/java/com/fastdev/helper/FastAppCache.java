package com.fastdev.helper;

import android.text.TextUtils;

import com.baselib.cache.CacheHelper;
import com.fastdev.ton.BuildConfig;

/**
 * @作者： ton
 * @创建时间： 2018\12\18 0018
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class FastAppCache {
    public static void putHost1(String host){
        if(TextUtils.isEmpty(host)) return;
        CacheHelper.getDiskFromApp().add("NEWKEY_HOST1", host);
    }
    public static String getHost1(){
        return CacheHelper.getDiskFromApp().get("NEWKEY_HOST1", BuildConfig.HOST1);
    }

}
