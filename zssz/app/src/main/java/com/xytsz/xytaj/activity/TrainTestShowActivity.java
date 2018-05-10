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
import com.xytsz.xytaj.adapter.TraintestShowAdapter;
import com.xytsz.xytaj.bean.TrainContent;
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
 * Created by admin on 2018/4/12.
 * 展示培训内容
 */
public class TrainTestShowActivity extends AppCompatActivity {

    @Bind(R.id.traintestshow_rv)
    RecyclerView traintestshowRv;
    @Bind(R.id.traintestshow_progressbar)
    LinearLayout traintestshowProgressbar;
    private int tag;
    private String title;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GlobalContanstant.FAIL:
                    traintestshowProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(),"数据未获取");
                    break;
                case GlobalContanstant.MYSENDSUCCESS:
                    traintestshowProgressbar.setVisibility(View.GONE);
                    String json = (String) msg.obj;
                    if (json != null && !json.equals("[]")){
                        trainContents = JsonUtil.jsonToBean(json, new TypeToken<List<TrainContent>>() {
                        }.getType());

                        if (trainContents.size()!= 0){
                            LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                            traintestshowRv.setLayoutManager(manager);
                            TraintestShowAdapter traintestShowAdapter = new TraintestShowAdapter(trainContents);
                            traintestshowRv.setAdapter(traintestShowAdapter);
                            traintestShowAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    switch (tag){
                                        //传递哪一场培训，
                                        //传递环节
                                        //跳转到指定界面
                                        case 0:
                                            //实施方案//总结评估  内容资料，培训记录，培训通知
                                        case 1:
                                        case 2:
                                        case 3:
                                        case 4:
                                            intent2show(position, trainContents,TrainTestDetailActivity.class,tag);
                                            break;
                                        case 5:
//                                             培训签到

                                            intent2show(position, trainContents,MoringSignActivity.class,true);
                                            break;
                                        case 6:
//                                            培训照片

                                            intent2show(position, trainContents,TrainPhotoActivity.class,true);
                                            break;
                                        case 7:
//                                            培训考试
                                            //判断 考试状态
                                            intent2show(position, trainContents,TestActivity.class,false);
                                            break;
                                        case 8:
//                                            成绩汇总
                                            intent2show(position, trainContents,TestCollectActivity.class,false);
                                            break;
                                    }
                                }


                            });
                        }else {
                            ToastUtil.shortToast(getApplicationContext(),"当前没有培训");
                        }
                    }else {
                        ToastUtil.shortToast(getApplicationContext(),"当前没有培训");
                    }
                    break;
            }
        }
    };
    private int personId;
    private List<TrainContent> trainContents;

    /**
     *
     * @param position 培训场次
     * @param trainContents 培训内容
     * @param activity 培训展示
     * @param tag 培训标签（环节）
     */
    private void intent2show(int position, List<TrainContent> trainContents, Class<TrainTestDetailActivity> activity, int tag) {
        Intent intent = new Intent(TrainTestShowActivity.this,activity);
        //传递哪一场培训
        intent.putExtra("train",trainContents.get(position));
        intent.putExtra("tag",tag);
        startActivity(intent);
    }

    /**
     *
     * @param position 哪一场培训
     * @param trainContents 培训内容
     * @param activity 指定环节
     *
     */
    private void intent2show(int position, List<TrainContent> trainContents, Class<?> activity,boolean t) {
        Intent intent = new Intent(TrainTestShowActivity.this,activity);
        //传递哪一场培训
        if (t){
            intent.putExtra("tag","trainsign");
        }
        intent.putExtra("trainId",trainContents.get(position).getId());
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traintestshow);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            tag = getIntent().getIntExtra("tag", -1);
        }

        switch (tag) {
            case 0:
                title = "实施方案";
                break;
            case 1:
                title = "总结评估";
                break;
            case 2:
                title = "内容资料";
                break;
            case 3:
                title = "培训记录";
                break;
            case 4:
                title = "培训通知";
                break;
            case 5:
                title = "培训签到";
                break;
            case 6:
                title = "培训照片";
                break;
            case 7:
                title = "培训考试";
                break;
            case 8:
                title = "成绩汇总";
                break;
        }

        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        initactionbar(title);
        initData();
    }

    private void initData() {

        traintestshowProgressbar.setVisibility(View.VISIBLE);
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
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.trainTestshowmethod);
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


