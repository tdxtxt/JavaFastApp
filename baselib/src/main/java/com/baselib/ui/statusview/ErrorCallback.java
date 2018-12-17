package com.baselib.ui.statusview;

import android.content.Context;
import android.view.View;

import com.baselib.R;
import com.kingja.loadsir.callback.Callback;

/**
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2017/11/24 10:55
 * 传参：
 * 返回:
 */
public class ErrorCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.baselib_layout_error;
    }
    //当前Callback的点击事件，如果返回true则覆盖注册时的onReloa()，如果返回false则两者都执行，先执行onReloadEvent()。
    @Override
    protected boolean onReloadEvent(final Context context, View view) {
//        Toast.makeText(context.getApplicationContext(), "Hello buddy! :p", Toast.LENGTH_SHORT).show();
//        (view.findViewById(R.id.btn_retry)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context.getApplicationContext(), "It's your gift! :p", Toast.LENGTH_SHORT).show();
//            }
//        });
        return false;
    }

    //是否在显示Callback视图的时候显示原始图(SuccessView)，返回true显示，false隐藏
    @Override
    public boolean getSuccessVisible() {
        return super.getSuccessVisible();
    }

    //将Callback添加到当前视图时的回调，View为当前Callback的布局View
    @Override
    public void onAttach(Context context, View view) {
        super.onAttach(context, view);
    }

    //将Callback从当前视图删除时的回调，View为当前Callback的布局View
    @Override
    public void onDetach() {
        super.onDetach();
    }
}
