package com.baselib.helper;

import com.baselib.bean.event.Event;

import org.greenrobot.eventbus.EventBus;

/**
 * @作者： ton
 * @时间： 2018\4\28 0028 
 * @描述： eventbus封装
 * @传入参数说明：
 * @返回参数说明：
 */
public class EventBusHelper {
    private EventBusHelper() {
    }

    /**
     * 注册 EventBus
     *
     * @param subscriber
     */
    public static void register(Object subscriber) {
        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(subscriber)) {
            eventBus.register(subscriber);
        }
    }

    /**
     * 解除注册 EventBus
     *
     * @param subscriber
     */
    public static void unregister(Object subscriber) {
        EventBus eventBus = EventBus.getDefault();
        if (eventBus.isRegistered(subscriber)) {
            eventBus.unregister(subscriber);
        }
    }

    /**
     * 发送事件消息
     *
     * @param event
     */
    public static void post(Event event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 发送粘性事件消息
     *
     * @param event
     */
    public static void postSticky(Event event) {
        EventBus.getDefault().postSticky(event);
    }
}
