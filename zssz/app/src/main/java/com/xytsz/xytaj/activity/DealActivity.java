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
import com.xytsz.xytaj.adapter.DealAdapter;
import com.xytsz.xytaj.bean.AudioUrl;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.bean.Review;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.HeaderProperty;
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
                    mtvfail.setText("数据未获取");
                    mtvfail.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    private ProgressBar mProgressBar;
    private List<Review> reviews;
    private List<Review> allDatas = new ArrayList<>();
    private TextView mtvfail;
    private String nodata;
    private DealAdapter adapter;
    private List<List<ImageUrl>> imageUrlLists = new ArrayList<>();
    private List<AudioUrl> audioUrls = new ArrayList<>();
    private int pageIndex;
    private int pageSize = 10;
    private boolean islastPage;
    private SmartRefreshLayout dealRefersh;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);

        personID = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        nodata = getString(R.string.deal_nodata);
        initAcitionbar();
        initView();
        pageIndex = 1;
        initData();

        dealRefersh.setOnLoadMoreListener(new OnLoadMoreListener() {
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

        dealRefersh.setOnRefreshListener(new OnRefreshListener() {
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
                Intent intent = new Intent(DealActivity.this, SendRoadDetailActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("tag", GlobalContanstant.NOTIFY);
                intent.putExtra("detail", allDatas.get(position));
                intent.putExtra("audioUrl", audioUrls.get(position));
                intent.putExtra("imageUrls", (Serializable) imageUrlLists.get(position));
                startActivityForResult(intent, 400);
            }
        });
    }

    private void initView() {
        mLv = (ListView) findViewById(R.id.Lv_deal);
        mProgressBar = (ProgressBar) findViewById(R.id.review_progressbar);
        mtvfail = (TextView) findViewById(R.id.tv_deal_fail);

        dealRefersh = (SmartRefreshLayout) findViewById(R.id.deal_refersh);

    }

    private void initData() {
        headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie,
                SpUtils.getString(getApplicationContext(),GlobalContanstant.CookieHeader));
        headerList.clear();
        headerList.add(headerPropertyObj);
        mProgressBar.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                try {

                    String dealData = getServiceData(NetUrl.getdealtask,personID);
                    if (dealData != null) {
                        if (dealData.equals("[]")) {
                            islastPage = true;
                            if (pageIndex == 1){
                                Message message = Message.obtain();
                                message.what = NODEAL;
                                handler.sendMessage(message);
                            }

                        } else {
                        reviews = JsonUtil.jsonToBean(dealData, new TypeToken<List<Review>>() {
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
                                String json = RoadActivity.getAllImagUrl(taskNumber, GlobalContanstant.GETREVIEW, headerList);
                                if (json != null) {

                                    //String list = new JSONObject(json).getJSONArray("").toString();
                                    List<ImageUrl> imageUrlList = new Gson().fromJson(json, new TypeToken<List<ImageUrl>>() {
                                    }.getType());

                                    imageUrlLists.add(imageUrlList);
                                }


                                String audioUrljson = RoadActivity.getAudio(taskNumber, headerList);

                                if (audioUrljson != null) {
                                    AudioUrl audioUrl = JsonUtil.jsonToBean(audioUrljson, AudioUrl.class);
                                    audioUrls.add(audioUrl);
                                }


                            }

                            if (pageIndex == 1) {

                                allDatas.clear();
                                allDatas.addAll(reviews);
                                adapter = new DealAdapter(allDatas, imageUrlLists, audioUrls);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mLv.setAdapter(adapter);
                                        mProgressBar.setVisibility(View.GONE);

                                    }
                                });
                            } else {
                                allDatas.addAll(reviews);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                        mProgressBar.setVisibility(View.GONE);

                                    }
                                });
                            }
                        }

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
    public List<HeaderProperty> headerList = new ArrayList<>();
    public HeaderProperty headerPropertyObj;

    public  String getServiceData(String method,int personID) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace,method);
        soapObject.addProperty("personId",personID);
        soapObject.addProperty("pageIndex", pageIndex);
        soapObject.addProperty("pageSize", pageSize);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getTasklist_SOAP_ACTION,envelope,headerList);

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
                if (reviews.size() == 0){
                    mtvfail.setText(nodata);
                    mtvfail.setVisibility(View.VISIBLE);
                }
            }
        }
    }



}
