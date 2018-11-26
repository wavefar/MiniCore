package com.lq.cxy.shop.activity.merchant;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.ActivityMerPayAccountBinding;
import com.lq.cxy.shop.model.entity.MerchantBindingAccountEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.merchant.MerFundApi;

import org.wavefar.lib.base.BaseSimpleActivity;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.AlertUtils;
import org.wavefar.lib.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MerPayAccount extends AppCompatActivity implements View.OnClickListener {
    private MerFundApi fundApi;
    List<MerchantBindingAccountEntity> accountList = new ArrayList<>();
    private static final String KEY_BIND_ACCOUNT_TYPE = "account_type";
    public static final String RESULT_EXTRA_ACCOUNT = "bind_account";
    public static final String RESULT_EXTRA_ACCOUNT_TYPE = "bind_account_type";
    private int forceBindSpecialAccount = -1;
    ActivityMerPayAccountBinding binding;
    private String realName, accountName;

    public static void bindAccountForResult(@NonNull Activity activity, int type, int requestCode) {
        if (activity == null) {
            return;
        }
        Intent i = new Intent(activity, MerPayAccount.class);
        i.putExtra(KEY_BIND_ACCOUNT_TYPE, type);
        activity.startActivityForResult(i, requestCode);
    }

    public static void bindAccountForResult(@NonNull Fragment fragment, int type, int requestCode) {
        if (fragment == null) {
            return;
        }
        Intent i = new Intent(fragment.getContext(), MerPayAccount.class);
        i.putExtra(KEY_BIND_ACCOUNT_TYPE, type);
        fragment.startActivityForResult(i, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mer_pay_account);
        fundApi = new MerFundApi();
        if (getIntent().hasExtra(KEY_BIND_ACCOUNT_TYPE)) {
            forceBindSpecialAccount = getIntent().getIntExtra(KEY_BIND_ACCOUNT_TYPE, -1);
        }
        if (forceBindSpecialAccount == -1) {

        }
        switch (forceBindSpecialAccount) {
            case -1:
                loadBindedAccount();
                break;
            case 0:
                binding.weiChatPayRoot.setVisibility(View.GONE);
                break;
            case 1:
                binding.alipayRoot.setVisibility(View.GONE);
                break;
        }
        binding.clickToBindAlipayAccount.setOnClickListener(this);
        binding.clickToBindWechatAccount.setOnClickListener(this);
        TextView title = binding.getRoot().findViewById(R.id.head_title);
        title.setText("提现账号管理");
        binding.getRoot().findViewById(R.id.head_more).setVisibility(View.GONE);
        binding.getRoot().findViewById(R.id.head_back_root).setOnClickListener(this);
    }

    public void initViewData() {
        if (accountList.size() > 0) {
            for (MerchantBindingAccountEntity account: accountList) {
                if (account.getAccountTypeInt() == 0) {
                    initAlipayAccount(account);
                } else if (account.getAccountTypeInt() == 1) {
                    initWechatPayAccount(account);
                }
            }
        }
    }

    private void initAlipayAccount(MerchantBindingAccountEntity account) {
        binding.alipayRoot.setVisibility(View.VISIBLE);
        binding.payAccountAlipay.setText(account.getAccount());
        binding.payAlipayRealName.setText(account.getRealName());
        binding.payAccountAlipay.setEnabled(false);
        binding.payAlipayRealName.setEnabled(false);
        binding.clickToBindAlipayAccount.setVisibility(View.GONE);
    }

    private void initWechatPayAccount(MerchantBindingAccountEntity account) {
        binding.weiChatPayRoot.setVisibility(View.VISIBLE);
        binding.payAccountWechatPay.setText(account.getAccount());
        binding.payWechatRealName.setText(account.getRealName());
        binding.payAccountWechatPay.setEnabled(false);
        binding.payWechatRealName.setEnabled(false);
        binding.clickToBindWechatAccount.setVisibility(View.GONE);
    }


    private void loadBindedAccount() {
        AlertUtils.showProgressDialog(this, "", "加载中...");
        fundApi.loadBindingPayAccount(new BaseResultCallback<BaseResponseEntity<List<MerchantBindingAccountEntity>>>() {
            @Override
            public void onResponse(BaseResponseEntity<List<MerchantBindingAccountEntity>> response) {
                AlertUtils.closeProgressDialog();
                if (response.isSuccess()) {
                    accountList.clear();
                    accountList.addAll(response.getContent());
                    initViewData();
                } else {
                    ToastUtils.showShort(response.getMessage());
                }
            }

            @Override
            public void onError(Throwable e, String body) {
                super.onError(e, body);
                AlertUtils.closeProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_root:
                setResult(RESULT_CANCELED, null);
                finish();
                break;
            case R.id.clickToBindAlipayAccount:
                realName = binding.payAlipayRealName.getText().toString().trim();
                accountName = binding.payAccountAlipay.getText().toString().trim();
                if (TextUtils.isEmpty(accountName)) {
                    ToastUtils.showShort("请输入支付宝账号");
                    return;
                }
                if (TextUtils.isEmpty(realName)) {
                    ToastUtils.showShort("请输入真实姓名");
                    return;
                }
                saveAccount(0);
                break;
            case R.id.clickToBindWechatAccount:
                realName = binding.payWechatRealName.getText().toString().trim();
                accountName = binding.payAccountWechatPay.getText().toString().trim();
                if (TextUtils.isEmpty(accountName)) {
                    ToastUtils.showShort("请输入微信账号");
                    return;
                }
                if (TextUtils.isEmpty(realName)) {
                    ToastUtils.showShort("请输入真实姓名");
                    return;
                }
                saveAccount(1);
                break;
        }
    }

    private void saveAccount(int type) {
        fundApi.addBindingPayAccount(realName, accountName, type,
                new BaseResultCallback<BaseResponseEntity<MerchantBindingAccountEntity>>() {
                    @Override
                    public void onResponse(BaseResponseEntity<MerchantBindingAccountEntity> response) {
                        if (response.isSuccess()) {
                            if (forceBindSpecialAccount != -1) {
                                Intent i = new Intent();
                                i.putExtra(RESULT_EXTRA_ACCOUNT, response.getContent());
                                i.putExtra(RESULT_EXTRA_ACCOUNT_TYPE, forceBindSpecialAccount);
                                setResult(RESULT_OK, i);
                                finish();
                            } else {
                                ToastUtils.showShort("账号绑定成功");
                                if (forceBindSpecialAccount == 0) {
                                    binding.clickToBindAlipayAccount.setVisibility(View.GONE);
                                    binding.payAlipayRealName.setEnabled(false);
                                    binding.payAccountAlipay.setEnabled(false);
                                }
                            }
                        } else {
                            ToastUtils.showShort(response.getMessage());
                        }
                    }
                });
    }
}
