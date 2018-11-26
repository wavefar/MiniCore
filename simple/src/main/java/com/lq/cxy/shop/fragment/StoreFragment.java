package com.lq.cxy.shop.fragment;


import android.annotation.SuppressLint;
import android.databinding.Observable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.FragmentStoreBinding;
import com.lq.cxy.shop.model.viewmodel.StoreViewModel;

import org.wavefar.lib.base.BaseFragment;

/**
 * 商店列表
 *
 * @author Administrator
 */
public class StoreFragment extends BaseFragment<FragmentStoreBinding, StoreViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.fragment_store;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public StoreViewModel initViewModel() {
        return new StoreViewModel(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void initData() {
        //设置头部刷新样式
        binding.twinklingRefreshLayout.setHeaderView(new ProgressLayout(getContext()));
        View view = binding.getRoot();
        TextView titleTv = view.findViewById(R.id.toolbar_title);
        titleTv.setText("选择商品");
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

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
    }
}