package com.lq.cxy.shop.model.viewmodel;

import android.content.Context;
import android.view.View;

import com.lq.cxy.shop.model.entity.AddressEntity;

import org.wavefar.lib.base.BaseViewModel;

public class AddressItemViewModel extends BaseViewModel {
    private AddressEntity userAddress;

    public OnItemViewClickListener getAddrClickListener() {
        return addrClickListener;
    }

    public void setAddrClickListener(OnItemViewClickListener addrClickListener) {
        this.addrClickListener = addrClickListener;
    }

    OnItemViewClickListener addrClickListener;

    public AddressEntity getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(AddressEntity userAddress) {
        this.userAddress = userAddress;
    }

    public AddressItemViewModel(Context context, AddressEntity address) {
        super(context);
        userAddress = address;
    }

    public interface OnItemViewClickListener {
        void onClick(View view, AddressEntity entity);
    }
}
