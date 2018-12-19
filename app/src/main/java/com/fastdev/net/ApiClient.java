package com.fastdev.net;

import com.baselib.net.reqApi.NetMgr;
import com.fastdev.net.host.Host1Api;
import com.fastdev.ton.BuildConfig;

/**
 * @作者： ton
 * @创建时间： 2018\12\18 0018
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class ApiClient {
    private static String HOST1 = BuildConfig.HOST1;

    public static Host1Api getService1() {
        return NetMgr.getInstance().getRetrofit(HOST1).create(Host1Api.class);
    }

    public static void changeHost1(String host){
        HOST1 = host;
    }
    public static String getHost1(){
        return HOST1;
    }
}
