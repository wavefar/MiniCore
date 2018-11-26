package com.lq.cxy.shop.model;

import android.text.TextUtils;

import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.model.entity.AddressEntity;
import com.lq.cxy.shop.model.entity.EvaluateEntity;
import com.lq.cxy.shop.model.entity.OrderEntity;
import com.lq.cxy.shop.model.entity.ProductEntity;
import com.lq.cxy.shop.model.entity.ProductListEntity;
import com.lq.cxy.shop.model.entity.ShopAddEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.merchant.ShopApi;

import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.JsonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * 商品相关api操作
 *
 * @author: summer
 * @date: 2018/8/17 18:32
 */
public class GoodsAPI extends BaseAPI {

    public void publishGoods(ProductEntity entity, BaseResultCallback<BaseResponseEntity> resultCallback) {
        TreeMap<String, String> msgMap = new TreeMap<>();
        msgMap.put("goodsId", entity.getId());
        post(Constant.POST_GOODS_PUBLISH_MERCHANT_ACTION, msgMap, resultCallback);
    }

    public void addGoods(ProductEntity entity, File file, File img, boolean isUpdate, BaseResultCallback<BaseResponseEntity<ProductEntity>> resultCallback) {
        TreeMap<String, String> msgMap = new TreeMap<>();
        msgMap.put("goodsName", entity.getGoodsName());
        msgMap.put("goodsCode", entity.getGoodsCode());
        msgMap.put("price", String.valueOf(entity.getPrice()));
        msgMap.put("remark", entity.getRemark());
        msgMap.put("categoryId", entity.getCategoryId());
        msgMap.put("goodStatus", String.valueOf(entity.getGoodStatus()));
        msgMap.put("stock", String.valueOf(entity.getStock()));
        msgMap.put("storeId", UserAPI.getShopInfo().getId());

        TreeMap<String, File> fileMap = new TreeMap<>();
        fileMap.put("file", file);
        fileMap.put("img", img);

        if (isUpdate) {
            msgMap.put("id", entity.getId());
            postFile(Constant.POST_GOODS_UPDATE_MERCHANT_ACTION, msgMap, fileMap, resultCallback);
        } else {
            postFile(Constant.POST_GOODS_ADD_MERCHANT_ACTION, msgMap, fileMap, resultCallback);
        }

    }

