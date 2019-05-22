package com.fastdev.app;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.baselib.DevApp;
import com.baselib.helper.ActStackHelper;
import com.baselib.helper.DialogHelper;
import com.baselib.helper.DrawableHelper;
import com.baselib.helper.OpenOsActHelper;
import com.baselib.helper.ToastHelper;
import com.baselib.helper.VersionHelper;
import com.baselib.net.reqApi.NetMgr;
import com.baselib.ui.dialog.callback.MenuDialogCallBack;
import com.baselib.ui.dialog.child.ProgressDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fastdev.bean.ResponseBody;
import com.fastdev.bean.update.Market;
import com.fastdev.bean.update.UpgradeInfo;
import com.fastdev.helper.AppHelper;
import com.fastdev.helper.FastAppCache;
import com.fastdev.helper.ShareHelper;
import com.fastdev.net.ApiClient;
import com.fastdev.net.setting.TonNetProvider;
import com.fastdev.ton.BuildConfig;
import com.fastdev.ton.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.base.CheckNotifier;
import org.lzh.framework.updatepluginlib.base.DownloadCallback;
import org.lzh.framework.updatepluginlib.base.DownloadNotifier;
import org.lzh.framework.updatepluginlib.base.FileChecker;
import org.lzh.framework.updatepluginlib.base.InstallNotifier;
import org.lzh.framework.updatepluginlib.base.UpdateChecker;
import org.lzh.framework.updatepluginlib.base.UpdateParser;
import org.lzh.framework.updatepluginlib.base.UpdateStrategy;
import org.lzh.framework.updatepluginlib.impl.DefaultInstallStrategy;
import org.lzh.framework.updatepluginlib.model.CheckEntity;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.util.SafeDialogHandle;

import java.io.File;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.net.ssl.SSLException;

