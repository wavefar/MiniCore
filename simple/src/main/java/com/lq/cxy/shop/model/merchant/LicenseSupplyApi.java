package com.lq.cxy.shop.model.merchant;

import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.model.BaseAPI;
import com.lq.cxy.shop.model.entity.LicenseInfoEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;

import org.wavefar.lib.net.callback.BaseResultCallback;

import java.io.File;
import java.util.TreeMap;

public class LicenseSupplyApi extends BaseAPI {

    public void querySuppliedLicenseInfo(BaseResultCallback<BaseResponseEntity<LicenseInfoEntity>> resultCallback) {
        get(Constant.GET_APPLY_MERCHANT, null, resultCallback);
    }

    public void publishLicenseInfo(String id, BaseResultCallback<BaseResponseEntity> listener) {
        TreeMap<String, String> msgMap = new TreeMap<>();
        msgMap.put("businessId", id);
        post(Constant.POST_APPLY_MERCHANT_PUBLISH, msgMap, listener);

    }

    public void submitLicenseInfo(LicenseInfoEntity entity, boolean isAdd, BaseResultCallback<BaseResponseEntity<LicenseInfoEntity>> resultCallback) {
        TreeMap<String, String> msgMap = new TreeMap<>();
        msgMap.put("companyName", entity.getCompanyName());
        msgMap.put("licenseNum", entity.getLicenseNum());
        msgMap.put("licenseAddress", entity.getLicenseAddress());
        msgMap.put("establishTime", entity.getEstablishTime());
        msgMap.put("beginTime", entity.getBeginTime());
        msgMap.put("endTime", entity.getEndTime());
        msgMap.put("scope", entity.getScope());
        msgMap.put("legalName", entity.getLegalName());
        msgMap.put("legalNum", entity.getLegalNum());
        msgMap.put("companyAddress", entity.getCompanyAddress());
        msgMap.put("emContact", entity.getEmContact());
        msgMap.put("emContactPhone", entity.getEmContactPhone());

        msgMap.put("licenseType", String.valueOf(entity.getLicenseType()));
        msgMap.put("capital", String.valueOf(entity.getCapital()));
        msgMap.put("idType", String.valueOf(entity.getIdType()));
        msgMap.put("customerId", String.valueOf(entity.getCustomerId()));
        msgMap.put("status", String.valueOf(entity.getStatus()));

        TreeMap<String, File> fileMap = new TreeMap<>();
        if(entity.getFile() != null){
            fileMap.put("file", entity.getFile());
        }

        if (isAdd) {
            postFile(Constant.POST_APPLY_MERCHANT, msgMap, fileMap, resultCallback);
        } else {
            msgMap.put("id", entity.getId());
            msgMap.put("licenseImg", entity.getLicenseImg());
            postFile(Constant.POST_UPDATE_APPLY_MERCHANT, msgMap, fileMap, resultCallback);
        }

    }
}
