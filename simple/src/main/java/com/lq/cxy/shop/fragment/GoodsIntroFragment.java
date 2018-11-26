package com.lq.cxy.shop.fragment;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bumptech.glide.Glide;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.activity.AddressEditActivity;
import com.lq.cxy.shop.activity.AddressManageActivity;
import com.lq.cxy.shop.activity.LoginActivity;
import com.lq.cxy.shop.databinding.FragGoodsIntroductionBinding;
import com.lq.cxy.shop.model.AddressApi;
import com.lq.cxy.shop.model.FavoriteGoodsAPI;
import com.lq.cxy.shop.model.GoodsAPI;
import com.lq.cxy.shop.model.ShopCartApi;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.entity.AddressEntity;
import com.lq.cxy.shop.model.entity.CartItemEntity;
import com.lq.cxy.shop.model.entity.OrderEntity;
import com.lq.cxy.shop.model.entity.ProductEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.http.base.BaseSimpleResultCallback;
import com.lq.cxy.shop.model.viewmodel.FavGoodsListViewModel;
import com.lq.cxy.shop.utils.UiUtil;

import org.greenrobot.eventbus.EventBus;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.AlertUtils;
import org.wavefar.lib.utils.IntentUtils;
import org.wavefar.lib.utils.ScreenUtils;
import org.wavefar.lib.utils.StringUtil;
import org.wavefar.lib.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import static org.wavefar.lib.utils.IntentUtils.startContainerActivity;

/**
 * 商品介绍页面
 *
 * @author summer
 */
public class GoodsIntroFragment extends GoodsDetailBaseFrag implements View.OnClickListener {
    public static final List<String> SEND_MTHOD = new ArrayList<>();

    static {
        SEND_MTHOD.add("邮寄");
        //SEND_MTHOD.add("自提");
    }