/**
 * @作者： ton
 * @创建时间： 2018\12\18 0018
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class TondxApp extends DevApp{
    /**
     * 单例
     */
    public static TondxApp getInstance() {
        return TondxApp.InstanceHolder.instance;
    }
    static class InstanceHolder {
        final static TondxApp instance = new TondxApp();
    }

    @Override
    public void onCreate(Application app) {
        super.onCreate(app);
        if(ActStackHelper.isMainProcess(app)){
            //设置Host
            settingHost();
            //设置前后台
            settingForeground(app);
            //更新插件的配置
            settingUpdate();
            //设置网络框架
            NetMgr.getInstance().registerProvider(new TonNetProvider());
            //设置分享
            ShareHelper.initSDK(app);
        }
    }

    private void settingHost() {
        if(!BuildConfig.isDevelopTool){
            String host = FastAppCache.getHost1();
            //如果host为开发服或者测试服或者国内预发布服或者国外预发布服，则更正为使用正式服
            if(host.equals(BuildConfig.HOST1_DEVE) ||
                    host.equals(BuildConfig.HOST1_TEST) ||
                    host.equals(BuildConfig.HOST1_RELEASE_PRE)){
                FastAppCache.putHost1(BuildConfig.HOST1);
            }
        }
        //设置新的
        ApiClient.changeHost1(FastAppCache.getHost1());
    }
    private void settingForeground(Application app) {
        ForegroundCallbacks.init(app)
                .addListener(new ForegroundCallbacks.Listener() {
                    @Override
                    public void onBecameForeground() {}
                    @Override
                    public void onBecameBackground() {}
                });
    }
    private void settingUpdate() {
        UpdateConfig.getConfig()
                .setCheckEntity(new CheckEntity().setUrl("http://www.test.api/common/version"))
                .setUpdateParser(new UpdateParser() {
                    @Override
                    public Update parse(String response) throws Exception {
                        // 需要在此对response数据进行解析，并创建出对应的update实体类数据
                        // 提供给框架内部进行使用
                        Gson gson = new GsonBuilder().create();
                        ResponseBody<UpgradeInfo> body = gson.fromJson(response, new TypeToken<ResponseBody<UpgradeInfo>>(){}.getType());
                        return body.data;
                    }
                })
                .setUpdateChecker(new UpdateChecker() {//更新检查机制，返回ture表示更新,false不更新
                    @Override
                    public boolean check(Update update) throws Exception {
                        //使用本地数据与update实体类数据进行比对。判断当前版本是否需要更新
                        return update.getVersionCode() > VersionHelper.getApkVersionCode();
                    }
                })
                .setUpdateStrategy(new UpdateStrategy() {//更新策略
                    @Override
                    public boolean isShowUpdateDialog(Update update) {
                        return true;
                    }
                    @Override
                    public boolean isShowDownloadDialog() {
                        return true;
                    }
                    @Override
                    public boolean isAutoInstall() {
                        return false;
                    }
                })
                .setCheckNotifier(new CheckNotifier() {//更新弹框
                    @Override
                    public Dialog create(final Activity context) {
                        return DialogHelper.createCommDialog(context,"版本更新",update.getUpdateContent(),!update.isForced(),false,
                                update.isForced() ? null : new MenuDialogCallBack("取消"){
                                    @Override
                                    public void onClick(View customView, Dialog dialog) {
                                        dialog.dismiss();
                                    }
                                },
                                new MenuDialogCallBack("马上更新"){
                                    @Override
                                    public void onClick(View customView, Dialog dialog) {
                                        // update: 更新数据实体类。activity:顶层Activity
                                        // 当检查到有更新且更新策略为UpdateStrategy.isShowUpdateDialog为true时调用到此
                                        // 当用户点击更新时：调用super.sendDownloadRequest(update)继续更新任务
                                        // 当用户点击取消时：调用super.sendUserCancel()取消任务
                                        // 当用户点击忽略时：调用super.sendUserIgnore(update)设置忽略此版本更新操作
                                        //本地下载,策略：再弹出一个下载渠道选择框（至少包含本地下载按钮）
                                        final List<Market> markets = AppHelper.getAppStores();
                                        markets.add(new Market(DrawableHelper.getDrawable(R.mipmap.ic_launcher),"直接下载","")
                                                .setCustomTag("nativeDownload"));

                                        DialogHelper.createCustomViewDialog(context, true, new RecyclerView(context), new DialogHelper.LifecycleListener() {
                                            @Override
                                            public void onCreate(View view, Dialog dialog) {
                                                RecyclerView recyclerView = (RecyclerView) view;
                                                recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),2));
                                                final BaseQuickAdapter<Market,BaseViewHolder> adapter;
                                                recyclerView.setAdapter(adapter = new BaseQuickAdapter<Market, BaseViewHolder>(R.layout.item_market,markets) {
                                                    @Override
                                                    protected void convert(BaseViewHolder helper, Market item) {
                                                        if(item != null && item.drawable != null){
                                                            helper.setImageDrawable(R.id.iv_icon,item.drawable);
                                                        }else{
                                                            helper.setImageResource(R.id.iv_icon,R.mipmap.ic_launcher);
                                                        }
                                                        if(item != null && !TextUtils.isEmpty(item.name)) helper.setText(R.id.tv_name,item.name);
                                                    }
                                                });
                                                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(BaseQuickAdapter adapte, View view, int position) {
                                                        Market item = adapter.getItem(position);
                                                        if("nativeDownload".equals(item.customTag)){//本地下载更新
                                                            if("1".equals(((UpgradeInfo)update).status)){//浏览器下载
                                                                sendUserCancel();
                                                                OpenOsActHelper.openBrowser(update.getUpdateUrl());
                                                            }else{//自下载
                                                                sendDownloadRequest();
                                                            }
                                                        }else{//其他市场更新下载
                                                            sendUserCancel();
                                                            OpenOsActHelper.openAppStore(item.packageName,getContext().getPackageName());
                                                        }
                                                    }
                                                });
                                            }
                                        }).show();
                                        if(!update.isForced()) dialog.dismiss();
                                    }
                                });
                    }
                })
                .setFileChecker(new FileChecker() {
                    @Override
                    protected boolean onCheckBeforeDownload() throws Exception {
                        // 当FileCreator接口所创建的文件存在时，在检查到有更新且在启动下载任务前。触发检查
                        // 返回true:检查成功。跳过下载任务并继续后续任务. 返回false:检查失败。执行下载任务并继续后续任务
                        //例如：可对比md5或者版本号
                        return true;
                    }

                    @Override
                    protected void onCheckBeforeInstall() throws Exception {
                        // 在调用安装apk的api之前，触发检查。
                        // 不抛出异常: 检查成功，调用安装api进行操作。
                        // 抛出异常: 检查失败。此次更新任务失败。通知到回调方法。
                    }
                })
                .setDownloadNotifier(new DownloadNotifier() {
                    @Override
                    public DownloadCallback create(Update update, Activity activity) {
                        final ProgressDialog dialog = DialogHelper.createProgressDialog(activity,"版本更新","正在下载，请稍候...",false);
                        return new DownloadCallback() {
                            @Override
                            public void onDownloadStart() {
                                dialog.show();
                            }
                            @Override
                            public void onDownloadProgress(long current, long total) {
                                int percent = (int) (current * 1.0f / total * 100);
                                dialog.setProgressValue(percent);
                            }
                            @Override
                            public void onDownloadComplete(File file) {
                                dialog.dismiss();
                            }
                            @Override
                            public void onDownloadError(Throwable t) {
                                if(t instanceof UnknownHostException){//一开始网络就断开
                                    ToastHelper.showToast("网络出错，请检查");
                                }else if(t instanceof SocketTimeoutException){//超时
                                    ToastHelper.showToast("网络超时，请检查");
                                }else if(t instanceof SocketException || t instanceof SSLException){//链接过程中网络断开
                                    ToastHelper.showToast("网络未连接");
                                }
                                SafeDialogHandle.safeDismissDialog(dialog.getDialog());
                            }
                        };
                    }
                })
                .setInstallNotifier(new InstallNotifier() {
                    @Override
                    public Dialog create(Activity activity) {
                        // update: 更新实体类。 path: apk文件路径， activity：顶层Activity。用于操作UI。
                        // 调用时机：当新版apk下载完成后且更新策略UpdateStrategy.isAutoInstall()为false时。调用到此。
                        // 返回一个Dialog。提示用户下载完成可直接更新。
                        // 当用户点击需要安装时：调用super.sendToInstall(path)
                        // 当用户点击取消安装时：调用super.sendUserCancel()
                        // 当用户点击忽略此版本更新时：调用super.sendCheckIgnore(update)
                        return DialogHelper.createCommDialog(activity,"安装提示","新版本下载完成,请点击安装",!update.isForced(),!update.isForced(),
                                null,
                                new MenuDialogCallBack("安装"){
                                    @Override
                                    public void onClick(View customView, Dialog dialog) {
                                        sendToInstall();
                                    }
                                });
                    }
                })
                .setInstallStrategy(new DefaultInstallStrategy());
    }
    @Override
    public boolean isLoggable() {
        return false;
    }
}
