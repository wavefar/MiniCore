package com.lq.cxy.shop.model.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.view.View;

import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.activity.AddressEditActivity;
import com.lq.cxy.shop.model.AddressApi;
import com.lq.cxy.shop.model.entity.AddressEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.http.base.BaseSimpleResultCallback;

import org.wavefar.lib.base.BaseViewModel;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.ToastUtils;

import java.util.List;

import io.reactivex.annotations.NonNull;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class AddressListViewModel extends BaseViewModel implements AddressItemViewModel.OnItemViewClickListener {
    private AddressApi addrApi;

    public boolean isSelectedMode() {
        return isSelectedMode;
    }

    public void setSelectedMode(boolean selectedMode) {
        isSelectedMode = selectedMode;
    }

    private boolean isSelectedMode;

    public AddressListViewModel(Context context) {
        super(context);
        addrApi = new AddressApi();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadAddressList();
    }

    public void loadAddressList() {
        addrApi.loadAddressList(new BaseSimpleResultCallback<BaseResponseEntity<List<AddressEntity>>,List<AddressEntity>>() {
            @Override
            public void onSucceed(List<AddressEntity> entites) {
                addressListObservable.clear();
                AddressItemViewModel tmp;
                for (AddressEntity entity: entites) {
                    tmp = new AddressItemViewModel(context, entity);
                    addressListObservable.add(tmp);
                    tmp.setAddrClickListener(AddressListViewModel.this);
                }
            }
        });
    }

    private void deleteAddress(@NonNull final AddressEntity address) {
        addrApi.deleteAddress(address, new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                if (response.isSuccess()) {
                    for (int i = 0, len = addressListObservable.size(); i < len; i++) {
                        if (addressListObservable.get(i).getUserAddress() == address) {
                            addressListObservable.remove(i);
                            break;
                        }
                    }
                } else {
                    ToastUtils.showLong(response.getMessage());
                }
            }
        });
    }

    public ObservableList<AddressItemViewModel> addressListObservable = new ObservableArrayList<>();
    public final BindingRecyclerViewAdapter<AddressItemViewModel> addressAdapter = new BindingRecyclerViewAdapter<>();
    public ItemBinding<AddressItemViewModel> addressItem = ItemBinding.of(BR.addressItem, R.layout.activity_address_item);

    @Override
    public void onClick(View view, AddressEntity entity) {
        switch (view.getId()) {
            case R.id.receive_addr_delete:
                deleteAddress(entity);
                break;
            case R.id.receive_addr_root:
            case R.id.receive_addr_edit:
                Bundle bundle = new Bundle();
                bundle.putParcelable(AddressEditActivity.KEY_ADDREES_TO_EDIT, entity);
                if (isSelectedMode) {
                    Activity activity = (Activity) context;
                    Intent i = new Intent();
                    i.putExtras(bundle);
                    activity.setResult(Activity.RESULT_OK, i);
                    activity.finish();
                } else {
                    startActivity(AddressEditActivity.class, bundle);
                }
                break;
                default:break;
        }
    }


}
