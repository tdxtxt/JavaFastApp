package com.baselib.helper;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import com.baselib.DevApp;

/**
 * @作者： ton
 * @创建时间： 2018\5\22 0022
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class DrawableHelper {
    public static boolean compareDrawable(Drawable oneDrawable,Drawable twoDrawable){
        if(null == oneDrawable) return false;
        if(null == twoDrawable) return false;
        return oneDrawable.getConstantState().equals(twoDrawable.getConstantState());
    }
    public static boolean compareDrawable(@DrawableRes int oneId, Drawable twoDrawable){
        return compareDrawable(ContextCompat.getDrawable(DevApp.getContext(),oneId),twoDrawable);
    }
    public static Drawable getDrawable(@DrawableRes int drawableResId){
        return ContextCompat.getDrawable(DevApp.getContext(),drawableResId);
    }
}
