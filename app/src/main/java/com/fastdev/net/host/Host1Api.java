package com.fastdev.net.host;

import com.baselib.helper.HashMapParams;
import com.fastdev.bean.ResponseBody;
import com.fastdev.bean.comm.StubString1;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @作者： ton
 * @创建时间： 2018\12\18 0018
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public interface Host1Api {
    @FormUrlEncoded
    @POST("api/xxx")
    Flowable<ResponseBody<StubString1>> testApi1(@FieldMap HashMapParams params);

    @FormUrlEncoded
    @POST("api/xxxyyy")
    Flowable<ResponseBody> testApi2(@Field("parmas") String parmas);
}
