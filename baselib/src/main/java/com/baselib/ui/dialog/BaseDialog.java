package com.baselib.ui.dialog;

import android.content.Context;
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
    public View getCustomView(){
        return dialog.getCustomView();
    }
}
