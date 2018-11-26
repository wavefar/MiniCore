package com.lq.cxy.shop;

import java.util.ArrayList;
import java.util.List;

/**
 * 一般常量设置
 *
 * @author summer
 * @date 2018/8/7 17:31
 */
public class Constant {
    public static final List<String> GOODS_STATUS = new ArrayList<>();

    static {
        GOODS_STATUS.add("待上架");
        GOODS_STATUS.add("已上架");
        GOODS_STATUS.add("已下架");
        GOODS_STATUS.add("待审核");
        GOODS_STATUS.add("审核未通过");
    }

    /**
     * 接口api host
     */
    public static final String BASE_HOST = BuildConfig.HOST;
    /**
     * 缓存父级目录
     */
    public static final String CACHE_ROOT = "/temp/";
    /**
     * 网络请求超时间
     */
    public static final int TIME_OUT = 10;

    /**
     * 分页每页条数
     */
    public static final int PAGE_SIZE = 20;

    /**
     * APP接口业务相关参数
     */
    public static final String APP_KEY = "";
    public static final String APP_SECRET = "";

    //==============静态资源配置======================================================================

    /**
     * 关于我们
     */
    public static final String ABOUT = "https://www.baidu.com";
    /**
     * 注册协议
     */
    public static final String AGREEMENT = "https://www.baidu.com";
    //===============公共相关=======================================================================
    /**
     * 品牌分类
     */
    public static final String GET_BRAND_ACTION = "/category/findCategory";
    /**
     * 轮播广告
     */
    public static final String GET_BANNER_ACTION = "/banner/findBanner";
    /**
     * 店铺接口
     */
    public static final String GET_NEARSTORE_BYCATEGORY_ACTION = "/store/findNearStoreByCategory";

    /**
     * 周边送水商家
     */
    public static final String GET_NEARSTORE_ACTION = "/store/findNearStore";

    /**
     * 版本检查
     */
    public static final String GET_VERSION_ACTION = "/version/checkUpdate";

    // ===============用户相关=======================================================================
    /**
     * 登录接口
     */
    public static final String LOGIN_ACTION = "/tokens/login";

    /**
     * 注销接口
     */
    public static final String LOGIN_OUT_ACTION = "/tokens/logout";
    /**
     * 注册接口
     */
    public static final String REGISTER_ACTION = "/customer/saveCustomer";

    /**
     * 更新用户资料
     */
    public static final String UPDATE_USER_ACTION = "/customer/updateCustomer";

    /**
     * 查询用户资料
     */
    public static final String GET_USER_DETAIL_ACTION = "/customer/findCustomerById";

    /**
     * 获取短信验证码
     */
    public static final String PHONE_SMS_ACTION = "/smsVerifityCode/sendMessage";
    /**
     * 验证短信验证码
     */
    public static final String VERIFITY_CODE_ACTION = "/smsVerifityCode/checkIsCorrectCode";
    /**
     * 忘记密码
     */
    public static final String FORGET_PASSWORD_ACTION = "/customer/forgetPassword";
    /**
     * 修改密码
     */
    public static final String MODIFY_PASSWORD_ACTION = "/customer/modifyPassword";
    //================商品相关=======================================================================
    /**
     * 查询商品接口
     */
    public static final String GET_GOODS_ACTION = "/goods/findGoods";

    /**
     * 商家查询所有商品列表
     */
    public static final String GET_MERCHANT_GOODS_LIST_ACTION = "/goods/findGoodsByStoreId";


    /**
     * 商户添加商品接口
     */
    public static final String POST_GOODS_ADD_MERCHANT_ACTION = "/goods/saveGoods";
    /**
     * 发布商品接口
     */
    public static final String POST_GOODS_PUBLISH_MERCHANT_ACTION = "/goods/publishGoods";
    /**
     * 商户更新商品接口
     */
    public static final String POST_GOODS_UPDATE_MERCHANT_ACTION = "/goods/updateGoods";
    /**
     * 商户商品下架接口
     */
    public static final String DELETE_GOODES_CLOSE = "/goods/closeGoods";
    /**
     * 商户刪除商品
     */
    public static final String DELETE_GOODS_ACTION = "/goods/deleteGoods";

