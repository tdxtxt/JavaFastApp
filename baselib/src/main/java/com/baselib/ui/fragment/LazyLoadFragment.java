package com.baselib.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * @作者： ton
 * @创建时间： 2018\5\10 0010
 * @功能描述： Viewpager + Fragment情况下，fragment的生命周期因Viewpager的缓存机制而失去了具体意义,该抽象类自定义一个新的回调方法，当fragment可见状态改变时会触发的回调方法
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public abstract class LazyLoadFragment extends BaseFragment {
    // mark  ：to judge fragment if is init complete
    // 标志位 ：用以判断"初始化"动作是否完成
    protected boolean isPrepared;
    // mark  ： to judge fragment if is visible
    // 标志位 ：用以判断当面是否可见
    protected boolean isVisible;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ready();
    }

    /**
     * en ：to judge if is visible to user
     * zh ：判断Fragment是否对用户显示
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            this.isVisible = true;
            onVisible();
        } else {
            this.isVisible = false;
            onInvisible();
        }
    }

    /**
     * en : when the  fragment is not visible to user
     * zh : 当Fragment不显示的时调用
     */
    protected void onInvisible() {
    }

    /**
     * en : when the fragment is visible to user lazyload
     * zh : 当Fragment显示的时候调用
     */
    protected void onVisible() {
        // 如果没初始化好或者不可见，则return
        if (isPrepared && isVisible) {
            lazyLoad();
        }
    }

    /**
     * en : when the fragment init is complete
     * zh : 当Fragment初始化完成后调用
     */
    protected void ready() {
        isPrepared = true;
        if (isVisible) {
            lazyLoad();
        }
    }

    /**
     * en : when fragment init is complete and is visible
     * zh : 初始化完成 且 页面可见，则执行本方法
     */
    protected abstract void lazyLoad();
}

