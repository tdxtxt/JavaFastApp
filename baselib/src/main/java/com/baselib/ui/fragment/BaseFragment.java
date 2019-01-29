package com.baselib.ui.fragment;

import android.support.v4.app.Fragment;

import com.baselib.net.reqApi.model.IModel;
import com.baselib.ui.view.IView;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.FlowableTransformer;

/**
 * @作者： ton
 * @创建时间： 2018\12\10 0010
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public abstract class BaseFragment extends RxFragment implements IView{
    @Override
    public void hideProgressBar() {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void showContentView() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showCustomView() {

    }

    @Override
    public void showErrorView(Throwable e) {

    }

    @Override
    public <T> FlowableTransformer<T, T> bindToLife() {
        return null;
    }

    @Override
    public <T extends IModel> FlowableTransformer<T, T> bindToLifeAndReqApi() {
        return null;
    }

    @Override
    public <T> FlowableTransformer<T, T> bindToLifeAndIgnoreError() {
        return null;
    }
}
