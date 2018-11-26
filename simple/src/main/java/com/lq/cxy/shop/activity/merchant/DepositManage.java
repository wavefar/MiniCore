package com.lq.cxy.shop.activity.merchant;


import android.databinding.Observable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.ActivityDepositManageBinding;
import com.lq.cxy.shop.model.viewmodel.MerchantDepositManViewModel;

import org.wavefar.lib.base.BaseActivity;

public class DepositManage extends BaseActivity<ActivityDepositManageBinding, MerchantDepositManViewModel> {


    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_deposit_manage;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MerchantDepositManViewModel initViewModel() {
        return new MerchantDepositManViewModel(this);
    }

    @Override
    public void initData() {
        binding.depositRefresh.setHeaderView(new ProgressLayout(this));
        TextView title = binding.depositHeadRoot.findViewById(R.id.head_title);
        title.setText("押金管理");
        binding.depositHeadRoot.findViewById(R.id.head_more).setVisibility(View.GONE);
        binding.depositHeadRoot.findViewById(R.id.head_back_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.merchantDepositNav.setOnNavigationItemSelectedListener(viewModel);
    }

    @Override
    public void initViewObservable() {
        //监听下拉刷新完成
        viewModel.isFreshFinished.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.depositRefresh.finishRefreshing();
            }
        });
        //监听上拉加载完成
        viewModel.isLoadMoreFinished.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.depositRefresh.finishLoadmore();
            }
        });
    }
}
