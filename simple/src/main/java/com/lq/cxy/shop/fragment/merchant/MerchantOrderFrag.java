package com.lq.cxy.shop.fragment.merchant;

import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.FragmentMerchantOrderBinding;
import com.lq.cxy.shop.model.viewmodel.MerchantOrderViewModel;

import org.wavefar.lib.base.BaseFragment;

public class MerchantOrderFrag extends BaseFragment<FragmentMerchantOrderBinding, MerchantOrderViewModel> implements View.OnClickListener {


    @Override
    public int initContentView(LayoutInflater inflater,
                               @Nullable ViewGroup container,
                               @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_merchant_order;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MerchantOrderViewModel initViewModel() {
        return new MerchantOrderViewModel(getContext());
    }

    @Override
    public void initData() {
        binding.merchantOrderNav.setOnNavigationItemSelectedListener(viewModel);
        binding.merchantOrderRefresh.setHeaderView(new ProgressLayout(getContext()));
    }

    @Override
    public void initViewObservable() {
        //监听下拉刷新完成
        viewModel.isFreshFinished.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.merchantOrderRefresh.finishRefreshing();
            }
        });
        //监听上拉加载完成
        viewModel.isLoadMoreFinished.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.merchantOrderRefresh.finishLoadmore();
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.head_right_tv:
//                startActivity(new Intent(getContext(), MerchantGoodsDetailActivity.class));
//            break;
        }
    }
}