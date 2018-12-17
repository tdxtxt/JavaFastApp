package com.baselib.cache;

import com.orhanobut.hawk.Hawk;

/**
 * @作者： ton
 * @创建时间： 2018\12\17 0017
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class AppDisk {
    /**
     * 单例
     */
    public static AppDisk getInstance() {
        return InstanceHolder.instance;
    }
    static class InstanceHolder {
        final static AppDisk instance = new AppDisk();
    }

    public <T> void add(String key,T t){
        if(Hawk.isBuilt()) Hawk.put(key,t);
    }
    public <T> T get(String key,T defaultValue){
        if(Hawk.isBuilt()) return Hawk.get(key,defaultValue);
        else return defaultValue;
    }
    public <T> T get(String key){
        return get(key,null);
    }
    public boolean remove(String key){
        if(Hawk.isBuilt()) return Hawk.delete(key);
        else return false;
    }
}
