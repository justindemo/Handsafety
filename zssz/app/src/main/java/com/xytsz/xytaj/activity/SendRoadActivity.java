package com.xytsz.xytaj.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.MyApplication;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.SendRoadAdapter;
import com.xytsz.xytaj.bean.AudioUrl;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.bean.Person;
import com.xytsz.xytaj.bean.Review;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.ui.TimeChoiceButton;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/2/23.
 * 派发二级页面
 */
public class SendRoadActivity extends AppCompatActivity {

    private static final int ISSEND = 1000002;
    private static final int ISSENDPERSON = 1000003;
    private static final int NOONE = 100003;

    private ListView mlv;
    private Bitmap largeBitmap;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContanstant.SENDFAIL:
                    mProgressBar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), "未数据获取,请稍后");
                    break;
                case NOONE:
                    ToastUtil.shortToast(getApplicationContext(), "已审批完毕");
                    mProgressBar.setVisibility(View.GONE);
                    break;

                case ISSEND:
                    String isPass = msg.getData().getString("issend");
                    if (isPass != null) {
                        if (isPass.equals("true")) {
                            ToastUtil.shortToast(getApplicationContext(), "审批成功");
                        }
                    }
                    break;


                case ISSENDPERSON:
                    String userName = (String) msg.obj;
                    largeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    NotificationManager nm = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
                    //Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Notification noti = new NotificationCompat.Builder(getApplicationContext())
                            .setTicker("任务已派发给：" + userName)
                            .setContentTitle(userName)
                            .setContentText("已收到新的派发任务")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(largeBitmap)
                            .setContentIntent(getContentIntent())
                            .setPriority(android.support.v4.app.NotificationCompat.PRIORITY_HIGH)//高优先级
                            .setDefaults(NotificationCompat.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                            .setVisibility(android.support.v4.app.NotificationCompat.VISIBILITY_PRIVATE)
                            //自动隐藏
                            .setAutoCancel(true)

                            .build();
                    //id =0 =  用来定义取消的id
                    nm.notify(1, noti);
                    break;


            }
        }
    };
    private int personId;
    private ProgressBar mProgressBar;
    private SendRoadAdapter sendRoadAdapter;
    private List<Person> personlist;
    private String[] servicePerson;
    private List<Review> reviews;

    private PendingIntent getContentIntent() {
        Intent intent = new Intent(this, DealActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        return PendingIntent.getActivity(getApplicationContext(), 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private List<List<ImageUrl>> imageUrlReportLists = new ArrayList<>();




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sendroad);

        initAcitionbar();
        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        mlv = (ListView) findViewById(R.id.lv_sendroad);
        mProgressBar = (ProgressBar) findViewById(R.id.review_progressbar);

        initData();

    }




    private List<AudioUrl> audioUrls = new ArrayList<>();
    private List<HeaderProperty> headerList = new ArrayList<>();

    private void initData() {

        headerList.clear();
        HeaderProperty headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie,
                SpUtils.getString(getApplicationContext(),GlobalContanstant.CookieHeader));

        headerList.add(headerPropertyObj);

        mProgressBar.setVisibility(View.VISIBLE);
        //点击的时候请求参数

        new Thread() {


            @Override
            public void run() {

                try {
                    String reviewData =DealActivity.getServiceData(NetUrl.getsendtask,personId);
                    String allPersonList = getAllPersonList();

                    if (reviewData != null) {
                        reviews = JsonUtil.jsonToBean(reviewData, new TypeToken<List<Review>>() {
                        }.getType());
                        if (reviews.size() == 0) {
                            Message message = Message.obtain();
                            message.what = NOONE;
                            handler.sendMessage(message);
                        } else {

                            audioUrls.clear();

                            //遍历list

                            for (Review detail : reviews) {
                                String taskNumber = detail.getDeciceCheckNum();
                                /**
                                 * 获取到图片的URl
                                 */
                                String json = RoadActivity.getAllImagUrl(taskNumber, GlobalContanstant.GETREVIEW,headerList);

                                if (json != null) {
                                    List<ImageUrl> imageUrlList = new Gson().fromJson(json, new TypeToken<List<ImageUrl>>() {
                                    }.getType());

                                    imageUrlReportLists.add(imageUrlList);

                                }

                                String audioUrljson = RoadActivity.getAudio(taskNumber,headerList);
                                if (audioUrljson != null) {
                                    AudioUrl audioUrl = JsonUtil.jsonToBean(audioUrljson, AudioUrl.class);
                                    audioUrls.add(audioUrl);
                                }


                            }

                            if (!allPersonList.equals("false")) {

                                personlist = JsonUtil.jsonToBean(allPersonList, new TypeToken<List<Person>>() {
                                }.getType());
                                //人员数量


                                servicePerson = new String[personlist.size()];
                                for (int i = 0; i < servicePerson.length; i++) {
                                    servicePerson[i] = personlist.get(i).getName();
                                }
                            }

                            sendRoadAdapter = new SendRoadAdapter(SendRoadActivity.this,handler, reviews,
                                    imageUrlReportLists, personlist, audioUrls,personId);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mlv.setAdapter(sendRoadAdapter);
                                    mProgressBar.setVisibility(View.GONE);

                                }
                            });

                        }
                    }
                } catch (Exception e) {

                    Message message = Message.obtain();
                    message.what = GlobalContanstant.SENDFAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();


    }



    private String getAllPersonList() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getPersonList);
        soapObject.addProperty("ID",personId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(NetUrl.getPersonList_SOAP_ACTION, envelope,headerList);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }


    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.send);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


}
