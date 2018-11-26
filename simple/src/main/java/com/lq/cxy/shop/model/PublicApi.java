package com.lq.cxy.shop.model;

import com.lq.cxy.shop.Constant;

import org.wavefar.lib.net.callback.BaseResultCallback;

import java.util.TreeMap;

/**
 * 首页聚合类API
 * @author: summer
 * @date: 2018/8/29 9:13
 */
public class PublicApi extends BaseAPI {
    /**
     * 获取banner列表
     * @param listener
     */
    public void getBanner(BaseResultCallback<?> listener) {
        get(Constant.GET_BANNER_ACTION, null, listener);
    }

    /**
     * 获取品牌
     * @param listener
     */
    public void getCategory(BaseResultCallback<?> listener) {
        get(Constant.GET_BRAND_ACTION, null, listener);
    }

    /**
     * 根据分类获取周边商家
     * @param longitude 经度
     * @param latitude  纬度
     * @param categoryId 分类ID
     */
    public void  getNearStore(double longitude,double latitude,String categoryId,BaseResultCallback<?> listener) {
        TreeMap<String ,String> hashMap = new TreeMap<>();
        hashMap.put("longitude",String.valueOf(longitude));
        hashMap.put("latitude",String.valueOf(latitude));
        hashMap.put("categoryId",String.valueOf(categoryId));
        get(Constant.GET_NEARSTORE_BYCATEGORY_ACTION,hashMap,listener);
    }

    /**
     * 获取周边商家
     * @param longitude 经度
     * @param latitude  纬度
     */
    public void  getNearStore(double longitude,double latitude,BaseResultCallback<?> listener) {
        TreeMap<String ,String> hashMap = new TreeMap<>();
        hashMap.put("longitude",String.valueOf(longitude));
        hashMap.put("latitude",String.valueOf(latitude));
        get(Constant.GET_NEARSTORE_ACTION,hashMap,listener);
    }

    /**
     * 获取最新版本
     * @param listener
     */
    public void getVersionInfo(BaseResultCallback<?> listener) {
        get(Constant.GET_VERSION_ACTION, null, listener);
    }
}
