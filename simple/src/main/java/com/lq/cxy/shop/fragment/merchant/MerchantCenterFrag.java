package com.lq.cxy.shop.fragment.merchant;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lq.cxy.shop.R;
import com.lq.cxy.shop.activity.merchant.AddShopActivity;
import com.lq.cxy.shop.activity.merchant.DepositManage;
import com.lq.cxy.shop.activity.merchant.LicenseSupply;
import com.lq.cxy.shop.activity.merchant.MerPayAccount;
import com.lq.cxy.shop.activity.merchant.MerchantAssessListActivity;
import com.lq.cxy.shop.databinding.FragmentMerchantCenterBinding;

import org.wavefar.lib.base.ContainerActivity;
import org.wavefar.lib.utils.IntentUtils;

public class MerchantCenterFrag extends Fragment implements View.OnClickListener {
    FragmentMerchantCenterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMerchantCenterBinding.inflate(inflater);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        binding.getRoot().findViewById(R.id.iv_back).setVisibility(View.INVISIBLE);
        TextView title_tv = binding.getRoot().findViewById(R.id.toolbar_title);
        title_tv.setText("商铺中心");
        binding.profileTv.setOnClickListener(this);
        binding.orderAssessManage.setOnClickListener(this);
        binding.myLicenseInfo.setOnClickListener(this);
        binding.fundManageBtn.setOnClickListener(this);
        binding.merPayWayAccount.setOnClickListener(this);
        binding.merDepositManage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_tv:
                startActivity(new Intent(getContext(), AddShopActivity.class));
                break;
            case R.id.orderAssessManage:
                startActivity(new Intent(getContext(), MerchantAssessListActivity.class));
                break;
            case R.id.my_license_info:
                startActivity(new Intent(getContext(), LicenseSupply.class));
                break;
            case R.id.fundManageBtn:
                IntentUtils.startContainerActivity(MerchantFundFrag.class.getName());
                break;
            case R.id.merPayWayAccount:
                startActivity(new Intent(getContext(), MerPayAccount.class));
                break;
            case R.id.merDepositManage:
                startActivity(new Intent(getContext(), DepositManage.class));
                break;
        }

    }
}