package com.fastdev.bean.share;

import android.os.Environment;
import android.text.TextUtils;
import com.fastdev.net.ApiClient;

/**
 * @作者： ton
 * @创建时间： 2018\9\18 0018
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class ShareEntity {
    public String url;
    public String remoteImage;//远程的图片路径,优先级高于本地的图片路径
    public String localImage;//本地图片
    public String title;
    public String content;

    public ShareEntity(String title, String content, String image, String url) {
        this.url = addHost(url);
        this.title = title;
        this.remoteImage = addHost(image);
        this.content = content;
    }

    public ShareEntity(String title, String content, String image, String url, String wxTitle, String wxContent, String wxUrl) {
        this.url = addHost(url);
        this.title = title;
        this.remoteImage = addHost(image);
        this.content = content;
        if (TextUtils.isEmpty(wxTitle)) wxTitle = title;
        if (TextUtils.isEmpty(wxContent)) wxContent = content;
        if (TextUtils.isEmpty(wxUrl)) wxUrl = url;
    }

    public ShareEntity() {
    }

    public ShareEntity setUrl(String url) {
        this.url = addHost(url);
        return this;
    }

    public ShareEntity setImage(String image) {
        this.remoteImage = addHost(image);
        return this;
    }

    public ShareEntity setImagePath(String path) {
        this.localImage = addHost(path);
        return this;
    }

    public ShareEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public ShareEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    //处理链接不全的情况
    protected static String addHost(String url) {
        if (TextUtils.isEmpty(url)) return "";
        if (url.startsWith("http")) return url;
        // 加载SD卡根目录的test.jpg 图片,例如：file:///android_asset/jdfw.gif
        // "file://"+ Environment.getExternalStorageDirectory().getPath()+"/test.jpg";
        if (url.startsWith("file://")) return url;
        if (url.contains(Environment.getExternalStorageDirectory().getPath())) return url;
        String host = ApiClient.getHost1();
        if (TextUtils.isEmpty(host)) return url;
        StringBuffer subHost = new StringBuffer(host);
        StringBuffer subUrl = new StringBuffer(url);
        if (subUrl.toString().startsWith("/")) subUrl.deleteCharAt(0);
        subHost.append(subUrl);
        return subHost.toString();
    }

    @Override
    public String toString() {
        return title + (!TextUtils.isEmpty(this.title) && !TextUtils.isEmpty(this.content) ? "\n" : "") + content + (!TextUtils.isEmpty(this.content) && !TextUtils.isEmpty(this.url) ? "\n" : "") + url;
    }
}
