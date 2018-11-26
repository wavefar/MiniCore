package org.wavefar.lib.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wavefar.lib.R;
import org.wavefar.lib.ui.widget.pageindicator.CirclePageIndicator;

import java.util.ArrayList;


/**
 * BannerView使用说明：
 * <pre>1、首先初始化initBannerViewData 图片集必选*</pre>
 * <pre>2、监听视图发生变化setOnBannerChangerListener（常用于title切换）</pre>
 *
 * @author summer
 * @description BannerView 控件封装
 * @date 2014年5月15日 下午6:41:23
 */
public class BannerView extends LinearLayout {

    /**
     * 自动播放间隔时间默认5s
     */
    private long delayMillis = 5000;
    /**
     * 装载视图集容器
     */
    private ViewPager mViewPager;
    private BannerAdaptor mBannerAdaptor;
    /**
     * banner底部视图包括圆点
     */
    private View mBannerBottomView;
    /**
     * banner 底部 title
     */
    private TextView mBannerTitleTV;
    /**
     * 偏移
     */
    private float lastX = 0;

    /**
     * 圆点集
     */
    private CirclePageIndicator mCircleFlowIndicator;
    private boolean mIsAutoScroll;

    public BannerView(Context context) {
        super(context);
        initView(context);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 初始化界面
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initView(Context context) {

        View headerView = LayoutInflater.from(context).inflate(
                R.layout.banner_item, null);


        mBannerBottomView = headerView.findViewById(R.id.banner_rl);
        mViewPager = headerView.findViewById(R.id.pager);
        mBannerTitleTV = headerView.findViewById(R.id.titleTV);
        mCircleFlowIndicator = headerView.findViewById(R.id.viewflowindic);
        addView(headerView);

        mViewPager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getRawX();
                        break;
                    case MotionEvent.ACTION_UP:
                        lastX = event.getRawX() - lastX;
                        if (Math.abs(lastX) > 20) {
                            stopAutoScroll();
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });


    }

    /**
     * 初始化banner信息
     *
     * @param views 布局集，可以是View设置布局背景即可
     */
    public void initBannerViewData(ArrayList<View> views) {
        mBannerAdaptor = new BannerAdaptor(views);
        mViewPager.setAdapter(mBannerAdaptor);
        mCircleFlowIndicator.setViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
        startAutoScroll();
    }

    /**
     * 设置Banner发生改变的回调函数
     *
     * @param onPageChangeListener
     */
    public void setOnBannerChangerListener(OnPageChangeListener onPageChangeListener) {
        mCircleFlowIndicator.setOnPageChangeListener(onPageChangeListener);
    }

    /**
     * 获取BannerBottom 底部的布局可以设置样式
     *
     * @return
     */
    public View getBannerBottomView() {
        return mBannerBottomView;
    }


    /**
     * 获取bannerTitle 的布局可以设置样式
     *
     * @return
     */
    public TextView getBannerTitleView() {
        return mBannerTitleTV;
    }

    /**
     * 获取圆点集视图可以控制样式
     *
     * @return
     */
    public CirclePageIndicator getmCircleFlowIndicator() {
        return mCircleFlowIndicator;
    }

    /**
     * 获取ViewPager可以自定义
     *
     * @return
     */
    public ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * 获取适配器
     *
     * @return
     */
    public BannerAdaptor getBannerAdaptor() {
        return mBannerAdaptor;
    }

    /**
     * 设置间隔时间
     *
     * @param delayMillis 毫秒位单位
     */
    public void setDelayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    /**
     * 处理自动播放消息处理
     */
    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            int count = mBannerAdaptor.getCount();
            if (count < 2) {
                stopAutoScroll();
                return false;
            }
            int item = mViewPager.getCurrentItem();
            int index = ++item % count;
            mCircleFlowIndicator.setCurrentItem(index);
            mHandler.sendEmptyMessageDelayed(0, delayMillis);
            return true;
        }
    });

    /**
     * 自动播放banner
     */
    public void startAutoScroll() {
        if (mBannerAdaptor == null) {
            throw new IllegalStateException("请先调用initBannerViewData()方法初始化数据");
        }
        if (!mIsAutoScroll) {
            mIsAutoScroll = true;
            mHandler.sendEmptyMessageDelayed(0, delayMillis);
        }
    }

    /**
     * 停止自动播放
     */
    public void stopAutoScroll() {
        if (mBannerAdaptor == null) {
            throw new IllegalStateException("请先调用initBannerViewData()方法初始化数据");
        }
        mIsAutoScroll = false;
        if (mHandler != null) {
            mHandler.removeMessages(0);
        }
    }

    /**
     * banner适配器
     *
     * @author summer
     * @description
     * @date 2014年5月16日 上午9:59:51
     */
    public class BannerAdaptor extends PagerAdapter {

        private ArrayList<View> list;

        public BannerAdaptor(ArrayList<View> childlist) {
            super();
            this.list = childlist;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 删除页卡
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 添加页卡
            container.addView(list.get(position), 0);
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

    }

}