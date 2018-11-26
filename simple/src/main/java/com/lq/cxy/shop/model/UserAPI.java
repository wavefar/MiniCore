package com.lq.cxy.shop.model;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.lq.cxy.shop.Constant;
import com.lq.cxy.shop.activity.LoginActivity;
import com.lq.cxy.shop.activity.MainActivity;
import com.lq.cxy.shop.model.entity.BrandEntity;
import com.lq.cxy.shop.model.entity.DepositEntity;
import com.lq.cxy.shop.model.entity.LicenseInfoEntity;
import com.lq.cxy.shop.model.entity.MerchantBindingAccountEntity;
import com.lq.cxy.shop.model.entity.ShopAddEntity;
import com.lq.cxy.shop.model.entity.UserEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.http.base.BaseSimpleResultCallback;

import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.AppUtils;
import org.wavefar.lib.utils.IntentUtils;
import org.wavefar.lib.utils.JsonUtils;
import org.wavefar.lib.utils.SharePreferenceUtils;
import org.wavefar.lib.utils.Utils;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

/**
 * 用户相关接口
 *
 * @author summer
 */
public class UserAPI extends BaseAPI {


    private static final String FILE = "saveUserInfo";

    private static final String USERINFO_KEY = "userInfo";
    private static final String SHOPINFO_KEY = "shopInfo";
    private static final String CATEINFO_KEY = "cateInfo";
    private static final String LISENCE_KEY = "lisenceInfo";


    /**
     * 获取手机短信验证码
     *
     * @param mobile   手机号码
     * @param listener
     */
    public void getPhoneSMS(String mobile, BaseResultCallback<BaseResponseEntity<?>> listener) {
        TreeMap<String, String> hashMap = new TreeMap<>();
        hashMap.put("mobile", mobile);
        get(Constant.PHONE_SMS_ACTION, hashMap, listener);
    }

    /**
     * 短信验证
     *
     * @param mobile       手机号码
     * @param identifyCode 验证码
     * @param listener
     */
    public void smsVerifityCode(String mobile, String identifyCode, BaseResultCallback<BaseResponseEntity<?>> listener) {
        TreeMap<String, String> hashMap = new TreeMap<>();
        hashMap.put("mobile", mobile);
        hashMap.put("identifyCode", identifyCode);
        get(Constant.VERIFITY_CODE_ACTION, hashMap, listener);
    }

    /**
     * 忘记密码
     *
     * @param phoneNum     手机号码
     * @param newPassword  新密码
     * @param verifityCode 验证码
     * @param listener
     */
    public void forgetPassword(String phoneNum, String newPassword, String verifityCode, BaseResultCallback<BaseResponseEntity<?>> listener) {
        TreeMap<String, String> hashMap = new TreeMap<>();
        hashMap.put("phoneNum", phoneNum);
        hashMap.put("newPassword", newPassword);
        hashMap.put("verifityCode", verifityCode);
        post(Constant.FORGET_PASSWORD_ACTION, hashMap, listener);
    }


    /**
     * 修改密码
     *
     * @param newPassword 新密码
     * @param listener
     */
    public void modifyPassword(String newPassword, BaseResultCallback<BaseResponseEntity<?>> listener) {
        TreeMap<String, String> hashMap = new TreeMap<>();
        hashMap.put("newPassword", newPassword);
        post(Constant.MODIFY_PASSWORD_ACTION, hashMap, listener);
    }

    /**
     * 本站登陆入口
     *
     * @param username 手机号码
     * @param passWord md5加密密码,使用 DesUtil.MD5()
     * @param listener 回调接口
     */
    public void login(String username, String passWord,
                      BaseSimpleResultCallback<BaseResponseEntity<UserEntity>, UserEntity> listener) {
        TreeMap<String, String> hashMap = new TreeMap<>();
        hashMap.put("username", username);
        hashMap.put("password", passWord);
        post(Constant.LOGIN_ACTION, hashMap, listener);
    }


