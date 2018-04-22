package com.xytsz.xytaj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.xytsz.xytaj.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/4/12.
 * 信息展示页
 */
public class TrainDemoShowActivity extends AppCompatActivity {

    @Bind(R.id.traindemoshow_wb)
    WebView traindemoshowWb;
    private String title;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traindemoshow);
        ButterKnife.bind(this);

        if(getIntent() != null){
            title = getIntent().getStringExtra("title");
            url = getIntent().getStringExtra("url");
        }


    }
}
