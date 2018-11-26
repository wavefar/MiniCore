package org.wavefar.lib.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.wavefar.lib.utils.Utils;


/**
 * 一个拥有DataBinding框架的基Activity
 * 这里根据项目业务可以换成你自己熟悉的BaseActivity, 但是需要继承RxAppCompatActivity,方便LifecycleProvider管理生命周期
 * @author summer
 */

public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity implements IBaseActivity {
    protected V binding;
    protected VM viewModel;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initParam();

        initViewDataBinding(savedInstanceState);

        initData();

        initViewObservable();

        viewModel.onCreate();

        viewModel.registerRxBus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //eventBus unregister
        if ( Utils.hasEventBus(this.getClass())) {
            EventBus.getDefault().unregister(this);
        }
        viewModel.removeRxBus();
        viewModel.onDestroy();
        viewModel = null;
        binding.unbind();
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding(Bundle savedInstanceState) {

        int layoutResId = initContentView(savedInstanceState);

        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        if (layoutResId != 0) {
            binding = DataBindingUtil.setContentView(this, layoutResId);
            binding.setVariable(initVariableId(), viewModel = initViewModel());
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
     *
     * @param bundle
     */
    protected void getBundle(Bundle bundle) {

    }

    /**
     * 添加页面之前添加的初始配置
     */
    @Override
    public void initParam() {
        if ( Utils.hasEventBus(this.getClass())) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 初始化根布局
     * @param savedInstanceState
     * @return 布局layout的id
     */
    public abstract int initContentView(Bundle savedInstanceState);

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public abstract int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public abstract VM initViewModel();

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {

    }
}
