package com.baselib.ui.dialog.child;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baselib.R;
import com.baselib.ui.dialog.NativeBaseDialog;
import com.baselib.ui.dialog.callback.MenuDialogCallBack;

public class NativeCommTipsDialog extends NativeBaseDialog {
    TextView tvTitle;
    TextView tvContent;
    TextView tvBtnLeft;
    TextView tvBtnCenter;
    TextView tvBtnRight;

    private boolean autoDismiss = true;

    public NativeCommTipsDialog(Activity context) {
        super(context, R.layout.baselib_dialog_commtips_view);
    }

    /**
     * 设置是否点击按钮会自动关闭弹框
     */
    public NativeCommTipsDialog setAutoDismiss(boolean autoDismiss){
        this.autoDismiss = autoDismiss;
        return this;
    }

    /**
     * 设置title
     */
    public NativeCommTipsDialog setTitle(String title){
        if(!TextUtils.isEmpty(title)){
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }else{
            tvTitle.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置显示内容
     */
    public NativeCommTipsDialog setContent(String content){
        if(!TextUtils.isEmpty(content)){
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(content);
        }else{
            tvContent.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置做菜单按钮
     */
    public NativeCommTipsDialog setLeftMenu(final MenuDialogCallBack callbackLeft){
        if(callbackLeft != null){
            tvBtnLeft.setVisibility(View.VISIBLE);
            tvBtnLeft.setText(callbackLeft.menuText);
            tvBtnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbackLeft.onClick(getRootView(),dialog);
                    if(autoDismiss) hide();
                }
            });
        }else{
            tvBtnLeft.setVisibility(View.GONE);
        }
        changeListener();
        return this;
    }

    /**
     * 设置中间菜单按钮
     */
    public NativeCommTipsDialog setCenterMenu(final MenuDialogCallBack callbackCenter){
        if(callbackCenter != null){
            tvBtnCenter.setVisibility(View.VISIBLE);
            tvBtnCenter.setText(callbackCenter.menuText);
            tvBtnCenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbackCenter.onClick(getRootView(),dialog);
                    if(autoDismiss) hide();
                }
            });
        }else{
            tvBtnCenter.setVisibility(View.GONE);
        }
        changeListener();
        return this;
    }

    /**
     * 设置右边菜单按钮
     */
    public NativeCommTipsDialog setRightMenu(final MenuDialogCallBack callbackRight){
        if(callbackRight != null){
            tvBtnRight.setVisibility(View.VISIBLE);
            tvBtnRight.setText(callbackRight.menuText);
            tvBtnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbackRight.onClick(getRootView(),dialog);
                    if(autoDismiss) hide();
                }
            });
        }else{
            tvBtnRight.setVisibility(View.GONE);
        }
        changeListener();
        return this;
    }

    private void changeListener(){
        if(tvBtnLeft.getVisibility() == View.GONE &&
                tvBtnCenter.getVisibility() == View.GONE &&
                tvBtnRight.getVisibility() == View.GONE){
            getRootView().findViewById(R.id.view_line).setVisibility(View.GONE);
            getRootView().findViewById(R.id.layout_menu).setVisibility(View.GONE);
        }
        else{
            getRootView().findViewById(R.id.view_line).setVisibility(View.VISIBLE);
            getRootView().findViewById(R.id.layout_menu).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initView(Dialog dialog, View dialogRootView) {
        tvTitle = (TextView) dialogRootView.findViewById(R.id.tv_common_prompt_title);
        tvContent = (TextView) dialogRootView.findViewById(R.id.tv_common_prompt_content);
        tvBtnLeft = (TextView) dialogRootView.findViewById(R.id.btn_common_prompt_left);
        tvBtnCenter = (TextView) dialogRootView.findViewById(R.id.btn_common_prompt_center);
        tvBtnRight = (TextView) dialogRootView.findViewById(R.id.btn_common_prompt_right);
    }

}
