package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.MyApplication;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.MyReportAdapter;
import com.xytsz.xytaj.bean.AudioUrl;
import com.xytsz.xytaj.bean.ForMyDis;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by admin on 2017/6/9.
 * <p>
 * wod 审核页面
 */
public class MyReviewedActivity extends AppCompatActivity {


    private static final int REPORTE = 100004;
    @Bind(R.id.lv_reprote)
    ListView lvReprote;
    @Bind(R.id.myreport_progressbar)
    ProgressBar myreportProgressbar;
    @Bind(R.id.tv_fail)
    TextView tvFail;

    private int personId;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContanstant.CHECKFAIL:
                    tvFail.setVisibility(View.VISIBLE);
                    myreportProgressbar.setVisibility(View.GONE);
                    tvFail.setText("未获取数据，请稍后");
                    break;


                case REPORTE:

                    final List<ForMyDis> details = (List<ForMyDis>) msg.obj;
                    if (details.size() != 0) {
                        MyReportAdapter adapter = new MyReportAdapter(details, imageUrlLists, audioUrls);

                        myreportProgressbar.setVisibility(View.GONE);
                        lvReprote.setAdapter(adapter);


                        lvReprote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(MyReviewedActivity.this, MyReporteDetailActivity.class);
                                intent.putExtra("detail", details.get(position));
                                intent.putExtra("audioUrl", audioUrls.get(position));
                                intent.putExtra("flag", 2);
                                intent.putExtra("imageUrlReport", (Serializable) imageUrlLists.get(position));
                                startActivity(intent);
                            }
                        });
                    } else {
                        tvFail.setVisibility(View.VISIBLE);
                        tvFail.setText(nodata);
                        myreportProgressbar.setVisibility(View.GONE);
                        ToastUtil.shortToast(getApplicationContext(), nodata);
                    }
                    break;
            }
        }
    };
    private List<List<ImageUrl>> imageUrlLists = new ArrayList<>();
    private List<AudioUrl> audioUrls = new ArrayList<>();
    private String nodata;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreporte);
        ButterKnife.bind(this);

        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        String loading = getString(R.string.loading);
        nodata = getString(R.string.myreviewed_nodata);


        ToastUtil.shortToast(getApplicationContext(), loading);
        initAcitionbar();
        initData();
    }

    private void initData() {
        myreportProgressbar.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                try {
                    String data = getData();
                    if (data != null) {
                        List<ForMyDis> details = JsonUtil.jsonToBean(data, new TypeToken<List<ForMyDis>>() {
                        }.getType());


                        for (ForMyDis forMyDis : details) {
                            String taskNumber = forMyDis.getTaskNumber();

                            String json = null;

                            json = MyApplication.getAllImagUrl(taskNumber, GlobalContanstant.GETREVIEW);

                            if (json != null) {
                                //String list = new JSONObject(json).getJSONArray("").toString();
                                List<ImageUrl> imageUrlList = new Gson().fromJson(json, new TypeToken<List<ImageUrl>>() {
                                }.getType());

                                imageUrlLists.add(imageUrlList);
                            }

                            String audioUrljson = RoadActivity.getAudio(taskNumber);
                            if (audioUrljson != null) {
                                AudioUrl audioUrl = JsonUtil.jsonToBean(audioUrljson, AudioUrl.class);
                                audioUrls.add(audioUrl);
                            }

                        }
                        Message message = Message.obtain();
                        message.obj = details;
                        message.what = REPORTE;
                        handler.sendMessage(message);

                    }
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.CHECKFAIL;
                    handler.sendMessage(message);

                }

            }
        }.start();
    }


    private String getData()throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getALlReviewByPersonID);
        soapObject.addProperty("personid", personId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);


        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(NetUrl.getALlReviewByPersonID_SOAP_ACTION, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;


    }

    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.myreviewed);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
