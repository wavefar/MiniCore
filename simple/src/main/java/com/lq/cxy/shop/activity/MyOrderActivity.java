package com.lq.cxy.shop.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.lq.cxy.shop.R;
import com.lq.cxy.shop.fragment.OrderFragment;

import org.wavefar.lib.base.BaseSimpleActivity;
import org.wavefar.lib.ui.widget.pageindicator.PagerSlidingTabStrip;

import java.util.ArrayList;

/**
 * 我的订单
 * @author summer
 * @date 2018/9/9 下午3:04
 */
public class MyOrderActivity extends BaseSimpleActivity implements View.OnClickListener {

    private PagerSlidingTabStrip tabStrip;
    private ViewPager pager;
    private ArrayList<OrderTitle> mOrderTitle;
    @Override
    protected int getContentView() {
        return R.layout.activity_my_order;
    }

    @Override
    public void initData() {

        findViewById(R.id.iv_back).setOnClickListener(this);
        TextView titleTv = findViewById(R.id.toolbar_title);
        titleTv.setText("我的订单");


        initTitleData();

        pager = findViewById(R.id.pager);
        pager.setAdapter(new TabAdapter(getSupportFragmentManager()));

        tabStrip = findViewById(R.id.indicator);

        tabStrip.setShouldExpand(true);
        tabStrip.setIndicatorColorResource(R.color.bottom_tab_textcolor_selected);
        tabStrip.setViewPager(pager);

    }

    /**
     * 构造分类标题
     */
    private void initTitleData() {
        mOrderTitle = new ArrayList<>();
        mOrderTitle.add(new OrderTitle("待付款",0));
        mOrderTitle.add(new OrderTitle("已付款",1));
        mOrderTitle.add(new OrderTitle("配送中",3));
        mOrderTitle.add(new OrderTitle("已完成",4));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }


    public class TabAdapter extends FragmentStatePagerAdapter {
        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return OrderFragment.newInstance((mOrderTitle.get(position)).getStatus());
        }

        @Override
        public int getCount() {
            return mOrderTitle.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mOrderTitle.get(position).getTitle();
        }
    }

    private class OrderTitle {

        private OrderTitle(String title, int status) {
            this.title = title;
            this.status = status;
        }

        private String title;
        /**
         * 订单状态 0待付款1已购买2待分配3配送中4已完成5待评价
         */
        private int status;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
