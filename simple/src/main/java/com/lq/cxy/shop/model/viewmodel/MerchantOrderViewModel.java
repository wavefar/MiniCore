package com.lq.cxy.shop.model.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.wavefar.lib.binding.command.BindingAction;
import org.wavefar.lib.binding.command.BindingCommand;
import org.wavefar.lib.net.callback.BaseResultCallback;

import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.model.entity.MerchantOrderList;
import com.lq.cxy.shop.model.entity.OrderEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.merchant.OrderApi;

import org.wavefar.lib.base.BaseViewModel;
import org.wavefar.lib.utils.ToastUtils;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class MerchantOrderViewModel extends BaseViewModel
        implements BottomNavigationView.OnNavigationItemSelectedListener {
    private OrderApi orderApi;
    private int currentPage = 1, totalPage = 1, curState;
    public ObservableBoolean isFreshFinished = new ObservableBoolean(true);
    public ObservableBoolean isLoadMoreFinished = new ObservableBoolean(true);
    public final BindingRecyclerViewAdapter<MerchantOrderItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ItemBinding<MerchantOrderItemViewModel> merchantOrderListItem = ItemBinding.of(BR.merchantOrderItemVm, R.layout.merchant_order_item_layout);
    public ObservableList<MerchantOrderItemViewModel> listObservable = new ObservableArrayList<>();
    private int currentId;

    public MerchantOrderViewModel(Context context) {
        super(context);
        orderApi = new OrderApi();
        showDialog("加载中...");
        updateData(R.id.order_to_send_goods);
    }

    public void updateData(int id) {
        switch (id) {
//            case R.id.order_to_assess:
//                loadDataInternal(5);
//                break;
            case R.id.order_in_sending_goods:
                loadDataInternal(3); // 配送中
                break;
            case R.id.order_complete:
                loadDataInternal(4); // 已完成
                break;
            default:
                loadDataInternal(1);

        }
    }

    private void loadDataInternal(int state) {
        curState = state;
        orderApi.loadOrderList(state, currentPage, new BaseResultCallback<BaseResponseEntity<MerchantOrderList>>() {
            @Override
            public void onResponse(BaseResponseEntity<MerchantOrderList> response) {
                dismissDialog();
                resetListLoadState();
                if (response.isSuccess()) {
                    totalPage = response.getContent().getPages();
                    isFreshFinished.set(!isFreshFinished.get());
                    if (currentPage == 1) {
                        listObservable.clear();
                    }
                    MerchantOrderItemViewModel itemViewModel;
                    for (OrderEntity item: response.getContent().getList()) {
                        itemViewModel = new MerchantOrderItemViewModel(context, item);
                        listObservable.add(itemViewModel);
                    }
                } else {
                    ToastUtils.showShort("数据错误");
                }
            }
        });
    }

    private void resetListLoadState() {
        if (!isFreshFinished.get()) {
            isFreshFinished.set(true);
        }
        if (!isLoadMoreFinished.get()) {
            isLoadMoreFinished.set(true);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        showDialog("加载中...");
        currentPage = 1;
        updateData(menuItem.getItemId());
        return true;
    }

    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isFreshFinished.set(false);
            //重新请求
            currentPage = 1;
            loadDataInternal(curState);
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
            loadDataInternal(curState);
        }
    });

    @Override
    public void registerRxBus() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void removeRxBus() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void handleEvent(OrderRefreshEvent event) {
        loadDataInternal(curState);
    }

    public static interface OrderRefreshEvent {
    }
}
