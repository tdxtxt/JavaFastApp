package com.baselib.net;

import com.baselib.net.reqApi.error.NetError;
import com.baselib.net.reqApi.model.IModel;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Function;

/**
 * @作者： ton
 * @创建时间： 2018\12\3 0003
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class ComposeHelper {
    public <T extends IModel> FlowableTransformer<T,T> getApiTransformer(){
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
                    public Publisher<? extends T> apply(Throwable throwable) throws Exception {
                        return null;
                    }
                });
            }
        };
    }
}
