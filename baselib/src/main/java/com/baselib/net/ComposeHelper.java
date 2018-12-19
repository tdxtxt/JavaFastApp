package com.baselib.net;

import com.baselib.cache.CacheHelper;
import com.baselib.helper.ToastHelper;
import com.baselib.net.reqApi.NetMgr;
import com.baselib.net.reqApi.error.NetError;
import com.baselib.net.reqApi.model.IModel;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @作者： ton
 * @创建时间： 2018\12\3 0003
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class ComposeHelper {
    public static <T> FlowableTransformer<T, T> getUIThread() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
    public static <T> FlowableTransformer<T,T> rxcache(final String key){
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> fromNetwork) {
                return fromNetwork.map(new Function<T, T>() {
                    @Override
                    public T apply(T result) throws Exception {
                        CacheHelper.getDiskFromApp().add(key,result);
                        return result;
                    }
                }).onErrorResumeNext(new Function<Throwable, Publisher<? extends T>>() {
                    @Override
                    public Publisher<? extends T> apply(Throwable throwable) throws Exception {
                        T result = CacheHelper.getDiskFromApp().get(key);
                        return Flowable.just(result);
                    }
                }).onExceptionResumeNext(new Publisher<T>() {
                    @Override
                    public void subscribe(Subscriber<? super T> subscriber) {
                        T result = CacheHelper.getDiskFromApp().get(key);
                        subscriber.onNext(result);
                        subscriber.onComplete();
                    }
                });
            }
        };
    }
    /**
     * 处理错误
     * @param <T>
     * @return
     */
    public static <T extends IModel> FlowableTransformer<T,T> getApiTransformer(){
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.flatMap(new Function<T, Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(T model) throws Exception {
                        if(model == null){
                            return Flowable.error(new NetError(model.getErrorMsg(),NetError.NoDataError));
                        }else if(model.isSuccess()){
                            return Flowable.just(model);
                        }else if(model.isAuthError()){
                            return Flowable.error(new NetError(model.getErrorMsg(),NetError.AuthError));
                        }
                        return Flowable.just(model);
                    }
                }).onErrorResumeNext(new Function<Throwable, Publisher<? extends T>>() {
                    @Override
                    public Publisher<? extends T> apply(Throwable e) throws Exception {
                        NetError error = NetMgr.getInstance().getCommonProvider() == null ? new NetError(e,NetError.UnknownError) :
                                NetMgr.getInstance().getCommonProvider().convertError(e);//转换错误
                        if (NetMgr.getInstance().getCommonProvider() != null && NetMgr.getInstance().getCommonProvider().handleError(error)) {//使用通用异常处理
                            return Flowable.empty();
                        }
                        if(error.getType() == NetError.NoDataError || error.getType() == NetError.ParseError || error.getType() == NetError.NoConnectError || error.getType() == NetError.TimeOutError)
                            ToastHelper.showToast(error.getMessage());
                        return Flowable.error(error);
                    }
                });
            }
        };
    }
}
