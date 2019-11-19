package com.baselib.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baselib.R;
import com.baselib.bean.event.Event;
import com.baselib.helper.DialogHelper;
import com.baselib.helper.EventBusHelper;
import com.baselib.helper.FragmentHelper;
import com.baselib.helper.HashMapParams;
import com.baselib.helper.ToastHelper;
import com.baselib.net.ComposeHelper;
import com.baselib.net.reqApi.model.IModel;
import com.baselib.ui.activity.callback.StartForResultListener;
import com.baselib.ui.fragment.StartForResultFragment;
import com.baselib.ui.statusview.CustomCallback;
import com.baselib.ui.statusview.EmptyCallback;
import com.baselib.ui.statusview.ErrorCallback;
import com.baselib.ui.statusview.LoadingCallback;
import com.baselib.ui.view.IView;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.Convertor;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.kingja.loadsir.core.Transport;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Publisher;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;

/**
 * @作者： ton
 * @创建时间： 2018\11\30 0030
 * @功能描述： 所有activity的基类，必须继承它(强制),封装类容:调整方法
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public abstract class BaseActivity extends RxAppCompatActivity implements IView {
    private Dialog mProgressDialog;
    private LoadService mLoadService;
    private RxPermissions mRxPermission;
    private Unbinder unbinder;
    protected abstract @LayoutRes int  getLayoutResID();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseParams();//解析参数
        setContentView(getLayoutResID());
        unbinder = ButterKnife.bind(this);
        initLoadView();
        overridePendingTransition(R.anim.baselib_slide_in_form_right, 0);//进入的切换动画
        initUi();
        if (isRegisteredEventBus()) {//尽量少用事件总线
            EventBusHelper.register(this);
        }
    }

/************************状态页封装***************************/
    protected int getContentResID(){
        return R.id.view_content;
    }
    /**
     * 特殊页面 可重写配置相应状态页面,默认app初始化配置为准
     */
    protected LoadSir configLoadView(){
        return LoadSir.getDefault();
    }
    private void initLoadView(){
        LoadSir loadSir = configLoadView();
        if(loadSir == null) loadSir = LoadSir.getDefault();
        View contentView = findViewById(getContentResID());
        if(contentView == null) return;
        //注册
        mLoadService = loadSir.register(contentView, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                BaseActivity.this.onReload();
            }
        }, new Convertor<Throwable>() {
            @Override
            public Class<? extends Callback> map(Throwable o) {
                return null;
            }
        });
        mLoadService.setCallBack(EmptyCallback.class, new Transport() {
            @Override
            public void order(Context context, View view) {
                dynamicEmptyView(((ImageView)view.findViewById(R.id.iv_empty)),((TextView)view.findViewById(R.id.tv_empty)));
            }
        });
    }
    @Override
    public void showLoadingView() {
        if(mLoadService != null) mLoadService.showCallback(LoadingCallback.class);
    }
    @Override
    public void showContentView() {
        if(mLoadService != null) mLoadService.showSuccess();
    }
    @Override
    public void showEmptyView() {
        if(mLoadService != null) mLoadService.showCallback(EmptyCallback.class);
    }
    @Override
    public void showCustomView(){
        if(mLoadService != null) mLoadService.showCallback(CustomCallback.class);
    }
    @Override
    public void showErrorView(Throwable e) {
        if(mLoadService != null) mLoadService.showCallback(ErrorCallback.class);
    }
    /**
     * 动态调整空页面
     */
    public void dynamicEmptyView(ImageView ivEmpty, TextView tvEmpty){}
    /*************************************参数封装解析*****************************************/
    private void parseParams(){
        Intent intent = getIntent();
        Bundle extraBundle = intent.getBundleExtra("Bundle");
        if(extraBundle == null) extraBundle = new Bundle();
        getParams(extraBundle);
    }
    protected void getParams(Bundle bundle){}
    protected void initUi(){}
    protected void reqApi(){}
    protected void onReload() {
        reqApi();
    }
    /*************************************进度条*****************************************/
    @Override
    public void showProgressBar() {
        if(isFinishing()) return;
        if(mProgressDialog == null) mProgressDialog = DialogHelper.createProgressDialog(this,"温馨提示","请耐心等待，正在处理...",true).getDialog();
        if(mProgressDialog != null){
            if(mProgressDialog.isShowing()) mProgressDialog.dismiss();//先关闭
            mProgressDialog.show();
        }
    }
    @Override
    public void hideProgressBar() {
        if(mProgressDialog != null) mProgressDialog.dismiss();
    }

    @Override
    public void showToast(String msg){
        ToastHelper.showToast(msg);
    }
    /*************************************startActivity*****************************************/
    public void startActivity(Class clazz, HashMapParams params){
        Intent intent = new Intent(this,clazz);
        intent.putExtra("Bundle",params.toBundle());
        startActivity(intent);
    }

    StartForResultFragment  mFragment;
    public void startActivityForResult(Class clazz, HashMapParams params, StartForResultListener listener){
        mFragment = (StartForResultFragment) getSupportFragmentManager().findFragmentByTag("__start_for_result");
        if (mFragment == null) {
            mFragment = new StartForResultFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(mFragment, "__start_for_result")
                    .commitAllowingStateLoss();//避免数据保存和恢复导致的crash
            getSupportFragmentManager().executePendingTransactions();
        }
        mFragment.setListener(listener);
        Intent intent = new Intent(this,clazz);
        intent.putExtra("Bundle",params.toBundle());
        mFragment.startActivityForResult(intent, 12);
    }

    /**
     * 动态权限
     */
    public Observable<Permission> requestPermissions(String ... permissions) {
        if(mRxPermission == null) mRxPermission = new RxPermissions(this);
        return mRxPermission.requestEach(permissions);
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
    /****************************************网络请求销毁******************************************/
    @Override
    public <T> FlowableTransformer<T,T> bindToLife(){
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.compose(ComposeHelper.<T>getUIThread())
                        .compose(BaseActivity.this.<T>bindToLifecycle());
            }
        };
    }
    @Override
    public <T> FlowableTransformer<T,T> bindToLifeAndIgnoreError(){
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.compose(BaseActivity.this.<T>bindToLife())
                        .onErrorResumeNext(Flowable.<T>empty())
                        .onExceptionResumeNext(Flowable.<T>empty());
            }
        };
    }
    @Override
    public <T extends IModel> FlowableTransformer<T,T> bindToLifeAndReqApi(){
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.compose(BaseActivity.this.<T>bindToLife())
                        .compose(ComposeHelper.<T>getApiTransformer());
            }
        };
    }

    /********************************************拦截返回事件*********************************************/
    /**
     * true表示拦截，不可返回，false表示不拦截，可返回
     */
    protected boolean interceptBackEvent(){
        return false;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(interceptBackEvent()){
                return false;//不吃掉事件,但不能返回
            }else{
                super.onKeyDown(keyCode, event);
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
        if(unbinder != null) unbinder.unbind();
        if (isRegisteredEventBus()) {
            EventBusHelper.unregister(this);
        }
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }
}
