package com.fastdev.app;

import android.app.Application;

/**
 * @作者： ton
 * @创建时间： 2018\12\20 0020
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class CustomApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        TondxApp.getInstance().onCreate(this);
    }
}
