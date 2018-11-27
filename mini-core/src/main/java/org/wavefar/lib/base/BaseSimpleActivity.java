package org.wavefar.lib.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.wavefar.lib.utils.Utils;


/**
 * 简单的activity基类，不含任何模式
 *
 * @author summer
 */
public abstract class BaseSimpleActivity extends AppCompatActivity implements IBaseActivity {

    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
        initViewData(savedInstanceState);

        initData();

        initViewObservable();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //eventBus unregister
        if (Utils.hasEventBus(this.getClass())) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 初始化视图和数据
     */
    private void initViewData(Bundle savedInstanceState) {

        int layoutResId = getContentView();

        if (0 != layoutResId) {
            setContentView(layoutResId);
        }

        initBundle(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mBundle != null) {
            outState.putBundle(getClass().getSimpleName(), mBundle);
        }
        super.onSaveInstanceState(outState);
    }


    /**
     * 初始化参数
     *
     * @param savedInstanceState
     */
    private void initBundle(Bundle savedInstanceState) {
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            getBundle(mBundle);
        } else if (savedInstanceState != null) {
            mBundle = savedInstanceState.getBundle(getClass().getSimpleName());
            getBundle(mBundle);
        }
    }

    /**
     * 如果有Bundle，直接返回；
     * @param bundle 传递的参数
     */
    protected void getBundle(Bundle bundle) {

    }


    /**
     * 添加页面之前添加的初始配置
     */
    @Override
    public void initParam() {
        if (Utils.hasEventBus(this.getClass())) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    @LayoutRes
    protected int getContentView() {
        return 0;
    }


    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {

    }
}
