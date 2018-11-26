package com.lq.cxy.shop.fragment;


import android.databinding.Observable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.FragmentOrderBinding;
import com.lq.cxy.shop.model.viewmodel.OrderViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.wavefar.lib.base.BaseFragment;

/**
 * 订单列表
 * @author summer
 */
public class OrderFragment extends BaseFragment<FragmentOrderBinding, OrderViewModel> {

    private static final String ORDER_TYPE = "ORDER_TYPE";

    /**
     * 订单状态类型
     */
    private int orderType;

    public static OrderFragment newInstance(int type) {
        OrderFragment fragment = new OrderFragment();
        Bundle b = new Bundle();
        b.putInt(ORDER_TYPE, type);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderType = getArguments() != null ? getArguments().getInt(ORDER_TYPE) : 0;
    }


    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.fragment_order;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public OrderViewModel initViewModel() {
        return new OrderViewModel(getContext(),orderType);
    }

    @Override
    public void initData() {
        //设置头部刷新样式
        binding.twinklingRefreshLayout.setHeaderView(new ProgressLayout(getContext()));
    }

    @Override
    public void initViewObservable() {
        //监听下拉刷新完成
        viewModel.uc.isFinishRefreshing.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.twinklingRefreshLayout.finishRefreshing();
            }
        });
        //监听上拉加载完成
        viewModel.uc.isFinishLoadmore.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.twinklingRefreshLayout.finishLoadmore();
            }
        });
    }


    @Subscribe
    public void subMessage(OrderEvent orderEvent){
        binding.twinklingRefreshLayout.startRefresh();
    }

    public interface OrderEvent {

    }
}