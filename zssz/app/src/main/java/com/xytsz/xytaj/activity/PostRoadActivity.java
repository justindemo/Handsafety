package com.xytsz.xytaj.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.xytsz.xytaj.adapter.PostRoadAdapter;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/2/22.
 * 报验二级菜单
 */
public class PostRoadActivity extends AppCompatActivity {

    private ListView mlv;
    private int personID;
    private List<List<ImageUrl>> imageUrlLists = new ArrayList<>();
    private ProgressBar mProgressBar;
    private PostRoadAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContanstant.SENDFAIL:
                    mProgressBar.setVisibility(View.GONE);
                    mtvfail.setVisibility(View.VISIBLE);
                    mtvfail.setText("数据未获取");
                    break;
                case GlobalContanstant.NODATA:
                    mProgressBar.setVisibility(View.GONE);
                    mtvfail.setVisibility(View.VISIBLE);
                    mtvfail.setText("已处置完毕");
                    break;

            }
        }
    };
    private List<Review> list;
    private List<Review> allDatas = new ArrayList<>();
    public  HeaderProperty headerPropertyObj;
    private TextView mtvfail;
    public int pageIndex;
    public int pageSize = 10;
    private boolean islastPage;
    private SmartRefreshLayout postroadRefersh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postroad);

        initAcitionbar();
        mlv = (ListView) findViewById(R.id.lv_postRoad);
        mProgressBar = (ProgressBar) findViewById(R.id.review_progressbar);
        mtvfail = (TextView) findViewById(R.id.tv_post_fail);

        //sp 获取当前登陆人的ID
        personID = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);

        postroadRefersh = (SmartRefreshLayout) findViewById(R.id.postroad_refersh);
        pageIndex = 1;
        initData();
        postroadRefersh.setOnLoadMoreListener(new OnLoadMoreListener() {
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

        postroadRefersh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pageIndex = 1;
                refreshLayout.finishRefresh(2000);
                initData();
            }
        });

    }

    private List<AudioUrl> audioUrls = new ArrayList<>();

    private void initData() {
        headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie, SpUtils.getString(getApplicationContext(),GlobalContanstant.CookieHeader));
        headerList.clear();
        headerList.add(headerPropertyObj);
        mProgressBar.setVisibility(View.VISIBLE);

        new Thread() {
            @Override
            public void run() {

                try {
                    String sendData = getDealData(NetUrl.getManagementList, personID);

                    if (sendData != null) {
                        if (sendData.equals("[]")) {
                            islastPage = true;
                            if (pageIndex == 1) {
                                Message message = Message.obtain();
                                message.what = GlobalContanstant.NODATA;
                                handler.sendMessage(message);
                            }
                        } else {
                            list = JsonUtil.jsonToBean(sendData, new TypeToken<List<Review>>() {
                            }.getType());


                            if (list.size() != 0) {

                                if (pageIndex == 1){
                                    audioUrls.clear();
                                    imageUrlLists.clear();

                                }
                                //遍历list
                                for (Review detail : list) {
                                    String taskNumber = detail.getDeciceCheckNum();
                                    /**
                                     * 获取到图片的URl
                                     */
                                    String json = RoadActivity.getAllImagUrl(taskNumber, GlobalContanstant.GETREVIEW, headerList);

                                    if (json != null) {

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
                                    allDatas.addAll(list);
                                    adapter = new PostRoadAdapter(allDatas, imageUrlLists, audioUrls);
                                    //主线程更新UI
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
                    message.what = GlobalContanstant.SENDFAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();


    }

    public List<HeaderProperty> headerList = new ArrayList<>();


    public  String getDealData(String methodName, int personID) throws Exception {

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
            actionBar.setTitle(R.string.post);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
