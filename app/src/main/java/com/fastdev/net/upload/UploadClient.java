package com.fastdev.net.upload;

import com.baselib.net.reqApi.NetMgr;
import com.fastdev.net.ApiClient;
import com.fastdev.net.host.HostUpload;
import com.fastdev.net.upload.listener.ProgressRequestListener;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @作者： ton
 * @创建时间： 2018\9\10 0010
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class UploadClient {
    public static HostUpload getUploadService(){
        return NetMgr.getInstance().getRetrofitUpload(ApiClient.getHost1()).create(HostUpload.class);
    }

    public static MultipartBody.Part createRequestBody(String fileUri,ProgressRequestListener listener){
        File file = new File(fileUri);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData("file", file.getName(), new ProgressRequestBody(requestFile,listener));//this是在当前类实现了ProgressRequestListener接口
    }
}
