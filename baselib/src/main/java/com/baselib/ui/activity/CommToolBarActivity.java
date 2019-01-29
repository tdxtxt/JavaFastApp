package com.baselib.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.baselib.R;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import butterknife.BindView;

/**
 * @作者： ton
 * @创建时间： 2018\12\26 0026
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public abstract class CommToolBarActivity extends BaseActivity{
    private CommonTitleBar mTitlebar;

    /**
     * 布局中CommonTitleBar控件id默认R.id.titlebar，若自定义id，需要重写getToolBarResId方法
     */
    public int getToolBarResId(){
        return R.id.titlebar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitlebar = getTitleBar();
        mTitlebar.showStatusBar(true);
//        mTitlebar.setStatusBarColor(Color.WHITE);
        setTitleBarListener();
    }

    protected void setTitleBarListener(){
        getTitleBar().setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_BUTTON) {//点击返回键
                    if(!interceptBackEvent()) finish();
                }else{
                    setTitleBarClickListener(v,action,extra);
                }
                // CommonTitleBar.ACTION_LEFT_TEXT;        // 左边TextView被点击
                // CommonTitleBar.ACTION_LEFT_BUTTON;      // 左边ImageBtn被点击
                // CommonTitleBar.ACTION_RIGHT_TEXT;       // 右边TextView被点击
                // CommonTitleBar.ACTION_RIGHT_BUTTON;     // 右边ImageBtn被点击
                // CommonTitleBar.ACTION_SEARCH;           // 搜索框被点击,搜索框不可输入的状态下会被触发
                // CommonTitleBar.ACTION_SEARCH_SUBMIT;    // 搜索框输入状态下,键盘提交触发，此时，extra为输入内容
                // CommonTitleBar.ACTION_SEARCH_VOICE;     // 语音按钮被点击
                // CommonTitleBar.ACTION_SEARCH_DELETE;    // 搜索删除按钮被点击
                // CommonTitleBar.ACTION_CENTER_TEXT;      // 中间文字点击
            }
        });
    }

    public void setTitleBarClickListener(View v, int action, String extra){}
    public void setTitle(int resId){
        if(getTitleBar().getCenterTextView() != null)  getTitleBar().getCenterTextView().setText(resId);
    }
    public void setRightText(String menuTxt){
        if(getTitleBar().getRightTextView() != null) getTitleBar().getRightTextView().setText(menuTxt);
    }
    public void setRightText(int resId){
        if(getTitleBar().getRightTextView() != null) getTitleBar().getRightTextView().setText(resId);
    }

    public CommonTitleBar getTitleBar(){
        if(mTitlebar == null) mTitlebar = findViewById(getToolBarResId());
        return mTitlebar;
    }
}