    private static final int SELECT_ADDRESS_REQUEST_CODE = 0X16;
    private FragGoodsIntroductionBinding binding;
    private int buyCount = 1;
    private ShopCartApi shopCartApi;
    private GoodsAPI goodsAPI;
    private AddressEntity sendProTo;
    private FavoriteGoodsAPI favApi;
    private boolean isMaxNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_goods_introduction, null, false);
        buyCount = 1;
        fillUiData();
        binding.goodsIntroBuyNow.setOnClickListener(this);
        binding.goodsIntroAdd.setOnClickListener(this);
        binding.goodsIntroDelete.setOnClickListener(this);
        binding.goodsIntroAddToCart.setOnClickListener(this);
        binding.goodsIntroUserAddr.setOnClickListener(this);
        binding.goodsIntoSendMethod.setOnClickListener(this);
        binding.goodsIntroFav.setOnCheckedChangeListener(checkListener);
        loadAddressList();
        return binding.getRoot();
    }

    private void fillUiData() {
        Glide.with(binding.goodsIntroImg).load(goodsDetail.getAvatar()).into(binding.goodsIntroImg);
        binding.goodsIntroImg.setLayoutParams(new RelativeLayout.LayoutParams(
                ScreenUtils.getScreenWidth(),
                ScreenUtils.getScreenWidth() / 2));
        binding.goodsIntroGoodsName.setText(goodsDetail.getGoodsName());
        binding.goodsIntroGoodsRemark.setText(goodsDetail.getRemark());
        binding.goodsIntroPrice.setText(StringUtil.formatSignMoney(goodsDetail.getPrice()));
        binding.goodsIntroStock.setText(String.valueOf(goodsDetail.getStock()));
        binding.goodsIntoSendMethod.setText(SEND_MTHOD.get(0));
        setCheckBoxWithoutNotify(goodsDetail.isFollow());
        buyCountChanged(buyCount);
    }

    public void loadAddressList() {
        if (!UserAPI.checkLogin()) {
            return;
        }
        new AddressApi().loadAddressList(new BaseResultCallback<BaseResponseEntity<List<AddressEntity>>>() {
            @Override
            public void onResponse(BaseResponseEntity<List<AddressEntity>> response) {
                if (response.isSuccess()) {
                    List<AddressEntity> addressList = response.getContent();
                    if (addressList.size() > 0) {
                        sendProTo = addressList.get(0);
                        setAddressUIWithAddress();
                    }
                } else {
                    ToastUtils.showShort(response.getMessage());
                }
            }
        });
    }

    private void setAddressUIWithAddress() {
        if (sendProTo == null) {
            binding.noUseSendAddressTv.setVisibility(View.GONE);
            binding.goodsIntroUserAddr.setVisibility(View.GONE);
        } else {
            binding.noUseSendAddressTv.setVisibility(View.VISIBLE);
            binding.goodsIntroUserAddr.setVisibility(View.VISIBLE);
            binding.goodsIntroUserAddr.setText(sendProTo.getAddress());
        }
    }

    @Override
    public void updateGoodsDetailInfo(ProductEntity goodsDetail) {
        this.goodsDetail = goodsDetail;
        fillUiData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goods_intro_buy_now:
                if (!UserAPI.checkLogin()) {
                    IntentUtils.redirect(LoginActivity.class);
                    return;
                }
                buyGoodsNow();
                break;
            case R.id.goods_intro_add:
                if (buyCount >= goodsDetail.getStock() && !isMaxNum) {
                    ToastUtils.showShort("别加了，商家快满足不了你的需求啦！");
                    isMaxNum = true;
                    return;
                }
                isMaxNum = false;
                buyCount++;
                buyCount = buyCount >= goodsDetail.getStock() ? goodsDetail.getStock() : buyCount;
                buyCountChanged(buyCount);
                break;
            case R.id.goods_intro_delete:
                if (buyCount <= 1) {
                    return;
                }
                isMaxNum = buyCount >= goodsDetail.getStock();
                buyCount--;
                buyCountChanged(buyCount);
                break;
            case R.id.goods_intro_add_to_cart:
                addGoodsToCart();
                break;
            case R.id.no_use_send_address_tv:
            case R.id.goods_intro_user_addr:
                Bundle b = new Bundle();
                b.putBoolean(AddressManageActivity.KEY_IS_SELECT_MODE, true);
                Intent selAddr = new Intent(getContext(), AddressManageActivity.class);
                selAddr.putExtras(b);
                startActivityForResult(selAddr, SELECT_ADDRESS_REQUEST_CODE, b);
                break;
            case R.id.goods_into_send_method:
                UiUtil.showOptionDialog(getContext(), SEND_MTHOD, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        binding.goodsIntoSendMethod.setText(SEND_MTHOD.get(options1));
                    }
                });
                break;
            default:
                break;
        }
    }

    private void buyGoodsNow() {
        if (goodsAPI == null) {
            goodsAPI = new GoodsAPI();
        }
        OrderEntity orderEntity = new OrderEntity();
        String disTypeStr = binding.goodsIntoSendMethod.getText().toString().trim();
        orderEntity.setDisType("邮寄".equals(disTypeStr) ? "0" : "1");
        //订单数据保存起来
        orderEntity.setAddress(sendProTo);
        Bundle mBundle = new Bundle();
        List<ProductEntity> list = new ArrayList<>();
        //购买数量
        goodsDetail.setNum(Integer.valueOf(binding.goodsIntroNum.getText().toString().trim()));
        list.add(goodsDetail);
        orderEntity.setGoods(list);
        mBundle.putParcelable(OrderConfirmFragment.ORDER_KEY, orderEntity);
        startContainerActivity(OrderConfirmFragment.class.getName(),mBundle);
    }

    private void addGoodsToCart() {
        if (shopCartApi == null) {
            shopCartApi = new ShopCartApi();
        }
        CartItemEntity entity = new CartItemEntity();
        entity.setGoodsId(goodsDetail.getId());
        entity.setNum(Integer.valueOf(binding.goodsIntroNum.getText().toString().trim()));
        shopCartApi.saveToShopCart(entity, new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                if (response.isSuccess()) {
                    ToastUtils.showShort("成功添加到购物车");
                } else {
                    ToastUtils.showShort(response.getMessage());
                }
            }
        });
    }

    private void buyCountChanged(int count) {
        binding.goodsIntroNum.setText(String.valueOf(count));
        binding.goodsIntroDelete.setImageResource(count <= 1 ? R.drawable.minus_gray : R.drawable.minus_dark_black);
        binding.goodsIntroTotalMoney.setText(StringUtil.formatSignMoney(count * goodsDetail.getPrice()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_ADDRESS_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    sendProTo = data.getExtras().getParcelable(AddressEditActivity.KEY_ADDREES_TO_EDIT);
                    setAddressUIWithAddress();
                }
                break;
            default:
                break;
        }
    }


    private final CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            switch (compoundButton.getId()) {
                case R.id.goods_intro_fav:
                    if (favApi == null) {
                        favApi = new FavoriteGoodsAPI();
                    }
                    if (b) {
                        handleChecked();
                        return;
                    }
                    handleUnchecked();
                    break;
                default:
                    break;
            }
        }
    };

    private void handleChecked() {
        favApi.addFavGoods(goodsDetail, new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                if (response.isSuccess()) {
                    EventBus.getDefault().post(new FavGoodsListViewModel.FavCheckedChangedEvent() {
                    });
                    ToastUtils.showShort("收藏成功");
                } else {
                    setCheckBoxWithoutNotify(false);
                    ToastUtils.showShort(response.getMessage());
                }
            }
        });
    }

    private void handleUnchecked() {
        favApi.deleteFavGoods(goodsDetail, new BaseResultCallback<BaseResponseEntity>() {
            @Override
            public void onResponse(BaseResponseEntity response) {
                if (response.isSuccess()) {
                    ToastUtils.showShort("收藏成功取消");
                    EventBus.getDefault().post(new FavGoodsListViewModel.FavCheckedChangedEvent() {
                    });
                } else {
                    setCheckBoxWithoutNotify(true);
                    ToastUtils.showShort(response.getMessage());
                }
            }
        });
    }

    private void setCheckBoxWithoutNotify(boolean checked) {
        binding.goodsIntroFav.setOnCheckedChangeListener(null);
        binding.goodsIntroFav.setChecked(checked);
        binding.goodsIntroFav.setOnCheckedChangeListener(checkListener);
    }
}
