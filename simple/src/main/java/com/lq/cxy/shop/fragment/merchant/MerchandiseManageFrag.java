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
import com.lq.cxy.shop.activity.merchant.MerchantGoodsDetailActivity;
import com.lq.cxy.shop.databinding.FragmentMerchandiseManageBinding;
import com.lq.cxy.shop.model.viewmodel.MerchandiseFragViewModel;

import org.wavefar.lib.base.BaseFragment;

public class MerchandiseManageFrag extends BaseFragment<FragmentMerchandiseManageBinding, MerchandiseFragViewModel> implements View.OnClickListener {


    @Override
    public int initContentView(LayoutInflater inflater,
                               @Nullable ViewGroup container,
                               @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_merchandise_manage;
    }

    @Override
    public int initVariableId() {
        return BR.listVm;
    }

    @Override
    public MerchandiseFragViewModel initViewModel() {
        return new MerchandiseFragViewModel(getContext());
    }

    @Override
    public void initData() {
        binding.merchandiseRefresh.setHeaderView(new ProgressLayout(getContext()));
        binding.merchandiseHeadRoot.findViewById(R.id.head_more).setVisibility(View.GONE);
        TextView rightTv = binding.merchandiseHeadRoot.findViewById(R.id.head_right_tv);
        rightTv.setVisibility(View.VISIBLE);
        rightTv.setText(R.string.merchant_add_goods);
        rightTv.setOnClickListener(this);
        TextView title = binding.merchandiseHeadRoot.findViewById(R.id.head_title);
        title.setText("我的商品");
        binding.merchandiseHeadRoot.findViewById(R.id.head_back_root).setVisibility(View.GONE);
    }


    @Override
    public void initViewObservable() {
        //监听下拉刷新完成
        viewModel.isFreshFinished.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.merchandiseRefresh.finishRefreshing();
            }
        });
        //监听上拉加载完成
        viewModel.isLoadMoreFinished.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.merchandiseRefresh.finishLoadmore();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_right_tv:
                startActivity(new Intent(getContext(), MerchantGoodsDetailActivity.class));
                break;
        }
    }
}
