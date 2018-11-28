package org.wavefar.lib.bus;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


/**
 * 只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
 * @author Administrator
 */
public class RxBus {
    private static volatile RxBus mDefaultInstance;
    private final Subject<Object> mBus;

    private final Map<Class<?>, Object> mStickyEventMap;

    public RxBus() {
        mBus = PublishSubject.create().toSerialized();
        mStickyEventMap = new ConcurrentHashMap<>();
    }

    public static RxBus getDefault() {
        if (mDefaultInstance == null) {
            synchronized (RxBus.class) {
                if (mDefaultInstance == null) {
                    mDefaultInstance = new RxBus();
                }
            }
        }
        return mDefaultInstance;
    }

    /**
     * 发送事件
     * @param event
     */
    public void post(Object event) {
        mBus.onNext(event);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }

    /**
     * 判断是否有订阅者
     * @return
     */
    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    public void reset() {
        mDefaultInstance = null;
    }


    /**
     * 发送一个新Sticky事件
     * @param event
     */
    public void postSticky(Object event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(event.getClass(), event);
        }
        post(event);
    }
    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservableSticky(final Class<T> eventType) {
        synchronized (mStickyEventMap) {
            Observable<T> observable = mBus.ofType(eventType);
            final Object event = mStickyEventMap.get(eventType);

            if (event != null) {
                return Observable.merge(observable, Observable.create(new ObservableOnSubscribe<T>() {
                    @Override
                    public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                        emitter.onNext(eventType.cast(event));
                    }
                }));
            } else {
                return observable;
            }
        }
    }

    /**
     * 根据eventType获取Sticky事件
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.get(eventType));
        }
    }

    /**
     *  移除指定eventType的Sticky事件
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }
}