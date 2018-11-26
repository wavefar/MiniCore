package com.lq.cxy.shop.activity.merchant;

import android.os.Bundle;
import android.text.TextUtils;

import com.lq.cxy.shop.BuildConfig;
import com.lq.cxy.shop.R;
import com.lq.cxy.shop.fragment.merchant.MerchandiseManageFrag;
import com.lq.cxy.shop.fragment.merchant.MerchantCenterFrag;
import com.lq.cxy.shop.fragment.merchant.MerchantOrderFrag;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.model.entity.UserEntity;
import com.lq.cxy.shop.utils.Util;

import org.wavefar.lib.ui.activity.TabFragmentActivity;
import org.wavefar.lib.ui.fragment.agentweb.AgentWebFragment;


public class MerchantMainActivity extends TabFragmentActivity {

    private static final String TAG = "MainActivity";

    @Override
    public void initData() {
        super.initData();
        setBottomMenuAndIntent(new String[]{"1", "2", "3", "4"},
                new int[]{R.drawable.bottom_home_selector, R.drawable.bottom_goods_selector, R.drawable.bottom_cart_selector, R.drawable.bottom_user_selector},
                getResources().getStringArray(R.array.tabs_merchant),
                getIntentArr(), getFragBundle(), R.color.bottom_tab_text_color, 0);

        //设置背景
        setTabBackground(R.drawable.bg_bottom_tab);
        //特定跳转
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String menuId = bundle.getString("menu_id");
            startIabIntent(menuId);
        }
        Util.loadCategory();
    }


    /**
     * 准备tab的内容Intent
     */
    private Class[] getIntentArr() {
        return new Class[]{
                MerchantOrderFrag.class,
                MerchandiseManageFrag.class,
                AgentWebFragment.class,
                MerchantCenterFrag.class};
    }

    private Bundle getBundle(String url, String title) {
        Bundle bundle = new Bundle();
        bundle.putString(AgentWebFragment.URL_KEY, url);
        bundle.putBoolean(AgentWebFragment.TITLE_IS_HIDE_KEY, true);
        if (!TextUtils.isEmpty(title)) {
            bundle.putString(AgentWebFragment.TITLE_KEY, title);
        }
        return bundle;
    }

    private Bundle[] getFragBundle() {
        UserEntity user = UserAPI.getUserInfo();
        return new Bundle[]{null, null,
                getBundle(BuildConfig.HOST + "/statistics/index?customerId=" + user.getId(), ""),
                null};
    }
}
