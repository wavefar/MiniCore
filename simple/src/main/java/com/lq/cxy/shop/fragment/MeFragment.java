package com.lq.cxy.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lq.cxy.shop.R;
import com.lq.cxy.shop.activity.AddressManageActivity;
import com.lq.cxy.shop.activity.FaveGoodsListActivity;
import com.lq.cxy.shop.activity.MyOrderActivity;
import com.lq.cxy.shop.activity.merchant.AddShopActivity;
import com.lq.cxy.shop.activity.merchant.LicenseSupply;
import com.lq.cxy.shop.activity.merchant.MerchantMainActivity;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.entity.ShopAddEntity;
import com.lq.cxy.shop.model.entity.UserEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;
import com.lq.cxy.shop.model.http.base.BaseSimpleResultCallback;
import com.lq.cxy.shop.model.merchant.ShopApi;
import com.lq.cxy.shop.utils.Util;

import org.greenrobot.eventbus.Subscribe;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.utils.IntentUtils;

/**
 * 我的个人中心
 *
 * @author summer
 */
public class MeFragment extends Fragment implements View.OnClickListener {

    private UserEntity userInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initView(view);
        if (UserAPI.getUserInfo() != null) {
            handleRefreshUser(null);
        }
        Util.loadCategory();
        return view;
    }


    private void initView(View view) {
        view.findViewById(R.id.iv_back).setVisibility(View.INVISIBLE);
        TextView title_tv = view.findViewById(R.id.toolbar_title);
        title_tv.setText("个人中心");

        ImageView iv_more = view.findViewById(R.id.iv_more);
        iv_more.setImageResource(R.drawable.ic_setting);
        iv_more.setVisibility(View.VISIBLE);

        iv_more.setOnClickListener(this);
        view.findViewById(R.id.myshipaddress_tv).setOnClickListener(this);
        view.findViewById(R.id.profile_tv).setOnClickListener(this);
        view.findViewById(R.id.myorder_tv).setOnClickListener(this);
        view.findViewById(R.id.myjifen_tv).setOnClickListener(this);
        view.findViewById(R.id.mywallet_tv).setOnClickListener(this);
        view.findViewById(R.id.mysaler_tv).setOnClickListener(this);
        view.findViewById(R.id.mymark_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //设置
            case R.id.iv_more:
                IntentUtils.startContainerActivity(SettingFragment.class.getName());
                break;
            //收货地址
            case R.id.myshipaddress_tv:
                startActivity(new Intent(getContext(), AddressManageActivity.class));
                break;
            //个人信息
            case R.id.profile_tv:
                IntentUtils.startContainerActivity(UserDetailFragment.class.getName());
                break;
            //我的订单
            case R.id.myorder_tv:
                IntentUtils.redirect(MyOrderActivity.class);
                break;
            //我的积分
            case R.id.myjifen_tv:

                break;
            //我的钱包
            case R.id.mywallet_tv:
                IntentUtils.startContainerActivity(DepositFragment.class.getName());
                break;
            //我的店铺
            case R.id.mysaler_tv:
                UserEntity user = userInfo == null ? UserAPI.getUserInfo() : userInfo;
                switch (user.getBusiness()) {
                    case 1:
                        Util.loadAndCacheLisence(null);
                        startActivity(new Intent(getContext(), AddShopActivity.class));
                        break;
                    case 2:
                        if (UserAPI.getShopInfo() == null) {
                            new ShopApi().queryShopInfo(new BaseResultCallback<BaseResponseEntity<ShopAddEntity>>() {
                                @Override
                                public void onResponse(BaseResponseEntity<ShopAddEntity> response) {
                                    if (response.isSuccess()) {
                                        UserAPI.saveShopInfo(response.getContent());
                                    }
                                }
                            });
                        }
                        startActivity(new Intent(getContext(), MerchantMainActivity.class));
                        break;
                    default:
                        startActivity(new Intent(getContext(), LicenseSupply.class));
                }
                Log.d("pppp", UserAPI.getUserInfo().getUserId()
                        + "_" + UserAPI.getUserInfo().getToken() + "|||======" + UserAPI.getUserInfo().isMerchantUser());
//                startActivity(new Intent(getContext(), MerchantMainActivity.class));
                break;
            //我的收藏
            case R.id.mymark_tv:
                IntentUtils.redirect(FaveGoodsListActivity.class);
            default:
        }
    }

    @Subscribe
    public void handleRefreshUser(RefreshUserDetailEvent event) {
        new UserAPI().getUserDetail(UserAPI.getUserInfo().getUserId(),
                new BaseSimpleResultCallback<BaseResponseEntity<UserEntity>, UserEntity>() {
                    @Override
                    public void onSucceed(UserEntity entity) {
                        userInfo = entity;
                        userInfo.setToken(UserAPI.getUserInfo().getToken());
                        userInfo.setUserId(UserAPI.getUserInfo().getUserId());
                        UserAPI.saveUserInfo(userInfo);
                    }
                });
    }

    public interface RefreshUserDetailEvent {
    }
}
