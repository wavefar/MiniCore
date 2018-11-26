package com.lq.cxy.shop.model.merchant;

import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.model.BaseAPI;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.entity.DepositEntity;
import com.lq.cxy.shop.model.entity.MerDepositList;
import com.lq.cxy.shop.model.entity.MerchantCashListEntity;
import com.lq.cxy.shop.model.entity.MerchantMoneyEntity;
import com.lq.cxy.shop.model.entity.OrderIncomeEntity;
import com.lq.cxy.shop.model.entity.OrderOutcomeEntity;
import com.lq.cxy.shop.model.entity.ShopAddEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;

import org.wavefar.lib.net.callback.BaseResultCallback;

import java.io.File;
import java.util.TreeMap;

public class ShopApi extends BaseAPI {

    public void queryShopInfo(BaseResultCallback<BaseResponseEntity<ShopAddEntity>> resultCallback) {
        get(Constant.GET_QUERY_MY_SHOP_INFO, null, resultCallback);
    }

    public void publishShop(ShopAddEntity shop, BaseResultCallback<BaseResponseEntity<String>> resultCallback) {
        TreeMap<String, String> msgMap = new TreeMap<>();
        msgMap.put("storeId", shop.getId());
        post(Constant.POST_MY_SHOP_INFO_PUBLISH, msgMap, resultCallback);
    }

    public void submitShopInfo(ShopAddEntity entity, boolean isAdd,
                               BaseResultCallback<BaseResponseEntity<ShopAddEntity>> resultCallback) {
        TreeMap<String, String> msgMap = new TreeMap<>();
        msgMap.put("storeName", entity.getStoreName());
//        msgMap.put("storeCode", entity.getStoreCode());
        msgMap.put("storeImg", entity.getStoreImg());
        msgMap.put("storeTel", entity.getStoreTel());
        msgMap.put("longitude", entity.getLongitude());
        msgMap.put("latitude", entity.getLatitude());
        msgMap.put("storeAddress", entity.getStoreAddress());
        msgMap.put("categoryId", String.valueOf(entity.getCategoryId()));
        TreeMap<String, File> fileMap = new TreeMap<>();
        fileMap.put("file", entity.getFile());

        if (isAdd) {
            postFile(Constant.POST_ADD_MY_SHOP_INFO, msgMap, fileMap, resultCallback);
        } else {
            msgMap.put("id", entity.getId());
            postFile(Constant.POST_UPDATE_MY_SHOP_INFO, msgMap, fileMap, resultCallback);
        }
    }

    /**
     * 获取余额信息
     */
    public void getMyRemainMoney(BaseResultCallback<BaseResponseEntity<MerchantMoneyEntity>> resultCallback) {
        get(Constant.GET_MERCHANT_CAPITAL, null, resultCallback);
    }

    /**
     * 获取商家进账明细
     */
    public void getMyIncomeDetailList(int pageNum, BaseResultCallback<BaseResponseEntity<MerchantCashListEntity<OrderIncomeEntity>>> resultCallback) {
        TreeMap<String, String> msgMap = new TreeMap<>();
        msgMap.put("moneyType", "");
        msgMap.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        msgMap.put("pageNum", String.valueOf(pageNum));
        get(Constant.GET_MERCHANT_INCOME_DETAIL, msgMap, resultCallback);
    }

    /**
     * 获取商家提现明细
     */
    public void getMyOutcomeDetailList(int pageNum, BaseResultCallback<BaseResponseEntity<MerchantCashListEntity<OrderOutcomeEntity>>> resultCallback) {
        TreeMap<String, String> msgMap = new TreeMap<>();
        msgMap.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        msgMap.put("pageNum", String.valueOf(pageNum));
        get(Constant.GET_MERCHANT_OUTCOME_DETAIL, msgMap, resultCallback);
    }

    /**
     * 商家收取押金列表
     */
    public void merchantDepositList(int pageNum, int status, BaseResultCallback<BaseResponseEntity<MerDepositList>> resultCallback) {
        TreeMap<String, String> msgMap = new TreeMap<>();
        msgMap.put("storeId", UserAPI.getShopInfo().getId());
        msgMap.put("orderStatus", String.valueOf(status));
        msgMap.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        msgMap.put("pageNum", String.valueOf(pageNum));
        get(Constant.GET_DEPSIT_MERCHANT_ACTION, msgMap, resultCallback);
    }

    /**
     * 商家退押金
     */
    public void merchantDepositBack(DepositEntity entity, BaseResultCallback<BaseResponseEntity> resultCallback) {
        TreeMap<String, String> msgMap = new TreeMap<>();
        msgMap.put("depositId", entity.getId());
        post(Constant.POST_MERCHANT_PUSH_BACK_CASH, msgMap, resultCallback);
    }
}
