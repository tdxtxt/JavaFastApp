package com.fastdev.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.baselib.helper.ToastHelper;
import com.baselib.ui.activity.CommToolBarActivity;
import com.fastdev.ton.R;


import butterknife.OnClick;

public class ForResultActivity extends CommToolBarActivity {

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void getParams(Bundle bundle) {
       ToastHelper.showToast( bundle.getString("pamas","没有获取到参数"));
    }

    @OnClick(R.id.btn_show)
    public void clickView(View view){
        switch (view.getId()){
            case R.id.btn_show:
                setResult(Activity.RESULT_OK,getIntent().putExtra("tex","ssss"));
                finish();
                break;
        }
    }
}
