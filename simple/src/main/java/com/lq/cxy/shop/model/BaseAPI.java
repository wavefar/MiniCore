package com.lq.cxy.shop.model;

import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.activity.LoginActivity;
import com.lq.cxy.shop.model.entity.UserEntity;

import org.wavefar.lib.net.HttpClientManager;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.DeviceUtils;
import org.wavefar.lib.utils.EncryptUtils;
import org.wavefar.lib.utils.IntentUtils;
import org.wavefar.lib.utils.LogUtil;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 所有API的基类封装系统级参数
 * @author summer
 * @description
 * @date 2014年5月28日 上午11:59:42
 */
public class BaseAPI {

    protected UserEntity userEntity;
    protected HttpClientManager httpClientManager;

    public BaseAPI() {
        userEntity = UserAPI.getUserInfo();
        TreeMap<String, String> header = new TreeMap<String, String>();
        // 登陆后增加authorization
        if (userEntity != null) {
            header.put("authorization", String.format("%s_%s", userEntity.getUserId(), userEntity.getToken()));
        } else {
            header = null;
        }
        httpClientManager = HttpClientManager.getInstance(false).setHeader(header);
    }

    /**
     * 部分字段签名，返回签名字符串 md5(action+timestamp+app_key+app_secret)
     *
     * @param time   当前时间戳
     * @param action 请求的方法
     * @return
     * @deprecated
     */
    private String sign(long time, String action) {
        StringBuilder sb = new StringBuilder();
        sb.append(action);
        sb.append(time);
        sb.append(Constant.APP_KEY);
        sb.append(Constant.APP_SECRET);
        try {
            return EncryptUtils.encodeMD5(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private TreeMap<String, String> getSystemTreeMap(TreeMap<String, String> hashMap) {
        long time = System.currentTimeMillis();
        TreeMap<String, String> hashMapStr = new TreeMap<>();
        if (hashMap == null) {
            hashMap = new TreeMap<>();
        }
        if (null != userEntity) {
            hashMap.put("customerId", userEntity.getUserId());
        }

        hashMap.put("deviceId", DeviceUtils.getDeviceId());
        hashMap.put("timestamp", String.valueOf(time));
        hashMapStr.putAll(hashMap);
        hashMapStr.put("sign", sign(hashMapStr));
        hashMapStr.put("appKey", Constant.APP_KEY);
        return hashMapStr;
    }

    /**
     * 全参数构造签名字符串 md5(APP_SECRET+Parmas+APP_SECRET)
     *
     * @return
     */
    private String sign(TreeMap<String, String> map) {
        String paramStr = getUrlParmas(map);
        paramStr = String.format("%s%s%s", paramStr, Constant.APP_KEY, Constant.APP_SECRET);
        LogUtil.i("BaseAPI待签名字符串", paramStr);
        try {
            return EncryptUtils.encodeMD5(paramStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * post 方式请求
     *
     * @param action   请求的方法名称
     * @param hashMap  请求业务参数,可以为null
     * @param listener
     */
    public void post(String action, TreeMap<String, String> hashMap,
                     BaseResultCallback<?> listener) {
        if (checkLoginApi(action)) {
            httpClientManager.post(String.format("%s%s", Constant.BASE_HOST, action), getSystemTreeMap(hashMap), listener);
        }
    }

    /**
     * get方式请求
     * @param action   请求的地址
     * @param hashMap  请求业务参数 可以为null
     * @param listener
     */
    public void get(String action, TreeMap<String, String> hashMap, BaseResultCallback<?> listener) {
        if (checkLoginApi(action)) {
            httpClientManager.get(String.format("%s%s", Constant.BASE_HOST, action), getSystemTreeMap(hashMap), listener);
        }
    }

    /**
     * get方式请求
     * @param action   请求的地址
     * @param hashMap  请求业务参数 可以为null
     * @param listener
     */
    public void delete(String action, TreeMap<String, String> hashMap, BaseResultCallback<?> listener) {
        if (checkLoginApi(action)) {
            httpClientManager.delete(String.format("%s%s", Constant.BASE_HOST, action), getSystemTreeMap(hashMap), listener);
        }
    }

    /**
     * 带文件的post 多表单提交
     * @param action  请求的地址
     * @param hashMap 请求参数 可以为null
     * @param files 文件参数 可以为null
     * @param listener
     */
    public void postFile(String action, TreeMap<String,String> hashMap, TreeMap<String,File> files, BaseResultCallback<?> listener) {
        if (checkLoginApi(action)) {
            httpClientManager.postFiles(String.format("%s%s", Constant.BASE_HOST, action), getSystemTreeMap(hashMap), files, listener);
        }
    }

    /**
     * 遍历map构造get参数字符串，此方法时候传统的get key=value& 参数 ...
     *
     * @param map
     * @return 返回get 全路径
     */
    private String getUrlParmas(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        if (sb.length() == 0) {
            return "";
        } else {
            return sb.substring(0, sb.length() - 1);
        }
    }

    /**
     * 将数据集合转化拼成字符串
     * @param collection 集合
     * @param delimiter  分隔符
     * @return
     */
    public static String join(Collection<?> collection, String delimiter) {
        StringBuilder builder = new StringBuilder();
        Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next());
            if (iterator.hasNext()) {
                builder.append(delimiter);
            }
        }
        return builder.toString();
    }


    /**
     * 检查api是否需要登录
     * @param action
     * @return
     */
    private boolean checkLoginApi(String action) {
        if (userEntity == null && Constant.CHECK_LOGIN_API.contains(action)){
            IntentUtils.redirect(LoginActivity.class);
            return false;
        }
        return  true;
    }
}
