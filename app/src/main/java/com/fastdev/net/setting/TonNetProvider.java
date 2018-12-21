package com.fastdev.net.setting;

import com.baselib.net.reqApi.error.NetError;
import com.baselib.net.reqApi.okhttpconfig.BaseNetProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @作者： ton
 * @创建时间： 2018\12\21 0021
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class TonNetProvider extends BaseNetProvider{
    /**
     * @return false:表示错误依然向页面传递；true:表示错误被拦截
     */
    @Override
    public boolean handleError(NetError error) {
        if(error.getType() == NetError.AuthError){
            //请跳转到登录页面
            return true;
        }
        return false;
    }

    @Override
    public Interceptor[] configInterceptors() {
        return new Interceptor[]{new ParamsInterceptor(),new HeadInterceptor(),new ResponseInterceptor()};
    }

    /**
     * 统一添加公共参数
     */
    static class ParamsInterceptor implements Interceptor{
        Map<String, String> params = new HashMap<>();
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request oldRequest = chain.request();
            if(!(params != null && params.size() > 0)) return chain.proceed(oldRequest);
            Request.Builder newRequestBuilder = oldRequest.newBuilder();
            if ("GET".equalsIgnoreCase(oldRequest.method())) {
                HttpUrl.Builder httpUrlBuilder = oldRequest.url().newBuilder();
                httpUrlBuilder.addQueryParameter("key", "value");

                newRequestBuilder.url(httpUrlBuilder.build());
            }else{
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                formBodyBuilder.add("key", "value");
                FormBody oldFormBody = (FormBody) oldRequest.body();
                if(oldFormBody != null && oldFormBody.size() > 0){
                    for (int i = 0; i < oldFormBody.size(); i++) {
                        formBodyBuilder.add(oldFormBody.name(i), oldFormBody.value(i));
                    }
                }
                newRequestBuilder.post(formBodyBuilder.build());
            }
            return chain.proceed(newRequestBuilder.build());
        }
    }

    /**
     * 统一添加head头
     */
    static class HeadInterceptor implements Interceptor{
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request oldRequest = chain.request();
            Request.Builder newRequestBuilder = oldRequest.newBuilder();
            newRequestBuilder.addHeader("key","value");
            return chain.proceed(newRequestBuilder.build());
        }
    }

    /**
     * 统一处理返回body数据
     */
    static class ResponseInterceptor implements Interceptor{
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            String cookie = response.header("Set-Cookie");
            return response;
        }
    }


}
