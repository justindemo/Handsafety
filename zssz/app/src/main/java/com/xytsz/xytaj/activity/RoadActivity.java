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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xytsz.xytaj.MyApplication;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.RoadAdapter;
import com.xytsz.xytaj.bean.AudioUrl;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.bean.Review;
import com.xytsz.xytaj.global.GlobalContanstant;

import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/1/11.
 * 审核页面的二级道路页面
 */
public class RoadActivity extends AppCompatActivity {

    private static final int NOONE = 100003;

    private ListView mLv;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NOONE:
                    mtvFail.setText("已核查完毕");
                    mtvFail.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    break;
                case GlobalContanstant.SENDFAIL:
                    mProgressBar.setVisibility(View.GONE);
                    mtvFail.setVisibility(View.VISIBLE);
                    mtvFail.setText("未数据获取,请稍后");
                    break;

            }
        }
    };
    private List<List<ImageUrl>> imageUrlLists = new ArrayList<>();
    private List<AudioUrl> audioUrls = new ArrayList<>();
    private int personId;

    private ProgressBar mProgressBar;
    private RoadAdapter roadAdapter;
    private List<Review> reviews;
    private List<Review> allDatas = new ArrayList<>();
    private String serviceData;
    private int role;
    private TextView mtvFail;
    private SmartRefreshLayout roadRefersh;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road);

        initAcitionbar();
        role = SpUtils.getInt(getApplicationContext(), GlobalContanstant.ROLE);
        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        initView();
        pageIndex = 1;
        initData();


        roadRefersh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (!islastPage) {
                    refreshLayout.finishLoadMore(2000);
                    ++pageIndex;

                    initData();
                } else {
                    refreshLayout.finishLoadMore();
                    ToastUtil.shortToast(getApplicationContext(), "没有更多了");
                }


            }
        });

        roadRefersh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pageIndex = 1;
                refreshLayout.finishRefresh(2000);
                initData();
            }
        });


        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RoadActivity.this, SendRoadDetailActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("tag", GlobalContanstant.REVIEW);
                intent.putExtra("detail", allDatas.get(position));
                intent.putExtra("audioUrl", audioUrls.get(position));
                intent.putExtra("imageUrls", (Serializable) imageUrlLists.get(position));
                startActivityForResult(intent, 300);
            }
        });

    }

    private void initView() {
        mLv = (ListView) findViewById(R.id.road_lv);
        mProgressBar = (ProgressBar) findViewById(R.id.review_progressbar);
        mtvFail = (TextView) findViewById(R.id.tv_road_fail);

        roadRefersh = (SmartRefreshLayout) findViewById(R.id.road_refersh);

    }

    //从服务器获取当前道路的信息  所有
    private List<HeaderProperty> headerList = new ArrayList<>();

    private void initData() {
        headerList.clear();
        HeaderProperty headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie,
                SpUtils.getString(getApplicationContext(), GlobalContanstant.CookieHeader));

        headerList.add(headerPropertyObj);
        mProgressBar.setVisibility(View.VISIBLE);
        //获取的数据当作 list传入构造中   ***应该传的是bean
        new Thread() {
            @Override
            public void run() {

                try {
                    serviceData = getServiceData(NetUrl.reviewmethodName, personId);
                    if (serviceData != null) {
                        if (serviceData.equals("[]")) {
                            islastPage = true;
                            if (pageIndex == 1) {
                                Message message = Message.obtain();
                                message.what = NOONE;
                                handler.sendMessage(message);
                            }
                        } else {

                            reviews = JsonUtil.jsonToBean(serviceData, new TypeToken<List<Review>>() {
                            }.getType());

                            if (reviews.size() != 0) {

                                if (pageIndex == 1){
                                    audioUrls.clear();
                                    imageUrlLists.clear();

                                }
                                //遍历list
                                for (Review detail : reviews) {

                                    String taskNumber = detail.getDeciceCheckNum();
                                    /**
                                     * 获取到图片的URl
                                     */
                                    String json = getAllImagUrl(taskNumber, GlobalContanstant.GETREVIEW, headerList);
                                    if (json != null) {
                                        //String list = new JSONObject(json).getJSONArray("").toString();
                                        List<ImageUrl> imageUrlList = new Gson().fromJson(json, new TypeToken<List<ImageUrl>>() {
                                        }.getType());

                                        imageUrlLists.add(imageUrlList);
                                    }


                                    String audioUrljson = getAudio(taskNumber, headerList);

                                    if (audioUrljson != null) {
                                        AudioUrl audioUrl = JsonUtil.jsonToBean(audioUrljson, AudioUrl.class);
                                        audioUrls.add(audioUrl);
                                    }
                                }

                                if (pageIndex == 1) {
                                    allDatas.clear();
                                    allDatas.addAll(reviews);
                                    roadAdapter = new RoadAdapter(allDatas, imageUrlLists, audioUrls);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mLv.setAdapter(roadAdapter);
                                            mProgressBar.setVisibility(View.GONE);

                                        }
                                    });

                                }else {
                                    allDatas.addAll(reviews);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            roadAdapter.notifyDataSetChanged();
                                            mProgressBar.setVisibility(View.GONE);

                                        }
                                    });

                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.SENDFAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();


    }

    public static String getAllImagUrl(String taskNumber, int phaseIndication, List<HeaderProperty> headerList) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getAllImageURLmethodName);
        soapObject.addProperty("DeciceCheckNum", taskNumber);
        soapObject.addProperty("PhaseId", phaseIndication);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(NetUrl.getAllImageURL_SOAP_ACTION, envelope, headerList);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }

    private int pageIndex;
    private int pageSize = 10;
    private boolean islastPage;

    public String getServiceData(String methodname, int personId) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, methodname);
        soapObject.addProperty("personId", personId);
        soapObject.addProperty("pageIndex", pageIndex);
        soapObject.addProperty("pageSize", pageSize);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getTasklist_SOAP_ACTION, envelope, headerList);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String json = object.getProperty(0).toString();

        return json;
    }

    public static String getAudio(String taskNumber, List<HeaderProperty> headerList) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getAudioMethodName);
        soapObject.addProperty("DeciceCheckNum", taskNumber);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(NetUrl.getAudio_SOAP_ACTION, envelope, headerList);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;

    }


    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.review);
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
        if (requestCode == 300) {
            if (resultCode == 302) {
                int backedPosition = data.getIntExtra("position", -1);
                if (reviews != null) {
                    reviews.remove(backedPosition);
                    imageUrlLists.remove(backedPosition);
                    audioUrls.remove(backedPosition);
                    roadAdapter.notifyDataSetChanged();
                    if (reviews.size() == 0) {
                        //显示 tv  检查任务完成
                        mtvFail.setText("核查任务完成");
                        mtvFail.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
}