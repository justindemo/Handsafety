package com.xytsz.xytaj.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.SytemManageList;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/4/20.
 * <p>
 * 制度展示
 */
public class InstitutionShowActivity extends AppCompatActivity {

    @Bind(R.id.institutionshow_wv)
    WebView institutionshowWv;
    @Bind(R.id.institutionshow_pb)
    LinearLayout institutionshowPb;
    private int tag;
    private String title;
    private String  url;


    private void initView(String json) {
        institutionshowWv.loadUrl(json);
        WebSettings settings = institutionshowWv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);

        institutionshowWv.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                institutionshowPb.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                institutionshowPb.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institutionshow);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            tag = getIntent().getIntExtra("tag", -1);
            url = getIntent().getStringExtra("content");

        }


        switch (tag) {
            case 1:
                title = "粉尘防爆";
                break;
            case 2:
                title = "消防安全";
                break;
            case 0:
                title = "职业卫生";
                break;
            case 3:
                title = "环境保护";
                break;
            case 4:
                title = "防洪演练";
                break;

        }

        initActionBar(title);
        initData();

    }

    private void initData() {
        institutionshowPb.setVisibility(View.VISIBLE);
        initView(url);

    }



    private void initActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(title);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
