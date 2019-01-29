package com.fastdev.net.host;

import com.fastdev.bean.ResponseBody;
import com.fastdev.bean.pic.RemotePictrue;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @作者： ton
 * @创建时间： 2018\9\10 0010
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public interface HostUpload {
    @Multipart //上传一张图片
    @Headers({"Connection: close"})
    @POST("uploader/upload")
    Flowable<ResponseBody<RemotePictrue>> uploadImage(@Part MultipartBody.Part file);
}
