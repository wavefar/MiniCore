package com.lq.cxy.shop.model.viewmodel;

import android.content.Context;

import com.lq.cxy.shop.model.entity.OrderEntity;

import org.wavefar.lib.base.BaseViewModel;

public class MerchantOrderItemViewModel extends BaseViewModel {
    public OrderEntity itemEntity;

    public MerchantOrderItemViewModel(Context context, OrderEntity entity) {
        super(context);
        this.itemEntity = entity;
    }

    public void onItemClick() {
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(MerchantOrderDetailFrag.KEY_MERCHANT_ORDER, itemEntity);
//        startContainerActivity(MerchantOrderDetailFrag.class.getName(),bundle);
    }
}
