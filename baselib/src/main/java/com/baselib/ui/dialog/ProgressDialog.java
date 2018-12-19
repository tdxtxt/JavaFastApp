package com.baselib.ui.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

/**
 * @作者： ton
 * @创建时间： 2018\12\19 0019
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class ProgressDialog extends BaseDialog{
    public ProgressDialog(Context context,String title,String content,boolean cancelable) {
        super(new MaterialDialog.Builder(context)
                .cancelable(cancelable)
                .theme(Theme.LIGHT)
                .title(title)
                .content(content)
                .progress(false, 100,false)
                .build());
    }

    public void setProgressValue(int progress){
        dialog.setProgress(progress);
    }
}
