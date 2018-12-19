package com.baselib.helper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.baselib.ui.dialog.ProgressDialog;


/**
 * @作者： ton
 * @创建时间： 2018\11\30 0030
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class DialogHelper {
    public static Dialog createCommDialog(Context context, String title, String content, final boolean cancelable, boolean isAutoDismiss , final MenuDialogCallBack leftMenu, final MenuDialogCallBack rightMenu){
        if(content == null) return null;
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.theme(Theme.LIGHT);
        builder.autoDismiss(isAutoDismiss);
        if(!TextUtils.isEmpty(title)) builder.title(title);
        if(!TextUtils.isEmpty(content)) builder.content(content);
        builder.cancelable(cancelable).canceledOnTouchOutside(cancelable)
                .keyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent event) {
                        return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && !cancelable;
                    }
                });
        if(leftMenu != null){
            builder.negativeText(leftMenu.menuText)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            leftMenu.onClick(dialog);
                            leftMenu.onClick(dialog.getCustomView(),dialog);
                        }
                    });
        }
        if(rightMenu != null){
            builder.positiveText(rightMenu.menuText)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            rightMenu.onClick(dialog);
                            rightMenu.onClick(dialog.getCustomView(),dialog);
                        }
                    });
        }
        return builder.build();
    }

    public static ProgressDialog createProgressDialog(Context context, String title, String content, boolean cancelable){
        if(context == null) return null;
        return new ProgressDialog(context,title,content,cancelable);
    }

    public static Dialog createCustomViewDialog(Context context, final boolean cancelable, View customView , final LifecycleListener lifecycleListener){
        if(context == null) return null;
        return new MaterialDialog.Builder(context)
                .cancelable(cancelable)
                .theme(Theme.LIGHT)
                .customView(customView,true)
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        if(dialog != null && dialog instanceof MaterialDialog){
                            if(lifecycleListener != null) lifecycleListener.onCreate(((MaterialDialog)dialog).getCustomView(), ((MaterialDialog)dialog));
                        }
                    }
                }).build();
    }

    public static Dialog createCustomViewDialog(Context context, final boolean cancelable, @LayoutRes int layoutId , final LifecycleListener lifecycleListener){
        return createCustomViewDialog(context,cancelable,View.inflate(context,layoutId,null),lifecycleListener);
    }

    public static class MenuDialogCallBack{
        String menuText;
        public MenuDialogCallBack(String menuText){
            this.menuText = menuText;
        }
        public void onClick(Dialog dialog) {}
        public void onClick(View customView, Dialog dialog) {}
    }
    public interface LifecycleListener{
        void onCreate(View view,Dialog dialog);
    }
}
