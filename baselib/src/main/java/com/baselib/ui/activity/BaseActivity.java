package com.baselib.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.baselib.helper.DialogHelper;
import com.baselib.helper.HashMapParams;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

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
    }


/***************************************************/
    private void parseParams(){
        Intent intent = getIntent();
        Bundle extraBundle = intent.getBundleExtra("Bundle");
        if(extraBundle == null) extraBundle = new Bundle();
        getParams(extraBundle);
    }
    protected void getParams(Bundle extraBundle){}
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

}
