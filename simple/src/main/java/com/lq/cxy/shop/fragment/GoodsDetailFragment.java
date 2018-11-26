package com.lq.cxy.shop.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.FragGoodsDetailBinding;
import com.lq.cxy.shop.databinding.FragGoodsIntroductionBinding;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.entity.ProductEntity;
import com.lq.cxy.shop.utils.PayUtil;

/**
 * 商品详情页面
 * @author summer
 */
public class GoodsDetailFragment extends GoodsDetailBaseFrag {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragGoodsDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.frag_goods_detail, container, false);
        if (goodsDetail != null) {
            binding.setVariable(BR.productEntity,goodsDetail);
        }
        return binding.getRoot();
    }
}
