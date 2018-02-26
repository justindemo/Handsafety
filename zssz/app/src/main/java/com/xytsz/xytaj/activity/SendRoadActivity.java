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
public class SendRoadActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ISSEND = 1000002;
    private static final int ISSENDPERSON = 1000003;
    private static final int ISSENDBACK = 1000004;
    private static final int ISSENDTASK = 1000005;
    private static final int NOONE = 100003;
    private static final int SENDBACK = 100006;
    private static final int SEND = 100007;

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
                    ToastUtil.shortToast(getApplicationContext(), "已下派完毕");
                    mProgressBar.setVisibility(View.GONE);
                    break;

                case ISSEND:
                    String isPass = msg.getData().getString("issend");
                    if (isPass != null) {
                        if (isPass.equals("true")) {
                            ToastUtil.shortToast(getApplicationContext(), "下派成功");
                        }
                    }
                    break;
                case ISSENDBACK:

                    String isBack = msg.getData().getString("isSendBack");
                    if (isBack != null) {
                        if (isBack.equals("true")) {
                            ToastUtil.shortToast(getApplicationContext(), "已驳回");

                            //finish();
                        }
                    }
                    break;
                case ISSENDTASK:

                    final int failPosition = msg.getData().getInt("failposition");
                    final String advice = msg.getData().getString("advice");
                    final String tasknumber = msg.getData().getString("diviceNum");


                    new Thread() {
                        @Override
                        public void run() {

                            try {
                                String isSendBack = toBack(tasknumber, GlobalContanstant.GETUNREVIEW, personId, advice);

                                Message message = Message.obtain();
                                message.what = ISSENDBACK;
                                Bundle bundle = new Bundle();
                                bundle.putInt("failposition", failPosition);
                                bundle.putString("isSendBack", isSendBack);
                                message.setData(bundle);

                                handler.sendMessage(message);

                            } catch (Exception e) {

                            }
                        }
                    }.start();
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
                            .setPriority(Notification.PRIORITY_HIGH)//高优先级
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                            .setVisibility(Notification.VISIBILITY_PRIVATE)
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
    private LinearLayout mLLbottom;
    private TimeChoiceButton mtvSendChoice;
    private TextView mtvSend;
    private TextView mtvBack;
    private String requestTime;
    private String str;
    private String isSend;
    private String isSendBack;

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

        mLLbottom = (LinearLayout) findViewById(R.id.ll_bottom);
        mtvSendChoice = (TimeChoiceButton) findViewById(R.id.tv_sendroad_choice);
        mtvSend = (TextView) findViewById(R.id.tv_send_send);
        mtvBack = (TextView) findViewById(R.id.tv_send_back);

        initData();


    }


    private String toBack(String taskNumber, int phaseIndication, int personID, String advice) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.reviewmethodName);
        soapObject.addProperty("TaskNumber", taskNumber);
        soapObject.addProperty("PhaseIndication", phaseIndication);
        soapObject.addProperty("personId", personID);
        soapObject.addProperty("RejectInfo", advice);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(null, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }

    private List<AudioUrl> audioUrls = new ArrayList<>();

    private void initData() {

        mProgressBar.setVisibility(View.VISIBLE);
        //点击的时候请求参数

        new Thread() {


            @Override
            public void run() {

                try {
                    final String reviewData = RoadActivity.getServiceData(GlobalContanstant.GETSEND);
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
                                String json = MyApplication.getAllImagUrl(taskNumber, GlobalContanstant.GETREVIEW);

                                if (json != null) {
                                    List<ImageUrl> imageUrlList = new Gson().fromJson(json, new TypeToken<List<ImageUrl>>() {
                                    }.getType());

                                    imageUrlReportLists.add(imageUrlList);

                                }

                                String audioUrljson = RoadActivity.getAudio(taskNumber);
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


                                    //是否是多选
                                    sendRoadAdapter.setOnItemLongClickListener(new SendRoadAdapter.OnItemLongClickListeners() {
                                        @Override
                                        public void OnLongclick(View v, int position) {

                                            mLLbottom.setVisibility(View.VISIBLE);
                                            //不显示底部
                                            for (Review detail : reviews) {
                                                detail.setShow(true);
                                                detail.setMultiSelect(true);
                                            }
                                            //把当前条目选中
                                            reviews.get(position).setCheck(true);
                                            sendRoadAdapter.notifyDataSetChanged();

                                        }
                                    });
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

        mtvSend.setOnClickListener(SendRoadActivity.this);
        mtvBack.setOnClickListener(SendRoadActivity.this);
    }

    private String toBack(String taskNumber, int phaseIndication, int personID, int isPersonId, String advice) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.reviewmethodName);
        soapObject.addProperty("TaskNumber", taskNumber);
        soapObject.addProperty("PhaseIndication", phaseIndication);
        soapObject.addProperty("PersonId", personID);
        soapObject.addProperty("IsPersonId", isPersonId);
        soapObject.addProperty("RejectInfo", advice);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(null, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }


    private String getAllPersonList() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getPersonList);
        soapObject.addProperty("ID",personId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(NetUrl.getPersonList_SOAP_ACTION, envelope);
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

    private String advise;
    private List<Review> list_delets = new ArrayList<>();
    private Review reviewRoadDetail;

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.tv_send_send:
                //拿到选中的checkbox的position
                mtvSendChoice.setReviewRoadDetail(null, null);

                requestTime = mtvSendChoice.getText().toString();
                list_delets.clear();
                //循环派发
                for (int i = 0; i < reviews.size(); i++) {
                    if (reviews.get(i).isCheck()) {
                        //选中的没有更新。
                        //把选中的添加上时间
                        reviewRoadDetail = reviews.get(i);
                        reviewRoadDetail.setRequestTime(requestTime);
                    }

                }

                if (reviewRoadDetail.getRequestTime().equals("要求时间")) {
                    ToastUtil.shortToast(v.getContext(), "请先选择要求时间");
                } else {
                    if (servicePerson.length != 0) {


                        AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                                .setTitle("请选择").setSingleChoiceItems(servicePerson, 0, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        str = servicePerson[which];
                                        dialog.dismiss();

                                        new AlertDialog.Builder(v.getContext()).setTitle("维修人").
                                                setMessage("确定让：" + str + " 维修?").setPositiveButton("确定",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        //添加选中的条目的人员 都是一个
                                                        TextView btn = (TextView) v;
                                                        btn.setText(str);

                                                        for (Review detail : reviews) {
                                                            if (detail.isCheck()) {
                                                                detail.setSendPerson(str);
                                                                //设置ID
                                                                for (int i = 0; i < personlist.size(); i++) {

                                                                    if (str.equals(personlist.get(i).getName())) {
                                                                        int requirementsComplete_person_id = personlist.get(i).getId();
                                                                        detail.setSendpersonID(requirementsComplete_person_id);
                                                                    }

                                                                }
                                                                //添加到删除的集合中
                                                                list_delets.add(detail);
                                                            }
                                                        }
                                                        //不能再迭代的时候remove
                                                        reviews.removeAll(list_delets);

                                                        //sendRoadAdapter.notifyDataSetChanged();
                                                        //把剩下的赋值为单选，未选中
                                                        for (Review detail : reviews) {
                                                            detail.setShow(false);
                                                            detail.setMultiSelect(false);
                                                            detail.setCheck(false);
                                                        }


                                                        //上传至服务端
                                                        new Thread() {
                                                            @Override
                                                            public void run() {
                                                                try {

                                                                    for (Review detail : list_delets) {

                                                                        isSend = sendRoadAdapter.toDispatching(detail.getDeciceCheckNum(),
                                                                                detail.getSendpersonID(),
                                                                                detail.getRequestTime(), GlobalContanstant.GETDEAL,personId,"");

                                                                    }
                                                                    //发送消息通知 删除的条目
                                                                    Message message = Message.obtain();
                                                                    message.what = SEND;
                                                                    Bundle bundle = new Bundle();
                                                                    bundle.putString("issend", isSend);
                                                                    bundle.putSerializable("deletlist", (Serializable) list_delets);
                                                                    message.setData(bundle);
                                                                    handler.sendMessage(message);


                                                                } catch (Exception e) {

                                                                }
                                                            }
                                                        }.start();
                                                        //发送消息给界面，显示通知
                                                        Message message = Message.obtain();
                                                        message.what = ISSENDPERSON;
                                                        message.obj = str;
                                                        handler.sendMessage(message);
                                                        sendRoadAdapter.notifyDataSetChanged();
                                                        dialog.dismiss();

                                                    }
                                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).create().show();


                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (Review detail : reviews) {
                                            detail.setMultiSelect(false);
                                            detail.setShow(false);
                                            detail.setCheck(false);
                                            detail.setRequestTime("要求时间");
                                        }
                                        sendRoadAdapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                    }
                                }).show();

                    } else {
                        ToastUtil.shortToast(getApplicationContext(), "人员数据未获取");
                    }
                }

                mLLbottom.setVisibility(View.GONE);
                break;
            case R.id.tv_send_back:

                list_delets.clear();
                // 将数据从list中移除

                final AlertDialog dialog = new AlertDialog.Builder(v.getContext()).create();
                View view = View.inflate(v.getContext(), R.layout.dialog_reject, null);
                final EditText etAdvice = (EditText) view.findViewById(R.id.dialog_et_advise);

                RadioGroup radiogroup = (RadioGroup) view.findViewById(R.id.back_rg);

                radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.noblong_me_rb:
                                advise = "非管护段";
                                break;
                            case R.id.noblong_rb:
                                advise = "非权属";
                                break;
                        }
                    }
                });

                Button btnOk = (Button) view.findViewById(R.id.btn_ok);
                Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
                dialog.setView(view);
                dialog.show();
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String advice = advise + " " + etAdvice.getText().toString();
                        for (int i = 0; i < reviews.size(); i++) {
                            if (reviews.get(i).isCheck()) {
                                reviews.get(i).setAdvice(advice);
                                list_delets.add(reviews.get(i));

                            }
                        }
                        reviews.removeAll(list_delets);

                        for (Review detail : reviews) {
                            detail.setCheck(false);
                            detail.setMultiSelect(false);
                            detail.setShow(false);
                        }
                        new Thread() {

                            @Override
                            public void run() {
                                try {
                                    for (Review detail : list_delets) {
                                        isSendBack = toBack(detail.getDeciceCheckNum(), GlobalContanstant.GETUNREVIEW, personId, detail.getAdvice());
                                    }
                                    Message message = Message.obtain();
                                    message.what = SENDBACK;
                                    Bundle bundle = new Bundle();
                                    bundle.putString("isSendBack", isSendBack);
                                    bundle.putSerializable("deletlist", (Serializable) list_delets);
                                    message.setData(bundle);
                                    handler.sendMessage(message);
                                } catch (Exception e) {

                                }
                            }
                        }.start();


                        sendRoadAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                        mLLbottom.setVisibility(View.GONE);
                    }

                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                break;
        }
    }
}
