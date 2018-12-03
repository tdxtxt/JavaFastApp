package com.baselib.bean.event;

/**
 * Created by Administrator on 2018\4\28 0028.
 */

public class Event<T> {

    private int code;
    private T data;

    public Event(int code) {
        this.code = code;
    }

    public Event(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public <V> V getData(Class<V> clazz) {
        return (V) this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EventMessage{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
