package com.fastdev.net.upload.listener;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.fastdev.net.upload.bean.ProgressModel;
import java.lang.ref.WeakReference;

/**
 * @作者： ton
 * @创建时间： 2018\9\11 0011
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public abstract class UIProgressRequestListener implements ProgressRequestListener{
    private static final int REQUEST_UPDATE = 0x01;

    //处理UI层的Handler子类
    private static class UIHandler extends Handler {
        //弱引用
        private final WeakReference<UIProgressRequestListener> mUIProgressRequestListenerWeakReference;

        public UIHandler(Looper looper, UIProgressRequestListener uiProgressRequestListener) {
            super(looper);
            mUIProgressRequestListenerWeakReference = new WeakReference<UIProgressRequestListener>(uiProgressRequestListener);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_UPDATE:
                    UIProgressRequestListener uiProgressRequestListener = mUIProgressRequestListenerWeakReference.get();
                    if (uiProgressRequestListener != null) {
                        //获得进度实体类
                        ProgressModel progressModel = (ProgressModel) msg.obj;
                        //回调抽象方法
                        uiProgressRequestListener.onUIRequestProgress(progressModel.currentBytes, progressModel.contentLength, progressModel.done);
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
    //主线程Handler
    private final Handler mHandler = new UIHandler(Looper.getMainLooper(), this);
    private int delayMillis = 0;
    @Override
    public void onRequestProgress(long bytesRead, long contentLength, boolean done) {
        //通过Handler发送进度消息
        Message message = Message.obtain();
        message.obj = new ProgressModel(bytesRead, contentLength, done);
        message.what = REQUEST_UPDATE;
        mHandler.sendMessageDelayed(message,delayMillis);
        delayMillis += 50;
    }

    public void removeCallback(){
        //在activity退出时，移除所有信息
        if(mHandler != null) mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * UI层回调抽象方法
     * @param bytesWrite 当前写入的字节长度
     * @param contentLength 总字节长度
     * @param done 是否写入完成
     */
    public abstract void onUIRequestProgress(long bytesWrite, long contentLength, boolean done);
}
