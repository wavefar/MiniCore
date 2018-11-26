package com.lq.cxy.shop.model.exception;

import org.wavefar.lib.exception.BaseException;
import org.wavefar.lib.exception.ExceptionHandle;

/**
 * 复写基类函数
 * ApiException
 */
public class ApiException extends ExceptionHandle {
    @Override
    public BaseException handleHttpException(Throwable e) {
        return super.handleHttpException(e);
    }
}
