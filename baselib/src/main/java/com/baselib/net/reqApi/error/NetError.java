package com.baselib.net.reqApi.error;


import com.baselib.DevApp;
import com.baselib.R;

/**
 * Created by Administrator on 2018\3\16 0016.
 */

public class NetError extends Exception {
    private Throwable exception;
    private int type = NoConnectError;

    public static final int ParseError = 0;   //数据解析异常
    public static final int NoConnectError = 1;   //无连接异常
    public static final int AuthError = 2;   //用户验证异常
    public static final int NoDataError = 3;   //无数据返回异常
    public static final int BusinessError = 4;   //业务异常
    public static final int TimeOutError = 5;//超时
    public static final int ServiceError = 6;//服务器繁忙，通常就是服务器挂了的表现
    public static final int UnknownError = 7;   //未知异常

    public NetError(Throwable exception, int type) {
        this.exception = exception;
        this.type = type;
    }

    public NetError(String detailMessage, int type) {
        super(detailMessage);
        this.type = type;
    }

    @Override
    public String getMessage() {
        if(this.type == ParseError){
            return DevApp.getString(R.string.baselib_parse_error);
        }else if(NoConnectError == this.type){
            return DevApp.getString(R.string.baselib_network_error);
        }else if(TimeOutError == this.type){
            return DevApp.getString(R.string.baselib_network_error);
        }else if(ServiceError == this.type){
            return DevApp.getString(R.string.baselib_service_error);
        }else if(AuthError == this.type){

        }else if(NoDataError == this.type){

        }else if(BusinessError == this.type){

        }else if(UnknownError == this.type){

        }
        if (exception != null){
            return exception.getMessage();
        }
        return super.getMessage();
    }

    public int getType() {
        return type;
    }
}

