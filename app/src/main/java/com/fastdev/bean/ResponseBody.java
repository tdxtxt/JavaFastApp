package com.fastdev.bean;

import com.baselib.net.reqApi.model.IModel;

/**
 * @作者： ton
 * @创建时间： 2018\12\18 0018
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class ResponseBody<T> implements IModel{
    public String status;
    public T data;
    public String msg;

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isAuthError() {
        return false;
    }

    @Override
    public boolean isBizError() {
        return false;
    }

    @Override
    public String getErrorMsg() {
        return null;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }
}
