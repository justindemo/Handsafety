package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.TraintestShowAdapter;
import com.xytsz.xytaj.bean.TrainContent;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/4/12.
 * 培训通知
 */
public class TrainDemoActivity extends AppCompatActivity {

    @Bind(R.id.traindemo_rv)
    RecyclerView traindemoRv;
    @Bind(R.id.traindemo_progressbar)
    LinearLayout traindemoProgressbar;
    private String title;
    private int tag;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GlobalContanstant.FAIL:
                    traindemoProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(),"获取数据异常");
                    break;
                case GlobalContanstant.MYSENDSUCCESS:
                    traindemoRv.setVisibility(View.GONE);
                    String json = (String) msg.obj;
                    if (json != null && !json.equals("[]")){
                        final List<TrainContent> trainContents = JsonUtil.jsonToBean(json, new TypeToken<List<TrainContent>>() {
                        }.getType());

                        if (trainContents.size()!= 0){
                            TraintestShowAdapter traintestShowAdapter = new TraintestShowAdapter(trainContents);
                            traindemoRv.setAdapter(traintestShowAdapter);
                            traintestShowAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
                                @Override
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                    Intent intent = new Intent(TrainDemoActivity.this,TrainDemoShowActivity.class);
                                    intent.putExtra("title",title);
                                    //intent.putExtra("url",trainContents.get(position).getUrl());
                                    startActivity(intent);
                                }


                            });
                        }else {
                            ToastUtil.shortToast(getApplicationContext(),"当前没有培训");
                        }
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traindemo);
        ButterKnife.bind(this);

        if (getIntent() != null){
            title = getIntent().getStringExtra("title");
            tag = getIntent().getIntExtra("tag", -1);
        }

        initactionBar(title);
        initData();
    }

    private void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        traindemoRv.setLayoutManager(manager);
        traindemoProgressbar.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                try {
                    String data = getData(tag);
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.MYSENDSUCCESS;
                    message.obj = data;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.FAIL;

                    handler.sendMessage(message);
                }
            }
        }.start();

    }

    private String getData(int tag)throws Exception{
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.trainDemomethod);
//        soapObject.addProperty("login_ID", loginID);
//        soapObject.addProperty("pwd", pWD);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();


        return result;
    }

    private void initactionBar(String title) {
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
