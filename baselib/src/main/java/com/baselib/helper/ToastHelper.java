package com.baselib.helper;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.baselib.DevApp;

/**
 * @作者： ton
 * @创建时间： 2018\5\16 0016
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class ToastHelper {
    public static void showToast(final String msg) {
        if(Thread.currentThread() != Looper.getMainLooper().getThread()){//不在主线程
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    show(msg);
                }
            });
        }else{
            show(msg);
        }
    }

    public static void showToast(@StringRes int res) {
        if (DevApp.getContext() == null) return;
        String msg = DevApp.getContext().getResources().getString(res);
        msg = TextUtils.isEmpty(msg) ? res + "" : msg;

        showToast(msg);
    }

    private static void show(String msg){
        if (DevApp.getContext() == null) return;
        if (TextUtils.isEmpty(msg)) return;
        Toast toast = Toast.makeText(DevApp.getContext(), null, Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }

}
