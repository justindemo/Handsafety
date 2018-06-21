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
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.SystemManageListAdapter;
import com.xytsz.xytaj.adapter.TraintestShowAdapter;
import com.xytsz.xytaj.bean.SytemManageList;
import com.xytsz.xytaj.bean.TrainContent;
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
 * 制度列表
 */
public class SystemManageListActivity extends AppCompatActivity {

    @Bind(R.id.systemmanagelist_rv)
    RecyclerView systemmanagelistRv;
    @Bind(R.id.systemmanagelist_progressbar)
    LinearLayout systemmanagelistProgressbar;

    private String title;
    private int tag;
    private int personId;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GlobalContanstant.FAIL:
                    systemmanagelistProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(),"数据未获取");
                    break;
                case GlobalContanstant.MYSENDSUCCESS:
                    systemmanagelistProgressbar.setVisibility(View.GONE);
                    String json = (String) msg.obj;
                    if (json != null && !json.equals("[]")){
                        manageLists = JsonUtil.jsonToBean(json, new TypeToken<List<SytemManageList>>() {
                        }.getType());

                        if (manageLists.size()!= 0){
                            LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                            systemmanagelistRv.setLayoutManager(manager);
                            SystemManageListAdapter systemManageListAdapter = new SystemManageListAdapter(manageLists);
                            systemmanagelistRv.setAdapter(systemManageListAdapter);
                            systemManageListAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.
                                    OnRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(SystemManageListActivity.this, InstitutionShowActivity.class);
                                    intent.putExtra("tag",tag);
                                    intent.putExtra("content",manageLists.get(position).getUrl());
                                    startActivity(intent);
                                }


                            });
                        }

                    }else {
                        ToastUtil.shortToast(getApplicationContext(),"制度未上传");
                    }
                    break;
            }
        }
    };
    private List<SytemManageList> manageLists;
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemmanagelist);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            tag = getIntent().getIntExtra("tag", -1);
        }
        switch (tag) {
            case 1:
                title = "粉尘防爆";
                type = 1;
                break;
            case 2:
                title = "消防安全";
                type = 2;
                break;
            case 0:
                title = "职业卫生";
                type =  3 ;
                break;
            case 3:
                title = "环境保护";
                type = 4;
                break;

        }
        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        initactionbar(title);
        initData();

    }

    private List<HeaderProperty> headerList = new ArrayList<>();

    private void initData() {

        headerList.clear();
        HeaderProperty headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie,
                SpUtils.getString(getApplicationContext(),GlobalContanstant.CookieHeader));

        headerList.add(headerPropertyObj);

        systemmanagelistProgressbar.setVisibility(View.VISIBLE);
        new Thread(){
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

    private String getData()throws Exception{
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getSystemmanagelist);
        soapObject.addProperty("personId", personId);
        soapObject.addProperty("type", type);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope,headerList);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();


        return result;
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

}
