package com.fastdev.bean.pic;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @作者： ton
 * @创建时间： 2018\8\7 0007
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class RemotePictrue implements Serializable{
    private static final long serialVersionUID = 826375507409305676L;
    public String uuid;//关联本地图片
    @SerializedName(value = "id",alternate = {"fid"})
    public String id;
    @SerializedName("path")
    public String url;
    @SerializedName("minPath")
    public String thumbnailUrl;

    public RemotePictrue setUUID(String uuid){
        this.uuid = uuid;
        return this;
    }

    //切记：此方法不能动，CreateNeedBean类中toReqParams方法需要调用此方法
    @Override
    public String toString() {
        return this.id == null ? "" : this.id;
    }
}
