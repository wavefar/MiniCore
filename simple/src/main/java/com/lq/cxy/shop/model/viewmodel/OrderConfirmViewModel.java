package com.lq.cxy.shop.model.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v4.app.Fragment;

import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.fragment.OrderConfirmFragment;
import com.lq.cxy.shop.model.GoodsAPI;
import com.lq.cxy.shop.model.entity.OrderEntity;
import com.lq.cxy.shop.model.entity.ProductEntity;

import org.wavefar.lib.base.BaseViewModel;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.AlertUtils;
import org.wavefar.lib.utils.StringUtil;
import org.wavefar.lib.utils.ToastUtils;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


/**
 * 订单确认
 * @author summer
 */
public class OrderConfirmViewModel extends BaseViewModel{


    public ObservableList<ProductEntity> observableList = new ObservableArrayList<>();

    public ItemBinding<ProductEntity> itemBinding = ItemBinding.of(BR.productEntity, R.layout.item_order_product);

    public final BindingRecyclerViewAdapter<ProductEntity> adapter = new BindingRecyclerViewAdapter<>();

    public OrderEntity mOrderEntity;
    public OrderConfirmViewModel(Fragment context) {
        super(context);
    }

    private GoodsAPI mGoodsAPI;

    @Override
    public void onCreate() {
        super.onCreate();
        mGoodsAPI = new GoodsAPI();
        if (fragment != null) {
            mOrderEntity = fragment.getArguments().getParcelable(OrderConfirmFragment.ORDER_KEY);
            if (mOrderEntity != null) {
                observableList.addAll(mOrderEntity.getGoods());
            }
        }
    }


    /**
     * 总的订单金额
     * @return
     */
    private double calcTotalMoney() {
        double totalMoney = 0;
        for (ProductEntity item: mOrderEntity.getGoods()) {
            totalMoney += item.getPrice() * item.getNum();
        }
        return totalMoney;
    }

    public String showTotalMoney() {
        return String.format("订单总额:%s", StringUtil.formatSignMoney(calcTotalMoney()));
    }


    public String showDateTime(){
        return String.format("配送时间：%s\t%s",mOrderEntity.getDisDate(),mOrderEntity.getDisTime());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        observableList.clear();
        observableList = null;
    }

    /**
     * 提交订单
     * @param resultCallback
     */
    public void submit(BaseResultCallback<?> resultCallback) {
        if (null == mOrderEntity.getAddress() && "0".equals(mOrderEntity.getDisType())) {
            AlertUtils.closeProgressDialog();
            ToastUtils.showShort("请选择收货地址");
            return;
        }
        mGoodsAPI.submitOrder(mOrderEntity, resultCallback);
    }
}
