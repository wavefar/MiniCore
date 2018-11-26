package org.wavefar.lib.base;

import android.app.Application;

import org.wavefar.lib.utils.Utils;

/**
 * app全局基类配置
 *
 * @author summer
 */
public class BaseApplication extends Application {
    private static BaseApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        //初始化工具类
        initUtils();
    }

    /**
     * 获得当前app运行的AppContext
     */
    public static BaseApplication getInstance() {
        return sInstance;
    }

    /**
     * 初始化工具类
     */
    private void initUtils() {
        Utils.init(this);

    }

}
