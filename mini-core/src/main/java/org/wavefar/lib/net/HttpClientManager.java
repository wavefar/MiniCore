package org.wavefar.lib.net;

import org.wavefar.lib.BuildConfig;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.net.callback.RxBusSubscriber;
import org.wavefar.lib.net.cookie.PersistentCookieStore;
import org.wavefar.lib.net.http.Http;
import org.wavefar.lib.net.interceptor.CacheInterceptor;
import org.wavefar.lib.net.interceptor.HeaderInterceptor;
import org.wavefar.lib.utils.JsonUtils;
import org.wavefar.lib.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 对http的所有请求进行封装管理
 *
 * @author: summer
 * @date: 2018/8/16 11:02
 */
public class HttpClientManager {

    private final Http mHttp;
    private static Map<String, String> mHeaders = new HashMap<>();

    private static class SingletonHolder {
        private static HttpClientManager INSTANCE = new HttpClientManager(mHeaders);
    }

    /**
     * 构建网络请求工具
     *
     * @return
     */
    public static HttpClientManager getInstance() {
        return getInstance(null);
    }

    /**
     * 构建网络请求工具
     *
     * @param headers 全局公共头信息
     * @return
     */
    public static HttpClientManager getInstance(Map<String, String> headers) {
        if (headers != null) {
            mHeaders.putAll(headers);
        }
        return SingletonHolder.INSTANCE;
    }


    private HttpClientManager(Map<String, String> headers) {
        Http.HttpBuilder httpBuilder = new Http.HttpBuilder()
                .setBaseUrl(BuildConfig.HOST);
        if (headers != null) {
            httpBuilder.addInterceptor(new HeaderInterceptor(headers));
        }
        //默认支持缓存
        httpBuilder.addInterceptor(new CacheInterceptor());
        //默认支持cookie
        mHttp = httpBuilder.setCookieJar(new PersistentCookieStore(Utils.getApp()))
                .build();
    }

    /**
     * 设置新的header,会清空之前设置的header
     *
     * @param map
     */
    public HttpClientManager setHeader(Map<String, String> map) {
        if (map != null) {
            mHeaders.clear();
            mHeaders.putAll(map);
        }
        return this;
    }

    /**
     * 添加header
     *
     * @param map
     * @return
     */
    public HttpClientManager addHeader(Map<String, String> map) {
        if (map != null) {
            mHeaders.putAll(map);
        }
        return this;
    }

    /**
     * 返回resetAPI
     *
     * @return
     */
    private BaseRestApi getRestService() {
        return mHttp.getService(BaseRestApi.class);
    }

    /**
     * 返回HTTP可以自定义
     *
     * @return
     */
    public Http getHttp() {
        return mHttp;
    }

    /**
     * get请求
     *
     * @param url
     */
    public void get(String url, BaseResultCallback<?> resultCallback) {
        get(url, null, resultCallback);
    }


    /**
     * get请求
     *
     * @param url
     * @param params         请求参数可以为null
     * @param resultCallback 回调
     */
    public void get(String url, Map<String, String> params, BaseResultCallback<?> resultCallback) {

        Observable<ResponseBody> observable;
        if (params != null) {
            observable = getRestService().get(url, params);
        } else {
            observable = getRestService().get(url);
        }

        response(resultCallback, observable);
    }

    /**
     * post 无参数请求
     *
     * @param url
     * @param resultCallback
     */
    public void post(String url, BaseResultCallback<?> resultCallback) {
        post(url, null, resultCallback);
    }

    /**
     * post 带参数请求
     *
     * @param url
     * @param params         可以为null
     * @param resultCallback
     */
    public void post(String url, Map<String, String> params, BaseResultCallback<?> resultCallback) {
        Observable<ResponseBody> observable;
        if (params != null) {
            observable = getRestService().postForm(url, params);
        } else {
            observable = getRestService().post(url);
        }

        response(resultCallback, observable);
    }

    /**
     * @param url
     * @param body           请求raw数据 如json字符串
     * @param resultCallback
     */
    public void postBody(String url, String body, BaseResultCallback<?> resultCallback) {
        response(resultCallback, getRestService().postBody(url, body));
    }


    /**
     * 表单请求，带多文件上传
     * @param url
     * @param params         请求参数
     * @param fileMap        请求的文件，支持任何类型文件上传
     * @param resultCallback
     */
    public void postFiles(String url, Map<String, String> params, Map<String, File> fileMap, BaseResultCallback<?> resultCallback) {
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (fileMap != null && fileMap.size() > 0) {
            File file;
            for (String key: fileMap.keySet()) {
                file = fileMap.get(key);
                if (file == null) {
                    continue;
                }
                requestBody.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
            }
        }

        if (params != null && params.size() > 0 ) {
            String value;
            for (String key: params.keySet()) {
                value = params.get(key);
                if (value == null) {
                    continue;
                }
                requestBody.addFormDataPart(key, value);
            }
        }

        response(resultCallback, getRestService().post(url, requestBody.build()));

    }


    /**
     * delete 请求封装
     *
     * @param url
     * @param params         可以为null
     * @param resultCallback
     */
    public void delete(String url, Map<String, String> params, BaseResultCallback<?> resultCallback) {
        Observable<ResponseBody> observable;
        if (params != null) {
            observable = getRestService().delete(url, params);
        } else {
            observable = getRestService().delete(url);
        }

        response(resultCallback, observable);
    }


    /**
     * 统一回调处理
     *
     * @param mCallback  回调接口
     * @param observable 观察者对象
     * @param <T>
     */
    private <T> void response(final BaseResultCallback<T> mCallback, Observable<ResponseBody> observable) {
        observable.compose(RxUtils.schedulersTransformer())
                .subscribe(new RxBusSubscriber<ResponseBody>() {
                    @Override
                    protected void onEvent(ResponseBody o) {
                        if (mCallback == null) {
                            return;
                        }
                        if (mCallback.mType != null) {

                            try {
                                String rawStr = o.string();

                                if (mCallback.mType == String.class) {
                                    exeSuccessCallback(rawStr, mCallback);
                                } else {
                                    Object object = JsonUtils.fromJson(rawStr,
                                            mCallback.mType);
                                    if (object == null) {
                                        Exception e = new Exception(
                                                "JsonUtils.fromJson(finalStr,callback.mType) return null!");
                                        exeFailedCallback(rawStr, e, mCallback);
                                    } else {
                                        exeSuccessCallback(object, mCallback);
                                    }
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                                exeFailedCallback(null, e, mCallback);
                            }

                        } else {
                            Throwable e = new Throwable("mCallback.mType 为空!!!");
                            exeFailedCallback(null, e, mCallback);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        exeFailedCallback(null, e, mCallback);
                    }
                });
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    private void exeFailedCallback(String body, Throwable e, BaseResultCallback callback) {
        if (callback != null) {
            callback.onError(e, body);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void exeSuccessCallback(Object o, BaseResultCallback callback) {
        if (callback != null) {
            callback.onResponse(o);
        }
    }


}

