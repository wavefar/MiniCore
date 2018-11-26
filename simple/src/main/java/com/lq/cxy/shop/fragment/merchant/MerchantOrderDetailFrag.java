package com.lq.cxy.shop.fragment.merchant;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lq.cxy.shop.R;
import com.lq.cxy.shop.databinding.FragMerchantOrderDetailBinding;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.entity.AddressEntity;
import com.lq.cxy.shop.model.entity.OrderEntity;
import com.lq.cxy.shop.model.entity.ProductEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.merchant.OrderApi;
import com.lq.cxy.shop.model.viewmodel.MerchantOrderViewModel;

import org.greenrobot.eventbus.EventBus;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.StringUtil;
import org.wavefar.lib.utils.ToastUtils;

import java.util.List;

public class MerchantOrderDetailFrag extends Fragment implements View.OnClickListener {
    public static final String KEY_MERCHANT_ORDER = "m_order_key";
    FragMerchantOrderDetailBinding binding;
    private OrderEntity orderEntity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        orderEntity = getArguments().getParcelable(KEY_MERCHANT_ORDER);
        binding = FragMerchantOrderDetailBinding.inflate(inflater);
        bindData();
        new UserAPI().getOrderDetail(orderEntity.getId(), new BaseResultCallback<BaseResponseEntity<OrderEntity>>() {
            @Override
            public void onResponse(BaseResponseEntity<OrderEntity> response) {
                if (response.isSuccess()) {
                    orderEntity = response.getContent();
                    if (orderEntity != null) {
                        bindData();
                    }
                } else {
                    ToastUtils.showShort("获取订单详细信息失败，请重试!");
                    getActivity().finish();
                }
            }
        });
        return binding.getRoot();
    }

    private void bindData() {
        if (orderEntity == null) {
            ToastUtils.showShort("订单内容不能为空！");
            getActivity().finish();
            return;
        }
        binding.confirmProductHasSent.setOnClickListener(this);
        binding.getRoot().findViewById(R.id.iv_back).setOnClickListener(this);
        TextView titleTv = binding.getRoot().findViewById(R.id.toolbar_title);
        titleTv.setText("订单详情");
        binding.orderNoTv.setText(String.format("订单号：%s", orderEntity.getOrderCode()));
        binding.orderDateTv.setText(String.format("订单时间：%s", orderEntity.getCreateTime()));
        binding.totalPrice.setText(String.format("订单总额：%s", StringUtil.formatSignMoney(orderEntity.getMoney())));

        //收货人信息
        AddressEntity addressEntity = orderEntity.getAddress();
        if (addressEntity != null) {
            binding.usernameTv.setText(String.format("收货人：%s", addressEntity.getUserName()));
            binding.linktelTv.setText(String.format("收货人电话：%s", addressEntity.getPhoneNum()));
            binding.shipaddressTv.setText(String.format("收货地址：%s", addressEntity.getAddress()));
        }

        if (orderEntity.getStatus() == OrderEntity.ORDER_HAS_COMPLETED
                || orderEntity.getStatus() == OrderEntity.ORDER_IN_SENDING) {
            binding.odfSendBillNumber.setEnabled(false);
            binding.odfSendBillNumber.setText(orderEntity.getWaybillId());
            binding.odfSendCompanyName.setEnabled(false);
            binding.odfSendCompanyName.setText(orderEntity.getWaybillName());
            binding.confirmProductHasSent.setVisibility(View.GONE);
        } else {

        }
        List<ProductEntity> products = orderEntity.getGoods();
        if (products != null) {
            addProduct(products);
        }
    }

    /**
     * 初始化商品列表
     */
    private void addProduct(List<ProductEntity> products) {

        binding.productLl.removeAllViews();
        for (ProductEntity productEntity: products) {
            LinearLayout ll = new LinearLayout(getContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
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
            numTv.setText(String.format("X%s", productEntity.getNum()));

            TextView priceTv = new TextView(getContext());
            priceTv.setPadding(10, 10, 10, 10);
            priceTv.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            priceTv.setTextColor(0xFFdc1313);
            priceTv.setText(StringUtil.formatSignMoney(productEntity.getPrice()));
            ll.addView(nameTv);
            ll.addView(numTv);
            ll.addView(priceTv);
            binding.productLl.addView(ll);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.confirmProductHasSent:
                String name = binding.odfSendCompanyName.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    ToastUtils.showShort("请填写物流公司名称");
                    return;
                }

                String num = binding.odfSendBillNumber.getText().toString().trim();
                if (TextUtils.isEmpty(num)) {
                    ToastUtils.showShort("请填写运单号");
                    return;
                }
                orderEntity.setWaybillId(num);
                orderEntity.setWaybillName(name);
                binding.confirmProductHasSent.setEnabled(false);
                new AlertDialog.Builder(getActivity())
                        .setMessage("请仔细核验送货信息")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                binding.confirmProductHasSent.setEnabled(true);
                                saveSendWayInfo();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                binding.confirmProductHasSent.setEnabled(true);
                            }
                        })
                        .setCancelable(false)
                        .create().show();
                break;
        }
    }

    private void saveSendWayInfo() {
        new OrderApi().saveSendProductInfo(orderEntity, new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                if (response.isSuccess()) {
                    ToastUtils.showShort("送货信息保存成功");
                    EventBus.getDefault().post(new MerchantOrderViewModel.OrderRefreshEvent() {
                    });
                    getActivity().finish();
                } else {
                    ToastUtils.showShort("送货信息保存失败，请稍后重试！");
                    binding.confirmProductHasSent.setEnabled(true);
                }
            }
        });
    }
}
