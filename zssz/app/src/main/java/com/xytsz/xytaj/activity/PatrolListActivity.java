package com.xytsz.xytaj.activity;


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
import android.widget.ProgressBar;

import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.PatrolListAdapter;
import com.xytsz.xytaj.bean.PatrolListBean;
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
 * Created by admin on 2018/3/26.
 * <p>
 * 排查list
 */
public class PatrolListActivity extends AppCompatActivity {

    @Bind(R.id.patrolrecycle)
    RecyclerView patrolrecycle;
    @Bind(R.id.patrol_progressbar)
    LinearLayout patrolProgressbar;
    @Bind(R.id.patrol_refersh)
    SmartRefreshLayout patrolRefersh;
    private int personId;
    private List<PatrolListBean> patrolListBeens;
    private List<PatrolListBean> allListBeens = new ArrayList<>();
    private int position;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContanstant.PATROLLISTSUCCESS:
                    patrolProgressbar.setVisibility(View.GONE);
                    String jsonData = (String) msg.obj;
                    if (jsonData != null) {
                        if (!jsonData.equals("[]")) {
                            patrolListBeens = JsonUtil.jsonToBean(jsonData, new TypeToken<List<PatrolListBean>>() {
                            }.getType());

                            if (pageIndex == 1) {
                                if (patrolListBeens != null && patrolListBeens.size() != 0) {
                                    //展示
                                    allListBeens.clear();
                                    allListBeens.addAll(patrolListBeens);
                                    patrolListAdapter = new PatrolListAdapter(allListBeens, false);
                                    patrolrecycle.setAdapter(patrolListAdapter);

                                } else {
                                    ToastUtil.shortToast(getApplicationContext(), "今天排查工作完成");
                                }

                            } else {
                                patrolListAdapter.addData(patrolListBeens);
                            }
                        } else {
                            islastPage = true;
                            if (pageIndex == 1) {
                                ToastUtil.shortToast(getApplicationContext(), "今天排查工作完成");
                            }

                        }
                    } else {
                        ToastUtil.shortToast(getApplicationContext(), "获取数据异常");
                    }
                    break;
                case GlobalContanstant.PATROLLISTFAIL:
                    patrolProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), "获取数据异常");
                    break;
            }
        }
    };
    private PatrolListAdapter patrolListAdapter;
    private int pageIndex;
    private int pageSize = 8;
    private boolean islastPage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrollist);
        ButterKnife.bind(this);
        initActionBar();
        initData();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.reprote_task);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private List<HeaderProperty> headerList = new ArrayList<>();

    /**
     * 初始化数据
     */
    private void initData() {
        //获取人员Id
        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        headerList.clear();
        HeaderProperty headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie,
                SpUtils.getString(getApplicationContext(), GlobalContanstant.CookieHeader));

        headerList.add(headerPropertyObj);
        pageIndex = 1;
        LinearLayoutManager manager = new LinearLayoutManager(PatrolListActivity.this);
        patrolrecycle.setLayoutManager(manager);
        getData();

        patrolRefersh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (!islastPage) {
                    refreshLayout.finishLoadMore(2000);
                    ++pageIndex;

                    getData();
                } else {
                    refreshLayout.finishLoadMore();
                    ToastUtil.shortToast(getApplicationContext(), "没有更多了");
                }


            }
        });

        patrolRefersh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pageIndex = 1;
                refreshLayout.finishRefresh(2000);
                getData();
            }
        });

    }

    /**
     * 获取显示的数据
     */
    private void getData() {
        patrolProgressbar.setVisibility(View.GONE);
        new Thread() {
            @Override
            public void run() {
                try {
                    String jsonData = downData(personId);
                    if (jsonData != null) {
                        Message message = Message.obtain();
                        message.what = GlobalContanstant.PATROLLISTSUCCESS;
                        message.obj = jsonData;
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.PATROLLISTFAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    private String downData(int personId) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getTaskByPersonID);
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

