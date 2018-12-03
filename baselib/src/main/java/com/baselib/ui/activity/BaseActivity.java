package com.baselib.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.baselib.R;
import com.baselib.bean.event.Event;
import com.baselib.helper.DialogHelper;
import com.baselib.helper.EventBusHelper;
import com.baselib.helper.HashMapParams;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.functions.Consumer;

/**
 * @作者： ton
 * @创建时间： 2018\11\30 0030
 * @功能描述： 所有activity的基类，必须继承它(强制),封装类容:调整方法
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public abstract class BaseActivity extends RxAppCompatActivity {
    private Dialog mProgressDialog;
    protected abstract @LayoutRes int  getLayoutResID();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseParams();//解析参数
        setContentView(getLayoutResID());
        overridePendingTransition(R.anim.baselib_slide_in_form_right, 0);//进入的切换动画
        initUi();
        if (isRegisteredEventBus()) {//尽量少用事件总线
            EventBusHelper.register(this);
        }
    }


/***************************************************/
    private void parseParams(){
        Intent intent = getIntent();
        Bundle extraBundle = intent.getBundleExtra("Bundle");
        if(extraBundle == null) extraBundle = new Bundle();
        getParams(extraBundle);
    }
    protected void getParams(Bundle bundle){}
    protected void initUi(){}
    protected void reqApi(){}

    /***************************************************/
    public void showProgressBar() {
        if(isFinishing()) return;
        if(mProgressDialog == null) mProgressDialog = DialogHelper.createProgressDialog(this,"温馨提示","请耐心等待，正在处理...",true);
        if(mProgressDialog != null){
            if(mProgressDialog.isShowing()) mProgressDialog.dismiss();//先关闭
            mProgressDialog.show();
        }
    }
    public void hideProgressBar() {
        if(mProgressDialog != null) mProgressDialog.dismiss();
    }
    /***************************************************/
    public void startActivity(Class clazz, HashMapParams params){
        Intent intent = new Intent(this,clazz);
        intent.putExtra("Bundle",params.toBundle());
        startActivity(intent);
    }
    /*************************************事件封装*****************************************/
    /**
     * 是否注册事件分发
     * @return true 注册；false 不注册，默认不注册
     */
    protected boolean isRegisteredEventBus() {
        return false;
    }
    /**
     * 接收到分发的事件
     * @param event 事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(Event event) {}

    /**
     * 接受到分发的粘性事件
     * 粘性事件，在注册之前便把事件发生出去，等到注册之后便会收到最近发送的粘性事件
     * 注意：只会接收到最近发送的一次粘性事件，之前的会接受不到。
     * @param event 粘性事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveStickyEvent(Event event) {
    }
    /****************************************键盘操作******************************************/
    private static boolean isInSpace(View v, MotionEvent event) {
        int[] location = new int[2];
        v.getLocationInWindow(location);
        int left = location[0];
        int top = location[1];
        int bottom = top + v.getHeight();
        int right = left + v.getWidth();
        return event.getX() > left
                && event.getX() < right
                && event.getY() > top
                && event.getY() < bottom;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v != null && (v instanceof EditText)) {
                if (!isInSpace(v, ev)) {
                    //当前触摸位置不处于焦点控件中，需要隐藏软键盘
                    hideKeyboard();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    public void hideKeyboard() {
        if (this != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }
    /********************************************拦截返回事件*********************************************/
    protected boolean isBackEnable = true;//返回是否可用
    /**
     * true可以返回，默认为ture，false不可返回
     * @param backEnable
     */
    protected void setBackEnable(boolean backEnable){
        this.isBackEnable = backEnable;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(isBackEnable){
                super.onKeyDown(keyCode, event);
            }else{
                return false;//不吃掉事件,但不能返回
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.baselib_slide_out_form_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegisteredEventBus()) {
            EventBusHelper.unregister(this);
        }
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }
}
