package com.fastdev.net.upload.listener;

/**
 * @作者： ton
 * @创建时间： 2018\9\10 0010
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public interface ProgressRequestListener {
    /**
     * 请求体进度回调接口，用于文件上传进度回调
     */
    void onRequestProgress(long bytesWritten, long contentLength, boolean done);
}
