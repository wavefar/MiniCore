package com.lq.cxy.shop.fragment;


import android.databinding.Observable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.FragmentGoodBinding;
import com.lq.cxy.shop.model.viewmodel.GoodViewModel;

import org.wavefar.lib.base.BaseFragment;
import org.wavefar.lib.utils.IntentUtils;

/**
 * 商品列表
 * @author Administrator
 */
public class GoodFragment extends BaseFragment<FragmentGoodBinding, GoodViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.fragment_good;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public GoodViewModel initViewModel() {
        return new GoodViewModel(getContext());
    }

    @Override
    public void initData() {
        //设置头部刷新样式
        binding.twinklingRefreshLayout.setHeaderView(new ProgressLayout(getContext()));
        View view = binding.getRoot();
        TextView titleTv = view.findViewById(R.id.toolbar_title);
        titleTv.setText("特产");
        view.findViewById(R.id.iv_back).setVisibility(View.INVISIBLE);
        ImageView searchIv = view.findViewById(R.id.iv_more);
        searchIv.setVisibility(View.VISIBLE);
        searchIv.setImageResource(R.drawable.ic_search);
        searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.startContainerActivity(SearchFragment.class.getName());
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
        //监听上拉加载完成
        viewModel.uc.isFinishLoadmore.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.twinklingRefreshLayout.finishLoadmore();
            }
        });
    }
}