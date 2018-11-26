package com.lq.cxy.shop.model;

import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.model.entity.AddressEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;

import org.wavefar.lib.net.callback.BaseResultCallback;

import java.util.List;
import java.util.TreeMap;

/**
 * 收货地址相关
 * @author Administrator
 */
public class AddressApi extends BaseAPI {
    public void addAddress(AddressEntity address, BaseResultCallback<BaseResponseEntity> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("userName", address.getUserName());
        treeMap.put("phoneNum", address.getPhoneNum());
        treeMap.put("address", address.getAddress());
        post(Constant.POST_ADD_ADDRESS_ACTION, treeMap, resultCallback);
    }

    public void updateAddress(AddressEntity address, BaseResultCallback<BaseResponseEntity> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("id", address.getId());
        treeMap.put("userName", address.getUserName());
        treeMap.put("phoneNum", address.getPhoneNum());
        treeMap.put("address", address.getAddress());
        post(Constant.POST_UPDATE_ADDRESS_ACTION, treeMap, resultCallback);
    }

    public void loadAddressList(BaseResultCallback<BaseResponseEntity<List<AddressEntity>>> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        get(Constant.GET_ADDRESS_LIST_ACTION, treeMap, resultCallback);
    }

    public void deleteAddress(AddressEntity address, BaseResultCallback<BaseResponseEntity> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("id", address.getId());
        delete(Constant.DELETE_ADDRESS_ACTION, treeMap, resultCallback);
    }
}
