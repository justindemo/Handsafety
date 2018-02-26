package com.xytsz.xytaj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.util.IntentUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2017/10/12.
 * <p>
 * 设施管理
 */
public class FacilityManageActivity extends AppCompatActivity {

    @Bind(R.id.head_road)
    RelativeLayout headRoad;
    @Bind(R.id.bridge_title)
    TextView bridgeTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilitymanage);
        ButterKnife.bind(this);
        initAcitionbar();
    }


    @OnClick({R.id.head_road, R.id.bridge_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_bridge:
                //桥梁
                IntentUtil.startActivity(view.getContext(),BridgeNumberActivity.class);
                break;
            case R.id.head_road:
                //道路
                IntentUtil.startActivity(view.getContext(), RoadNumberActivity.class);
                break;
        }
    }

    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.facilitymanage);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


}
