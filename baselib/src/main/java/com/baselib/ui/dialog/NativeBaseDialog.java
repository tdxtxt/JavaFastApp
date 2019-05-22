package com.baselib.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.baselib.R;
import com.baselib.helper.ScreenHelper;

public class NativeBaseDialog {
    protected Activity context;
    protected Dialog dialog;
    protected View rootView;
    protected boolean mCancelable = true;
    protected boolean mCancelableOnTouchOutside = true;

    public NativeBaseDialog(Activity context,int layoutId){
        this(context,layoutId, R.style.smart_show_dialog);
    }

    public NativeBaseDialog(Activity context,int layoutId,int style){
        this.context = context;
        dialog = new AppCompatDialog(context, style);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        inflate(context,layoutId, null);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnViewGlobalLayoutListener(rootView, provideDialogMaxHeight(context)));
        initView(dialog, rootView);
        ViewGroup.MarginLayoutParams rootLp = new ViewGroup.MarginLayoutParams(provideDialogWidth(context), provideDialogHeight(context));
        setCancelableOnTouchOutside(true);

        dialog.setContentView(rootView, rootLp);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                onDestroy();
            }
        });
    }

    /**
     * 设置是否可取消弹框【点击弹框外部窗口、返回按键的屏蔽】
     */
    public <T extends NativeBaseDialog> T setCancelable(boolean cancelable){
        this.mCancelable = cancelable;
        dialog.setCancelable(mCancelable);
        return (T) this;
    }

    /**
     * 设置点击弹框外部窗口是否可取消弹框
     */
    public <T extends NativeBaseDialog> T setCancelableOnTouchOutside(boolean cancelableOnTouchOutside){
        this.mCancelableOnTouchOutside = cancelableOnTouchOutside;
        dialog.setCanceledOnTouchOutside(mCancelable ? mCancelableOnTouchOutside : false);
        return (T) this;
    }

    /**
     * 子类都必须调用此方法
     */
    public <T extends NativeBaseDialog> T show(){
        if(dialog == null) return (T)this;
        if(context.isFinishing()) return (T)this;
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        dialog.show();
        return (T)this;
    }

    public void hide(){
        if(dialog != null) dialog.dismiss();
    }

    /**
     * 子类若要手动释放资源，需重写此方法
     */
    public void onDestroy(){

    }

    /**
     * 用于初始化控件，子类可重写
     */
    protected void initView(Dialog dialog, View dialogRootView) {

    }

    private void inflate(Context context, int layoutId, ViewGroup root) {
        rootView = View.inflate(context,layoutId,root);
    }

    public View getRootView(){
        return rootView;
    }

    /**
     * 弹框的宽度
     */
    protected int provideDialogWidth(Context context) {
        return Math.min(ScreenHelper.getScreenWidth(context) - ScreenHelper.dp2px(context,70), ScreenHelper.dp2px(context,290));
    }

    /**
     * 弹框的高度
     */
    protected int provideDialogHeight(Context context) {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    /**
     * 允许弹框的最大高度
     */
    protected int provideDialogMaxHeight(Context context) {
        return ScreenHelper.getScreenHeight(context) * 2 / 3;
    }

    /**
     * 设置最大高度的监听器
     */
    public static class OnViewGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private int maxHeight = 500;
        private View view;

        public OnViewGlobalLayoutListener(View view, int height) {
            this.view = view;
            this.maxHeight = height;
        }

        @Override
        public void onGlobalLayout() {
            if (view.getHeight() > maxHeight)
                view.getLayoutParams().height = maxHeight;
        }
    }

}
