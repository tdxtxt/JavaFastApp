package com.baselib.image.core.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.baselib.image.ImageLoader;
import com.baselib.image.ImageLoaderOptions;
import com.baselib.image.listener.IListener;
import com.baselib.image.progress.ProgressInterceptor;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/6/27
 *     desc   : Glide图片加载器
 *              https://github.com/bumptech/glide/
 *              https://github.com/VeiZhang/ImageLoader
 * </pre>
 */
public final class GlideImageLoader implements ImageLoader {
    private Context mContext = null;
    private ImageLoaderOptions mOptions = null;

    public GlideImageLoader(Context context) {
        this(context, null);
    }

    public GlideImageLoader(Context context, ImageLoaderOptions options) {
        mContext = context.getApplicationContext();
        mOptions = options;
        if (mOptions == null) {
            mOptions = new ImageLoaderOptions.Builder().build();
        }
    }

    private void load(@NonNull ImageView view, Object obj, int placeholderResId, int errorResId, IListener listener) {
        if (view == null) return;
        GlideRequests glideRequests = GlideApp.with(mContext);
        GlideRequest<Drawable> glideRequest = glideRequests.load(obj);

        // 全局占位图和错误图
        if (mOptions.mPlaceholderResId != 0) {
            glideRequest.placeholder(mOptions.mPlaceholderResId);
        }
        if (mOptions.mErrorResId != 0) {
            glideRequest.error(mOptions.mErrorResId);
        }

        // 定制的占位图和错误图，同时有全局图时，以定制的为准
        if (placeholderResId != 0) {
            glideRequest.placeholder(placeholderResId);
        }
        if (errorResId != 0) {
            glideRequest.error(errorResId);
        }

        if (!mOptions.isFade) {
            glideRequest.transition(new DrawableTransitionOptions().crossFade());
        }

        if (!mOptions.isCache) {
            // 跳过内存缓存
            glideRequest.skipMemoryCache(true);
            // 跳过磁盘缓存
            glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE);
        }

        final String url = obj.toString();
        ImageLoaderListener imageLoaderListener = new ImageLoaderListener(url, listener);
        ProgressInterceptor.addListener(url, imageLoaderListener);
        glideRequest.listener(imageLoaderListener).into(view);
    }

    @Override
    public void loadImage(@NonNull ImageView view, int resId) {
        loadImage(view, resId, null);
    }

    @Override
    public void loadImage(@NonNull ImageView view, int resId, IListener listener) {
        loadImage(view, resId, 0, 0, listener);
    }

    @Override
    public void loadImage(@NonNull ImageView view, int resId, int placeholderResId, int errorResId) {
        loadImage(view, resId, placeholderResId, errorResId, null);
    }

    @Override
    public void loadImage(@NonNull ImageView view, int resId, int placeholderResId, int errorResId, IListener listener) {
        load(view, resId, placeholderResId, errorResId, listener);
    }

    @Override
    public void loadImage(@NonNull ImageView view, @NonNull File file) {
        loadImage(view, file, null);
    }

    @Override
    public void loadImage(@NonNull ImageView view, @NonNull File file, IListener listener) {
        loadImage(view, file, 0, 0, listener);
    }

    @Override
    public void loadImage(@NonNull ImageView view, @NonNull File file, int placeholderResId, int errorResId) {
        loadImage(view, file, placeholderResId, errorResId, null);
    }

    @Override
    public void loadImage(@NonNull ImageView view, @NonNull File file, int placeholderResId, int errorResId, IListener listener) {
        load(view, file, placeholderResId, errorResId, listener);
    }

    @Override
    public void loadImage(@NonNull ImageView view, @NonNull String url) {
        loadImage(view, url, null);
    }

    @Override
    public void loadImage(@NonNull ImageView view, @NonNull String url, IListener listener) {
        loadImage(view, url, 0, 0, listener);
    }

    @Override
    public void loadImage(@NonNull ImageView view, @NonNull String url, int placeholderResId, int errorResId) {
        loadImage(view, url, placeholderResId, errorResId, null);
    }

    @Override
    public void loadImage(@NonNull ImageView view, @NonNull String url, int placeholderResId, int errorResId, IListener listener) {
        load(view, url, placeholderResId, errorResId, listener);
    }

    @Override
    public void clearCache() {
        Glide.get(mContext).clearMemory();
        Glide.get(mContext).clearDiskCache();
    }

    private class ImageLoaderListener implements RequestListener<Drawable>, IListener {
        private String mUrl = null;
        private IListener mListener = null;

        public ImageLoaderListener(String url, IListener listener) {
            mUrl = url;
            mListener = listener;
        }

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            ProgressInterceptor.removeListener(mUrl);
            onError(e);
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            ProgressInterceptor.removeListener(mUrl);
            onSuccess();
            return false;
        }

        @Override
        public void onProgress(long current, long size) {
            if (mListener != null) {
                mListener.onProgress(current, size);
            }
        }

        @Override
        public void onSuccess() {
            if (mListener != null) {
                mListener.onSuccess();
            }
        }

        @Override
        public void onError(Throwable t) {
            if (mListener != null) {
                mListener.onError(t);
            }
        }
    }
}
