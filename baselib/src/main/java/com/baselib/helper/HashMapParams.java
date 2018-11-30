package com.baselib.helper;

import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @作者： ton
 * @创建时间： 2018\5\4 0004
 * @功能描述： 工具类，封装链式调用的HashMap
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class HashMapParams extends HashMap<String, Object> {
    public HashMapParams(Bundle bundle){
        super();
        if(bundle != null){
            Set<String> entrySet = bundle.keySet();
            for (String key : entrySet) {
                add(key ,bundle.get(key));
            }
        }
    }

    public HashMapParams(){
        super();
    }
    public HashMapParams add(String key, Object value) {
        if(value != null) put(key, value);
        return this;
    }
    public HashMapParams remove(String key){
        remove(key);
        return this;
    }
    public <T> T get(String key, Class<T> clazz){
        return (T) get(key);
    }

    public <T> T get(String key, T defaultValue){
        T v = (T) get(key);
        return (v != null) ? v : defaultValue;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        for(Entry<String, Object> entry : entrySet()){
            Object obj = entry.getValue();
            if(obj != null){
                if (obj instanceof String) {
                    bundle.putString(entry.getKey(),get(entry.getKey(),String.class));
                }else if(obj instanceof Integer){
                    bundle.putInt(entry.getKey(),get(entry.getKey(),Integer.class));
                }else if(obj instanceof Boolean){
                    bundle.putBoolean(entry.getKey(),get(entry.getKey(),Boolean.class));
                }else if(obj instanceof Serializable){
                    bundle.putSerializable(entry.getKey(),get(entry.getKey(),Serializable.class));
                }
            }
        }
        return bundle;
    }

    public Intent toIntent(){
        Intent intent = new Intent();
        for(Entry<String, Object> entry : entrySet()){
            Object obj = entry.getValue();
            if(obj != null){
                if (obj instanceof String) {
                    intent.putExtra(entry.getKey(),get(entry.getKey(),String.class));
                }else if(obj instanceof Integer){
                    intent.putExtra(entry.getKey(),get(entry.getKey(),Integer.class));
                }else if(obj instanceof Boolean){
                    intent.putExtra(entry.getKey(),get(entry.getKey(),Boolean.class));
                }else if(obj instanceof Serializable){
                    intent.putExtra(entry.getKey(),get(entry.getKey(),Serializable.class));
                }
            }
        }
        return intent;
    }

    public Map<String,String> toStrHashMap(){
        Map<String,String> hashMap = new HashMap<>();
        for(Entry<String, Object> entry : entrySet()){
            Object obj = entry.getValue();
            if(null != obj)hashMap.put(entry.getKey(),obj.toString());
        }
        return hashMap;
    }
}
