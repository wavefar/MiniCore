package com.lq.cxy.shop.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lq.cxy.shop.R;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.entity.AddressEntity;
import com.lq.cxy.shop.model.entity.OrderEntity;
import com.lq.cxy.shop.model.entity.ProductEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.utils.PayUtil;

import org.greenrobot.eventbus.EventBus;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.StringUtil;
import org.wavefar.lib.utils.ToastUtils;

import java.util.List;

/**
 * 订单详情
 * @author summer
 */
public class OrderDetailFragment extends Fragment implements View.OnClickListener {

    public static final String ORDER_KEY = "ORDER_KEY";

    private UserAPI mUserAPI;
    //订单
    private OrderEntity mOrderEntity;
    private LinearLayout productLl;
    private PayUtil mPayUtil;
    //立即支付按钮\取消\确认收货
    private Button mPayBtn,mCancelBtn,mConfirmBtn;
    private TextView totalPrice;
    private TextView orderNoTv;
    private TextView orderDateTv;
    private TextView usernameTv;
    private TextView linktelTv;
    private TextView shipaddressTv;
    private TextView disTypeTv;
    private TextView disDateTimeTv;

    //状态是否变化
    private boolean isChange;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        bindData(view);
        return  view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mOrderEntity = getArguments().getParcelable(ORDER_KEY);
        mUserAPI = new UserAPI();
        mPayUtil = new PayUtil(getActivity());
    }


    private void bindData(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        TextView titleTv = view.findViewById(R.id.toolbar_title);
        titleTv.setText("订单详情");

        mPayBtn = view.findViewById(R.id.paybtn);
        mCancelBtn = view.findViewById(R.id.cancelBtn);
        mConfirmBtn = view.findViewById(R.id.confirmBtn);
        mPayBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);
        productLl = view.findViewById(R.id.product_ll);
        totalPrice = view.findViewById(R.id.total_price);
        orderNoTv = view.findViewById(R.id.order_NoTv);
        orderDateTv = view.findViewById(R.id.orderDateTv);

        disTypeTv = view.findViewById(R.id.disTypeTv);
        disDateTimeTv = view.findViewById(R.id.disDateTimeTv);
        usernameTv = view.findViewById(R.id.usernameTv);
        linktelTv = view.findViewById(R.id.linktelTv);
        shipaddressTv = view.findViewById(R.id.shipaddressTv);
    }


    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void bindData() {
        if (mOrderEntity == null) {
            return;
        }

        changeBtn();


        orderNoTv.setText(String.format("订单号：%s",mOrderEntity.getOrderCode()));
        orderDateTv.setText(String.format("订单时间：%s", mOrderEntity.getCreateTime()));
        totalPrice.setText(String.format("订单总额：%s", StringUtil.formatSignMoney(mOrderEntity.getMoney())));

        disTypeTv.setText(String.format("提货方式：%s","0".equals(mOrderEntity.getDisType()) ? "邮寄" :"自提"));
        if (!TextUtils.isEmpty(mOrderEntity.getDisDate())) {
            disDateTimeTv.setText(String.format("配送时间：%s %s",mOrderEntity.getDisDate(),mOrderEntity.getDisTime()));
        } else {
            disDateTimeTv.setVisibility(View.GONE);
        }
        //收货人信息
        AddressEntity addressEntity = mOrderEntity.getAddress();
        if (addressEntity != null) {
            usernameTv.setText(String.format("收货人：%s",addressEntity.getUserName()));
            linktelTv.setText(String.format("收货人电话：%s",addressEntity.getPhoneNum()));
            shipaddressTv.setText(String.format("收货地址：%s",addressEntity.getAddress()));
        } else {
            usernameTv.setVisibility(View.GONE);
            linktelTv.setVisibility(View.GONE);
            shipaddressTv.setVisibility(View.GONE);
        }

        List<ProductEntity> products = mOrderEntity.getGoods();
        if (products != null) {
            addProduct(products);
        }
    }

    /**
     * 初始化商品列表
     */
    private void addProduct(List<ProductEntity> products) {

        productLl.removeAllViews();
        for (ProductEntity productEntity : products) {
            LinearLayout ll = new LinearLayout(getContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1);
            TextView nameTv = new TextView(getContext());
            nameTv.setLayoutParams(lp);
            nameTv.setPadding(10, 10, 10, 10);
            nameTv.setText(productEntity.getGoodsName());
            nameTv.setTextColor(0xFF331313);

            //当前购买数量
            TextView numTv = new TextView(getContext());
            numTv.setPadding(10, 10, 10, 10);
            numTv.setText(productEntity.getGoodsName());
            numTv.setTextColor(0xFF331313);
            numTv.setText(String.format("X%s",productEntity.getNum()));

            TextView priceTv = new TextView(getContext());
            priceTv.setPadding(10, 10, 10, 10);
            priceTv.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
            priceTv.setTextColor(0xFFdc1313);
            priceTv.setText(StringUtil.formatSignMoney(productEntity.getPrice()));
            ll.addView(nameTv);
            ll.addView(numTv);
            ll.addView(priceTv);
            productLl.addView(ll);
        }
    }

    /**
     * 网络获取数据
     */
    private void initData() {
        if (mOrderEntity == null) {
            return;
        }

        mUserAPI.getOrderDetail(String.valueOf(mOrderEntity.getId()), new BaseResultCallback<BaseResponseEntity<OrderEntity>>() {
            @Override
            public void onResponse(BaseResponseEntity<OrderEntity> response) {
                if(response.isSuccess()) {
                     mOrderEntity = response.getContent();
                    bindData();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回
            case R.id.iv_back:
                if (isChange) {
                    EventBus.getDefault().post(new OrderFragment.OrderEvent(){});
                }
                getActivity().finish();
                break;
            //支付订单
            case R.id.paybtn:
                if (mOrderEntity == null) {
                    ToastUtils.showShort( "数据有误！");
                    return;
                }
                checkOrder();
                break;
            //取消订单
            case R.id.cancelBtn:
                if (mOrderEntity == null) {
                    ToastUtils.showShort( "数据有误！");
                    return;
                }
                cancelOrder();
                break;
            //确认收货
            case R.id.confirmBtn:
                if (mOrderEntity == null) {
                    ToastUtils.showShort( "数据有误！");
                    return;
                }
                confirmOrder();
                break;
                default:break;
        }
    }

    /**
     * 确定收货
     */
    private void confirmOrder() {
        mUserAPI.finishOrder(mOrderEntity.getId(), new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                ToastUtils.showShort(response.getMessage());
                if (response.isSuccess()) {
                    mOrderEntity.setStatus(4);
                    isChange = true;
                    changeBtn();
                }
            }
        });
    }

    /**
     * 取消订单
     */
    private void cancelOrder() {
        mUserAPI.cancelOrder(mOrderEntity.getId(), new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                ToastUtils.showShort(response.getMessage());
                if (response.isSuccess()) {
                    mOrderEntity.setStatus(4);
                    isChange = true;
                    changeBtn();
                }
            }
        });
    }

    /**
     * 改变按钮样式
     */
    private void changeBtn() {
        //可以支付、可以取消
        if (0 == mOrderEntity.getStatus()) {
            mPayBtn.setVisibility(View.VISIBLE);
            mCancelBtn.setVisibility(View.VISIBLE);
        } else {
            mPayBtn.setVisibility(View.GONE);
            mCancelBtn.setVisibility(View.GONE);
        }

        //已经发货，显示确认收货操作
        if (3 == mOrderEntity.getStatus()) {
            mConfirmBtn.setVisibility(View.VISIBLE);
        } else {
            mConfirmBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 检查订单是否支付
     */
    private void checkOrder() {

        mUserAPI.pay(mOrderEntity.getId(), new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                if (response.isSuccess()) {
                    mPayUtil.pay(String.valueOf(response.getContent()));
                    mPayUtil.setPayCallback(new PayUtil.PayCallback() {
                        @Override
                        public void onSucceed(PayUtil.PayResult payResult) {
                            mOrderEntity.setStatus(1);
                            isChange = true;
                        }

                        @Override
                        public void onFailure(PayUtil.PayResult payResult) {

                        }
                    });
                } else {
                    ToastUtils.showShort(response.getMessage());
                }
            }
        });
    }

}