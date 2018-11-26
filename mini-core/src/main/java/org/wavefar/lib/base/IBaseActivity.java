package org.wavefar.lib.base;

/**
 * activity基类接口契约
 * @author Administrator
 */
public interface IBaseActivity {
    /**
     * 初始化界面传递参数
     */
    void initParam();
    /**
     * 初始化数据
     */
    void initData();

    /**
     * 初始化界面观察者的监听
     */
    void initViewObservable();
}
