package com.baselib.helper;


import android.support.annotation.Nullable;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * @作者： ton
 * @创建时间： 2018\5\16 0016
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class LogA {
    static boolean isLog;
    public static void init(boolean isLog){
        LogA.isLog = isLog;
        init();
    }
    /**
     * 初始化log工具，在app入口处调用
     */
    public static void init() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  //是否显示线程信息，默认 显示
                .methodCount(0)         // 中间的方法栈打印的行数，默认是 2
                .methodOffset(7)        // 设置调用堆栈的函数偏移值，默认是 5
//                .logStrategy(customLog) // 设置log打印策略，默认是 LogCat
                .tag("application-ton")   // 设置全局TAG，默认是 PRETTY_LOGGER
                .build();
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy){
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                //根据返回的boolean值来控制是否打印log
                return isLog;
            }
        });
    }
    public static void d(String message) {
        if(isLog) Logger.d(message);
    }

    public static void i(String message) {
        Logger.i(message);
    }

    public static void w(String message, Throwable e) {
        String info = e != null ? e.toString() : "null";
        if(isLog) Logger.w(message + "：" + info);
    }

    public static void e(String message, Throwable e) {
        if(isLog) Logger.e(e, message);
    }

    public static void json(String json) {
        if(isLog) Logger.json(json);
    }
}
