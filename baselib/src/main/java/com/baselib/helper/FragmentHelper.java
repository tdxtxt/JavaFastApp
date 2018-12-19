package com.baselib.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * @作者： ton
 * @创建时间： 2018\12\3 0003
 * @功能描述：
 * @传入参数说明： 无
 * @返回参数说明： 无
 */
public class FragmentHelper {
    /*
     * add() + remove() = replace() 销毁当前显示的Fragment，添加需要显示的Fragment。
     * 缺点：Fragment会被重新创建，导致View重绘。View重绘的同时也表示用户之前在该View上做的操作都消失了。
     * 优点：节省内存空间。
     * 使用场景：不要求保留用户操作。
     */
    public static <T extends Fragment> void replaceFragment(FragmentManager fManager,T fragment , int fragmentResId){
        //直接replace()就可以了，不需要先add~~~
        fManager.beginTransaction()
                .replace(fragmentResId,fragment)
                .commit();
    }
    public <T extends Fragment> void removeFragment(FragmentManager fManager,T fragment) {
        fManager.beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss();
    }
    /**
     * show() + hide() 将Fragment视图隐藏之后在显示。
     * 缺点：由于Fragment只是被隐藏了，并未被销毁，所以需要占用内存空间来保存。
     * 优点：不用重新创建Fragment
     * 使用场景；需要保留用户数据的情形。
     */
    public static <T extends Fragment> void showFragment(FragmentManager fManager,T inFragment, int fragmentResId) {
        // 开启事物
        FragmentTransaction fragmentTransaction = fManager.beginTransaction();
        // 先隐藏当前所有的Fragment
        List<Fragment> childFragments = fManager.getFragments();
        if(childFragments != null){
            for (Fragment childFragment : childFragments) {
                if(childFragment != null) fragmentTransaction.hide(childFragment);
            }
        }
        // 2.如果容器里面没有我们就添加，否则显示
        String tag = inFragment.getClass().getSimpleName();
        Fragment tempFragment = fManager.findFragmentByTag(tag);
        if(tempFragment != null){
            fragmentTransaction.show(inFragment);
        }else{
            if(!inFragment.isAdded()) fragmentTransaction.add(fragmentResId,inFragment,tag);//.addToBackStack(tag)//将一个事务添加到返回栈中
        }
        // 一定要commit
        fragmentTransaction.commitAllowingStateLoss();
    }
    /**
     * attach() + detch() 保留Fragment（就是不会重复执行Fragment的生命周期），但删除Fragment的View（就是replaceFragment的改良版）
     * 使用场景：如果你的当前Activity一直存在，那么在不希望保留用户操作的时候，你可以优先使用detach
     */
    public static <T extends Fragment> void attachFragment(){}
}
