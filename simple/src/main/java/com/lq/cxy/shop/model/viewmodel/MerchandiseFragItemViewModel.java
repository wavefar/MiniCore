package com.lq.cxy.shop.model.viewmodel;

import android.content.Context;
import android.view.View;

import com.lq.cxy.shop.model.entity.ProductEntity;

import org.wavefar.lib.base.BaseViewModel;

public class MerchandiseFragItemViewModel extends BaseViewModel {
    public ProductEntity item;
    public OnItemClickListener listener;

    public MerchandiseFragItemViewModel(Context context, ProductEntity itemEntity) {
        super(context);
        item = itemEntity;
    }

    public void setItemListener(OnItemClickListener l) {
        listener = l;
    }

    public interface OnItemClickListener {
        void clickToEditProduct(View v, ProductEntity data);

        void clickRemoveFromStock(View v, ProductEntity data);
        void clickDelete(View v, ProductEntity data);
    }
}