    /**
     * 获取商品列表
     *
     * @param pageNum  当前页码
     * @param keyWords 关键字
     * @param listener
     */
    public void getGoods(int pageNum,String keyWords, BaseResultCallback<?> listener) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        if (!TextUtils.isEmpty(keyWords)) {
            treeMap.put("goodsName", keyWords);
        }
        treeMap.put("pageNum", String.valueOf(pageNum));
        get(Constant.GET_GOODS_ACTION, treeMap, listener);
    }

    /**
     * 获取商品详情
     *
     * @param entity
     * @param listener
     */
    public void getGoodsDetail(ProductEntity entity, BaseResultCallback<BaseResponseEntity<ProductEntity>> listener) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("id", entity.getGoodsId() != null ? entity.getGoodsId() : entity.getId());
        get(Constant.GET_GOODS_DETAIL_ACTION, treeMap, listener);
    }

    /**
     * 单个商品直接购买
     *
     * @param entity
     * @param address
     * @param count
     * @param orderEntity
     * @param resultCallback
     */
    public void buySingleGoods(ProductEntity entity, AddressEntity address, int count, OrderEntity orderEntity, BaseResultCallback<?> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("goodsId", entity.getGoodsId() != null ? entity.getGoodsId() : entity.getId());
        treeMap.put("num", String.valueOf(count));
        //自提不用填写收货地址
        if (address != null) {
            treeMap.put("receiveId", String.valueOf(address.getId()));
        }
        treeMap.put("money", String.valueOf(entity.getPrice() * count));
        if (orderEntity != null) {
            treeMap.put("disType", String.valueOf(orderEntity.getDisType()));
            if (!TextUtils.isEmpty(orderEntity.getDisDate())) {
                treeMap.put("disDate", orderEntity.getDisDate());
                treeMap.put("disTime", orderEntity.getDisTime());
            }
        }
        post(Constant.POST_BUY_SINGLE_GOODS_ACTION, treeMap, resultCallback);
    }

    /**
     * 单商品或多商品订单提交
     *
     * @param orderEntity    订单信息
     * @param resultCallback
     */
    public void submitOrder(OrderEntity orderEntity, BaseResultCallback<?> resultCallback) {
        List<ProductEntity> productEntities = orderEntity.getGoods();
        //购物车下单
        if (orderEntity.isShopCar()) {
            buyMultiGoods(orderEntity, resultCallback);
        } else {
            ProductEntity productEntity = productEntities.get(0);
            buySingleGoods(productEntity, orderEntity.getAddress(), productEntity.getNum(), orderEntity, resultCallback);
        }
    }

    /**
     * 多商品下订单
     *
     * @param orderEntity
     * @param resultCallback
     */
    private void buyMultiGoods(OrderEntity orderEntity, BaseResultCallback<?> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("cartIds", join(getCarId(orderEntity.getGoods()), ","));
        AddressEntity addressEntity = orderEntity.getAddress();
        //自提不用填写收货地址
        if (addressEntity != null) {
            treeMap.put("receiveId", String.valueOf(addressEntity.getId()));
        }
        treeMap.put("money", calcTotalMoney(orderEntity));
        treeMap.put("disType", String.valueOf(orderEntity.getDisType()));
        post(Constant.SUBMIT_ORDER_ACTION, treeMap, resultCallback);
    }

    /**
     * 总的订单金额
     *
     * @return
     */
    private String calcTotalMoney(OrderEntity orderEntity) {
        double totalMoney = 0;
        for (ProductEntity item: orderEntity.getGoods()) {
            totalMoney += item.getPrice() * item.getNum();
        }
        return String.valueOf(totalMoney);
    }

    /**
     * 获取购物车商品对应的购物车ID
     *
     * @param productEntites
     * @return
     */
    private List<String> getCarId(List<ProductEntity> productEntites) {
        List<String> ids = new ArrayList<>();
        for (ProductEntity productEntity: productEntites) {
            ids.add(productEntity.getId());
        }
        return ids;
    }

    /**
     * 获取商品评价列表
     *
     * @param goodsId  商品ID
     * @param pageNum  当前页码
     * @param listener
     */
    public void getEvaluate(String goodsId, int pageNum, BaseResultCallback<?> listener) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("goodsId", goodsId);
        treeMap.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        treeMap.put("pageNum", String.valueOf(pageNum));
        get(Constant.GET_EVALUATE, treeMap, listener);
    }

    /**
     * 评价商品
     *
     * @param entity
     * @param resultCallback
     */
    public void saveEvaluate(EvaluateEntity entity, BaseResultCallback<?> resultCallback) {
        TreeMap<String, String> treeMap = null;
        try {
            treeMap = JsonUtils.convertBeanToMap(entity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        post(Constant.POST_EVALUATE, treeMap, resultCallback);
    }


    public void loadMerchantGoodsList(final int num, final BaseResultCallback<BaseResponseEntity<ProductListEntity>> resultCallback) {
        ShopAddEntity shopInfo;
        if ((shopInfo = UserAPI.getShopInfo()) == null) {
            new ShopApi().queryShopInfo(new BaseResultCallback<BaseResponseEntity<ShopAddEntity>>() {
                @Override
                public void onResponse(BaseResponseEntity<ShopAddEntity> response) {
                    if (response.isSuccess()) {
                        UserAPI.saveShopInfo(response.getContent());
                        loadMerchantGoodsList(num, resultCallback);
                        return;
                    }
                    resultCallback.onError(null, "店铺信息获取失败");
                }

                @Override
                public void onError(Throwable e, String body) {
                    super.onError(e, body);
                    resultCallback.onError(e, body);
                }
            });
            return;
        }

        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("storeId", shopInfo.getId());
        treeMap.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        treeMap.put("pageNum", String.valueOf(num));
        get(Constant.GET_MERCHANT_GOODS_LIST_ACTION, treeMap, resultCallback);
    }

    public void closeGoods(ProductEntity entity, BaseResultCallback<BaseResponseEntity> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("goodsId", entity.getId());
        delete(Constant.DELETE_GOODES_CLOSE, treeMap, resultCallback);
    }
    public void deleteGoods(ProductEntity entity, BaseResultCallback<BaseResponseEntity> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("id", entity.getId());
        delete(Constant.DELETE_GOODS_ACTION, treeMap, resultCallback);
    }
}
