package com.baselib.ui.mvp.factory;


import com.baselib.ui.mvp.presenter.BaseMvpPresenter;
import com.baselib.ui.mvp.view.BaseMvpView;

/**
 * @date 2017/11/17
 * @description Presenter工厂接口
 */
public interface PresenterMvpFactory<V extends BaseMvpView,P extends BaseMvpPresenter<V>> {

    /**
     * 创建Presenter的接口方法
     * @return 需要创建的Presenter
     */
    P createMvpPresenter();
}
