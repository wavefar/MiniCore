package org.wavefar.lib.net.callback;


import org.wavefar.lib.exception.BaseException;
import org.wavefar.lib.exception.ExceptionHandle;
import org.wavefar.lib.utils.ToastUtils;

import io.reactivex.observers.DisposableObserver;

/**
 * 统一回调处理
 * 为RxBus使用的Subscriber, 主要提供next事件的try,catch
 * @author summer
 */
public abstract class RxBusSubscriber<T> extends DisposableObserver<T> {

    @Override
    public void onNext(T t) {
        try {
            onEvent(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        BaseException exception = new ExceptionHandle().handleHttpException(e);
        ToastUtils.showShort(exception.message);
    }

    protected abstract void onEvent(T t);
}