    /**
     * 获取商品详情
     */
    public static final String GET_GOODS_DETAIL_ACTION = "/goods/findGoodsById";
    /**
     * 获取商品评价列表
     */
    public static final String GET_EVALUATE = "/evaluate/findEvaluateByGoodsId";

    /**
     * 商品评价
     */
    public static final String POST_EVALUATE = "/evaluate/saveEvaluate";

    /**
     * 单个商品购买
     */
    public static final String POST_BUY_SINGLE_GOODS_ACTION = "/order/submitSingleGoodsOrder";

    /**
     * 多个商品提交订单
     */
    public static final String SUBMIT_ORDER_ACTION = "/order/submitOrder";
    /**
     * 支付请求
     */
    public static final String ALIPAY_ACTION = "/alipay/appPay";

    /**
     * 转账到支付宝
     */
    public static final String ALIPAY_TRANSFER_MONEY_ACTION = "/alipay/transfer";

    /**
     * 用户取消订单
     */
    public static final String CANCEL_ORDER_ACTION = "/order/cancelOrder";
    /**
     * 确认收货
     */
    public static final String COMPLETE_ORDER_ACTION = "/order/completeOrder";

    /**
     * 查询当前用户的订单列表
     */
    public static final String GET_ORDERS_ACTION = "/order/findOrders";

    /**
     * 根据订单ID查询商品信息
     */
    public static final String GET_GOODS_BY_ORDERID = "/order/findGoodsByOrderId";

    /**
     * 购物车列表
     */
    public static final String GET_CART_LIST_ACTION = "/cart/findCartByCustomerId";

    /**
     * 添加到购物车
     */
    public static final String POST_CART_SAVE_ACTION = "/cart/saveCart";
    /**
     * 从购物车中删除
     */
    public static final String DELETE_FROM_SHOPCART_ACTION = "/cart/deleteCart";
    /**
     * 更新购物车
     */
    public static final String UPDATE_SHOPCART_ACTION = "/cart/updateCartNum";


    /**
     * 获取用户地址列表
     */
    public static final String GET_ADDRESS_LIST_ACTION = "/receive/findReceiveAddressByCustomerId";

    /**
     * 添加地址
     */
    public static final String POST_ADD_ADDRESS_ACTION = "/receive/saveReceiveAddress";

    /**
     * 更新地址
     */
    public static final String POST_UPDATE_ADDRESS_ACTION = "/receive/updateReceiveAddress";
    /**
     * 删除地址
     */
    public static final String DELETE_ADDRESS_ACTION = "/receive/deleteReceiveAddress";


    /**
     * 添加商品收藏
     */
    public static final String POST_ADD_FAV_GOODS_ACTION = "/follow/saveFollow";

    /**
     * 删除商品收藏
     */
    public static final String DELETE_FAV_GOODS_ACTION = "/follow/deleteFollow";
    /**
     * 删除商品收藏
     */
    public static final String GET_FAV_LIST_ACTION = "/follow/findMyFollow";


    /**
     * 申请商家入驻
     */
    public static final String POST_APPLY_MERCHANT = "/enter/saveBusinessEnter";
    /**
     * 获取商家余额
     */
    public static final String GET_MERCHANT_CAPITAL = "/capital/findMyCapital";
    /**
     * 获取商家进账明细
     */
    public static final String GET_MERCHANT_INCOME_DETAIL = "/capital/findCapitalLog";
    /**
     * 获取商家进账明细
     */
    public static final String GET_MERCHANT_OUTCOME_DETAIL = "/cashlog/findCashLog";
    /**
     * 发布申请
     */
    public static final String POST_APPLY_MERCHANT_PUBLISH = "/enter/publishBusinessEnter";
    /**
     * 商家入驻状态查询
     */
    public static final String GET_APPLY_MERCHANT = "/enter/findMyBusinessEnter";
    /**
     * 申请商家入驻信息更新
     */
    public static final String POST_UPDATE_APPLY_MERCHANT = "/enter/updateBusinessEnter";

