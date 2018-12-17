package com.baselib.ui.statusview;

import com.baselib.R;
import com.kingja.loadsir.callback.Callback;

/**
 * @作者： ton
 * @创建时间： 2018\9\12 0012
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class CustomCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.baselib_layout_custom;
    }
}

