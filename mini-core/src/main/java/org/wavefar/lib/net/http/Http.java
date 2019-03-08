package org.wavefar.lib.net.http;


import org.wavefar.lib.BuildConfig;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ConnectionPool;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * HTTP网络配置
 * 1.OkHttpClient配置
 * 2.Retrofit配置
 * 3.Builder构建初始化数据
 * @author summer
 */
public class Http {

    private HttpBuilder mBuilder;
    private OkHttpClient mClient;
    private Retrofit mRetrofit;

    private Http(HttpBuilder builder) {
        mBuilder = builder;
        configOKHttp();
        configRetrofit();
    }

    private void configOKHttp() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (mBuilder.enableDebug) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        for (Interceptor interceptor : mBuilder.interceptors) {
            builder.addInterceptor(interceptor);
        }
        //ssl验证
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        if (mBuilder.enableSSL) {
            builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager).hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
        }

        mClient = builder.retryOnConnectionFailure(true)
                 .connectTimeout(mBuilder.timeout, TimeUnit.SECONDS)
                 .readTimeout(mBuilder.timeout,TimeUnit.SECONDS)
                 .writeTimeout(mBuilder.timeout, TimeUnit.SECONDS)
                 .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                 .cookieJar(mBuilder.cookieJar).build();
    }

    private void configRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(mBuilder.baseUrl)
                .client(mClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * HTTP 构建类
     */
    public static final class HttpBuilder {
        private String baseUrl;
        private CookieJar cookieJar;
        private long timeout = 20;
        private boolean enableSSL;
        private boolean enableDebug = BuildConfig.DEBUG;
        private ArrayList<Interceptor> interceptors = new ArrayList<>();

        public HttpBuilder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public HttpBuilder setCookieJar(CookieJar cookieJar) {
            this.cookieJar = cookieJar;
            return this;
        }

        public HttpBuilder setTimeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        public HttpBuilder addInterceptor(Interceptor interceptor) {
            interceptors.add(interceptor);
            return this;
        }
        public HttpBuilder enableSSL(boolean enableSSL) {
            this.enableSSL = enableSSL;
            return  this;
        }
        public HttpBuilder enableDebug(boolean enableDebug) {
            this.enableDebug = enableDebug;
            return  this;
        }

        public Http build() {
            return new Http(this);
        }
    }


    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public OkHttpClient getClient() {
        return mClient;
    }

    public <T> T getService(Class<T> t) {
        return getRetrofit().create(t);
    }

    /**
     * /**
     * execute your customer API
     * For example:
     * MyApiService service =
     * BaseApplication.getInstance().getHttp().getService(MyApiService.class);
     * <p>
     * BaseApplication.getInstance().getHttp()
     * .execute(service.lgon("name", "password"), subscriber)
     * * @param subscriber
     */

    public <T> T execute(Observable<T> observable, Observer<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return null;
    }
}
