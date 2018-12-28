package com.baselib.ui.mvp.factory;


import com.baselib.ui.mvp.presenter.BaseMvpPresenter;
import com.baselib.ui.mvp.view.BaseMvpView;

/**
 * @date 2017/11/17
 * @description Presenter工厂实现类
 */
public class PresenterMvpFactoryImpl<V extends BaseMvpView, P extends BaseMvpPresenter<V>> implements PresenterMvpFactory<V, P> {

    /**
     * 需要创建的Presenter的类型
     */
    private final Class<P> mPresenterClass;
    private P mPresenter;



    /**
     * 根据注解创建Presenter的工厂实现类
     * @param viewClazz 需要创建Presenter的V层实现类
     * @param <V> 当前View实现的接口类型
     * @param <P> 当前要创建的Presenter类型
     * @return 工厂类
     */
    public static <V extends BaseMvpView, P extends BaseMvpPresenter<V>> PresenterMvpFactoryImpl<V,P> createFactory(Class<?> viewClazz, P presenter){
        CreatePresenter annotation = viewClazz.getAnnotation(CreatePresenter.class);
        Class<P> aClass = null;
        if(annotation != null){
            aClass = (Class<P>) annotation.value();
        }
        return aClass == null ? new PresenterMvpFactoryImpl<V, P>(presenter) : new PresenterMvpFactoryImpl<V, P>(aClass);
    }
    public static <V extends BaseMvpView, P extends BaseMvpPresenter<V>> PresenterMvpFactoryImpl<V,P> createFactory(Class<?> viewClazz){
        CreatePresenter annotation = viewClazz.getAnnotation(CreatePresenter.class);
        Class<P> aClass = null;
        if(annotation != null){
            aClass = (Class<P>) annotation.value();
        }
        return aClass == null ? null : new PresenterMvpFactoryImpl<V, P>(aClass);
    }


    private PresenterMvpFactoryImpl(P mPresenter) {
        this.mPresenterClass = (Class<P>) mPresenter.getClass();
        this.mPresenter = mPresenter;
    }
    private PresenterMvpFactoryImpl(Class<P> presenterClass) {
        this.mPresenterClass = presenterClass;
    }

    @Override
    public P createMvpPresenter() {
        try {
            if(mPresenter == null && mPresenterClass != null){
                mPresenter = mPresenterClass.newInstance();
            }
            return mPresenter;
        } catch (Exception e) {
            throw new RuntimeException("Presenter创建失败!，检查是否声明了@CreatePresenter(xx.class)注解或者是否创建Presenter");
        }
    }
}
