package com.lq.cxy.shop.fragment.merchant;

import android.content.Intent;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.ActivityFundManageBinding;
import com.lq.cxy.shop.model.viewmodel.MerchantFundFragViewModel;

import org.wavefar.lib.base.BaseFragment;

public class MerchantFundFrag extends BaseFragment<ActivityFundManageBinding, MerchantFundFragViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater,
                               @Nullable ViewGroup container,
                               @Nullable Bundle savedInstanceState) {
        return R.layout.activity_fund_manage;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MerchantFundFragViewModel initViewModel() {
        return new MerchantFundFragViewModel(this);
    }

    @Override
    public void initData() {
        binding.getRoot().findViewById(R.id.head_back_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        TextView title = binding.getRoot().findViewById(R.id.head_title);
        title.setText(R.string.merchant_fund_manage_title);
        binding.getRoot().findViewById(R.id.head_more).setVisibility(View.GONE);
        binding.merchantFundManageNav.setOnNavigationItemSelectedListener(viewModel);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data);
    }
}
