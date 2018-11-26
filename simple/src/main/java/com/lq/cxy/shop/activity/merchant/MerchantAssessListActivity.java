package com.lq.cxy.shop.activity.merchant;

import android.databinding.Observable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.ActivityMerchantAssessReplyBinding;
import com.lq.cxy.shop.model.viewmodel.MerchantAssessReplyViewModel;

import org.wavefar.lib.base.BaseActivity;

public class MerchantAssessListActivity extends BaseActivity<ActivityMerchantAssessReplyBinding, MerchantAssessReplyViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_merchant_assess_reply;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MerchantAssessReplyViewModel initViewModel() {
        return new MerchantAssessReplyViewModel(this);
    }

    @Override
    public void initData() {
        binding.merchantReplyRefresh.setHeaderView(new ProgressLayout(this));
        TextView title = binding.merchantReplyHeadRoot.findViewById(R.id.head_title);
        title.setText("用户评价");
        binding.merchantReplyHeadRoot.findViewById(R.id.head_more).setVisibility(View.GONE);
        binding.merchantReplyHeadRoot.findViewById(R.id.head_back_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void initViewObservable() {
        //监听下拉刷新完成
        viewModel.isFreshFinished.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.merchantReplyRefresh.finishRefreshing();
            }
        });
        //监听上拉加载完成
        viewModel.isLoadMoreFinished.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.merchantReplyRefresh.finishLoadmore();
            }
        });
    }
}
