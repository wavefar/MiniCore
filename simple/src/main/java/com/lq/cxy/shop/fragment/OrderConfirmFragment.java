package com.lq.cxy.shop.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.activity.AddressEditActivity;
import com.lq.cxy.shop.activity.AddressManageActivity;
import com.lq.cxy.shop.databinding.FragmentOrderConfirmBinding;
import com.lq.cxy.shop.model.entity.AddressEntity;
import com.lq.cxy.shop.model.entity.DepositEntity;
import com.lq.cxy.shop.model.entity.OrderEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.http.base.BaseSimpleResultCallback;
import com.lq.cxy.shop.model.viewmodel.DepositViewModel;
import com.lq.cxy.shop.model.viewmodel.OrderConfirmViewModel;

import org.wavefar.lib.base.BaseFragment;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.AlertUtils;
import org.wavefar.lib.utils.IntentUtils;
import org.wavefar.lib.utils.JsonUtils;
import org.wavefar.lib.utils.StringUtil;
import org.wavefar.lib.utils.ToastUtils;

import static android.app.Activity.RESULT_OK;
import static com.lq.cxy.shop.activity.AddressManageActivity.KEY_IS_SELECT_MODE;

/**
 * 订单确认
 * @author summer
 */
public class OrderConfirmFragment extends BaseFragment<FragmentOrderConfirmBinding, OrderConfirmViewModel>  implements View.OnClickListener{
    /**
     * 获取构造一个订单对象
     */
    public static final String ORDER_KEY = "order_key";
    private TextView addressTv;
    /**
     * 支付方式，预留
     */
    private String paymethod;

    private DepositViewModel depositViewModel;

    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.fragment_order_confirm;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public OrderConfirmViewModel initViewModel() {
        return new OrderConfirmViewModel(this);
    }


    @Override
    public void initData() {
        View view = binding.getRoot();
        TextView titleTv = view.findViewById(R.id.toolbar_title);
        titleTv.setText("订单确认");
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        addressTv = view.findViewById(R.id.address_tv);
        //支付方式
        binding.payMethodRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = group.findViewById(checkedId);
                String str = (String) button.getTag();
                if(!TextUtils.isEmpty(str)) {
                    paymethod = str;
                }

            }
        });

        //配送方式
        binding.disTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = group.findViewById(checkedId);
                String disType = String.valueOf(button.getTag());
                if (disType.equals("1")) {
                    binding.addressRl.setVisibility(View.GONE);
                } else {
                    binding.addressRl.setVisibility(View.VISIBLE);
                }
                viewModel.mOrderEntity.setDisType(disType);
            }
        });

        binding.saveBtn.setOnClickListener(this);
        addressTv.setOnClickListener(this);
        view.findViewById(R.id.change_address_tv).setOnClickListener(this);
        depositViewModel = new DepositViewModel(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        bindData();
    }

    private void bindData() {
        OrderEntity orderEntity = viewModel.mOrderEntity;
        if (orderEntity != null) {
            AddressEntity addressEntity = orderEntity.getAddress();
            if (addressEntity!=null) {
                addressTv.setText(String.format("%s\t\t%s\n%s",
                        addressEntity.getUserName(),
                        addressEntity.getPhoneNum(),
                        addressEntity.getDecodedFullAddr()));
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_btn:
                submitOrder();
                break;
            case R.id.change_address_tv:
            case R.id.address_tv:
                Bundle bundle = new Bundle();
                bundle.putBoolean(KEY_IS_SELECT_MODE,true);
                IntentUtils.redirectAndPrameterResult(getActivity(), AddressManageActivity.class,bundle,1);
                break;
                default:break;
        }
    }

    private void submitOrder() {
        AlertUtils.showProgressDialog(getActivity(),"","请求中...");
        viewModel.submit(new BaseSimpleResultCallback<BaseResponseEntity<OrderEntity>,OrderEntity>() {
            @Override
            public void onSucceed(OrderEntity entity) {
                AlertUtils.closeProgressDialog();
                if (entity != null) {
                    ToastUtils.showShort("下单成功！");
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(OrderDetailFragment.ORDER_KEY, entity);
                    IntentUtils.startContainerActivity(OrderDetailFragment.class.getName(),bundle);
                }
            }

            @Override
            public void onResponse(BaseResponseEntity<OrderEntity> response) {
                super.onResponse(response);
                AlertUtils.closeProgressDialog();
                if (-1023 == response.getCode()) {
                    OrderEntity entity = response.getContent();
                    final DepositEntity depositEntity = new DepositEntity();
                    depositEntity.setMoney(entity.getPrice());
                    depositEntity.setName("押金支付");
                    depositEntity.setStoreId(entity.getStoreId());
                    AlertUtils.showAlert(getActivity(), "提示",
                            String.format("购买此商品还需要缴纳%s押金,是否立即支付押金？",
                                    StringUtil.formatSignMoney(entity.getPrice())),
                            "立即支付",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    depositViewModel.submitDeposit(depositEntity);
                                }
                            },"取消",null);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 1) {
            if (data != null) {
                setAddressEntity((AddressEntity) data.getParcelableExtra(AddressEditActivity.KEY_ADDREES_TO_EDIT));
            }
        }
    }

    /**
     * 回调设置地址
     * @param addressEntity
     */
    private void setAddressEntity(AddressEntity addressEntity){
        if (addressEntity != null) {
            viewModel.mOrderEntity.setAddress(addressEntity);
            addressTv.setText(String.format("%s\t\t%s\n%s",
                    addressEntity.getUserName(),
                    addressEntity.getPhoneNum(),
                    addressEntity.getAddress()));
        }
    }


}