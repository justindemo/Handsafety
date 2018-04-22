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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.MyApplication;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.CheckRoadAdapter;
import com.xytsz.xytaj.adapter.DealAdapter;
import com.xytsz.xytaj.bean.AudioUrl;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.bean.Review;
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


/**
 * Created by admin on 2017/2/22.
 * <p>
 * Item界面
 */
public class CheckRoadActivity extends AppCompatActivity {

    private static final int ROADDATA = 50001;
    private ListView mlv;
    private int position;
    private List<Review> reviewRoad;

    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case GlobalContanstant.CHECKPASS:
                    mProgressBar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), "已验收完毕");
                    break;

                case GlobalContanstant.CHECKFAIL:
                    mProgressBar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), "未获取数据,请稍后");
                    break;

                case ROADDATA:
                    reviewRoad = (List<Review>) msg.obj;
                    mlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(parent.getContext(), CheckDetailActivity.class);
                            intent.putExtra("position", position);
                            intent.putExtra("audioUrls", (Serializable) audioUrls);
                            intent.putExtra("reviewRoad", reviewRoad.get(position));
                            intent.putExtra("imageUrlReport", (Serializable) imageUrlLists);
                            intent.putExtra("imageUrlPost", (Serializable) imageUrlPostLists);
                            startActivityForResult(intent, 40001);

                        }
                    });


                    break;
            }
        }
    };
    private CheckRoadAdapter adapter;
    private List<List<ImageUrl>> imageUrlPostLists = new ArrayList<>();
    private List<List<ImageUrl>> imageUrlLists = new ArrayList<>();
    private List<AudioUrl> audioUrls = new ArrayList<>();
    private List<Review> list;
    private ProgressBar mProgressBar;
    private int personid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            position = getIntent().getIntExtra("position", 0);
        }

        setContentView(R.layout.activity_checkroad);
        initAcitionbar();
        mlv = (ListView) findViewById(R.id.lv_checkroad);
        mProgressBar = (ProgressBar) findViewById(R.id.review_progressbar);
        personid = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        initData();


    }

    private void initData() {

        mProgressBar.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {

                try {

                    String checkData = PostRoadActivity.getDealData(NetUrl.CHECKMETHODNAME,personid);

                    if (checkData != null) {

                        list = JsonUtil.jsonToBean(checkData, new TypeToken<List<Review>>() {
                        }.getType());


                        if (list.size() == 0) {

                            Message message = Message.obtain();
                            message.what = GlobalContanstant.CHECKPASS;
                            handler.sendMessage(message);
                        } else {

                            //遍历list
                            for (Review detail : list) {
                                String taskNumber = detail.getDeciceCheckNum();
                                /**
                                 * 获取到图片的URl
                                 */

                                String json = MyApplication.getAllImagUrl(taskNumber, GlobalContanstant.GETREVIEW);
                                String postJson = getPostImagUrl(taskNumber);

                                if (json != null) {
                                    //String list = new JSONObject(json).getJSONArray("").toString();
                                    List<ImageUrl> imageUrlList = new Gson().fromJson(json, new TypeToken<List<ImageUrl>>() {
                                    }.getType());

                                    imageUrlLists.add(imageUrlList);
                                }


                                if (postJson != null) {
                                    List<ImageUrl> imageUrlPostList = new Gson().fromJson(postJson, new TypeToken<List<ImageUrl>>() {
                                    }.getType());

                                    imageUrlPostLists.add(imageUrlPostList);
                                }


                                String audioUrljson = RoadActivity.getAudio(taskNumber);
                                if (audioUrljson != null) {
                                    AudioUrl audioUrl = JsonUtil.jsonToBean(audioUrljson, AudioUrl.class);
                                    audioUrls.add(audioUrl);
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter = new CheckRoadAdapter(list, imageUrlLists, imageUrlPostLists);
                                    mlv.setAdapter(adapter);
                                    mProgressBar.setVisibility(View.GONE);

                                }
                            });


                            Message message = Message.obtain();
                            message.obj = list;
                            message.what = ROADDATA;
                            handler.sendMessage(message);

                        }
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

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getAllImageURLmethodName);
        soapObject.addProperty("DeciceCheckNum", taskNumber);
        soapObject.addProperty("PhaseId", GlobalContanstant.GETPOST);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(null, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
            case GlobalContanstant.CHECKPASS:
                int passposition = data.getIntExtra("passposition", -1);
                list.remove(passposition);
                imageUrlLists.remove(passposition);
                imageUrlPostLists.remove(passposition);
                adapter.notifyDataSetChanged();

                break;

            case GlobalContanstant.CHECKFAIL:
                int failposition = data.getIntExtra("failposition", -1);
                list.remove(failposition);
                imageUrlLists.remove(failposition);
                imageUrlPostLists.remove(failposition);
                adapter.notifyDataSetChanged();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.check);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
