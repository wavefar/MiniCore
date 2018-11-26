package com.lq.cxy.shop.model.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;

import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.adapter.OnItemClickListener;
import com.lq.cxy.shop.model.entity.ProductEntity;
import com.lq.cxy.shop.model.entity.StoreEntity;

import org.wavefar.lib.base.BaseViewModel;
import org.wavefar.lib.binding.command.BindingAction;
import org.wavefar.lib.binding.command.BindingCommand;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


/**
 * 店铺子项
 *
 * @author Administrator
 */
public class StoreItemViewModel extends BaseViewModel implements OnItemClickListener<ProductEntity> {
    public static final String PRODUCT_KEY = "PRODUCT_KEY";
    public StoreEntity storeEntity;
    public ObservableList<ProductEntity> observableList = new ObservableArrayList<>();

    public ItemBinding<ProductEntity> itemBinding = ItemBinding.<ProductEntity>of(BR.productEntity, R.layout.item_good)
            .bindExtra(BR.listener, this);

    public final BindingRecyclerViewAdapter<ProductEntity> adapter = new BindingRecyclerViewAdapter<>();


    public StoreItemViewModel(Context context, StoreEntity entity) {
        super(context);
        this.storeEntity = entity;
        observableList.addAll(entity.getGoods());
    }

    /**
     * 店铺点击
     */
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();
        observableList.clear();
        observableList = null;
    }

    /**
     * 商品列表点击去下单
     */
    @Override
    public void onItemClick(ProductEntity entity) {
        Activity activity = (Activity) context;
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        entity.setStoreName(storeEntity.getStoreName());
        bundle.putParcelable(PRODUCT_KEY, entity);
        intent.putExtras(bundle);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }
}
