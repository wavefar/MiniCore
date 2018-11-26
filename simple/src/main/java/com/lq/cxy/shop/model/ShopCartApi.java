package com.lq.cxy.shop.model;

import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.model.entity.CartItemEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;

import org.wavefar.lib.net.callback.BaseResultCallback;

import java.util.List;
import java.util.TreeMap;

public class ShopCartApi extends BaseAPI {
    /**
     * 添加到购物车
     *
     * @param goods    添加到购物车的商品信息
     * @param listener
     */
    public void saveToShopCart(CartItemEntity goods, BaseResultCallback<BaseResponseEntity> listener) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("num", String.valueOf(goods.getNum()));
        treeMap.put("goodsId", goods.getGoodsId());
        post(Constant.POST_CART_SAVE_ACTION, treeMap, listener);
    }

    /**
     * 从购物车删除
     *
     * @param goods    添加到购物车的商品信息
     * @param listener
     */
    public void deleteFromShopCart(CartItemEntity goods, BaseResultCallback<BaseResponseEntity> listener) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("num", String.valueOf(goods.getNum()));
        treeMap.put("goodsId", goods.getGoodsId());
        treeMap.put("id", goods.getId());
        delete(Constant.DELETE_FROM_SHOPCART_ACTION, treeMap, listener);
    }

    /**
     * 更新购物车数量
     *
     * @param goods    更新购物车的商品信息
     * @param listener
     */
    public void updatemShopCart(CartItemEntity goods, BaseResultCallback<BaseResponseEntity> listener) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("num", String.valueOf(goods.getNum()));
        treeMap.put("goodsId", goods.getGoodsId());
        treeMap.put("id", goods.getId());
        post(Constant.UPDATE_SHOPCART_ACTION, treeMap, listener);
    }

    public void loadShopCartList(int pageNum, BaseResultCallback<BaseResponseEntity<List<CartItemEntity>>> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        treeMap.put("pageNum", String.valueOf(pageNum));
        get(Constant.GET_CART_LIST_ACTION, treeMap, resultCallback);
    }
}
