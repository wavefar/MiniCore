package com.lq.cxy.shop.model.merchant;

import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.model.BaseAPI;
import com.lq.cxy.shop.model.entity.MerchantOrderList;
import com.lq.cxy.shop.model.entity.OrderEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;

import org.wavefar.lib.net.callback.BaseResultCallback;

import java.util.TreeMap;

public class OrderApi extends BaseAPI {
    public void loadOrderList(int status, int pageNum,
                              BaseResultCallback<BaseResponseEntity<MerchantOrderList>> resultCallback) {
        TreeMap<String, String> msgMap = new TreeMap<>();
        msgMap.put("status", String.valueOf(status));
        msgMap.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        msgMap.put("pageNum", String.valueOf(pageNum));
        get(Constant.GET_MERCHANT_ORDER_LIST, msgMap, resultCallback);
    }

    public void saveSendProductInfo(OrderEntity orderEntity, BaseResultCallback<BaseResponseEntity> resultCallback) {
        TreeMap<String, String> msgMap = new TreeMap<>();
        msgMap.put("orderId", String.valueOf(orderEntity.getId()));
        msgMap.put("waybillId", String.valueOf(orderEntity.getWaybillId()));
        msgMap.put("waybillName", String.valueOf(orderEntity.getWaybillName()));
        post(Constant.POST_MERCHANT_SEND_PRODUCT, msgMap, resultCallback);
    }



}
