package com.lq.cxy.shop.model.viewmodel;

import android.content.Context;
import android.view.View;

import com.lq.cxy.shop.model.entity.DepositEntity;

import org.wavefar.lib.base.BaseViewModel;

public class MerchantDepositItemViewModel extends BaseViewModel {
    public DepositEntity item;

    public OnBackDepositClickListener getListener() {
        return listener;
    }

    public void setListener(OnBackDepositClickListener listener) {
        this.listener = listener;
    }

    public OnBackDepositClickListener listener;

    public MerchantDepositItemViewModel(Context context, DepositEntity deposit) {
        super(context);
        item = deposit;
    }

    public void onclickBackDeposit(View v) {
        if (listener != null) {
            listener.onClick(item);
        }
    }

    public interface OnBackDepositClickListener {
        void onClick(DepositEntity item);
    }
}
