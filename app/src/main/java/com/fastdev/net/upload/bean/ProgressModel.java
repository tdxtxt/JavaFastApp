package com.fastdev.net.upload.bean;

/**
 * @作者： ton
 * @创建时间： 2018\9\11 0011
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class ProgressModel {
    //当前读取字节长度
    public long currentBytes;
    //总字节长度
    public long contentLength;
    //是否读取完成
    public boolean done;

    public ProgressModel(long currentBytes, long contentLength, boolean done){
        this.currentBytes = currentBytes;
        this.contentLength = contentLength;
        this.done = done;
    }
}
