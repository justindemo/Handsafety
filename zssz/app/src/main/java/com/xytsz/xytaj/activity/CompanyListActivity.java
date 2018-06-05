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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.CompanyListAdapter;
import com.xytsz.xytaj.adapter.FacilityCategroyAdapter;
import com.xytsz.xytaj.bean.Company;
import com.xytsz.xytaj.bean.CompanyList;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/5/30.
 * 公司列表
 */
public class CompanyListActivity extends AppCompatActivity {

    @Bind(R.id.companylist_rv)
    RecyclerView companylistRv;
    @Bind(R.id.companylist_progressbar)
    LinearLayout companylistProgressbar;
    private String tag;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContanstant.FAIL:
                    ToastUtil.shortToast(getApplicationContext(), "数据未加载");
                    break;
                case GlobalContanstant.MYSENDSUCCESS:
                    String result = (String) msg.obj;
                    if (result != null && !result.equals("[]")) {
                        List<CompanyList> companyLists = JsonUtil.jsonToBean(result,
                                new TypeToken<List<CompanyList>>() {
                                }.getType());

                        CompanyListAdapter companyListAdapter = new CompanyListAdapter(companyLists);
                        companylistRv.setAdapter(companyListAdapter);


                    }
                    break;

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            tag = getIntent().getStringExtra("tag");
        }
        setContentView(R.layout.activity_companylist);
        ButterKnife.bind(this);
        if (tag != null) {
            switch (tag) {
                case "company":
                    title = "企业会员";
                    break;
                case "thrid":
                    title = "第三方服务";
                    break;
            }
        }
        initActionBar();
        initView();
        //initData();
    }

    private List<String> contents = new ArrayList<>();

    private void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        companylistRv.setLayoutManager(manager);
        contents.clear();
        contents.add("北京向阳天科技有限公司");
        contents.add("重庆品智家居有限公司");
        contents.add("科能文化有限公司");

        FacilityCategroyAdapter facilityCategroyAdapter = new FacilityCategroyAdapter(contents);
        companylistRv.setAdapter(facilityCategroyAdapter);

        LayoutInflater inflater = CompanyListActivity.this.getLayoutInflater();
        View headView = inflater.inflate(R.layout.pop_tv, companylistRv,false);
        facilityCategroyAdapter.addHeaderView(headView);

        facilityCategroyAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(CompanyListActivity.this, MemberCompanyShowActivity.class);
                intent.putExtra("companyName",contents.get(position));
                intent.putExtra("Id",position);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        companylistProgressbar.setVisibility(View.VISIBLE);
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
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getConpanyList);

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

    private String title;

    private void initActionBar() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
