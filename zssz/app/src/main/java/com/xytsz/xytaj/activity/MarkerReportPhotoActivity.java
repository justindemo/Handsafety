package com.xytsz.xytaj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.PhotoShowAdapter;
import com.xytsz.xytaj.bean.ImageUrl;

import java.util.List;

/**
 * Created by admin on 2017/3/31.
 * 定位图片显示
 */
public class MarkerReportPhotoActivity extends AppCompatActivity {

    private ViewPager mvp;
    private List<ImageUrl> imageUrllist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            imageUrllist = (List<ImageUrl>) getIntent().getSerializableExtra("imageUrls");
        }


        setContentView(R.layout.activity_photoshow);
        mvp = (ViewPager) findViewById(R.id.photo_vp);
        initData();
    }

    private void initData() {
        //根据图片URL获取图片传入adapter里面
        if (imageUrllist != null) {
            PhotoShowAdapter adapter = new PhotoShowAdapter(imageUrllist);
            mvp.setAdapter(adapter);
        }
    }
}
