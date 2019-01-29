package com.fastdev.net.upload.listener;

/**
 * @作者： ton
 * @创建时间： 2018\9\10 0010
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public interface ProgressResponseListener {
    /**
     * 响应体进度回调接口，用于文件下载进度回调
     */
    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}
