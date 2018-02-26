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
 * Created by admin on 2017/5/26.
 * 自己处置的界面
 */
public class MyDealedActivity extends AppCompatActivity {

    private static final int DEALED = 100002;
    @Bind(R.id.lv_reprote)
    ListView lvReprote;
    @Bind(R.id.myreport_progressbar)
    ProgressBar myreportProgressbar;
    @Bind(R.id.tv_fail)
    TextView tvFail;
    private int personId;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContanstant.CHECKFAIL:
                    tvFail.setVisibility(View.VISIBLE);
                    myreportProgressbar.setVisibility(View.GONE);
                    tvFail.setText("未获取数据，请稍后");
                    break;

                case DEALED:

                    final List<ForMyDis> details = (List<ForMyDis>) msg.obj;

                    if (details.size() != 0) {
                        //加载数据
                        MyReportAdapter adapter = new MyReportAdapter(details, imageUrlLists, audioUrls);
                        myreportProgressbar.setVisibility(View.GONE);
                        lvReprote.setAdapter(adapter);

                        lvReprote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //点击的时候把数据传进去

                                Intent intent = new Intent(MyDealedActivity.this, MyDealedDetailActivity.class);
                                intent.putExtra("detail", details.get(position));
                                intent.putExtra("audioUrl", audioUrls.get(position));
                                intent.putExtra("imageUrlreport", (Serializable) imageUrlLists.get(position));
                                intent.putExtra("imageUrlpost", (Serializable) imageUrlPostLists.get(position));

                                startActivity(intent);
                            }
                        });
                    } else {
                        tvFail.setVisibility(View.VISIBLE);
                        tvFail.setText(nodata);
                        myreportProgressbar.setVisibility(View.GONE);
                    }
                    break;

            }
        }
    };
    private List<List<ImageUrl>> imageUrlLists = new ArrayList<>();
    private List<List<ImageUrl>> imageUrlPostLists = new ArrayList<>();
    private String nodata;
    private List<AudioUrl> audioUrls = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreporte);
        ButterKnife.bind(this);
        //拿到当前登陆人的ID;
        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        //取网络数据
        nodata = getString(R.string.mydealed_nodata);
        initAcitionbar();
        initData();

    }


    private void initData() {

        myreportProgressbar.setVisibility(View.VISIBLE);
        new Thread() {
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
                            String post = null;

                            json = MyApplication.getAllImagUrl(taskNumber, GlobalContanstant.GETREVIEW);
                            post = getPostImagUrl(taskNumber);
                            if (json != null) {
                                //String list = new JSONObject(json).getJSONArray("").toString();
                                List<ImageUrl> imageUrlList = new Gson().fromJson(json, new TypeToken<List<ImageUrl>>() {
                                }.getType());

                                imageUrlLists.add(imageUrlList);
                            }


                            if (post != null) {
                                List<ImageUrl> imageUrlPostList = new Gson().fromJson(post, new TypeToken<List<ImageUrl>>() {
                                }.getType());

                                imageUrlPostLists.add(imageUrlPostList);
                            }
                            String audioUrljson = RoadActivity.getAudio(taskNumber);


                            if (audioUrljson != null){
                                AudioUrl audioUrl = JsonUtil.jsonToBean(audioUrljson, AudioUrl.class);
                                audioUrls.add(audioUrl);
                            }

                        }
                        Message message = Message.obtain();
                        message.obj = details;
                        message.what = DEALED;
                        handler.sendMessage(message);

                    }
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.CHECKFAIL;
                    handler.sendMessage(message);


                }
            }
        }.start();


    }


    public String getPostImagUrl(String taskNumber) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getPostImageURLmethodName);
        soapObject.addProperty("TaskNumber", taskNumber);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(NetUrl.getPostImageURL_SOAP_ACTION, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }

    private String getData()throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getALlDealByPersonID);

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
            actionBar.setTitle(R.string.mydealed);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
