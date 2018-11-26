package com.lq.cxy.shop.model.viewmodel;

import android.content.Context;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.lq.cxy.shop.R;
import com.lq.cxy.shop.activity.GoodsDetailActivity;
import com.lq.cxy.shop.fragment.GoodsDetailBaseFrag;
import com.lq.cxy.shop.model.ShopCartApi;
import com.lq.cxy.shop.model.entity.CartItemEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;

import org.wavefar.lib.base.BaseViewModel;
import org.wavefar.lib.binding.command.BindingAction;
import org.wavefar.lib.binding.command.BindingCommand;
import org.wavefar.lib.binding.command.BindingConsumer;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.ToastUtils;

/**
 * 购物车子项
 */
public class CartItemViewModel extends BaseViewModel {
    public CartItemEntity cartItem;
    public static Drawable defaultCartItemImg;
    ShopCartApi chageGoodsApi;
    private OnItemStateChangeListener checkStateChanged;
    private ObservableField<Integer> goodsNumObservable = new ObservableField<>();
    private ObservableField<Boolean> goodsCheckObservable = new ObservableField<>();

    public OnItemStateChangeListener getCheckStateChanged() {
        return checkStateChanged;
    }

    public void setCheckStateChanged(OnItemStateChangeListener checkStateChanged) {
        this.checkStateChanged = checkStateChanged;
    }


    public CartItemViewModel(Context context, CartItemEntity entity) {
        super(context);
        cartItem = entity;
        defaultCartItemImg = ContextCompat.getDrawable(context, R.mipmap.ic_launcher);
        chageGoodsApi = new ShopCartApi();
    }

    public BindingCommand cartItemClicked = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(GoodsDetailBaseFrag.KEY_GOODS_DETAIL, CartItemEntity.toProduct(cartItem));
            startActivity(GoodsDetailActivity.class, mBundle);
        }
    });
    public BindingCommand itemCheckStateChange = new BindingCommand<Boolean>(new BindingConsumer<Boolean>() {
        @Override
        public void call(Boolean aBoolean) {
            if (checkStateChanged != null) {
                checkStateChanged.changeToSelectState(cartItem, aBoolean);
            }
        }
    });


    public BindingCommand addOneToCart = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            cartItem.setNum(cartItem.getNum() + 1);
            chageGoodsApi.updatemShopCart(cartItem, new BaseResultCallback<BaseResponseEntity>() {
                @Override
                public void onResponse(BaseResponseEntity response) {
                    if (response.isSuccess()) {
                        if (checkStateChanged != null) {
                            checkStateChanged.changeNum(cartItem, cartItem.getNum());
                        }
                    } else {
                        cartItem.setNum(cartItem.getNum() - 1);
                        ToastUtils.showShort(response.getMessage());
                    }
                }
            });
        }
    });
    public BindingCommand deleteOneFromCart = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (cartItem.getNum() == 1) {
                return;
            }
            cartItem.setNum(cartItem.getNum() - 1);
            chageGoodsApi.updatemShopCart(cartItem, new BaseResultCallback<BaseResponseEntity>() {
                @Override
                public void onResponse(BaseResponseEntity response) {
                    if (response.isSuccess()) {
                        if (checkStateChanged != null) {
                            checkStateChanged.changeNum(cartItem, cartItem.getNum());
                        }
                    } else {
                        cartItem.setNum(cartItem.getNum() + 1);
                        ToastUtils.showShort(response.getMessage());
                    }
                }
            });
        }
    });

    public BindingCommand changeNumOfCart = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
        }
    });

    public interface OnItemStateChangeListener {
        void changeToSelectState(CartItemEntity item, boolean selected);

        void changeNum(CartItemEntity item, int num);
    }
}
