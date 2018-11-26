package com.lq.cxy.shop.activity.merchant;


import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.ActivityCashToCardBinding;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.entity.MerchantBindingAccountEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;

import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.StringUtil;
import org.wavefar.lib.utils.ToastUtils;

public class CashToCardActivity extends AppCompatActivity {
    private static final String KEY_ACCOUNT = "pay_Account_key";
    private static final String KEY_CASH = "pay_cash_key";

    public static void startGetCashForResult(Fragment frag,
                                             MerchantBindingAccountEntity account,
                                             double money, int requestCode) {
        if (frag == null) {
            return;
        }
        Intent i = new Intent(frag.getContext(), CashToCardActivity.class);
        i.putExtra(KEY_ACCOUNT, account);
        i.putExtra(KEY_CASH, money);
        frag.startActivityForResult(i, requestCode);
    }

    private MerchantBindingAccountEntity account;
    private double cash;
    private ActivityCashToCardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cash_to_card);
        account = getIntent().getParcelableExtra(KEY_ACCOUNT);
        cash = getIntent().getDoubleExtra(KEY_CASH, 0);
        if (account == null) {
            ToastUtils.showShort("提现必须提供绑定账号！");
            finish();
        } else if (cash < 0) {
            ToastUtils.showShort("提现金额必须大于0！");
            finish();
        }

        binding.payTypeImg.setImageResource(account.getAccountTypeInt() == 0 ?
                R.drawable.pay_account_alipay : R.drawable.pay_account_wechat);
        binding.payTypeMoney.setText(StringUtil.formatSignMoney(cash));
        binding.payTypeUserName.setText(account.getRealName());
        binding.payTypeCount.setText(account.getAccount());
        binding.confirmToGetCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWarningDialog();

            }
        });
        TextView title = binding.getRoot().findViewById(R.id.head_title);
        title.setText("提现账号确认");
        binding.getRoot().findViewById(R.id.head_more).setVisibility(View.GONE);
        binding.getRoot().findViewById(R.id.head_back_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showWarningDialog() {
        new AlertDialog.Builder(this)
                .setMessage("确定将现金提取到该账号？")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new UserAPI().transferMoney(account, cash, new BaseResultCallback<BaseResponseEntity>() {
                            @Override
                            public void onResponse(BaseResponseEntity response) {
                                if (response.isSuccess()) {
                                    ToastUtils.showShort("转账成功");
                                    handleTransferSuccess(response);
                                } else {
                                    ToastUtils.showShort(response.getMessage());
                                }
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setCancelable(false)
                .create().show();
    }

    private void handleTransferSuccess(BaseResponseEntity resp) {
        setResult(RESULT_OK);
        finish();
    }
}
