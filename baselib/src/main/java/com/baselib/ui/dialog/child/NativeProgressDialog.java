package com.baselib.ui.dialog.child;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baselib.R;
import com.baselib.ui.dialog.NativeBaseDialog;

public class NativeProgressDialog extends NativeBaseDialog {
    TextView tvDesc;

    public NativeProgressDialog(Activity context) {
        super(context, R.layout.baselib_dialog_commprogress_view);
    }

    @Override
    protected void initView(Dialog dialog, View dialogRootView) {
        tvDesc = (TextView) dialogRootView.findViewById(R.id.tv_desc);
    }
    public NativeProgressDialog setDesc(String desc){
        if(!TextUtils.isEmpty(desc)){
            tvDesc.setText(desc);
        }else{
            tvDesc.setText("正在加载...");
        }
        return this;
    }

    @Override
    protected int provideDialogWidth(Context context) {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }
    @Override
    protected int provideDialogHeight(Context context) {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }
}
