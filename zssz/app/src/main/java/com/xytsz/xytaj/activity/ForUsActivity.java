package com.xytsz.xytaj.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.ApkUtils;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.HeaderProperty;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/9/15.
 * 关于我们界面
 */
public class ForUsActivity extends AppCompatActivity {

    @Bind(R.id.tv_forus_versioncode)
    TextView tvForusVersioncode;
    @Bind(R.id.image_qr_code)
    ImageView imageQrCode;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GlobalContanstant.MYSENDSUCCESS:
                    String imageUrl = (String) msg.obj;
                    Glide.with(getApplicationContext()).load(imageUrl).into(imageQrCode);
                    break;
                case GlobalContanstant.FAIL:
                    ToastUtil.shortToast(getApplicationContext(),"未获取");
                    imageQrCode.setImageResource(R.mipmap.ic_launcher);
                    break;
            }
        }
    };

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
        new Thread(){
            @Override
            public void run() {
                try {
                    String codeUrl = getCodeUrl();
                    if (codeUrl != null){
                        Message message = Message.obtain();
                        message.what = GlobalContanstant.MYSENDSUCCESS;
                        message.obj = codeUrl;
                        handler.sendMessage(message);

                    }
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.FAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    private List<HeaderProperty> headerList = new ArrayList<>();
    private String getCodeUrl() throws Exception{
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getImageCode);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        //添加cookie
//
        headerList.clear();
        HeaderProperty headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie, SpUtils.getString(getApplicationContext(),GlobalContanstant.CookieHeader));

        headerList.add(headerPropertyObj);

        httpTransportSE.call(null, envelope,headerList);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String json = object.getProperty(0).toString();
        return json;
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
