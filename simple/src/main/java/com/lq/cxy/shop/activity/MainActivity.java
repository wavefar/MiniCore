package com.lq.cxy.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.lq.cxy.shop.R;
import com.lq.cxy.shop.fragment.HomeFragment;
import com.lq.cxy.shop.fragment.MeFragment;
import com.lq.cxy.shop.fragment.ShopCarFragment;
import com.lq.cxy.shop.fragment.GoodFragment;
import com.lq.cxy.shop.model.UserAPI;
import com.lq.cxy.shop.utils.AppDownloadManager;

import org.wavefar.lib.ui.activity.TabFragmentActivity;
import org.wavefar.lib.utils.AppUtils;
import org.wavefar.lib.utils.IntentUtils;


/**
 * 主页
 * @author summer
 */
public class MainActivity extends TabFragmentActivity {

    private static final String TAG = "MainActivity";
    @Override
    public void initData() {
        super.initData();
        setBottomMenuAndIntent(new String[]{"1", "2", "3", "4"},
                new int[]{R.drawable.bottom_home_selector, R.drawable.bottom_goods_selector, R.drawable.bottom_cart_selector, R.drawable.bottom_user_selector},
                getResources().getStringArray(R.array.tabs),
                getIntentArr(), R.color.bottom_tab_text_color, 0);

        setTabBarChangeListener(new TabBarChangeListener() {
            @Override
            public void getTabButton(String currentTag, String lastTag) {
                // 未登录成功,在指定的地方做拦截
                if ((!UserAPI.checkLogin())) {
                    if ((currentTag.equals("4"))) {
                        Bundle bundle = new Bundle();
                        bundle.putString("menu_id", currentTag);
                        startIabIntent(lastTag.equals("4") ? "1" : lastTag);
                        IntentUtils.redirect(LoginActivity.class);
                    }
                }
            }
        });


        //设置背景
        setTabBackground(R.drawable.bg_bottom_tab);
        //特定跳转
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String menuId = bundle.getString("menu_id");
            startIabIntent(menuId);
        }

        //推送初始化
        initPush();
        //检查是否有更新
        checkUpdate();
    }


    /**
     * 初始化jpush推送
     */
    private void initPush() {
//        LogUtil.d(TAG, "initPush");
//        UserEntity userEntity = UserAPI.getUserInfo();
//        if (userEntity != null && !StringUtil.isEmpty(userEntity.getUserId())) {
//            JPushInterface.setAlias(getApplicationContext(), 0, userEntity.getUserId());
//        }
//        Set<String> strings = new HashSet<>();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        strings.add(DeviceUtils.getDeviceId());
//        JPushInterface.setTags(getApplicationContext(),0,strings);
    }

    private void checkUpdate() {
        AppDownloadManager appDownloadManager = new AppDownloadManager(this,
                false);
        appDownloadManager.update();
    }

    /**
     * 准备tab的内容Intent
     */
    private Class[] getIntentArr() {
        return new Class[]{
                HomeFragment.class,
                GoodFragment.class,
                ShopCarFragment.class,
                MeFragment.class};
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            AppUtils.exitShowToast();
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("1");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
