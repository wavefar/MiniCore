package com.lq.cxy.shop.model.http.base;

/**
 * 返回实体基类
 * @param <T>
 */
public class BaseResponseEntity<T> extends BaseStatus {
    private T result;

    private T content;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
