package com.baselib.net.reqApi.okhttpconfig;

import com.baselib.net.reqApi.error.NetError;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * @作者： ton
 * @创建时间： 2018\12\17 0017
 * @功能描述： 回调处理
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public interface NetProvider {

    Interceptor[] configInterceptors();

    void configHttps(OkHttpClient.Builder builder);

    CookieJar configCookie();

    RequestHandler configHandler();

    long configConnectTimeoutSecs();

    long configReadTimeoutSecs();

    long configWriteTimeoutSecs();

    boolean configLogEnable();

    boolean handleError(NetError error);

    NetError convertError(Throwable throwable);

}
