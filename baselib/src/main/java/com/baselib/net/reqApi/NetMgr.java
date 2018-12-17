package com.baselib.net.reqApi;

import com.baselib.net.reqApi.log.HttpLogger;
import com.baselib.net.reqApi.okhttpconfig.NetInterceptor;
import com.baselib.net.reqApi.okhttpconfig.NetProvider;
import com.baselib.net.reqApi.okhttpconfig.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @作者： ton
 * @创建时间： 2018\12\17 0017
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class NetMgr {
    private final long connectTimeoutMills = 10;
    private final long readTimeoutMills = 10;
    private NetProvider sProvider = null;
    private Map<String, NetProvider> providerMap = new HashMap<>();
    private Map<String, Retrofit> retrofitMap = new HashMap<>();
    private Map<String, OkHttpClient> clientMap = new HashMap<>();

    //  创建单例
    private static class SingletonHolder {
        private static final NetMgr INSTANCE = new NetMgr();
    }
    public static NetMgr getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public <S> S get(String baseUrl, Class<S> service) {
        return getInstance().getRetrofit(baseUrl).create(service);
    }

    public void registerProvider(NetProvider provider) {
        this.sProvider = provider;
    }

    public void registerProvider(String baseUrl, NetProvider provider) {
        getInstance().providerMap.put(baseUrl, provider);
    }

    public NetProvider getCommonProvider() {
        return sProvider;
    }

    public void clearCache() {
        getInstance().retrofitMap.clear();
        getInstance().clientMap.clear();
    }

    public Retrofit getRetrofit(String baseUrl) {
        return getRetrofit(baseUrl, null);
    }

    public Retrofit getRetrofit(String baseUrl, NetProvider provider) {
        if (empty(baseUrl)) {
            throw new IllegalStateException("baseUrl can not be null");
        }
        if (retrofitMap.get(baseUrl) != null) {
            return retrofitMap.get(baseUrl);
        }

        if (provider == null) {
            provider = providerMap.get(baseUrl);
            if (provider == null) {
                provider = sProvider;
            }
        }
        checkProvider(provider);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getClient(baseUrl, provider))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit = builder.build();
        retrofitMap.put(baseUrl, retrofit);
        providerMap.put(baseUrl, provider);

        return retrofit;
    }

    private boolean empty(String baseUrl) {
        return baseUrl == null || baseUrl.isEmpty();
    }

    private OkHttpClient getClient(String baseUrl, NetProvider provider) {
        if (empty(baseUrl)) {
            throw new IllegalStateException("baseUrl can not be null");
        }
        if (clientMap.get(baseUrl) != null) {
            return clientMap.get(baseUrl);
        }

        checkProvider(provider);

//        OkHttpClient.Builder builder = RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder());
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(provider.configConnectTimeoutSecs() != 0
                ? provider.configConnectTimeoutSecs()
                : connectTimeoutMills, TimeUnit.SECONDS);
        builder.readTimeout(provider.configReadTimeoutSecs() != 0
                ? provider.configReadTimeoutSecs() : readTimeoutMills, TimeUnit.SECONDS);

        builder.writeTimeout(provider.configWriteTimeoutSecs() != 0
                ? provider.configReadTimeoutSecs() : readTimeoutMills, TimeUnit.SECONDS);
        CookieJar cookieJar = provider.configCookie();
        if (cookieJar != null) {
            builder.cookieJar(cookieJar);
        }
        provider.configHttps(builder);

        RequestHandler handler = provider.configHandler();
        if (handler != null) {
            builder.addInterceptor(new NetInterceptor(handler));
        }

        Interceptor[] interceptors = provider.configInterceptors();
        if (!empty(interceptors)) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        if (provider.configLogEnable()) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        OkHttpClient client = builder.build();
        clientMap.put(baseUrl, client);
        providerMap.put(baseUrl, provider);

        return client;
    }

    private boolean empty(Interceptor[] interceptors) {
        return interceptors == null || interceptors.length == 0;
    }

    private void checkProvider(NetProvider provider) {
        if (provider == null) {
            throw new IllegalStateException("must register provider first");
        }
    }

    public Map<String, Retrofit> getRetrofitMap() {
        return retrofitMap;
    }

    public Map<String, OkHttpClient> getClientMap() {
        return clientMap;
    }

}
