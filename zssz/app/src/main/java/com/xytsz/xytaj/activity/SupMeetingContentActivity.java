package com.xytsz.xytaj.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.xytsz.xytaj.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/5/9.
 *
 * 会议内容
 */
public class SupMeetingContentActivity extends AppCompatActivity {

    @Bind(R.id.meetingcontent_wv)
    WebView meetingcontentWv;
    @Bind(R.id.meetingcontent_progressbar)
    LinearLayout meetingcontentProgressbar;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supmeetingcontent);
        ButterKnife.bind(this);

        if (getIntent() != null){
            url = getIntent().getStringExtra("url");
        }
        initAcitonBar();
        initData();

    }

    private void initData() {
        meetingcontentProgressbar.setVisibility(View.VISIBLE);
        meetingcontentWv.loadUrl(url);
        WebSettings settings = meetingcontentWv.getSettings();
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        meetingcontentWv.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                meetingcontentProgressbar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                meetingcontentProgressbar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });

    }



    private void initAcitonBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("会议内容");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