    /**
     * 查询我的店铺
     */
    public static final String GET_QUERY_MY_SHOP_INFO = "/store/findMyStore";

    /**
     * 添加我的店铺
     */
    public static final String POST_ADD_MY_SHOP_INFO = "/store/saveStore";
    /**
     * 发布我的店铺等待审核
     */
    public static final String POST_MY_SHOP_INFO_PUBLISH = "/store/publishStore";

    /**
     * 更新我的店铺信息
     */
    public static final String POST_UPDATE_MY_SHOP_INFO = "/store/updateStore";

    /**
     * 获取商家订单列表
     */
    public static final String GET_MERCHANT_ORDER_LIST = "/business/order/findBusinessOrders";
    /**
     * 商家发货
     */
    public static final String POST_MERCHANT_SEND_PRODUCT = "/business/order/deliverGoods";
    /**
     * 商家端获取评论待回复列表
     */
    public static final String GET_WAIT_REPLY_LIST = "/reply/findEvaluateReplyByCustomerId";

    /**
     * 保存商家回复评论
     */
    public static final String POST_SAVE_MERCHANT_REPLY = "/reply/saveReply";

    /**
     * 新增提现账号绑定
     */
    public static final String POST_SAVE_PAY_ACOUNT_BINDING = "/accountbind/saveAccountBind";

    /**
     * 查询绑定的提现账号
     */
    public static final String POST_FIND_PAY_ACOUNT_LIST = "/accountbind/findMyAccountBind";

    /**
     * 删除绑定账号
     */
    public static final String DELETE_BIND_PAY_ACCOUNT = "/accountbind/deleteAccountBind";


    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final int REQUEST_CODE_PERMISSION = 17;
    public static final int REQUEST_CODE_CHOOSE = 12;

    /**
     * 那些API需要检查登录
     */
    public final static List<String> CHECK_LOGIN_API = new ArrayList<>();

    static {
        CHECK_LOGIN_API.add(GET_ADDRESS_LIST_ACTION);
        CHECK_LOGIN_API.add(POST_ADD_ADDRESS_ACTION);
        CHECK_LOGIN_API.add(POST_UPDATE_ADDRESS_ACTION);
        CHECK_LOGIN_API.add(DELETE_ADDRESS_ACTION);
        CHECK_LOGIN_API.add(POST_ADD_FAV_GOODS_ACTION);
        CHECK_LOGIN_API.add(DELETE_FAV_GOODS_ACTION);
        CHECK_LOGIN_API.add(GET_FAV_LIST_ACTION);
        CHECK_LOGIN_API.add(POST_BUY_SINGLE_GOODS_ACTION);
        CHECK_LOGIN_API.add(SUBMIT_ORDER_ACTION);
    }

    /**
     * 查询运单信息
     */
    public final static String MY_EXPRESS = "https://m.kuaidi100.com/result.jsp?nu=%s";

    /**
     * ===========================押金相关==========================================================
     */
    /**
     * 新增押金
     */
    public final static String SAVE_DEPOSIT_ACTION = "/deposit/saveDeposit";
    /**
     * 查询我的押金
     */
    public final static String GET_MY_DEPOSIT_ACTION = "/deposit/findMyDeposit";
    /**
     * 支付押金
     */
    public final static String DEPOSIT_PAY_ACTION = "/alipay/depositPay";

    /**
     * 取消押金订单
     */
    public final static String DELETE_DEPOSIT_ACTION = "/deposit/deleteDeposit";
    /**
     * 消费者发起退押金
     */
    public final static String REFUND_DEPOSIT_ACTION = "/deposit/applyRefund";

    /**
     * 查询商家押金
     */
    public final static String GET_DEPSIT_MERCHANT_ACTION = "/deposit/findDeposit";
    /**
     * 商家退押金
     */
    public final static String POST_MERCHANT_PUSH_BACK_CASH = "/deposit/refund";
}

