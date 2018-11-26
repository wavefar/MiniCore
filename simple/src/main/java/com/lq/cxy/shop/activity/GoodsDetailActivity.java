package com.lq.cxy.shop.activity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lq.cxy.shop.R;
import com.lq.cxy.shop.fragment.GoodsAssessFragment;
import com.lq.cxy.shop.fragment.GoodsDetailBaseFrag;
import com.lq.cxy.shop.fragment.GoodsDetailFragment;
import com.lq.cxy.shop.fragment.GoodsIntroFragment;
import com.lq.cxy.shop.model.GoodsAPI;
import com.lq.cxy.shop.model.entity.ProductEntity;
import com.lq.cxy.shop.model.http.base.BaseResponseEntity;

import org.greenrobot.eventbus.EventBus;
import org.wavefar.lib.base.BaseSimpleActivity;
import org.wavefar.lib.net.callback.BaseResultCallback;
import org.wavefar.lib.ui.widget.BottomNavigationViewEx;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 商品详情
 * @author Administrator
 */
public class GoodsDetailActivity extends BaseSimpleActivity implements View.OnClickListener {
    private ProductEntity goodsDetail;
    private boolean isEntityUpdated;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private int mlastId;
    /**
     * 定义主页面片段
     */
    private final static Map<Integer, Fragment> fragmentMap = new LinkedHashMap<>();

    static {
        fragmentMap.put(R.id.goods_top_introduction, new GoodsIntroFragment());
        fragmentMap.put(R.id.goods_top_detail, new GoodsDetailFragment());
        fragmentMap.put(R.id.goods_top_assess, new GoodsAssessFragment());
    }

    @Override
    protected void getBundle(Bundle bundle) {
        goodsDetail = bundle.getParcelable(GoodsDetailBaseFrag.KEY_GOODS_DETAIL);
        Collection<Fragment> fragments = fragmentMap.values();
        for (Fragment frag: fragments) {
            frag.setArguments(bundle);
        }

    }

    private void reloadGoodsDetailInfo() {
        new GoodsAPI().getGoodsDetail(goodsDetail, new BaseResultCallback<BaseResponseEntity<ProductEntity>>() {
            @Override
            public void onResponse(BaseResponseEntity<ProductEntity> response) {
                if (response.isSuccess()) {
                    goodsDetail = response.getContent();
                    isEntityUpdated = true;
                    EventBus.getDefault().postSticky(goodsDetail);
                }
            }
        });
    }

    @Override
    public void initData() {
        bottomNavigationViewEx = findViewById(R.id.goods_detail_navigation);
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switchFrament(id);
                mlastId = id;
                return true;
            }
        });
        initFragments();
        findViewById(R.id.head_back_root).setOnClickListener(this);
        TextView title = findViewById(R.id.head_title);
        title.setText(R.string.goods_detail_title);
        findViewById(R.id.head_more).setVisibility(View.GONE);
        reloadGoodsDetailInfo();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_goods_detail;
    }

    /**
     * 切换Fragment
     *
     * @param id 需要显示的Fragment的id索引
     */
    public void switchFrament(int id) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = fragmentMap.get(id);
        transaction.hide(fragmentMap.get(mlastId));
        //这样添加增加页面加载速度
        if (fragment == null) {
            return;
        }

        if (!fragment.isAdded()) {
            transaction.add(R.id.goods_detail_container, fragment);
        }
        transaction.show(fragment).commit();
    }

    private void initFragments() {
        //默认选中第一个
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mlastId = R.id.goods_top_introduction;
        transaction.add(R.id.goods_detail_container, fragmentMap.get(mlastId));
        transaction.commit();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_root:
                finish();
                break;
                default:break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isEntityUpdated) {
            EventBus.getDefault().removeStickyEvent(goodsDetail);
        }
    }
}
