package com.fastdev.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baselib.helper.DeviceHelper;
import com.baselib.helper.ImageLoadHelper;
import com.baselib.ui.activity.BaseActivity;
import com.baselib.ui.activity.CommToolBarActivity;
import com.fastdev.ton.R;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import org.lzh.framework.updatepluginlib.UpdateBuilder;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends CommToolBarActivity {

    @BindView(R.id.iv_test)
    ImageView ivTest;
    @BindView(R.id.tv_content)
    TextView textView;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initUi() {
        getTitleBar().showStatusBar(false);
        textView.setText(DeviceHelper.getDeviceId());
    }

    @OnClick(R.id.btn_show)
    public void clickView(View view){
        switch (view.getId()){
            case R.id.btn_show:
                UpdateBuilder.create().check();
//                ImageLoadHelper.displayImage(ivTest,"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1415088147,1132690104&fm=26&gp=0.jpg");
                break;
        }
    }
}
