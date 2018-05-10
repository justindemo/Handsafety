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

import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.PatrolListAdapter;
import com.xytsz.xytaj.bean.PatrolListBean;
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
 * Created by admin on 2018/4/26.
 * <p/>
 * 未排查
 */
public class NoCheckActivity extends AppCompatActivity {

    @Bind(R.id.nocheck_rv)
    RecyclerView nocheckRv;
    @Bind(R.id.nocheck_progressbar)
    LinearLayout nocheckProgressbar;
    private int personId;
    private List<PatrolListBean> patrolListBeens;
    private int position;
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

                        if (patrolListBeens != null) {
                            if (patrolListBeens.size() != 0) {
                                //展示
                                LinearLayoutManager manager = new LinearLayoutManager(NoCheckActivity.this);
                                nocheckRv.setLayoutManager(manager);
                                PatrolListAdapter patrolListAdapter = new PatrolListAdapter(patrolListBeens);
                                nocheckRv.setAdapter(patrolListAdapter);

                            } else {
                                ToastUtil.shortToast(getApplicationContext(), "今天排查工作完成");
                            }

                        }
                    } else {
                        ToastUtil.shortToast(getApplicationContext(), "今天排查工作完成");
                    }
                    break;
                case GlobalContanstant.PATROLLISTFAIL:
                    nocheckProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), "获取数据异常");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nocheck);
        ButterKnife.bind(this);
        initActionbar();
        initData();

    }

    private void initData() {
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

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getnoTaskByPersonID);
        soapObject.addProperty("personId", personId);

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

    private void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("未排查任务");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}
