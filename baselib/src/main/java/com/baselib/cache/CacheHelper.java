package com.baselib.cache;

import android.content.Context;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

/**
 * @作者： ton
 * @创建时间： 2018\12\17 0017
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class CacheHelper {
    public static void init(Context context){
        Hawk.init(context)
                .setEncryption(new NoEncryption())//不加密
                .build();
    }

    public static AppDisk getDiskFromApp(){
        return AppDisk.getInstance();
    }

    public static ExpiryDisk getExpiryDisk(){
        return ExpiryDisk.getInstance();
    }
}
