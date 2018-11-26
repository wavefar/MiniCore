package com.lq.cxy.shop.model.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableDouble;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.lq.cxy.shop.BR;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.activity.LoginActivity;
import com.lq.cxy.shop.fragment.OrderConfirmFragment;
import com.lq.cxy.shop.model.ShopCartApi;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.entity.CartItemEntity;
import com.lq.cxy.shop.model.entity.OrderEntity;
import com.lq.cxy.shop.model.entity.ProductEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;

import org.wavefar.lib.base.BaseViewModel;
import org.wavefar.lib.binding.command.BindingAction;
import org.wavefar.lib.binding.command.BindingCommand;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.IntentUtils;
import org.wavefar.lib.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class CartCenterViewModel extends BaseViewModel implements CartItemViewModel.OnItemStateChangeListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private int currentPage = 1;
    private ArrayList<CartItemEntity> selectedGoodsList = new ArrayList<>();
    private ShopCartApi shopCartApi;
    public final ObservableDouble totalMoneyObservable = new ObservableDouble();

    public OnSelectedItemChangedListener getHasSelectedListener() {
        return hasSelectedListener;
    }

    public void setHasSelectedListener(OnSelectedItemChangedListener hasSelectedListener) {
        this.hasSelectedListener = hasSelectedListener;
    }

    OnSelectedItemChangedListener hasSelectedListener;

    public CartCenterViewModel(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        shopCartApi = new ShopCartApi();
    }

    private void loadCartData(int pageNum) {
        shopCartApi.loadShopCartList(pageNum, new BaseResultCallback<BaseResponseEntity<List<CartItemEntity>>>() {

            @Override
            public void onResponse(BaseResponseEntity<List<CartItemEntity>> response) {
                resetListLoadState();
                if (response.isSuccess()) {
                    isFreshFinished.set(!isFreshFinished.get());
                    if (currentPage == 1) {
                        cartListObserverable.clear();
                        selectedGoodsList.clear();
                    }
                    CartItemViewModel itemViewModel;
                    for (CartItemEntity item: response.getContent()) {
                        itemViewModel = new CartItemViewModel(context, item);
                        itemViewModel.setCheckStateChanged(CartCenterViewModel.this);
                        cartListObserverable.add(itemViewModel);
                    }
                } else {
                    ToastUtils.showShort("数据错误");
                }
                handleSeletedGoods();
            }
        });
    }

    private void resetListLoadState() {
        if (isFreshFinished.get()) {
            isFreshFinished.set(true);
        }
        if (isLoadMoreFinished.get()) {
            isLoadMoreFinished.set(true);
        }
    }

    public ObservableList<CartItemViewModel> cartListObserverable = new ObservableArrayList<>();
    public ObservableBoolean isFreshFinished = new ObservableBoolean(true);
    public ObservableBoolean isLoadMoreFinished = new ObservableBoolean(true);
    public final BindingRecyclerViewAdapter<CartItemViewModel> adapter = new BindingRecyclerViewAdapter<>();
    public ItemBinding<CartItemViewModel> cartListItem = ItemBinding.of(BR.cartItemViewModel, R.layout.fragment_shop_cart_goods_item);

    //下拉刷新changeToSelectState
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isFreshFinished.set(false);
            //重新请求
            currentPage = 1;
            loadCartData(currentPage);
        }
    });
    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isLoadMoreFinished.set(false);
            currentPage++;
            loadCartData(currentPage);
        }
    });

    @Override
    public void changeToSelectState(CartItemEntity item, boolean selected) {
        selectedGoodsList.remove(item);
        if (selected) {
            selectedGoodsList.add(item);
        }
        for (CartItemViewModel vm: cartListObserverable) {
            if (vm.cartItem == item) {
                vm.cartItem.setItemSeleted(selected);
                break;
            }
        }
        handleSeletedGoods();
    }

    @Override
    public void changeNum(CartItemEntity item, int num) {
        item.setNum(num);
        adapter.notifyDataSetChanged();
    }

    private void handleSeletedGoods() {
        double totalMoney = 0;
        for (CartItemEntity item: selectedGoodsList) {
            totalMoney += item.getPrice() * item.getNum();
        }
        totalMoneyObservable.set(totalMoney);
        if (hasSelectedListener != null) {
            hasSelectedListener.onSelectedChange(selectedGoodsList.isEmpty(), selectedGoodsList.size() == cartListObserverable.size() && !cartListObserverable.isEmpty());
        }
    }

    private void deleteSelectedItems(final int index) {
        int len = selectedGoodsList.size();
        if (index < 0 || index >= len) {
            return;
        }
        shopCartApi.deleteFromShopCart(selectedGoodsList.get(index), new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                if (response.isSuccess()) {
                    for (int j = 0, len = cartListObserverable.size(); j < len; j++) {
                        if (cartListObserverable.get(j).cartItem == selectedGoodsList.get(index)) {
                            cartListObserverable.remove(j);
                            break;
                        }
                    }
                    selectedGoodsList.remove(index);
                    handleSeletedGoods();
                    deleteSelectedItems(index);
                } else {
                    ToastUtils.showShort(response.getMessage());
                    deleteSelectedItems(1 + index);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_more:
                deleteSelectedItems(0);
                break;
            case R.id.go_to_ordering:
                if (selectedGoodsList.size() == 0) {
                    ToastUtils.showShort("请选择商品");
                    return;
                }

                if (!UserAPI.checkLogin()) {
                    IntentUtils.redirect(LoginActivity.class);
                    return;
                }

                OrderEntity orderEntity = new OrderEntity();
                Bundle mBundle = new Bundle();
                List<ProductEntity> list = new ArrayList<>();
                ProductEntity productEntity;
                for (CartItemEntity item: selectedGoodsList) {
                    productEntity = new ProductEntity();

                    productEntity.setNum(item.getNum());
                    productEntity.setGoodsId(item.getGoodsId());
                    productEntity.setPrice(item.getPrice());
                    productEntity.setAvatar(item.getAvatar());
                    productEntity.setGoodsName(item.getGoodsName());
                    productEntity.setGoodsCode(item.getGoodsCode());

                    //构造购物车ID
                    productEntity.setId(item.getId());

                    list.add(productEntity);
                }

                orderEntity.setGoods(list);
                orderEntity.setShopCar(true);
                mBundle.putParcelable(OrderConfirmFragment.ORDER_KEY, orderEntity);
                startContainerActivity(OrderConfirmFragment.class.getName(), mBundle);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        selectedGoodsList.clear();
        CartItemEntity tmp;
        for (int i = 0, len = cartListObserverable.size(); i < len; i++) {
            tmp = cartListObserverable.get(i).cartItem;
            if (b) {
                selectedGoodsList.add(tmp);
                tmp.setItemSeleted(b);
            } else {
                if (tmp.isItemSeleted()) {
                    selectedGoodsList.add(tmp);
                }
            }
        }
        adapter.notifyDataSetChanged();
        handleSeletedGoods();
    }

    public interface OnSelectedItemChangedListener {
        void onSelectedChange(boolean noneSelected, boolean isFullSelected);
    }
}
