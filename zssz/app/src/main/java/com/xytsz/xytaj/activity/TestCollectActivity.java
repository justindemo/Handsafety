package com.xytsz.xytaj.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.TestCollectAdapter;
import com.xytsz.xytaj.bean.TestCollect;
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
 * Created by admin on 2018/4/12.
 * <p>
 * 成绩汇总
 */
public class TestCollectActivity extends AppCompatActivity {

    @Bind(R.id.testcollect_rv)
    RecyclerView testcollectRv;
    @Bind(R.id.testcollect_progressbar)
    LinearLayout testcollectProgressbar;
    private int trainId;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GlobalContanstant.FAIL:
                    testcollectProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(),"数据未获取");
                    break;
                case GlobalContanstant.MYSENDSUCCESS:
                    testcollectProgressbar.setVisibility(View.GONE);
                    String json = (String) msg.obj;
                    if (!json.equals("[]")){
                        List<TestCollect> testCollects= JsonUtil.jsonToBean(json, new TypeToken<List<TestCollect>>() {
                        }.getType());

                        TestCollectAdapter testCollectAdapter = new TestCollectAdapter(testCollects);
                        testcollectRv.setAdapter(testCollectAdapter);
                        View headerView = inflateView(R.layout.item_testcollectheader, testcollectRv);
                        testCollectAdapter.addHeaderView(headerView);
                    }
                    break;
            }
        }
    };


    private View inflateView(int layoutId,RecyclerView rv) {
        //升级版的适配器支持添加headerView
        LayoutInflater inflater = TestCollectActivity.this.getLayoutInflater();
        //参三为false 表示 条目视图打气进来之后不添加rv.
        return inflater.inflate(layoutId,rv,false);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testcollect);
        ButterKnife.bind(this);
        if (getIntent() != null){
            trainId = getIntent().getIntExtra("trainId", -1);
        }

        initActionBar();

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        testcollectRv.setLayoutManager(manager);

        initData();


    }

    private void initData() {

        headerList.clear();
        HeaderProperty headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie,
                SpUtils.getString(getApplicationContext(),GlobalContanstant.CookieHeader));

        headerList.add(headerPropertyObj);

        new Thread(){
            @Override
            public void run() {

                try {
                    String data = getData();
                    if (data != null){
                        Message message = Message.obtain();
                        message.what = GlobalContanstant.MYSENDSUCCESS;
                        message.obj = data;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.FAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    private List<HeaderProperty> headerList = new ArrayList<>();
    private String getData()throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.testcollectmethod);
        soapObject.addProperty("TrainID", trainId);

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

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.testcollect);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
