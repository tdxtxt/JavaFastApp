package com.baselib.net.reqApi.model;

/**
 * Created by Administrator on 2018\4\19 0019.
 */

public interface IModel {

    boolean isNull();       //空数据

    boolean isAuthError();  //验证错误

    boolean isBizError();   //业务错误

    String getErrorMsg();   //后台返回的错误信息

    boolean isSuccess();

}
