package com.baselib.cache;

import com.orhanobut.hawk.Hawk;

/**
 * @作者： ton
 * @创建时间： 2018\12\17 0017
 * @功能描述： 带有效期的磁盘存储工具
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class ExpiryDisk {
    /**
     * 单例
     */
    public static ExpiryDisk getInstance() {
        return InstanceHolder.instance;
    }
    static class InstanceHolder {
        final static ExpiryDisk instance = new ExpiryDisk();
    }
    //有效时间
    private long validTime = 5 * 60 * 1000L;//5分钟
    private String keyPrefix = "expiry";

    private String createKeyPrefix(String key){
        return keyPrefix + key;
    }

    private boolean isInvalid(String key){
        if(!Hawk.contains(createKeyPrefix(key))) return false;
        long validTime  = Hawk.get(createKeyPrefix(key),0L);
        if(Math.abs(validTime - System.currentTimeMillis()) > validTime){//超出预期范围
            Hawk.delete(createKeyPrefix(key));
            Hawk.delete(key);
            return false;
        }
        return true;
    }

    public <T> void add(String key,T t){
        if(Hawk.isBuilt()){
            Hawk.put(createKeyPrefix(key),System.currentTimeMillis() + validTime);
            Hawk.put(key,t);
        }
    }

    public <T> T get(String key,T defaultValue){
        if(isInvalid(key)) return Hawk.get(key,defaultValue);
        else return defaultValue;
    }

    public <T> T get(String key){
        return get(key,null);
    }
}
