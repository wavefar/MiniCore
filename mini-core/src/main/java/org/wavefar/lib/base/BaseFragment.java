package org.wavefar.lib.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.wavefar.lib.utils.Utils;


/**
 * MVVM 模式Fragment基类
 * @author summer
 */
public abstract class BaseFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends Fragment implements IBaseActivity {
    protected V binding;
    protected VM viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam();
    }

    @Override
    public void onDestroy() {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, initContentView(inflater, container, savedInstanceState), container, false);
            binding.setVariable(initVariableId(), viewModel = initViewModel());
            viewModel.onCreate();
            viewModel.registerRxBus();
            initData();
            initViewObservable();
        }
        return binding.getRoot();
    }


    @Override
    public void initParam() {
        //eventBus unregister
        if ( Utils.hasEventBus(this.getClass())) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 初始化根布局
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return 布局layout的id
     */
    public abstract int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

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
