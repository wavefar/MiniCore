package org.wavefar.lib.net.callback;

import com.google.gson.internal.$Gson$Types;

import org.wavefar.lib.exception.BaseException;
import org.wavefar.lib.exception.ExceptionHandle;
import org.wavefar.lib.utils.AlertUtils;
import org.wavefar.lib.utils.ToastUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * 统一回调接口
 * @param <T>
 * @author summer
 */
public abstract class BaseResultCallback<T> {
    public Type mType;

    public BaseResultCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }

    public static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types
                .canonicalize(Objects.requireNonNull(parameterized).getActualTypeArguments()[0]);
    }

    /**
     * 由于业务接口方返回的结构不规范;可能需要支持进一步解析
     * 解析失败后的进一步回调处理，放权给调用方；
     * @param e 异常
     * @param body 返回的字符串，方便扩展
     */
    public  void onError(Throwable e,String body){
        AlertUtils.closeProgressDialog();
        e.printStackTrace();
        BaseException exception = new ExceptionHandle().handleHttpException(e);
        if (!exception.message.equals(ExceptionHandle.UNKOWN)) {
            ToastUtils.showShort(exception.message);
        }
    }

    /**
     * 响应回调
     * @param response 响应对象
     */
    public abstract void onResponse(T response);
}