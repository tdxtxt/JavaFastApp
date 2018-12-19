package com.fastdev.bean.update;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * @作者： ton
 * @创建时间： 2018\7\2 0002
 * @功能描述： app市场
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class Market {
    public String customTag;
    public Drawable drawable;
    public String name;
    public String packageName;

    public Market(Drawable drawable, String name, String packageName) {
        this.drawable = drawable;
        this.name = name;
        this.packageName = packageName;
    }

    public Market setCustomTag(String tag){
        this.customTag = tag;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) return false;
        if(TextUtils.isEmpty(this.packageName)) return false;
        if (obj instanceof Market){
            Market other = (Market) obj;
            if (this.packageName.equals(other.packageName)) {
                return true;
            }
        }
        return false;
    }
}
