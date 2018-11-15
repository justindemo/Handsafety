package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.SupMeetingShowAdapter;
import com.xytsz.xytaj.bean.SupMeeting;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/5/9.
 * <p>
 * <p>
 * 列表
 */
public class SupMeetingListActivity extends AppCompatActivity {

    @Bind(R.id.supmeeting_rv)
    RecyclerView supmeetingRv;
    @Bind(R.id.supmeeting_progressbar)
    LinearLayout supmeetingProgressbar;
    @Bind(R.id.meeting_refersh)
    SmartRefreshLayout meetingRefersh;
    private int tag;
    private int personId;
    private String title;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case GlobalContanstant.MYSENDSUCCESS:
                    supmeetingProgressbar.setVisibility(View.GONE);
                    String json = (String) msg.obj;
                    if (json != null ) {
                        if ( !json.equals("[]")) {
                            supMeetings = JsonUtil.jsonToBean(json, new TypeToken<List<SupMeeting>>() {
                            }.getType());
                            if (pageIndex == 1){
                                allDatas.clear();
                                allDatas.addAll(supMeetings);
                                supMeetingShowAdapter = new SupMeetingShowAdapter(allDatas);
                                supmeetingRv.setAdapter(supMeetingShowAdapter);
                            }else {
                                allDatas.addAll(supMeetings);
                                supMeetingShowAdapter.addData(supMeetings);
                            }


                            supMeetingShowAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    switch (tag) {
                                        //传递哪一场培训，
                                        //传递环节
                                        //跳转到指定界面
                                        case 0:
                                            intent2show(position, allDatas, SupMeetingContentActivity.class, false);

                                            break;
                                        case 1:
//                                             签到
                                            intent2show(position, allDatas, MeetingSignActivity.class, true);

                                            break;
                                        case 2:
//                                            培训照片
                                            intent2show(position, allDatas, TrainPhotoActivity.class, true);
                                            break;

                                    }
                                }


                            });



                        }else {
                            islastPage = true;
                            if (pageIndex == 1){
                                ToastUtil.shortToast(getApplicationContext(), "当前没有会议");
                            }
                        }
                    }

                    break;
                case GlobalContanstant.FAIL:
                    supmeetingProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), "数据未获取");
                    break;
            }
        }
    };
    private SupMeetingShowAdapter supMeetingShowAdapter;


    private void intent2show(int position, List<SupMeeting> supMeetings, Class<?> activity, boolean b) {

        Intent intent = new Intent(SupMeetingListActivity.this, activity);
        if (b) {
            intent.putExtra("tag", "meetingsign");
            //传递哪一场培训
            intent.putExtra("trainId", supMeetings.get(position).getMeetId());
        } else {
            intent.putExtra("url", supMeetings.get(position).getUrl());
        }

        startActivity(intent);
    }

    private List<SupMeeting> supMeetings;
    private List<SupMeeting> allDatas = new ArrayList<>();
    private int pageIndex;
    private int pageSize = 10;
    private boolean islastPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supmeetinglist);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            tag = getIntent().getIntExtra("tag", -1);
        }

        switch (tag) {
            case 0:
                title = "会议内容";
                break;
            case 1:
                title = "会议签到";
                break;
            case 2:
                title = "会议照片";
                break;

        }

        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        initactionbar(title);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        supmeetingRv.setLayoutManager(manager);
        pageIndex = 1;

        initData();
        meetingRefersh.setOnLoadMoreListener(new OnLoadMoreListener() {
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

        meetingRefersh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pageIndex = 1;
                refreshLayout.finishRefresh(2000);
                initData();
            }
        });

    }

    private void initactionbar(String title) {
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

    private List<HeaderProperty> headerList = new ArrayList<>();

    private void initData() {
        headerList.clear();
        HeaderProperty headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie,
                SpUtils.getString(getApplicationContext(), GlobalContanstant.CookieHeader));

        headerList.add(headerPropertyObj);
        supmeetingProgressbar.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                try {
                    String data = getData();
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

    private String getData() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getsupMeetingList);
        soapObject.addProperty("personId", personId);
        soapObject.addProperty("pageIndex", pageIndex);
        soapObject.addProperty("pageSize", pageSize);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope, headerList);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();


        return result;
    }
}
