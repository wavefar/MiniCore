package com.lq.cxy.shop.fragment;


import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.FragmentShopCarBinding;
import com.lq.cxy.shop.model.viewmodel.CartCenterViewModel;

import org.wavefar.lib.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopCarFragment extends BaseFragment<FragmentShopCarBinding, CartCenterViewModel> implements CartCenterViewModel.OnSelectedItemChangedListener {

    ImageView rightImg;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_shop_car;
    }

    @Override
    public int initVariableId() {
        return BR.cartCenterViewModel;
    }

    @Override
    public CartCenterViewModel initViewModel() {
        return new CartCenterViewModel(getContext());
    }

    @Override
    public void initData() {
        binding.shopCarHeadRoot.findViewById(R.id.head_back_root).setVisibility(View.GONE);
        binding.cartCenterRefreshLayout.setHeaderView(new ProgressLayout(getContext()));
        rightImg = binding.shopCarHeadRoot.findViewById(R.id.head_more);
        rightImg.setOnClickListener(viewModel);
        rightImg.setImageResource(R.drawable.delete_white);
        rightImg.setVisibility(View.GONE);
        TextView title = binding.shopCarHeadRoot.findViewById(R.id.head_title);
        title.setText(R.string.shop_car_str);
        rightImg.setOnClickListener(viewModel);
        viewModel.setHasSelectedListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        binding.cartCenterRefreshLayout.startRefresh();
    }

    @Override
    public void initViewObservable() {
        //监听下拉刷新完成
        viewModel.isFreshFinished.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.cartCenterRefreshLayout.finishRefreshing();
            }
        });
        //监听上拉加载完成
        viewModel.isLoadMoreFinished.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.cartCenterRefreshLayout.finishLoadmore();
            }
        });
    }

    @Override
    public void onSelectedChange(boolean noneSelected, boolean fullSelected) {
        rightImg.setVisibility(noneSelected ? View.GONE : View.VISIBLE);
        binding.cartSelectAll.setChecked(fullSelected);
    }
}




