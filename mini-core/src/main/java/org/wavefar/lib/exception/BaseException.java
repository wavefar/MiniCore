package org.wavefar.lib.exception;

/**
 * 所有异常基类
 */
public class BaseException extends Exception {
    public String message;
    public int code;
    public BaseException(Throwable throwable,int code) {
        super(throwable);
        this.code=code;
    }
}