package com.lq.cxy.shop.activity;

import android.databinding.Observable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.ActivityFaveGoodsListBinding;
import com.lq.cxy.shop.model.viewmodel.FavGoodsListViewModel;

import org.wavefar.lib.base.BaseActivity;

public class FaveGoodsListActivity extends BaseActivity<ActivityFaveGoodsListBinding, FavGoodsListViewModel> implements View.OnClickListener {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_fave_goods_list;
    }

    @Override
    public int initVariableId() {
        return BR.favListViewModel;
    }

    @Override
    public void initData() {
        binding.goodsFavRefreshLayout.setHeaderView(new ProgressLayout(this));
        binding.goodsFavHeadRoot.setOnClickListener(this);
        binding.goodsFavHeadRoot.findViewById(R.id.head_back_root).setOnClickListener(this);
        binding.goodsFavHeadRoot.findViewById(R.id.head_more).setVisibility(View.GONE);
        TextView title = binding.goodsFavHeadRoot.findViewById(R.id.head_title);
        title.setText(R.string.goods_favorite_title);
    }

    @Override
    public FavGoodsListViewModel initViewModel() {
        return new FavGoodsListViewModel(this);
    }

    @Override
    public void initViewObservable() {
        //监听下拉刷新完成
        viewModel.isFreshFinished.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.goodsFavRefreshLayout.finishRefreshing();
            }
        });
        //监听上拉加载完成
        viewModel.isLoadMoreFinished.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.goodsFavRefreshLayout.finishLoadmore();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_root:
                finish();
                break;
        }
    }
}
