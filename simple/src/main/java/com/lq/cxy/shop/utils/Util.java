package com.lq.cxy.shop.utils;

import android.text.TextUtils;

import com.lq.cxy.shop.model.PublicApi;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.entity.BrandEntity;
import com.lq.cxy.shop.model.entity.LicenseInfoEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.http.base.BaseSimpleResultCallback;
import com.lq.cxy.shop.model.merchant.LicenseSupplyApi;

import org.wavefar.lib.net.callback.BaseResultCallback;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static String getCategoryNameBaseId(String cateId) {
        List<BrandEntity> cateList = UserAPI.getCateINfo();
        if (TextUtils.isEmpty(cateId) || cateList == null || cateList.size() == 0) {
            return "";
        }
        for (int i = 0, len = cateList.size(); i < len; i++) {
            if (cateId.equals(cateList.get(i).getId())) {
                return cateList.get(i).getCategoryName();
            }
        }
        return "";
    }

    public static void loadCategory() {
        new PublicApi().getCategory(new BaseSimpleResultCallback<
                BaseResponseEntity<ArrayList<BrandEntity>>, ArrayList<BrandEntity>>() {
            @Override
            public void onSucceed(ArrayList<BrandEntity> entity) {
                if (entity != null && entity.size() > 0) {
                    UserAPI.saveCateInfo(entity);
                }
            }
        });
    }

    public static List<BrandEntity> getCategoryList() {
        return UserAPI.getCateINfo();
    }

    public static List<String> getCategoryNameList(List<BrandEntity> cateList) {
        List<String> showList = new ArrayList<>();
        if (cateList != null) {
            for (BrandEntity b: cateList) {
                showList.add(b.getCategoryName());
            }
        }
        return showList;
    }

    public static String getIdBaseName(List<BrandEntity> cateList, String name) {
        if (TextUtils.isEmpty(name) || cateList == null || cateList.size() == 0) {
            return "";
        }
        for (int i = 0; i < cateList.size(); i++) {
            if (name.equals(cateList.get(i).getCategoryName())) {
                return cateList.get(i).getId();
            }
        }
        return "";
    }

    public static void loadAndCacheLisence(final BaseResultCallback<BaseResponseEntity<LicenseInfoEntity>> receiver) {
        new LicenseSupplyApi().querySuppliedLicenseInfo(new BaseResultCallback<BaseResponseEntity<LicenseInfoEntity>>() {
            @Override
            public void onResponse(BaseResponseEntity<LicenseInfoEntity> response) {
                if(response.isSuccess() && response.getContent() != null){
                    UserAPI.saveLisenceInfo(response.getContent());
                }
                if(receiver != null){
                    receiver.onResponse(response);
                }
            }

            @Override
            public void onError(Throwable e, String body) {
                super.onError(e, body);
                if(receiver != null){
                    receiver.onError(e,body);
                }
            }
        });
    }
}
