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
import com.xytsz.xytaj.bean.TrainContent;
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
 * Created by admin on 2018/4/23.
 * <p>
 * <p>
 * 网页端的展示 培训环节
 */
public class TrainTestDetailActivity extends AppCompatActivity {

    @Bind(R.id.traintestdetail_wv)
    WebView traintestdetailWv;
    @Bind(R.id.traintestdetail_progressbar)
    LinearLayout traintestdetailProgressbar;
    private int tag;
    private String title;
    private TrainContent train;
    private String url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traintestdetail);
        ButterKnife.bind(this);
        if (getIntent() != null){
            tag = getIntent().getIntExtra("tag", -1);
            train = (TrainContent) getIntent().getSerializableExtra("train");
        }


        switch (tag) {
            case 0:
                title = "实施方案";
                //设置请求参数
                url = train.getAJTrainProgrammeUrl();
                break;
            case 1:
                title = "总结评估";
                url = train.getAJTrainSummaryUrl();
                break;
            case 2:
                title = "内容资料";
                url = train.getAJTrainContentUrl();
                break;
            case 3:
                title = "培训记录";
                url = train.getAJTrainRecordUrl();
                break;
            case 4:
                title = "培训通知";
                url = train.getAJTrainNoticeUrl();
                break;

        }

        initAcitonBar(title);
        initData();

    }

    private void initData() {
        traintestdetailProgressbar.setVisibility(View.VISIBLE);
        traintestdetailWv.loadUrl(url);
        WebSettings settings = traintestdetailWv.getSettings();
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        traintestdetailWv.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                traintestdetailProgressbar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                traintestdetailProgressbar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });

    }



    private void initAcitonBar(String title) {
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
