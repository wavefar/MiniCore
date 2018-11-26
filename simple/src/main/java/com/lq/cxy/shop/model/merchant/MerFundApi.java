package com.lq.cxy.shop.model.merchant;

import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.model.BaseAPI;
import com.lq.cxy.shop.model.entity.MerchantBindingAccountEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;

import org.wavefar.lib.base.BaseApplication;
import org.wavefar.lib.net.callback.BaseResultCallback;

import java.util.List;
import java.util.TreeMap;

public class MerFundApi extends BaseAPI {
    public void loadBindingPayAccount(BaseResultCallback<BaseResponseEntity<List<MerchantBindingAccountEntity>>> l) {
        get(Constant.POST_FIND_PAY_ACOUNT_LIST, null, l);
    }

    public void addBindingPayAccount(String name, String account, int type,
                                     BaseResultCallback<BaseResponseEntity<MerchantBindingAccountEntity>> l) {
        TreeMap<String, String> msgMap = new TreeMap<>();
        msgMap.put("account", account);
        msgMap.put("accounType", String.valueOf(type));
        msgMap.put("realName", name);
        post(Constant.POST_SAVE_PAY_ACOUNT_BINDING, msgMap, l);
    }

    public void deleteBindAccount(String bindId, BaseResultCallback<BaseResponseEntity> l) {
        TreeMap<String, String> msgMap = new TreeMap<>();
        msgMap.put("bindId ", bindId);
        delete(Constant.DELETE_BIND_PAY_ACCOUNT, msgMap, l);
    }
}
