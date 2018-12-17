package com.baselib.ui.statusview;

import com.baselib.R;
import com.kingja.loadsir.callback.Callback;

/**
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2017/11/24 17:28
 * 传参：
 * 返回:
 */
public class EmptyCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.baselib_layout_empty;
    }
}
