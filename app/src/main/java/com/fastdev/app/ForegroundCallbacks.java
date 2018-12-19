package com.fastdev.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.baselib.helper.ActStackHelper;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @作者： ton
 * @创建时间： 2018\12\18 0018
 * @功能描述： 用于 监听/判断 app前后台
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class ForegroundCallbacks implements Application.ActivityLifecycleCallbacks {
    public static final long CHECK_DELAY = 500;
    public interface Listener {
        /**
         * 切换到前台时调用
         */
        void onBecameForeground();
        /**
         * 切换到后台时调用
         */
        void onBecameBackground();
    }
    private static ForegroundCallbacks instance;
    private boolean foreground = false, paused = true;
    private Handler handler = new Handler();
    private List<Listener> listeners = new CopyOnWriteArrayList<Listener>();
    private Runnable check;
    public static ForegroundCallbacks init(Application application){
        if (instance == null) {
            instance = new ForegroundCallbacks();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {//October 2011: Android 4.0.
                application.registerActivityLifecycleCallbacks(instance);
            }
        }
        return instance;
    }
    public static ForegroundCallbacks get(Application application){
        if (instance == null) {
            init(application);
        }
        return instance;
    }
    public static ForegroundCallbacks get(Context ctx){
        if (instance == null) {
            Context appCtx = ctx.getApplicationContext();
            if (appCtx instanceof Application) {
                init((Application)appCtx);
            }
            throw new IllegalStateException(
                    "Foreground is not initialised and " +
                            "cannot obtain the Application object");
        }
        return instance;
    }
    public static ForegroundCallbacks get(){
        if (instance == null) {
            throw new IllegalStateException(
                    "Foreground is not initialised - invoke " +
                            "at least once with parameterised init/get");
        }
        return instance;
    }
    public boolean isForeground(){
        return foreground;
    }
    public boolean isBackground(){
        return !foreground;
    }
    public void addListener(Listener listener){
        listeners.add(listener);
    }
    public void removeListener(Listener listener){
        listeners.remove(listener);
    }
    @Override
    public void onActivityResumed(Activity activity) {
        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;
        if (check != null)
            handler.removeCallbacks(check);
        if (wasBackground){
//            LogA.e("went foreground");
            for (Listener l : listeners) {
                try {
                    l.onBecameForeground();
                } catch (Exception exc) {
//                    LogA.e("Listener threw exception!", exc);
                }
            }
        } else {
//            LogA.e("still foreground");
        }
    }
    @Override
    public void onActivityPaused(Activity activity) {
        paused = true;
        if (check != null)
            handler.removeCallbacks(check);
        handler.postDelayed(check = new Runnable(){
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
//                    LogA.e("went background");
                    for (Listener l : listeners) {
                        try {
                            l.onBecameBackground();
                        } catch (Exception exc) {
//                            LogA.e("Listener threw exception!", exc);
                        }
                    }
                } else {
//                    LogA.e("still foreground");
                }
            }
        }, CHECK_DELAY);
    }
    @Override
    public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
        ActStackHelper.getInstance().addActivity(activity);//加入管理栈中
    }
    @Override
    public void onActivityStarted(Activity activity) {}
    @Override
    public void onActivityStopped(Activity activity) {}
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
    @Override
    public void onActivityDestroyed(Activity activity) { ActStackHelper.getInstance().removeActivity(activity);}
}

