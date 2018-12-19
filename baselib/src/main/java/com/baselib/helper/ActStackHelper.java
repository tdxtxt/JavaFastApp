package com.baselib.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @作者： ton
 * @创建时间： 2018\6\20 0020
 * @功能描述： Activity栈管理器
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class ActStackHelper {
    /**
     * 单例
     */
    public static ActStackHelper getInstance() {
        return InstanceHolder.instance;
    }
    static class InstanceHolder {
        final static ActStackHelper instance = new ActStackHelper();
    }

    private List<Activity> mActs = Collections.synchronizedList(new LinkedList());

    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    public void addActivity(Activity activity) {
        mActs.add(activity);
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    public void removeActivity(Activity activity) {
        mActs.remove(activity);
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (mActs == null || mActs.isEmpty()) {
            return null;
        }
        Activity activity = mActs.get(mActs.size() - 1);
        return activity;
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public void finishCurrentActivity() {
        if (mActs == null || mActs.isEmpty()) {
            return;
        }
        Activity activity = mActs.get(mActs.size() - 1);
        finishActivity(activity);
    }
    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (mActs == null||mActs.isEmpty()) {
            return;
        }
        if (activity != null) {
            mActs.remove(activity);
            activity.finish();
            activity = null;
        }
    }
    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (mActs == null||mActs.isEmpty()) {
            return;
        }
        for (Activity activity : mActs) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }
    /**
     * 按照指定类名找到activity
     * @param cls
     * @return
     */
    public Activity findActivity(Class<?> cls) {
        Activity targetActivity = null;
        if (mActs != null) {
            for (Activity activity : mActs) {
                if (activity.getClass().equals(cls)) {
                    targetActivity = activity;
                    break;
                }
            }
        }
        return targetActivity;
    }
    /**
     * @return 作用说明 ：获取当前最顶部activity的实例
     */
    public Activity getTopActivity() {
        Activity mBaseActivity = null;
        synchronized (mActs) {
            final int size = mActs.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActs.get(size);
        }
        return mBaseActivity;
    }
    public boolean isTopActivity(Activity activity){
        if(activity == null) return false;
        return activity.getClass().getName().equals(getTopActivityName());
    }
    /**
     * @return 作用说明 ：获取当前最顶部的acitivity 名字
     */
    public String getTopActivityName() {
        Activity mBaseActivity = null;
        synchronized (mActs) {
            final int size = mActs.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActs.get(size);
        }
        return mBaseActivity.getClass().getName();
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (mActs == null) {
            return;
        }
        for (Activity activity : mActs) {
            activity.finish();
        }
        mActs.clear();
    }
    public List<Activity> getAllActivity(){
        return mActs;
    }

    public static boolean isMainProcess(Application app) {
        ActivityManager am = ((ActivityManager) app.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = app.getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
/**
 * 获取进程号对应的进程名
 * @param pid 进程号
 * @return 进程名
 */
public static String getProcessName(int pid) {
    BufferedReader reader = null;
    try {
        reader = new BufferedReader(new java.io.FileReader("/proc/" + pid + "/cmdline"));
        String processName = reader.readLine();
        if (!android.text.TextUtils.isEmpty(processName)) {
            processName = processName.trim();
        }
        return processName;
    } catch (Throwable throwable) {
        throwable.printStackTrace();
    } finally {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    return "";
}
    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {	}
    }
}
