package com.baselib.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

/**
 * @作者： ton
 * @创建时间： 2018\12\3 0003
 * @功能描述： 自定义dialog基类
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class BaseDialog {
    protected MaterialDialog dialog;
    public BaseDialog(Context context, int layoutId){
        dialog = new MaterialDialog.Builder(context)
                .theme(Theme.LIGHT)
                .customView(layoutId,false)
                .build();
    }
    public BaseDialog(MaterialDialog dialog){
        this.dialog = dialog;
    }
    public View getCustomView(){
        return dialog.getCustomView();
    }

    public Dialog getDialog(){
        return dialog;
    }

    public void show(){
        if (dialog == null || dialog.isShowing()) {
            return;
        }
        Activity bindAct = getActivity(dialog);

        if (!isValid(bindAct)) {
            return;
        }

        dialog.show();
    }

    public void dismiss(){
        if (dialog == null || !dialog.isShowing()) {
            return;
        }

        Activity bindAct = getActivity(dialog);
        if (bindAct != null && !bindAct.isFinishing()) {
            dialog.dismiss();
        }
    }

    private boolean isValid(Activity activity) {
        return activity != null && !activity.isFinishing();
    }

    private Activity getActivity(Dialog dialog) {
        Activity bindAct = null;
        Context context = dialog.getContext();
        do {
            if (context instanceof Activity) {
                bindAct = (Activity) context;
                break;
            } else if (context instanceof ContextThemeWrapper) {
                context = ((ContextThemeWrapper) context).getBaseContext();
            } else {
                break;
            }
        } while (true);
        return bindAct;
    }
}
