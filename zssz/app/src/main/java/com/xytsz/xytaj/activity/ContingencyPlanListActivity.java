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
import com.xytsz.xytaj.bean.SytemManageList;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/5/10.
 * <p/>
 * 应急预案展示列表
 */
public class ContingencyPlanListActivity extends AppCompatActivity {

    @Bind(R.id.contingencylist_rv)
    RecyclerView contingencylistRv;
    @Bind(R.id.contingencylist_progressbar)
    LinearLayout contingencylistProgressbar;
    private int tag;
    private String title;
    private int type;
    private  int personId;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GlobalContanstant.FAIL:
                    contingencylistProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(),"数据未获取");
                    break;
                case GlobalContanstant.MYSENDSUCCESS:
                    contingencylistProgressbar.setVisibility(View.GONE);
                    String json = (String) msg.obj;
                    if (json != null && !json.equals("[]")){
                        manageLists = JsonUtil.jsonToBean(json, new TypeToken<List<SytemManageList>>() {
                        }.getType());

                        if (manageLists.size()!= 0){
                            LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                            contingencylistRv.setLayoutManager(manager);
                            SystemManageListAdapter systemManageListAdapter = new SystemManageListAdapter(manageLists);
                            contingencylistRv.setAdapter(systemManageListAdapter);
                            systemManageListAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.
                                    OnRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(ContingencyPlanListActivity.this, InstitutionShowActivity.class);
                                    intent.putExtra("tag",tag);
                                    //代表展示的预案 或者演练
                                    intent.putExtra("content", manageLists.get(position).getUrl());
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
    private int tagdetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contingencyplanlist);
        ButterKnife.bind(this);

        if (getIntent() != null){
            tag = getIntent().getIntExtra("tag", -1);
            tagdetail = getIntent().getIntExtra("tagdetail", -1);
        }
        switch (tag) {
            case 1:
                title = "粉尘防爆";
                type = 2 ;
                break;
            case 2:
                title = "消防安全";
                type = 3 ;
                break;
            case 0:
                title = "职业卫生";
                type = 1;
                break;
            case 3:
                title = "环境保护";
                type = 4;
                break;
            case 4:
                title = "防洪演练";
                type =5 ;
                break;

        }

        switch (tagdetail){
            case 0:
                //应急预案
                method =NetUrl.getcontingencylist;
                break;
            case 1:
                method = NetUrl.getExerciselist;
                break;
        }
        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        initactionbar(title);
        initData();

    }

    private String method;

    private void initData() {

        contingencylistProgressbar.setVisibility(View.VISIBLE);
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
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, method);
        soapObject.addProperty("personId", personId);
        soapObject.addProperty("type", type);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope);

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
