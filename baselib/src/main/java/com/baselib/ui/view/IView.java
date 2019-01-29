package com.baselib.ui.view;

import com.baselib.net.reqApi.model.IModel;
import io.reactivex.FlowableTransformer;

/**
 * @作者： ton
 * @创建时间： 2019\1\10 0010
 * @功能描述： 普通view的操作接口
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public interface IView {
    void hideProgressBar();
    void showProgressBar();

    void showToast(String message);

    void showLoadingView();
    void showContentView();
    void showEmptyView();
    void showCustomView();
    void showErrorView(Throwable e);


    <T> FlowableTransformer<T, T> bindToLife();
    <T extends IModel> FlowableTransformer<T, T> bindToLifeAndReqApi();
    <T> FlowableTransformer<T, T> bindToLifeAndIgnoreError();

}
