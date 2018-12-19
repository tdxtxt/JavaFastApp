package com.fastdev.bean.comm;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @作者： ton
 * @创建时间： 2018\12\18 0018
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class StubList1<T> {
    @SerializedName(value = "list",alternate = {"list_1","list_one"})
    public List<T> list;
}