    /**
     * 注册接口
     *
     * @param userEntity 不确定需要传递些什么参数，传递一个实体类；
     * @param listener
     */
    public void register(UserEntity userEntity, BaseResultCallback<BaseResponseEntity<UserEntity>> listener) {
        try {
            TreeMap<String, String> hashMap = JsonUtils.convertBeanToMap(userEntity);
            postFile(Constant.REGISTER_ACTION, hashMap, null, listener);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * 更新用户信息
     *
     * @param userEntity 不确定需要传递些什么参数，传递一个实体类；
     * @param avatar     头像
     * @param listener
     */
    public void update(UserEntity userEntity, File avatar, BaseResultCallback<BaseResponseEntity> listener) {
        try {
            TreeMap<String, String> hashMap = JsonUtils.convertBeanToMap(userEntity);
            TreeMap<String, File> fileMap = new TreeMap<>();
            if (avatar != null) {
                fileMap.put("file", avatar);
            }
            postFile(Constant.UPDATE_USER_ACTION, hashMap, fileMap, listener);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public static void saveCateInfo(List<BrandEntity> cateInfo) {
        SharePreferenceUtils.putString(FILE, Utils.getApp(), CATEINFO_KEY, JsonUtils.toJson(cateInfo));
    }

    public static List<BrandEntity> getCateINfo() {
        String jsonStr = SharePreferenceUtils.getString(FILE, Utils.getApp(), CATEINFO_KEY);
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        return JsonUtils.fromJson(jsonStr, new TypeToken<List<BrandEntity>>() {
        }.getType());
    }

    public static void saveLisenceInfo(LicenseInfoEntity lisence) {
        SharePreferenceUtils.putString(FILE, Utils.getApp(), LISENCE_KEY, JsonUtils.toJson(lisence));
    }

    public static LicenseInfoEntity getLisenceInfo() {
        String jsonStr = SharePreferenceUtils.getString(FILE, Utils.getApp(), LISENCE_KEY);
        return JsonUtils.fromJson(jsonStr, LicenseInfoEntity.class);
    }

    public static void saveShopInfo(ShopAddEntity shopInfo) {
        SharePreferenceUtils.putString(FILE, Utils.getApp(), SHOPINFO_KEY, JsonUtils.toJson(shopInfo));
    }

    public static ShopAddEntity getShopInfo() {
        String jsonStr = SharePreferenceUtils.getString(FILE, Utils.getApp(), SHOPINFO_KEY);
        return JsonUtils.fromJson(jsonStr, ShopAddEntity.class);
    }

    /**
     * 保存用户登录信息
     *
     * @param userInfo
     * @return
     */
    public static void saveUserInfo(UserEntity userInfo) {
        SharePreferenceUtils.putString(FILE, Utils.getApp(), USERINFO_KEY, JsonUtils.toJson(userInfo));
    }

    /**
     * 获取用户登录信息 如token、id
     */
    public static UserEntity getUserInfo() {
        String jsonStr = SharePreferenceUtils.getString(FILE, Utils.getApp(), USERINFO_KEY);
        return JsonUtils.fromJson(jsonStr, UserEntity.class);
    }

    /**
     * 检查是否登陆中
     *
     * @return
     */
    public static boolean checkLogin() {
        UserEntity userEntity = getUserInfo();
        if (userEntity != null) {
            return !TextUtils.isEmpty(userEntity.getToken());
        }
        return false;
    }

    /**
     * 清理登录信息
     *
     * @return
     */
    public static void clearLogin() {
        SharePreferenceUtils.clear(FILE, Utils.getApp());
    }


    /**
     * 没登录回调跳转主页
     */
    public static void unLoginPreHome() {
        unLoginPreHome(null);
    }

    /**
     * 没登录回调跳转主页
     *
     * @param bundle 参数
     */
    public static void unLoginPreHome(Bundle bundle) {
        Bundle bundles = new Bundle();
        bundles.putString(LoginActivity.PRE_CLASSNAME_KEY, MainActivity.class.getName());
        if (bundle != null) {
            bundle.putAll(bundle);
        }
        IntentUtils.redirectAndPrameter(LoginActivity.class, bundles);
    }

    /**
     * 注销当前用户
     */
    public void logout(Context mActivity) {
        AppUtils.exitApp();
        clearLogin();
        clearUser(null);
        unLoginPreHome();
        if (mActivity instanceof Activity) {
            ((Activity) mActivity).finish();
        }
    }

    /**
     * 注销
     */
    public void clearUser(BaseResultCallback<?> listener) {
        delete(Constant.LOGIN_OUT_ACTION, null, listener);
    }


    //====================我的订单==================================

    /**
     * 获取商品列表
     *
     * @param pageNum   当前页码
     * @param orderType 订单状态类型
     * @param listener
     */
    public void getOrders(int pageNum, int orderType, BaseResultCallback<?> listener) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("status", String.valueOf(orderType));
        treeMap.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        treeMap.put("pageNum", String.valueOf(pageNum));
        get(Constant.GET_ORDERS_ACTION, treeMap, listener);
    }

    /**
     * 通过订单ID查询订单详情
     *
     * @param orderId
     * @param listener
     */
    public void getOrderDetail(String orderId, BaseResultCallback<?> listener) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("orderId", orderId);
        get(Constant.GET_GOODS_BY_ORDERID, treeMap, listener);
    }

    /**
     * 支付订单
     *
     * @param orderId
     * @param resultCallback
     */
    public void pay(String orderId, BaseResultCallback<BaseResponseEntity> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("orderId", orderId);
        post(Constant.ALIPAY_ACTION, treeMap, resultCallback);
    }

    public void transferMoney(MerchantBindingAccountEntity account, double money, BaseResultCallback<BaseResponseEntity> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("bindId", account.getId());
        treeMap.put("cash", String.valueOf(money));
        post(Constant.ALIPAY_TRANSFER_MONEY_ACTION, treeMap, resultCallback);
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @param resultCallback
     */
    public void cancelOrder(String orderId, BaseResultCallback<BaseResponseEntity> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("orderId", orderId);
        delete(Constant.CANCEL_ORDER_ACTION, treeMap, resultCallback);
    }

    /**
     * 确认收货订单
     *
     * @param orderId
     * @param resultCallback
     */
    public void finishOrder(String orderId, BaseResultCallback<BaseResponseEntity> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("orderId", orderId);
        post(Constant.COMPLETE_ORDER_ACTION, treeMap, resultCallback);
    }

    /**
     * 通过用户ID查询用户详情
     *
     * @param customerId
     * @param listener
     */
    public void getUserDetail(String customerId, BaseResultCallback<?> listener) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("customerId", customerId);
        get(Constant.GET_USER_DETAIL_ACTION, treeMap, listener);
    }

    /**
     * 获取我的押金
     */
    public void getMyDeposit(BaseResultCallback<?> resultCallback) {
        get(Constant.GET_MY_DEPOSIT_ACTION, null, resultCallback);
    }

    /**
     * 创建一个押金订单
     *
     * @param depositEntity
     * @param resultCallback
     */
    public void saveDeposit(DepositEntity depositEntity, BaseResultCallback<?> resultCallback) {
        try {
            TreeMap<String, String> treeMap = JsonUtils.convertBeanToMap(depositEntity);
            post(Constant.SAVE_DEPOSIT_ACTION, treeMap, resultCallback);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付押金
     *
     * @param depositId
     * @param resultCallback
     */
    public void depositPay(String depositId, BaseResultCallback<?> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("depositId", depositId);
        post(Constant.DEPOSIT_PAY_ACTION, treeMap, resultCallback);
    }

    /**
     * 取消押金订单
     *
     * @param depositId
     * @param resultCallback
     */
    public void cancelDeposit(String depositId, BaseResultCallback<?> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("depositId", depositId);
        delete(Constant.DELETE_DEPOSIT_ACTION, treeMap, resultCallback);
    }

    /**
     * 退押金
     *
     * @param depositId
     * @param resultCallback
     */
    public void refundDeposit(String depositId, BaseResultCallback<?> resultCallback) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("depositId", depositId);
        post(Constant.REFUND_DEPOSIT_ACTION, treeMap, resultCallback);
    }
}
