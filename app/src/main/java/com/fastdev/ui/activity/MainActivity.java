package com.fastdev.ui.activity;

import android.view.View;
import android.widget.ImageView;

import com.baselib.helper.ImageLoadHelper;
import com.baselib.ui.activity.BaseActivity;
import com.fastdev.ton.R;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.iv_test)
    ImageView ivTest;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.btn_show)
    public void clickView(View view){
        switch (view.getId()){
            case R.id.btn_show:
                ImageLoadHelper.displayImage(ivTest,"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1415088147,1132690104&fm=26&gp=0.jpg");
                break;
        }
    }
}
