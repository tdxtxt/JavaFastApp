package com.fastdev.ui;

import android.content.Intent;
import android.view.View;

import com.baselib.helper.HashMapParams;
import com.baselib.helper.ToastHelper;
import com.baselib.ui.activity.callback.StartForResultListener;
import com.baselib.ui.fragment.BaseFragment;
import com.fastdev.ton.R;
import com.fastdev.ui.activity.ForResultActivity;

import butterknife.OnClick;

public class ForResultFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.btn_show)
    public void clickView(View view){
        switch (view.getId()){
            case R.id.btn_show:
                startActivityForResult(ForResultActivity.class, new HashMapParams().add("pamas", "3333"), new StartForResultListener() {
                    @Override
                    public void onActivityResult(int requestCode, int resultCode, Intent data) {
                        ToastHelper.showToast(data.getStringExtra("tex") == null ? "没有获取到返回结果" :
                                data.getStringExtra("tex"));
                    }
                });
                break;
        }
    }
}
