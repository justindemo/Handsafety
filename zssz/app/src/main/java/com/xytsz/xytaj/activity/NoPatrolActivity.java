package com.xytsz.xytaj.activity;

import android.content.Context;
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
 * Created by admin on 2018/4/26.
 * <p>
 * 未排查
 */
public class NoPatrolActivity extends AppCompatActivity {

    @Bind(R.id.nocheck_rv)
    RecyclerView nocheckRv;
    @Bind(R.id.nocheck_progressbar)
    LinearLayout nocheckProgressbar;
    @Bind(R.id.nopatrol_refersh)
    SmartRefreshLayout nopatrolRefersh;
    private int personId;
    private List<PatrolListBean> patrolListBeens;
    private int position;
    private boolean isVisiable;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContanstant.PATROLLISTSUCCESS:
                    nocheckProgressbar.setVisibility(View.GONE);
                    String jsonData = (String) msg.obj;
                    if (!jsonData.equals("[]")) {
                        patrolListBeens = JsonUtil.jsonToBean(jsonData, new TypeToken<List<PatrolListBean>>() {
                        }.getType());
                        if (pageIndex == 1) {
                            if (patrolListBeens != null && patrolListBeens.size() != 0) {
                                //展示
                                allDatas.clear();
                                allDatas.addAll(patrolListBeens);
                                patrolListAdapter = new PatrolListAdapter(allDatas, isVisiable);
                                nocheckRv.setAdapter(patrolListAdapter);

                            } else {
                                ToastUtil.shortToast(getApplicationContext(), "今天工作完成");
                            }

                        }else {
                            patrolListAdapter.addData(patrolListBeens);
                        }
                    } else {
                        islastPage = true;
                        if (pageIndex == 1) {
                            ToastUtil.shortToast(getApplicationContext(), "今天工作完成");
                        }
                    }
                    break;
                case GlobalContanstant.PATROLLISTFAIL:
                    nocheckProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), "获取数据异常");
                    break;
            }
        }
    };
    private String title;
    private String tag;
    private String method;
    private PatrolListAdapter patrolListAdapter;
    private int pageIndex ;
    private int pageSize = 10;
    private boolean islastPage;
    private List<PatrolListBean> allDatas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            title = getIntent().getStringExtra("title");
            tag = getIntent().getStringExtra("tag");

        }
        setContentView(R.layout.activity_nocheck);
        ButterKnife.bind(this);
        if (title != null) {
            initActionbar(title);
        }
        if (tag != null) {
            switch (tag) {
                case GlobalContanstant.NOCHECK:
                    method = NetUrl.getnoCheckTaskByPersonID;
                    isVisiable = true;
                    break;
                case GlobalContanstant.NOPATROL:
                    method = NetUrl.getnoPatrolTaskByPersonID;
                    isVisiable = false;
                    break;
            }
        }

        pageIndex = 1;
        LinearLayoutManager manager = new LinearLayoutManager(NoPatrolActivity.this);
        nocheckRv.setLayoutManager(manager);
        initData();

        nopatrolRefersh.setOnLoadMoreListener(new OnLoadMoreListener() {
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

        nopatrolRefersh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pageIndex = 1;
                refreshLayout.finishRefresh(2000);
                initData();
            }
        });


    }

    private List<HeaderProperty> headerList = new ArrayList<>();

    private void initData() {

        headerList.clear();
        HeaderProperty headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie,
                SpUtils.getString(getApplicationContext(), GlobalContanstant.CookieHeader));

        headerList.add(headerPropertyObj);
        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
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

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, method);
        soapObject.addProperty("personId", personId);
        soapObject.addProperty("pageIndex", pageIndex);
        soapObject.addProperty("pageSize", pageSize);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL /*"http://192.168.1.179:10801"*/);
        httpTransportSE.call(null, envelope, headerList);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }

    public static void intent2Activity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, NoPatrolActivity.class);
        intent.putExtra("title", bundle.getString("title"));
        intent.putExtra("tag", bundle.getString("tag"));
        context.startActivity(intent);
    }

    private void initActionbar(String title) {
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

}
