package org.wavefar.lib.exception;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;

import retrofit2.HttpException;


/**
 * 异常基类处理
 * @author summer
 */
public class ExceptionHandle {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int SERVICE_UNAVAILABLE = 503;
    public  static final String UNKOWN = "未知错误";

    public  BaseException handleHttpException(Throwable e) {
        BaseException ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new BaseException(e,httpException.code());
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    ex.message = "操作未授权";
                    break;
                case FORBIDDEN:
                    ex.message = "请求被拒绝";
                    break;
                case NOT_FOUND:
                    ex.message = "资源不存在";
                    break;
                case REQUEST_TIMEOUT:
                    ex.message = "服务器执行超时";
                    break;
                case INTERNAL_SERVER_ERROR:
                    ex.message = "服务器内部错误";
                    break;
                case SERVICE_UNAVAILABLE:
                    ex.message = "服务器不可用";
                    break;
                default:
                    ex.message = "网络错误";
                    break;
            }
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException || e instanceof MalformedJsonException) {
            ex = new BaseException(e, e.hashCode());
            ex.message = "解析错误";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new BaseException(e, e.hashCode());
            ex.message = "连接失败";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new BaseException(e, e.hashCode());
            ex.message = "证书验证失败";
            return ex;
        } else if (e instanceof ConnectTimeoutException) {
            ex = new BaseException(e, e.hashCode());
            ex.message = "连接超时";
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new BaseException(e, e.hashCode());
            ex.message = "连接超时";
            return ex;
        } else if (e instanceof java.net.UnknownHostException) {
            ex = new BaseException(e, e.hashCode());
            ex.message = "主机地址未知";
            return ex;
        } else {
            ex = new BaseException(e, e.hashCode());
            ex.message = UNKOWN;
            return ex;
        }
    }

}

