package com.xytsz.xytaj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.LightFragmentAdapter;
import com.xytsz.xytaj.fragment.FacilityCategroyFragment;
import com.xytsz.xytaj.global.GlobalContanstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/5/28.
 * <p>
 * 产品分类
 */
public class FacilityCategoryActivity extends AppCompatActivity {

    @Bind(R.id.facility_tab)
    TabLayout facilityTab;
    @Bind(R.id.facility_viewpager)
    ViewPager facilityViewpager;
    private String categroy;

    private String title;
    private ArrayList<Fragment> fragments;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            categroy = getIntent().getStringExtra("category");
        }
        setContentView(R.layout.activity_facilitycategroy);
        ButterKnife.bind(this);
        switch (categroy) {
            case GlobalContanstant.BOOM:
                title = "粉尘防爆";
                break;
            case GlobalContanstant.FIRE:
                title = "消防安全";
                break;
            case GlobalContanstant.HEALTH:
                title = "职业卫生";
                break;
            case GlobalContanstant.ENVIR:
                title = "环境保护";
                break;
        }
        initActionbar(title);

        initData();

    }

    private void initData() {
        List<String> titles = new ArrayList<>();
        titles.clear();
        fragments = new ArrayList<>();
        fragments.clear();
        titles.add("灭火器");
        titles.add("消防栓");
        titles.add("消防帽");
        titles.add("消防服");
        titles.add("消防车");

        for (int i = 0; i < titles.size(); i++) {

            facilityTab.addTab(facilityTab.newTab().setText(titles.get(i)));
            fragments.add(FacilityCategroyFragment.newInstance(i));

        }


        //添加viewpager的adapter
        LightFragmentAdapter lightFragmentAdapter = new LightFragmentAdapter(getSupportFragmentManager(), fragments, titles);
        facilityViewpager.setAdapter(lightFragmentAdapter);

        //让标签 跟着viewpager 滑动
        facilityTab.setupWithViewPager(facilityViewpager);

        facilityTab.setTabsFromPagerAdapter(lightFragmentAdapter);

    }

    private void initActionbar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
