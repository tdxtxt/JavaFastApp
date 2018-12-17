package com.baselib.net.reqApi.okhttpconfig;


import android.text.TextUtils;

import com.baselib.net.reqApi.error.NetError;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import org.json.JSONException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.adapter.rxjava2.HttpException;

/**
 * 界面描述：NetProvider实现类
 * <p>
 * Created by tianyang on 2017/9/27.
 */

public class BaseNetProvider implements NetProvider {

    private static final long CONNECT_TIME_OUT = 30;
    private static final long READ_TIME_OUT = 180;
    private static final long WRITE_TIME_OUT = 30;
    private static final boolean isLog = true;


    @Override
    public Interceptor[] configInterceptors() {
        return null;
    }

    @Override
    public void configHttps(OkHttpClient.Builder builder) {

    }

    @Override
    public CookieJar configCookie() {
        return null;
    }

    @Override
    public RequestHandler configHandler() {
        return new HeaderHandler();
    }

    @Override
    public long configConnectTimeoutSecs() {
        return CONNECT_TIME_OUT;
    }

    @Override
    public long configReadTimeoutSecs() {
        return READ_TIME_OUT;
    }

    @Override
    public long configWriteTimeoutSecs() {
        return WRITE_TIME_OUT;
    }

    @Override
    public boolean configLogEnable() {
        return isLog;
    }

    @Override
    public boolean handleError(NetError error) {
        if(NetError.AuthError == error.getType()){
            //跳转到登录界面
            return true;//ture表示消耗事件
        }
        //这里处理共有的错误
        return false;
    }
    @Override
    public NetError convertError(Throwable e) {
        NetError error = null;
        if (e != null) {
            if (!(e instanceof NetError)) {
                if (e instanceof UnknownHostException
                        || e instanceof ConnectException) {//断网&主机错误
                    error = new NetError(e, NetError.NoConnectError);
                } else if (e instanceof JSONException
                        || e instanceof JsonParseException
                        || e instanceof JsonSyntaxException) {
                    error = new NetError(e, NetError.ParseError);
                } else if(e instanceof HttpException
                        || e instanceof retrofit2.HttpException){
                    error = new NetError(e,NetError.ServiceError);
                } else if (e instanceof TimeoutException) {//超时
                    error = new NetError(e, NetError.TimeOutError);
                } else {
                    error = new NetError(e, NetError.UnknownError);
                }
            } else {
                error = (NetError) e;
            }
        }
        return error;
    }

    private class HeaderHandler implements RequestHandler {
        @Override
        public Response onAfterRequest(Response response, Interceptor.Chain chain)
                throws IOException {
//            ApiException e = null;
            if (401 == response.code()) {
//                throw new ApiException("登录已过期,请重新登录!");
            } else if (403 == response.code()) {
//                throw new ApiException("禁止访问!");
            } else if (404 == response.code()) {
//                throw new ApiException(TextUtils.isEmpty(response.message()) ? "链接错误" : response.message());
            } else if (503 == response.code()) {
//                throw new ApiException("服务器升级中!");
            }/* else if (500 == response.code()) {
                throw new ApiException("服务器内部错误!");
            }*/
            return response;
        }
        @Override
        public Request onBeforeRequest(Request request, Interceptor.Chain chain) {
            return chain.request().newBuilder()
                    .addHeader("Authorization", "tondx")
                    .build();
        }
    }

}
