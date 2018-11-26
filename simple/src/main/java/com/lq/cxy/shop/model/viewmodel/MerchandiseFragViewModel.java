package com.lq.cxy.shop.model.viewmodel;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.v7.app.AlertDialog;
import android.util.EventLog;
import android.util.Log;
import android.view.View;

import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.activity.merchant.MerchantGoodsDetailActivity;
import com.lq.cxy.shop.model.GoodsAPI;
import com.lq.cxy.shop.model.entity.OrderEntity;
import com.lq.cxy.shop.model.entity.ProductEntity;
import com.lq.cxy.shop.model.entity.ProductListEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.wavefar.lib.base.BaseViewModel;
import org.wavefar.lib.binding.command.BindingAction;
import org.wavefar.lib.binding.command.BindingCommand;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.ToastUtils;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class MerchandiseFragViewModel extends BaseViewModel implements MerchandiseFragItemViewModel.OnItemClickListener {
    GoodsAPI goodsAPI;
    private int currentPage = 1, totalPage = 1;
    public ObservableBoolean isFreshFinished = new ObservableBoolean(true);
    public ObservableBoolean isLoadMoreFinished = new ObservableBoolean(true);
    public final BindingRecyclerViewAdapter<MerchandiseFragItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ItemBinding<MerchandiseFragItemViewModel> vmMerGoods = ItemBinding.of(BR.vmMerGoods, R.layout.merchant_goods_list_item_layout);
    public ObservableList<MerchandiseFragItemViewModel> merchandiseGoodsListObservable = new ObservableArrayList<>();

    public MerchandiseFragViewModel(Context context) {
        super(context);
        goodsAPI = new GoodsAPI();
        loadMerchantGoodsList();
    }

    private void resetListLoadState() {
        if (!isFreshFinished.get()) {
            isFreshFinished.set(true);
        }
        if (!isLoadMoreFinished.get()) {
            isLoadMoreFinished.set(true);
        }
    }

    private void loadMerchantGoodsList() {
        showDialog("加载中...");
        goodsAPI.loadMerchantGoodsList(currentPage, new BaseResultCallback<BaseResponseEntity<ProductListEntity>>() {
            @Override
            public void onResponse(BaseResponseEntity<ProductListEntity> response) {
                dismissDialog();
                resetListLoadState();
                if (response.isSuccess()) {
                    totalPage = response.getContent().getPages();
                    isFreshFinished.set(!isFreshFinished.get());
                    if (currentPage == 1) {
                        merchandiseGoodsListObservable.clear();
                    }
                    MerchandiseFragItemViewModel itemViewModel;
                    for (ProductEntity item: response.getContent().getList()) {
                        itemViewModel = new MerchandiseFragItemViewModel(context, item);
                        itemViewModel.setItemListener(MerchandiseFragViewModel.this);
                        merchandiseGoodsListObservable.add(itemViewModel);
                    }
                } else {
                    ToastUtils.showShort("数据错误");
                }
            }
        });
    }

    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            reload();
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
            loadMerchantGoodsList();
        }
    });

    @Override
    public void clickToEditProduct(View v, ProductEntity data) {
        MerchantGoodsDetailActivity.launch(context, data);
    }

    @Override
    public void clickRemoveFromStock(View v, final ProductEntity data) {
        new AlertDialog.Builder(context)
                .setMessage("确定要将该商品下架？")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goodsAPI.closeGoods(data, new BaseResultCallback<BaseResponseEntity>() {
                            @Override
                            public void onResponse(BaseResponseEntity response) {
                                if (response.isSuccess()) {
                                    reload();
                                } else {
                                    ToastUtils.showShort("请求失败，请重试！");
                                }
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setCancelable(false)
                .create().show();

    }

    @Override
    public void clickDelete(View v, final ProductEntity data) {
        new AlertDialog.Builder(context)
                .setMessage("确定要删除该商品？")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goodsAPI.deleteGoods(data, new BaseResultCallback<BaseResponseEntity>() {
                            @Override
                            public void onResponse(BaseResponseEntity response) {
                                MerchandiseFragItemViewModel tmp = null;
                                if (response.isSuccess()) {
                                    for (MerchandiseFragItemViewModel vm: merchandiseGoodsListObservable) {
                                        if (vm.item == data) {
                                            tmp = vm;
                                            break;
                                        }
                                    }
                                    if (tmp != null) {
                                        merchandiseGoodsListObservable.remove(tmp);
                                    }
                                } else {
                                    ToastUtils.showShort(response.getMessage());
                                }
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setCancelable(false)
                .create().show();

    }

    private void reload() {
        isFreshFinished.set(false);
        //重新请求
        currentPage = 1;
        loadMerchantGoodsList();
    }

    @Subscribe
    public void onEvent(GoodsInfoUpdateEvent event) {
        reload();
    }


    @Override
    public void registerRxBus() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void removeRxBus() {
        EventBus.getDefault().unregister(this);
    }

    public interface GoodsInfoUpdateEvent {
    }
}
