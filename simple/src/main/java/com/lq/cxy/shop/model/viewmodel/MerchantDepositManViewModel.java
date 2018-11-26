package com.lq.cxy.shop.model.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.model.entity.DepositEntity;
import com.lq.cxy.shop.model.entity.MerDepositList;
import com.lq.cxy.shop.model.entity.OrderIncomeEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.merchant.ShopApi;

import org.wavefar.lib.base.BaseViewModel;
import org.wavefar.lib.binding.command.BindingAction;
import org.wavefar.lib.binding.command.BindingCommand;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.ToastUtils;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class MerchantDepositManViewModel extends BaseViewModel implements BottomNavigationView.OnNavigationItemSelectedListener, MerchantDepositItemViewModel.OnBackDepositClickListener {

    private ShopApi shopApi;
    public ObservableBoolean isFreshFinished = new ObservableBoolean(true);
    public ObservableBoolean isLoadMoreFinished = new ObservableBoolean(true);
    public final BindingRecyclerViewAdapter<MerchantDepositItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ItemBinding<MerchantDepositItemViewModel> itemBinding = ItemBinding.of(BR.vm, R.layout.activity_deposit_manage_item);
    public ObservableList<MerchantDepositItemViewModel> listObservable = new ObservableArrayList<>();
    private int currentPage = 1, totalPage = 1;
    private int curSelectId;

    public MerchantDepositManViewModel(Context context) {
        super(context);
        shopApi = new ShopApi();
        curSelectId = R.id.deposit_paid;
        refresh();
    }

    private void refresh() {
        showDialog();
        int state = 1;
        switch (curSelectId) {
            case R.id.deposit_paid:
                state = 1;
                break;
            case R.id.deposit_wait_payback:
                state = 3;
                break;
            case R.id.deposit_have_back:
                state = 2;
                break;
        }
        shopApi.merchantDepositList(currentPage, state, new BaseResultCallback<BaseResponseEntity<MerDepositList>>() {
            @Override
            public void onResponse(BaseResponseEntity<MerDepositList> response) {
                dismissDialog();
                resetListLoadState();
                if (response.isSuccess()) {
                    totalPage = response.getContent().getPages();
                    isFreshFinished.set(!isFreshFinished.get());
                    if (currentPage == 1) {
                        listObservable.clear();
                    }
                    MerchantDepositItemViewModel itemViewModel;
                    for (DepositEntity item: response.getContent().getList()) {
                        itemViewModel = new MerchantDepositItemViewModel(context, item);
                        itemViewModel.setListener(MerchantDepositManViewModel.this);
                        listObservable.add(itemViewModel);
                    }
                } else {
                    ToastUtils.showShort(response.getMessage());
                }
            }

            @Override
            public void onError(Throwable e, String body) {
                super.onError(e, body);
                dismissDialog();
            }
        });
    }

    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isFreshFinished.set(false);
//            //重新请求
            currentPage = 1;
            refresh();
        }
    });
    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isLoadMoreFinished.set(false);
            if (currentPage >= totalPage) {
                resetListLoadState();
                return;
            }
            currentPage++;
            refresh();
        }
    });

    private void resetListLoadState() {
        if (!isFreshFinished.get()) {
            isFreshFinished.set(true);
        }
        if (!isLoadMoreFinished.get()) {
            isLoadMoreFinished.set(true);
        }
    }

    @Override
    public void onClick(final DepositEntity item) {
        shopApi.merchantDepositBack(item, new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                if (response.isSuccess()) {
                    currentPage = 1;
                    refresh();
                    ToastUtils.showShort("押金退款成功");
                } else {
                    ToastUtils.showShort(response.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        curSelectId = menuItem.getItemId();
        listObservable.clear();
        refresh();
        return true;
    }
}
