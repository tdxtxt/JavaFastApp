package com.fastdev.bean.pic;

import java.util.UUID;

/**
 * @作者： ton
 * @创建时间： 2018\8\7 0007
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class LocalPictrue {
    public String uuid;
    public String path;
    public int progress;
    public int status;//状态

    public LocalPictrue(String path){
        this.path = path;
        uuid = UUID.randomUUID().toString();
    }

    public void setUploading(){
        if(this.status == -1) return;//这里是因为进度有延迟，所以上传错误可能发生在进度前面
        if(this.status == 1) return;//这里是因为进度有延迟，所以上传成功可能发生在进度前面
        this.status = 0;
    }

    public boolean isUploading(){
        return this.status == 0;
    }
    public void setUploadSuccess(){
        this.status = 1;
    }

//    public boolean isUploadSuccess(){
//        return this.status == 1;
//    }
    public void setUploadFail(){
        this.status = -1;
    }

//    public boolean isUploadFail(){
//        return this.status == -1;
//    }
}
