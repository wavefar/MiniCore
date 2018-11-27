package org.wavefar.lib.ui.activity;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

import org.wavefar.lib.R;
import org.wavefar.lib.base.BaseSimpleActivity;

/**
 * 通过FragmentTabHost实现
 * Tab布局基类包括（底部菜单和顶部菜单的管理）
 *
 * @author summer
 */
public class TabFragmentActivity extends BaseSimpleActivity {

    private FragmentTabHost mTabHost;

    /**
     * 记录上次显示视图
     */
    private View lastView;
    /**
     * 记录上次位置
     */
    private static String mLastTag;
    private TabBarChangeListener tabBarChangeListener;

    public FragmentTabHost getTabHost() {
        return mTabHost;
    }

    @Override
    protected int getContentView() {
        return R.layout.tabhost_fragment;
    }

    @Override
    public void initData() {
        this.mTabHost = findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                if (tabBarChangeListener != null) {
                    tabBarChangeListener.getTabButton(tabId, mLastTag);
                }
                mLastTag = tabId;
                if (lastView != null && lastView.getVisibility() == View.VISIBLE) {
                    lastView.setVisibility(View.GONE);
                }
                FrameLayout frameLayout = mTabHost.getTabContentView();
                frameLayout.setVisibility(View.VISIBLE);
                lastView = frameLayout;
            }
        });
    }


    /**
     * 填充底部菜单 注意所有参数一一对应
     *
     * @param menuId                 预先设置菜单ID
     * @param topIconResId           菜单图标资源文件数组
     * @param strResId               菜单文字资源文件数组
     * @param fragments              片段类数组
     * @param titleColor             菜单文字颜色
     * @param tabBottomBackgroundRes 菜单背景颜色
     */
    public void setBottomMenuAndIntent(String[] menuId, int[] topIconResId,
                                       String[] strResId, Class[] fragments, int titleColor, int tabBottomBackgroundRes) {
        setBottomMenuAndIntent(menuId, topIconResId, strResId, fragments, null, titleColor, tabBottomBackgroundRes);
    }

    /**
     * 填充底部菜单 注意所有参数一一对应
     *
     * @param menuId                 预先设置菜单ID
     * @param topIconResId           菜单图标资源文件数组
     * @param strResId               菜单文字资源文件数组
     * @param fragments              片段类数组
     * @param bundles                默认参数
     * @param titleColor             菜单文字颜色
     * @param tabBottomBackgroundRes 菜单背景颜色
     */
    public void setBottomMenuAndIntent(String[] menuId, int[] topIconResId,
                                       String[] strResId, Class[] fragments, Bundle[] bundles, int titleColor, int tabBottomBackgroundRes) {
        if (null != mTabHost) {
            for (int j = 0; j < menuId.length; j++) {
                // 菜单按钮布局文件
                View view1 = View.inflate(TabFragmentActivity.this, R.layout.tab_item,
                        null);
                TextView titleTv = view1.findViewById(R.id.tab_textview_title);
                titleTv.setCompoundDrawablesWithIntrinsicBounds(0, topIconResId[j], 0, 0);
                titleTv.setText(strResId[j]);
                if (tabBottomBackgroundRes != 0) {
                    view1.setBackgroundResource(tabBottomBackgroundRes);
                }
                if (titleColor != 0) {
                    ColorStateList colorStateList = getResources().getColorStateList(titleColor);
                    titleTv.setTextColor(colorStateList);
                    //设置默认着色
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        titleTv.setCompoundDrawableTintList(colorStateList);
                    } else {
                        Drawable tintIcon = DrawableCompat.wrap(titleTv.getCompoundDrawables()[1]);
                        DrawableCompat.setTintList(tintIcon, colorStateList);
                        titleTv.setCompoundDrawablesWithIntrinsicBounds(null, tintIcon, null, null);
                    }
                }
                /* 初始化意图 */
                mTabHost.addTab(buildTabSpec(String.valueOf(menuId[j]), view1), fragments[j], bundles == null ? null : bundles[j]);
            }
        }
    }

    /**
     * 是否隐藏底部
     *
     * @param isVisibility 是否显示底部菜单
     */
    public void hideBottomMenu(boolean isVisibility) {
        if (isVisibility) {
            mTabHost.getTabWidget().setVisibility(View.GONE);
        } else {
            mTabHost.getTabWidget().setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置底部背景图片
     *
     * @param bgResId 资源ID
     */
    public void setTabBackground(int bgResId) {
        mTabHost.getTabWidget().setBackgroundResource(bgResId);
    }

    /**
     * 初始化tab
     *
     * @param tag  唯一标示
     * @param view 每个选项的视图
     * @return
     */
    private TabHost.TabSpec buildTabSpec(String tag, View view) {

        return mTabHost.newTabSpec(tag).setIndicator(view);
    }

    /**
     * 启动指定的页面
     *
     * @param menuId
     */
    public void startIabIntent(String menuId) {
        this.mTabHost.setCurrentTabByTag(menuId);
    }

    /**
     * 必须先构造菜单再调用该方法，不然不显示
     * 菜单选项中消息显示
     *
     * @param postion  索引从0开始,选择显示菜单位置 如果大于菜单项默认为0
     * @param bgImgRes 提示信息的背景图标 可选 0
     * @param num      显示数量
     */
    public void setTipStr(int postion, int bgImgRes, int num) {
        TabWidget tab = getTabHost().getTabWidget();
        int count = tab.getChildCount();
        if (count > 0) {
            postion = postion > count ? 0 : postion;
            View menuView = tab.getChildAt(postion);
            TextView unReadTv = menuView.findViewById(R.id.tab_textview_unread);
            unReadTv.setVisibility(View.VISIBLE);
            if (bgImgRes != 0) {
                unReadTv.setBackgroundResource(bgImgRes);
            }
            unReadTv.setText(String.valueOf(num));
        }
    }

    /**
     * 隐藏菜单消息
     *
     * @param position 索引从0开始，如果大于菜单项默认为0
     */
    public void hideTipStr(int position) {
        TabWidget tab = getTabHost().getTabWidget();
        int count = tab.getChildCount();
        if (count > 0) {
            position = position > count ? 0 : position;
            View menuView = tab.getChildAt(position);
            TextView unReadTv = menuView.findViewById(R.id.tab_textview_unread);
            unReadTv.setVisibility(View.GONE);
        }
    }

    /**
     * 设置监听器
     *
     * @param tabBarChangeListener
     */
    public void setTabBarChangeListener(TabBarChangeListener tabBarChangeListener) {
        this.tabBarChangeListener = tabBarChangeListener;
    }

    /**
     * 切换监听器
     *
     * @author Administrator
     */
    public interface TabBarChangeListener {

        /**
         * 获取当前和最后点击的按钮索引别名
         *
         * @param currentTag 当前索引别名
         * @param lastTag    最后索引别名
         */
        void getTabButton(String currentTag, String lastTag);
    }

}
