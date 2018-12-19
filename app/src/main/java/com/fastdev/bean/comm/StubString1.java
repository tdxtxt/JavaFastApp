package com.fastdev.bean.comm;

import com.google.gson.annotations.SerializedName;

/**
 * @作者： ton
 * @创建时间： 2018\12\18 0018
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class StubString1 {
    @SerializedName(value = "vaule",alternate = {"vaule_1","vaule_one"})
    public String vaule;
}
