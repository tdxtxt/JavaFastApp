package com.baselib.ui.mvp.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import com.baselib.ui.activity.BaseActivity;
import com.baselib.ui.mvp.factory.PresenterMvpFactory;
import com.baselib.ui.mvp.factory.PresenterMvpFactoryImpl;
import com.baselib.ui.mvp.presenter.BaseMvpPresenter;
import com.baselib.ui.mvp.proxy.BaseMvpProxy;
import com.baselib.ui.mvp.proxy.PresenterProxyInterface;
import com.baselib.ui.mvp.view.BaseMvpView;


/**
 * @作者： ton
 * @时间： 2018\5\11 0011
 * @描述： 创建Presenter有两种方式：1、重写createPresenter方法；2、class类上面添加注释@CreatePresenter(XXXPresenter.class)
 * @传入参数说明：
 * @返回参数说明：
 */
public abstract class AbstractMvpActivitiy<V extends BaseMvpView, P extends BaseMvpPresenter<V>> extends BaseActivity implements PresenterProxyInterface<V,P> {

    private static final String PRESENTER_SAVE_KEY = "presenter_save_key";
    /**
     * 创建被代理对象,传入默认Presenter的工厂
     */
    private BaseMvpProxy<V,P> mProxy = new BaseMvpProxy<>(PresenterMvpFactoryImpl.<V,P>createFactory(getClass(),createPresenter()));

    protected P createPresenter(){return null;}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("perfect-mvp","V onCreate");
        Log.e("perfect-mvp","V onCreate mProxy = " + mProxy);
        Log.e("perfect-mvp","V onCreate this = " + this.hashCode());

        if(savedInstanceState != null){
            mProxy.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_SAVE_KEY));
        }
        mProxy.onCreate((V) this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("perfect-mvp","V onResume");
        mProxy.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("perfect-mvp","V onDestroy = " );
        mProxy.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("perfect-mvp","V onSaveInstanceState");
        outState.putBundle(PRESENTER_SAVE_KEY,mProxy.onSaveInstanceState());
    }

    @Override
    public void setPresenterFactory(PresenterMvpFactory<V, P> presenterFactory) {
        Log.e("perfect-mvp","V setPresenterFactory");
        mProxy.setPresenterFactory(presenterFactory);
    }

    @Override
    public PresenterMvpFactory<V, P> getPresenterFactory() {
        Log.e("perfect-mvp","V getPresenterFactory");
        return mProxy.getPresenterFactory();
    }

    @Override
    public P getMvpPresenter() {
        Log.e("perfect-mvp","V getMvpPresenter");
        return mProxy.getMvpPresenter();
    }
}
