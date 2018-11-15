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
 * Created by admin on 2018/9/29.
 * </p>
 */
public class CheckStandardDetailActivity extends AppCompatActivity {

    @Bind(R.id.standard_wv)
    WebView standardWv;
    @Bind(R.id.standarddetail_progressbar)
    LinearLayout standarddetailProgressbar;
    private String title;
    private int type;
    private int deviceID;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContanstant.CHECKPASS:
                    standarddetailProgressbar.setVisibility(View.GONE);
                    String data = (String) msg.obj;
                    if (data != null) {
                        standardWv.loadUrl(data);
                        WebSettings settings = standardWv.getSettings();
                        settings.setJavaScriptEnabled(true);
                        settings.setUseWideViewPort(true);
                        standardWv.setWebViewClient(new WebViewClient() {
                            @Override
                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                standarddetailProgressbar.setVisibility(View.VISIBLE);
                                super.onPageStarted(view, url, favicon);

                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                standarddetailProgressbar.setVisibility(View.GONE);
                                super.onPageFinished(view, url);
                            }
                        });
                    } else {
                        ToastUtil.shortToast(getApplicationContext(), "获取出错");
                    }
                    break;
                case GlobalContanstant.FAIL:
                    standarddetailProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), "获取出错");
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            title = getIntent().getStringExtra("name");
            type = getIntent().getIntExtra("type", -1);
            deviceID = getIntent().getIntExtra("DeviceID", -1);

        }
        setContentView(R.layout.activity_checkstandarddeatil);
        ButterKnife.bind(this);

        initAcitionbar();
        initData();

    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String data = getData();
                    Message message = Message.obtain();
                    message.obj = data;
                    message.what = GlobalContanstant.CHECKPASS;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.FAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();


    }

    private String getData() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getStandard);
        soapObject.addProperty("DeviceID", deviceID);
        soapObject.addProperty("Type", type);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;

        return object.getProperty(0).toString();

    }

    private void initAcitionbar() {
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
