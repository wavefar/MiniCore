package org.wavefar.lib.net.interceptor;

import org.wavefar.lib.utils.LogUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求头封装
 * @author summer
 */
public class HeaderInterceptor implements Interceptor {
    private Map<String, String> headers;

    public HeaderInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder();
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            String headerValue;
            for (String headerKey : keys) {
                headerValue = headers.get(headerKey);
                if (null == headerValue) {
                    continue;
                }
                builder.addHeader(headerKey, headerValue);
                LogUtil.d("OkHttp:Header",String.format("%s=%s",headerKey,headerValue));
            }
        }
        //请求信息
        return chain.proceed(builder.build());
    }
}