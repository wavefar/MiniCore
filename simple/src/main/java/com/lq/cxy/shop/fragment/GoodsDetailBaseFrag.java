package com.lq.cxy.shop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.lq.cxy.shop.model.entity.ProductEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public abstract class GoodsDetailBaseFrag extends Fragment {
    public static final String KEY_GOODS_DETAIL = "key_must_has_goods_entity";
    protected ProductEntity goodsDetail;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goodsDetail = getArguments().getParcelable(KEY_GOODS_DETAIL);
        assert goodsDetail != null;
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void updateGoodsDetailInfo(ProductEntity goodsDetail) {
    }
}
