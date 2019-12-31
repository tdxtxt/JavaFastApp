package com.baselib.helper;

import android.content.Context;
import android.widget.ImageView;

import com.baselib.image.ImageLoader;
import com.baselib.image.ImageLoaderOptions;
import com.baselib.image.core.glide.GlideImageLoader;

/**
 * @作者： ton
 * @创建时间： 2018\12\21 0021
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class ImageLoadHelper {
    public static ImageLoader mImageLoader;
    public static void init(Context context){
        ImageLoaderOptions options = new ImageLoaderOptions.Builder().isLogEnable(false).isCache(true).build();
        mImageLoader = new GlideImageLoader(context, options);
    }

    /**
     * 加载图片：统一调用入口
     */
    public static void displayImage(ImageView imgView,String url){
        if(imgView == null) return;
        mImageLoader.loadImage(imgView,url);
    }

}
