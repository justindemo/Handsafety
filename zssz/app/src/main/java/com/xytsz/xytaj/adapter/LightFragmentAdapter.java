package com.xytsz.xytaj.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xytsz.xytaj.bean.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/7/11.
 *
 * 照明设施根布局的 viewpager 的adapter
 */
public class LightFragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
    private List<String> titles;

    public LightFragmentAdapter(FragmentManager supportFragmentManager, ArrayList<Fragment> fragments,
                                List<String> titles) {
        super(supportFragmentManager);


        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return titles.get(position % titles.size());
    }
}
