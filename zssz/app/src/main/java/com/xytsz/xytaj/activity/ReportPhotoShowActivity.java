package com.xytsz.xytaj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.ReportPhotoShowAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/7/25.
 *
 *
 */
public class ReportPhotoShowActivity extends AppCompatActivity {


    @Bind(R.id.photoshow_viewpager)
    ViewPager photoshowViewpager;
    private List<String> imageUrllist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            imageUrllist = (List<String>) getIntent().getSerializableExtra("imageUrllist");
        }

        setContentView(R.layout.activity_reportphotoshow);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("上报图片");

        }



        ReportPhotoShowAdapter adapter = new ReportPhotoShowAdapter(imageUrllist);
        photoshowViewpager.setAdapter(adapter);


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
