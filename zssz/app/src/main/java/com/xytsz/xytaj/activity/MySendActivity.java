package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.MyApplication;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.MyReportAdapter;
import com.xytsz.xytaj.bean.AudioUrl;
import com.xytsz.xytaj.bean.ForMyDis;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/12/14.
 * 我下派的
 */
public class MySendActivity extends AppCompatActivity {


    @Bind(R.id.lv_mysend)
    ListView lvMysend;
    @Bind(R.id.pb_mysend)
    ProgressBar pbMysend;
    @Bind(R.id.tv_fail)
    TextView tvFail;
    private int personId;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GlobalContanstant.MYSENDFAIL:
                    tvFail.setVisibility(View.VISIBLE);
                    pbMysend.setVisibility(View.GONE);
                    tvFail.setText("数据未获取");
                    break;
                case GlobalContanstant.MYSENDSUCCESS:


                    final List<ForMyDis> details = (List<ForMyDis>) msg.obj;

                    if (details.size() != 0) {
                        MyReportAdapter adapter = new MyReportAdapter(details, imageUrlLists, audioUrls);

                        pbMysend.setVisibility(View.GONE);
                        lvMysend.setAdapter(adapter);

                        lvMysend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(MySendActivity.this, MySendDetailActivity.class);
                                intent.putExtra("detail", details.get(position));
                                intent.putExtra("audioUrl", audioUrls.get(position));
                                intent.putExtra("imageUrlReport", (Serializable) imageUrlLists.get(position));
                                startActivity(intent);
                            }
                        });
                    } else {
                        tvFail.setVisibility(View.VISIBLE);
                        tvFail.setText("下派数据未获取");
                        pbMysend.setVisibility(View.GONE);
                        ToastUtil.shortToast(getApplicationContext(), "下派数据未获取");
                    }


                    break;
            }
        }
    };
    private List<List<ImageUrl>> imageUrlLists = new ArrayList<>();
    private List<AudioUrl> audioUrls = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysend);
        ButterKnife.bind(this);

        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        initAcitionbar();
        initData();

    }

    private void initData() {
        pbMysend.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                try {
                    String data = getData();
                    if (data != null) {
                        List<ForMyDis> details = JsonUtil.jsonToBean(data, new TypeToken<List<ForMyDis>>() {
                        }.getType());

                        for (ForMyDis forMyDis : details) {
                            String taskNumber = forMyDis.getTaskNumber();
                            String json = null;


                            json = MyApplication.getAllImagUrl(taskNumber, GlobalContanstant.GETREVIEW);

                            if (json != null) {

                                List<ImageUrl> imageUrlList = new Gson().fromJson(json, new TypeToken<List<ImageUrl>>() {
                                }.getType());

                                imageUrlLists.add(imageUrlList);
                            }


                            String audioUrljson = RoadActivity.getAudio(taskNumber);
                            if (audioUrljson != null) {
                                AudioUrl audioUrl = JsonUtil.jsonToBean(audioUrljson, AudioUrl.class);
                                audioUrls.add(audioUrl);
                            }

                        }
                        Message message = Message.obtain();
                        message.obj = details;
                        message.what = GlobalContanstant.MYSENDSUCCESS;
                        handler.sendMessage(message);


                    }
                }catch (Exception e){
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.MYSENDFAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();


    }

    private String getData() throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getAllSendByPersonID);
        soapObject.addProperty("personid", personId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getALlDealByPersonID_SOAP_ACTION, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;


    }


    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("我的下派");
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}
