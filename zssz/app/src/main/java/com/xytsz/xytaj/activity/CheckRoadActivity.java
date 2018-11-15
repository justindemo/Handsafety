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
import com.xytsz.xytaj.adapter.CheckRoadAdapter;

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
                    mtvFail.setText("已验收完毕");
                    mtvFail.setVisibility(View.VISIBLE);

                    break;

                case GlobalContanstant.CHECKFAIL:
                    mProgressBar.setVisibility(View.GONE);
                    mtvFail.setText("数据未获取");
                    mtvFail.setVisibility(View.VISIBLE);
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
    private HeaderProperty headerPropertyObj;
    private TextView mtvFail;
    private SmartRefreshLayout checkroadRefersh;


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
        mtvFail = (TextView) findViewById(R.id.tv_check_fail);
        checkroadRefersh = (SmartRefreshLayout) findViewById(R.id.checkroad_refersh);
        personid = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        pageIndex = 1;
        initData();
        checkroadRefersh.setOnLoadMoreListener(new OnLoadMoreListener() {
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

        checkroadRefersh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pageIndex = 1;
                refreshLayout.finishRefresh(2000);
                initData();
            }
        });

        mlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), CheckDetailActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("audioUrls", (Serializable) audioUrls);
                intent.putExtra("reviewRoad", allDatas.get(position));
                intent.putExtra("imageUrlReport", (Serializable) imageUrlLists);
                intent.putExtra("imageUrlPost", (Serializable) imageUrlPostLists);
                startActivityForResult(intent, 40001);

            }
        });


    }

    private int pageIndex;
    private int pageSize = 10;
    private boolean islastPage;
    private List<Review> allDatas = new ArrayList<>();

    private void initData() {
        headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie, SpUtils.getString(getApplicationContext(), GlobalContanstant.CookieHeader));
        headerList.clear();
        headerList.add(headerPropertyObj);
        mProgressBar.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {

                try {

                    String checkData = getDealData(NetUrl.CHECKMETHODNAME, personid);

                    if (checkData != null) {
                        if (checkData.equals("[]")) {
                            islastPage = true;
                            if (pageIndex == 1) {
                                Message message = Message.obtain();
                                message.what = GlobalContanstant.CHECKPASS;
                                handler.sendMessage(message);
                            }
                        } else {

                            list = JsonUtil.jsonToBean(checkData, new TypeToken<List<Review>>() {
                            }.getType());

                            if (list.size() != 0) {
                                if (pageIndex == 1){
                                    audioUrls.clear();
                                    imageUrlLists.clear();
                                    imageUrlPostLists.clear();
                                }

                                //遍历list
                                for (Review detail : list) {
                                    String taskNumber = detail.getDeciceCheckNum();
                                    /**
                                     * 获取到图片的URl
                                     */

                                    String json = RoadActivity.getAllImagUrl(taskNumber, GlobalContanstant.GETREVIEW, headerList);
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


                                    String audioUrljson = RoadActivity.getAudio(taskNumber, headerList);
                                    if (audioUrljson != null) {
                                        AudioUrl audioUrl = JsonUtil.jsonToBean(audioUrljson, AudioUrl.class);
                                        audioUrls.add(audioUrl);
                                    }
                                }
                                if (pageIndex == 1) {
                                    allDatas.clear();
                                    allDatas.addAll(list);
                                    adapter = new CheckRoadAdapter(allDatas, imageUrlLists, imageUrlPostLists);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            mlv.setAdapter(adapter);
                                            mProgressBar.setVisibility(View.GONE);
                                        }
                                    });

                                } else {
                                    allDatas.addAll(list);
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
                    message.what = GlobalContanstant.CHECKFAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();


    }

    private List<HeaderProperty> headerList = new ArrayList<>();


    public String getDealData(String methodName, int personID) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, methodName);
        soapObject.addProperty("personId", personID);
        soapObject.addProperty("pageIndex", pageIndex);
        soapObject.addProperty("pageSize", pageSize);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        //添加cookie

        httpTransportSE.call(NetUrl.getManagementList_SOAP_ACTION, envelope, headerList);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String json = object.getProperty(0).toString();

        return json;
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

        httpTransportSE.call(null, envelope, headerList);
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

                if (list.size() == 0) {
                    mtvFail.setText("已验收完毕");
                    mtvFail.setVisibility(View.VISIBLE);
                }
                break;

            case GlobalContanstant.CHECKFAIL:
                int failposition = data.getIntExtra("failposition", -1);
                list.remove(failposition);
                imageUrlLists.remove(failposition);
                imageUrlPostLists.remove(failposition);
                adapter.notifyDataSetChanged();
                if (list.size() == 0) {
                    mtvFail.setText("已验收完毕");
                    mtvFail.setVisibility(View.VISIBLE);
                }
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
