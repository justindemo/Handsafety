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
import com.xytsz.xytaj.adapter.DealAdapter;
import com.xytsz.xytaj.bean.AudioUrl;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.bean.Review;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/2/15.
 * 处置界面
 * 处置界面是根据下派 来获取的需要处置的信息
 */
public class DealActivity extends AppCompatActivity {

    private static final int NODEAL = 3003;
    private static final int FAIL = 500;
    private ListView mLv;
    private int personID;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NODEAL:
                    mProgressBar.setVisibility(View.GONE);
                    mtvfail.setText(nodata);
                    mtvfail.setVisibility(View.VISIBLE);
                    break;
                case FAIL:
                    mProgressBar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), "未获取数据,请稍后");
                    break;
            }
        }
    };
    private ProgressBar mProgressBar;
    private List<Review> reviews;
    private TextView mtvfail;
    private String nodata;
    private DealAdapter adapter;
    private List<List<ImageUrl>> imageUrlLists = new ArrayList<>();
    private List<AudioUrl> audioUrls = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);

        personID = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        nodata = getString(R.string.deal_nodata);
        initAcitionbar();
        initView();

        initData();
    }

    private void initView() {
        mLv = (ListView) findViewById(R.id.Lv_deal);
        mProgressBar = (ProgressBar) findViewById(R.id.review_progressbar);
        mtvfail = (TextView) findViewById(R.id.tv_deal_fail);
    }

    private void initData() {

        mProgressBar.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                try {

                    String dealData = getServiceData(NetUrl.getdealtask,personID);
                    if (dealData != null) {

                        reviews = JsonUtil.jsonToBean(dealData, new TypeToken<List<Review>>() {
                        }.getType());

                        if (reviews.size() == 0) {

                            Message message = Message.obtain();
                            message.what = NODEAL;
                            handler.sendMessage(message);

                        } else {
                            audioUrls.clear();
                            //遍历list
                            for (Review detail : reviews) {

                                String taskNumber = detail.getDeciceCheckNum();
                                /**
                                 * 获取到图片的URl
                                 */
                                String json = MyApplication.getAllImagUrl(taskNumber, GlobalContanstant.GETREVIEW);
                                if (json != null) {

                                    //String list = new JSONObject(json).getJSONArray("").toString();
                                    List<ImageUrl> imageUrlList = new Gson().fromJson(json, new TypeToken<List<ImageUrl>>() {
                                    }.getType());

                                    imageUrlLists.add(imageUrlList);
                                }


                                String audioUrljson = RoadActivity.getAudio(taskNumber);

                                if (audioUrljson != null){
                                    AudioUrl audioUrl = JsonUtil.jsonToBean(audioUrljson, AudioUrl.class);
                                    audioUrls.add(audioUrl);
                                }



                            }

                            adapter = new DealAdapter(reviews, imageUrlLists, audioUrls);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mLv.setAdapter(adapter);
                                    mProgressBar.setVisibility(View.GONE);
                                    mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent intent = new Intent(DealActivity.this,SendRoadDetailActivity.class);
                                            intent.putExtra("position",position);
                                            intent.putExtra("tag",GlobalContanstant.NOTIFY);
                                            intent.putExtra("detail", reviews.get(position));
                                            intent.putExtra("audioUrl", audioUrls.get(position));
                                            intent.putExtra("imageUrls", (Serializable) imageUrlLists.get(position));
                                            startActivityForResult(intent,400);
                                        }
                                    });
                                }
                            });


                        }
                    }
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = FAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();



    }

    public static String getServiceData(String method,int personID) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace,method);
        soapObject.addProperty("personId",personID);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getTasklist_SOAP_ACTION,envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String json = object.getProperty(0).toString();

        return json;
    }


    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.deal);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 400 ){
            if (resultCode == 301 ){
                int backedPosition = data.getIntExtra("position", -1);
                reviews.remove(backedPosition);
                imageUrlLists.remove(backedPosition);
                audioUrls.remove(backedPosition);
                adapter.notifyDataSetChanged();
            }
        }
    }



}
