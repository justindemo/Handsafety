package com.xytsz.xytaj.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.MyApplication;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.PostRoadAdapter;
import com.xytsz.xytaj.bean.AudioUrl;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.bean.Review;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/2/22.
 * 报验二级菜单
 */
public class PostRoadActivity extends AppCompatActivity {

    private ListView mlv;
    private int personID;
    private List<List<ImageUrl>> imageUrlLists = new ArrayList<>();
    private ProgressBar mProgressBar;
    private PostRoadAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContanstant.SENDFAIL:
                    mProgressBar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), "未数据获取,请稍后");
                    break;
            }
        }
    };
    private List<Review> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postroad);

        initAcitionbar();
        mlv = (ListView) findViewById(R.id.lv_postRoad);
        mProgressBar = (ProgressBar) findViewById(R.id.review_progressbar);

        //sp 获取当前登陆人的ID
        personID = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);

        initData();

    }

    private List<AudioUrl> audioUrls = new ArrayList<>();

    private void initData() {

        mProgressBar.setVisibility(View.VISIBLE);

        new Thread() {
            @Override
            public void run() {

                try {
                    String sendData = getDealData(NetUrl.getManagementList, personID);

                    if (sendData != null) {

                        list = JsonUtil.jsonToBean(sendData, new TypeToken<List<Review>>() {
                        }.getType());


                        audioUrls.clear();
                        //遍历list
                        for (Review detail : list) {
                            String taskNumber = detail.getDeciceCheckNum();
                            /**
                             * 获取到图片的URl
                             */
                            String json = MyApplication.getAllImagUrl(taskNumber, GlobalContanstant.GETREVIEW);

                            if (json != null) {

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
                        adapter = new PostRoadAdapter(list, imageUrlLists, audioUrls);
                        //主线程更新UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mlv.setAdapter(adapter);
                                mProgressBar.setVisibility(View.GONE);
                            }
                        });

                    }
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.SENDFAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();


    }


    public static String getDealData(String methodName, int personID) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, methodName);
        soapObject.addProperty("personId", personID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getManagementList_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String json = object.getProperty(0).toString();

        return json;
    }

    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.post);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
