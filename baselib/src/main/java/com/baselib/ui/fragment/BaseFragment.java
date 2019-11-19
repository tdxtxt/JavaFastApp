package com.baselib.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baselib.helper.HashMapParams;
import com.baselib.net.reqApi.model.IModel;
import com.baselib.ui.activity.callback.StartForResultListener;
import com.baselib.ui.view.IView;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.FlowableTransformer;

/**
 * @作者： ton
 * @创建时间： 2018\12\10 0010
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public abstract class BaseFragment extends RxFragment implements IView{
    protected View mRootView;
    private Unbinder unbinder;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParams(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    protected abstract int getLayoutId();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null) unbinder.unbind();
    }

    protected void getParams(Bundle bundle){}

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


    public void startActivity(Class clazz, HashMapParams params){
        Intent intent = new Intent(getActivity(),clazz);
        intent.putExtra("Bundle",params.toBundle());
        startActivity(intent);
    }

    StartForResultFragment  mFragment;
    public void startActivityForResult(Class clazz, HashMapParams params, StartForResultListener listener){
        mFragment = (StartForResultFragment) getFragmentManager().findFragmentByTag("__start_for_result");
        if (mFragment == null) {
            mFragment = new StartForResultFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(mFragment, "__start_for_result")
                    .commitAllowingStateLoss();//避免数据保存和恢复导致的crash
            getFragmentManager().executePendingTransactions();
        }
        mFragment.setListener(listener);
        Intent intent = new Intent(getActivity(),clazz);
        intent.putExtra("Bundle",params.toBundle());
        mFragment.startActivityForResult(intent, 12);
    }

    /**
     * 获取fragment依赖的activity
     */
    public <T extends Activity> T getActivityNew(){
        return (T) getActivity();
    }

    public static <T extends BaseFragment> T newInstance(Class<T> clazz){
        return newInstance(clazz,null);
    }

    public static <T extends BaseFragment> T newInstance(Class<T> clazz, HashMapParams params){
        T instance = null;
        if(clazz != null) {
            try {
                instance = clazz.newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if(params != null){
            instance.setArguments(params.toBundle());
        }
        return instance;
    }
}
