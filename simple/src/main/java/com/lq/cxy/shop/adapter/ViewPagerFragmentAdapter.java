package com.lq.cxy.shop.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * viewpager切换fragment的适配器
 * Created by Chony on 2015/9/29.
 */
public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {

    private SparseArray<Fragment> fragmengs;
    private List<String> titles = new ArrayList<>();

    public ViewPagerFragmentAdapter(FragmentManager fm, SparseArray<Fragment> fragmengs) {
        this(fm, fragmengs, null);
    }

    public ViewPagerFragmentAdapter(FragmentManager fm, SparseArray<Fragment> fragmengs, List<String> titles) {
        super(fm);
        this.fragmengs = fragmengs;
        if (titles != null) {
            this.titles.addAll(titles);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return null != fragmengs ? fragmengs.get(position) : null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles.size() == 0) {
            return super.getPageTitle(position);
        }
        return titles.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return null != fragmengs ? fragmengs.size() : 0;
    }
}
