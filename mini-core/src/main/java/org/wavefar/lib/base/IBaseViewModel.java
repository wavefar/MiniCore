package org.wavefar.lib.base;

/**
 * ViewModel基类接口契约
 * @author Administrator
 */
public interface IBaseViewModel {

    /**
     * View的界面创建时回调
     */
    void onCreate();

    /**
     * View的界面销毁时回调
     */
    void onDestroy();

    /**
     * 注册RxBus
     */
    void registerRxBus();
    /**
     * 移除RxBus
     */
    void removeRxBus();
}
