package com.baselib.ui.mvp.view;

import com.baselib.net.reqApi.model.IModel;
import com.baselib.ui.activity.BaseActivity;

import io.reactivex.FlowableTransformer;

/**
 * @date 2017/11/17
 * @description 所有View层接口的基类
 */
public interface BaseMvpView {
    /***针对界面View的***/
    void showLoading();
    void showSuccess();
    void showError(Throwable error);
    void showEmpty();

    /***针对进度条ProgressBar的***/
    void showProgressBar();
    void hideProgressBar();

    <T> FlowableTransformer<T, T> bindToLife();

    <T extends IModel> FlowableTransformer<T, T> bindToLifeAndReqApi();

    BaseActivity getActivity();
}
