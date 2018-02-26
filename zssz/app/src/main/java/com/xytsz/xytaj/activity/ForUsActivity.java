package com.xytsz.xytaj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.util.ApkUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/9/15.
 * 关于我们界面
 */
public class ForUsActivity extends AppCompatActivity {

    @Bind(R.id.tv_forus_versioncode)
    TextView tvForusVersioncode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forus);
        ButterKnife.bind(this);
        initAcitionbar();
        initData();
    }

    private void initData() {
        String versionName = ApkUtils.getVersionName(getApplicationContext());
        tvForusVersioncode.setText(versionName);
    }

    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("关于我们");
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
