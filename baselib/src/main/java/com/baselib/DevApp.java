package com.baselib;

import android.app.Application;
import android.content.Context;

import com.baselib.cache.CacheHelper;
import com.baselib.helper.ImageLoadHelper;
import com.baselib.helper.LogA;
import com.baselib.ui.statusview.CustomCallback;
import com.baselib.ui.statusview.EmptyCallback;
import com.baselib.ui.statusview.ErrorCallback;
import com.baselib.ui.statusview.LoadingCallback;
import com.kingja.loadsir.callback.SuccessCallback;
import com.kingja.loadsir.core.LoadSir;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

/**
 * @作者： ton
 * @创建时间： 2018\12\17 0017
 * @功能描述： 需在application中调用onCreate方法
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public abstract class DevApp {
    protected static Context mContext;

    public void onCreate(Application app) {
        mContext = app;
        initStatusView();
        LogA.init(isLoggable());
        CacheHelper.init(app);
        ImageLoadHelper.init(app);
    }

    public abstract boolean isLoggable();

    public static Context getContext(){
        return mContext;
    }

    private void initStatusView(){
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())//添加各种状态页
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(SuccessCallback.class)//设置默认状态页
                .commit();
    }
    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new MaterialHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    public static String getString(int resId){
        return DevApp.getContext().getString(resId);
    }
}
