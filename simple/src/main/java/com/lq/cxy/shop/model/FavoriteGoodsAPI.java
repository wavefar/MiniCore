package com.lq.cxy.shop.model;

import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.model.entity.FavGoodsListEntity;
import com.lq.cxy.shop.model.entity.ProductEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;

import org.wavefar.lib.net.callback.BaseResultCallback;

import java.util.TreeMap;

/**
 * 收藏商品
 * @author Administrator
 */
public class FavoriteGoodsAPI extends BaseAPI {

    public void addFavGoods(ProductEntity entity, BaseResultCallback<BaseResponseEntity> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("goodsId", entity.getGoodsId()!= null ? entity.getGoodsId() : entity.getId());
        post(Constant.POST_ADD_FAV_GOODS_ACTION, treeMap, resultCallback);
    }

    public void deleteFavGoods(ProductEntity entity, BaseResultCallback<BaseResponseEntity> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("goodsId", entity.getGoodsId()!= null ? entity.getGoodsId() : entity.getId());
        delete(Constant.DELETE_FAV_GOODS_ACTION, treeMap, resultCallback);
    }

    public void loadFavList(int pageNum, BaseResultCallback<BaseResponseEntity<FavGoodsListEntity>> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        treeMap.put("pageNum", String.valueOf(pageNum));
        get(Constant.GET_FAV_LIST_ACTION, treeMap, resultCallback);
    }
}
