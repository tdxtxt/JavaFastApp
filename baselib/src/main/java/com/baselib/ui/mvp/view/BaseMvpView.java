package com.baselib.ui.mvp.view;

import com.baselib.ui.activity.BaseActivity;
import com.baselib.ui.view.IView;

/**
 * @date 2017/11/17
 * @description 所有View层接口的基类
 */
public interface BaseMvpView extends IView {
    BaseActivity getActivity();
}
