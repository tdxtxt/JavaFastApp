package com.fastdev.net.upload;

import android.os.Handler;

import com.fastdev.net.upload.listener.ProgressRequestListener;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * @作者： ton
 * @创建时间： 2018\9\10 0010
 * @功能描述： 包装的请求体，处理上传进度,参考https://www.loongwind.com/archives/290.html
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class ProgressRequestBody extends RequestBody {
//    Emitter<Integer> emitter;
    Handler handler;
    //实际的待包装请求体
    private  RequestBody requestBody;
    //进度回调接口
    private ProgressRequestListener progressListener;
    //包装完成的BufferedSink
    private BufferedSink bufferedSink;

    /**
     * 构造函数，赋值
     * @param requestBody 待包装的请求体
     * @param progressListener 回调接口
     */
    public ProgressRequestBody(RequestBody requestBody, ProgressRequestListener progressListener) {
        this.requestBody = requestBody;
        this.progressListener = progressListener;
    }

    public ProgressRequestBody(RequestBody requestFile, Handler handler) {
        this.requestBody = requestBody;
        this.handler = handler;
    }

    /**
     * 重写调用实际的响应体的contentType
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    /**
     * 重写进行写入
     * @param sink BufferedSink
     * @throws IOException 异常
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
//            //包装
            bufferedSink = Okio.buffer(sink(sink));
        }
        //写入
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();

    }

    /**
     * 写入，回调进度接口
     * @param sink Sink
     * @return Sink
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;
                //回调
                if(progressListener != null){
                    progressListener.onRequestProgress(bytesWritten, contentLength, bytesWritten == contentLength);
                }
//                if(emitter != null){
//                    emitter.onNext((int) ((float)bytesWritten / (float)contentLength * 100));
//                }
            }
        };
    }
}
