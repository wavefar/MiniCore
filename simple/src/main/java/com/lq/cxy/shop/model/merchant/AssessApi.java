package com.lq.cxy.shop.model.merchant;

import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.model.BaseAPI;
import com.lq.cxy.shop.model.entity.MerchantAssesItemEntity;
import com.lq.cxy.shop.model.entity.MerchantAssessEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;

import org.wavefar.lib.net.callback.BaseResultCallback;

import java.util.TreeMap;

public class AssessApi extends BaseAPI {

    public void loadWaitReplyList(int curPage , BaseResultCallback<BaseResponseEntity<MerchantAssessEntity>> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        treeMap.put("pageNum", String.valueOf(curPage));
        get(Constant.GET_WAIT_REPLY_LIST, treeMap, resultCallback);
    }

    public void saveMerchantReply(MerchantAssesItemEntity entity, String msg, BaseResultCallback<BaseResponseEntity> resultCallback) {
        TreeMap<String, String> msgMap = new TreeMap<>();
        msgMap.put("evaluateId", entity.getId());
        msgMap.put("fromCustomerId", entity.getCustomerId());
        msgMap.put("replyMsg", msg);
        post(Constant.POST_SAVE_MERCHANT_REPLY, msgMap, resultCallback);
    }
}
