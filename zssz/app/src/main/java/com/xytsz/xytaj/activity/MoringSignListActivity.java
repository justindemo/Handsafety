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
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.MoringSignAdapter;
import com.xytsz.xytaj.bean.MoringMeeting;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.IntentUtil;
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
 * Created by admin on 2018/4/21.
 * 签到列表
 */
public class MoringSignListActivity extends AppCompatActivity {

    @Bind(R.id.moringsign_rv)
    RecyclerView moringsignRv;
    @Bind(R.id.moringsign_progressbar)
    LinearLayout moringsignProgressbar;
    private Handler handler = new Handler() {

        private List<String> noPersonlist;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContanstant.MYSENDSUCCESS:
                    moringsignProgressbar.setVisibility(View.GONE);
                    Bundle bundle = msg.getData();
                    String meetingJson = bundle.getString("data");
                    String personData = bundle.getString("personData");
                    if (personData != null && !personData.equals("[]")) {
                        noPersonlist = JsonUtil.jsonToBean(personData, new TypeToken<List<String>>() {
                        }.getType());
                    }
                    if (meetingJson != null && !meetingJson.equals("[]")){
                        moringMeetings = JsonUtil.jsonToBean(meetingJson, new TypeToken<List<MoringMeeting>>() {
                        }.getType());

                        if (moringMeetings.size() == 0) {
                            ToastUtil.shortToast(getApplicationContext(), "签到完毕");
                        }

                        moringSignAdapter = new MoringSignAdapter(moringMeetings,MoringSignListActivity.this,noPersonlist);
                        moringsignRv.setAdapter(moringSignAdapter);

                        moringSignAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if (moringMeetings.get(position).getSumCount() == moringMeetings.get(position).getCount() ){
                                    ToastUtil.shortToast(getApplicationContext(), "签到完毕");

                                }else {
                                    Intent intent = new Intent(MoringSignListActivity.this, MoringSignActivity.class);
                                    intent.putExtra("tag", "moringsign");
                                    intent.putExtra("count", moringMeetings.get(position).getCount());
                                    intent.putExtra("singnid", moringMeetings.get(position).getID());
                                    startActivity(intent);
                                }
                            }

                        });

                    }

                    break;
                case GlobalContanstant.FAIL:
                    moringsignProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), "数据未获取");
                    break;

            }
        }
    };

    private List<MoringMeeting> moringMeetings;
    private int personId;
    private MoringSignAdapter moringSignAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moringsignlist);
        ButterKnife.bind(this);

        initActionBar();
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        moringsignRv.setLayoutManager(manager);
        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        initData();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("签到列表");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initData() {
        moringsignProgressbar.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {

                try {
                    String personData = getPersonData();
                    String data = getData();

                    if (data != null && personData != null) {
                        Message message = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("data",data);
                        bundle.putString("personData",personData);
                        message.what = GlobalContanstant.MYSENDSUCCESS;
                        message.setData(bundle);
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

    private String getPersonData()throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getnoPersonList);

        soapObject.addProperty("personId", personId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;

        return object.getProperty(0).toString();
    }

    private String getData() throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getMoringMeeting);

        soapObject.addProperty("personId", personId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;

        return object.getProperty(0).toString();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
