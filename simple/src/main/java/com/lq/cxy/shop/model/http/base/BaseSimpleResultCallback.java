package com.lq.cxy.shop.model.http.base;

import com.lq.cxy.shop.model.UserAPI;

import org.wavefar.lib.exception.BaseException;
import org.wavefar.lib.exception.ExceptionHandle;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.JsonUtils;
import org.wavefar.lib.utils.ToastUtils;
import org.wavefar.lib.utils.Utils;

/**
 * 业务层回调封装
 * @author summer
 * @date 2018/8/19 上午9:41
 */
public abstract  class BaseSimpleResultCallback<T,S> extends BaseResultCallback<T> {


    @Override
    public void onResponse(T response) {
        if (response instanceof  BaseResponseEntity){
            BaseResponseEntity baseResponseEntity = ((BaseResponseEntity) response);
            if (baseResponseEntity.isSuccess()) {
                onSucceed((S)baseResponseEntity.getContent());
            } else {
                onFailure(baseResponseEntity.getMessage());
            }
        }
    }

    /**
     * 响应成功回调
     * @param entity 返回实体类
     */
    public  abstract void onSucceed(S entity);




    public void onFailure(String message){
        ToastUtils.showShort(message);
    }

    /**
     * 包含业务层和网络层异常输出
     * @param e
     * @param body 返回的字符串，方便重新解析
     */
    @Override
    public void onError(Throwable e, String body) {
        BaseException exception = new ExceptionHandle().handleHttpException(e);
        //被别人挤下来了，需要重新登录；
        if (401 == exception.code) {
            new UserAPI().logout(Utils.getApp().getApplicationContext());
        }
        super.onError(e,body);
        if ( body != null) {
            BaseResponseEntity baseStatus = JsonUtils.fromJson(body,BaseResponseEntity.class);
            if (baseStatus != null  && !baseStatus.isSuccess()) {
                onFailure(baseStatus.getMessage());
            }
        }
    }
}
